/**
 * Pop-over.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @module library/uielements/popover
 * @alias PopOver
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/popover',
       ['core', 'library/uielements/tooltip'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      tooltip = require('Tooltip');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _popover = {
    TYPE_DEFAULT: 'default',
    TYPE_WARNING: 'warning',
    TYPE_DANGER: 'danger',
    TYPE_SUCCESS: 'success',
    /**
     * @see PopOver.create
     */
    create: function(selector, options) {
      var _self = this,
          eventsObj = {}, positionObj, hideObj, contentObj, ajaxObj, tipDimensions,
          element = core.dom.select(selector, true),
          actionDone = false,
          tipDimensionsHorizontal = {
            width: 40,
            height: 20
          },
          tipDimensionsVertical = {
            width: 20,
            height: 40
          };

      core.dom.each(selector, function(element){
        if (!options.content && !options.url) {
          options.content = element.attr('title'); //If content is empty then get title attribute from the selector.
        } else if (options.url) {
          options.content = 'Loading...'; //TODO: replace with image
        }

        //Prepare object with tooltip position.
        switch(options.orientation) {
          case tooltip.ORIENTATION_ABOVE:
            positionObj = {
              my: "bottom center",
              at: "top center"
            };
            tipDimensions = tipDimensionsHorizontal;
            break;
          case tooltip.ORIENTATION_LEFT:
            positionObj = {
              my: "right center",
              at: "left center"
            };
            tipDimensions = tipDimensionsVertical;
            break;
          case tooltip.ORIENTATION_RIGHT:
            positionObj = {
              my: "left center",
              at: "right center"
            };
            tipDimensions = tipDimensionsVertical;
            break;
          default:
            positionObj = {
              my: "top center",
              at: "bottom center"
            };
            tipDimensions = tipDimensionsHorizontal;
        }

        //Prepare object with content.
        contentObj = {
          text: options.content,
          title: {
            text: options.title
          }
        };
        //Enable close button for title?
        if (options.hide.manual) {
          contentObj.title.button = true;
        }

        //Prepare object with hide properties.
        //Hide the popover on blur or leave it open?
        hideObj = (options.hide.blur) ? { event: 'mouseleave', fixed: true } : { event: false, fixed: true };

        //To hide the popover automatically we need to start counting time when the element is visible.
        if (options.hide.time) {
          eventsObj.visible = function(event, api) {
            var popover = api.elements.tooltip,
                timeOutFunc = function() {
                  popover.hide();
                };

            setTimeout(timeOutFunc, options.hide.time);
          };
        }

        //An ajax call is done when a popover is shown. After the call is finished, then 'loading...' image is replaced with the content.
        if (options.url) {
          //Show event
          eventsObj.show = function(event, api) {
            var popoverApi = api;

            core.AJAX.call(options.url, {}, {
              onSuccess: function(data) {
                popoverApi.set('content.text', data);
              },
              onError: function() {
                popoverApi.set('content.text', 'An error occurred. Please try again later or check if the url is correct.');
              },
              onComplete: function() {
                //After ajax call, qtip does't take height into account so we need to force it again here.
                popoverApi.elements.tooltip.height(options.height);
              },
              dataType: 'html'
            });
          };
        }

        //Popover is not yet rendered so we need to use render event object.
        eventsObj.render = function(event, api) {
          //Change close button's class.
          if (options.hide.manual && api.elements.titlebar) {
            _self.changeCloseButtonClass(api.elements.titlebar);
          }

          //Add an icon.
          if (options.title && options.icon) {
            _self.setIcon(element, options.icon);
          }
        };

        element.qtip({
          content: contentObj,
          position: positionObj,
          show: {
            event: options.show.trigger,
            delay: options.show.delay
          },
          hide: hideObj,
          style: {
            classes: 'popover ' + options.type + ' ' + options.className,
            width: options.width,
            height: options.height,
            tip: {
              width: tipDimensions.width,
              height: tipDimensions.height,
              border: true
            }
          },
          events: eventsObj
        });
        

        actionDone = true;
      });

      //We wrap selector with jQuery here because there may be multiple elements.
      return (actionDone) ? core.dom.select(selector) : false;
    },
    
    /**
     * Change close button's class
     * @param {CoreObj} titlebar The titlebar with close button.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    changeCloseButtonClass: function(titlebar) {
      titlebar.find('.ui-icon-close').removeClass().addClass('icon-close-thin lightgrey');
    },

    /**
     * @see PopOver.getIcon
     */
    getIcon: function(initiator) {
      var _self = this,
          element = core.dom.select(initiator),
          iconClass, titlebar;

      if (_self.has(initiator)) {
        titlebar = element.data('qtip').elements.titlebar;

        if (titlebar) {
          iconClass = titlebar.find('i.popover').attr('class');
        }
      }

      //Remove default 'popover' class before returning icon class.
      return (iconClass) ? iconClass.replace('popover ', '') : false;
    },

    /**
     * @see PopOver.getTitle
     */
    getTitle: function(initiator) {
      var _self = this,
          element = core.dom.select(initiator);

      return (_self.has(initiator)) ? element.qtip('options', 'content.title.text') : false; //Returns title if it's set and false if not.
    },

    /**
     * @see PopOver.has
     */
    has: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      return core.dom.is(element) && core.object.is(element.data('qtip'), false);
    },

    /**
     * @see PopOver.hideCloseButton
     */
    hideCloseButton: function(initiator) {
      var _self = this,
          actionDone = false;

      core.dom.each(initiator, function(element) {
        if (_self.has(element)) {
          element.qtip('option', 'content.title.button', false);
          actionDone = true;
        }
      });

      return (actionDone) ? true : false;
    },

    /**
     * @see PopOver.showCloseButton
     */
    showCloseButton: function(initiator) {
      var _self = this,
          actionDone = false;

      core.dom.each(initiator, function(element) {
        if (_self.has(element)) {
          element.qtip('option', 'content.title.button', true);
          _self.changeCloseButtonClass(element.qtip().elements.titlebar);
          actionDone = true;
        }
      });

      return (actionDone) ? true : false;
    },

    /**
     * @see PopOver.setContent
     */
    setContent: function(initiator, newContent) {
      var _self = this,
          element = core.dom.select(initiator);

      if (_self.has(initiator)) {
        element.qtip('option', 'content.text', newContent);
        
        return element.data('qtip').elements.content;
      }

      return false;
    },

    /**
     * @see PopOver.setIcon
     */
    setIcon: function(initiator, icon) {
      var _self = this,
          actionDone = false,
          titlebar, oldIcon;

      core.dom.each(initiator, function(element) {
        if (_self.has(element)) {
          titlebar = element.data('qtip').elements.titlebar;
  
          if (titlebar) {
            oldIcon = titlebar.find('i.popover');
  
            if (oldIcon) {
              oldIcon.remove(); //if there is any old icon remove it
              actionDone = true;
            }
  
            //If icon is passed then set it. If it's false then this method is used to reset the old one.
            if (icon) {
              titlebar.prepend('<i class="popover ' + icon + '"></i>');
              actionDone = true;
            }
          }
        }
      });

      return (actionDone) ? true : false;
    },

    /**
     * @see PopOver.setTitle
     */
    setTitle: function(initiator, title) {
      var _self = this,
          actionDone = false;

      core.dom.each(initiator, function(element) {
        if (_self.has(element)) {
          element.qtip('option', 'content.title.text', title);
          actionDone = true;
        }
      });

      return (actionDone) ? true : false;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var PopOver = /** @lends PopOver */ {
    /**
     * Placement of the pop-over above the initiator.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    ORIENTATION_ABOVE: tooltip.ORIENTATION_ABOVE,
    /**
     * Placement of the pop-over below the initiator.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    ORIENTATION_BELOW: tooltip.ORIENTATION_BELOW,
    /**
     * Placement of the pop-over left of the initiator.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    ORIENTATION_LEFT: tooltip.ORIENTATION_LEFT,
    /**
     * Placement of the pop-over right of the initiator.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    ORIENTATION_RIGHT: tooltip.ORIENTATION_RIGHT,
    
    /**
     * Default type of the pop-over
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_DEFAULT: _popover.TYPE_DEFAULT,
    
    /**
     * Warning type of the pop-over
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_WARNING: _popover.TYPE_WARNING,
    
    /**
     * Danger type of the pop-over
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_DANGER: _popover.TYPE_DANGER,
    
    /**
     * Success type of the pop-over
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    TYPE_SUCCESS: _popover.TYPE_SUCCESS,

     /**
     * Create pop-over for this element.
     *
     * @param {CoreObj/Selector} selector="" A CoreObj or selector pointing to the pop-over's origin.
     * @param {Object} [options] Options for the pop-over.
     *   @option {String} type="TYPE_DEFAULT" The type of displayed PopOver. (default, warning, danger, success)
     *   @option {String} className="" Additional class
     *   @option {String} title="" The title displayed. If empty, none is displayed.
     *   @option {String} icon="" The icon shown. This corresponds to the icon classes (see Style Guide).
     *   @option {String} orientation="ORIENTATION_BELOW" The orientation of the pop-over. (above, left, below, right)
     *   @option {Boolean} hide.manual=true Can the user close the pop-over manually?
     *   @option {Boolean} hide.blur=true Close the pop-over when the focus is lost?
     *   @option {Number} hide.time=0 After how many milliseconds the pop-over should close itself automatically? 0 = never.
     *   @option {CoreObj/String} content="" The content shown. Can be a CoreObj pointing to/containing content or a string representing content. If it's empty, content is taken from originator's title attribute.
     *   @option {String} url="" A URL where content can be retrieved to display. (Will be fetched at every show.)
     *   @option {Number} width=200 The width of the pop-over.
     *   @option {Number} height=200 The height of the pop-over.
     *   @option {String} show.trigger="click" When should the pop-over be shown? Use the name of an event.
     *   @option {Number} show.delay=0 The delay in milliseconds before it is actually shown.
     * @returns {Boolean/CoreObj} Returns false on failure and the originator as a CoreObj on success.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(selector, options) {
      return _popover.create(selector, options);
    }.defaults('', {
      type: _popover.TYPE_DEFAULT,
      className: '',
      title: '',
      icon: '',
      orientation: tooltip.ORIENTATION_BELOW,
      hide: {
        manual: false,
        blur: true,
        time: 0
      },
      show: {
        trigger: 'click',
        delay: 0
      },
      width: 200,
      height: 200,
      content: '',
      url: ''
    }),

    /**
     * Removes the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the pop-over's origin.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(initiator) {
      return tooltip.destroy(initiator);
    }.defaults('', false),

    /**
     * Get the content of the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {CoreObj} The content of the pop-over.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getContent: function(initiator) {
      return tooltip.getContent(initiator);
    }.defaults(''),

    /**
     * Get the icon of the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {String/Boolean} The icon class of the pop-over of false it there is no icon.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getIcon: function(initiator) {
      return _popover.getIcon(initiator);
    }.defaults(''),

    /**
     * Get the title of the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {String/Boolean} The title of the pop-over or false if there is no title.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getTitle: function(initiator) {
      return _popover.getTitle(initiator);
    }.defaults(''),

    /**
     * Check if the element has a pop-over attached to it.
     *
     * @param {CoreObj/Selector} selector A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {Boolean} Whether the CoreObj or selector actually has a pop-over binded to it.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    has: function(selector) {
      return _popover.has(selector);
    }.defaults(''),

    /**
     * Hide the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the pop-over's origin.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    hide: function(initiator) {
      return tooltip.hide(initiator);
    }.defaults(''),

    /**
     * Hide the closing button.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    hideCloseButton: function(initiator) {
      return _popover.hideCloseButton(initiator);
    }.defaults(''),

    /**
     * Show the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    show: function(initiator) {
      return tooltip.show(initiator);
    }.defaults(''),

    /**
     * Show the closing button.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    showCloseButton: function(initiator) {
      return _popover.showCloseButton(initiator);
    }.defaults(''),

    /**
     * Set the content of the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @param {CoreObj/String} newContent Replace the current content with new content.
     * @returns {CoreObj} The new content as a CoreObj.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setContent: function(initiator, newContent) {
      return _popover.setContent(initiator, newContent);
    }.defaults(''),

    /**
     * Set a new icon for the pop-over.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @param {String} icon The class name of the new icon.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setIcon: function(initiator, icon) {
      return _popover.setIcon(initiator, icon);
    }.defaults('', ''),

    /**
     * Give the pop-over a new title.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to an originator that should have a pop-over.
     * @param {String} title A new title for the pop-over.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setTitle: function(initiator, title) {
      return _popover.setTitle(initiator, title);
    }.defaults('', '')
  };
window.p = PopOver;
  return PopOver;
});

core.module.alias.add('PopOver', 'library/uielements/popover');