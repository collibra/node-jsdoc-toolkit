/**
 * RadioCheck module for stylish radio/checboxes.
 *
 * Copyright Collibra NV/SA
 * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
 * @module library/uielements/radiocheck
 * @alias RadioCheck
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/radiocheck',
       [ 'core'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _radiocheck = {
    TYPE_CHECKBOX: 'check',
    TYPE_RADIO: 'radio',
    options: {
      generatedIdPrefix: 'radiocheck-',
      newInputIdSuffix: '-radiocheckinput',
      classes: {
        originalInput: 'hiddenInput',
        newInput: 'radioCheck',
        newInputRadio: 'radioInput',
        newInputCheckbox: 'checkboxInput',
        label: 'radioCheckLabel'
      }
    },

    /**
     * Init function.
     */
    _init: function() {
      var _self = this;

      // Bind Click only if input is not disabled
      core.dom.select("BODY").off("click", "span.radioCheck").on("click", "span.radioCheck", function(event) {
        var currentNewInput = core.dom.select(this),
            originalInput = core.dom.select('#' + currentNewInput.attr('originalId'));

        if (!currentNewInput.hasClass('disabled')) {
          // If Radio Input clear all first
          if (currentNewInput.hasClass(_self.options.classes.newInputRadio)) {
            core.dom.select('span[originalName="' + currentNewInput.attr('originalName') + '"]').removeClass('checked');
          }

          // Toggle input state
          _self.toggle(originalInput);
        }
      });
    },

    /**
     * @see RadioCheck.check
     */
    check: function(selector) {
      var _self = this,
          originalInput = false,
          counter = 0;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        if (_self.checkElement(el)) {
          counter = counter + 1;
        }
      });

      return counter ? true : false;
    },

    /**
     * @see RadioCheck.create
     */
    create: function(selector) {
      var _self = this,
          newInput = false,
          inputLabel = false,
          counter = 0;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        var originalInput = el,
            originalInputId = false;

        //if no ID generate unique one
        if (!originalInput.attr("id")) {
          core.dom.generateID(originalInput, {'prefix' : _self.options.generatedIdPrefix});
        }
        originalInputId = originalInput.attr("id");
        newInput = _self.getNewInputByOriginalId(originalInputId);

        // Inserting the new input, and hiding the original:
        if (newInput.length === 0) {
          originalInput.after('<span id="' + originalInputId + _self.options.newInputIdSuffix + '" originalId="' + originalInputId + '" originalName="' + originalInput.attr("name") + '"></span>');
          newInput = _self.getNewInputByOriginalId(originalInputId);
        }

        originalInput.addClass(_self.options.classes.originalInput);
        newInput.addClass(_self.options.classes.newInput);
        newInput.addClass(originalInput.attr("type") + 'Input');

        // Input Checked?
        if (originalInput.prop('checked')) {
          newInput.addClass('checked');
        }

        // Input Disabled?
        if (originalInput.prop('disabled')) {
          newInput.addClass('disabled');
        }

        // Adding class to the Label
        inputLabel = core.dom.select('label[for="' + originalInputId + '"]');
        if (inputLabel) {
          inputLabel.addClass(_self.options.classes.label);
        }

        counter = counter + 1;
      });

      // Listening for changes on the original and affecting the new one
      selector.unbind('change').bind('change',function(){
        _self.getNewInputByOriginalId(core.dom.select(this).attr('id')).click();
      });

      return counter ? true : false;
    },

    /**
     * @see RadioCheck.disable
     */
    disable: function(selector) {
      var _self = this,
          newInput = false,
          counter = 0;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        var originalInput = el,
            newInput = _self.getNewInputByOriginalId(originalInput.attr('id'));

        // Disable input
        if (originalInput.prop('disabled', true)) {
          newInput.addClass('disabled');
        }

        counter = counter + 1;
      });

      return counter ? true : false;
    },

    /**
     * Enable all inputs pointed by selector
     */
    enable: function(selector) {
      var _self = this,
          newInput = false,
          counter = 0;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        var originalInput = el,
            newInput = _self.getNewInputByOriginalId(originalInput.attr('id'));

        // Enable input
        if (originalInput.prop('disabled', false)) {
          newInput.removeClass('disabled');
        }

        counter = counter + 1;
      });

      return counter ? true : false;
    },

    /**
     * Check whether the current CoreObj/selector is a checkbox or radio button?
     */
    isType: function(selector, type) {
      var _self = this,
          newInput = false;

      selector = core.dom.select(selector);

      if (selector.length !== 1) {
        return false;
      }

      newInput = _self.getNewInputByOriginalId(selector.attr('id'));

      switch (type) {
        case _self.TYPE_CHECKBOX :
          return newInput.hasClass(_self.options.classes.newInputCheckbox);
        case _self.TYPE_RADIO :
          return newInput.hasClass(_self.options.classes.newInputRadio);
        default :
          return newInput.hasClass(_self.options.classes.newInput);
      }

    },

    /**
     * Toggle state of all inputs pointed by selector
     */
    toggle: function(selector) {
      var _self = this,
          counter = 0;

      selector = core.dom.select(selector).filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        var originalInput = el,
            newInput = _self.getNewInputByOriginalId(originalInput.attr('id'));

        if (newInput.hasClass('checked')){
          _self.uncheckElement(originalInput,newInput);
        } else {
          _self.checkElement(originalInput,newInput);
        }

        counter = counter + 1;
      });

      return counter ? true : false;
    },

    /**
     * @see RadioCheck.uncheck
     */
    uncheck: function(selector) {
      var _self = this,
          originalInput = false,
          counter = 0;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');

      core.dom.each(selector, function(el) {
        if (_self.uncheckElement(el)) {
          counter = counter + 1;
        }
      });

      return counter ? true : false;
    },

    /**
     * Check input
     */
    checkElement: function(originalInput, newInput) {
      var _self = this;

      if (!originalInput) {
        return false;
      }

      if (!newInput) {
       newInput = _self.getNewInputByOriginalId(originalInput.attr('id'));
      }

      // If Radio Input clear all first
      if (newInput.hasClass(_self.options.classes.newInputRadio)) {
        core.dom.select('span[originalName="' + newInput.attr('originalName') + '"]').removeClass('checked');
      }

      originalInput.prop('checked',true);
      newInput.addClass('checked').trigger("RadioCheck:change");

      return true;
    },

    /**
     * Uncheck input
     */
    uncheckElement: function(originalInput, newInput) {
      var _self = this;

      if (!originalInput) {
        return false;
      }

      if (!newInput) {
        newInput = _self.getNewInputByOriginalId(originalInput.attr('id'));
      }

      originalInput.prop('checked',false);
      newInput.removeClass('checked').trigger("RadioCheck:change");

      return true;
    },

    /**
     * Input value changed
     */
    onChange: function(selector, callback) {
      var _self = this,
          newInput = false;

      selector = core.dom.select(selector);
      selector = selector.filter('input:radio, input:checkbox');


      core.dom.each(selector, function(el) {
        newInput = _self.getNewInputByOriginalId(el.attr('id'));
        newInput.off("RadioCheck:change").on("RadioCheck:change", callback);
      });
    },

    /**
     * Get new input by id of the original input
     */
    getNewInputByOriginalId: function(originalInputId) {
      var _self = this;
      return core.dom.select('#' + originalInputId + _self.options.newInputIdSuffix);
    }
   };

  _radiocheck._init(); //Initialize

  /*************************************
   *               Public              *
   *************************************/

  var RadioCheck = /** @lends RadioCheck*/ {
    /**
     * Checkbox type.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_CHECKBOX: _radiocheck.TYPE_CHECKBOX,

    /**
     * Radio button type.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_RADIO: _radiocheck.TYPE_RADIO,

    /**
     * Check the checkbox/radio button.
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to a radio button or checkbox.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    check: function(radioCheck) {
      return _radiocheck.check(radioCheck);
    }.defaults(''),

    /**
     * Initialize radio/checkboxes.
     * For it to work properly every labels need to have a attribute linking to the ID of their corresponding checkbox/radio.</br>
     * Label should NOT contain corresponding input inside itself
     *
     * @param {CoreObj/Selector} selector Selector for (an) input(s) with type 'radio' or 'checkbox'.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    create: function(selector) {
      return _radiocheck.create(selector);
    }.defaults(''),

    /**
     * Disable radio/checkbox
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to a radio button or checkbox.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    disable: function(radioCheck) {
      return _radiocheck.disable(radioCheck);
    }.defaults(''),

    /**
     * Enable radio/checkbox.
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to a radio button or checkbox.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    enable: function(radioCheck) {
      return _radiocheck.enable(radioCheck);
    }.defaults(''),

    /**
     * Check whether the current CoreObj/selector is a checkbox or radio button?
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to a radio button or checkbox.
     * @param  {String} [type=TYPE_CHECKBOX] The type to check for. (Both if left empty.) Can be {@link RadioCheck.TYPE_CHECKBOX} and {@link RadioCheck.TYPE_RADIO}.
     * @returns {Boolean} True if the selection corresponds to the type.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    is: function(radioCheck, type) {
      return _radiocheck.isType(radioCheck, type);
    }.defaults('', _radiocheck.TYPE_CHECKBOX),

    /**
     * Check/Uncheck selected inputs
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to a radio button or checkbox.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    toggle: function(radioCheck) {
      return _radiocheck.toggle(radioCheck);
    }.defaults('', ''),


    /**
     * Uncheck radio button/checkbox.
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to (a) radio button(s) or checkbox(es).
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    uncheck: function(radioCheck) {
      return _radiocheck.uncheck(radioCheck);
    }.defaults(''),

    /**
     * Event on changed input.
     *
     * @param {CoreObj/String} radioCheck CoreObj/selector pointing to (a) radio button(s) or checkbox(es).
     * @param {Function} callback Callback function.
     * @returns {Boolean} True on success, false on failure
     * @example RadioCheck.onChange('#inputName', function(){
     *   console.log('Input changed!');
     * });
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    onChange: function(radioCheck, callback){
      return _radiocheck.onChange(radioCheck, callback);
    }
  };

  return RadioCheck;

});

core.module.alias.add('RadioCheck', 'library/uielements/radiocheck');
core.module.use('RadioCheck');
