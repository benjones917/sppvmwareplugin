$(document).ready(function() {
	var $reginfo = $.getJSON(PluginUtil.getWebContextPath() + "/rest/spp/register", function (data) {
		populate('#registerForm', data);
	});
		
	
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

function populate(frm, data) {
	  $.each(data, function(key, value){
	    $('[name='+key+']', frm).val(value);
	  });
	}