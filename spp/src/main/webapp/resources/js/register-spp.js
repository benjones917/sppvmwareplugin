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
						+ "/rest/spp/vm", "vmName=BCJVM1", function(data) {
					alert(data);
				})
			});
		});

function populate(frm, data) {
	$.each(data, function(key, value) {
		$('[name=' + key + ']', frm).val(value);
	});
}