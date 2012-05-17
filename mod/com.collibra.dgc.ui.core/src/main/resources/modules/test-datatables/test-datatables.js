/**
 * Testing Felix Data Tables
 */
/*global define,require,core,console */
define('modules/test-datatables',
      ['core', '!modules/test-datatables/libs/datatables/media/js/jquery.dataTables.js'], // Add entry in this array to include lib files. e.g.: 'modules/test-datatables/test-datatables/libs/...'
      function(require, exports, module) {
 "use strict";

 /*************************************
   *           Requirements            *
   *************************************/

 /** @private */
 var core = require('core'),
     $ = core.libs.get('jquery'),
     _config, _tableElement;


 /*************************************
   *              Private              *
   *************************************/

 /** @private */
 var _testDataTables = {
                        
   _init: function() {
     var _self = this;
     // Everything that happens here will execute on load.

     console.log('It works!');
   },

   _addFilter : function(configObject, operator, value) {
     if (value && value.length > 0) {
       if (configObject.filter !== undefined) {
         configObject.filter.operator = operator;
         configObject.filter.value = value;
       } else {
         configObject.filter = {
           'value' : value,
           'operator' : operator
         };
       }
     }
   },
   
   _getColumnConfig: function(config, colName) {
     if(colName === 'term') {
       return config.reportconfig.term;
     }
     var result;
     $.each(config.reportconfig.term, function(key, value) {
       if (value.name === colName) {
         result = value;
       }
     });
     return result;
   },
   
   _removeAllSorting: function(config) {
     delete config.reportconfig.term.sortDESC;
     $.each(config.reportconfig.term, function(key, value) {
       delete value.sortDESC;
     });
   },
   
   _setConfig: function(config) {
     _config = config;
   },
   
   _getConfig: function() {
     return _config;
   },
   
   _getTable: function() {
     return _tableElement.dataTable();
   },
   
   _setTable: function(tableElement) {
     _tableElement = tableElement;
   }
 };

 /*************************************
   *               Public              *
   *************************************/

 var TestDataTables = /** @lends TestDataTables */
 {
   // Every function in here will be accessible through
   // TestDataTables.function() at runtime on the page.

   initTable : function(jsonConfig, columns) {
     var _self = this;
     _self.setConfig(jsonConfig);
     core.debug.show("hello from TestDataTables.initTable() start");
     var ajaxURL = 'http://localhost:8080/com.collibra.dgc.war/rest/1.0/report';
     $(document).ready(function() {
       $('#table').dataTable({
         "bProcessing" : true,
         "bServerSide" : true,
         "bFilter"     : false,
         "sAjaxSource" : ajaxURL,
         "iDisplayLength" : 50,
         "aLengthMenu" : [[50, 100, -1], [50, 100, "All"]],
         "aoColumns"   : columns,
         "bDeferRender": true,
         "fnServerData" : function(sSource, aoData, fnCallback) {
           _self.getConfig().reportconfig.displayStart = aoData[3].value;
           _self.getConfig().reportconfig.displayLength = aoData[4].value;
           _testDataTables._removeAllSorting(_self.getConfig());
           var sortColIndex;
           var sortDESC;
           // find sorting directions
           $.each(aoData, function(i, val) {
             var key = val.name;
             var value = val.value;
             if(key.indexOf("iSortCol_0") === 0) {
               sortColIndex = value;
             }
             if(key.indexOf("sSortDir_0") === 0) {
               if(value === "desc") {
                 sortDESC = true;
               } else {
                 sortDESC = false;
               }
               $.each(aoData, function(i, val) {
                 if(val.name === "mDataProp_" + sortColIndex) {
                   var colName = val.value;
                   _testDataTables._getColumnConfig(_self.getConfig(), colName).sortDESC = sortDESC;
                 }
               });
             }
           });
           // push the json config
           aoData.push({
             "name" : "config",
             "value" : JSON.stringify(_self.getConfig())
           });
           $.getJSON(sSource, aoData, function(json) {
             // do wathever post-json processing, then pass it back to
             // datatables
             fnCallback(json);
           });
         }
       });
     });
     _testDataTables._setTable($('#table'));

     core.debug.show("hello from TestDataTables.initTable() end");
   },

   addFilter : function(jsonConfig, columnName, operator, val) {
     var config = _testDataTables._getColumnConfig(jsonConfig, columnName);
     _testDataTables._addFilter(config, operator, val);
   },
   
   setConfig: function(config) {
     _testDataTables._setConfig(config);
   },
   
   getConfig: function() {
     return _testDataTables._getConfig();
   },

   reDraw: function() {
     _testDataTables._getTable().fnDraw();
   }
 };

 _testDataTables._init();

 return TestDataTables;
});

core.module.alias.add('TestDataTables', 'modules/test-datatables');
core.module.use('TestDataTables');