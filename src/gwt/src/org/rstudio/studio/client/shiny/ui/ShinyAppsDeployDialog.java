/*
 * ShinyAppsDeployDialog.java
 *
 * Copyright (C) 2009-14 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.shiny.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rstudio.core.client.widget.ProgressIndicator;
import org.rstudio.core.client.widget.ThemedButton;
import org.rstudio.studio.client.application.events.EventBus;
import org.rstudio.studio.client.common.GlobalDisplay;
import org.rstudio.studio.client.common.shiny.model.ShinyAppsServerOperations;
import org.rstudio.studio.client.server.ServerError;
import org.rstudio.studio.client.server.ServerRequestCallback;
import org.rstudio.studio.client.shiny.events.ShinyAppsDeployInitiatedEvent;
import org.rstudio.studio.client.shiny.model.ShinyAppsApplicationInfo;
import org.rstudio.studio.client.shiny.model.ShinyAppsDeploymentRecord;
import org.rstudio.studio.client.workbench.views.console.events.SendToConsoleEvent;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.PopupPanel;

public class ShinyAppsDeployDialog 
             extends ShinyAppsDialog<ShinyAppsDeploy>
{
   public ShinyAppsDeployDialog(ShinyAppsServerOperations server, 
                                final GlobalDisplay display, 
                                EventBus events,
                                String sourceDir, 
                                final String lastAccount, 
                                String lastAppName)
                                
   {
      super(server, display, new ShinyAppsDeploy());
      setText("Deploy to ShinyApps");
      setWidth("350px");
      deployButton_ = new ThemedButton("Deploy");
      addCancelButton();
      addOkButton(deployButton_);
      sourceDir_ = sourceDir;
      events_ = events;
      lastAppName_ = lastAppName;

      launchCheck_ = new CheckBox("Launch browser");
      launchCheck_.setValue(true);
      launchCheck_.setStyleName(contents_.getStyle().launchCheck());
      addLeftWidget(launchCheck_);
      
      contents_.setSourceDir(sourceDir);
      
      deployButton_.addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            onDeploy();
         }
      });
      
      indicator_ = addProgressIndicator(false);

      // Get the deployments of this directory from any account (should be fast,
      // since this information is stored locally in the directory). 
      server_.getShinyAppsDeployments(sourceDir, 
            new ServerRequestCallback<JsArray<ShinyAppsDeploymentRecord>>()
      {
         @Override
         public void onResponseReceived(
               JsArray<ShinyAppsDeploymentRecord> records)
         {
            processDeploymentRecords(records);
         }

         @Override
         public void onError(ServerError error)
         {
            // If an error occurs we won't have any local deployment records,
            // but the user can still create new deployments.
         }
      });
      
      server_.getShinyAppsAccountList(new ServerRequestCallback<JsArrayString>()
      {
         @Override
         public void onResponseReceived(JsArrayString accounts)
         {
            if (accounts.length() == 0)
            {
               // The user has no accounts connected--hide ourselves and 
               // ask the user to connect an account before we continue.
               hide();
               ShinyAppsConnectAccountDialog dialog = 
                     new ShinyAppsConnectAccountDialog(server_, display_);
               dialog.addCloseHandler(new CloseHandler<PopupPanel>()
               {
                  @Override
                  public void onClose(CloseEvent<PopupPanel> event)
                  {
                     onConnectAccountFinished();
                  }
               });
               dialog.showModal();
            }
            else
            {
               // pre-select the last account used to deploy this app, or 
               // the first account if we don't have any deployment records
               String initialAccount = lastAccount == null ? 
                                          accounts.get(0) : lastAccount;
               contents_.setAccountList(accounts, initialAccount);
               updateApplicationList();
            }
         }
         
         @Override
         public void onError(ServerError error)
         {
            display_.showErrorMessage("Error retrieving ShinyApps accounts", 
                                     error.getMessage());
            closeDialog();
         }
      });
      
      // Update the list of applications when the account is changed
      contents_.addAccountChangeHandler(new ChangeHandler()
      {
         @Override
         public void onChange(ChangeEvent event)
         {
            updateApplicationList();
         }
      });
      
      // Update app info when the application is changed
      contents_.addAppChangeHandler(new ChangeHandler()
      {
         @Override
         public void onChange(ChangeEvent event)
         {
            updateApplicationInfo();
         }
      });
      
      contents_.setOnDeployDisabled(new Command()
      {
         @Override
         public void execute()
         {
            deployButton_.setEnabled(false);
         }
      });

      contents_.setOnDeployEnabled(new Command()
      {
         @Override
         public void execute()
         {
            deployButton_.setEnabled(true);
         }
      });
   }
   
   // Runs when the selected application changes; shows the cached information
   // (URL and status) for the selected application
   private void updateApplicationInfo()
   {
      String appName = contents_.getSelectedApp();
      if (appName == "Create New")
      {
         contents_.showAppInfo(null);
      }
      else if (apps_.containsKey(contents_.getSelectedAccount()))
      {
         JsArray<ShinyAppsApplicationInfo> apps =
               apps_.get(contents_.getSelectedAccount());
         for (int i = 0; i < apps.length(); i++)
         {
            if (apps.get(i).getName().equals(appName))
            {
               contents_.showAppInfo(apps.get(i));
            }
         }
      }
   }
   
   private void updateApplicationList()
   {
      final String accountName = contents_.getSelectedAccount();
      if (accountName == null)
         return;

      // Check to see if the app list is already in our cache
      if (apps_.containsKey(accountName))
      {
         setAppList(apps_.get(accountName));
         return;
      }
      
      // This operation hits the ShinyApps service, so show some progress if 
      // it takes more than a few ms
      final Timer t = new Timer() {
         @Override
         public void run()
         {
            indicator_.onProgress("Contacting ShinyApps...");
         }
      };
      t.schedule(500);

      // Not already in our cache, fetch it and populate the cache
      server_.getShinyAppsAppList(accountName,
            new ServerRequestCallback<JsArray<ShinyAppsApplicationInfo>>()
      {
         @Override
         public void onResponseReceived(
               JsArray<ShinyAppsApplicationInfo> apps)
         {

            t.cancel();
            indicator_.onCompleted();
            apps_.put(accountName, apps);
            setAppList(apps);
         }

         @Override
         public void onError(ServerError error)
         {
            t.cancel();
            indicator_.onCompleted();
            // we can always create a new app
            contents_.setAppList(null, null);
         }
      });
   }
   
   private void setAppList(JsArray<ShinyAppsApplicationInfo> apps)
   {
      ArrayList<String> appNames = new ArrayList<String>();
      for (int i = 0; i < apps.length(); i++)
      {
         ShinyAppsApplicationInfo appInfo = apps.get(i);
         // Filter the app list by URLs deployed from this directory 
         // specifically
         if (deployments_.containsKey(appInfo.getUrl()))
         {
            appNames.add(apps.get(i).getName());
         }
      }
      contents_.setAppList(appNames, lastAppName_);
      updateApplicationInfo();
   }
   
   // Runs when we've finished doing a just-in-time account connection
   private void onConnectAccountFinished()
   {
      server_.getShinyAppsAccountList(new ServerRequestCallback<JsArrayString>()
      {
         @Override
         public void onResponseReceived(JsArrayString accounts)
         {
            if (accounts.length() == 0)
            {
               // The user didn't successfully connect an account--just close 
               // ourselves
               closeDialog();
            }
            else
            {
               // We have an account, show it and re-display ourselves
               contents_.setAccountList(accounts, accounts.get(0));
               updateApplicationList();
               showModal();
            }
         }

         @Override
         public void onError(ServerError error)
         {
            display_.showErrorMessage("Error retrieving ShinyApps accounts", 
                                     error.getMessage());
            closeDialog();
         }
      });
   }
   
   private void onDeploy()
   {
      String appName = contents_.getSelectedApp();
      if (appName == null || appName == "Create New")
         appName = contents_.getNewAppName();
      
      String account = contents_.getSelectedAccount();
      String launch = launchCheck_.getValue() ? "TRUE" : "FALSE";
      
      // send the deployment command to the console
      String cmd = "shinyapps::deployApp(appDir=\"" + sourceDir_ + "\", " + 
                   "account=\"" + account + "\", " + 
                   "appName=\"" + appName + "\", " + 
                   "launch.browser=" + launch + ")";
      
      events_.fireEvent(new SendToConsoleEvent(cmd, true));
      
      // let everyone know a deployment has started (this triggers the 
      // deployment record to be cached for this directory, so we can 
      // issue an identical deployment next time)
      events_.fireEvent(new ShinyAppsDeployInitiatedEvent(
            sourceDir_,
            ShinyAppsDeploymentRecord.create(appName, account, "")));

      closeDialog();
   }
   
   // Create a lookup from app URL to deployments made of this directory
   // to that URL
   private void processDeploymentRecords(
         JsArray<ShinyAppsDeploymentRecord> records)
   {
      for (int i = 0; i < records.length(); i++)
      {
         ShinyAppsDeploymentRecord record = records.get(i);
         deployments_.put(record.getUrl(), record);
      }
   }
   
   private final EventBus events_;
   
   private String sourceDir_;
   private String lastAppName_;
   private ThemedButton deployButton_;
   private ProgressIndicator indicator_;
   private CheckBox launchCheck_;
   
   // Map of account name to a list of applications owned by that account
   private Map<String, JsArray<ShinyAppsApplicationInfo>> apps_ = 
         new HashMap<String, JsArray<ShinyAppsApplicationInfo>>();
   
   // Map of app URL to the deployment made to that URL
   private Map<String, ShinyAppsDeploymentRecord> deployments_ = 
         new HashMap<String, ShinyAppsDeploymentRecord>();
}