/**
 * Model is responsible for managing the repository of existing model objects.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/model
 * @alias Model
 */
/*global define,require,core */
define('model/model', [ 'core' ], function(require, exports, module) {
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
  var _model = {

    repo: {},
    
    /**
     * @see Model.add
     */
    add : function(mobject) {
      var rid = mobject.rid;

      if(!_model.check(rid)) {
        _model.repo[rid] = mobject;

        return true;
      }

      return false;
    },
    
    /**
     * @see Model.cache
     */
    cache : function(rid, mobject) {
      //TODO! Implement when requirements are created for this.
    },
    
    /**
     * Check if model object with given resourceId exists in the Model Repository.
     *
     * @param {String} rid ResourceId of the Model entity that you want to check.
     * @returns {Object/Boolean} Return true if found, false otherwise.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    check : function(rid) {
      if(_model.repo.hasOwnProperty(rid)) {
        return true;
      }
      
      return false;
    },
    
    /**
     * @see Model.get
     */
    get : function(rid) {
      if(_model.check(rid)) {
        return _model.repo[rid];
      }

      return false;
    },
    
    /**
     * @see Model.remove
     */
    remove : function(rid) {
      if(_model.check(rid)) {
        delete _model.repo[rid];

        return true;
      }

      return false;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var Model = {

    _private: _model,

    /**
     * Add model object to the repository.
     *
     * @param {Object} mobject A model object to add to the model.
     * @returns {Boolean} Return true if successful add, false otherwise or in case the model object already exists in the repository.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    add : function(mobject) {
      return _model.add(mobject);
    },
    
    /**
     * Cache x number of objects for the next page load.
     *
     * @param {Number} [cnt=0] Number of model objects to cache locally. Cache as many as possible when 0.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    cache : function(cnt) {
      // !TODO: Implement when specs are done.
    }.defaults(0),
    
    /**
     * Retrieve a model object from the model.
     *
     * @param {String} rid ResourceId of the model object that you want to retrieve from the model.
     * @returns {Object/Boolean} Return previously created model object if found, false otherwise.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    get : function(rid) {
      return _model.get(rid);
    },
    
    /**
     * Remove a model object from repository.
     *
     * @param {String} rid ResourceId of the model object that you want to remove from the model.
     * @returns {Object/Boolean} Return object if successful, false otherwise.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    remove : function(rid) {
      return _model.remove(rid);
    }
  };

  return Model;
});

core.module.alias.add('Model', 'model/model');
core.module.use('Model');