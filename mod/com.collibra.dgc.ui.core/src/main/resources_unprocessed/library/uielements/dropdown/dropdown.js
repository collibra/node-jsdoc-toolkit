/**
 * Dropdown and autocomplete module.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module library/uielements/dropdown
 * @alias DropDown
 * @namespace controls
 */
/*global define,require,core*/
define('library/uielements/dropdown',
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
  var _i18n = core.i18n.getTranslations("library.uielements.dropdown");
   
  /** @private */
  var _dropdown = {
                   
    /**
    * @see dropdown.USERS
    */
    USERS: "Users",

    /**
    * @see dropdown.TERMS
    */
    TERMS: "Terms",

    /**
    * @see dropdown.VOCABULARIES
    */
    VOCABULARIES: "Vocabularies",

    /**
    * @see dropdown.COMMUNITIES
    */
    COMMUNITIES: "Communities",

    //preset configuration types
    types: {
      Default : {
        multiple: false,
        minimumInputLength: 0,
        minimumResultsForSearch: 10,
        formatInputTooShort: function(term, minLength) {
          if (!term) {
            return _i18n["default"].text.tooShort;
          }
        },
        singleDeselect: true,
        text: {
          "default": _i18n["default"].text.placeholder
        },
        formatNoMatch: function() {
          return _i18n["default"].text.noMatch;
        },
        formatResult: function(option) {
          return option.text;
        },
        formatSelection: function(option) {
          return option.text;
        },
        autoComplete: {
          enabled: false,
          url: "",
          timeout: 700
        },
        //to be filled with jquery option selectors ["#op1","#op2"]
        initialSelection: [],
        className: "dropdown-type-default"
      },
      Users: {
        multiple: false,
        singleDeselect: true,
        minimumInputLength: 0,
        minimumResultsForSearch: 10,
        text: {
          "default": _i18n.users.text.placeholder
        },
        formatNoMatch: function() {
          return _i18n.users.text.noMatch;
        },
        formatResult: function(option) {
          if (option) {
            return '<div><img width="20" src="'+option.attributes.imgsrc+'" style="margin-bottom: -3px;">' + option.text + '</div>';
          }
        },
        formatSelection: function(option) {
          if (option) {
            return '<img width="15" src="'+option.attributes.imgsrc+'" style="position: relative; margin-bottom: -4px;">' + option.text;
          }
        },
        autoComplete: {
          enabled: false,
          url: "",
          timeout: 700
        },
        //to be filled with jquery option selectors ["#op1","#op2"]
        initialSelection: [],
        className: "dropdown-type-users"
      },
      Terms : {
        multiple: false,
        singleDeselect: true,
        minimumInputLength: 0,
        minimumResultsForSearch: 10,
        text: {
          "default": _i18n.terms.text.placeholder
        },
        formatNoMatch: function() {
          return _i18n.terms.text.noMatch;
        },
        formatResult: function(option) {
          return option.text;
        },
        formatSelection: function(option) {
          return option.text;
        },
        autoComplete: {
          enabled: true,
          url: "",
          timeout: 700
        },
        //to be filled with jquery option selectors ["#op1","#op2"]
        initialSelection: [],
        className: "dropdown-type-terms"
      },
      Vocabularies : {
        multiple: false,
        singleDeselect: true,
        minimumInputLength: 0,
        minimumResultsForSearch: 10,
        text: {
          "default": _i18n.vocabularies.text.placeholder
        },
        formatNoMatch: function() {
          return _i18n.vocabularies.text.noMatch;
        },
        formatResult: function(option) {
          return option.text;
        },
        formatSelection: function(option) {
          return option.text;
        },
        autoComplete: {
          enabled: true,
          url: "",
          timeout: 700
        },
        //to be filled with jquery option selectors ["#op1","#op2"]
        initialSelection: [],
        className: "dropdown-type-vocabularies"
      },
      Communities : {
        multiple: false,
        singleDeselect: true,
        minimumInputLength: 0,
        minimumResultsForSearch: 10,
        text: {
          "default": _i18n.communities.text.placeholder
        },
        formatNoMatch: function() {
          return _i18n.communities.text.noMatch;
        },
        formatResult: function(option) {
          return option.text;
        },
        formatSelection: function(option) {
          return option.text;
        },
        autoComplete: {
          enabled: true,
          url: "",
          timeout: 700
        },
        //to be filled with jquery option selectors ["#op1","#op2"]
        initialSelection: [],
        className: "dropdown-type-communities"
      }
    },

    /**
     * Return options object for specified type. Can be either String or Object.
     * Internal use only.
     * @param {String/Object} type Type name as a string or object with options.
     * @returns {Object} Returns options object specific for chosen type.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getType: function(type) {
      if (_dropdown.types.hasOwnProperty(type) || type !== "Default" && core.string.is(type)) {
        return core.object.extend(core.object.copy(_dropdown.types.Default), _dropdown.types[type]);
      } else if(type === "Default" || !type) {
        return _dropdown.types.Default;
      } else if(core.object.is(type)) {
        return core.object.extend(core.object.copy(_dropdown.types.Default), type);
      }
    },

    /**
     * @see dropdown.addOption
     */
    addOption: function(selector, option, position) {
      var action = "append",
          select = null;

      selector = core.dom.select(selector);
      select = core.object.copy(selector);
      if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
        //Setting avatar here is not yet possible. i need to consider possible solutions.
        //Will return here when needed.
        if (position === "first") {
          action = "prepend";
          if (!selector.children(":eq(0)").attr("value")) {
            select = select.children(":eq(0)");
            action = "after";
          }
        }
        select[action]("<option value='"+ option.id +"'>"+ option.label +"</option>");

        return true;
      }

      return false;
    },
    
   /**
    * @see dropdown.close
    */
   close: function(selector) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
       selector.select2("close");

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.create
    */
   create: function(selector, type, moreOptions) {
     var temp_id = "",
         options = {},
         transportFunction = null,
         initialValues = null;

     selector = core.dom.select(selector);

     if (core.dom.is(selector) && !selector.hasClass("with-dropdown")) {

       options = _dropdown.getType(type);

       if (moreOptions) {
         options = core.object.extend(options, moreOptions);
       }

         
       options.placeholder = options.text["default"];
       options.allowClear = options.singleDeselect;
       
       if(options.tags === []) {
         delete options.tags;
       }

       //Apply ajax.
       options.ajax = {};
       

       //make multiple
       if (!options.multiple) {
         selector.removeAttr("multiple");
         selector.addClass("single");
       } else {
         selector.attr("multiple","multiple");
         selector.addClass("mulitple");
       }

       //Add 'selected' attribute to options specified in options.initialSelection array.
       for (var opt = 0; opt < options.initialSelection.length; opt = opt + 1) {
         var sel_option = selector.children("option[value="+options.initialSelection[opt]+"]");
         if (sel_option.length) {
           sel_option.attr("selected","selected");
         }
       }

       //-------CREATE SELECT2-----------
       selector.select2(options);
       //--------------------------------

       //Add type classname to the dropdown and to the select.
       selector.next().addClass(options.className);
       selector.addClass("with-dropdown");

       if (options.autoComplete.enabled) {
         selector.addClass("autocomplete");
         selector.data("select2").container.addClass("select2-autocomplete");
       }

       //save starting values
       selector.data("select2", core.object.extend(selector.data("select2"), {"initialValues": selector.select2("val")}));

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.destroy
    */
   destroy: function(selector, original) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector)) {
       selector.select2("destroy");
       selector.removeClass("with-dropdown single multi autocomplete");

       return true;
     }

     return false;
   },
   
   /**
    * @see dropdown.disable
    */
   disable: function(selector) {
     selector = core.dom.select(selector);

     if (core.dom.is(selector) && !_dropdown.isDisabled(selector)) {
       selector.addClass("disabled");
       selector.next().append("<div class='dropdown-overlay-disabled'></div>");
       return true;
     }

     return false;
   },
   
   /**
    * @see dropdown.enable
    */
   enable: function(selector) {
     selector = core.dom.select(selector);
     if (core.dom.is(selector) && _dropdown.isDisabled(selector)) {
       selector.removeClass("disabled");
       selector.next().find(".dropdown-overlay-disabled").remove();
       return true;
     }

     return false;
   },
   
   /**
    * @see dropdown.focus
    */
   focus: function(selector) {
     selector = core.dom.select(selector);
     if (core.dom.is(selector) && !_dropdown.isDisabled(selector)) {
       selector.select2("focus");
       return true;
     }

     return false;
   },

   /**
    * @see dropdown.getAutoCompleteOptions
    */
   getOptions: function(selector) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector)) {

       return selector.data("select2").opts.ajax;
     }

     return false;
   },
   
   /**
    * @see dropdown.getValue
    */
   getSelected: function(selector) {
     selector = core.dom.select(selector);

     if (_dropdown.is(selector)) {
       return selector.select2("val");
     }

     return false;
   },
   
   /**
    * @see dropdown.is
    */
   is: function(selector) {
     selector = core.dom.select(selector);
     if (core.dom.is(selector) && selector.hasClass("with-dropdown")) {
       return true;
     }

     return false;
   },
   
   /**
    * @see dropdown.isDisabled
    */
   isDisabled: function(selector) {
     selector = core.dom.select(selector);
     if (core.dom.is(selector)) {
       return selector.hasClass("disabled");
     }

     return false;
   },
   
   /**
    * @see dropdown.isFocused
    */
   isFocused: function(selector) {
     selector = core.dom.select(selector);
     if (core.dom.is(selector) && !_dropdown.isDisabled(selector)) {
       return selector.select2("isFocused");
     }

     return false;
   },

   /**
    * @see dropdown.onchange
    */
   onchange: function(selector, callback) {
     var args = [];

     selector = core.dom.select(selector);
     if (_dropdown.is(selector)) {
       selector.on("change", function() {
         args = Array.prototype.slice.call(arguments);
         args.push(_dropdown.getValue(selector));
         callback.apply(this, args);
       });

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.open
    */
   open: function(selector) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
       selector.select2("open");

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.toggleResults
    */
   toggleResults: function(selector) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {

       if (selector.next().hasClass("select2-dropdown-open")) {
         selector.select2("close");
       } else {
         selector.select2("open");
       }

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.refresh
    */
   refresh: function(selector) {
     var options = null;

     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
       options = selector.data("select2");

       //Recreate.
       _dropdown.destroy(selector);
       core.array.each(options.initialValues, function(i, key) {
         var sel_option = selector.children("option[value="+key+"]");
         if (sel_option.length) {
           sel_option.attr("selected","selected");
         }
       });

       _dropdown.create(selector, options.opts);

       return true;
     }

     return false;
   },
   
   /**
    * @see dropdown.removeOption
    */
   removeOption: function(selector, optionID) {
     var option = null;

     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
       option = selector.children("option[value='"+optionID+"']");
       if (core.dom.is(option)) {
         option.remove();
         return true;
       }
     }

     return false;
   },

   /**
    * @see dropdown.reset
    */
   reset: function(selector) {
     var initialValues = null;

     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {

       initialValues = !selector.data("select2").opts.ajax.enabled ? selector.data("select2").initialValues || [] : [];
       
       //If its an autocomplete, reset will remove all selected options.
       //Otherwise set to initial values.
       if (selector.hasClass("single") && !selector.hasClass("autocomplete")) {

         selector.select2("val", [initialValues]);

         return true;
       } else if(selector.hasClass("single") && selector.hasClass("autocomplete")) {

         //If autocomplete then clear all selected options.
         selector.select2("val", []);

         return true;
       }

       selector.select2("val", initialValues);

       return true;
     }

     return false;
   },


   /**
    * @see dropdown.setValue
    */
   setSelected: function(selector, newValue, triggerOnChange) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector) && !_dropdown.isDisabled(selector)) {
       selector.select2("val", newValue);
       if (triggerOnChange) {
         selector.trigger("change", [_dropdown.getValue(selector)]);
       }

       return true;
     }

     return false;
   },

   /**
    * @see dropdown.setAutoCompleteOptions
    */
   setOptions: function(selector, options) {
     selector = core.dom.select(selector);
     if (_dropdown.is(selector)) {
       options.quietMillis = options.timeout;
       selector.data("select2").opts.ajax = core.object.extend(selector.data("select2").opts.ajax, options);

       return true;
     }

     return false;
   }
 };


