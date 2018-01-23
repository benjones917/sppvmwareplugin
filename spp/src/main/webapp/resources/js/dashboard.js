$(document).ready(function() {
	/** Check that SPP has been registered **/
	var registeredFlag = false;
	$.ajaxSetup({
	    async: false
	});
	var $regreq = $.getJSON(PluginUtil.getWebContextPath()
			+ "/rest/spp/register", function(data) {
		if(!$.isEmptyObject(data)){
			registeredFlag = true;
		}
	});
	$.ajaxSetup({
	    async: true
	});
	
	if(!registeredFlag){
		$("#sppRegistration").show();
		$("#registerForm").submit(function() {
			var $form = $(this) 
			var sppHost = $("#sppHost").val();
			var sppUser = $("#sppUser").val();
			var sppPass = $("#sppPass").val();
			if(sppHost == "" || sppUser == "" || sppPass == ""){
				alert("Please make sure all fields are filled out");
				return false;
			}
			json = { registrationInfo : $form.serializeJson()};
			url = PluginUtil.getWebContextPath() + $form.attr('action');
			$.post(url, json, function(data) {
				location.reload();
				return true;
			})
			return false;
		});
	}else{
		$("#dashboard").show();
		var userSession = WEB_PLATFORM.getUserSession();
		var slaPolicyNames = [];
		var protectedVms = [];
		var protectedVmCount = 0;
		$('#dvLoading, #overlay, #overlay-back').fadeIn();
		vcenterUID = userSession.serversInfo[0].serviceGuid;
		$.ajaxSetup({
		    async: false
		});
		var $slareq = $.getJSON(PluginUtil.getWebContextPath()
				+ "/rest/spp/sla", function(data) {
			slaHeaderDiv = document.createElement("div");
			slaHeaderDiv.className = "pure-u-1";
			slaHeaderText = document.createElement("h3");
			slaHeaderText.innerText = "SLA Policies";
			slaHeaderDiv.append(slaHeaderText);
			$("#slaPolicies").append(slaHeaderDiv);
			
			for(x=0;x<data.length;x++){
				slaPolicyNames.push(data[x].name);
				protectedVms[x] = [];
				slaPolicyDiv = document.createElement("div");
				slaPolicyDiv.className = "pure-u-1-" + data.length;
				slaPolicyDiv.id = "sla-" + data[x].name;
				slaPolicyHeader = document.createElement("h3");
				slaPolicyHeader.innerText = data[x].name;
				slaPolicyDiv.append(slaPolicyHeader);
				$("#slaPolicies").append(slaPolicyDiv);
			}
		});
		$.ajaxSetup({
		    async: true
		});
		var $dashReg = $.getJSON(PluginUtil.getWebContextPath()
				+ "/rest/spp/dashboard", "hvid="+vcenterUID, function(data) {
			
			var totalVms = data.length;
			
			for(i=0;i<totalVms;i++){
				var slaPolicies = data[i].storageProfiles;
				if(slaPolicies.length > 0){
					protectedVmCount++;
					for(y=0;y<slaPolicies.length;y++){
						arrIndex = slaPolicyNames.indexOf(slaPolicies[y]);
						protectedVms[arrIndex].push(data[i].name);
					}
				}
			}
			var unprotectedVm = totalVms - protectedVmCount
			$("#unprotectedCount").text(unprotectedVm);
			$("#protectedCount").text(protectedVmCount);

			for(a=0;a<slaPolicyNames.length;a++){
				if(protectedVms[a].length >0){
					vmList = document.createElement("ul");
					for(b=0;b<protectedVms[a].length; b++){
						vmListRow = document.createElement("li");
						vmListRow.innerText = protectedVms[a][b];
						vmList.append(vmListRow);
					}
					$("#sla-" + slaPolicyNames[a]).append(vmList);
				}else{
					emptyVms = document.createElement("p");
					emptyVms.innerText = "There are no VMs under this SLA policy"
					$("#sla-" + slaPolicyNames[a]).append(emptyVms);
				}
			}
			$('#dvLoading, #overlay, #overlay-back').fadeOut();
		});
	}
});
