$(document).ready(function() {
	// Set variables
	var selectedName;
	var selectedOptions;
	var selectedType;
	var selectedIdType;
	var selectedId;
	var params = {};
	var versionParams = {};
	var selectedVer = {};
	
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
		console.log(data);
	})
	var table = document.getElementById("restoreTable");
	if(restorePoints != null){
		$("#hiddenLatestRestoreId").val(restorePoints[0].uuid);
		for(i=0;i<restorePoints.length;i++){
			var row = table.insertRow(-1);
			var selectCell = row.insertCell(-1);
			//if(i>0){
				//var radio = document.createElement("p");
			//}else{
				var radio = document.createElement("input");
				radio.type = "radio";
				radio.name = "restorePoint";
				radio.value = restorePoints[i].uuid;
			//}
			
			selectCell.appendChild(radio);
			var dateCell = row.insertCell(-1);
			dateCell.innerHTML = convertTimestamp(restorePoints[i].protectionTime);
			var policyCell = row.insertCell(-1);
			var policyText = document.createTextNode(restorePoints[i].protectionInfo['storageProfileName']);
			policyCell.appendChild(policyText);
			row.appendChild(selectCell);
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
		$("#restoreBtns").hide();
	}
	
	
	$("#restoreObject").submit(function() {
		var restoreUrl;
		if(!$("input[name='restoreTypes']", this).is(':checked') || !$("input[name='restorePoint']", this).is(':checked')){
			alert("Please select restore type/point");
			return false;
		}
		/* Changed selectedType once the endpoint has been created to restore different types */
		var selectedType = $( "input[name='restoreTypes']:checked").val();
		//var selectedType = 'test'
		var selectedPoint = $( "input[name='restorePoint']:checked").val();
		var latestPoint = $("#hiddenLatestRestoreId").val();

		if(selectedPoint == latestPoint){
			restoreURL = PluginUtil.getWebContextPath() + "/rest/spp/restore/latest/" + selectedType + "?";
		}else{
			// Change restoreURL for endpoints when selecting different restore points
			restoreURL = PluginUtil.getWebContextPath() + "/rest/spp/restore/version/" + selectedType + "?";
		}
		for(p=0;p<restorePoints.length;p++){
			if(restorePoints[p].uuid == selectedPoint){
				//selectedVer = JSON.stringify(restorePoints[p]);
				selectedVer = restorePoints[p];
			}
		}
		console.log(selectedVer);
		//console.log(restoreURL);
		console.log(params);
		//var test = params.serializeJson();
		for (let key in params) {
		    let value = params[key];
		    console.log(key + "=" + value);
		    restoreURL += key + "=" + value + "&"
		    
		}
		restoreURL = restoreURL.slice(0, -1);
		testParams = {};
		console.log(restoreURL);
		postData = JSON.stringify(selectedVer);
		var $req = $.ajax({
			  url:restoreURL,
			  type:"POST",
			  data:postData,
			  contentType:"application/json; charset=utf-8",
			  dataType:"json",
			  success: function(data){
				  console.log(data);
			  }})
//		var $req = $.post(restoreURL, postData, function(data) {
//					console.log(data);
//		}, 'json')
		WEB_PLATFORM.closeDialog();
		//return false;
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