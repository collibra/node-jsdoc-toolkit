/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertFalse
assertBoolean
assertObject
assertUndefined
assertNotEquals
core
*/
(function() {
  "use strict";
  var testInput = "<input type='text' id='testInput' />",
      testTextarea = "<textarea id='testTextarea'></textarea>";

  new AsyncTestCase('uielements :: textinput', {
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var create = TextInput.create,
              jQNode;

          assertFunction('create of the TextInput uielement is not a function!', create);

          //BIG input
          core.dom.create(testInput).appendTo("BODY");
          create('#testInput', 0, {value: 'abcd', label: 'nice label', focus: true});
          jQNode = core.dom.select('#testInput');

          assertTrue('The input is different type then big', jQNode.hasClass('big'));
          assertEquals('abcd', jQNode.val());
          assertEquals('nice label', jQNode.attr('placeholder'));
          assertTrue('The Input is not focused' , TextInput.isFocused('#testInput'));

          create('#testInput', 0, {disable: true});
          assertEquals('disabled', jQNode.attr('disabled'));
          jQNode.remove();

          //small
          core.dom.create(testInput).appendTo("BODY");
          create('#testInput', 3, {value: 'abcd', label: 'nice label', focus: true});
          jQNode = core.dom.select('#testInput');

          assertTrue('The input is of different type then small', jQNode.hasClass('small'));
          assertEquals('abcd', jQNode.val());
          assertEquals('nice label', jQNode.attr('placeholder'));
          assertTrue('The Input is not focused' , TextInput.isFocused('#testInput'));

          create('#testInput', 3, {disable: true});
          assertEquals('disabled', jQNode.attr('disabled'));
          jQNode.remove();

          //normal
          core.dom.create(testInput).appendTo("BODY");
          create('#testInput', 1, {value: 'abcd', label: 'nice label', focus: true});
          jQNode = core.dom.select('#testInput');

          assertFalse('The Input is small instead of normal', jQNode.hasClass('small'));
          assertFalse('The Input is big instead of normal', jQNode.hasClass('big'));
          assertFalse('The Input is multi instead of normal', jQNode.hasClass('multi'));
          assertEquals('abcd', jQNode.val());
          assertEquals('nice label', jQNode.attr('placeholder'));
          assertTrue('The Input is not focused' , TextInput.isFocused('#testInput'));

          create('#testInput', 1, {disable: true});
          assertEquals('disabled', jQNode.attr('disabled'));
          jQNode.remove();

          //multi
          core.dom.create(testTextarea).appendTo("BODY");
          create('#testTextarea', 2, {value: 'abcd', label: 'nice label', focus: true});
          jQNode = core.dom.select('#testTextarea');

          assertTrue('The input is different type then multi line', jQNode.hasClass('multi'));
          assertEquals('abcd', jQNode.val());
          assertEquals('nice label', jQNode.attr('placeholder'));
          assertTrue('The Input is not focused' , TextInput.isFocused('#testTextarea'));

          create('#testTextarea', 2, {disable: true});
          assertEquals('disabled', jQNode.attr('disabled'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// destroy
    //////////
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var destroy = TextInput.destroy,
              jQNode;

          assertFunction('destroy of the TextInput uielement is not a function!', destroy);

          //small
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 3, {label: 'abc', disable: true});
          jQNode = core.dom.select('#testInput');
          assertTrue('Value returned by disable() is not true.', destroy('#testInput'));
          assertFalse('Small class is not removed from input after using destroy().', jQNode.hasClass('small'));
          assertNotEquals('abc', jQNode.attr('placeholder'));
          assertNotEquals('disabled', jQNode.attr('disabled'));
          destroy('#testInput', true);
          assertEquals(0, core.dom.select('#testInput').length);

          //big
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 0, {label: 'abc', disable: true});
          jQNode = core.dom.select('#testInput');
          assertTrue('Value returned by disable() is not true.', destroy('#testInput'));
          assertFalse('Big class is not removed from input after using destroy().', jQNode.hasClass('big'));
          assertNotEquals('abc', jQNode.attr('placeholder'));
          assertNotEquals('disabled', jQNode.attr('disabled'));
          assertFalse('Input (type big) was not unwrapped.', jQNode.parent().hasClass('big-wrapper'));
          destroy('#testInput', true);
          assertEquals(0, core.dom.select('#testInput').length);
   
          //normal
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1, {label: 'abc', disable: true});
          jQNode = core.dom.select('#testInput');
          assertTrue('Value returned by disable() is not true.', destroy('#testInput'));
          assertNotEquals('abc', jQNode.attr('placeholder'));
          assertNotEquals('disabled', jQNode.attr('disabled'));
          destroy('#testInput', true);
          assertEquals(0, core.dom.select('#testInput').length);
          
          //multi
          core.dom.create(testTextarea).appendTo("BODY");
          TextInput.create('#testTextarea', 2, {label: 'abc', disable: true});
          jQNode = core.dom.select('#testTextarea');
          assertTrue('Value returned by disable() is not true.', destroy('#testTextarea'));
          assertFalse('Small class is not removed from input after using destroy().', jQNode.hasClass('multi'));
          assertNotEquals('abc', jQNode.attr('placeholder'));
          assertNotEquals('disabled', jQNode.attr('disabled'));
          destroy('#testTextarea', true);
          assertEquals(0, core.dom.select('#testTextarea').length);
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// disable
    //////////
    testDisable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var disable = TextInput.disable;

          assertFunction('disable of the TextInput uielement is not a function!', disable);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertTrue('Value returned by disable() is not true.', disable('#testInput'));
          assertEquals('disabled', core.dom.select('#testInput').attr('disabled'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },
    
    //////////
    ////////// enable
    //////////
    testEnable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var enable = TextInput.enable;

          assertFunction('enable of the TextInput uielement is not a function!', enable);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          TextInput.disable('#testInput');
          assertTrue('Value returned by enable() is not true.', enable('#testInput'));
          assertNotEquals('disabled', core.dom.select('#testInput').attr('disabled'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// getPlaceholder
    //////////
    testGetPlaceholder: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var getPlaceholder = TextInput.getPlaceholder;

          assertFunction('getPlaceholder of the TextInput uielement is not a function!', getPlaceholder);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1, {label : 'abcd'});
          assertEquals('abcd', getPlaceholder('#testInput'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// getValue
    //////////
    testGetValue: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var getValue = TextInput.getValue;

          assertFunction('getValue of the TextInput uielement is not a function!', getValue);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1, {value : 'abcd'});
          assertEquals('abcd', TextInput.getValue('#testInput'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },


    //////////
    ////////// setPlaceholder
    //////////
    testSetPlaceholder: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var setPlaceholder = TextInput.setPlaceholder,
              jQNode;

          assertFunction('setPlaceholder of the TextInput uielement is not a function!', setPlaceholder);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          jQNode = core.dom.select('#testInput');

          assertTrue('Value returned by setLabel is not true.', setPlaceholder('#testInput', 'abcd'));
          assertEquals('abcd', jQNode.attr('placeholder'));

          setPlaceholder('#testInput', false);
          assertUndefined('There should be no placeholder attribute.', jQNode.attr('placeholder'));
          setPlaceholder('#testInput', 'abcd');
          setPlaceholder('#testInput', '');
          assertUndefined('There should be no placeholder attribute.', jQNode.attr('placeholder'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// setValue
    //////////
    testSetValue: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var setValue = TextInput.setValue;

          assertFunction('setValue of the TextInput uielement is not a function!', setValue);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertTrue('Value returned by setValue is not true.', setValue('#testInput', 'abcd'));
          assertEquals('abcd', core.dom.select('#testInput').val());
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },


    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var is = TextInput.is;

          assertFunction('is of the TextInput uielement is not a function!', is);

          //big
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertFalse('Value returned from is() method after creating normal input is not false.', is('#testInput', 0));

          TextInput.create('#testInput', 0);
          assertTrue('Value returned from is() method after creating big input is not true.', is('#testInput', 0));
          core.dom.select('#testInput').remove();

          //normal
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 3);
          assertFalse('Value returned by is() method after creating small input is not false.', is('#testInput', 1));

          TextInput.create('#testInput', 1);
          assertTrue('Value returned by is() method after creating notmal input is not true.', is('#testInput', 1));
          core.dom.select('#testInput').remove();

          //small
          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertFalse('Value returned by is() method after creating normal input is not false.', is('#testInput', 3));

          TextInput.create('#testInput', 3);
          assertTrue('Value returned by is() method after creating small input is not true.', is('#testInput', 3));
          core.dom.select('#testInput').remove();

          //multi
          core.dom.create(testTextarea).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertFalse('Value returned by is() method after creating textinput is not false.', is('#testInput', 2));

          TextInput.create('#testTextarea', 2);
          assertTrue('Value returned by is() method after creating texarea is not true.', is('#testTextarea', 2));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// isDisabled
    //////////
    testIsDisabled: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var isDisabled = TextInput.isDisabled;

          assertFunction('isDisabled of the TextInput uielement is not a function!', isDisabled);

          core.dom.create(testInput).appendTo("BODY");
          TextInput.create('#testInput', 1);
          assertFalse('Value returned by isDisabled after normal() is not false.', isDisabled('#testInput'));

          core.dom.select('#testInput').attr('disabled', 'disabled').addClass('disabled');
          assertTrue('Value returned by isDisabled after disable() is not true.', isDisabled('#testInput'));
          assertTrue('The element does not have "disabled" class.', core.dom.select('#testInput').hasClass('disabled'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    },

    //////////
    ////////// isFocused
    //////////
    testIsFocused: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextInput) {
          var isFocused = TextInput.isFocused;

          assertFunction('isFocused of the TextInput uielement is not a function!', isFocused);

          core.dom.create(testInput).appendTo("BODY");

          TextInput.create('#testInput', 1);
          assertFalse('Value returned by isFocused before focus() is not false.', isFocused('#testInput'));

          core.dom.select('#testInput').focus();
          assertTrue('Value returned by isFocused after focus() is not true.', isFocused('#testInput'));
        });

        core.module.use('TextInput', moduleLoaded);
      });
    }
  });
}());