$(document).ready(
		function() {
			var $regreq = $.getJSON(PluginUtil.getWebContextPath()
					+ "/rest/spp/register", function(data) {
				populate('#registerForm', data);
			});

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
				var $slareq = $.getJSON(PluginUtil.getWebContextPath()
						+ "/rest/spp/sla", function(data) {
					alert(data);
				})
			});
			
			$('#getvminfotest').click(function () {
				var $vmreq = $.get(PluginUtil.getWebContextPath()
						+ "/rest/spp/vm", "vm=BCJ-LINUX", function(data) {
					alert(data);
				})
			});
			
			$('#getfolderinfotest').click(function () {
				var $vmreq = $.get(PluginUtil.getWebContextPath()
						+ "/rest/spp/folder", "folder=BCJ-SPP", function(data) {
					alert(data);
				})
			});
			
			$('#assignvmtest').click(function () {
				var $assreq = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignvm", "vm=BCJ-LINUX&sla=Gold,Silver", function(data) {
					alert(data);
				})
			});
			
			$('#unassignvmtest').click(function () {
				var $assreq = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignvm", "vm=BCJ-LINUX&sla=", function(data) {
					alert(data);
				})
			});
			
			$('#assignfoldertest').click(function () {
				var $assreq = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignfolder", "folder=BCJ-SPP&sla=Silver,Bronze", function(data) {
					alert(data);
				})
			});
			
			$('#unassignfoldertest').click(function () {
				var $assreq = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/assignfolder", "folder=BCJ-SPP&sla=", function(data) {
					alert(data);
				})
			});
			
			$('#restorevmtest').click(function () {
				var $assreq = $.post(PluginUtil.getWebContextPath()
						+ "/rest/spp/restore/latest/test", "vm=BCJ-LINUX", function(data) {
					alert(data);
				})
			});
		});

function populate(frm, data) {
	$.each(data, function(key, value) {
		$('[name=' + key + ']', frm).val(value);
	});
}