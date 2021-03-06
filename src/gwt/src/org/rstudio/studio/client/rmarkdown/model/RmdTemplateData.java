/*
 * RmdTemplateData.java
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
package org.rstudio.studio.client.rmarkdown.model;

import com.google.gwt.core.client.JsArray;

public class RmdTemplateData
{
   public static native JsArray<RmdTemplate> getTemplates() /*-{
      return [ 
         {
         template_name: "Document", 
         template_formats: [ 
            {
            format_name: "html_document",
            format_ui_name: "HTML",
            format_options: [ "theme", "highlight", "fig_width", 
                              "fig_height", "fig_caption", "toc", "smart", 
                              "self_contained", "css" ],
            format_notes: "Recommended format for authoring (you can switch to PDF or Word output anytime)."
            },
            {
            format_name: "pdf_document", 
            format_ui_name: "PDF",
            format_options: [ "highlight", 
                              "latex_engine", "fig_crop", "fig_width", 
                              "fig_height" ,"fig_caption",
                              "toc", "number_sections" ],
            format_notes: "PDF output requires an installation of TeX"
            },
            {
            format_name: "word_document", 
            format_ui_name: "Word",
            format_options: [ "highlight", "fig_width", "fig_height", 
                              "fig_caption" ]
            } 
         ],
         template_options: [ 
            {
            option_name: "toc",
            option_ui_name: "Include table of contents", 
            option_type: "boolean", 
            option_for_create: true,
            option_transferable: true,
            option_default: "false"
            },
            {
            option_name: "self_contained",
            option_ui_name: "Create a standalone HTML document", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "theme",
            option_ui_name: "Apply theme", 
            option_type: "choice", 
            option_default: "default",
            option_for_create: true,
            option_nullable: true,
            option_list: [ "default", "cerulean", "journal", "flatly",
                           "readable", "spacelab", "united", "yeti", "cosmo"]
            },
            {
            option_name: "highlight",
            option_ui_name: "Syntax highlighting", 
            option_type: "choice", 
            option_for_create: false,
            option_nullable: true,
            option_default: "default",
            option_list: [ "default", "tango", "pygments", "kate", "monochrome",
                           "espresso", "zenburn", "haddock"]
            },
            {
            option_name: "highlight",
            option_ui_name: "Syntax highlighting", 
            option_format: "html_document",
            option_type: "choice", 
            option_for_create: false,
            option_nullable: true,
            option_default: "default",
            option_list: [ "default", "tango", "pygments", "kate", "monochrome",
                           "espresso", "zenburn", "haddock", "textmate" ]
            },
            {
            option_name: "smart",
            option_ui_name: "Use smart punctuation", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "number_sections",
            option_ui_name: "Number section headings", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "false"
            },
            {
            option_name: "latex_engine",
            option_ui_name: "LaTeX Engine", 
            option_type: "choice", 
            option_for_create: false,
            option_default: "pdflatex",
            option_list: [ "pdflatex", "lualatex", "xelatex" ]
            },
            {
            option_name: "fig_width",
            option_format: "html_document",
            option_ui_name: "Default figure width in inches", 
            option_type: "float", 
            option_for_create: true,
            option_default: "7"
            },
            {
            option_name: "fig_height",
            option_format: "html_document",
            option_ui_name: "Default figure height in inches", 
            option_type: "float", 
            option_for_create: true,
            option_default: "5"
            },
            {
            option_name: "fig_width",
            option_ui_name: "Default figure width in inches", 
            option_type: "float", 
            option_for_create: true,
            option_default: "6"
            },
            {
            option_name: "fig_height",
            option_ui_name: "Default figure height in inches", 
            option_type: "float", 
            option_for_create: true,
            option_default: "4.5"
            },
            {
            option_name: "fig_crop",
            option_ui_name: "Crop figures with pdfcrop (if available)", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "fig_caption",
            option_ui_name: "Render figures with captions", 
            option_for_create: false,
            option_type: "boolean", 
            option_default: "true"
            },
            {
            option_name: "fig_caption",
            option_format: "html_document",
            option_ui_name: "Render figures with captions", 
            option_for_create: false,
            option_type: "boolean", 
            option_default: "false"
            },
            {
            option_name: "css",
            option_ui_name: "Apply CSS file", 
            option_type: "file", 
            option_for_create: false,
            option_nullable: true,
            option_default: "null"
            },
         ]
         },
         {
         template_name: "Presentation",
         template_formats: [
            {
            format_name: "ioslides_presentation",
            format_ui_name: "HTML (ioslides)",
            format_options: [ "logo", "fig_width", "fig_height", "fig_caption", 
                              "incremental", "transition", "smaller",
                              "widescreen",
                              "highlight", "self_contained", "smart" ],
            format_notes: "Recommended format for HTML presentations (you can also print ioslides to PDF with Chrome)."
            },
            {
            format_name: "revealjs_presentation",
            format_ui_name: "HTML (reveal.js)",
            format_options: ["center", "incremental", "theme", "transition", 
                             "fig_width", "fig_height", "fig_caption",
                             "highlight", "self_contained", "smart" ]
            },
            {
            format_name: "beamer_presentation",
            format_ui_name: "PDF (Beamer)",
            format_options: [ "incremental", "theme", "colortheme", 
                              "fonttheme", "fig_width", "fig_height", 
                              "fig_crop", "fig_caption", "toc", "highlight" ],
            format_notes: "PDF output requires an installation of TeX"
            }
         ],
         template_options: [
            {
            option_name: "center",
            option_ui_name: "Vertically center content on slides", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "false"
            },
            {
            option_name: "incremental",
            option_ui_name: "Render slide bullets incrementally", 
            option_type: "boolean", 
            option_for_create: false,
            option_transferable: true,
            option_default: "false"
            },
            {
            option_name: "self_contained",
            option_ui_name: "Create a standalone HTML presentation", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "theme",
            option_ui_name: "Theme", 
            option_type: "choice", 
            option_for_create: true,
            option_default: "default",
            option_list: [ "default", "simple", "sky", "beige", "serif", 
                           "solarized" ]
            },
            {
            option_name: "transition",
            option_ui_name: "Slide transition", 
            option_type: "choice", 
            option_for_create: false,
            option_default: "default",
            option_list: [ "default", "cube", "page", "concave", "zoom", 
                           "linear", "fade", "none" ]
            },
            {
            option_name: "transition",
            option_ui_name: "Slide transition speed", 
            option_format: "ioslides_presentation", 
            option_type: "choice", 
            option_for_create: false,
            option_default: "default",
            option_list: [ "default", "slower", "faster" ]
            },
            {
            option_name: "smart",
            option_ui_name: "Use smart punctuation", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "widescreen",
            option_ui_name: "Use widescreen dimensions", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "false"
            },
            {
            option_name: "smaller",
            option_ui_name: "Use smaller text on all slides", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "false"
            },
            {
            option_name: "fig_caption",
            option_ui_name: "Render figures with captions", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "false"
            },
            {
            option_name: "fig_width",
            option_ui_name: "Default figure width (in inches)", 
            option_type: "float", 
            option_for_create: true,
            option_default: "7.5"
            },
            {
            option_name: "fig_height",
            option_ui_name: "Default figure width (in inches)", 
            option_type: "float", 
            option_for_create: true,
            option_default: "4.5"
            },
            {
            option_name: "fig_width",
            option_format: "revealjs_presentation",
            option_ui_name: "Default figure width (in inches)", 
            option_for_create: true,
            option_type: "float", 
            option_default: "8"
            },
            {
            option_name: "fig_height",
            option_format: "revealjs_presentation",
            option_ui_name: "Default figure width (in inches)", 
            option_for_create: true,
            option_type: "float", 
            option_default: "6"
            },
            {
            option_name: "fig_width",
            option_format: "beamer_presentation",
            option_ui_name: "Default figure width (in inches)", 
            option_for_create: true,
            option_type: "float", 
            option_default: "10"
            },
            {
            option_name: "fig_height",
            option_format: "beamer_presentation",
            option_ui_name: "Default figure width (in inches)", 
            option_for_create: true,
            option_type: "float", 
            option_default: "7"
            },
            {
            option_name: "highlight",
            option_ui_name: "Syntax highlighting", 
            option_type: "choice", 
            option_for_create: false,
            option_transferable: true,
            option_default: "default",
            option_list: [ "default", "tango", "pygments", "kate", "monochrome",
                           "espresso", "zenburn", "haddock"]
            },
            {
            option_name: "toc",
            option_ui_name: "Include a table of contents", 
            option_type: "boolean", 
            option_for_create: true,
            option_default: "false"
            },
            {
            option_name: "fig_crop",
            option_ui_name: "Crop figures with pdfcrop (if available)", 
            option_type: "boolean", 
            option_for_create: false,
            option_default: "true"
            },
            {
            option_name: "theme",
            option_format: "beamer_presentation",
            option_ui_name: "Theme", 
            option_type: "choice", 
            option_for_create: true,
            option_default: "default",
            option_list: [ "AnnArbor", "Antibes", "Bergen", "Berkeley",
                           "Berlin", "Boadilla", "boxes", "CambridgeUS", 
                           "Copenhagen", "Darmstadt", "default", "Dresden", 
                           "Frankfurt", "Goettingen", "Hannover", "Ilmenau", 
                           "JuanLesPins", "Luebeck", "Madrid", "Malmoe", 
                           "Marburg", "Montpellier", "PaloAlto", "Pittsburgh", 
                           "Rochester", "Singapore", "Szeged", "Warsaw" ]
            },
            {
            option_name: "fonttheme",
            option_ui_name: "Font theme", 
            option_type: "choice", 
            option_for_create: false,
            option_default: "default",
            option_list: [ "default", "professionalfonts", "serif", 
                           "structurebold", "structureitalicserif", 
                           "structuresmallcapsserif" ]
            },
            {
            option_name: "colortheme",
            option_ui_name: "Color theme", 
            option_type: "choice", 
            option_for_create: true,
            option_default: "default",
            option_list: [ "albatross", "beaver", "beetle", "crane", "default", 
                           "dolphin", "dove", "fly", "lily", "orchid", "rose", 
                           "seagull", "seahorse", "sidebartab", "structure", 
                           "whale", "wolverine" ]
            },
            {
            option_name: "logo",
            option_ui_name: "Show logo (square, at least 128x128)", 
            option_type: "file", 
            option_for_create: true,
            option_nullable: true,
            option_default: "null"
            },
         ]
         }
   ];
   }-*/;
   
   public final static String DOCUMENT_TEMPLATE = "Document";
   public final static String PRESENTATION_TEMPLATE = "Presentation";
}
