$(document).ready(function() {
	
	// Set variables
	var selectedName;
	var selectedOptions;
	var selectedType;
	var selectedIdType;
	var selectedId;
	var params = {};
	
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
	$("#hiddenObjectName").val(selectedName);
	$("#hiddenObjectName").attr("name",selectedType);
	$("#hiddenObjectId").val(selectedId);
	$("#hiddenObjectId").attr("name",selectedIdType);
	
	// Get all SLA policies and select all that are enabled for specific VM
	var $slareq = $.getJSON(PluginUtil.getWebContextPath()
				+ "/rest/spp/sla", function(data) {
					var checkbox;
					var label; 
					for(i=0;i<data.length;i++){
						checkbox = document.createElement("input");
						label = document.createElement("label");
						var description = document.createTextNode("   " + data[i].name);
						checkbox.type = "checkbox";
						checkbox.name = "sla";
						checkbox.value = data[i].name;
						checkbox.id = "sla-" + data[i].name;
						
						label.className = "pure-checkbox";
						var forAtt = document.createAttribute("for");       
						forAtt.value = "sla-" + data[i].name          
						label.setAttributeNode(forAtt); 
						
						if($.inArray(data[i].name, selectedOptions.storageProfiles) >= 0){
							checkbox.checked = true;
						}
						
						label.appendChild(checkbox);
						label.appendChild(description);
						$("#listslapolicies").append(label);
					}
		})
		
		$("#setSlaPolicy").submit(function() {
			var $form = $(this)
			var params = $form.serialize().replace(/&sla=/g,",");
			
			if(params.search("sla=") < 0) {
				params += "&sla=";				
			}
			var $assreq = $.post(PluginUtil.getWebContextPath()
					+ "/rest/spp/assign" + selectedType, params, function(data) {
				//console.log(data);
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