$(document).ready(function() {
	$('#sppRegistration').hide();
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
			json = { registrationInfo : $form.serializeJson()} 
			url = PluginUtil.getWebContextPath() + $form.attr('action')
			$.post(url, json, function(data) {
				location.reload();
				return true;
			})
			return false;
		});
	}else{
		$("#sppvCenter").show();
		var userSession = WEB_PLATFORM.getUserSession();
		$("#vCenterIn").hide();
	    $("#vCenterOut").hide();
		console.log("vCenter Reg");
		vcenterUID = userSession.serversInfo[0].serviceGuid;
		vCenterName = userSession.serversInfo[0].name
		$("#vCenterHostInput").val(vCenterName);
		var $dashReg = $.getJSON(PluginUtil.getWebContextPath() + "/rest/spp/vcreg", "vcid="+vcenterUID)
			.done(function( data ) {
				console.log(data);
				loadData(data);
				$("#vCenterIn").show();
			    $("#vCenterOut").hide();
				
				//$("#vCenterOut").show();
			  })
			  .fail(function( jqxhr, textStatus, error ) {
			    console.log( "vCenter not registered" );
			    //$("#vCenterIn").hide();
			    $("#vCenterHostInput").val(vCenterName);
			    $("#vCenterOut").show();
			});
		
		$("#vcenterRegisterForm").submit(function() {
			var $form = $(this)
			var vcenterHost = $("#vCenterHostInput").val();
			var vcenterUser = $("#vCenterUserInput").val();
			var vcenterPass = $("#vCenterPassInput").val();
			var vcenterPort = $("#vCenterPortInput").val();
			if(vcenterHost == "" || vcenterUser == "" || vcenterPass == "" || vcenterPort == ""){
				alert("Please make sure all fields are filled out");
				return false;
			}

			if($("#vCenterSSLCheckBox").prop('checked')) {
				$("#vCenterSSLHidden").val("true");
			}else{
				$("#vCenterSSLHidden").val("false");
			}
			//console.log($form);
			formData = $form.serializeJson();
			var formDataJson = JSON.parse(formData);
			formDataJson['opProperties'] = {snapshotConcurrency : formDataJson['snapshotConcurrency']};
			delete formDataJson['snapshotConcurrency'];
			
			//console.log(formDataJson);
			formattedFormData = JSON.stringify(formDataJson);
			json = {registrationInfo : formattedFormData} ;
			//console.log(json);
			//json = {registrationInfo : $form.serializeJson() } ;

			url = PluginUtil.getWebContextPath() + $form.attr('action');
			console.log(json);
			$('#dvLoading, #overlay, #overlay-back').fadeIn();
			$.post(url, json, function(data) {
				
				response = JSON.parse(data);
				console.log(data);
				if(response.statusCode == 201){
					location.reload();
					return true;
				}else{
					$('#dvLoading, #overlay, #overlay-back').fadeOut();
					alert(response.response.description);
				}
			})
			return false;
		});
	}
	
	
	
});

function loadData(data){
	console.log(data.name);
	$("#vCenterNameVal").append(document.createTextNode(data.name));
	$("#vCenterSiteVal").append(document.createTextNode(data.siteName));
	$("#vCenterPortVal").append(document.createTextNode(data.portNumber));
	$("#vCenterSSLVal").append(document.createTextNode(data.sslConnection));
}
