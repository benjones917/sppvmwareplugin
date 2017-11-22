/* Copyright (c) 2012-2017 VMware, Inc. All rights reserved. */
// ------------------------------------------------------------------------------
// Javascript initialization required for using the HTML Client SDK API.
// This also creates the plugin's private namespace com_ibm_spp
//
//                       DO NOT MAKE CHANGES HERE!
// ------------------------------------------------------------------------------

// WEB_PLATFORM is the VMware Web Client platform reference:
// - When the plugin runs in the HTML client WEB_PLATFORM is a real JS object
// - When it runs in the Flex client WEB_PLATFORM is defined as the Flash container

var WEB_PLATFORM = self.parent.WEB_PLATFORM;
var isChromeBrowser = (window.navigator.userAgent.indexOf("Chrome/") >= 0);
var isFlexClient = !!self.parent.document.getElementById("container_app");

if (isFlexClient) {
   if (!WEB_PLATFORM || isChromeBrowser) {
      WEB_PLATFORM = self.parent.document.getElementById("container_app");
      if (isChromeBrowser) {
         // Object.create is required to support Chrome version >= 55 on Flex client
         WEB_PLATFORM = Object.create(WEB_PLATFORM);
      }
      self.parent.WEB_PLATFORM = WEB_PLATFORM;
   }

   WEB_PLATFORM.getRootPath = function() {
      return "/vsphere-client";
   };

   WEB_PLATFORM.getClientType = function() {
      return "flex";
   };

   (function() {
      var version = "6.0";
      try {
         version = WEB_PLATFORM.getClientVersion();
      } catch (ex) { }
      WEB_PLATFORM.getClientVersion = function() {
         return version;
      };
   }());
}

// Define a private namespace using the plugin bundle name,
// It should be the only global symbol added by this plugin!
var com_ibm_spp;
if (!com_ibm_spp) {
   com_ibm_spp = {};

   // The web context path to use for server requests, compatible with Flex and HTML clients
   com_ibm_spp.webContextPath = WEB_PLATFORM.getRootPath() + "/spp";

   // The API setup is done inside an anonymous function to keep things clean.
   // See the HTML SDK documentation for more info on those APIs.
   (function () {
      // Namespace shortcut
      var ns = com_ibm_spp;

      // ------------------------ Private functions -------------------------------

      // Get a string from the resource bundle defined in plugin.xml
      function getString(key, params) {
         return WEB_PLATFORM.getString("com_ibm_spp", key, params);
      }

      // Get a parameter value from the current document URL
      function getURLParameter(name) {
         // Use location.href because location.search may be null with some frameworks
         return (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)')
               .exec(location.href) || [,""])[1].replace(/\+/g, '%20') || null;
      }

      // Build the REST url prefix to retrieve a list of properties,
      // this is mapped to the DataAccessController on the java side.
      function buildDataUrl(objectId, propList) {
         var propStr = propList.toString();
         var dataUrl = ns.webContextPath +
               "/rest/data/properties/" + objectId + "?properties=" + propStr;
         return dataUrl;
      }

      // -------------------------- Public APIs --------------------------------

      // Functions exported to the com_ibm_spp namespace
      ns.getString = getString;
      ns.buildDataUrl = buildDataUrl;

      // Get the current context object id or return null if none is defined
      WEB_PLATFORM.getObjectId = function() {
         return getURLParameter("objectId");
      };
      // Get the current action Uid or return null if none is defined
      WEB_PLATFORM.getActionUid = function() {
         return getURLParameter("actionUid");
      };
      // Get the comma-separated list of object ids for an action, or null for a global action
      WEB_PLATFORM.getActionTargets = function() {
         return getURLParameter("targets");
      };
      // Get the current locale
      WEB_PLATFORM.getLocale = function() {
         return getURLParameter("locale");
      };

      // Get the info provided in a global view using a vCenter selector
      WEB_PLATFORM.getVcSelectorInfo = function() {
         var info = {serviceGuid: getURLParameter("serviceGuid"),
                     sessionId: getURLParameter("sessionId"),
                     serviceUrl: getURLParameter("serviceUrl")};
         return info;
      };

      // Set a refresh handler called when the user hits Refresh in the WebClient top toolbar.
      // This is the Flex Client implementation which doesn't need document as 2nd argument.
      // This is also made compatible with 6.5.0 GA H5 client
      if (WEB_PLATFORM.getClientType() === "flex" || WEB_PLATFORM.getClientVersion() === "6.5.0") {
         WEB_PLATFORM.setGlobalRefreshHandler = function (handler) {
            WEB_PLATFORM["refresh" + window.name] = handler;
         };
      }

   })();
} // end of if (!com_ibm_spp)
