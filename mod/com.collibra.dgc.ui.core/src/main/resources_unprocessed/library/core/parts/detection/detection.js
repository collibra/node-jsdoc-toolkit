/**
 * Core :: Detection
 *
 * Copyright Collibra 2012
 * @author Clovis Six <clovis@collibra.com>
 */

/**
 * @namespace
 */
var core = (function(core) {
  "use strict";

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var check = core.libs.get('modernizr'),
      dom = core.dom,
      html = dom.select('HTML');

  /** @private */
  var _core = core.object.extend(core._private, {
    detect: {

      /**
       * @constructor
       */
      _init: function() {
        var _self = this;

        _self.browser._init();
        _core.ajax.setBrowserMaxRequests(_core.detect.browser);
      },

      browser: {
        ie: false, ie6: false, ie7: false, ie8: false, ie9: false, ie10: false,
        gecko: false, khtml: false, presto: false, trident: false, webkit: false,
        opera: false, firefox: false, chrome: false, safari: false,
        engine: '', browser: '', version: 0, majorVersion: 0,

        _init: function() {
          var _self = this,
              ua = navigator.userAgent.toLowerCase(),
              i = 6;

          // Get the browser type and version from the userAgent string.
          ua =  /(webkit)[ \/]([\w.]+)/.exec(ua) ||
                /(opera)(?:.*version)?[ \/]([\w.]+)/.exec(ua) ||
                /(msie) ([\w.]+)/.exec(ua) ||
                !/compatible/.test(ua) && /(mozilla)(?:.*? rv:([\w.]+))?/.exec(ua) || [];

          _self.browser = ua[1];

          // Internet Explorer
          if (ua[1] === 'msie') {
            _self.ie = true;
            _self.browser = 'ie';
            ua[2] = document.documentMode || ua[2];
            ua[2] = parseFloat(ua[2]);

            html.addClass('ie' + ua[2]);

            // Set the correct version to true.
            _self['ie' + ua[2]] = true;

            // IE uses the Trident engine.
            _self.engine = 'trident';
            _self.trident = true;
          }

          // Firefox
          if (ua[1] === 'mozilla') {
            _self.engine = 'gecko';
            _self.gecko = true;

            if (ua[0].indexOf('camino') >= 0) {
              _self.browser = 'camino';
            } else {
              _self.browser = 'firefox';
            }
          }

          // Chrome & Safari
          if (ua[1] === 'webkit') {
            if (ua[0].indexOf('safari') >= 0) {
              _self.browser = 'safari';
            } else {
              _self.browser = 'chrome';
            }

            _self.engine = 'webkit';
            _self.webkit = true;
          }

          // Opera
          if (ua[1] === 'opera') {
            _self.engine = 'presto';
            _self.presto = true;
          }

          // Set the browser version.
          _self.version = parseFloat(ua[2]);
          _self.majorVersion = parseInt(_self.version, 10);

          // Add the browser type as a html class.
          html.addClass(_self.engine).addClass(_self.browser);
        }
      }
    }
  });

  _core.detect._init();

  /*************************************
   *               Public              *
   *************************************/

  return core.object.extend(core, /** @lends core */ {
    _private: _core,

    detect: /** @lends detect */ {
      /**
       * Detection of CSS3 / HTML 5 properties, browsers, versions and compatibility.
       * Browser detection.
       * @namespace core
       * @alias detect
       */

      browser: /** @lends browser */ {
        /**
         * @namespace core/detect
         * @alias browser
         */

        /**
         * Is the current browser <b>Internet Explorer</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE: _core.detect.browser.ie,

        /**
         * Is the current browser <b>Internet Explorer 6</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE6: _core.detect.browser.ie6,

        /**
         * Is the current browser <b>Internet Explorer 7</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE7: _core.detect.browser.ie7,

        /**
         * Is the current browser <b>Internet Explorer 8</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE8: _core.detect.browser.ie8,

        /**
         * Is the current browser <b>Internet Explorer 9</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE9: _core.detect.browser.ie9,

        /**
         * Is the current browser <b>Internet Explorer 10</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        IE10: _core.detect.browser.ie10,

        /**
         * Is the current browser engine <b>Gecko</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        GECKO: _core.detect.browser.gecko,

        /**
         * Is the current browser engine <b>Presto</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        PRESTO: _core.detect.browser.presto,

        /**
         * Is the current browser engine <b>Trident</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        TRIDENT: _core.detect.browser.trident,

        /**
         * Is the current browser engine <b>Webkit</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        WEBKIT: _core.detect.browser.webkit,

        /**
         * Is the current browser <b>Opera</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        OPERA: _core.detect.browser.opera,

        /**
         * Is the current browser <b>Firefox</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        FIREFOX: _core.detect.browser.firefox,

        /**
         * Is the current browser <b>Chrome</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        CHROME: _core.detect.browser.chrome,

        /**
         * Is the current browser <b>Safari</b>?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        SAFARI: _core.detect.browser.safari,

        /**
         * What is the current browser engine?
         * @returns {String}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        ENGINE: _core.detect.browser.engine,

        /**
         * What is the current browser?
         * @returns {String}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        BROWSER: _core.detect.browser.browser,

        /**
         * What is the current browser version?
         * @returns {Float}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        VERSION: _core.detect.browser.version,

        /**
         * What is the current browser major version?
         * @returns {Integer}
         * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
         */
        MAJORVERSION: _core.detect.browser.majorVersion
      },

      feature: /** @lends feature */ {
        /**
         * Is local storage supported?
         * @returns {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        LOCALSTORAGE: check.localstorage
      },

      input: /** @lends input */ {
        /**
         * Are placeholders supporten on input fields?
         * @type {Boolean}
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        PLACEHOLDER: check.input.placeholder
      }
    }
  });
}(core));
