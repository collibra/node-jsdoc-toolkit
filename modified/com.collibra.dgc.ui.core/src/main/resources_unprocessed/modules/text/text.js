/**
 * Text manipulation methods.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @module modules/text/text
 * @alias Text
 * @namespace modules
 */
/*global define,require,core */
define('modules/text',
       ['core'],
       function(require, exports, module) {
  "use strict";

  /** @private */
  var core = require('core'),
      $ = core.libs.get('jquery');

  /** @private */
  var _text = {
    // Dummy element to use to calculate width and height of text.
    spanText: core.dom.select('#spanCalculateTextHeight'),
    // The string to create this dummy element.
    spanTextStr: "<span id='spanCalculateTextHeight' style='filter: alpha(0);'></span>",

    /**
     * @constructor
     */
    _init: function() {
      if (this.spanText.size() < 1) {
        this.spanText = core.dom.create(this.spanTextStr, 'BODY');
      }
    },

    /**
     * @see text.autoSize
     */
    autoSize: function(el, minSize, maxSize, truncate) {
      var _self = this;

      core.dom.each(el, function(item) {
        var width = item.innerWidth(),
            tWidth = _self.width(item),
            fontSize = parseInt(item.css('font-size'), 10);

        while (width < tWidth || (maxSize && fontSize > maxSize)) {
          if (minSize && fontSize < parseInt(minSize, 10)) {
            break;
          }

          fontSize = fontSize - 1;
          item.css('font-size', fontSize + 'px');

          tWidth = _self.width(item);
        }

        if (truncate) {
          _self.truncate(item);
        }
      });

      return core.dom.select(el);
    },

    /**
     * @see text.height
     */
    height: function(el, width) {
      var _self = this,
          valu = el.val();

      if (!valu) {
        valu = el.text();
      }

      _self.spanText
        .text(valu)
        .css({
          "fontSize": el.css('fontSize'),
          "fontWeight": el.css('fontWeight'),
          "fontFamily": el.css('fontFamily'),
          "lineHeight": el.css('lineHeight'),
          "letterSpacing": el.css('letterSpacing'),
          "top": 0,
          "left": -1 * width + 'px',
          "position": 'absolute',
          "display": "inline-block",
          "width": width,
          "opacity": 0,
          "word-wrap": "break-word"
        });

      return _self.spanText.innerHeight();
    },

    /**
     * @see text.maxSizes
     */
    maxSizes: function(els) {
      var interval = {
        min: 99999,
        max: 0
      };

      core.dom.each(els, function(item) {
        var fontSize = parseInt(item.css('font-size'), 10);
        interval.max = Math.max(interval.max, fontSize);
        interval.min = Math.min(interval.min, fontSize);
      });

      return interval;
    },

    /**
     * @see text.truncate
     */
    truncate: function(el) {
      var _self = this;

      core.dom.each(el, function(item) {
        var width = item.outerWidth(),
            textHeight = parseInt(_self.height(item, width), 10),
            text = item.text(),
            truncated = false;

        // As long as the height of the text is higher than that
        // of the container, we'll keep removing a character.
        while (textHeight > item.outerHeight()) {
          text = text.slice(0,-1);
          item.text(text);
          textHeight = parseInt(_self.height(item, width), 10);
          truncated = true;
        }

        // When we actually truncated the text, we'll remove the last
        // 3 characters and replace it with '...'.
        if (!truncated) {
          return false;
        }
        text = text.slice(0, -3);

        // Make sure there is no dot or space right in front of '...'.
        var lastChar = text[text.length - 1];
        if (lastChar === ' ' || lastChar === '.') {
          text = text.slice(0, -1);
        }
        item.text(text + '...');
      });

      return core.dom.select(el);
    },

    /**
     * @see text.width
     */
    width: function(el) {
      var _self = this,
          valu = el.val(),
          extra = 0;

      if (!valu) {
        valu = el.text();
      }

      _self.spanText
        .text(valu)
        .css({
          "fontSize": el.css('fontSize'),
          "fontWeight": el.css('fontWeight'),
          "fontFamily": el.css('fontFamily'),
          "lineHeight": el.css('lineHeight'),
          "letterSpacing": el.css('letterSpacing'),
          "position": "absolute",
          "top": 0,
          "opacity": 0,
          "left": -2000
        });

      if (core.detect.browser.IE7) {
        extra = _self.spanText.outerWidth() * 0.20;
      }

      return _self.spanText.outerWidth() + extra + parseInt(el.css('paddingLeft'), 10) + 'px';
    }
  };

  _text._init();

  var Text = /** @lends Text */ {
    /**
     * Adjust the font-size of the text so it fits the element.
     * @param {CoreObj/Selector} el The element(s) the text should fit in.
     * @param {Number} minSize The minimum size of the font.
     * @param {Number} maxSize The maximum size of the font.
     * @param {Boolean} truncate Truncate text after resizing to make sure it fits?
     * @returns {CoreObj} The element(s) of which the font-size is adjusted.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    autoSize: function(el, minSize, maxSize, truncate) {
      return _text.autoSize(el, minSize, maxSize, truncate);
    }.defaults('', 10, 18, true),

    /**
     * Return the height of the element containing the text. This is the
     * minimum height necessary for the full text to be visible.
     * @param {CoreObj/Selector} el The element of which to calculate the height.
     * @param {Number} [width] The width of the element to calculate the text of. The element's width is taken when this is 0.
     * @returns {Number} The minimum height necessary for the text to fit this element.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    height: function(el, width) {
      return _text.height(el, width);
    }.defaults('', 0),

    /**
     * Returns an object with the interval of maximum sizes of the elements fonts.
     * @param {CoreObj/Selector} els The element(s) of which to calculate the interval.
     * @returns {Object} Object containing min and max: { min: ..., max: ... }.
     */
    maxSizes: function(els) {
      return _text.maxSizes(els);
    }.defaults(''),

    /**
     * Function that truncates the text inside the element according to the
     * width and height of that container. In other words, makes it fit by chopping
     * off characters and adding '...'.
     * @param {CoreObj/Selector} el The element(s) of which the text should be truncated.
     * @returns {CoreObj} The element(s) of which the text is truncated.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    truncate: function(el) {
      return _text.truncate(el);
    }.defaults(''),

    /**
     * Returns the width of the element containing the text. This is the
     * minimum width necessary for the full text to be visible.
     * @param {CoreObj/Selector} el The element of which to calculate
     */
    width: function(el) {
      return _text.width(el);
    }.defaults('')
  };

  return Text;
});

core.module.alias.add('Text', 'modules/text');
core.module.use('Text');