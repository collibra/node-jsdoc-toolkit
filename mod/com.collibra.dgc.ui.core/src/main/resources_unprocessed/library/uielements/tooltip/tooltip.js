/**
 * Tooltip.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module library/uielements/tooltip
 * @alias Tooltip
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/tooltip',
       ['core', 'library/uielements/shared/qtip'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core');

  require('library/uielements/shared/qtip');

  require('library/uielements/shared/qtip');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _tooltip = {
    ORIENTATION_ABOVE: 0,
    ORIENTATION_BELOW: 1,
    ORIENTATION_LEFT: 2,
    ORIENTATION_RIGHT: 3,
    ORIENTATION_ABOVE_LEFT: 4,
    ORIENTATION_ABOVE_RIGHT: 5,
    ORIENTATION_BELOW_LEFT: 6,
    ORIENTATION_BELOW_RIGHT: 7,
    ORIENTATION_LEFT_TOP: 8,
    ORIENTATION_LEFT_BOTTOM: 9,
    ORIENTATION_RIGHT_TOP: 10,
    ORIENTATION_RIGHT_BOTTOM: 11,
    
    _init: function() {
      var _self = this;

      core.dom.select(document).off("mouseenter", ".tooltip, .tooltip-warning, .tooltip-danger, .tooltip-success").on("mouseenter", ".tooltip, .tooltip-warning, .tooltip-danger, .tooltip-success", function(){
        var node = core.dom.select(this),
            tooltipPosition = node.attr('tooltip-position'),
            position;

        switch(tooltipPosition) {
          case 'above':
            position = _self.ORIENTATION_ABOVE;
            break;
          case 'above-left':
            position = _self.ORIENTATION_ABOVE_LEFT;
            break;
          case 'above-right':
            position = _self.ORIENTATION_ABOVE_RIGHT;
            break;
          case 'left':
            position = _self.ORIENTATION_LEFT;
            break;
          case 'left-top':
            position = _self.ORIENTATION_LEFT_TOP;
            break;
          case 'left-bottom':
            position = _self.ORIENTATION_LEFT_BOTTOM;
            break;
          case 'right':
            position = _self.ORIENTATION_RIGHT;
            break;
          case 'right-top':
            position = _self.ORIENTATION_RIGHT_TOP;
            break;
          case 'right-bottom':
            position = _self.ORIENTATION_RIGHT_BOTTOM;
            break;
          case 'below-left':
            position = _self.ORIENTATION_BELOW_LEFT;
            break;
          case 'below-right':
            position = _self.ORIENTATION_BELOW_RIGHT;
            break;
          default:
            position = _self.ORIENTATION_BELOW;
        }

        if (!_self.has(this)) {
          _self.create(this, {
            orientation: position,
            content: ''
          });

          node.mouseenter();
        }
      });
    },

    /**
     * Calculate Tip Offset
     */
    calculateTipOffset: function(orientation, api) {
      var _self = this,
          offset = 0;
      
      switch(orientation) {
        case _self.ORIENTATION_ABOVE_LEFT:
        case _self.ORIENTATION_ABOVE_RIGHT:
        case _self.ORIENTATION_BELOW_LEFT:
        case _self.ORIENTATION_BELOW_RIGHT:
          offset = api.elements.tooltip.width() / 6 - api.elements.tip.width() / 2;
          break;
          
        case _self.ORIENTATION_LEFT_TOP:
        case _self.ORIENTATION_LEFT_BOTTOM:
        case _self.ORIENTATION_RIGHT_TOP:
        case _self.ORIENTATION_RIGHT_BOTTOM:
          offset = api.elements.tooltip.height() / 6 - api.elements.tip.height() / 2;
          break;
          
        default:
          offset = 0;
          break;
      }
      return offset;
    },

    /**
     * @see Tooltip.create
     */
    create: function(selector, options) {
      var _self = this,
          positionObj,
          tooltipClass = '';

      core.dom.each(selector, function(element) {
        if (!options.content) {
            options.content = element.attr('title'); //If content is empty then get title attribute from the selector.
        }

        if (options.content) {
          switch(options.orientation) {
            case _self.ORIENTATION_ABOVE:
              positionObj = {
                my: "bottom center",
                at: "top center"
              };
              break;
            case _self.ORIENTATION_ABOVE_LEFT:
              positionObj = {
                my: "bottom right",
                at: "top center"
              };
              break;
            case _self.ORIENTATION_ABOVE_RIGHT:
              positionObj = {
                my: "bottom left",
                at: "top center"
              };
              break;
            case _self.ORIENTATION_LEFT:
              positionObj = {
                my: "right center",
                at: "left center"
              };
              break;
            case _self.ORIENTATION_LEFT_TOP:
              positionObj = {
                my: "right bottom",
                at: "left center"
              };
              break;
            case _self.ORIENTATION_LEFT_BOTTOM:
              positionObj = {
                my: "right top",
                at: "left center"
              };
              break;
            case _self.ORIENTATION_RIGHT:
              positionObj = {
                my: "left center",
                at: "right center"
              };
              break;
            case _self.ORIENTATION_RIGHT_TOP:
              positionObj = {
                my: "left bottom",
                at: "right center"
              };
              break;
            case _self.ORIENTATION_RIGHT_BOTTOM:
              positionObj = {
                my: "left top",
                at: "right center"
              };
              break;
            case _self.ORIENTATION_BELOW_LEFT:
              positionObj = {
                my: "top right",
                at: "bottom center"
              };
              break;
            case _self.ORIENTATION_BELOW_RIGHT:
              positionObj = {
                my: "top left",
                at: "bottom center"
              };
              break;
            default:
              positionObj = {
                my: "top center",
                at: "bottom center"
              };
          }
  
          //Choose style
          if (element.hasClass('tooltip-danger')) {
            tooltipClass = 'ui-tooltip-danger';
          } else if (element.hasClass('tooltip-warning')) {
            tooltipClass = 'ui-tooltip-warning';
          } else if (element.hasClass('tooltip-success')) {
            tooltipClass = 'ui-tooltip-success';
          } else {
            tooltipClass = 'ui-tooltip-default';
          }
  
          element.qtip({
            content: {
              text: options.content
            },
            position: positionObj,
            style: {
              classes: tooltipClass,
              tip: {
                width: 12,
                height: 10,
                border: false,
                mimic: 'center'
              }
            },
            events: {
              show: function(event, api) {
                // Set tip offset for advanced positioning
                api.options.style.tip.offset = _self.calculateTipOffset(options.orientation, api);
              }
            }
          });
        }
      });
    },

    /**
     * @see Tooltip.destroy
     */
    destroy: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      if (_self.has(selector)) {
        element.qtip('destroy');
        return true;
      }

      return false;
    },

    /**
     * @see Tooltip.getContent
     */
    getContent: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      if (_self.has(selector)) {
        return element.qtip('options', 'content.text');
      }

      return false;
    },

    /**
     * @see Tooltip.has
     */
    has: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      //Greg: I removed checking: element.data('qtip').options.style.classes === 'popover') ? false : true
      //This caused many problems. Popover functions which used tooltip functions(like hide, setContent) wasnt working
      //because this condition always returned false for popover.
      //In my opinion its a bad pattern when you need to extend parent when using children.
      return core.dom.is(element) && core.object.is(element.data('qtip'), false);

    },

    /**
     * @see Tooltip.hide
     */
    hide: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      if (_self.has(selector)) {
        element.qtip('hide');
        return true;
      }

      return false;
    },

    /**
     * @see Tooltip.setContent
     */
    setContent: function(selector, content) {
      var _self = this,
          element = core.dom.select(selector);

      if (_self.has(selector)) {
        if (core.string.is(content) && content === '') {
          content = element.attr('title'); //If string is empty then get title attribute from the selector.
        }

        element.qtip('option', 'content.text', content);

        return true;
      }

      return false;
    },

    /**
     * @see Tooltip.show
     */
    show: function(selector) {
      var _self = this,
          element = core.dom.select(selector);

      if (_self.has(selector)) {
        element.qtip('show');
        return true;
      }

      return false;
    }
  };

  core.dom.ready(function() {
    _tooltip._init();
  });

  /*************************************
   *               Public              *
   *************************************/

  var Tooltip = /** @lends Tooltip */ {
    /**
     * Tooltip is placed above selected element and it's centered horizontally (25%, 50%, 75%), within top edge.
     */
    ORIENTATION_ABOVE: _tooltip.ORIENTATION_ABOVE,
    ORIENTATION_ABOVE_LEFT: _tooltip.ORIENTATION_ABOVE_LEFT,
    ORIENTATION_ABOVE_RIGHT: _tooltip.ORIENTATION_ABOVE_RIGHT,
    /**
     * Tooltip is placed below selected element and it's centered horizontally (25%, 50%, 75%), within bottom edge. This is default setting.
     */
    ORIENTATION_BELOW: _tooltip.ORIENTATION_BELOW,
    ORIENTATION_BELOW_LEFT: _tooltip.ORIENTATION_BELOW_LEFT,
    ORIENTATION_BELOW_RIGHT: _tooltip.ORIENTATION_BELOW_RIGHT,
    /**
     * Tooltip is placed left to selected element and it's centered vertically (25%, 50%, 75%), within left edge.
     */
    ORIENTATION_LEFT: _tooltip.ORIENTATION_LEFT,
    ORIENTATION_LEFT_TOP: _tooltip.ORIENTATION_LEFT_TOP,
    ORIENTATION_LEFT_BOTTOM: _tooltip.ORIENTATION_LEFT_BOTTOM,
    /**
     * Tooltip is placed right to selected element and it's centered vertically (25%, 50%, 75%), within right edge.
     */
    ORIENTATION_RIGHT: _tooltip.ORIENTATION_RIGHT,
    ORIENTATION_RIGHT_TOP: _tooltip.ORIENTATION_RIGHT_TOP,
    ORIENTATION_RIGHT_BOTTOM: _tooltip.ORIENTATION_RIGHT_BOTTOM,

     /**
      * Create tooltip for this element.
      *
      * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
      * @param {Object} [options] Options for the tooltip.
      *   @option {String} orientation=ORIENTATION_BELOW The orientation of the pop-over. Can be {@link Tooltip.ORIENTATION_ABOVE}, {@link Tooltip.ORIENTATION_LEFT}, {@link Tooltip.ORIENTATION_BELOW} or {@link Tooltip.ORIENTATION_RIGHT}.
      *   @option {CoreObj/String} content="" The content shown. Can be a CoreObj pointing to/containing content or a string representing content. If nothing is specified, it looks for the content in title.
      * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
      * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
      */
    create: function(selector, options) {
      _tooltip.create(selector, options);
    }.defaults('', {
      orientation: _tooltip.ORIENTATION_BELOW,
      content: ''
    }),

    /**
     * Removes the tooltip.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
     * @returns {Boolean} Returns true on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(initiator) {
      return _tooltip.destroy(initiator);
    }.defaults(''),

    /**
     * Get content of the tooltip.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
     * @returns {Boolean/String} String with content on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getContent: function(initiator) {
      return _tooltip.getContent(initiator);
    }.defaults(''),

    /**
     * Check if the element has a tooltip attached to it.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
     * @returns {Boolean} Whether the CoreObj or selector actually has a tooltip binded to it.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    has: function(initiator) {
      return _tooltip.has(initiator);
    }.defaults(''),

    /**
     * Hide the tooltip.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    hide: function(initiator) {
      return _tooltip.hide(initiator);
    }.defaults(''),

    /**
     * Set the content of the tooltip.
     *
     * @param {CoreObj/Selector} initiator A CoreObj or selector pointing to the tooltip's initiator.
     * @param {CoreObj/String} content The content shown. Can be a CoreObj pointing to/containing content or a string representing content. If nothing is specified, it looks for the content in title.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setContent: function(initiator, content) {
      return _tooltip.setContent(initiator, content);
    }.defaults('', ''),

    /**
     * Show the tooltip.
     *
     * @param {CoreObj/Selector} tooltipOrigin A CoreObj or selector pointing to the tooltip's initiator.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    show: function(tooltipOrigin) {
      return _tooltip.show(tooltipOrigin);
    }.defaults('')
  };

  return Tooltip;
});

core.module.alias.add('Tooltip', 'library/uielements/tooltip');
core.module.use('Tooltip');
