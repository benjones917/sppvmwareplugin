$(document).ready(function() {
	console.log('Restore');
	// Set variables
	var selectedName;
	var selectedOptions;
	var selectedType;
	var selectedIdType;
	var selectedId;
	var params = {};
	var versionParams = {};
	
	// Get current objectID
	var targets = WEB_PLATFORM.getActionTargets();
	if (!targets) {
		return;
	}

	// Set Asynchronous to false to allow all requests to complete before moving to next step
	$.ajaxSetup({
	    async: false
	});
	
	// Get the VM name using the VMware object ID
	var dataUrl = '/ui/data/properties/' + targets + '?properties=name,childType';
	$.getJSON(dataUrl, function (data) {
        // data object contains the properties listed above
		if(data.childType != null) {
			selectedType = 'folder';
			selectedIdType = 'groupid';
			selectedId = getUniqueID(targets,'Folder');
	    }else{
	    	selectedType = 'vm';
	    	selectedIdType = 'vmid';
	    	selectedId = getUniqueID(targets,'VirtualMachine');
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
	$("#objectNameText").append(document.createTextNode(selectedName));
	if(selectedType == 'vm'){
		$("#objectTypeText").append("virtual machine");
	}else{
		$("#objectTypeText").append("folder");
	}
	versionParams['hvid'] = selectedOptions.hypervisorManagementServerID;
	versionParams[selectedIdType] = selectedOptions.id;
	
	var restorePoints;
	var $req = $.getJSON(PluginUtil.getWebContextPath()
			+ "/rest/spp/vmversions", versionParams, function(data) {
		restorePoints = data;
	})
	var table = document.getElementById("restoreTable");
	$("#hiddenLatestRestoreId").val(restorePoints[0].uuid);
	for(i=0;i<restorePoints.length;i++){
		var row = table.insertRow(-1);
		var selectCell = row.insertCell(-1);
		var radio = document.createElement("input");
		radio.type = "radio";
		radio.name = "restorePoint";
		radio.value = restorePoints[i].uuid;
		selectCell.appendChild(radio);
		var dateCell = row.insertCell(-1);
		dateCell.innerHTML = convertTimestamp(restorePoints[i].protectionTime);
		var policyCell = row.insertCell(-1);
		var policyText = document.createTextNode(restorePoints[i].storageProfiles[0]);
		policyCell.appendChild(policyText);
		row.appendChild(selectCell);
		row.appendChild(dateCell);
		row.appendChild(policyCell);
		$("#restorePoints").append(row);
	}
	
	$("#restoreObject").submit(function() {
		var restoreUrl;
		if(!$("input[name='restoreTypes']", this).is(':checked') || !$("input[name='restorePoint']", this).is(':checked')){
			alert("Please select restore type/point");
			return false;
		}
		/* Changed selectedType once the endpoint has been created to restore different types */
		//var selectedType = $( "input[name='restoreTypes']:checked").val();
		var selectedType = 'test'
		var selectedPoint = $( "input[name='restorePoint']:checked").val();
		var latestPoint = $("#hiddenLatestRestoreId").val();

		if(selectedPoint == latestPoint){
			restoreURL = PluginUtil.getWebContextPath() + "/rest/spp/restore/latest/" + selectedType;
		}else{
			// Change restoreURL for endpoints when selecting different restore points
			restoreURL = PluginUtil.getWebContextPath() + "/rest/spp/restore/latest/" + selectedType;
		}
		var $req = $.post(restoreURL, params, function(data) {
					console.log(data);
		})
		WEB_PLATFORM.closeDialog();
		return false;
	});
});

function getUniqueID(objectId, type){
	var uniqueId;
	arrObj = objectId.split(":");
	if(arrObj.indexOf(type) >= 0) {
		uniqueId = arrObj[arrObj.indexOf(type)+1];
	}
	return uniqueId;
}

function convertTimestamp(unixTimestamp){
	var d = new Date(parseInt(unixTimestamp));
	var options = { year: 'numeric', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true };
	return d.toLocaleString('en-US',options);
}