(function() {
  "use strict";

  var tabbarElement = '<div id="tabbarWrap">' +
                        '<div id="tabbar">' +
                          '<ul class="tabmenu">' +
                            '<li class="overviewclass active" id="overviewtab" data-key="overview" event="overview">' +
                              '<div class="tabsettings">' +
                                '<i class="icon-cogwheel lightgrey"></i>' +
                              '</div>' +
                              '<div module="../tabs/overview" class="label">overview</div>' +
                            '</li>' +
                            '<li id="attributes" data-key="attributes" event="attributes">' +
                              '<div class="tabsettings">' +
                                '<i class="icon-cogwheel lightgrey"></i>' +
                              '</div>' +
                              '<div module="../tabs/attributes" class="label">attributes</div>' +
                              '<div event="attributes::add" class="action addattribute ui-menu-initiator" data-key="add" style="display: none;" menuid="ui-menu-13342140755459">' +
                                '<i class="icon-plus"></i>' +
                                '<div module="#">add</div>' +
                              '</div>' +
                            '</li>' +
                            '<li event="more" class="moremenu">' +
                              '<i class="icon-list"></i>' +
                              '<div module="more" class="label">More</div>' +
                            '</li>' +
                          '</ul>' +
                          '<div class="active-tabmenu-item-wrapper" style="top: 10px;">' +
                            '<div class="active-tabmenu-item-bg" style="height: 33px;"></div>' +
                          '</div>' +
                        '</div>' +
                      '</div>';

  AsyncTestCase("modules :: menus :: tabbar :: tabbar.js", {

    //////////
    ////////// collapse
    //////////
    testCollapse: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var collapse = TabBar.collapse,
              tabbarNode, actionButton;

          assertFunction("collapse() from the TabBar is not a function.", collapse);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');
          actionButton = tabbarNode.find('.addattribute');

          assertFalse("Action Button should not be visible.", actionButton.is(':visible'));
          actionButton.show();
          assertTrue("Action Button should be visible.", actionButton.is(':visible'));

          assertFalse("expand() with not existing selector didn't return false.", collapse('#abcd'));
          assertTrue("expand() with existing selector didn't return true.", collapse('#tabbar'));
          assertFalse("Action Button should not be visible.", actionButton.is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// create
    //////////
    testCreate: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var create = TabBar.create,
              tabbarNode;

          assertFunction("create() from the TabBar is not a function.", create);

          core.dom.create('<div id="newtabbar"></div>').appendTo("BODY");

          assertFalse("Creating TabBar for selector that does not exist does not return false.", create('#abcd'));//Selector that does not exist.
          assertTrue("create() from TabBar does not return true.", create('#newtabbar'));

          tabbarNode = core.dom.select('#newtabbar');

          assertTrue("There is no DIV#tabbar after creating Tabbar.", core.dom.is(tabbarNode.find('DIV#tabbar')));
          assertTrue("There is no UL.tabmenu after creating Tabbar.", core.dom.is(tabbarNode.find('UL.tabmenu')));
          assertTrue("There is no DIV.active-tabmenu-item-wrapper after creating Tabbar.", core.dom.is(tabbarNode.find('DIV.active-tabmenu-item-wrapper')));
          assertTrue("There is no DIV.active-tabmenu-item-bg after creating Tabbar.", core.dom.is(tabbarNode.find('DIV.active-tabmenu-item-bg')));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// destroy
    //////////
    testDestroy: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var destroy = TabBar.destroy,
              tabbarNode;

          assertFunction("destroy() from the TabBar is not a function.", destroy);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          assertFalse("destroy() [from TabBar] for seletcor that does not exsit does not return false.", destroy('#abcd'));//Selector that does not exist.
          assertTrue("destroy() from TabBar does not return true.", destroy('#tabbar'));

          tabbarNode = core.dom.select('#tabbarWrap');

          //Some elements should still be inside tabbar.
          assertTrue("There is no DIV#tabbar after 'simple' destroy().", core.dom.is(tabbarNode.find('DIV#tabbar')));
          assertTrue("There is no UL.tabmenu after 'simple' destroy().", core.dom.is(tabbarNode.find('UL.tabmenu')));
          assertTrue("There is no DIV.active-tabmenu-item-wrapper after 'simple' destroy().", core.dom.is(tabbarNode.find('DIV.active-tabmenu-item-wrapper')));
          assertTrue("There is no DIV.active-tabmenu-item-bg 'simple' destroy().", core.dom.is(tabbarNode.find('DIV.active-tabmenu-item-bg')));
          assertFalse("A LI tag can be found after 'simple' destroy().", core.dom.is(tabbarNode.find('LI')));

          //Remove all elements.
          assertTrue(destroy('#tabbar', true));
          //Check if there is anything inside.
          assertFalse("HTML tags can be found after 'full' destroy().", core.dom.is(tabbarNode.children()));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// expand
    //////////
    testExpand: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var expand = TabBar.expand,
              tabbarNode;

          assertFunction("expand() from the TabBar is not a function.", expand);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');

          assertFalse("Action Button shouldn't be visible.", tabbarNode.find('.addattribute').is(':visible'));

          assertFalse("expand() with not existing selector didn't return false.", expand('#abcd'));
          assertTrue("expand() with existing selector didn't return true.", expand('#tabbar'));
          assertTrue("Action Button should be visible.", tabbarNode.find('.addattribute').is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.add
    //////////
    testTabsAdd: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var add = TabBar.Tabs.add,
              tabbarNode, newTab;

          assertFunction("add() from the TabBar is not a function.", add);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');

          assertFalse("Element shouldn't exist.", core.dom.is(core.dom.select('#abc')));
          assertFalse("add() with not existing selector didn't return false.", add('#zzzz', {
            id: 'abc'
          }));
          assertTrue("add() with existing selector didn't return true.", add('#tabbar', {
            id: 'abc',
            module: '../module5',
            domid: 'abc',
            domclass: 'xyz',
            index: 1,
            enablesettings: true
          }));

          newTab = core.dom.select('#abc');
          assertTrue('Element should exist.', core.dom.is(newTab));
          assertEquals('../module5', newTab.find('div.label').attr('module'));
          assertTrue('Element should have class "xyz".', newTab.hasClass('xyz'));
          assertTrue('Element should have edit cogwheel.', core.dom.is(newTab.find('div.tabsettings')));//It has settings cogwheel.
          assertTrue('Order of tabs is wrong.', tabbarNode.find('LI').eq(1).hasClass('xyz'));//New tab is 2nd.
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.addAction
    //////////
    testTabsAddAction: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var addAction = TabBar.Tabs.addAction,
              tabbarNode, newButton;

          assertFunction("addAction() from the TabBar is not a function.", addAction);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');

          assertFalse("Element shouldn't exist.", core.dom.is(core.dom.select('#abc')));
          assertFalse("addAction() with not existing selector didn't return false.", addAction('#zzzz', 'overview', {
            id: 'abc'
          }));
          assertTrue("add() with existing selector didn't return true.", addAction('#tabbar', 'overview', {
            id: 'abc',
            module: '../module5',
            domid: 'abc',
            domclass: 'xyz',
            icon: 'icon-plus'
          }));

          newButton = core.dom.select('#abc');
          assertTrue('Element should exist.', core.dom.is(newButton));
          assertEquals('../module5', newButton.find('div').attr('module'));
          assertTrue('Element should have class "xyz".', newButton.hasClass('xyz'));
          assertTrue('New button should have an icon.', newButton.find('i').hasClass('icon-plus'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.hide
    //////////
    testTabsHide: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var hide = TabBar.Tabs.hide,
              tabbarNode, attributesTab;

          assertFunction("hide() from the TabBar is not a function.", hide);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');
          attributesTab = tabbarNode.find('#attributes');

          assertTrue("The tab should be visible.", attributesTab.is(':visible'));

          assertFalse("hide() with not existing selector didn't return false.", hide('#abcd', 'attributes'));
          assertTrue("The tab should be visible.", attributesTab.is(':visible'));//Still visible.
          assertTrue("hide() with existing selector didn't return true.", hide('#tabbar', 'attributes'));
          assertFalse("The tab shouldn't be visible.", attributesTab.is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.hideAction
    //////////
    testTabsHideAction: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var hideAction = TabBar.Tabs.hideAction,
              tabbarNode, actionButton;

          assertFunction("hideAction() from the TabBar is not a function.", hideAction);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');
          actionButton = tabbarNode.find('.addattribute');
          actionButton.show();

          assertTrue("The button should be visible.", actionButton.is(':visible'));

          assertFalse("hideAction() with not existing selector didn't return false.", hideAction('#abcd', 'attributes', 'add'));
          assertTrue("The button should be visible.", actionButton.is(':visible'));//Still visible.
          assertTrue("hideAction() with existing selector didn't return true.", hideAction('#tabbar', 'attributes', 'add'));
          assertFalse("The button shouldn't be visible.", actionButton.is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.load
    //////////
    testTabsLoad: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var load = TabBar.Tabs.load,
              callback = sinon.spy();

          assertFunction("load() from the TabBar is not a function.", load);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          assertFalse("Callback was already fired.", callback.calledOnce);
          core.mediator.subscribe('tabbar::tab::changed', callback);
          core.mediator.subscribe('tabbar::tab::attributes::loaded', callback);
          assertFalse("Callback was already fired.", callback.calledOnce);

          assertTrue("load() didn't return true.", load('#tabbar', 'attributes', true));

          //I can't test if events from above are published because it's done after tab was loaded.
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.remove
    //////////
    testTabsRemove: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var remove = TabBar.Tabs.remove,
              tabbarNode;

          assertFunction("remove() from the TabBar is not a function.", remove);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');

          assertTrue("The tab should exist.", core.dom.is(tabbarNode.find('#attributes')));

          assertFalse("remove() with not existing selector didn't return false.", remove('#abcd', 'attributes'));
          assertTrue("The tab shouldn't be removed.", core.dom.is(tabbarNode.find('#attributes')));//Still visible.
          assertTrue("remove() with existing selector didn't return true.", remove('#tabbar', 'attributes'));
          assertFalse("The tab should be removed.", core.dom.is(tabbarNode.find('#attributes')));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.removeAction
    //////////
    testTabsRemoveAction: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var removeAction = TabBar.Tabs.removeAction,
              tabbarNode, actionButton;

          assertFunction("removeAction() from the TabBar is not a function.", removeAction);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');

          assertTrue("The button should exist.", core.dom.is(tabbarNode.find('.addattribute')));

          assertFalse("removeAction() with not existing selector didn't return false.", removeAction('#abcd', 'attributes', 'add'));
          assertTrue("The button shouldn't be removed.", core.dom.is(tabbarNode.find('.addattribute')));//Still visible.
          assertTrue("removeAction() with existing selector didn't return true.", removeAction('#tabbar', 'attributes', 'add'));
          assertFalse("The button should be removed.", core.dom.is(tabbarNode.find('.addattribute')));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.show
    //////////
    testTabsShow: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var show = TabBar.Tabs.show,
              tabbarNode, attributesTab;

          assertFunction("show() from the TabBar is not a function.", show);

          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');
          attributesTab = tabbarNode.find('#attributes');

          assertTrue("The tab should be visible.", attributesTab.is(':visible'));
          attributesTab.hide();
          assertFalse("The tab shouldn't be visible.", attributesTab.is(':visible'));

          assertFalse("show() with not existing selector didn't return false.", show('#abcd', 'attributes'));
          assertFalse("The tab shouldn't be visible.", attributesTab.is(':visible'));//Still not visible.
          assertTrue("show() with existing selector didn't return true.", show('#tabbar', 'attributes'));
          assertTrue("The tab should be visible.", attributesTab.is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    },

    //////////
    ////////// Tabs.showAction
    //////////
    testTabsShowAction: function(tabbarQueue) {
      tabbarQueue.call(function(callbacks) {
        var tabbarLoaded = callbacks.add(function(TabBar) {
          var showAction = TabBar.Tabs.showAction,
              tabbarNode, actionButton;

          assertFunction("collapse() from the TabBar is not a function.", showAction);
          
          //Create and insert tabbar.
          core.dom.create(tabbarElement).appendTo("BODY");

          tabbarNode = core.dom.select('#tabbarWrap');
          actionButton = tabbarNode.find('.addattribute');

          assertFalse("The button shouldn't be visible.", actionButton.is(':visible'));

          assertFalse("showAction() with not existing selector didn't return false.", showAction('#abcd', 'attributes', 'add'));
          assertFalse("The action button shouldn't be visible.", actionButton.is(':visible'));//Still visible.
          assertTrue("showAction() with existing selector didn't return true.", showAction('#tabbar', 'attributes', 'add'));
          assertTrue("The action button should be visible.", actionButton.is(':visible'));
        });

        core.module.use('TabBar', tabbarLoaded);
      });
    }
  });
}());
