/**
 * This is the JavaScript module for the bootstrap
 * functionality.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:dieter@collibra.com">Dieter Wachters</a>
 * @module pages/bootstrap/bootstrap
 * @alias Bootstrap
 * @namespace bootstrap
 */
/*global define,require,core */
/*global define,require,core */
define('pages/bootstrap',
       ['core'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core');

  /** @private */
  var $ = core.libs.get('jquery'),
      i18n = core.i18n.getTranslations('bootstrap');

  $("#bootstrapForm").submit(function() {
	$.ajax({
      url: "${contextPath}rest/latest/initialize/bootstrap",
      data: { bootstrap: $("input:radio[name=bootstrapChoice]:checked").val() },
      type: 'POST',
      accept: 'application/json',
      beforeSend: function(request){
        $("#loading").show();
        $("#submit").attr("disabled", "disabled");
      },
      success: function(data) {
        updateStatus(data);
      },
      error: function(a, b, c) {
        window.alert("An error occured while bootstrapping the server: "+a.message);
        doneWorking();
      }
    });
    return false;
  });
  
  function doneWorking() {
    $("#loading").hide();
    $("#submit").removeAttr("disabled");
  }
  
  function updateStatus(id) {
	$.ajax({
      url: "${contextPath}rest/latest/job/"+id,
      type: 'GET',
      accept: 'application/json',
      success: function(data) {
        if (data.state === "ERROR") {
          $("#progress").text(data.percentage + "%");
          window.alert("An error occured while bootstrapping: "+data.message);
          doneWorking();
        }
        else if (data.state === "COMPLETED") {
          $("#progress").text("100%");
          window.alert("Congratulations, the bootstrapping is finished.");
          window.location = '${contextPath}';
        }
        else if (data.state === "WAITING" || data.state === "RUNNING") {
          $("#progress").text(data.percentage + "%");
          setTimeout(function(){updateStatus(id);}, 800);
        }
      }
    });
  }
});

core.module.alias.add('Bootstrap', 'pages/bootstrap');
core.module.use('Bootstrap');