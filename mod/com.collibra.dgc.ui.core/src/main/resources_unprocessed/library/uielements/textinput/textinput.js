/**
 * Style and modify simple text inputs.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module library/uielements/textinput
 * @alias TextInput
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/textinput',
       ['core'],
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
  var _textinput = {
    TYPE_BIG: 0,
    TYPE_NORMAL: 1,
    TYPE_MULTI: 2,
    TYPE_SMALL: 3,
    placeholder: false,

    /**
     * @see TextInput.create
     */
    create: function(input, type, options){
      var _self = this;

      //check if placeholder tag is supported
      if (!_self.placeholder && !core.detect.input.PLACEHOLDER) {
        _self.placeholderFix();
        _self.placeholder = true;
      }

      switch(type) {
        case _self.TYPE_NORMAL:
          _self.normal(input, options);
          break;
        case _self.TYPE_SMALL:
          _self.small(input, options);
          break;
        case _self.TYPE_MULTI:
          _self.multi(input, options);
          break;
        default:
          _self.big(input, options);
      }
    },

    /**
     * @see TextInput.big
     */
    big: function(item, options) {
      var _self = this,
          inputID,
          label;

      core.dom.each(item, function(input) {
        _self.normal(input, options);

        inputID = input.attr('id');

        input.wrapAll('<div class="big-wrapper" id="' + inputID + '-wrapper" inputid="' + inputID + '" />').addClass('big');

        if (options.focus) {
          _self.focus(input); //focus after wrapping
        }
      });
    },

    /**
     * @see TextInput.destroy
     */
    destroy: function(item, original) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        _self.setPlaceholder(input, false); //Remove label.

        if (input.hasClass('big')) {
          input.unwrap();
        }

        if (original) {
          input.remove();
        } else {
          if (_self.isDisabled(input)) {
            _self.enable(input);
          }

          input.removeClass('big small multi');
        }

        return true;
      }

      return false;
    },

    /**
     * This code fixes html5 placeholder tag for browsers that don't support it.
     */
    placeholderFix: function() {
      var active = document.activeElement;

      core.dom.select('input, textarea').focus(function () {
        var element = core.dom.select(this);

        if (element.attr('placeholder') !== '' && element.val() === element.attr('placeholder')) {
          element.val('').removeClass('hasPlaceholder');
        }
      }).blur(function () {
        var element = core.dom.select(this);

        if (element.attr('placeholder') !== '' && (element.val() === '' || element.val() === element.attr('placeholder'))) {
          element.val(element.attr('placeholder')).addClass('hasPlaceholder');
        }
      });

      core.dom.select('input, textarea').blur();
      core.dom.select(active).focus();
      core.dom.select('form').submit(function () {
        core.dom.select(this).find('.hasPlaceholder').each(function() {
          core.dom.select(this).val('');
          });
      });
    },

    /**
     * @see TextInput.normal
     */
    normal: function(item, options) {
      var _self = this,
          label, inputID, inputval;

      core.dom.each(item, function(input) {
        inputID = input.attr('id') || core.dom.generateID(input)[0];

        _self.destroy(input, false);

        if (options.disable) {
          _self.disable(input);
        }

        if (options.value) {
          _self.setValue(input, options.value);
        }

        if (options.label) {
          _self.setPlaceholder(input, options.label);
        }

        input.focus(function() {
          // We need only to add class in order to change display.
          input.addClass('focus');
        }).focusout(function() {
          input.removeClass('focus');
        });

        if (options.focus) {
          _self.focus(input);
        }
      });
    },

    /**
     * @see TextInput.small
     */
    small: function(item, options) {
      var _self = this;

      core.dom.each(item, function(input) {
        _self.normal(input, options);

        input.addClass('small');
      });
    },

    /**
     * @see TextInput.multi
     */
    multi: function(item, options) {
      var _self = this,
          input = core.dom.select(item);

      core.dom.each(item, function(input) {
        if(input.is('textarea')) {
          _self.normal(input, options);
  
          input.addClass('multi');
        }
      });
    },

    /**
     * @see TextInput.disable
     */
    disable: function(item) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        input.attr('disabled', 'disabled').addClass('disabled');

        return true;
      }

      return false;
    },

    /**
     * @see TextInput.enable
     */
    enable: function(item) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        input.removeAttr('disabled').removeClass('disabled');

        return true;
      }

      return false;
    },

    /**
     * @see TextInput.focus
     */
    focus: function(item) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        input.select().focus().addClass('focus');

        return input;
      }

      return false;
    },

    /**
     * @see TextInput.getPlaceholder
     */
    getPlaceholder: function(item) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        return input.attr('placeholder');
      }

      return false;
    },

    /**
     * @see TextInput.getValue
     */
    getValue: function(item) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {
        return input.val();
      }

      return false;
    },

    /**
     * @see TextInput.setPlaceholder
     */
    setPlaceholder: function(item, label) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input)) {

        if (!label) {
          input.removeAttr('placeholder');
        } else if (core.string.is(label)) {
          // !TODO: escape label and update UnitTest
          input.attr('placeholder', label);
        }

        return true;
      }

      return false;
    },

    /**
     * @see TextInput.setValue
     */
    setValue: function(item, value) {
      var _self = this,
          input = core.dom.select(item);

      if (core.dom.is(input) && core.string.is(value)) {
        if (value !== '') {
          input.prev('label').hide();
        }

        input.val(value);
        return true;
      }

      return false;
    },

    /**
     * @see TextInput.is
     */
    is: function(input, type){
      var _self = this;

      switch(type) {
        case _self.TYPE_BIG:
          return _self.isBig(input);

        case _self.TYPE_SMALL:
          return _self.isSmall(input);

        case _self.TYPE_MULTI:
          return _self.isMultiLine(input);

        default:
          return _self.isNormal(input);
      }
    },

    /**
     * @see TextInput.isDisabled
     */
    isDisabled: function(item) {
      var _self = this,
          input = core.dom.select(item);

      return (core.dom.is(input) && input.attr('disabled') === 'disabled' && input.hasClass('disabled'));
    },

    /**
     * @see TextInput.isFocused
     */
    isFocused: function(item) {
      var _self = this,
          input = core.dom.select(item);

      return (core.dom.is(input) && input.hasClass('focus'));
    },

    /**
     * @see TextInput.isBig
     */
    isBig: function(item) {
      var _self = this,
          input = core.dom.select(item);

      return (core.dom.is(input) && input.is('input') && input.hasClass('big') && input.parent().hasClass('big-wrapper'));
    },

    /**
     * @see TextInput.isNormal
     */
    isNormal: function(item) {
      var _self = this,
          input = core.dom.select(item);

      //If there is an input, it's styled and it's not small or big then it's normal.
      return (core.dom.is(input) && input.is('input') && !input.hasClass('small') && !input.hasClass('big'));
    },

    /**
     * @see TextInput.isSmall
     */
    isSmall: function(item) {
      var _self = this,
          input = core.dom.select(item);

      return (core.dom.is(input) && input.is('input') && input.hasClass('small'));
    },

    /**
     * @see TextInput.isMulti
     */
    isMultiLine: function(item) {
      var _self = this,
          input = core.dom.select(item);

      return (core.dom.is(input) && input.is('textarea'));
    }

  };

  /*************************************
   *               Public              *
   *************************************/

  var TextInput = /** @lends TextInput */ {
    /**
     * To set this type pass 0 as type. The input is going to get "big" class and a wrapping div.
     * This is a default option when only input is passed.
     */
    TYPE_BIG: _textinput.TYPE_BIG,
    /**
     * This is how default input without any class looks like.
     * To keep this display and add other features like label, pass 1 as type.
     */
    TYPE_NORMAL: _textinput.TYPE_NORMAL,
    /**
     * To set this type pass 2 as type.
     * This type is used to style textarea.
     */
    TYPE_MULTI: _textinput.TYPE_MULTI,
    /**
     * To set this type pass 3 as type.
     */
    TYPE_SMALL: _textinput.TYPE_SMALL,

    /**
     * Create/convert the input to a big, normal, small or multi-line input.
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @param {String} type=TextInput.TYPE_BIG  The type of text input to create. Either {@link TextInput.TYPE_BIG}, {@link TextInput.TYPE_NORMAL}, {@link TextInput.TYPE_SMALL} or {@link TextInput.TYPE_MULTI}.
     * @param {Object} [options] The options given to this input field.
     *   @option {String} value='' The initial value displayed in the input field.
     *   @option {String} label='' The label displayed. This label will disappear when the user inputs text. When this value is empty, no label will be displayed.
     *   @option {Boolean} disable=false Disable this input field.
     *   @option {Boolean} focus=false Focus this input field. (This does not unfocus other inputs.)
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(input, type, options) {
      _textinput.create(input, type, options);
    }.defaults('', _textinput.TYPE_BIG, {
      value: '',
      label: '',
      disable: false,
      focus: false
    }),

    /**
     * Remove the {@link TextInput} (style).
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @param  {Boolean} [original=false] Do you want to remove the original INPUT/TEXTARA as well?
     * @return {Boolean} True on success and false on error.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(input, original) {
      return _textinput.destroy(input, original);
    }.defaults('', false),

    /**
     * Disable the input.
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    disable: function(input) {
      return _textinput.disable(input);
    }.defaults(''),

    /**
     * Enable the input.
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    enable: function(input) {
      return _textinput.enable(input);
    }.defaults(''),

    /**
     * What is the placeholder (label) of the input?
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @returns {String} The placeholder of the input.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getPlaceholder: function(input) {
      return _textinput.getPlaceholder(input);
    }.defaults(''),

    /**
     * What is the value of the input?
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @returns {String} The value of the input.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getValue: function(input) {
      return _textinput.getValue(input);
    }.defaults(''),

    /**
     * Add/set a placeholder to the input.
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @param {String/Boolean} label The placeholder text. Turns off the placeholder functionality if empty/false.
     * @returns {CoreObj} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setPlaceholder: function(input, label) {
      return _textinput.setPlaceholder(input, label);
    }.defaults('', ''),

    /**
     * Change the inputs value.
     *
     * @param {CoreObj/Selector} input A CoreObj or selector pointing to the input element.
     * @param {String} value The value.
     * @returns {CoreObj} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setValue: function(input, value) {
      return _textinput.setValue(input, value);
    }.defaults('', ''),

    /**
     * Check whether the given input is a chosen type text input.
     *
     * @param {CoreObj/Selector} input The input element that will be styled/converted.
     * @param {String} type=TextInput.TYPE_BIG  The type of text input to check. Either {@link TextInput.TYPE_BIG}, {@link TextInput.TYPE_NORMAL}, {@link TextInput.TYPE_SMALL} or {@link TextInput.TYPE_MULTI}.
     * @returns {Boolean} Whether this input is of the chosen type.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    is: function(input, type) {
      return _textinput.is(input, type);
    }.defaults('', _textinput.TYPE_NORMAL),

    /**
     * Is this input disabled?
     *
     * @param {CoreObj/Selector} input The input element that should be checked.
     * @returns {Boolean} Whether this input is disabled or not.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    isDisabled: function(input) {
      return _textinput.isDisabled(input);
    }.defaults(''),

    /**
     * Is this input focused?
     *
     * @param {CoreObj/Selector} input The input element that should be checked.
     * @returns {Boolean} Whether this input is focused or not.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    isFocused: function(input) {
      return _textinput.isFocused(input);
    }.defaults('')
  };

  return TextInput;
});

core.module.alias.add('TextInput', 'library/uielements/textinput');