/*************************************
 *               Public              *
 *************************************/

/**
 * @namespace uielements.dropdown
 */
var DropDown = /** @lends DropDown */ {

   /**
    * Show users.
    */
   USERS: _dropdown.USERS,

   /**
    * Show terms.
    */
   TERMS: _dropdown.TERMS,

   /**
    * Show vocabularies.
    */
   VOCABULARIES: _dropdown.VOCABULARIES,

   /**
    * Show communities.
    */
   COMMUNITIES: _dropdown.COMMUNITIES,

   /**
    * Add a new option to the dropdown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {String/Object} data New data formatted as an object or a plain string.
    *   @option {String} id The identifier for this option. (Will be given back to getSelected.)
    *   @option {String} avatar The path to the avatar image.
    *   @option {String} label The label. Can be user information, term/vocabulary/community name, etc.
    * @param {String} position Where the new option should be added, at the begining: "first" or at the end: "last".
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   addOption: function(dropdown, data, position) {
     return _dropdown.addOption(dropdown, data, position);
   }.defaults('', {}),

   /**
    * Close the drop-down.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   close: function(dropdown) {
     return _dropdown.close(dropdown);
   }.defaults(''),

    /**
    * Create a dropdown or autocomplete.
    *
    * @param {CoreObj/Selector} selector A CoreObj or selector pointing to the SELECT element.
    * @param {String/CoreObj} type Predefined dropdown behaviour (internal layout, templates etc).
    * Can be either {@link Dropdown.USERS}, {@link Dropdown.TERMS}, {@link Dropdown.VOCABULARIES}, {@link Dropdown.COMMUNITIES}.
    * @param {Object} [options] Options for the dropdown.
    *   @option {Boolean} multiple=false Force the select to behave like a multiple select (even when original select doesn't have the multiple attribute).
    *   @option {Function} formatNoMatch Function that returns text to be shown when no results are found.
    *   @option {Function} formatSelection Function that returns markup for rendering selected option.
    *   @option {Function} formatResult Function that returns markup for rendering results (dropdown options).
    *   @option {Function} formatInputTooShort Function used to render the "Search input too short." message.
    *   @option {Integer} minimumInputLength=1 Number of characters necessary to start a search.
    *   @option {Integer} minimumResultsForSearch The minimum of results needed in order for the dropdown to show results as autocomplete.
    *   This is useful for cases where local data is used with just a few results, in which case the search box is not very useful and wastes screen space.
    *   @option {String} text.default Text to show when nothing is selected.
    *   @option {Boolean} singleDeselect=true Enable deselect feature(close button) when an option is selected.
    *   @option {Array/CoreObj/Selector} initialSelection=[] The elements that are selected.
    *   @option {String} className The class name that should be added to the dropdown to apply custom styles.
    *   @option {Boolean} autoComplete.enabled=false Enable the autocomplete function.
    *   @option {String} autoComplete.url URL to fetch the results from.
    *   @option {Integer} autoComplete.timeout Number of milliseconds to wait for the user to stop typing before issuing the ajax request.
    *   @option {Array} data Array of options to render. Be aware that this is not initialSelection(which only contains options to select), this creates options in the DropDown.
    *   Each element of the array is an object with key 'id' and 'text'. Example: data: [{id: 1, text: "first"}, {id: 2, text: "second"}]
    *   @option {Array/Function} tags=[] Puts dropdown into 'tagging' mode where the user can add new choices and pre-existing tags are provided by this option,
    *   which is either an array or a function that returns an array of objects or strings.
    *   If strings are used instead of objects they will be converted into an object that has an id and text attribute equal to the value of the string.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   create: function(selector, type, additionalOptions) {
     return _dropdown.create(selector, type, additionalOptions);
   },
   //There should be no default options here because this var(now additionalOptions) is for settings additional only settings.
   //Everything here will override type variable(this is usefull when i want for example render dropdown for users either in single or multi mode)
   //Defaults settings are defined in the type "Default".
   //If you want to use defaults here then we need to replace "Defaults" string with the object, but then we need to remove Default type.
   //In this case imo its better now, without them here.

   /**
    * Removes the drop-down/autocomplete functionality.
    * When autocomplete this will remove it completely and no SELECT tag will be shown(as there are none to show).
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Boolean} [original=false] Remove the original HTML code (the select).
    * @returns {Boolean} Returns true on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   destroy: function(dropdown, original) {
     return _dropdown.destroy(dropdown, original);
   }.defaults('', false),
   
   /**
    * Disable DropDown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Boolean} [original=false] Remove the original HTML code (the select).
    * @returns {Boolean} Returns true on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   disable: function(dropdown) {
     return _dropdown.disable(dropdown);
   }.defaults('', false),
   
   /**
    * Enable DropDown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Boolean} [original=false] Remove the original HTML code (the select).
    * @returns {Boolean} Returns true on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   enable: function(dropdown) {
     return _dropdown.enable(dropdown);
   }.defaults('', false),
   
   /**
    * Put focus on DropDown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Boolean} [original=false] Remove the original HTML code (the select).
    * @returns {Boolean} Returns true on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   focus: function(dropdown) {
     return _dropdown.focus(dropdown);
   }.defaults('', false),

   /**
    * Retrieve options.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Object} Returns object with current options.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   getOptions: function(dropdown) {
     return _dropdown.getOptions(dropdown);
   }.defaults(''),

   /**
    * Retrieve the value of the selected options.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Array/String} Returns string or array of the strings (multiple selects), or false if fail.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   getSelected: function(dropdown) {
     return _dropdown.getSelected(dropdown);
   }.defaults(''),

   /**
    * Check if the element is a dropdown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} Whether the CoreObj or selector is an actual dropdown.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   is: function(dropdown) {
     return _dropdown.is(dropdown);
   }.defaults(''),
   
   /**
    * Check whether DropDown is disabled or not.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} Whether the CoreObj or selector is an actual dropdown.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   isDisabled: function(dropdown) {
     return _dropdown.isDisabled(dropdown);
   }.defaults(''),
   
   /**
    * Check whether DropDown is focused or not.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} Whether the CoreObj or selector is an actual dropdown.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   isFocused: function(dropdown) {
     return _dropdown.isFocused(dropdown);
   }.defaults(''),

   /**
    * Attach the callback to the change event.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Function} callback Function to be execute on change event. function(ev, new_value) {}
    * @example
    * dropdown.onchange(selector, function(event, new_value) {
    *   core.debug.show(event); //this contains $.Event object.
    *   core.debug.show(event); //this contains current dropdown value(value after change).
    * });
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   onChange: function(dropdown, callback) {
     return _dropdown.onchange(dropdown, callback);
   }.defaults('', function() {}),

   /**
    * Open the dropdown.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   open: function(dropdown) {
     return _dropdown.open(dropdown);
   }.defaults(''),

   /**
    * Generate the drop-down again.
    * When autocomplete there are no initialValues so DropDown becomes empty(of course options can be then fetched).
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   refresh: function(dropdown) {
     return _dropdown.refresh(dropdown);
   }.defaults(''),

   /**
    * Removes an existing option.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {String} optionID The id of the option to remove. This is the number hold in the option's "value" attribute.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   removeOption: function(dropdown, optionID) {
     return _dropdown.removeOption(dropdown, optionID);
   }.defaults('', ''),

   /**
    * Resets previously selected options. Brings default up.
    *‡
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   reset: function(dropdown) {
     return _dropdown.reset(dropdown);
   }.defaults(''),

   /**
    * Set the selected option(s) of the dropdown/autocomplete.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Array/String} [newValue] The value(s) to select.
    * @param {Boolean} [triggerOnChange=false] Whether to trigger change event on select when value is changed using dropdown.value or not.
    * @returns {Array/String} Returns true on success, false on fail.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   setSelected: function(dropdown, newValue, triggerOnChange) {
     return _dropdown.setSelected(dropdown, newValue, triggerOnChange);
   }.defaults('', '', false),

   /**
    * Set options for the autocomplete.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @param {Object} [options] Options for the dropdown autocomplete.
    *   @option {Boolean} autoComplete.enabled=false Enable the autocomplete function.
    *   @option {String} autoComplete.url URL to fetch the results from.
    *   @option {Integer} autoComplete.timeout Throttle timeout(in milliseconds) after which ajax call is made.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   setOptions: function(dropdown, options) {
     return _dropdown.setOptions(dropdown, options);
   }.defaults('', {
       enabled: false,
       url: '',
       timeout: 700
   }),

   /**
    * Toggle the dropdown results.
    *
    * @param {CoreObj/Selector} dropdown A CoreObj or selector pointing to the original SELECT.
    * @returns {Boolean} True on success, false on failure.
    * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
    * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
   toggleResults: function(dropdown) {
     return _dropdown.toggleResults(dropdown);
   }.defaults('')
  };

  return DropDown;
});

core.module.alias.add('DropDown', 'library/uielements/dropdown');