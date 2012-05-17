/**
 * Converter manipulation methods.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:michal@collibra.com">Michal Hans</a>
 * @module modules/converter/converter
 * @alias Converter
 * @namespace modules
 */
/*global define,require,core */
define('modules/converter', ['core'], function(require, exports, module) {
  "use strict";

  /** @private */
  var core = require('core');

  /** @private */
  var _converter = {

    /**
     * Return pixel value as an integer
     */
    csspx2px: function(value) {
      return (value === 'auto') ? 0 : parseInt(value.slice(0,-2), 10);
    }

  };

  var Converter = /** @lends Converter */ {
      
    /**
     * Return pixel value as an integer
     * @param {String} value Css value in pixels (ex. 25px)
     * @returns {Integer} Integer value
     */
    csspx2px: function(value) {
      return _converter.csspx2px(value);
    }
  };

  return Converter;
});

core.module.alias.add('Converter', 'modules/converter');
core.module.use('Converter');