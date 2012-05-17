/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertFalse
assertBoolean
assertObject
assertUndefined
core
*/
(function() {
  "use strict";
  var testTooltip1 = "<div id='testDiv'></div>",
      testTooltip2 = "<p id='testP'></p>",
      testTooltip3 = "<div id='testDiv2' class='tooltip' title='z1z2z3'>one two</div>";

  new AsyncTestCase('uielements :: tooltip', {
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var create = Tooltip.create;

          assertFunction('create of the Tooltip uielement is not a function!', create);

          core.dom.create(testTooltip1).appendTo("BODY");

          create('#testDiv', {content: 'abc'});

          assertEquals('abc', core.dom.select('#testDiv').data('qtip').options.content.text);
          core.dom.select('.qtip').remove();

          core.dom.create(testTooltip3).appendTo("BODY");
          create('#testDiv2');
          assertEquals('z1z2z3', core.dom.select('#testDiv2').data('qtip').options.content.text);
          core.dom.select('.qtip').remove();

          core.dom.create(testTooltip2).appendTo("BODY");
          create('#testP', {content: core.dom.select('#testDiv2')});
          assertEquals('one two', core.dom.select('#testP').data('qtip').options.content.text.text());

          //default orientation
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.at.x);
          assertEquals('bottom', core.dom.select('#testP').data('qtip').options.position.at.y);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.my.x);
          assertEquals('top', core.dom.select('#testP').data('qtip').options.position.my.y);

          create('#testP', {content: 'a1', orientation: 0});
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.at.x);
          assertEquals('top', core.dom.select('#testP').data('qtip').options.position.at.y);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.my.x);
          assertEquals('bottom', core.dom.select('#testP').data('qtip').options.position.my.y);

          create('#testP', {content: 'a1', orientation: 1});
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.at.x);
          assertEquals('bottom', core.dom.select('#testP').data('qtip').options.position.at.y);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.my.x);
          assertEquals('top', core.dom.select('#testP').data('qtip').options.position.my.y);

          create('#testP', {content: 'a1', orientation: 2});
          assertEquals('left', core.dom.select('#testP').data('qtip').options.position.at.x);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.at.y);
          assertEquals('right', core.dom.select('#testP').data('qtip').options.position.my.x);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.my.y);

          create('#testP', {content: 'a1', orientation: 3});
          assertEquals('right', core.dom.select('#testP').data('qtip').options.position.at.x);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.at.y);
          assertEquals('left', core.dom.select('#testP').data('qtip').options.position.my.x);
          assertEquals('center', core.dom.select('#testP').data('qtip').options.position.my.y);
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// destroy
    //////////
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var destroy = Tooltip.destroy;

          assertFunction('destroy of the Tooltip uielement is not a function!', destroy);
          core.dom.create(testTooltip1).appendTo("BODY");
          Tooltip.create('#testDiv', {content: 'abc'});
          assertEquals('abc', core.dom.select('#testDiv').data('qtip').options.content.text);
          assertBoolean('destroy does not return boolean.', destroy('#testDiv'));
          assertUndefined('abc', core.dom.select('#testDiv').data('qtip'));
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// getContent
    //////////
    testGetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var getContent = Tooltip.getContent;

          assertFunction('getContent of the Tooltip uielement is not a function!', getContent);

          assertFalse('False is not returned when non-existing originator is passed.', Tooltip.getContent('#testDiv'));
          core.dom.create(testTooltip1).appendTo("BODY");
          Tooltip.create('#testDiv', {content: 'abc'});
          assertEquals('abc', getContent('#testDiv'));
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// has
    //////////
    testHas: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var has = Tooltip.has;

          assertFunction('has of the Tooltip uielement is not a function!', has);

          assertFalse('False is not returned when non-existing originator is passed.', Tooltip.has('#testDiv'));
          core.dom.create(testTooltip1).appendTo("BODY");

          assertFalse('There should be no tooltip.', has('#testDiv'));
          Tooltip.create('#testDiv', {content: 'abc'});
          assertObject('abc', core.dom.select('#testDiv').data('qtip'));
          assertTrue('There should be a tooltip.', has('#testDiv'));
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// hide
    //////////
    testHide: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var hide = Tooltip.hide;

          assertFunction('hide of the Tooltip uielement is not a function!', hide);

          // Can't test it without CSS. Hide from qtip (without CSS included) doesn't change the display.
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// setContent
    //////////
    testSetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var setContent = Tooltip.setContent;

          assertFunction('setContent of the Tooltip uielement is not a function!', setContent);

          core.dom.create(testTooltip1).appendTo("BODY");
          Tooltip.create('#testDiv', {content: 'abc'});
          assertBoolean('Boolean value is not returned.', setContent('#testDiv', 'new content'));
          assertEquals('new content', core.dom.select('#testDiv').data('qtip').options.content.text);
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    },

    //////////
    ////////// show
    //////////
    testShow: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tooltip) {
          var show = Tooltip.show;

          assertFunction('show of the Tooltip uielement is not a function!', show);

          core.dom.create(testTooltip3).appendTo("BODY");
          core.dom.select('.qtip').each(function(){
            core.dom.select(this).remove();
          });
          Tooltip.create('#testDiv2');
          show('#testDiv2');
          assertEquals("block", core.dom.select('.qtip').css('display'));
        });

        core.module.use('Tooltip', moduleLoaded);
      });
    }
  });
}());