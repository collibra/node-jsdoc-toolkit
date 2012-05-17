/**
 * Breadcrumbs.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module library/uielements/breadcrumbs
 * @alias BreadCrumbs
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/breadcrumbs',
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
  var _breadcrumb = {
    /**
     * @see BreadCrumbs.create
     */
    create: function(selector, options) {
      var breadcrumb, plgOptions,
          node = core.dom.select(selector);

      if (core.dom.is(node)) {
        plgOptions = {
          maxFinalElementLength: options.breadcrumb.expanded.max,
          minFinalElementLength: options.breadcrumb.expanded.min,
          minimumCompressionElements: options.minimumCompression,
          endElementsToLeaveOpen: options.breadcrumb.lastOpen,
          beginingElementsToLeaveOpen: options.breadcrumb.beginOpen
        };

        // Add breadcrumb to element.
        return node.jBreadCrumb(plgOptions).find('li.first a').addClass('ui-icon ui-icon-home').end();
      }

      return false;
    },

    /**
     * @see breadcrumb.removebreadcrumb
     */
    removebreadcrumb: function(selector, original, leavehome) {
      var home;
      selector = core.dom.select(selector);

      if (core.dom.is(selector)) {
        if (original) {
          selector.find('ul').remove();
        } else {
          if (leavehome) {
            home = selector.find('li.first');
            home.siblings().remove();
            home.addClass('last');
          } else {
            selector.find('li').remove();
          }
        }

        return true;
      }

      return false;
    },

    /**
     * @see BreadCrumbs.removeEntry
     */
    removeEntry: function(selector, index) {
      var item;
      selector = core.dom.select(selector);

      if (core.dom.is(selector)) {
        if(!index || selector.find('ul li').size() === index){
          item = selector.find('li.last');
          item.prev('li').addClass('last');
        } else if(index === 1) {
          return false; //Do not allow to remove 1st crumb - home button.
        } else {
          item = selector.find('li:nth-child(' + index + ')');
        }

        return item.remove();
      }

      return false;
    },

    /**
     * @see BreadCrumbs.addBreadcrumb
     */
    addEntry: function(breadcrumbTrail, label, action, index) {
      var item,
          breadsize,
          selector = core.dom.select(breadcrumbTrail).find('ul');

      if (core.dom.is(selector)) {
        breadsize = selector.find('li').size();

        //If "action" is a string then it's an URL.
        if(core.string.is(action)){
          label = '<li><a href="' + action + '">' + label + '</a></li>';
        } else {
          label = '<li><a href="#">' + label + '</a></li>';
        }

        if(!index || breadsize === index || index > breadsize){
          item = selector.find('li.last');

          core.dom.create(label, selector);
          item.removeClass('last');

          item = item.next('li');
          item.addClass('last');

        } else {
          item = selector.find('li:nth-child(' + index + ')');
          item.after(label);
          item = item.next('li');
        }

        if (core.func.is(action)) {
          action(item);
        }

        return true;
      }

      return false;
    },

    /**
     * @see BreadCrumbs.replaceEntry
     */
    replaceEntry: function(selector, changeindex, crumb, action) {
      var changeditem;
      selector = core.dom.select(selector).find('ul');

      if (core.dom.is(selector)) {
        //If "action" is a string then it's an URL.
        if(core.string.is(action)){
          crumb = '<a href="' + action + '">' + crumb + '</a>';
        } else {
          crumb = '<a href="#">' + crumb + '</a>';
        }

        changeditem = selector.find('li:nth-child(' + changeindex + ') a').replaceWith(crumb);

        if (core.func.is(action)) {
          //Pass new node after change.
          changeditem = selector.find('li:nth-child(' + changeindex + ') a');
          action(changeditem);
        }

        return true;
      }

      return false;
    },

    /**
     * @see BreadCrumbs.setLabel
     */
    setLabel: function(selector, index, label) {
      var changeditem;
      selector = core.dom.select(selector).find('ul');

      if (core.dom.is(selector)) {
        if (!index) {
          changeditem = selector.find('li.last a');
        } else {
          changeditem = selector.find('li:nth-child(' + index + ') a');
        }

        changeditem.text(label);

        return true;
      }

      return false;
    },

    /**
     * @see BreadCrumbs.setAction
     */
    setAction: function(selector, index, action) {
      var changeditem;
      selector = core.dom.select(selector).find('ul');

      if (core.dom.is(selector)) {
        if (!index) {
          changeditem = selector.find('li.last a');
        } else {
          changeditem = selector.find('li:nth-child(' + index + ') a');
        }

        //If "action" is a string then it's an URL.
        if(core.string.is(action)){
          changeditem.attr('href', action);
        }

        if (core.func.is(action)) {
          action(changeditem);
        }

        return true;
      }

      return false;
    },

    /**
     * @see BreadCrumbs.is
     */
    is: function(selector) {
      selector = core.dom.select(selector).find('ul');

      return (core.dom.is(selector) && selector.find('li').first().hasClass('first'));
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var BreadCrumbs = /** @lends BreadCrumbs */ {
    /**
     * Add element to the breadcrumb trail. If index is not passed, item is added to the end of breadcrumb trail.
     * If index is passed then new breadcrumb is added after the element with passed index.
     * @param {CoreObj/Selector} breadcrumbTrail A CoreObj or selector pointing to the breadcrumb trail.
     * @param {String} label The label for the entry.
     * @param {Function/String} action Callback function for added element or URL added to 'a' tag.
     * @param {Number} index Index of element after which new crumb is added. Index starts at 1. When index is 0, we add at the end.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    addBreadcrumb: function(breadcrumbTrail, label, action, index) {
      return _breadcrumb.addEntry(breadcrumbTrail, label, action, index);
    }.defaults('', '', '', 0),

    /**
     * Create breadcrumb trail.
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to a list.
     * @param {Object} [options] Options for breadcrumb.
     *   @option {Number} expanded.max=400 Maximum length of a breadcrumb fully expanded.
     *   @option {Number} expanded.min=200 Minimum length of a breadcrumb fully expanded.
     *   @option {Number} lastOpen=1 The number of breadcrumbs at the end of the trail that should always be expanded.
     *   @option {Number} beginOpen=0 The number of breadcrumbs at the beginning of the trail that should be open.
     *   @option {Number} minimumCompression=4 Start compressing at this amount of breadcrumbs.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(selector, options) {
      return _breadcrumb.create(selector, options);
    }.defaults('', {
      breadcrumb: {
        expanded: {
          max: 400,
          min: 200
        },
        lastOpen: 1,
        beginOpen: 0
      },
      minimumCompression: 4
    }),

    /**
     * Change the action of a breadcrumb.
     * @param {CoreObj/Selector} breadcrumbTrail A CoreObj or selector pointing to the breadcrumb trail.
     * @param {Number} index Index of element after which new crumb is added. Index starts at 1. When index is 0/undefined, we change the last entry.
     * @param {Function/String} action Callback function for added element or URL added to 'a' tag.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setAction: function(breadcrumbTrail, index, action) {
      _breadcrumb.setAction(breadcrumbTrail, index, action);
    }.defaults('', 1, ''),

    /**
     * Change the label of a breadcrumb entry.
     * @param {CoreObj/Selector} breadcrumbTrail A CoreObj or selector pointing to the breadcrumb trail.
     * @param {Number} index Index of element after which new crumb is added. Index starts at 1. When index is 0/undefined, we change the last entry.
     * @param {String} label The new label.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    // !TODO: Change into setLabel similar to setAction.
    setLabel: function(breadcrumbTrail, index, label) {
      _breadcrumb.setLabel(breadcrumbTrail, index, label);
    }.defaults('', 1, ''),

    /**
     * Check whether the selection is an actual breadcrumb.
     * @param {CoreObj/Selector} breadcrumb A CoreObj or selector pointing to a supposed breadcrumb.
     * @returns {Boolean} Whether the supposed breadcrumb is an actual breadcrumb.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    is: function(breadcrumb) {
      return _breadcrumb.is(breadcrumb);
    }.defaults(''),

    /**
     * Removes last entry from the breadcrumb.
     * @param {CoreObj/Selector} breadCrumb A CoreObj or selector pointing to the breadcrumb.
     * @returns {CoreObj} A CoreObj pointing to the element removed (in memory).
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    pop: function(breadcrumb) {
      return _breadcrumb.removeEntry(breadcrumb, false);
    }.defaults(''),

    /**
     * Adds a new entry to the breadcrumb.
     * @param {CoreObj/Selector} breadCrumb A CoreObj or selector pointing to the breadcrumb.
     * @param {String} label The label of the entry.
     * @param {Function/String} action Callback function for added element or URL added to 'a' tag.
     * @returns {Boolean} Whether the element is returned or not.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    push: function(breadcrumb, label, action) {
      return _breadcrumb.addEntry(breadcrumb, label, action);
    }.defaults('', '', ''),

    /**
     * Removes the entire breadcrumb trail functionality.
     * @param {CoreObj/Selector} breadcrumbTrail A CoreObj or selector pointing to the breadcrumb trail.
     * @param {Boolean} [original=false] Remove the original html source as well?
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(breadcrumbTrail, original) {
      _breadcrumb.removebreadcrumb(breadcrumbTrail, original, false);
    }.defaults('', false),

    /**
     * Removes all entries. Leaves 'home' crumb.
     * @param {CoreObj/Selector} breadcrumb A CoreObj or selector pointing to the breadcrumb.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    removeAllEntries: function(breadcrumb) {
      _breadcrumb.removebreadcrumb(breadcrumb, false, true);
    }.defaults(''),

    /**
     * Removes an entry in the breadcrumb.
     * @param {CoreObj/Selector} breadcrumb A CoreObj or selector pointing to the breadcrumb.
     * @param {Number} index Remove the entry at this index. Index starts at 1. When index is 0/undefined we remove from the end.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    removeEntry: function(breadcrumb, index) {
      _breadcrumb.removeEntry(breadcrumb, index);
    }.defaults(''),

    /**
     * Replace breadcrumb entry.
     * @param {CoreObj/Selector} breadcrumb A CoreObj or selector pointing to the breadcrumb.
     * @param {Number} index Index of element to replace.
     * @param {String} label Label of item that's going to added.
     * @param {Function/String} action Callback function for changed element or URL added to 'a' tag.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    replaceEntry: function(breadcrumb, index, label, action) {
      _breadcrumb.replaceEntry(breadcrumb, index, label, action);
    }.defaults('')
  };

  return BreadCrumbs;
});

core.module.alias.add('BreadCrumbs', 'library/uielements/breadcrumbs');
core.module.use('BreadCrumbs');