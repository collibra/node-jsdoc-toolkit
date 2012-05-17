/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertFalse
assertBoolean
assertObject
core
*/
(function() {
  "use strict";
  var testCheck = "<input type='checkbox' id='check-1' name='check-1' />",
      testCheckLabel = "<input type='checkbox' id='check-2' name='check-2' /><label for='check-2'>Sample label</label>",
      testRadio = "<input type='radio' id='radio-1' name='radio-1' />",
      testRadioLabel = "<input type='radio' id='radio-2' name='radio-2' /><label for='radio-2'>Sample label</label>";
 
  new AsyncTestCase('uielements :: radiocheck', {
    
    //////////
    ////////// create / is
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var create = RadioCheck.create,
              newInput = false,
              originalInput = false;

          assertFunction('create of the RadioCheck uielement is not a function!', create);

          //Checkbox input
          core.dom.create(testCheck).appendTo("BODY");
          originalInput = core.dom.select('#check-1');
          assertTrue('Checkbox input is not found', originalInput.is(':checkbox'));
          
          //Create new one
          RadioCheck.create('#check-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));
          //Is checkbox?
          assertTrue('New input is not checkbox (has no checkboxInput class)', newInput.hasClass('checkboxInput'));
          assertTrue('New input is not checkbox', RadioCheck.is(originalInput, RadioCheck.TYPE_CHECKBOX));
          
          //Radio input
          core.dom.create(testRadio).appendTo("BODY");
          originalInput = core.dom.select('#radio-1');
          assertTrue('Radio input is not found', originalInput.is(':radio'));
          
          //Create new one
          RadioCheck.create('#radio-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));
          //Is checkbox?
          assertTrue('New input is not radio (has no radioInput class)', newInput.hasClass('radioInput'));
          assertTrue('New input is not radio', RadioCheck.is(originalInput, RadioCheck.TYPE_RADIO));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },

    //////////
    ////////// checkCheckbox
    //////////
    testCheckCheckbox: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var check = RadioCheck.check,
              newInput = false,
              originalInput = false;

          assertFunction('create of the RadioCheck uielement is not a function!', check);

          //Checkbox input
          core.dom.create(testCheck).appendTo("BODY");
          originalInput = core.dom.select('#check-1');
          assertTrue('Input is not found', originalInput.is(':checkbox'));
          
          //Create new one
          RadioCheck.create('#check-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Check
          assertTrue('Input checking failed',  RadioCheck.check(originalInput));
          assertTrue('Input is not checked', originalInput.is(':checked'));
          assertTrue('New input is not checked', newInput.hasClass('checked'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    //////////
    ////////// checkRadio
    //////////
    testCheckRadio: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var check = RadioCheck.check,
              newInput = false,
              originalInput = false;

          assertFunction('create of the RadioCheck uielement is not a function!', check);

          //Checkbox input
          core.dom.create(testRadio).appendTo("BODY");
          originalInput = core.dom.select('#radio-1');
          assertTrue('Input is not found', originalInput.is(':radio'));
          
          //Create new one
          RadioCheck.create('#radio-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Check
          assertTrue('Input checking failed',  RadioCheck.check(originalInput));
          assertTrue('Input is not checked', originalInput.is(':checked'));
          assertTrue('New input is not checked', newInput.hasClass('checked'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    //////////
    ////////// uncheckCheckbox
    //////////
    testUncheckCheckbox: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var check = RadioCheck.check,
              uncheck = RadioCheck.uncheck,
              newInput = false,
              originalInput = false;

          assertFunction('check of the RadioCheck uielement is not a function!', check);
          assertFunction('uncheck of the RadioCheck uielement is not a function!', uncheck);

          //Checkbox input
          core.dom.create(testCheck).appendTo("BODY");
          originalInput = core.dom.select('#check-1');
          assertTrue('Input is not found', originalInput.is(':checkbox'));
          
          //Create new one
          RadioCheck.create('#check-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Check
          assertTrue('Input checking failed',  RadioCheck.check(originalInput));
          assertTrue('Input is not checked', originalInput.is(':checked'));
          assertTrue('New input is not checked', newInput.hasClass('checked'));
          
          //UnCheck
          assertTrue('Input checking failed',  RadioCheck.uncheck(originalInput));
          assertFalse('Input is not checked', originalInput.is(':checked'));
          assertFalse('New input is not checked', newInput.hasClass('checked'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    //////////
    ////////// uncheckRadio
    //////////
    testUnheckRadio: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var check = RadioCheck.check,
              uncheck = RadioCheck.uncheck,
              newInput = false,
              originalInput = false;

          assertFunction('check of the RadioCheck uielement is not a function!', check);
          assertFunction('uncheck of the RadioCheck uielement is not a function!', uncheck);

          //Checkbox input
          core.dom.create(testRadio).appendTo("BODY");
          originalInput = core.dom.select('#radio-1');
          assertTrue('Input is not found', originalInput.is(':radio'));
          
          //Create new one
          RadioCheck.create('#radio-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Check
          assertTrue('Input checking failed',  RadioCheck.check(originalInput));
          assertTrue('Input is not checked', originalInput.is(':checked'));
          assertTrue('New input is not checked', newInput.hasClass('checked'));
          
          //UnCheck
          assertTrue('Input checking failed',  RadioCheck.uncheck(originalInput));
          assertFalse('Input is not checked', originalInput.is(':checked'));
          assertFalse('New input is not checked', newInput.hasClass('checked'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    //////////
    ////////// toggle Checkbox
    //////////
    testToggleCheckbox: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var toggle = RadioCheck.toggle,
              newInput = false,
              originalInput = false;
          
          assertFunction('toggle of the RadioCheck uielement is not a function!', toggle);

          //Checkbox input
          core.dom.create(testCheck).appendTo("BODY");
          originalInput = core.dom.select('#check-1');
          assertTrue('Input is not found', originalInput.is(':checkbox'));
          
          //Create new one
          RadioCheck.create('#check-1');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Check - toggle
          assertTrue('Input checking failed',  RadioCheck.toggle(originalInput));
          assertTrue('Input is not checked', originalInput.is(':checked'));
          assertTrue('New input is not checked', newInput.hasClass('checked'));
          
          //UnCheck - toggle
          assertTrue('Input checking failed',  RadioCheck.toggle(originalInput));
          assertFalse('Input is not checked', originalInput.is(':checked'));
          assertFalse('New input is not checked', newInput.hasClass('checked'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    
    //////////
    ////////// enable/disable Checkbox
    //////////
    testEnableDisableCheck: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var enable = RadioCheck.enable,
              disable = RadioCheck.disable,
              newInput = false,
              originalInput = false;

          assertFunction('enable of the RadioCheck uielement is not a function!', enable);
          assertFunction('disable of the RadioCheck uielement is not a function!', disable);

          //Checkbox input
          core.dom.create(testCheckLabel).appendTo("BODY");
          originalInput = core.dom.select('#check-2');
          assertTrue('Input is not found', originalInput.is(':checkbox'));
          
          //Create new one
          RadioCheck.create('#check-2');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Disable
          assertTrue('Input disabling failed',  RadioCheck.disable(originalInput));
          assertFalse('Input is not disabled', originalInput.is(':enabled'));
          assertTrue('New input is not disabled', newInput.hasClass('disabled'));
          
          //Enable
          assertTrue('Input enabling failed',  RadioCheck.enable(originalInput));
          assertTrue('Input is not enable', originalInput.is(':enabled'));
          assertFalse('New input is not checked', newInput.hasClass('disabled'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    },
    
    //////////
    ////////// enable/disable Radio
    //////////
    testEnableDisableRadio: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(RadioCheck) {
          var enable = RadioCheck.enable,
              disable = RadioCheck.disable,
              newInput = false,
              originalInput = false;

          assertFunction('enable of the RadioCheck uielement is not a function!', enable);
          assertFunction('disable of the RadioCheck uielement is not a function!', disable);

          //Checkbox input
          core.dom.create(testRadioLabel).appendTo("BODY");
          originalInput = core.dom.select('#radio-2');
          assertTrue('Input is not found', originalInput.is(':radio'));
          
          //Create new one
          RadioCheck.create('#radio-2');
          newInput = originalInput.next();
          assertTrue('New input is not created', newInput.hasClass('radioCheck'));

          //Disable
          assertTrue('Input disabling failed',  RadioCheck.disable(originalInput));
          assertFalse('Input is not disabled', originalInput.is(':enabled'));
          assertTrue('New input is not disabled', newInput.hasClass('disabled'));
          
          //Enable
          assertTrue('Input enabling failed',  RadioCheck.enable(originalInput));
          assertTrue('Input is not enable', originalInput.is(':enabled'));
          assertFalse('New input is not checked', newInput.hasClass('disabled'));
          
        });
        core.module.use('RadioCheck', moduleLoaded);
      });
    }
  });
}());