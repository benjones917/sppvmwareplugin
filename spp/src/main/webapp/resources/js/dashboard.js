$(document).ready(function() {
	var userSession = WEB_PLATFORM.getUserSession();
	console.log("Dashboard");
	vcenterUID = userSession.serversInfo[0].serviceGuid;
	var $dashReg = $.getJSON(PluginUtil.getWebContextPath()
			+ "/rest/spp/dashboard", "hvid="+vcenterUID, function(data) {
		console.log(data);

	});
});
