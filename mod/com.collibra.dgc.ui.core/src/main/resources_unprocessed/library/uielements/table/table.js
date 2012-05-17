/**
 * Table.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module library/uielements/table
 * @alias Table
 * @namespace controls
 */
/*global define,require,core,FixedHeader */
define('library/uielements/table',
       [
        'core',
        'modules/text',
        'library/uielements/radiocheck',
        'library/uielements/popover',
        'library/uielements/menu',
        'library/uielements/notifications/notification'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      text = require('modules/text'),
      radioCheck = require('library/uielements/radiocheck'),
      popover = require('library/uielements/popover'),
      menu = require('library/uielements/menu'),
      notification = require('library/uielements/notifications/notification'),
      i18n = core.i18n.getTranslations('library.uielements.table');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _table = {
    /**
     * @see Table.create
     */
    create: function(selector, options) {
      var _self = this,
          element = core.dom.select(selector),
          tableInstance, actionDone, columns, reportConf, tableProperyObject = {},
          buttonRemove = null,
          buttonConfig = null,
          popoverDelContent = null,
          columnsRendered = [];

      if (core.dom.is(element)) {
        //Object passed to DataTable.
        tableProperyObject = {
          bProcessing : true, //show 'processing...'
          bFilter     : false, //disable filter
          bLengthChange : false, //disable table length selection
          bAutoWidth: false,
          bRetrieve: false,
          //iDisplayLength: -1 //display all
          sDom: "<'#controlsBox'><'#selectCounter'>lfrtip"
        };

        tableProperyObject.aoColumns = _self.generate.generateColumnObject(selector, options.url);

        //Use data from REST.
        if (options.url) {
          reportConf = _self.generate.createRESTConfig(selector, {}, options);
          reportConf = {reportconfig: reportConf};
          
          tableProperyObject.bServerSide = true;
          tableProperyObject.sAjaxSource = options.url;
          tableProperyObject.bDeferRender = true;
          tableProperyObject.fnServerData = function(sSource, aoData, fnCallback) {
            var sortColIndex, sortDESC, key, value, colName; //sorting vars

            //Ask REST for data for a single table sheet. Override createRESTConfig()!
            reportConf.reportconfig.displayStart = aoData[3].value;
            reportConf.reportconfig.displayLength = aoData[4].value;

            //Remove old sorting.
            reportConf = _self.sorting.removeAllSorting(reportConf);

            //sorting
            if (options.sorting) {
              // find sorting directions
              core.object.each(aoData, function(i, val) {
                key = val.name;
                value = val.value;

                //Get number of column for sorting.
                if(key.indexOf("iSortCol_0") === 0) {
                  sortColIndex = value;
                }

                //Get sorting type.
                if(key.indexOf("sSortDir_0") === 0) {
                  if(value === "desc") {
                    sortDESC = true;
                  } else {
                    sortDESC = false;
                  }

                  core.object.each(aoData, function(i, val) {
                    if(val.name === "mDataProp_" + sortColIndex) {
                      colName = val.value;

                      //Set new sorting.
                      reportConf = _self.sorting.updateSorting(reportConf, colName, sortDESC);
                    }
                  });
                }
              });
            }

            //Push the json config.
            aoData.push({
              name : "config",
              value : JSON.stringify(reportConf)
            });

            core.AJAX.call(sSource, aoData, {
              dataType: 'json',
              onSuccess: function(json) {
                // do whatever post-json processing, then pass it back to datatables
                fnCallback(json);
              }
            });
          };
        }

        //Set pagination.
        if (options.pagination) {
          tableProperyObject.bPaginate = true;
          tableProperyObject.sPaginationType = "collibra_pagination";
        } else {
          tableProperyObject.bPaginate = false;
        }

        //Show information? (X to Y of the Z records).
        if (options.counting) {
          tableProperyObject.bInfo = true;
          tableProperyObject.fnInfoCallback = _self.dataTableInitMethods.countingInfo;
        } else {
          tableProperyObject.bInfo = false;
        }
        
        //Enable/disable sorting.
        tableProperyObject.bSort = options.sorting;

        //Add extra items to header.
        element.find('TH').append('<i class="sorting-icon"></i>');

        if (options.selectable) {
          //Bulk action checkbox.
          element.find('TH').eq(0).prepend('<input type="checkbox" name="bath-checkbox" />');

          //Add checkbox to each row.
          //tableProperyObject.fnRowCallback = _self.dataTableInitMethods.rowCallback;

          tableProperyObject.fnDrawCallback = function(oSettings) {
            _self.initializeCheckboxUI(oSettings.nTable);
          };

          //Bind checkbox action. We bind it to a whole table (it exists), so we can do it before initializing dataTable.
          _self.bindCheckboxEvents(element);
        }

        tableProperyObject.iDisplayLength = options.display.length; //Display (options.display.length) elements.
        
        //#########TABLE CREATION##########
        tableInstance = element.dataTable(tableProperyObject);
        //#################################
        window.oTable = tableInstance;
        
        if (options.deletable) {
          
          //TODO! Force selectable when using deletable?
          if(!options.selectable) {
            throw "options.selectable is set to false. Can we use deletable without selectable?";
          }
          
          buttonRemove = core.dom.select('<div style="display: none;" class="table-delete-btn btn no-label"><i class="icon-trash"></i></div>');
          popoverDelContent = core.dom.select('<div class="table-delete-tooltip"><div class="table-delete-tooltip-label">Are you sure you want to delete all the selected business terms?</div></div>');
          
          //Save the reference.
          tableInstance.data("controls", {
            "delete": buttonRemove
          });
          
          popover.create(buttonRemove, {
            type: popover.TYPE_DANGER,
            hide:{
              blur: false
            },
            height: 'auto',
            content: 'dummy content',
            orientation: popover.ORIENTATION_BOTTOM
          });
          core.dom.select("#controlsBox").append(buttonRemove);
          
          //Add buttons with actions.
          popoverDelContent.append(core.dom.select('<button class="btn-danger"><i class="icon-ok"></i><span>Yes</span></button>').on("click", function() {
            core.debug.show("delete");
//            core.object.each(_table.getSelected(tableInstance), function(key, v) {
//              tableInstance.dataTable().fnDeleteRow(v.ref);
//            });
            _self.updateDeleteButton(tableInstance);
            
            notification.create({
              content: {
                title: "Rows deleted.",
                message: "TODO! REST call needs to be implemented."
              }
            });
            
            popover.hide(buttonRemove);
//            core.REST.remove("/report/", {
//              rid: [1, 2]
//            }, {
//              onSuccess: function() {
//                popover.hide(buttonRemove);
//                //Show notification success.
//              },
//              onError: function() {
//                //Show notification error.
//                core.object.each(_table.getSelected(tableInstance), function(key, v) {
//                  tableProperyObject.fnDeleteRow( v.id );
//                });
//
//                popover.hide(buttonRemove);
//              }
//            });
            
          })).append(core.dom.select('<button class="btn-white"><i class="icon-none"></i><span>No</span></button>').on("click", function() {
            core.debug.show("cancel");
            popover.hide(buttonRemove);
          }));
          
          popover.setContent(buttonRemove, popoverDelContent);
          
        }
        
        //Add configuration control button, for selecting/moving columns.
        if(options.columnsConfig) {
          
          buttonConfig = core.dom.select('<span id="" style="float: right;" class="btn no-label"><i class="icon-cogwheel"></i></span>');
          core.dom.select("#controlsBox").append(buttonConfig);
          
          core.array.each(tableInstance.fnSettings().aoColumns, function(i, val) {
            if(val.mDataProp) {
              columnsRendered.push({
                label: val.mDataProp,
                selectable: true,
                name: "col-" + val.mDataProp.toLowerCase(),
                selection: {
                  checked: val.bVisible,
                  on: {
                    "change": function(e) {
                      tableInstance.fnSetColumnVis(core.dom.select(e.currentTarget).parents("li").index() + (options.selectable ? 1 : 0), core.dom.select(e.currentTarget).prev().prop("checked"));
                    }
                  }
                }
              });
            }
          });
          
          menu.create(buttonConfig, [{
              label: 'Columns',
              name: 'cols',
              sortable: true,
              menu: columnsRendered
          }, {
              label: 'Rows',
              name: 'rows'
          }], {
            onSort: function(items, oldIndex, newIndex) {
              var ind = (options.selectable ? 1 : 0);
              tableInstance.dataTable().fnColReorder(oldIndex + ind, newIndex + ind);
            }
          });
        }
        
        //Truncate text after inserting.
        text.truncate(element.find('TD'));

        //Add fixed header.
        element.tableFixedHeader();

        //Save the instance.
        element.data('tableInstance', tableInstance);
        
        //Save the options.
        element.data('options', options);

        //Initialize radiocheck. (used once, after table was created - It's not used after ajax call)
        _self.initializeCheckboxUI(element);

        //Initialize bulk edit checkbox actions. (Checkbox UI needs to be already created)
        _self.bindMasterCheckboxEvents(element.find('TH').eq(0).find('SPAN.checkboxInput'));

        return true;
      }

      return false;
    },

    dataTableInitMethods: {
      /**
       * Adds checkbox to each table row.
       *
       * @param {Node} nRow "TR" element for the current row.
       * @param {Array} aData Raw data array for this row.
       * @param {Number} iDisplayIndex The display index for the current table draw.
       * @param {Number} iDisplayIndexFull The index of the data in the full list of rows (after filtering).
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      rowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        //Add a hidden checkbox to each row. Don't initialize here - I won't work with RadioCheck UI.
       // core.dom.select(nRow).find('TD').eq(0).prepend('<input type="checkbox" text="' + aData.term + '" rid="' + aData.term_rid + '" name="row_' + iDisplayIndex + '" style="display: none;"/>');
      },

      /**
       * Create counting information.
       *
       * @param {Object} oSettings DataTables settings object.
       * @param {Number} iStart Starting position in data for the draw.
       * @param {Number} iEnd End position in data for the draw.
       * @param {Number} iMax Total number of rows in the table (regardless of filtering).
       * @param {Number} iTotal Total number of rows in the data set, after filtering.
       * @param {String} sPre The string that DataTables has formatted using it's own rules.
       * @returns {String} String with counting information used by the Table.
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      countingInfo: function(oSettings, iStart, iEnd, iMax, iTotal, sPre) {
        if (iTotal === 1) {
          return iTotal + " " + i18n.counting.single;
        } else {
          if (iStart === 1 && iEnd === iTotal) {
            return iTotal + " " + i18n.counting.multiple;
          }
          return iStart + " " + i18n.counting.to + " " + iEnd + " " + i18n.counting.of + " " + iTotal + " " + i18n.counting.multiple;
        }
      }
    },

    generate: {
      /**
       * Generate array with columns to use it with results from REST.
       *
       * @param {String/CoreObj} selector The selector pointing to the table.
       * @param {String/Boolean} options options.url property from the options object passed to create() method. Used to check if data is static or from REST.
       * @returns {Array/Boolean} Array on success and false on failure.
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      generateColumnObject: function(selector, optionsURL) {
        var node = core.dom.select(selector),
            columnObject = [];

        if (core.dom.is(node)) {
          
          //Add column with checkboxes.
          columnObject.push({
            mDataProp: null,
            "sClass": "table-selection-checkbox",
            bSortable: false,
            sWidth : "25px",
            sDefaultContent: "<input type='checkbox' />"
          });
          
          //Add other columns from data.
          core.dom.each(node.find('TH'), function(column, index) {
            if(optionsURL) {
              columnObject.push({
                mDataProp: column.data('propname'),
                sDefaultContent: ""
              });
            } else {
              columnObject.push({ "sType": "natural"});
            }
          });
          
          node.find('TH:eq(0)').before("<th></th>");

          return columnObject;
        }

        return false;
      },

      /**
       * Create config for REST
       *
       * @param {TABLE_TYPE} main The main item used to set REST config.
       * @param {Array} content An array of TABLE_TYPES used to create REST config. Table columns created in this order.
       * @param {Object} config Additional congif params.
       * @returns {Object} REST config object.
       * @example Example object structure: {
       *        "columnViewOrder":[
       *          "term",
       *          "Definition",
       *          "Status"
       *        ],
       *        "displayLength":-1,
       *        "term":{
       *          "sortDESC":true,
       *          "name":"term",
       *          "status":{
       *            "name":"Status",
       *            "filter":{}
       *          },
       *          "stringattribute":{
       *            "name":"Definition",
       *            "attributetype":"7c35be9d-188e-4ee2-af9b-0370ddebd89c",
       *            "filter":{}
       *          }
       *        },
       *        "displayStart":0
       *      }
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      createRESTConfig: function(selector, config, options) {
        var columns = core.dom.select(selector).find('TH'),
            reportconfig = {},
            item, maintype, name, type, attributetype;

        if (core.dom.is(columns)) {
          reportconfig.displayLength = config.displayLength || -1;
          reportconfig.displayStart = config.displayStart || 0;
          reportconfig.columnViewOrder = [];

          if(options.selectable) {
            //Exclude first header(bulk-checkbox) from sortable columns.
            columns = columns.not(":eq(0)");
          }
          
          core.dom.each(columns, function(column, index) {
            name = column.data("propname");
            type = column.data("tabletype");
            attributetype = column.data("attributetype");

            if (index === 0) {
              maintype = type;

              //It's the main element
              reportconfig[maintype] = {
                name: name,
                sortDESC: column.data('sortDESC') || false
              };
            } else {
              item = {}; //Properties holder for the item.
              item.name = name;

              if (attributetype) {
                item.attributetype = attributetype;
              }

              item.filter = {};
              reportconfig[maintype][type] = item;
            }

            reportconfig.columnViewOrder.push(name);
          });

          return reportconfig;
        }
        
        return false;
      }
    },

    UI: {},

    sorting: {
      /**
       * Get column object from config and update it's sorting method.
       *
       * @param {Object} config REST config object.
       * @param {String} colName Change sorting method of the column with passed name.
       * @param {Boolean} sorting How to sort the column? True for desc, false for asc.
       * @returns {Object} Return new config object with sorting updated.
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      updateSorting: function(config, colName, sorting) {
        if(colName === 'term') {
          config.reportconfig.term.sortDESC = sorting;
        } else {
          core.object.each(config.reportconfig.term, function(key, value) {
            if (value.name === colName) {
              config.reportconfig.term[key].sortDESC = sorting;
            }
          });
        }

        return config;
      },

      /**
       * Remove all sorting information from configuration object passed to REST.
       *
       * @param {Object} config REST config object.
       * @returns {Object} Return new config object with sorting removed.
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      removeAllSorting: function(config) {
        delete config.reportconfig.term.sortDESC;

        core.object.each(config.reportconfig.term, function(key, value) {
          delete value.sortDESC;
        });

        return config;
      }
    },

    /**
     * Initialize checkbox UI over checkbox input. (A row or a whole table can be passed)
     *
     * @param {String/CoreObj} selector The selector pointing to a container with checkboxes. (Or object)
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeCheckboxUI: function(selector) {
      var element = (core.dom.is(selector)) ? selector : core.dom.select(selector);

      core.dom.each(element.find('input:checkbox'), function(item) {
        radioCheck.create(item);
      });
    },
    
    
    /**
     * @see Table.getSelected
     */
    getSelected: function(table) {
      var selected = [];
      
      table = core.dom.select(table);
      
      
      
      if(_table.has(table) && table.data("options") && table.data("options").selectable) {
        core.dom.each(core.dom.select(table).find(".selected"), function(s) {
          core.debug.show(s);
          selected.push({
            id: s.find("input:checkbox").attr("name").split("row_")[1],
            rid: s.find("input:checkbox").attr("rid"),
            name: s.find("input:checkbox").attr("name"),
            text: s.find("input:checkbox").attr("text"),
            ref: s
          });
        });
        
        return selected;
      }
      
      return false;
    },

    /**
     * Bind checkbox actions. (selecting a row, updating counter and master checkbox)
     *
     * @param {CoreObj} table The table to interact with.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    bindCheckboxEvents: function(table) {
      var _self = this;

      table.on('click', 'SPAN.radioCheck', function(e) {
        var element = core.dom.select(this);

        element.closest('TR').toggleClass('selected');

        //Bind event to every checkbox.
        radioCheck.onChange(element.prev(), function() {
          var dataTable = core.dom.select(this).closest('TABLE');

          _self.updateDeleteButton(dataTable);
          _self.updateSelectCounter(dataTable);
          _self.updateMasterCheckbox(dataTable);
        });
      });
    },

    /**
     * Update master checkbox (in table header) class. (Add or remove mixed state)
     *
     * @param {CoreObj} table The table to interact with.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    updateMasterCheckbox: function(table) {
      var masterCheckboxID = table.find('TH').eq(0).find('SPAN.checkboxInput').attr('originalid'),
          masterCheckbox = core.dom.select("SPAN[originalid='" + masterCheckboxID + "']"),
          checkboxes, totalNumber, selectedNumber;

      checkboxes = table.find('SPAN.radioCheck');
      totalNumber = checkboxes.length;
      selectedNumber = checkboxes.filter('.checked').length;
      
      if (totalNumber !== selectedNumber && selectedNumber !== 0) {
        masterCheckbox.addClass('mixed');
      } else {
        masterCheckbox.removeClass('mixed');
      }
    },

    /**
     * Master checkbox events - bath select/unselect.
     *
     * @param {CoreObj} table The table to interact with.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    bindMasterCheckboxEvents: function(checkbox) {
      var _self = this,
          result,
          masterCheckboxSpanID = checkbox.attr('id'),
          table = checkbox.closest('TABLE');

      checkbox.on('click', function(e) {
        var fxHeaderCheckbox;

        if (!checkbox.hasClass('disabled')) {
          result = (checkbox.hasClass('checked')) ? _self.uncheckAll(table) : _self.checkAll(table);

          //Since radioCheck doesn't work with double IDs and we operate only on main checkbox for checking it's states, we can update only the dispaly of duplicate checkbox - we don't have to update duplicate input.
          fxHeaderCheckbox = core.dom.select('.fixedHeader').find('SPAN#' + masterCheckboxSpanID);
          fxHeaderCheckbox.toggleClass('checked').removeClass('mixed');
          
          _self.updateSelectCounter(table);
          _self.updateDeleteButton(table);
        }

        e.stopPropagation();
      });
    },

    /**
     * Update selection counter.
     *
     * @param {CoreObj} table The table to interact with.
     * @returns {String} The string with information about selected items.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    updateSelectCounter: function(table) {
      var counter = core.dom.select('DIV#selectCounter'),
          number, msg;
      
      number = table.find('TD SPAN.checkboxInput').filter('.checked').length; //Don't count master checkbox.
      
      if (number === 0) {
        msg = '';
      } else if (number === 1) {
        msg = "1 " + i18n.selection.counting.single;
      } else {
        msg = number + " " + i18n.selection.counting.multiple;
      }
      
      counter.text(msg);
      
      return msg;
    },
    
    updateDeleteButton: function(table) {
      var number;
      
      number = table.find('TD SPAN.checkboxInput').filter('.checked').length; //Don't count master checkbox.
      
      if(table.data("controls")) {
        if (number > 0) {
          table.data("controls")["delete"].show();
        } else {
          table.data("controls")["delete"].hide();
        }
      }
    },

    /**
     * @see Table.destroy
     */
    destroy: function(table, original) {
      var _self = this,
          actionDone = false;

      core.dom.each(table, function(element) {
        if (_self.has(element)) {
          _self.destroyFixedHeader(element);
          
          //Check for DataTable.
          //TODO: change it and add a bug for DataTable;
          original = (original) ? true : undefined;
          element.data('tableInstance').fnDestroy(original);
          actionDone = true;
        }
      });

      return actionDone;
    },
    
    /**
     * Destroy table's fixed header.
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} False when table has no FixedHeader, true then FixedHeder was removed.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroyFixedHeader: function(table) {
      var _self = this,
          header, headerInstance;

      headerInstance = table.data('fixedHeader');

      if (core.object.is(headerInstance, false)) {
        core.dom.select(headerInstance.fnGetSettings().aoCache[0].nNode).remove(); //fixedHeader DOM node.
        table.removeData('fixedHeader');

        return true;
      }

      return false;
    },

    /**
     * @see Table.is
     */
    has: function(selector) {
      var node = core.dom.select(selector);

      if (core.dom.is(node)) {
        //fnIsDataTable() needs DOM node (not jQuery)
        return (node.dataTable.fnIsDataTable(node[0])) ? true : false;
      }

      return false;
    },

    /**
     * Redraw the table to update it's content. (temp method - not used yet)
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} True on success or false if selector in not an instance of table.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    redraw: function(selector) {
      var _self = this;

      if(_self.has(selector)) {
        core.dom.select(selector).data('tableInstance').fnDraw();

        return true;
      }
 
      return false;
    },

    /**
     * Get Table settings. (temp method - not used yet)
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns False when there is no table or settings when table is created.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getSettings: function(selector) {
      var _self = this,
          table = core.dom.select(selector);

      if (_self.has(table)) {
        return table.data('tableInstance').fnSettings();
      }

      return false;
    },

    /**
     * @see Table.checkAll
     */
    checkAll: function(table) {
      core.dom.each(table.find('SPAN.checkboxInput').not('.checked'), function(elem) {
        radioCheck.check(elem.prev());
        elem.closest('TR').addClass('selected');
      });

      table.find('TH').eq(0).find('SPAN.checkboxInput').removeClass('mixed'); //Global action happened so there is no mixed state.
    },

    /**
     * @see Table.uncheckAll
     */
    uncheckAll: function(table) {
      core.dom.each(table.find('SPAN.checkboxInput').filter('.checked'), function(elem) {
        radioCheck.uncheck(elem.prev());
        elem.closest('TR').removeClass('selected');
      });

      table.find('TH').eq(0).find('SPAN.checkboxInput').removeClass('mixed'); //Global action happened so there is no mixed state.
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var Table = /** @lends Table */ {
    /**
     * Sort ascending.
     *
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    SORT_ASCENDING: _table.SORT_ASCENDING,

    /**
     * Sort descending.
     *
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    SORT_DESCENDING: _table.SORT_DESCENDING,

    /**
     * Check all rows.
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} True on success and false on failure.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    checkAll: function(selector) {
      return _table.checkAll(selector);
    }.defaults(''),

    /**
     * Create a table.
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @param {Object} [options] Options.
     *   @option {String} data.url="" Where should the table ask for its data?
     *   @option {Object} display={} Object that contains information about table display.
     *   @option {Number} display.length=50 How many items to display on a single table sheet. Use -1 to display all.
     *   @option {Boolean} pagination=true Enable pagination?
     *   @option {Boolean} counting=true Enable counting information? (X to Y of the Z records)
     *   @option {Boolean} sorting.enabled=true Enable sorting?
     *   @option {String} sorting.column='' The column to sort. If empty, none will be sorted by default.
     *   @option {Boolean} sorting.order=SORT_ASCENDING The order of the default column sorting. Can be {@link Table.SORT_ASCENDING} and {@link Table.SORT_DESCENDING}.
     * @returns {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>, <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(selector, options) {
      return _table.create(selector, options);
    }.defaults('', {
      data: {
        url: ""
      },
      display: {
        length: 50
      },
      counting: true,
      pagination: true,
      deletable: true,
      columnsConfig: false,
      sorting: { // !TODO: Updated API!
        enabled: true,
        column: '',
        order: _table.SORT_ASCENDING
      },
      selectable: true
    }),

    /**
     * Destroy the table.
     *
     * @param {CoreObj/Selector} table A CoreObj or selector pointing to the TABLE element.
     * @param {Boolean} original=false Do you want to remove the HTML with it?
     * @returns {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(table, original) {
      return _table.destroy(table, original);
    }.defaults('', false),
    
    /**
     * Get all selected rows. Only if options.selectable is on. Otherwise return false.
     *
     * @param {CoreObj/Selector} table A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} True on success and false on failure(or when options.selectable is not set to true).
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getSelected: function(table) {
      return _table.getSelected(table);
    }.defaults(''),

    /**
     * Check if a table is already added to the element?
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} True when there is a table and false otherwise.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    is: function(selector) {
      return _table.has(selector);
    }.defaults(''),

    /**
     * Uncheck all rows.
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the TABLE element.
     * @returns {Boolean} True on success and false on failure.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    uncheckAll: function(selector) {
      return _table.uncheckAll(selector);
    }.defaults('')
  };

  return Table;
});

core.module.alias.add('Table', 'library/uielements/table');
