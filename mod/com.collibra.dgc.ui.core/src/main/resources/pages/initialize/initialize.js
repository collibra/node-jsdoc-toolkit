/**
 * This is the JavaScript module for the initialize
 * functionality.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:dieter@collibra.com">Dieter Wachters</a>
 * @module pages/initialize/initialize
 * @alias Initialize
 * @namespace initialize
 */
/*global define,require,core */
/*global define,require,core */
define('pages/initialize',
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
      i18n = core.i18n.getTranslations('initialize');

  $("#initializeForm").submit(function() {
	$.ajax({
      url: "${contextPath}rest/latest/initialize/init",
      data: {driver: $("#driver").val(), url: $("#url").val(), username: $("#username").val(), password: $("#password").val() , database: $("#database").val()},
      type: 'POST',
      accept: 'application/json',
      beforeSend: function(request){
        $("#loadingIcon").show();
        $("#submit").attr("disabled", "disabled");
      },
      success: function(data) {
        window.location = 'bootstrap';
      },
      error: function(a, b, c) {
        window.alert("An error occured while initializing the database: "+a.message);
        $("#loadingIcon").hide();
        $("#submit").removeAttr("disabled");
      }
    });
    return false;
  });
  
  function driverUpdated(driver) {
    $("#username").val("");
    $("#password").val("");
    $("#database").val("");
    
    if (driver === "org.h2.Driver") {
      $("#url-row").hide();
      $("#username-row").hide();
      $("#password-row").hide();
      $("#database-row").hide();
    }
    else {
      $("#url-row").show();
      $("#username-row").show();
      $("#password-row").show();
      $("#database-row").show();
    }
    if (driver === "oracle.jdbc.driver.OracleDriver") {
      $("#url").val("jdbc:oracle:thin:@localhost:1521:dgc");
    }
    else if (driver === "com.mysql.jdbc.Driver") {
      $("#url").val("jdbc:mysql://localhost/dgc");
      $("#database").val("dgc");
    }
    else if (driver === "com.microsoft.sqlserver.jdbc.SQLServerDriver") {
      $("#url").val("jdbc:sqlserver://localhost;databaseName=dgc");
      $("#database").val("dgc");
    }
    else {
      $("#url").val("");
    }
  }
  
  $("#driver").change(function() {
    driverUpdated($(this).val());
  });
  
  driverUpdated($("#driver").val());
});

core.module.alias.add('Initialize', 'pages/initialize');
core.module.use('Initialize');