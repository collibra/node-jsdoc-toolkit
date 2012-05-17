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
  var testPopover1 = "<div id='p1' title='Content from title.'>abcd</div>",
      testPopover2 = "<p id='p2'>1234</p>",
      testPopover3 = "<div id='p3'>one two</div>",
      testPopover4 = "<div id='p4'>a</div>",
      testPopover5 = "<div id='p5'>b</div>",
      testPopover6 = "<div id='p6'>c</div>";

  new AsyncTestCase('uielements :: popover', {
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var create = PopOver.create;

          assertFunction('create of the PopOver uielement is not a function!', create);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          core.dom.create(testPopover2).appendTo("BODY");
          core.dom.create(testPopover3).appendTo("BODY");
          core.dom.create(testPopover4).appendTo("BODY");

          //Create popover.
          assertObject("", PopOver.create('#p1', {
            title: 'Nice title',
            icon: 'icon-plus',
            orientation: PopOver.ORIENTATION_ABOVE,
            hide: {
              manual: true,
              blur: false
            },
            show: {
              trigger: 'click'
            }
          }));

          //Check click initialization.
          assertFalse("Popover shouldn't be visible.", core.dom.select('DIV.popover').is(':visible'));
          //Initialize popover.
          core.dom.select('#p1').click();
          assertTrue("Popover shouldn't be visible.", core.dom.select('DIV.popover').is(':visible'));

          //There is no title bar.
          assertEquals("Nice title", core.dom.select('DIV.popover').find('.ui-tooltip-title').text());
          assertTrue("The popover should have icon-plus icon.", core.dom.select('DIV.popover').find('.ui-tooltip-titlebar I').hasClass('icon-plus'));
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar .ui-tooltip-close')));
          assertEquals('center', core.dom.select('#p1').data('qtip').options.position.at.x);
          assertEquals('top', core.dom.select('#p1').data('qtip').options.position.at.y);
          assertEquals('center', core.dom.select('#p1').data('qtip').options.position.my.x);
          assertEquals('bottom', core.dom.select('#p1').data('qtip').options.position.my.y);

          //Create popover.
          assertObject("", PopOver.create('#p2', {content: 'abc'}));
          //Initialize popover.
          core.dom.select('#p2').click();
          //default orientation
          assertEquals('center', core.dom.select('#p2').data('qtip').options.position.at.x);
          assertEquals('bottom', core.dom.select('#p2').data('qtip').options.position.at.y);
          assertEquals('center', core.dom.select('#p2').data('qtip').options.position.my.x);
          assertEquals('top', core.dom.select('#p2').data('qtip').options.position.my.y);

          //Create popover.
          assertObject("", PopOver.create('#p3', {content: 'abc', orientation: PopOver.ORIENTATION_LEFT}));
          //Initialize popover.
          core.dom.select('#p3').click();
          assertEquals('left', core.dom.select('#p3').data('qtip').options.position.at.x);
          assertEquals('center', core.dom.select('#p3').data('qtip').options.position.at.y);
          assertEquals('right', core.dom.select('#p3').data('qtip').options.position.my.x);
          assertEquals('center', core.dom.select('#p3').data('qtip').options.position.my.y);

          //Create popover.
          assertObject("", PopOver.create('#p4', {content: 'abc', orientation: PopOver.ORIENTATION_RIGHT}));
          //Initialize popover.
          core.dom.select('#p4').click();
          assertEquals('right', core.dom.select('#p4').data('qtip').options.position.at.x);
          assertEquals('center', core.dom.select('#p4').data('qtip').options.position.at.y);
          assertEquals('left', core.dom.select('#p4').data('qtip').options.position.my.x);
          assertEquals('center', core.dom.select('#p4').data('qtip').options.position.my.y);
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// getIcon
    //////////
    testGetIcon: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var getIcon = PopOver.getIcon;

          assertFunction('getIcon of the PopOver uielement is not a function!', getIcon);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          PopOver.create('#p1', {title:'Title', hide: {blur: false, manual: true}});
          core.dom.create(testPopover2).appendTo("BODY");
          PopOver.create('#p2', {content: 'abc'});

          //Check if false is returned when there is no icon.
          assertFalse('Returned value is not equal to false when there is no icon.', getIcon('#p1'));
          assertFalse('Returned value is not equal to false when there is no title bar.', getIcon('#p2'));
          assertFalse('Returned value is not equal to false when there is no element.', getIcon('#p3'));

          //Create element with an icon and click it to initialize and add icon.
          core.dom.create(testPopover3).appendTo("BODY");
          PopOver.create('#p3', {content: 'abc', title:'Title', icon: 'icon-home', hide:{blur: false, manual: true}}).click();

          assertEquals('icon-home', getIcon('#p3'));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// getTitle
    //////////
    testGetTitle: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var getTitle = PopOver.getTitle;

          assertFunction('getTitle of the PopOver uielement is not a function!', getTitle);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");

          //Check element without popover.
          assertFalse('Returned value is not equal to false when element has no popover.', getTitle('#p1'));

          PopOver.create('#p1', {hide: {blur: false, manual: true}});
          core.dom.create(testPopover2).appendTo("BODY");
          PopOver.create('#p2', {title:'Title', content: 'abc'});

          //Check if false is returned when there is no title.
          assertFalse('Returned value is not equal to false when there is no title.', getTitle('#p1'));

          //When title is set:
          assertEquals('Title', getTitle('#p2'));

          //When title is set along with other options
          core.dom.create(testPopover3).appendTo("BODY");
          PopOver.create('#p3', {title:'Title', content: 'abc', hide: {blur: false, manual: false}});
          assertEquals('Title', getTitle('#p3'));

          core.dom.create(testPopover4).appendTo("BODY");
          PopOver.create('#p4', {title:'Title', content: 'abc', hide: {blur: true, manual: true}});
          assertEquals('Title', getTitle('#p4'));

          core.dom.create(testPopover5).appendTo("BODY");
          PopOver.create('#p5', {title:'Title', content: 'abc', hide: {blur: true, manual: false}});
          assertEquals('Title', getTitle('#p5'));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// has
    //////////
    testHas: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var has = PopOver.has;

          assertFunction('has of the PopOver uielement is not a function!', has);

          //Not existing selector.
          assertFalse('Returned value is not equal to false when element does not exist.', has('#p1'));
          
          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");

          //Element without popover.
          assertFalse('Returned value is not equal to false when element has no popover.', has('#p1'));

          //Element with popover.
          PopOver.create('#p1', {hide: {blur: false, manual: true}});
          assertTrue('Returned value is not equal to true, for element with a popover.', has('#p1'));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// hideCloseButton
    //////////
    testHideCloseButton: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var hideCloseButton = PopOver.hideCloseButton;

          assertFunction('hideCloseButton of the PopOver uielement is not a function!', hideCloseButton);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");

          //Create popover.
          assertObject("", PopOver.create('#p1', {title: "Title", hide: {blur: true, manual: true}}));
          //Initialize popover.
          core.dom.select('#p1').click();

          //There is a title bar, but there is no close button.
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar .ui-tooltip-close')));
          assertTrue('Returned value is not equal to true when element has a popover.', hideCloseButton('#p1'));
          assertFalse("The popover should have a close button.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar .ui-tooltip-close')));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// setContent
    //////////
    testSetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var setContent = PopOver.setContent;

          assertFunction('setContent of the PopOver uielement is not a function!', setContent);

        //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          core.dom.create(testPopover2).appendTo("BODY");

          //Element without popover.
          assertFalse('Returned value is not equal to false when element has no popover.', setContent('#p1', 'xyz'));

          //Create popover.
          assertObject("", PopOver.create('#p1'));
          //Initialize popover.
          core.dom.select('#p1').click();

          //There is content already.
          assertEquals("Content from title.", core.dom.select('DIV.popover').find('.ui-tooltip-content').text());
          assertObject('Returned value from setContent() is not equal to true.', setContent('#p1', 'abcd'));
          assertEquals("abcd", core.dom.select('DIV.popover').find('.ui-tooltip-content').text());
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// showCloseButton
    //////////
    testShowCloseButton: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var showCloseButton = PopOver.showCloseButton;

          assertFunction('showCloseButton of the PopOver uielement is not a function!', showCloseButton);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          core.dom.create(testPopover2).appendTo("BODY");
          core.dom.create(testPopover3).appendTo("BODY");
          core.dom.create(testPopover4).appendTo("BODY");
          
          //Element without popover.
          assertFalse('Returned value is not equal to false when element has no popover.', showCloseButton('#p1'));

          //Create popover.
          assertObject("", PopOver.create('#p1', {title: "Title", hide: {blur: true, manual: false}}));
          //Initialize popover.
          core.dom.select('#p1').click();

          //There is a title bar, but there is no close button.
          assertFalse("The popover shouldn't have a close button.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar .ui-tooltip-close')));
          assertTrue('Returned value is not equal to true when element has a popover.', showCloseButton('#p1'));
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar .ui-tooltip-close')));

          //Create another popover.
          PopOver.create('#p2', {title: "Title", content: 'abc', hide: {blur: true, manual: true}});
          //Initialize popover.
          core.dom.select('#p2').click();

          //There is a titlebar, and there is a close button.
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar .ui-tooltip-close')));
          assertTrue('Returned value is not equal to true when element has a popover.', showCloseButton('#p2'));
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar .ui-tooltip-close')));

          //Element with popover.
          PopOver.create('#p3', {title: "Title", content: 'abc', hide: {blur: false, manual: false}});
          //Initialize popover.
          core.dom.select('#p3').click();

          //There is no title bar, and no close button.
          assertFalse("The popover shouldn't have a close button.", core.dom.is(core.dom.select('#p3').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar .ui-tooltip-close')));
          assertTrue('Returned value is not equal to true when element has a popover.', showCloseButton('#p3'));
          assertTrue("The popover should have a close button.", core.dom.is(core.dom.select('#p3').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar .ui-tooltip-close')));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// setIcon
    //////////
    testSetIcon: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var setIcon = PopOver.setIcon;

          assertFunction('setIcon of the PopOver uielement is not a function!', setIcon);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          core.dom.create(testPopover2).appendTo("BODY");

          //Create popover.
          assertObject("", PopOver.create('#p1', {title: "Title"}));
          //Initialize popover.
          core.dom.select('#p1').click();

          //There is a title bar.
          assertFalse("The popover shouldn't have an icon.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar I')));
          assertTrue('Returned value from setIcon() is not equal to true.', setIcon('#p1', 'icon-plus'));
          assertTrue("The popover should have an icon.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-titlebar I')));
          assertTrue("The popover should have icon-plus icon.", core.dom.select('DIV.popover').find('.ui-tooltip-titlebar I').hasClass('icon-plus'));

          //Create another popover.
          PopOver.create('#p2', {content: 'abc'});
          //Initialize popover.
          core.dom.select('#p2').click();

          //There is no titlebar.
          assertFalse("The popover shouldn't have an icon.", core.dom.is(core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar I')));
          assertFalse('Returned value is not false.', setIcon('#p2', 'icon-plus'));
          assertFalse("The popover shouldn't have an icon.", core.dom.is(core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-titlebar I')));
        });

        core.module.use('PopOver', moduleLoaded);
      });
    },

    //////////
    ////////// setTitle
    //////////
    testSetTitle: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(PopOver) {
          var setTitle = PopOver.setTitle;

          assertFunction('setTitle of the PopOver uielement is not a function!', setTitle);

          //Create dom elements.
          core.dom.create(testPopover1).appendTo("BODY");
          core.dom.create(testPopover2).appendTo("BODY");

          //Element without popover.
          assertFalse('Returned value is not equal to false when element has no popover.', setTitle('#p1', 'xyz'));

          //Create popover.
          assertObject("", PopOver.create('#p1'));
          //Initialize popover.
          core.dom.select('#p1').click();

          //There is no title bar.
          assertFalse("The popover shouldn't have a titlebar.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-title')));
          assertTrue('Returned value from setTitle() is not equal to true.', setTitle('#p1', 'Title'));
          assertTrue("The popover should have a titlebar.", core.dom.is(core.dom.select('DIV.popover').find('.ui-tooltip-title')));
          assertEquals("Title", core.dom.select('DIV.popover').find('.ui-tooltip-title').text());

          //Create another popover.
          PopOver.create('#p2', {content: 'abc', title: 'abc'});
          //Initialize popover.
          core.dom.select('#p2').click();

          //There is no titlebar.
          assertTrue("The popover should have a titlebar.", core.dom.is(core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-title')));
          assertTrue('Returned value from setTitle() is not equal to true.', setTitle('#p2', 'New title'));
          assertEquals("New title", core.dom.select('#p2').data('qtip').elements.tooltip.find('.ui-tooltip-title').text());
        });

        core.module.use('PopOver', moduleLoaded);
      });
    }
  });
}());