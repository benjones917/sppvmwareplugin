$(document).ready(
		
		function() {
			WEB_PLATFORM.sendNavigationRequest('com.ibm.spp.vcReg');
			var registeredFlag = false;
			var userSession = WEB_PLATFORM.getUserSession();
			console.log(userSession.serversInfo);
			vcenterUID = userSession.serversInfo[0].serviceGuid;
			var $dashReg = $.getJSON(PluginUtil.getWebContextPath()
					+ "/rest/spp/dashboard", "hvid="+vcenterUID, function(data) {
				console.log(data);
				
				//populate('#registerForm', data);
			});
			
			var $regreq = $.getJSON(PluginUtil.getWebContextPath()
					+ "/rest/spp/register", function(data) {
				if(!$.isEmptyObject(data)){
					registeredFlag = true;
				}
				console.log(data);
				populate('#registerForm', data);
			});
			console.log(registeredFlag);
			$("#registerForm").submit(function() {
				var $form = $(this), json = {
					registrationInfo : $form.serializeJson()
				}, url = PluginUtil.getWebContextPath() + $form.attr('action')
				$.post(url, json, function(data) {
					alert(data);
				})
				return false;
			});
			
			$("#sppsla").click(function() {
				var $req = $.getJSON(PluginUtil.getWebContextPath()
						+ "/rest/spp/sla", function(data) {
					alert(data);
				})
			});
			
			$('#getvminfotest').click(function () {
				var $req = $.get(PluginUtil.getWebContextPath()
						+ "/rest/spp/vm", "vm=BCJ-LINUX&vmid=vm-216", function(data) {
					//alert(data);
					console.log(data);
				})
			});
			
			$('#getfolderinfotest').click(function () {
				var $req = $.get(PluginUtil.getWebContextPath()
						+ "/rest/spp/folder", "folder=BCJ-SPP&groupid=group-v1347", function(data) {
					alert(data);
				})
			});
			
			$('#assignvmtest').click(function () {
				var $req = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignvm", "vm=BCJ-LINUX&vmid=vm-216&sla=Gold,Silver", function(data) {
					alert(data);
				})
			});
			
			$('#unassignvmtest').click(function () {
				var $req = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignvm", "vm=BCJ-LINUX&vmid=vm-216&sla=", function(data) {
					alert(data);
				})
			});
			
			$('#assignfoldertest').click(function () {
				var $req = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignfolder", "folder=BCJ-SPP&groupid=group-v1347&sla=Silver,Bronze", function(data) {
					alert(data);
				})
			});
			
			$('#unassignfoldertest').click(function () {
				var $req = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignfolder", "folder=BCJ-SPP&groupid=group-v1347&sla=", function(data) {
					alert(data);
				})
			});
			
			$('#restorevmtest').click(function () {
				var $req = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/restore/latest/test", "vm=BCJ-LINUX&vmid=vm-216", function(data) {
					alert(data);
				})
			});
			
			$('#getrestoreses').click(function () {
				var $req = $.getJSON(PluginUtil.getWebContextPath()
						+ "/rest/spp/activerestores", function(data) {
					alert(data);
				})
			});
			
			$('#getvmversinfotest').click(function () {
				var $req = $.getJSON(PluginUtil.getWebContextPath()
						+ "/rest/spp/vmversions", "hvid=1004&vmid=7df14b8e9cc59eb16c2157e83c775b64", function(data) {
					alert(data);
				})
			});
			
			$('#getfolderversinfotest').click(function () {
				var $req = $.getJSON(PluginUtil.getWebContextPath()
						+ "/rest/spp/folderversions", "hvid=1004&folderid=4e4e4bf2bb8505ce19e37488a6c0474b", function(data) {
					alert(data);
				})
			});
		});

function populate(frm, data) {
	$.each(data, function(key, value) {
		$('[name=' + key + ']', frm).val(value);
	});
}