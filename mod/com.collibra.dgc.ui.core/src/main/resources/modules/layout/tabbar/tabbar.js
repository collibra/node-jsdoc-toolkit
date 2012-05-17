/**
 * Tab Bar.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module modules/layout/tabbar
 * @alias TabBar
 * @namespace modules/layout
 */
/*global define,require,core */
define('modules/layout/tabbar',
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
  var _tabBar = {
    tabbar: '#tabbar',
    cogwheel: '<div class="tabsettings"><i class="icon-cogwheel lightgrey"></i></div>',

    _init: function(bar) {
      var _self = this,
          tabs,
          tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar);

      _self.prerenderBackground(tabbar);

      core.URL.hash.watch('tab', function(newVal, oldVal) {
        if (newVal !== undefined) {
          _self.Tabs.load(tabbar, newVal, true);
        }
      });

      // !TODO: Publish the id of the current tab (at first display).
      // core.URL.hash.put('tab', );

      if (core.dom.is(tabbar)) {
        //Create deferred object for tabbar.
        core.mediator.promise('tabbar::events::loaded');

        //Publish events.
        tabbar.on('click', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::click', [event]);
        }).on('mouseenter', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::mouseenter', [event]);
        }).on('mouseleave', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::mouseleave', [event]);
        }).on('mouseover', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::mouseover', [event]);
        }).on('mousedown', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::mousedown', [event]);
        }).on('mouseout', 'li[event], div[event]', function(event) {
          event.stopPropagation();
          core.mediator.publish('tabbar::tab::' + this.getAttribute('event') + '::mouseout', [event]);
        }).on('click', 'li[event]', function(event) {
          //Limit detection of "change" event only to LI tag.
          var newTab = core.dom.select(this),
              oldTab, module, eventAttr, tab;

          //Don't move background if active tab was clicked or "More" tab.
          if (!newTab.hasClass('active') && !newTab.hasClass('moremenu')) {
            //Another (not active) tab clicked, time to change tabs.
            oldTab = newTab.siblings('.active').removeClass('active');
            newTab.addClass('active');

            //Show action buttons.
            oldTab.find('.action').hide();
            newTab.find('.action').show();

            //Load module.
            module = newTab.find('div.label').attr('regionpath');
            if (module) {
              _self.loadTabContent(newTab, module, oldTab);
            }

            //Remove edit mode from old tab's cogwheel.
            oldTab.find('.tabsettings').removeClass('edit');

            core.mediator.publish('tabbar::tab::change', [newTab.data('key')]);
          }
        });

        _self.cogwheelEvents(tabbar);

        //Subscribe change event to move background for tabs added in the future.
        core.mediator.subscribe('tabbar::tab::change', function(newTabID) {
          _self.moveBackground(newTabID, tabbar);
        });

        core.mediator.publish('tabbar::events::loaded');
      }
    },

    /**
     * Cogwheel events manipulation.
     * @param {CoreObj} tabbar Object pointing to dom element that wrapps ul tab bar list.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    cogwheelEvents: function(tabbar) {
      var _self = this,
          tabID;

      //Publish enter settings event.
      tabbar.on('click', 'DIV.tabsettings:not(.edit)', function(event) {
        event.stopPropagation();
        tabID = core.dom.select(this).parent().data('key');

        core.mediator.publish('tabbar::tab::settings::enter', [tabID]);
      });

      //Publish exit settings event.
      tabbar.on('click', 'DIV.tabsettings.edit', function(event) {
        event.stopPropagation();
        tabID = core.dom.select(this).parent().data('key');

        core.mediator.publish('tabbar::tab::settings::exit', [tabID]);
      });

      //Settings enter.
      core.mediator.subscribe('tabbar::tab::settings::enter', function(tabID) {
        var tab;

        if (!core.string.is(tabID)) {
          //Use current tab.
          tab = tabbar.find('LI.active');
          tabID = tab.data('key');
        } else {
          //TabID passed. Find the tab.
          tab = tabbar.find('LI[data-key="' + tabID + '"]');
        }

        //Change mode - add class
        tab.find('.tabsettings').addClass('edit');

        //Entered setting state - publish an event.
        core.mediator.publish('tabbar::tab::' + tabID + '::settings::entered', [tab]);
      });

      //Settings exit.
      core.mediator.subscribe('tabbar::tab::settings::exit', function(tabID) {
        var tab;

        if (!core.string.is(tabID)) {
          //Use current tab.
          tab = tabbar.find('LI.active');
          tabID = tab.attr('data-key');
        } else {
          //TabID passed. Find the tab.
          tab = tabbar.find('LI[data-key="' + tabID + '"]');
        }

        //Change mode - add class
        tab.find('.tabsettings').removeClass('edit');

        //Entered setting state - publish an event.
        core.mediator.publish('tabbar::tab::' + tabID + '::settings::exited', [tab]);
      });
    },

    /**
     * Loads content of the tab. Uses deferred object and publishes events when content is loaded.
     * @param {CoreObj} newTab The tab which content is needed to show.
     * @param {String} module The module to load.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>, <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    loadTabContent: function(newTab, tabRegion, oldTab) {
      var eventAttr;

      //If there was no oldTab then pass false.
      if (!oldTab) {
        oldTab = false;
      }

      eventAttr = newTab.attr('event');
      //Create deferred object.
      core.mediator.promise('tabbar::tab::' + eventAttr + '::loaded');
      
      core.module.region.get(tabRegion, function(data) {
        core.dom.select('.rightcolumn > .pagecontent').html(data);

        core.mediator.publish('tabbar::tab::' + eventAttr + '::loaded', [newTab]); //Resolve the deferred after content is loaded.
        core.mediator.publish('tabbar::tab::changed', [oldTab, newTab]); //Module is loaded. The tab is now changed.

        core.URL.hash.put('tab', newTab.attr('data-key'));
      });
    },

    /**
     * Move white background to the first active tab after page load.
     * @param {CoreObj} tabbar Object pointing to dom element that wrapps ul tab bar list.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    prerenderBackground: function(tabbar) {
      var _self = this;

      core.dom.create('<div class="active-tabmenu-item-wrapper"><div class="active-tabmenu-item-bg"></div></div>').appendTo(tabbar);

      _self.updateBackgroundPosition(tabbar);
    },

    /**
     * Animate moving white background to active tab.
     * @param {CoreObj} newTab Object pointing to the tab that was clicked.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    moveBackground: function(newTabID, tabbar) {
      var _self = this,
          newTab = tabbar.find('LI[data-key="' + newTabID + '"]'),
          background = tabbar.find('DIV.active-tabmenu-item-wrapper');

      core.dom.animate(background, {
        top: newTab.position().top
      });

      core.dom.animate(background.find('DIV.active-tabmenu-item-bg'), {
        height: newTab.outerHeight()
      });

    },

    /**
     * Updated position of active tab's white background. Useful for adding new tabs.
     * @param {CoreObj} tabbar Object pointing to dom element that wrapps ul tab bar list.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    updateBackgroundPosition: function(tabbar) {
      var _self = this,
          active = tabbar.find('li.active');

      if (core.dom.is(tabbar) && core.dom.is(active)) {
        tabbar.find('DIV.active-tabmenu-item-wrapper').css({
          top: active.position().top
        }).find('div').css({
          height: active.outerHeight()
        });

        return true;
      }

      return false;
    },

    /**
     * @see TabBar.collapse
     */
    collapse: function(bar) {
      var _self = this,
          tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar);

      if (core.dom.is(tabbar)) {
        tabbar.find('div.action').hide();
        _self.updateBackgroundPosition(tabbar);

        return true;
      }

      return false;
    },

    /**
     * @see TabBar.create
     */
    create: function(selector) {
      var _self = this,
          node = core.dom.select(selector);

      if (core.dom.is(node)) {
        node.append('<div id="tabbar"><ul class="tabmenu"></ul></div>');

        _self._init(node.find('div#tabbar'));

        return true;
      }

      return false;
    },

    /**
     * @see TabBar.destroy
     */
    destroy: function(bar, original) {
      var _self = this,
          tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar);

      if (core.dom.is(tabbar) && tabbar.attr('id') === 'tabbar') {
        if (original) {
          //Remove everything.
          tabbar.remove();
        } else {
          //Remove lis and hide background.
          tabbar.find('li').remove();
          tabbar.find('div.active-tabmenu-item-bg').css('height', 0);
        }

        return true;
      }

      return false;
    },

    /**
     * @see TabBar.expand
     */
    expand: function(bar) {
      var _self = this,
          tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar);
    
      if (core.dom.is(tabbar)) {
        tabbar.find('div.action').show();
        _self.updateBackgroundPosition(tabbar);

        return true;
      }

      return false;
    },

    Tabs: {
      /**
       * @see TabBar.Tabs.add
       */
      add: function(bar, options) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li:visible'),//Do not include hidden tabs - this is important for inserting order.
            size = tabs.length,
            newTab, newLabel, oldTab;

        if (core.dom.is(tabbar) && core.dom.is(tabs) && core.string.is(options.id)) {
          newTab = core.dom.create('<li event="' + options.id + '" data-key="' + options.id + '"></li>');

          //Add setting cogwheel.
          if (options.enablesettings) {
            newTab.append(_self.cogwheel);
          }

          //Create new label and add tags if options are passed.
          newLabel = core.dom.create('<div class="label">' + options.id + '</div>'); //TODO: translations

          if (options.module) {
            newLabel.attr('module', options.module);
          }
          if (options.domid) {
            newTab.attr('id', options.domid);
          }
          if (options.domclass) {
            newTab.addClass(options.domclass);
          }

          //Add label.
          newTab.append(newLabel);

          //TODO: add fadein on creation
          if (!options.index || options.index >= size || options.index < 0) {
            //Insert at the end, before "more".
            newTab.insertBefore(tabs.filter('.moremenu'));
          } else {
            newTab.insertBefore(tabs.eq(options.index)); //Since .eq() starts at 0 and we want to start at 1 use insertBefore instead of insertAfter.
          }

          //Update position of active background.
          //TODO: execute only if new element inserted before active.
          _self.updateBackgroundPosition(tabbar);

          if (options.active) {
            oldTab = tabs.filter('.active').removeClass('active');
            newTab.addClass('active');

            core.mediator.publish('tabbar::tab::change', [newTab.data('key')]);
          }

          if (options.addAction) {
            _self.Tabs.addAction(bar, options.id, options.addAction);
          }

          return true;
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.addAction
       */
      addAction: function(bar, tabid, options) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li'),
            theTab, eventName, newAction, newActionDiv;

        //There has to be at least one tab.
        if (core.dom.is(tabs) && options.id) {
          theTab = tabs.filter('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            eventName = theTab.attr('event');

            newAction = core.dom.create('<div class="action" data-key="' + options.id + '" event="' + eventName + '::' + options.id + '"></div>'); //TODO: translations

            if (options.icon) {
              newAction.append('<i class="' + options.icon + '"></i>');
            }

            newActionDiv = core.dom.create('<div>' + options.id + '</div>');

            if (options.module) {
              newActionDiv.attr('module', options.module);
            }
            if (options.domid) {
              newAction.attr('id', options.domid);
            }
            if (options.domclass) {
              newAction.addClass(options.domclass);
            }

            newAction.append(newActionDiv);
            theTab.append(newAction);

            //If tab is active then show new action button.
            if (theTab.hasClass('active')) {
              theTab.find('.action').show();
            }

            //Update active background.
            _self.updateBackgroundPosition(tabbar);
            return true;
          }
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.hide
       */
      hide: function(bar, tabid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li:visible'),//Get only visible tabs so we won't hide hidden ones again.
            size = tabs.length,
            theTab, newTab;

        //Hide only if there is more than one tab apart from "More".
        if (size > 1 && tabid) {
          theTab = tabbar.find('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            if (theTab.hasClass('active')) {
              //Set 1st tab as new tab shown.
              newTab = tabs.eq(0);

              //When we hide 1st tab:
              if (newTab.hasClass('active') && tabs.eq(1).hasClass('moremenu')) {
                //We want to hide 1st tab that is active and we have no more tabs other than "More" tab, so we hide "active" background.
                tabbar.find('div.active-tabmenu-item-bg').css('height', 0);
                theTab.removeClass('active').hide();

                return true;
              } else if (newTab.hasClass('active')) {
                //Because we hide 1st tab, show 2nd one.
                newTab = tabs.eq(1);
              }

              theTab.removeClass('active').hide();
              newTab.trigger('click');
            } else {
              theTab.hide();
            }

            _self.updateBackgroundPosition(tabbar);
  
            return true;
          }
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.hideAction
       */
      hideAction: function(bar, tabid, actionid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            action = _self.Tabs.findAction(tabbar, tabid, actionid);

        if (action) {
          //Hide action.
          action.hide();

          //Update active background.
          _self.updateBackgroundPosition(tabbar);
          return true;
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.load
       */
      load: function(bar, tabid, active) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li'),
            theTab, module;

        //There has to be at least one tab.
        if (core.dom.is(tabs) && tabid) {
          theTab = tabs.filter('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            if (active) {
              theTab.trigger('click');

              return true;
            }

            module = theTab.find('div.label').attr('regionpath');

            //Load module.
            if (module) {
              _self.loadTabContent(theTab, module);
            }
          }
          return true;
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.remove
       */
      remove: function(bar, tabid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li'),
            size = tabs.length,
            theTab, newTab;

        //Remove only if there is more than one tab.
        if (size > 1 && tabid) {
          theTab = tabbar.find('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            if (theTab.hasClass('active')) {
              //Set 1st tab as new tab shown.
              newTab = tabs.eq(0);

              //When we remove 1st tab:
              if (newTab.hasClass('active') && tabs.eq(1).hasClass('moremenu')) {
                //We want to remove 1st tab that is active and we have no more tabs other than "More" tab, so we hide "active" background.
                tabbar.find('div.active-tabmenu-item-bg').css('height', 0);
                theTab.remove();

                return true;
              } else if (newTab.hasClass('active')) {
                //Because we hide 1st tab, show 2nd one.
                newTab = tabs.eq(1);
              }

              theTab.remove();
              newTab.trigger('click');
            } else {
              theTab.remove();
            }

            _self.updateBackgroundPosition(tabbar);

            return true;
          }
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.removeAction
       */
      removeAction: function(bar, tabid, actionid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            action = _self.Tabs.findAction(tabbar, tabid, actionid);

        if (action) {
          //Remove action.
          action.remove();

          //Update active background.
          _self.updateBackgroundPosition(tabbar);
          return true;
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.show
       */
      show: function(bar, tabid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            tabs = tabbar.find('li'),
            theTab, newTab;

        if (core.string.is(tabid)) {
          theTab = tabbar.find('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            theTab.show();

            _self.updateBackgroundPosition(tabbar);

            return true;
          }
        }

        return false;
      },

      /**
       * @see TabBar.Tabs.showAction
       */
      showAction: function(bar, tabid, actionid) {
        var _self = _tabBar,
            tabbar = (bar) ? core.dom.select(bar) : core.dom.select(_self.tabbar),
            action = _self.Tabs.findAction(tabbar, tabid, actionid);

        if (action) {
          //Show action.
          action.show();

          //update active background
          _self.updateBackgroundPosition(tabbar);
          return true;
        }

        return false;
      },

      /**
       * It's used to find action button based on action and tab id passed to other methods.
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @param  {String} actionid An id that represents an action. It's used for translations and it's also stored as data-key property added to the html tag.
       * @return {Boolean} Returns CoreObj on success, false on failure.
       * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      findAction: function(tabbar, tabid, actionid) {
        var _self = _tabBar,
            tabs = tabbar.find('li'),
            theTab, action;

        if (core.dom.is(tabs)) {
          theTab = tabbar.find('li[data-key="' + tabid + '"]');

          if (core.dom.is(theTab)) {
            action = theTab.find('div.action[data-key="' + actionid + '"]');

            if (core.dom.is(action)) {
              //The action.
              return action;
            }
          }
        }

        return false;
      }
    }
  };

  core.dom.ready(function() {
    _tabBar._init();
  });

  /*************************************
   *               Public              *
   *************************************/

  var TabBar = /** @lends TabBar */{

    /**
     * Collapse extanded actions on every tab.
     *
     * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul with tabs.
     * @return {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    collapse: function(tabbar) {
      return _tabBar.collapse(tabbar);
    }.defaults(''),

    /**
     * Creates tabbar structure and events.
     *
     * @param  {CoreObj/String} selector Selector or object pointing to a container where new tabbar will be added.
     * @return {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(selector) {
      return _tabBar.create(selector);
    }.defaults(''),

    /**
     * Removes every tab from the tabbar, leaving empty ul tag (check "original" option).
     *
     * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul with tabs.
     * @param {Boolean} original=false If true then everything inside passed selector is removed (both ul, dynamic background and wrapping div).
     * @return {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(tabbar, original) {
      return _tabBar.destroy(tabbar, original);
    }.defaults('', false),

    /**
     * Expand collapsed actions on every tab.
     *
     * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul with tabs.
     * @return {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    expand: function(tabbar) {
      return _tabBar.expand(tabbar);
    }.defaults(''),

    Tabs: /* @lends Tabs */ {
      /**
       * Add a single tab to the tabbar. Only visible tabs are taken into account when new tab is inserted.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul with tabs.
       * @param  {Object} options Object with information about new element.
       *   @option {String} id="" Translation ID used to pull tab's label
       *   @option {String} module="" Module to execute upon clicking
       *   @option {String} domid="" The ID attribute for html tag. Added to li tag.
       *   @option {String} domclass="" The class attribute for html tag. Added to li tag.
       *   @option {Number} index=0 Index of an element after which new tab is added. Counting starts at 1. If 0, empty value or value larger then number of elements is passed, then new tab is inserted before "more" tab.
       *   @option {Boolean} active=false Make this tab active after inserting? Reloads content of main window. //TODO reload content
       *   @option {Boolean} enablesettings=false Enable cogwheel settings for the tab.
       *   @option {Object/Boolean} addAction=false If true, this object is passed to TabBar.Tabs.addAction()
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      add: function(tabbar, options) {
        return _tabBar.Tabs.add(tabbar, options);
      }.defaults('', {
        id: '',
        module: '',
        domid: '',
        domclass: '',
        index: 0,
        active: false,
        enablesettings: false,
        addAction: false
      }),

      /**
       * Adds the action button to the tab.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid The tab's id [stored in data-key custom attribute and as id in properties file].
       * @param  {Object} options Object with information about new element.
       *   @option {String} id="" Translation ID used to pull tab's label.
       *   @option {String} module="" Module to execute upon clicking
       *   @option {String} domid="" The ID attribute for html tag.
       *   @option {String} domclass="" The class attribute for html tag.
       *   @option {String} icon="" The for new action button. For e.g. 'icon-plus'.
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      addAction: function(tabbar, tabid, options) {
        return _tabBar.Tabs.addAction(tabbar, tabid, options);
      }.defaults('', '', {
        id: '',
        module: '',
        domid: '',
        domclass: '',
        icon: ''
      }),

      /**
       * Hides a tab.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid The tab's id [stored in data-key custom attribute and as id in properties file].
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      hide: function(tabbar, tabid) {
        return _tabBar.Tabs.hide(tabbar, tabid);
      }.defaults('', ''),

      /**
       * Hides an action for the tab.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid The tab's id [stored in data-key custom attribute and as id in properties file].
       * @param  {String} actionid The id from action button [stored in data-key custom attribute and as id in properties file].
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      hideAction: function(tabbar, tabid, actionid) {
        return _tabBar.Tabs.hideAction(tabbar, tabid, actionid);
      }.defaults('', '', ''),

      /**
       * Load module from the tab.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @param  {Boolean} active=true Make this tab active?
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      load: function(tabbar, tabid, active) {
        return _tabBar.Tabs.load(tabbar, tabid, active);
      }.defaults('', '', true),

      /**
       * Removes a tab from the tabbar.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      remove: function(tabbar, tabid) {
        return _tabBar.Tabs.remove(tabbar, tabid);
      }.defaults('', false),

      /**
       * Removes the action button from the tab.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @param  {String} actionid An id that represents an action. It's used for translations and it's also stored as data-key property added to the html tag.
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      removeAction: function(tabbar, tabid, actionid) {
        return _tabBar.Tabs.removeAction(tabbar, tabid, actionid);
      }.defaults('', '', ''),

      /**
       * Hides a tab from the tabbar.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      show: function(tabbar, tabid) {
        return _tabBar.Tabs.show(tabbar, tabid);
      }.defaults('', ''),

      /**
       * Show an action button that was hidden before.
       *
       * @param  {CoreObj/String} tabbar Selector or object pointing to div element that is wrapped around ul containing tabs.
       * @param  {String} tabid An id that represents a tab. It's used for translations and it's also stored as data-key property added to the html tag.
       * @param  {String} actionid An id that represents an action. It's used for translations and it's also stored as data-key property added to the html tag.
       * @return {Boolean} Returns true on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      showAction: function(tabbar, tabid, actionid) {
        return _tabBar.Tabs.showAction(tabbar, tabid, actionid);
      }.defaults('', '', '')
    }
  };

  return TabBar;
});

core.module.alias.add('TabBar', 'modules/layout/tabbar');
core.module.use('TabBar');
