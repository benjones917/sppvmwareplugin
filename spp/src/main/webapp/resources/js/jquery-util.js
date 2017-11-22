/* Copyright (c) 2012-2017 VMware, Inc. All rights reserved. */
// Utility to serialize a form into Json data
// See http://benalman.com/projects/jquery-misc-plugins/#serializeobject
(function($,undefined){
   $.fn.serializeJson = function(){
     var obj = {};
     $.each( this.serializeArray(), function(i,o){
         var n = o.name,
         v = o.value;
         obj[n] = (obj[n] === undefined) ? v
           : $.isArray( obj[n] ) ? obj[n].concat( v )
           : [ obj[n], v ];
     });
     return JSON.stringify(obj);
   };
 })(jQuery);


// Set style of JQuery-ui accordion widget. See http://jqueryui.com/accordion/
$(function() {
   var accordion$ = $("#accordion");
   if (accordion$.length > 0) {
      $("#accordion").accordion({
        heightStyle: "content"
      });
   }
});
