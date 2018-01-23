$(document).ready(function() {

	// Set variables
	var selectedName;
	var selectedOptions;
	var selectedType;
	var selectedIdType;
	var selectedId;
	var params = {};
	var versionParams = {};
	
	// Get current objectID
	var objectId = WEB_PLATFORM.getObjectId();
	//console.log(WEB_PLATFORM.getRootPath());
	if (!objectId) {
		return;
	}

	// Set Asynchronous to false to allow all requests to complete before moving to next step
	$.ajaxSetup({
	    async: false
	});
	
	// Get the VM name using the VMware object ID
	var dataUrl = '/ui/data/properties/' + objectId + '?properties=name,childType';
	$.getJSON(dataUrl, function (data) {
        // data object contains the properties listed above
		if(data.childType != null) {
			selectedType = 'folder';
			selectedIdType = 'groupid';
			selectedId = getUniqueID(objectId,'Folder');
	    }else{
	    	selectedType = 'vm';
	    	selectedIdType = 'vmid';
	    	selectedId = getUniqueID(objectId,'VirtualMachine');
	    }
		
		selectedName = data.name;
		params[selectedIdType] = selectedId;
		params[selectedType] = selectedName;
    });
	if(selectedId == null){
		return;
	}
	// Get the VM data from SPP
	var $vmreq = $.get(PluginUtil.getWebContextPath()
			+ "/rest/spp/" + selectedType, params, function(data) {
			selectedOptions = JSON.parse(data);
	});

	versionParams['hvid'] = selectedOptions.hypervisorManagementServerID;
	versionParams[selectedIdType] = selectedOptions.id;
	
	var restorePoints;
	var $req = $.getJSON(PluginUtil.getWebContextPath()
			+ "/rest/spp/vmversions", versionParams, function(data) {
		restorePoints = data;
	})
	var table = document.getElementById("restoreTable");
	if(restorePoints != null){
		//$("#hiddenLatestRestoreId").val(restorePoints[0].id);
		for(i=0;i<restorePoints.length;i++){
			//var row = document.createElement("tr");
			//var selectCell = document.createElement("td");
			//console.log(restorePoints[i].protectionInfo['policyName']);

			var row = table.insertRow(-1);
			//var selectCell = row.insertCell(-1);
			//var radio = document.createElement("input");
			//radio.type = "radio";
			//radio.name = "restorePoint";
			//radio.value = restorePoints[i].id;
			//selectCell.appendChild(radio);
			var dateCell = document.createElement("td");
			dateCell.innerHTML = convertTimestamp(restorePoints[i].protectionTime);
			var policyCell = document.createElement("td");
			var policyText = document.createTextNode(restorePoints[i].protectionInfo['storageProfileName']);
			policyCell.appendChild(policyText);
			//row.appendChild(selectCell);
			row.appendChild(dateCell);
			row.appendChild(policyCell);
			$("#restorePoints").append(row);
		}
	}else{
		var row = table.insertRow(-1);
		var emptyCell = row.insertCell(-1);
		var emptyText = document.createTextNode("There are no restore points for this VM");
		emptyCell.colSpan = 3;
		emptyCell.append(emptyText);
		row.append(emptyCell);
		$("#restorePoints").append(row);
	}
});

function getUniqueID(objectId, type){
	var uniqueId;
	arrObj = objectId.split(":");
	if(arrObj.indexOf(type) >= 0) {
		uniqueId = arrObj[arrObj.indexOf(type)+1];
	}
	return uniqueId;
}
// It is recommended to escape user-entered names to avoid persistent XSS.
function escape(string) {
   return $("<div>").text(string).html();
}

function convertTimestamp(unixTimestamp){
	var d = new Date(parseInt(unixTimestamp));
	var options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true };
	return d.toLocaleString('en-US',options);
}