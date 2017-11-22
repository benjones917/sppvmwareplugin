$(document).ready(function() {
	   $("#registerForm").submit(function() {
		      var $form = $( this ),
		         json = {registrationInfo : $form.serializeJson()},
		         url = PluginUtil.getWebContextPath() + $form.attr('action')
		         $.post(url, json,
		        		 function() {
		        	 alert("Registering");
		         })
		         return false;
		   });
   });