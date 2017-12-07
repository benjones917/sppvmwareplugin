/* Copyright (c) 2012-2017 VMware, Inc. All rights reserved. */
// Use JQuery's $(document).ready to execute the script when the document is loaded.
// All variables and functions are also hidden from the global scope.

$(document).ready(function() {

   // Get current object and return if document is loaded before context is set
   var objectId = WEB_PLATFORM.getObjectId();
   //console.log(WEB_PLATFORM.getRootPath());
   if (!objectId) {
      return;
   }
   var locale = WEB_PLATFORM.getLocale().replace("_","-");
   
   var test = getUniqueID(objectId, "VirtualMachine");

   // REST url to retrieve a list of properties
   /*var dataUrl = PluginUtil.buildDataUrl(objectId, ["samples:vmData"]);

   // Refresh the view and register same function as GlobalRefresh handler so that
   // the view is also refreshed when the user hits the toolbar's Refresh button
   refreshData();
   WEB_PLATFORM.setGlobalRefreshHandler(refreshData, document);

   function refreshData() {
      // JQuery call to the DataAccessController in the Java plugin
      $.getJSON(dataUrl, function (data) {
         // The "sample:vmData" property is an object containing the VmData field
         var vmData = data["samples:vmData"];

         // Add a timestamp just to show when content is refreshed
         var currentTime = new Date().toLocaleTimeString(locale);
         var currentDate = new Date().toLocaleDateString(locale);
         var updatedInfo = PluginUtil.getString("updatedInfo", [currentTime, currentDate]);

         $("#sppInfo").html(
            "<p><b>Virtual Machine Name: </b>" + escape(vmData.vmName) +
            "<br/><b>Data Center Name: </b>" + escape(vmData.datacenterName) +
            "<br/><b>Virtual CPUs: </b>" + vmData.numberOfVirtualCpus +
            "<br/><b>Disk Capacity: </b>" + vmData.capacityInKb  +
            "<p><i>" + updatedInfo + "</i></p>");
      });
   }*/
});

// It is recommended to escape user-entered names to avoid persistent XSS.
function escape(string) {
   return $("<div>").text(string).html();
}

function getUniqueID(objectId, type){
	console.log(objectId);
	arrObj = objectId.split(":");
	console.log(arrObj);
	if(arrObj.indexOf(type) >= 0) {
		uniqueId = arrObj[arrObj.indexOf(type)+1];
		console.log(uniqueId);
	}
}
