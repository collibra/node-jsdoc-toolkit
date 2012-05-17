/**
 * Date & Time Picker.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
 * @module library/uielements/datepicker
 * @alias DatePicker
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/datepicker',
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
  var _datePicker = {
      
    prepareMyDatePicker: function(datepicker) {
      datepicker = core.dom.select(datepicker);
      var title = datepicker.find('.ui-datepicker-title'),
          calendar = datepicker.find('.ui-datepicker-calendar').html();
      
      datepicker.addClass('datepicker').prepend('<div class="ui-datepicker-tip"></div>');
      core.dom.select('<div class="ui-datepicker-date"></div>').insertAfter(datepicker.find('.ui-datepicker-header'));
      datepicker.find('.ui-datepicker-date').prepend(title.html());
      
      title.empty();
      title.prepend('<i class="icon icon-calendar"></i> Date & Time');
      datepicker.find('.ui-datepicker-close').prepend('<i class="icon-thin-close lightgrey"></i>');
      
      datepicker.find('.ui-datepicker-date').append('<table class="ui-datepicker-calendar black">' + calendar + '</table>');
      datepicker.children('.ui-datepicker-calendar').remove();
    },
      
    create: function(selector, options) {
      // !TODO: Implement.
      var _self = this,
        input = '',
        selObj = core.dom.select(selector);
      
      // If selector is wrapper.
      if (options.input.create) {
        // Create input and icon
        selObj.prepend('<input type="text" id="datepicker-' + Math.round((new Date()).getTime() / 1000) + '" class="datepicker-input ' + options.input.extraClass + '" />').append('<i class="icon-calendar black"></i>').addClass('datepicker-input-wrapper');
        input = selObj.find('input');
      // Else if selector is already input.
      } else {
        // Wrap this input, append icon and add extraClass.
        selObj.wrap('<span class="datepicker-input-wrapper">').addClass(options.input.extraClass);
        core.dom.select('<i class="icon-calendar black"></i>').insertAfter(selector);
        input = selObj;
      }
      
      if (options.date.show) {
       input.parent().datepicker({changeMonth: true,
         changeYear: true,
         showOtherMonths: true,
         selectOtherMonths: true,
         onSelect: function(dateText, inst) {
           core.debug.show('aa');
           _self.prepareMyDatePicker(inst);
           return false;
         }
       });
      }
      
      this.prepareMyDatePicker(input.parent().find('.ui-datepicker-inline'));
     
      return false;
//      return core.dom.select(selector).datepicker(options);
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var DatePicker = /** @lends DatePicker */{
    /**
     * Create a data/time picker.
     *
     * @param {CoreObj/Selector} selector A CoreObj/Selector to either a wrapper for the input or the input itself.
     * @param {Object} options Options for the date and time picker.
     *   @option {Boolean} input.create=true Create the corresponding input within the selector?
     *   @option {String} input.extraClass='' Add a class to the input.
     *   @option {Boolean} input.editable=false Make the input box editable.
     *   @option {Boolean} input.enabled=true Is the control enabled?
     *   @option {Boolean} time.show=false Show time?
     *   @option {String} time.format='HH:MM' What is the time format?
     *   @option {Boolean} date.show=true Show date?
     *   @option {String} date.format='yy-mm-dd' What is the date format?
     *   @option {Date} date.min Minimum date selectable.
     *   @option {Date} date.max Maximum date selectable.
     * @returns {Boolean/CoreObj} False on failure otherwise a CoreObj pointing at the control.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    create: function(selector, options) {
      return _datePicker.create(selector, options);
    }.defaults('', {
      input: {
        create: true,
        extraClass: '',
        editable: false,
        enabled: true
      },
      time: {
        show: false,
        format: ''
      },
      date: {
        format: 'yy-mm-dd',
        show: true,
        min: 0, // !TODO: Add same date as today but 100 years ago.
        max: 0 // !TODO: Add same date as today but 100 years from now.
      }
    }),

    /**
     * Destroy the date/time picker.
     *
     * @param {Selector/CoreObj} dateInput a CoreObj/Selector that points to the user input with the date / time picker attached to it.
     * @param {Boolean} original Also destroy the input itself?
     * @returns {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    destroy: function(dateInput, original) {
      // !TODO: Implement.
    }.defaults('', true),

    disable: function(dateInput) {},
    enable: function(dateInput) {},
    getValue: function(dateInput, textual) {},
    is: function(dateInput) {},
    hide: function(dateInput) {},
    setValue: function(dateInput, textual) {},
    show: function(dateInput) {}
  };

  return DatePicker;
});

core.module.alias.add('DatePicker', 'library/uielements/datepicker');