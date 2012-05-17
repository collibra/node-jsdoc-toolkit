/**
 * Title Bar.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:michal@collibra.com">Michal Hans</a>
 * @module modules/content/titlebar
 * @alias TitleBar
 * @namespace modules/content
 */
/*global define,require,core */
define('modules/content/titlebar',
       ['core', 'library/uielements/dropdown'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      DropDown = require('library/uielements/dropdown');

//
  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _titleBar = {
    selectors: {
      settingsButton: '#titlebar .resource-configuration',
      settingsContainer: '.titlebar-settings',
      settingsSave: '#titlebar-settings-save',
      settingsCancel: '#titlebar-settings-cancel',
      settingsInputURI: '#titlebar-uri',
      settingsInputType: '#titlebar-type',
      settingsInputStatus: '#titlebar-status',
      settingsInputResourceId: '#titlebar-id'
    },
    values: {
      uri: '',
      concepttype: '',
      status: ''
    },

    inputs: false,

    _init: function() {
      var _self = this,
          settingsButton = core.dom.select(_self.selectors.settingsButton),
          settingsContainer = core.dom.select(_self.selectors.settingsContainer);

      // click on Settings button
      settingsButton.on('click', function(){
        _self.actions.onSettingsButtonClick(function() {
        });
      });

      //click on Settings Save
      settingsContainer.on('click', _self.selectors.settingsSave, function(){
        _self.actions.onSaveButtonClick();
      });

      //click on Settings Cancel
      settingsContainer.on('click', _self.selectors.settingsCancel, function(){
        _self.actions.onCancelButtonClick();
      });


      // store original values
      _self.inputs = core.dom.select(_self.selectors.settingsInputURI + ', ' + _self.selectors.settingsInputType + ', ' + _self.selectors.settingsInputStatus);
      _self.originalValues.set(_self.inputs);

      // subscribe to the correct events: https://docs.collibra.com/display/DEV/Mediator
      core.mediator.subscribe('uri::changed', function(data) {
        _self.actions.onURIChangedInModel(data);
      });
      core.mediator.subscribe('status::changed', function(ev) {
        _self.actions.onStatusChangedInModel(ev);
      });
      core.mediator.subscribe('concepttype::changed', function(ev) {
        _self.actions.onConceptTypeChangedInModel(ev);
      });

    },

    /**
     * Initialize Dropdown plugin for Type input
     */
    initializeTypeDropdown: function(){
      var _self = this;

      DropDown.create(_self.selectors.settingsInputType, {
        autoComplete: {
          enabled: true,
          useAjax: true
        },
        singleDeselect: false
      });
    },

    /**
     * Initialize Dropdown plugin for Status input
     */
    initializeStatusDropdown: function(){
      var _self = this;

      DropDown.create(_self.selectors.settingsInputStatus, {
        autoComplete: {
          enabled: false,
          useAjax: false
        },
        singleDeselect: false
      });
    },

    originalValues: {
      /**
       * Store original values in jQuery data for each input
       * @param {CoreObj/String} selector Inputs
       */
      set: function(selector){
        selector = core.dom.select(selector);

        core.dom.each(selector, function(element){
          element.data('originalValue', element.val());
        });

      },

      /**
       * Compare new values with original stored in jQuery data
       * @param {CoreObj/String} selector Inputs
       * @returns {Array} Array of modified inputs (CoreObj's)
       */
      compare: function(selector){
        var originalValue = false,
            newValue = false,
            modifiedInputs = false;

        core.dom.each(selector, function(element){
          originalValue = element.data('originalValue');
          newValue = element.val();

          // if value modified
          if(originalValue !== newValue) {
            if (!core.array.is(modifiedInputs)) {
              modifiedInputs = [];
            }

            modifiedInputs.push(element);
          }

        });

        return modifiedInputs;
      }
    },

    actions: {

      /**
       * Define action on Settings button clicked
       */
      onSettingsButtonClick: function(callback) {
        var _self = _titleBar;

        _self.toggleSettings(callback);
      },

      /**
       * Define action on Save button clicked
       */
      onSaveButtonClick: function(callback) {
        var _self = _titleBar;

        // check for changed inputs
        core.array.each(_self.originalValues.compare(_self.inputs), function(input) {
          switch (input.attr('name')) {
            case 'titlebar-uri':
              _self.actions.onURIChanged(input);
              break;

            case 'titlebar-type':
              _self.actions.onConceptTypeChanged(input);
              break;

            case 'titlebar-status':
              _self.actions.onStatusChanged(input);
              break;

            default:
              core.debug.show('Unrecognized input name');
              break;
          }

        });
      },

      /**
       * Define action on Cancel button clicked
       */
      onCancelButtonClick: function(callback) {
        var _self = _titleBar;

        _self.hideSettings(callback);
      },

      /**
       * Define action on URI changed
       */
      onURIChanged: function(input){
        var _self = _titleBar,
            data = false;

        // collect changed data
        data = {
          resourceID: core.dom.select(_self.selectors.settingsInputResourceId).val(),
          oldURI: input.data('originalValue'),
          URI: input.val()
        };

        core.debug.show('New URI!');
        core.debug.show(data);

        // publish changed data
        core.mediator.publish('uri::change', [data]);
      },

      /**
       * Define action on Status changed
       */
      onStatusChanged: function(input){
        var _self = _titleBar,
            data = false;

        // collect changed data
        data = {
          resourceID: core.dom.select(_self.selectors.settingsInputResourceId).val(),
          oldStatusID: input.data('originalValue'),
          oldStatusName: input.find('option[value=' + input.data('originalValue') + ']').text(),
          oldStatusURL: false,    // !TODO: Get this value
          statusID: input.val(),
          statusName: input.find('option[value=' + input.val() + ']').text(),
          statusURL: false        // !TODO: Get this value
        };

        core.debug.show('New Status!');
        core.debug.show(data);

        // publish changed data
        core.mediator.publish('status::change', [data]);
      },

      /**
       * Define action on Concept Type changed
       */
      onConceptTypeChanged: function(input){
        var _self = _titleBar,
            data = false;

        // collect changed data
        data = {
          resourceID: core.dom.select(_self.selectors.settingsInputResourceId).val(),
          oldConceptTypeID: input.data('originalValue'),
          oldConceptTypeName: input.find('option[value=' + input.data('originalValue') + ']').text(),
          oldConceptTypeURL: false,   // !TODO: Get this value
          conceptTypeID: input.val(),
          conceptTypeName: input.find('option[value=' + input.val() + ']').text(),
          conceptTypeURL: false       // !TODO: Get this value
        };

        core.debug.show('New ConceptType!');
        core.debug.show(data);

        // publish changed data
        core.mediator.publish('concepttype::change', [data]);
      },

      /**
       * Define action on URI changed in Model
       */
      onURIChangedInModel: function(data){
        var _self = _titleBar,
            input = false;

        if (data && data.URI) {
          input = core.dom.select(_self.selectors.settingsInputURI);
          if (input) {
            input.val(data.URI);
            _self.originalValues.set(input);

            return true;
          }
        }

        return false;
      },

      /**
       * Define action on Status changed in Model
       */
      onStatusChangedInModel: function(data){
        var _self = _titleBar,
            input = false;

        core.debug.show('New Status in the Model!');
        core.debug.show(data);

        if (data && data.statusID) {
          input = core.dom.select(_self.selectors.settingsInputStatus);
          if (input) {
            input.val(data.statusID);
            _self.originalValues.set(input);

            return true;
          }
        }

        return false;
      },

      /**
       * Define action on Concept type changed in Model
       */
      onConceptTypeChangedInModel: function(data){
        var _self = _titleBar,
            input = false;

        if (data && data.conceptTypeID) {
          input = core.dom.select(_self.selectors.settingsInputType);
          if (input) {
            input.val(data.conceptTypeID);
            _self.originalValues.set(input);

            return true;
          }
        }

        return false;
      }
    },

    /**
     * Toggle visibility of the settings
     */
    toggleSettings: function(callback) {
      var _self = this,
          settingsContainer = core.dom.select(_self.selectors.settingsContainer);

      if(settingsContainer.is(":visible")) {
        _self.hideSettings(settingsContainer, callback);
      } else {
        _self.showSettings(settingsContainer, callback);
      }
    },

    /**
     * Show settings
     */
    showSettings: function(settingsContainer, callback) {
      var _self = this;

      _self.initializeTypeDropdown();
      _self.initializeStatusDropdown();

      if(core.func.is(settingsContainer)){
        callback = settingsContainer;
      }
      if (!core.dom.is(settingsContainer)) {
        settingsContainer = core.dom.select(_self.selectors.settingsContainer);
      }

      settingsContainer.slideDown(200, callback);

      // change icon to red cogwheel
      core.dom.select(_self.selectors.settingsButton).find('I.icon-cogwheel').removeClass('lightGrey').addClass('red');

    },

    /**
     * Hide Settings
     */
    hideSettings: function(settingsContainer, callback) {
      var _self = this;
      
      if(core.func.is(settingsContainer)){
        callback = settingsContainer;
      }
      if (!core.dom.is(settingsContainer)) {
        settingsContainer = core.dom.select(_self.selectors.settingsContainer);
      }

      settingsContainer.slideUp(100, callback);

      // change icon to grey cogwheel
      core.dom.select(_self.selectors.settingsButton).find('I.icon-cogwheel').removeClass('red').addClass('lightGrey');
    }
  };

  core.dom.ready(function() {
    _titleBar._init();
  });

  /*************************************
   *               Public              *
   *************************************/

  var TitleBar = /** @lends TitleBar */{
    /**
     * Hide the settings bar. (Doesn't save them.)
     *
     * @param {Function} [callback] Function called when animation is complete.
     * @responsibleAPI <a>Clovis Six<a href="mailto:clovis@collibra.com">
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    hideSettings: function(callback) {
      _titleBar.hideSettings(false, callback);
    },

    /**
     * Show the settings bar.
     *
     * @param {Function} [callback] Function called when animation is complete.
     * @responsibleAPI <a>Clovis Six<a href="mailto:clovis@collibra.com">
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    showSettings: function(callback) {
      _titleBar.showSettings(false, callback);
    }
  };

  return TitleBar;
});

core.module.alias.add('TitleBar', 'modules/content/titlebar');
core.module.use('TitleBar');