/**
 * This is the JavaScript module for the search
 * functionality.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @module modules/navigation/search
 * @alias Search
 * @namespace modules/navigation
 */
/*global define,require,core */
define('modules/navigation/search',
       ['core', 'library/uielements/radiocheck'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      RadioCheck = require('library/uielements/radiocheck');

  /** @private */
  var $ = core.libs.get('jquery'),
      i18n = core.i18n.getTranslations('modules.navigation.search');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _search = {
    contextPath: '${contextPath}',
    allResultTypes: false,
    allResultTypesArray: [],
    visibleResultTypes: false,
    combineResultTypes: false,
    defaultAttributeTypes: ['description', 'definition'],
    searchURL: '${contextPath}rest/latest/search',
    resourceURL: '${contextPath}{resourceType}/{resourceId}',
    searchContentURL: '${contextPath}rest/latest/{resourceType}/{resourceId}',
    filteredTypesMap: {},
    filteredTypesArray: [],
    shortTypes: false,
    bar: false,
    body: false,
    container: false,
    previewContainer: false,
    resultsContainter: false,
    loader: false,
    content: false,
    input: false,
    inputs: {},
    query: false,
    minQueryLength:2,
    styleLink: false,
    scriptTag: false,
    currentItem: false,
    searchResultsJSON: false,
    searchResults: {},
    searchResultsCounter: 0,
    searchDisplayedResultsCounter: 0,
    groupedResultsMap: false,
    groupResults: false,
    pageHeightChanged: false,
    groupByAttribute: 'type',  //can be: 'attribute' or 'type'
    maxResults: 100,
    debounceTime: 400,
    lastTimeoutId: false,
    maxTerms: 25,
    closeIcon: false,
    temp: false,
    temp2: false,
    lastRequest: null,
    pauseRequests: false, //if true no request will be send
    defaultInputValue: "Search...",
    options: {
      categories: {
        "attribute": {
          active: true,    // ACTIVE  -> CHECKED html attribute
          enabled: true,   // ENABLED -> DISABLE html attribute
          show: true       // SHOW    -> VISIBLE css attribute
        },
        "name": {
          active: true,
          enabled: true,
          show: true
        },
        "term": {
          active: true,
          enabled: true,
          show: true
        },
        "vocabulary": {
          active: true,
          enabled: true,
          show: true
        },
        "community": {
          active: true,
          enabled: true,
          show: true
        }
      },
      grouping: {
        active: false,
        enabled: true,
        show: true
      }
    },

    /**
     * Constructor
     * @private
     */
    _init : function() {
      var _self = this;

      // Initialize vars.
      _self.container = core.dom.select("#searchmenu");
      _self.input = core.dom.select('#menusearchinput');
      _self.closeIcon = core.dom.select("#menusearchclose");
      _self.bar = core.dom.select("#searchmenubar");
      _self.body = core.dom.select('#searchmenucontent');
      _self.loader = _self.body.find('.loader');
      _self.content = _self.body.find('.searchcontent');
      _self.noresults = _self.body.find(".noresults");
      _self.resultsContainter = _self.body.find("#searchresults");
      _self.previewContainer = _self.body.find("#searchpreview");
      
      //Initialize inputs
      _self.inputs.group = core.dom.select('#searchmenuitemCheckbox_group');
      _self.inputs.filters = {
        "term" : core.dom.select('#filter_term'),
        "attribute" : core.dom.select('#filter_attribute'),
        "vocabulary" : core.dom.select('#filter_vocabulary'),
        "name" : core.dom.select('#filter_name'),
        "community" : core.dom.select('#filter_community'),
        "subcommunity" : core.dom.select('#filter_subcommunity')
      };

      //Style all input buttons
      RadioCheck.create('#searchmenubar input[type=checkbox]');

      //TODO: This should be generated!
      //possible full Types > short Types
      _self.allResultTypes = {
        "term" : "TE",
        "attribute" : "AT",
        "vocabulary" : "VC",
        "name" : "NA",
        "community" : "CO",
        "subcommunity" : "SC"
      };

      //possible short Types > full Types
      _self.shortTypes = {};
      core.object.each(_self.allResultTypes, function(index, value){
        _self.shortTypes[value] = index;
      });
      //store possible Types keys in Array [full Types]
      _self.allResultTypesArray = [];
      core.object.each(_self.allResultTypes, function(index, value){
        _self.allResultTypesArray.push(index);
      });
      //TODO: This should be generated!
      //which Types should be visible [short Types]
      _self.visibleResultTypes = ["TE", "VC", "CO"];

      //TODO: This should be generated!
      //which Types should be combined with parent [short Types]
      _self.combineResultTypes = ["AT", "NA"];

      //TODO: This should be generated!
      //which types should be loaded in preview for parent type   {full Type:[full Types]}
      _self.previewContentTypes = {
        "term" : ["attribute", "name"],
        "vocabulary" : ["term"],
        "community" : ["subcommunity", "vocabulary"]
      };

      //TODO: This should be generated!
      //which types should be loaded in preview for parent type   {full Type:[full Types]}
      _self.defaultAttributeTypes = ["description", "definition"];

      // SEARCH INPUT
      //Event on select the input
      _self.input.on('focus', function(el) {
        if( $(this).val() === _self.defaultInputValue ) {
          $(this).val(''); //empty input
        }
      });

      //Event on typing in input
      _self.input.on('keyup', function(e) {
        if (e.which !== 13 && e.which !== 27) {
          //Delete last search query from queue if new is ready
          if (_self.lastTimeoutId) {
            window.clearTimeout(_self.lastTimeoutId);
          }
  
          //Add new search query to queue
          _self.lastTimeoutId = window.setTimeout(function() {
            _self.search();
          }, _self.debounceTime);
        }
      });
      
      //Don't refresh on enter
      _self.input.on('keydown', function(e){
        if (e.which === 13) {
          e.preventDefault();
          return false;
        } else if (e.which === 27) {
          e.preventDefault();
          _self.hide();
          return false;
        }
      });

      //Event on unfocus of empty input
      _self.input.on('blur', function(){
        if ($(this).val().trim().length < _self.minQueryLength) {
          _self.hide();
        }
      });

      // FILTERS
      //update filters checked before init
      _self.bar.find('.searchfilter input').each(function() {
        var filter = $(this),
            filterType = filter.val(),
            filterState = filter.prop('checked') ? true : false;

        // set initial states
        _self.toggleFilter(filterType, filterState, false);
      });

      //Update filters on input change
      RadioCheck.onChange(_self.bar.find('.searchfilter input'), function(){
        var newInput = $(this),
            originalInput = $('#' + newInput.attr('originalId')),
            inputState = originalInput.prop('checked');

        _self.toggleFilter(originalInput.val(), inputState, false);
        
        if (!_self.pauseRequests) {
          _self.defaultAjaxSearch();
        }
      });

      //GROUPS
      //Event on changing Group checkbox
      RadioCheck.onChange('#searchmenuitemCheckbox_group', function(){
        var newInput = $(this),
            originalInput = $('#' + newInput.attr('originalId')),
            inputState = originalInput.prop('checked');

        _self.toggleGrouping(inputState);
        
        if (!_self.pauseRequests) {
          _self.defaultAjaxSearch();
        }
      });

      //RESULTS
      //Event on result click
      $('.searchresultitem').live('click', function() {
        _self.clickOnSearchResult( $(this) );
      });

      //CLOSE BUTTON
      _self.closeIcon.unbind('click').click( function() {
        _self.hide();
      });
    },

    /**
     * Set up search menu bar configuration
     * @param {Object} options Settings
     * @returns {Object} Settings object
     */
    setOptions: function(options) {
      var _self = this,
          currentInput = false;
      
      //Store options
      _self.options = options;
      
      //stop sending requests in a loop
      _self.pauseRequests = true;
      
      if (options) {
        //Filtering
        if (options.categories) {
          core.object.each(options.categories, function(categoryName, categoryConfig) {
            currentInput = _self.inputs.filters[categoryName];
            _self.setInputOptions(currentInput, {
              'active' : categoryConfig.active,
              'enabled' : categoryConfig.enabled,
              'show' : categoryConfig.show
            });
          });
        }
        //Grouping
        if (options.grouping) {
          _self.setInputOptions(_self.inputs.group, {
            'active' : options.grouping.active,
            'enabled' : options.grouping.enabled,
            'show' : options.grouping.show
          });
        }
      }
      
      //start sending requests normally
      _self.pauseRequests = false;
      
      return _self.options;
    },
    
    /**
     * Get search menu bar configuration
     * @returns {Object} Settings object
     */
    getOptions: function() {
      var _self = this;
      
      return _self.options;
    },
    
    /**
     * Configure options of the search bar input
     *
     * @param {CoreObj} radioCheckInput Original input styled by radioCheck plugin
     * @param {Object} options Options to set up (active, enabled, show)
     */
    setInputOptions: function(radioCheckInput, options) {
      //Grouping input checked/unchecked
      if (options.active) {
        RadioCheck.check(radioCheckInput);
      } else {
        RadioCheck.uncheck(radioCheckInput);
      }
      //Grouping input enabled/disabled
      if (options.enabled) {
        RadioCheck.enable(radioCheckInput);
      } else {
        RadioCheck.disable(radioCheckInput);
      }
      //Grouping input and label visible/hidden
      if (options.show) {
        radioCheckInput.parent().show();
      } else {
        radioCheckInput.parent().hide();
      }
    },

    /**
     * Show the search menu bar.
     *
     * @param {Number/String} speed Duration of the effect.
     * @param {Function} callback Callback function for when effect finishes.
     */
    show: function(speed, callback) {
      var _self = this;

      //first hide all other panels
      _self.hideAllPanels();

      _self.bar.slideDown(speed, function() {
        _self.content.show(speed, callback);
      });
      _self.closeIcon.fadeIn();
      _self.adjustPageHeight();
    },

    /**
     * Hide the search menu bar.
     *
     * @param {Number/String} speed Duration of the effect.
     * @param {Function} callback Callback function for when effect finishes.
     */
    hide: function(speed, callback) {
      var _self = this;
      _self.closeIcon.fadeOut();
      _self.input.val(_self.defaultInputValue);
      _self.content.slideUp(speed, function(){
        _self.bar.hide(speed, callback);
        _self.adjustPageHeight();
      });
    },

    /**
     * Hide all other panels and windows.
     */
    hideAllPanels: function() {
      //TODO: switch on after converting all global modules
      /*
      //hide all messages
      hideMessages();
      //hide all qtips
      $('.qtip').each(function() {
         $(this).qtip('hide');
      });
      //hide all contextual menus
      if (typeof(ContextualMenu) !== 'undefined') {
        ContextualMenu.close();
      }
      //hide navigator menu
      $('#navigatortrigger').OneTwoThreeNavigator('collapse');
      //hide BSGWizard and BSGFrame
      $('.BSGWizardClose:visible, .BSGFrameClose:visible').click();
      */
    },

    /**
     * Trigger search manually.
     *
     * @param {String} value String to search for.
     */
    search: function(value) {
      var _self = this;
      if (value) {
        _self.query = value.trim();
        _self.input.val(_self.query);
      } else {
        _self.query = _self.input.val().trim();
      }

      if (_self.query.length) {
        if (_self.query.length >= _self.minQueryLength) {
          if (!_self.bar.is(':visible')) {
            _search.show();
          }
          _self.defaultAjaxSearch();
        }
      }
    },

    /**
     * Generate HTML for combined children like:
     * 2 match in Attributes & 1 in Names
     *
     * @param {Object[]} children Array of children results.
     * @return {String} Generated html code.
     */
    generateCombinedChildrenHtml: function(children) {
      var _self = this,
          html = '',
          grouped = {},
          counter = 0;
      //group by type
      core.object.each(children, function(index,value){
        var child = value;
        if (!grouped[child[_self.groupByAttribute]]) {
          grouped[child[_self.groupByAttribute]] = [];
        }
        grouped[child[_self.groupByAttribute]].push(child[_self.groupByAttribute]);
      });
      //count children in each types
      core.object.each(grouped, function(index,value){
          var group = value;
          if (counter) {
            html += " " + i18n.search.and + " ";
          }
          html += "<span>"+group.length;
          if (!counter) {
            if ( group.length > 1 ) {
              html += " " + i18n.search.matches + " ";
            } else {
              html += " " + i18n.search.match + " ";
            }
          }
          html += " " + i18n.search.inside + " " + i18n.search.category.plural[ _self.shortTypes[ group[0] ] ] + " </span>";
          counter = counter + 1;
      });

      return html;
    },

    /**
     * Reset previous results
     */
    resetResults: function() {
      var _self = this;
      _self.searchResultsCounter=0;
      _self.searchResultsJSON = [];
      _self.searchResults = {};
      _self.resultsContainter.html('');
      _self.noresults.html(i18n.search.noresults + '<i>' + _self.query + '</i>').hide();
      _self.preview.showPreview(false,true);
      _self.groupedResultsMap = {};
    },

    /**
     * Generate HTML for result box.
     *
     * @param {String} type Type of resource.
     * @param {String} resourceId Resource id.
     * @param {String} title Title of resource.
     * @param {String} parent Parent resource title.
     * @param {Object[]} children Children objects.
     * @param {String} score Score of the result.
     * @return {String} Html code for result box.
     */
    generateResultBox: function(type, resourceId, title, parent, children, score) {
      var _self = this,
          html = '';

      html += "<div class='searchresultitem' id='" + resourceId + "'>";
      if (type && !_self.groupResults) {
        html += "<div class='icon " + type + "'></div>";
      }
      html += "<div class='resulttitle'><a href='" + _self.generateResourceURL(_self.shortTypes[type],resourceId) + "'>" + title + "</a></div>";
      if (parent) {
        html += "<div class='parentresult'>" + parent + "</div>";
      }
      if (children) {
        html += "<div class='childrenresults'>"+ _self.generateCombinedChildrenHtml(children) +"</div>";
      }
      if (score) {
        html += "<div class='score'>"+ score +"</div>";
      }
      html += "</div>";

      return html;
    },
    
    generateResourceURL: function(resourceType, resourceId) {
      var _self = this,
          currentURL = '';
      
      if (!core.string.is(resourceType)) {
        resourceType = resourceType.toString();
      }
      if (!core.string.is(resourceId)) {
        resourceId = resourceId.toString();
      }
      
      currentURL = _self.resourceURL.replace('{resourceType}',resourceType);
      currentURL = currentURL.replace('{resourceId}',resourceId);
      
      return currentURL;
    },


    /**
     * Generate HTML for multiple result boxes.
     *
     * @return Html code for result boxes.
     */
    generateResultBoxes: function() {
      var _self = this,
          html = '',
          resourceId = false,
          result = false,
          score = 0,
          counter = 0,
          parentTitle = '',
          children = false,
          i=0,
          j=0;

      _self.searchDisplayedResultsCounter = 0;

      if (_self.searchResults && _self.searchResultsCounter){
        if (_self.groupResults) {
          //results must be grouped
          //for each group
          core.object.each(_self.allResultTypesArray, function(j,val) {
            var groupLong = val;
            var group = _self.allResultTypes[groupLong];
            if (_self.groupedResultsMap[group]) {
              html += '<div class="resultsgroup">';
              html += '<div class="resultsgrouptitle"><div class="icon ' + group +'"></div>' + i18n.search.category.plural[_self.shortTypes[group]] + '</div>';
              //show all results in each group
              core.object.each(_self.groupedResultsMap[group], function(i,val) {
                resourceId = val;
                result = _self.searchResults[resourceId];
                if (result) {
                  parentTitle = '';
                  children = false;
                  if (result.parent) {
                    parentTitle = result.parent.name;
                  }
                  if (result.children) {
                    children = result.children;
                  }
                  html = html + _self.generateResultBox(result.type, result.resourceId, result.name, parentTitle, children, result.score);
                  _self.searchDisplayedResultsCounter = _self.searchDisplayedResultsCounter + 1;
                }
              });
              html += '</div>';
            }
          });
        } else {
          //results dont must be grouped
          //show all results
          counter = 0;
            core.object.each(_self.searchResults, function(index, val) {
              result = val;
              if (result) {
                parentTitle = '';
                children = false;
                if (result.parent) {
                  parentTitle = result.parent.name;
                }
                if (result.children) {
                  children = result.children;
                }
                html = html + _self.generateResultBox(result.type, result.resourceId, result.name, parentTitle, children, result.score);
                _self.searchDisplayedResultsCounter = _self.searchDisplayedResultsCounter + 1;
              }
              counter = counter + 1;
            });
        }
      }
      return html;
    },

    /**
     * Append results to page.
     */
    showResults: function() {
      var _self = this;
      if  (_self.searchResultsCounter) {
         _self.resultsContainter.html( _self.generateResultBoxes(_self.searchResults) );
        _self.preview.showPreview();
      } else {
        _self.noresults.fadeIn();
        _self.searchDisplayedResultsCounter = 0;
      }
      _self.adjustPageHeight();

      //update results counter
      _self.bar.find('div.label .counter').text(_self.searchDisplayedResultsCounter);
    },

     /**
     * Action after click on search result.
     *
     * @param {CoreObj} object jQuery element.
     */
    clickOnSearchResult: function(object) {
      var _self = this;

      if(!object.hasClass("selected")){
        _self.preview.showPreview(object.attr('id'));
      }
    },

     /**
     * Show / hide loader.
     *
     * @param {Boolean} show Show or hide loader.
     */
    showLoader: function(show) {
      var _self = this;
      if (show) {
        _self.resultsContainter.html('<div id="searchloaderimage"></div>');
      } else {
        _self.resultsContainter.find('#searchloaderimage').remove();
      }
    },

     /**
     * Count height of the container to overlap page content.
     */
    adjustPageHeight: function() {
      var _self = this,
          extraMargin = 80;

      //adjust height of the page
      var sH = _self.content.outerHeight(),
          pH = $("#contentcolumn").outerHeight();

      if (sH > pH) {
        $("#contentwrapper").height(sH+extraMargin+"px");
        _self.pageHeightChanged = true;
      } else {
        _self.content.css('min-height', $("#contentcolumn").outerHeight()+'px');
        if (_self.pageHeightChanged){
          $("#contentwrapper").height("auto");
          _self.pageHeightChanged = false;
        }
      }
    },

    /**
     * Make default ajax search call.
     *
     * @param {String} [query] Custom query.
     */
    defaultAjaxSearch: function(query) {
      var _self = this;
      if (query) {
        _self.query = query;
      }
      if (_self.filteredTypesArray.length > 0) {
        //always add wildcard * at the end of the query
        if (_self.query.charAt(_self.query.length-1) !== '*') {
          _self.query = _self.query + '*';
        }
        _self.ajaxSearch(_self.query, _self.maxResults, _self.filteredTypesArray, function(){_self.showResults();});
      } else {
        _self.resultsContainter.html('');
        _self.preview.showPreview(false,true);
        _self.noresults.text(i18n.search.nocategories).show();
      }
    },

    /**
     * Action after click on search result.
     *
     * @param {String} query Query to search.
     * @param {String} max Max number of results.
     * @param {String[]} typesArray Array with types.
     * @param {Function} [callback] Callback function for when effect finishes.
     */
    ajaxSearch: function(query, max, typesArray, callback) {
      var _self = this,
          dataMap = { 'media': 'json'};
      if (query) {
        dataMap.query = query;
      }
      if (max) {
        dataMap.max = max;
      }
      if (typesArray) {
        dataMap.types = typesArray.join(",");
      }

      ////Select in backend via json

      $.ajax({
        url: _self.searchURL,
        data: dataMap,
        type: 'GET',
        dataType: 'json',
        accept: 'application/json',
        beforeSend: function(request){
          _self.showLoader(true);
        },
        complete: function(){
          _self.showLoader(false);
        },
        success: function(data) {
          _self.resetResults();
          if (data && data.searchResultItem) {
            _self.searchResultsJSON = data.searchResultItem;
            _self.combineResults();

          } else {
            _self.searchResultsJSON = false;
          }

          if (typeof callback === 'function') {
            callback();
          }
        },
        error: function(a, b, c) {
          _self.noresults.text('Server Error: ' + c).show();
        }
      });

      _self.resetResults();
    },

     /**
     * Combine search results by type.
     * Combined types should be defined in combineResultTypes array.
     *
     * @returns Counter of combined elements.
     */
    combineResults: function() {
      var _self = this,
          result = false,
          combinedCounter = 0,
          child = false;
      if(_self.searchResultsJSON) {
        //loop ofer each result
        core.object.each(_self.searchResultsJSON, function(index,value) {
          result = _self.searchResultsJSON[index];
          //check if type must be combined
          if ($.inArray(result.type, _self.combineResultTypes) >= 0) {
            //strip html tags from value
            result.name = _self.tools.stripHtml(result.name);
            //agregate only if still match (after stripping html tags)
            if (_self.tools.searchPattern(result.name) >= 0) {
              //add parent as a result
              //but first check if parent already exist
              if(!_self.searchResults[result.parent.resourceId]) {
                //if no score assign score of the child
                if(!result.parent.score) {
                  result.parent.score = result.score;
                }

                _self.searchResults[result.parent.resourceId] = result.parent;
                //group result
                _self.addResultToGroup(result.parent.resourceId, result.parent.type);
              }
              _self.searchResultsCounter = _self.searchResultsCounter + 1;

              //add combined result as a child for added parent
              //but first check if children array already exist
              if (!_self.searchResults[result.parent.resourceId].children) {
                _self.searchResults[result.parent.resourceId].children = [];
              }
              //save children
              _self.searchResults[result.parent.resourceId].children.push( {
                'resourceId': result.resourceId,
                'value': result.name,
                'type': result.type,
                'score': result.score,
                'attribute': result.attribute
              } );
              combinedCounter = combinedCounter + 1;
            }
          //type don't must be combined
          } else {
            //don't overwrite result if already exists
            if (!_self.searchResults[result.resourceId]) {
              _self.searchResults[result.resourceId] = result;
            }
            _self.searchResultsCounter = _self.searchResultsCounter + 1;

            //group result
            _self.addResultToGroup(result.resourceId, result.type);
          }

        });
        return combinedCounter;
      }else{
        return false;
      }
    },

    /**
     * Add result to group.
     *
     * @param {String} resourceId Resource id.
     * @param {String} type Type of result.
     */
    addResultToGroup: function(resourceId, type){
      var _self = this;
      if (!_self.groupResults) {
        return false;
      }
      //if group doesnt exist than creeate it
      if (!_self.groupedResultsMap[type]) {
        _self.groupedResultsMap[type] = [];
      }
      //add resource id to group if is new one
      if ($.inArray(resourceId, _self.groupedResultsMap[type])<0) {
        _self.groupedResultsMap[type].push(resourceId);
      }
    },

    /**
     * Toggle grouping results.
     *
     * @param {Boolean} enable Enable grouping?
     */
    toggleGrouping: function(enable){
      var _self = this;
      if (enable) {
        _self.groupResults = true;
      } else {
        _self.groupResults = false;
      }
      
      _self.options.grouping.active = _self.groupResults;
    },

     /**
     * Show the search menu bar.
     *
     * @param {String} filterType Type of filtered results.
     * @param {Boolean} switchOn Switch filter on or off.
     * @param {Boolean} refresh Refresh results after that.
     */
    toggleFilter: function(filterType, switchOn, refresh){
      var _self = this;
      //set filter
      if (switchOn) {
        _self.filteredTypesMap[filterType] = _self.allResultTypes[filterType];
      } else {
        _self.filteredTypesMap[filterType] = null;
      }

      //rebuild filterArray after filterMap updated
      _self.filteredTypesArray = [];
      for (var i=0; i < _self.allResultTypesArray.length; i = i + 1) {
        if (_self.filteredTypesMap[_self.allResultTypesArray[i]]) {
          _self.filteredTypesArray.push( _self.allResultTypes[ _self.allResultTypesArray[i] ] );
        }
      }

      //refresh results
      if (refresh){
        if (_self.filteredTypesArray.length > 0) {
          _self.defaultAjaxSearch();
        }else{
          _self.resultsContainter.html('');
          _self.preview.showPreview(false,true);
          _self.noresults.text(i18n.search.nocategories).show();
        }
      }

    },

    /**
     * Text tools
     */
    tools: {
      /**
       * Strip HTML tags.
       *
       * @param {String} OriginalString Content with HTML tags.
       */
      stripHtml: function(OriginalString) {
        return OriginalString.replace(/(<([^>]+)>)/ig,"");
      },

      /**
       * Search for pattern in a string.
       *
       * @param {String} OriginalString Content.
       * @param {RegEx} pattern Pattern to search for.
       */
      searchPattern: function(OriginalString,pattern) {
        var _self = _search;
        if(!pattern) {
          pattern = _self.query;
        }
        var specials = new RegExp("[.*+?|()\\[\\]{}\\\\]", "g");
        pattern = pattern.replace(specials, "\\$&");
        return OriginalString.search(new RegExp( pattern , 'gi' ));
      },

      /**
       * Highlight searched pattern.
       *
       * @param {String} OriginalString Content.
       * @param {RegEx} pattern Pattern to search for.
       */
      highlightPattern: function(OriginalString, pattern){
        var _self = _search;
        if(!pattern) {
          pattern = _self.query;
        }
        var specials = new RegExp("[.*+?|()\\[\\]{}\\\\]", "g");
        pattern = pattern.replace(specials, "\\$&");
        return OriginalString.replace(new RegExp( pattern , 'gi' ), function(matched) {return "<span class=\"highlight\">" + matched + "</span>";} );
      }
    },

    /**
     * Preview functionality.
     */
    preview: {
      /**
       * Load preview content.
       *
       * @param {String} resourceId Resource ID.
       * @param {String} type Type of content (ex: vocabulary,term,attribute).
       * @param {String} option Additional type of content (ex: description,definition).
       * @param {Function} callback Callback function for when effect finishes.
       */
      loadPreviewContent: function(resourceId, type, option, callback) {
        var _self = _search,
            dataMap = { 'xpage': 'plain'},
            callBack = false,
            callbackType = false;
        if (resourceId) {
          dataMap.resourceId = resourceId;
        }
        if (type) {
          dataMap.type = type;
        }
        if (option) {
          dataMap.attributetype = option;
        }

        if (option) {
          callbackType = option;
        } else {
          callbackType = type;
        }
        if (dataMap.type === 'attribute' && !dataMap.attributetype) {
          return false;
        }

        ////Select in backend via json
        /*
        $.ajax({
          url: _self.searchContentURL,
          data: dataMap,
          dataType: 'json',
          type: 'POST',
          success: function(data) {
            if (data) {
              if(data.type) {
                callbackType = data.type;
              }
              if (typeof callback === 'function') {
                return callback(data.data, callbackType);
              }
            }
          },
          error: function(a, b, c) {
          }
        });
        */
      },

      /**
       * Show preview window (or hide).
       *
       * @param {String} resourceId Resource ID.
       * @param {Boolean } hide Hide preview?
       */
      showPreview: function(resourceId, hide) {
        var _self = _search,
            el = false,
            position = false;

        //if no resource id than show the first one
        if (!resourceId) {
          resourceId = $('.searchresultitem:first').attr('id');
        }

        //if no results hide preview
        if (!resourceId) {
          hide = true;
        }

        if (!hide) {
          el = _self.searchResults[resourceId];
          position = _self.resultsContainter.find('div[id="'+resourceId+'"]').position().top;
          _self.previewContainer.find('.previewcontent').html('');
          if(el && el.type){
            //render preview with breadcrumbs
            _self.preview.showPreviewHeader(resourceId);

            //load preview content depend on the configuration
            var fullname = _self.shortTypes[el.type];
            if (_self.previewContentTypes[fullname]) {
              core.object.each(_self.previewContentTypes[fullname], function(t,val) {
                var contentType = t;
                //for Attributes
                if (contentType === 'attribute') {
                  _self.preview.showPreviewTermAttributes(resourceId);
                } else {
                  _self.preview.showPreviewSectionList(resourceId, el.type, contentType, i18n.search.category.plural[contentType], contentType);
                }
              });
            }
          }

          _self.previewContainer.fadeIn("slow", function(){
            _self.body.find('.rightcolumn').animate({"top":position+'px'});
            //deselect other Objects
            _self.content.find('.selected').removeClass("selected");
            //select Object
            _self.content.find('.searchresultitem[id="' + resourceId + '"]').addClass("selected");
          });
        } else {
          _self.previewContainer.hide();
        }
      },

       /**
       * Show preview icon, title and breadcrumbs.
       *
       * @param {String} resourceId Resource ID.
       */
      showPreviewHeader: function(resourceId) {
        var _self = _search,
            breadcrumbs = '',
            el = _self.searchResults[resourceId],
            title = '';

        if (el) {
          //title
          title = '<a href="' + _self.generateResourceURL(_self.shortTypes[el.type] ,resourceId) +'">' + _self.tools.stripHtml(el.name) + '</a>';
          _self.previewContainer.find('.previewtitle').html(title);

          //icon
          _self.previewContainer.find('.previewicon div').attr('class','icon '+el.type);

          //breadcrumbs
          var parent = el.parent,
              first = true;
          while (parent) {
            //_self.previewContainer.find('.breadcrumbs')
            if (first) {
              breadcrumbs += '<span class="separator-first" ></span><a href="' + _self.generateResourceURL(_self.shortTypes[parent.type], parent.resourceId) +'">' + _self.tools.stripHtml(parent.name) + '</a>';
              first = false;
            } else {
              breadcrumbs += '<span class="separator" ></span><a href="' + _self.generateResourceURL(_self.shortTypes[parent.type], parent.resourceId) +'">' + _self.tools.stripHtml(parent.name) + '</a>';
            }
            parent = parent.parent;
          }
          _self.previewContainer.find('.breadcrumbs').html(breadcrumbs);

          // set type
          _self.previewContainer.find('.previewmain').attr('resourceType',_self.shortTypes[el.type]);
        }
      },

      /**
       * Show preview attributes for term.
       *
       * @param {String} resourceId Resource ID.
       */
      showPreviewTermAttributes: function(resourceId) {
        var _self = _search,
            el = _self.searchResults[resourceId],
            html = '',
            counter = 0,
            signsBefore = 140,
            signsAfter = 140,
            subStart = 0,
            subEnd = 0,
            i = 0,
            sectionExists = false,
            patternPosition = false,
            subPreviewContent = false,
            previewContent = false,
            thisAttributeTitle = false;

        if (el) {
          //if element have combined children
          if (el.children) {
            html += '<div class="childsection attributes">';
            html += '<div class="childsectiontitle">' + i18n.search.category.attribute + " " + i18n.search.matches + '</div>';
            for (i = 0; i < el.children.length; i = i + 1) {
              if (el.children[i].type === _self.allResultTypes.attribute) {
                subStart = 0;
                subEnd = 0;
                previewContent = el.children[i].name;
                previewContent = _self.tools.stripHtml(previewContent);
                patternPosition = _self.tools.searchPattern(previewContent);

                if (patternPosition-signsBefore > 0) {
                  subStart = patternPosition-signsBefore;
                }

                if (patternPosition+signsAfter > previewContent.length) {
                  subEnd = previewContent.length;
                } else {
                  subEnd = patternPosition+signsAfter;
                }

                subPreviewContent = previewContent.substring(subStart,subEnd);
                if (subStart > 0) {
                  subPreviewContent = '...' + subPreviewContent;
                }
                if (subEnd < previewContent.length) {
                  subPreviewContent = subPreviewContent + '...';
                }

                subPreviewContent = _self.tools.highlightPattern(subPreviewContent);

                thisAttributeTitle = el.children[i].attribute;
                if (i18n.search.category[thisAttributeTitle.toLowerCase()]) {
                  thisAttributeTitle = i18n.search.category[thisAttributeTitle.toLowerCase()];
                }

                html += '<div class="attributeitem">';
                html += '<div class="attributeitemtile">' + thisAttributeTitle + '</div>';
                html += '<div class="attributeitemvalue">' + subPreviewContent + '</div>';
                html += '</div>';
                counter = counter + 1;
              }
            }
            html += '</div>';
            if (counter && ('term' === _self.previewContainer.find('.previewmain').attr('resourceType'))) {
              _self.previewContainer.find('.previewcontent').prepend(html);
            }

          //if element dont have combined children get default attributes
          } else {
            //foreach default attribute
            core.object.each(_self.defaultAttributeTypes, function(a, value){
              _self.temp2 = _self.preview.loadPreviewContent(resourceId, 'attribute', _self.defaultAttributeTypes[a], function(data, att){
                  html = '';
                  sectionExists = _self.previewContainer.find('.previewcontent .childsection.attributes').length;
                  //add title only for first attribute
                  if ( !sectionExists ) {
                    html += '<div class="childsection attributes">';
                    html += '<div class="childsectiontitle">' + i18n.search.category.plural.attribute + '</div>';
                  }
                  if (data) {
                    for (i=0; i<data.length; i = i + 1) {
                      html += '<div class="attributeitem">';
                      if (data[i].name) {
                        html += '<div class="attributeitemtile">' + data[i].name + '</div>';
                      }
                      if (data[i].name) {
                        previewContent = _self.tools.stripHtml(data[i].name);
                        subPreviewContent = previewContent.substring(0,(signsBefore + signsAfter));
                        if (previewContent.length > (signsBefore + signsAfter)) {
                          subPreviewContent = subPreviewContent + '...';
                        }
                        html += '<div class="attributeitemvalue">' + subPreviewContent + '</div>';
                      }
                      html += '</div>';
                      counter = counter + 1;
                    }
                  } else {
                    html += '<div class="attributeitem">';
                    html += '<div class="attributeitemtile">' + i18n.search.category[att] + '</div>';
                    html += '<div class="attributeitemvalue">' + i18n.search.no + ' ' + i18n.search.category.plural[att].toLowerCase() + ' ' + i18n.search.addedyet +'</div>';
                    html += '</div>';
                    counter = counter + 1;
                  }
                  if ( !sectionExists ) {
                    html += '</div>';
                  }
                  if (counter && ('term' === _self.previewContainer.find('.previewmain').attr('resourceType'))) {
                    if ( sectionExists ) {
                      _self.previewContainer.find('.previewcontent .childsection.attributes').append(html);
                    } else {
                      _self.previewContainer.find('.previewcontent').prepend(html);
                    }
                  }
              });
            });
          }
        }
      },

      /**
       * Show list of elements (for each TYPE show VALUE).
       *
       * @param {String} resourceId Resource ID.
       * @param {string} type Type of elements.
       * @param {String} title Resource title.
       * @param {String} className Class name.
       */
      showPreviewSectionList: function(resourceId, type, contentType, title, className) {
        var _self = _search,
            el = _self.searchResults[resourceId],
            html = '',
            counter = 0,
            separator = '',
            i = 0;

        if (el) {
          //if element have combined children with wanted typer
          var childType = (function(){
            if(!el.children) {
              return false;
            }
            for (i = 0; i < el.children.length; i = i + 1) {
              if (el.children[i].type === _self.allResultTypes[contentType]) {
                return _self.allResultTypes[contentType];
              }
            }
            return false;
          }());

          if (el.children && childType) {
            separator = ', ';
            html += '<div class="childsection ' + className +'">';
            html += '<div class="childsectiontitle">' + title + '</div>';
            html += ' <ul class="listvalues">';
            core.object.each(el.children, function(i,value){
              if (el.children[i].type === childType) {
                var previewContent = el.children[i].name;
                previewContent = _self.tools.stripHtml(previewContent);
                previewContent = _self.tools.highlightPattern(previewContent);

                html += '<li class="listvalue">';
                if (counter) {
                  html += separator;
                }
                html += '<a href="' + _self.generateResourceURL(contentType, el.children[i].resourceId) +'">' + previewContent + '</a></li>';
                html += '</li>';
                counter = counter + 1;
              }
            });
            html += ' </ul>';
            html += '</div>';
            if (counter && (_self.shortTypes[type] === _self.previewContainer.find('.previewmain').attr('resourceType'))){
              //remove if preview for this type is already loaded
              if (_self.previewContainer.find('.previewcontent .'+className)) {
                _self.previewContainer.find('.previewcontent .'+className).remove();
              }
              _self.previewContainer.find('.previewcontent').append(html);
            }

          //if element dont have combined children load by ajax
          } else if (contentType) {
            _self.temp2 = _self.preview.loadPreviewContent(resourceId, contentType, false, function(data,att){
                var html = '',
                    counter = 0;
                html += '<div class="childsection ' + className +'">';
                html += '<div class="childsectiontitle">' + title + '</div>';
                if (data) {
                  separator = ', ';
                  html += ' <ul class="listvalues">';
                  for (var i=0; i<data.length; i = i + 1) {
                    html += '<li class="listvalue">';
                    if (i) {
                      html += separator;
                    }
                    if (i < (_self.maxTerms)) {
                      html += '<a href="' + _self.generateResourceURL(contentType, data[i].resourceId) + '">' + data[i].name + '</a></li>';
                    } else {
                      html += '...';
                    }
                    counter = counter + 1;
                  }
                  html += ' </ul>';
                } else {
                  html += '<div class="attributeitem">';
                  html += '<div class="attributeitemvalue">' + i18n.search.no + ' ' + i18n.search.category.plural[att].toLowerCase() + ' ' + i18n.search.addedyet +'</div>';
                  html += '</div>';
                  counter = counter + 1;
                }
                html += '</div>';
                if (counter && (_self.shortTypes[type] === _self.previewContainer.find('.previewmain').attr('resourceType'))){
                  //remove if preview for this type is already loaded
                  if (_self.previewContainer.find('.previewcontent .'+className)) {
                    _self.previewContainer.find('.previewcontent .'+className).remove();
                  }

                  _self.previewContainer.find('.previewcontent').append(html);
                }
            });
          }
        }
      }
    }

  };

  _search._init();

  /*************************************
   *               Public              *
   *************************************/

  var Search = /** @lends Search */ {
    /**
     * Get options of the search functionality.
     * @return {Object} Options object formatted as follows:
     * <pre>
     * {
     *   categories: {
     *     "&lt;Category ID&gt;": {
     *       enabled,
     *       show,
     *       active
     *     }
     *   },
     *   grouping: {
     *     active,
     *     enabled,
     *     show
     *   }
     * }
     * </pre>
     */
    getOptions: function() {
      return _search.getOptions();
    },

    /**
     * Hides search bar and results.
     *
     * @param {Function} [callback] Function executed when hide is complete.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    hide: function(callback) {
      _search.hide();
    },

    /**
     * Reset the search. (Empty search field and hide.)
     *
     * @param {Function} [callback] Function executed on finish.
     * @return {Boolean} True on success, false on failure.
     */
    reset: function(callback) {
      // !TODO: Implement function.
    },

    /**
     * Search for a keyword. Also open search bar and results, fill input with given query.
     * @param {String} query The text to search for. This text can contain the following special characters: !TODO: Complete...
     * @param {Object} [options] Options for the search.
     * @option {String} categories.(attribute,name,term,vocabulary,community).label The label of a particular category.
     * @option {Boolean} categories.(category).enabled=true Whether the category can be selected or not.
     * @option {Boolean} categories.(category).show=true Is this category shown as an option?
     * @option {Boolean} categories.(category).active=true Whether this category is taken up in the results or not.
     * @option {Boolean} categories.enabled=true Whether you want to divide into categories.
     * @option {Boolean} grouping.show=false Is the grouping option actually shown
     * @option {Boolean} grouping.enabled=true Enable/disable grouping option.Whether it can be clicked on etc.
     * @option {Boolean} grouping.active=true Combine search results into groups.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    search: function(query, options) {
      _search.search(query, options);
    }.defaults('', {
      categories: {
        "attribute": {
          active: true,    // ACTIVE  -> CHECKED html attribute
          enabled: true,   // ENABLED -> DISABLE html attribute
          show: true       // SHOW    -> VISIBLE css attribute
        },
        "name": {
          active: true,
          enabled: true,
          show: true
        },
        "term": {
          active: true,
          enabled: true,
          show: true
        },
        "vocabulary": {
          active: true,
          enabled: true,
          show: true
        },
        "community": {
          active: true,
          enabled: true,
          show: true
        }
      },
      grouping: {
        active: false,
        enabled: true,
        show: true
      }
    }),

    /**
     * Set options for the current search functionality.
     * @param {Object} [options] The options to set. Same options as in {@link Search.search}.
     */
    setOptions: function(options) {
      _search.setOptions(options);
    }.defaults({
      categories: {
        "attribute": {
          active: true,    // ACTIVE  -> CHECKED html attribute
          enabled: true,   // ENABLED -> DISABLE html attribute
          show: true       // SHOW    -> VISIBLE css attribute
        },
        "name": {
          active: true,
          enabled: true,
          show: true
        },
        "term": {
          active: true,
          enabled: true,
          show: true
        },
        "vocabulary": {
          active: true,
          enabled: true,
          show: true
        },
        "community": {
          active: true,
          enabled: true,
          show: true
        }
      },
      grouping: {
        active: false,
        enabled: true,
        show: true
      }
    }),

    /**
     * Shows search bar and results.
     *
     * @param {Function} [callback] Function executed when show is complete.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    show: function(callback) {
      _search.show();
    }.defaults()
  };

  return Search;
});

core.module.alias.add('Search', 'modules/navigation/search');
core.module.use('Search');