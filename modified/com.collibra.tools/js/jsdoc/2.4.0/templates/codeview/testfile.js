/**
 * This is the JavaScript TEST module
 *
 * Copyright Collibra NV/SA
 * @author <a href="#">Grzegorz Kaczan</a>
 * @module library/uielements/T/T
 * @alias TEST
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/T/T',
       ['core'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      $ = core.libs.get('jquery');

//
  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _T = {
    /**
     * @see T.add
     */
    add: function(selector, options) {
      var _self = this;

      if(!selector.jquery) {
        selector = $(selector);
      }

      if(!options) {
        options = _self.defaultOptions;
      }

      //create T dialog
      options.T = true;
      selector.dialog(options);

    },

    /**
     * @see T.open
     */
    open: function() {
      var _self = this;

    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var T = /** @lends TEST */{
    /**
     * Create a T window.
     * 
     * @param  {Object} options.{A}.ads Options for the T window.
     *   @option {Boolean} test.(Type, Term, Sth).jupi Pla pla.
     *   @option {Boolean} upa.(A,B).jupi.(C).sdfsdf Ble Ble.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    create: function(initiator, options) {
      // !TODO: Implement.
    }.defaults('', {
      hide: {
        enabled: true,
        onClose: function() { return true; }
      },
      title: '',
      subtitle: '',
      description: '',
      content: {
        element: '',
        url: ''
      },
      dimensions: {
        width: 400,
        height: 300
      }
    })
  };

  return T;
});

core.module.alias.add('TEST', 'library/uielements/T/T');