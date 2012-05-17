/**
 * Asset model
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/asset
 * @alias Asset
 */
/*global define,require,core */
define('model/asset', [ 'core' ], function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      Model = require('model/model');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _asset = {

  };

  /*************************************
   *               Public              *
   *************************************/

  /**
   * @module model/asset
   */
  var Asset = {
    /**
     *
     */
    get : function() {
      
    }
  };

  return Asset;
});

core.module.alias.add('Asset', 'model/asset');
core.module.use('Asset');