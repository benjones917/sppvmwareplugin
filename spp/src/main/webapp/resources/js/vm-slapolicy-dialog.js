$(document).ready(function() {
	
	// Set variables
	var vmName;
	var selectedVM;
	
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
	var dataUrl = '/ui/data/properties/' + targets + '?properties=name';
	$.getJSON(dataUrl, function (data) {
        // data object contains the properties listed above
        vmName = data.name;
    });
	
	// Get the VM data from SPP
	var $vmreq = $.get(PluginUtil.getWebContextPath()
			+ "/rest/spp/vm", "vm="+vmName, function(data) {
			selectedVM = JSON.parse(data);
	})
	
	$("#vmNameText").append(document.createTextNode(vmName));
	$("#hiddenVMName").val(vmName);
	
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
						
						if($.inArray(data[i].name, selectedVM.storageProfiles) >= 0){
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
					+ "/rest/spp/assignvm", params, function(data) {
				console.log(data);
			})
			WEB_PLATFORM.closeDialog();
			return false;
		});
});