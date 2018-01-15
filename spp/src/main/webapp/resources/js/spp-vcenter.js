$(document).ready(function() {
	var userSession = WEB_PLATFORM.getUserSession();
	console.log("vCenter Reg");
	vcenterUID = userSession.serversInfo[0].serviceGuid;
	var $dashReg = $.getJSON(PluginUtil.getWebContextPath()
			+ "/rest/spp/vcreg", "vcid="+vcenterUID, function(data) {
		console.log(data);

	});
	
	$("#vcenterRegisterForm").submit(function() {
		var $form = $(this)
		var vcenterHost = $("#vCenterHost").val();
		var vcenterUser = $("#vCenterUser").val();
		var vcenterPass = $("#vCenterPass").val();
		var vcenterPort = $("#vCenterPort").val();
		if(vcenterHost == "" || vcenterUser == "" || vcenterPass == "" || vcenterPort == ""){
			console.log("Empty host");
		}
		json = {registrationInfo : $form.serializeJson() } ;
		url = PluginUtil.getWebContextPath() + $form.attr('action');
		console.log(json);
		/*$.post(url, json, function(data) {
			alert(data);
		})*/
		return false;
	});
});
