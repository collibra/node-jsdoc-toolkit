/**
 * ModelImpl is hold all the logic shared by models.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/ModelImpl
 * @alias ModelImpl
 */
/*global define,require,core */
define('model/modelImpl', [ 'core', 'model/model'], function(require, exports, module) {
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
  var _modelImpl = {

    baseUrl: "/com.collibra.dgc.war/rest/1.0",
    fullAppendix: "/full",

    //List of models modules that can be loaded.
    //Added to prevent multiple 404 requests.
    //This will continuously grow as new model modules are implemented.
    allowedModels: [
      "community",
      "vocabulary",
      "term",
      "attribute"
    ],

    /**
     * Function that extends model object with all the public methods used by model objects.
     * This is shared logic, so i decided to move it from CommunityModel.
     */
    create: function(rid, model, onFinish, noRestCall) {
      var mobject = null;

      //Check if Model repository already has this model object.
      mobject = Model.get(rid);
      
      if(!mobject) {
        //Its completely new.
        mobject = {
          isModelObject: true, //for testing
          rid: rid || "[rid]",
          _private: {
            handlers: {
              propertyChanged: {}
            },
            type: model._private.type,
            ref: model._private,
            data: {},
            unstagedData: {}
          }
        };
        
        //Create model object specific REST URLs.
        mobject._private.urls = _modelImpl.resolveUrls(mobject, model._private.urls);
        
        //Extend model object with model specific methods.
        core.object.extend(mobject, model._private.methods);
        
        //Extend model object with CRUD methods.
        mobject.get = function(prop, callback, forget) {
          _modelImpl.getProperty(mobject, prop, callback, forget);
          return this;
        };

        mobject.set = function(prop, value, force, callback) {
          _modelImpl.setProperty(mobject, prop, value, force, callback);
          return this;
        };
        
        mobject.update = function(callback) {
          _modelImpl.update(mobject, callback);
          return this;
        };
        
        mobject.save = function(callback) {
          _modelImpl.save(mobject, callback);
          return this;
        };
        
        mobject.remove = function(callback) {
          _modelImpl.remove(mobject, callback);
        };
        
        mobject.isConsistent = function() {
          //If unstaged props exists, the model is in incosistent state.
          return _modelImpl.isConsistent(mobject);
        };
        
        //If object is provided in 'rid' variable it is used to create new community.
        if(core.object.is(rid)) {
          mobject.set("name", rid.name);
          mobject.set("uri", rid.uri);
          mobject.set("language", rid.language || "English");
          
          //Set rid to null, so it means that we have fresh, new object.
          mobject.rid = null;
          //..and save it.
          mobject.save(function(new_data){
            //Add it to Model repo when its created.
            mobject.rid = new_data.resourceId;
            Model.add(mobject);
            
            onFinish.apply(this, [new_data]);
          });
        } else {
          //Add it to the repo.
          Model.add(mobject);
        }
      }
      
      //Update it, if its not new object. Because if its new, this is unnecessary.
      if(!core.object.is(rid) && !noRestCall) {
        _modelImpl.update(mobject, onFinish);
      } else {
        _modelImpl.resolveReferenceData(mobject._private.ref.references, mobject._private.data);
      }

      //Extend model object with all the other public methods.
      mobject.onChange = function(property, callback) {
        _modelImpl.onChange(mobject, property, callback);
        return this;
      };
      

      mobject.fromJSON = function(json) {
        _modelImpl.fromJSON(mobject, json);
      };

      mobject.toJSON = function() {
        return _modelImpl.toJSON(mobject);
      };

      mobject.listHandlers = function() {
        return _modelImpl.listHandlers(mobject);
      };

      return mobject;
    },

    isConsistent: function(mobject) {
      return core.object.isEmpty(mobject._private.unstagedData);
    },

    fromJSON: function(mobject, data) {
      mobject._private.data = core.string.is(data) ? JSON.parse(data) : data;
    },

    toJSON: function(mobject) {
      return mobject._private.data.toSource();
    },

    /**
     * Method that invokes .create on every object returned in json *Refence data object.
     */
    createFromData: function(data, type, model) {
      var mos = [],
          objects = null,
          child = null;

      if(core.array.is(data)) {
        core.array.each(data, function(ind, value) {
          child = null;
          child = ModelImpl.create(value.resourceId, model, function(){}, true);
          child._private.data = value;
          mos.push(child);
        });

        return mos;
      } else {
        return ModelImpl.create(data.resourceId, model, function(){}, true);
      }
    },

    /**
     * Method that invokes .create on every object returned in json *Refence data object.
     */
    find: function(model, query, callback, options) {
      options = options || {};
      if(!callback) {
        core.debug.show("You need to specify callback function as second parameter.", 'warning');
      }
      
      if(core.string.is(query) && query.length > 1) {
        core.REST.get(_modelImpl.baseUrl + model._private.urls.find + (options.full ? "/full" : ""), {
          searchName: query,
          offset: options.offset,
          number: options.number
        }, {
          onSuccess: function(data) {
            //Create model objects from the returned data.
            data = _modelImpl.resolveReferenceData(model._private.references, data, true);
            callback.apply(this, [data || []]);
          }
        });
      } else {
        core.debug.show("Query string must be a string literal and at least of 2 chars long", 'warning');
      }
    },

    /**
     * @see ModelImpl
     */
    getProperty: function(mobject, property, callback, attach) {
      var temp_data = null;
      
      if(!callback) {
        core.debug.show("You need to specify callback function to get the property.", 'warning');
      }
      
      attach = attach !== undefined ? true : false;
      
      //Check original props and if it was set(is not null)
      if(mobject._private.data.hasOwnProperty(property) && mobject._private.data[property]) {
        
      //Return property value from original data.
        callback.apply(this, [mobject._private.data[property]]);
        
      } else {
      //Otherwise, ask REST and update our object.
        _modelImpl.update(mobject, function(data) {
          //Now check if required property is in the object(after updating it).
          if(data[property]) {

            //Return property value data after update.
            callback.apply(this, [data[property]]);

          } else if(!data[property] && mobject._private.urls.hasOwnProperty(property)) {

            //There is no such property.
            //Last check on the urls to look for specific url for properties.
            core.object.each(mobject._private.urls, function(key, value) {
              if(property === key) {

                //Ok, we found an url bound to needed property.
                //Make a REST call on that url and return returned data as property value.
                _modelImpl.update(mobject, function(updata) {
                  temp_data = _modelImpl.resolveReferenceData(mobject._private.ref.references, updata);

                  //Return property value data after update on custom property URL.
                  callback.apply(this, [temp_data]);

                }, mobject._private.urls[property]);
              }
            });
          } else {
            core.debug.show("Property '" + property + "' does not exists in the '" + mobject._private.type + "' type model object.", 'warning');
          }
          
        });
      }
      
      //Attach a handler to the change event and trigger it every time property has changed.
      //Only if 'attach' flag is set.
      if(attach) {
        _modelImpl.onChange(mobject, property, callback);
      }
    },

    /**
     * @see ModelImpl
     */
    setProperty: function(mobject, property, value, forceRest, callback) {

        //Check if the property can be updated.
        if(mobject._private.ref.updatableProperties.indexOf(property) === -1) {
          core.debug.show("Property " + property + " cannot be updated.", 'warning');
        }

        //If property is an object, use the object from property argument only.
        if(core.object.is(property)) {
          core.object.extend(mobject._private.unstagedData, property);
        } else {
        //If its not, use property and value arguments.
          mobject._private.unstagedData[property] = value;
        }

        //Make the forced save call.
        if(forceRest) {
          ModelImpl.save(mobject, callback);
        }
    },

    /**
     * @see ModelImpl
     */
    save: function(mobject, callback) {
      var callbacksDone = 0,
          changesSize = 0,
          postData = {},
          createUrl = "";
      
      callback = callback || function(){};
      //If object is consistent, there's no need to save.
      if(!mobject.isConsistent()) {
        
        //ResourceId is specified, make the UPDATE.
        if(mobject.rid) {
          //For now, each of the property needs to be saved separately.
          changesSize = core.object.size(mobject._private.unstagedData);
          core.object.each(mobject._private.unstagedData, function(key, value) {
            
            //Set the property to be changed.
            postData = {};
            postData.rid = mobject.rid;
            postData[key] = value;
            
            //Make the UPDATE call.
            core.REST.update(mobject._private.urls.crud, postData, {
              onSuccess: function(data) {
                
                callbacksDone += 1;
                if(callbacksDone === changesSize) {
                  //We have the CommunityReference here.
                  callback.apply(this, [data]);
                  
                  //Save the data.
                  _modelImpl.onUpdate(mobject, data);
                }
                
                //Remove saved data from the waiting changes.
                delete mobject._private.unstagedData[key];
              }
            });
          });
        } else {
          //ResourceId is null, make the CREATE(INSERT).
          postData = {};
          core.object.each(mobject._private.unstagedData, function(key, value) {
            //Set the property to be created.
            postData[key] = value;
          });
          
          //Get the create url from crud url.
          createUrl = mobject._private.urls.crud.split("/[rid]")[0];
          
          //Make the CREATE call.
          core.REST.update(createUrl, postData, {
            onSuccess: function(data) {

              //We have the created CommunityReference here.
              callback.apply(this, [data]);

              //Save the data.
              _modelImpl.onUpdate(mobject, data);

              //Update model object urls with the new resourceId.
              mobject._private.urls = _modelImpl.resolveUrls(mobject, mobject._private.urls);

              //Remove saved data from the waiting changes.
              mobject._private.unstagedData = {};
            }
          });
        }
      }
    },

    /**
     * @see ModelImpl
     */
    update: function(mobject, callback, customURL) {
      var url = null;

      callback = callback || function(){};
      url = customURL || mobject._private.urls.crud || core.debug.show("There is no CRUD url specified in " + mobject._private.type + " Model", "warning");

      core.REST.get(url, {}, {
        onSuccess: function(data) {
          //We have the entity data here. Fill the object with them.
          _modelImpl.onUpdate(mobject, data);

          callback.apply(this, [data]);
        }
      });
    },

    /**
     * @see ModelImpl
     */
    remove: function(mobject, callback) {
      callback = callback || function(){};

      core.REST.remove(mobject._private.urls.crud, {}, {
        onSuccess: function() {
          //Entity is deleted. remove it from Model repo too.
          Model.remove(mobject.rid);
          callback.apply(this, arguments);
        }
      });
    },

    /**
     * Callback to be executed every time update() method is successful.
     */
    onUpdate: function(mobject, data) {
      var old_value = null;

      core.object.each(data, function(key, value) {

        //Compare old data and new data and fire corresponding propertyChanged event for those properties that have changed.
        //Deal with everything that is not an object.
        if(mobject._private.data[key] !== value && !core.object.is(value)) {
          //Old property is different than new one. Fire propertChanged event.
          old_value = mobject._private.data[key] ? mobject._private.data[key] : false;
          core.mediator.publish(mobject._private.handlers.propertyChanged[key], [value, old_value]);
        }
      });

      //Resolve reference data(reference objects inside data).
      data = _modelImpl.resolveReferenceData(mobject._private.ref.references, data);

      //Now update internal model object data..
      mobject._private.data = data;
      //..and its resource id property rid.
      mobject.rid = data.resourceId;
    },

    resolveReferenceData: function(model_ref, data, noKey) {
      var ref_key = "",
          modelName = "",
          alias = "",
          ref_data = null,
          tr = null,
          lowered_type = "",
          references = {};

      core.object.each(data, function(key, value) {

        //Resolve all sub-objects.
        if(core.object.is(value) || core.array.is(value)) {

          //Store reference key.
          ref_key = key;
          //If parent object is added(i.e vocabularyReferences) we need to descend to the type array(vocabularyReference)
          //and resolve all the objects inside(without update on each, because basic data is there).
          if(!!(key.match(/References/))) {
            //Go deeper one level.
            for(var descendant in value) {
                if(value.hasOwnProperty(descendant)) {
                    value = value[descendant];
                    ref_key = descendant;
                    break;
                }
            }
          }

          //Now
          //if key=sthReferences
          //then ref_key = sthReferences children key, key = sthReferences
          //else ref_key = key = sthReferences

          //Now load needed model module and create model objects for it.
          //Based from the references.[name].type var.
          modelName = model_ref[key] && model_ref[key].type;
          if(modelName && _modelImpl.allowedModels.indexOf(modelName) !== -1) {
            core.module.use("model/" + modelName, function(module) {

              //Create a model object from the reference data.
              ref_data = ModelImpl.createFromData(value, key, module);
              //Delete the original reference property.
              delete data[key];

              alias = model_ref[key].alias;

              //Finally use either an alias or original key name for created data.
              if(core.array.is(alias)) {
                //If there are multiple aliases defined, create a property for each alias(by reference).
                core.array.each(alias, function(index, key){
                  data[key] = ref_data;
                });
              } else {
                //Create single alias otherwise.
                if(noKey) {
                  data = ref_data;
                } else {
                  data[alias ? alias : key] = ref_data;
                }
              }
            });
          }
        }
      });

      return data;
    },

    /**
     * @see ModelImpl.onChange
     */
    onChange: function(mobject, property, handler) {

      //Register all model object handlers.
      _modelImpl.registerHandlers(mobject, property);

      //Subscribe to the property change event.
      core.mediator.subscribe(mobject._private.handlers.propertyChanged[property], handler);
    },

    /**
     * Create model object specific handlers from its info.
     * List of those handlers can be obtained using listHandlers() method.
     */
    registerHandlers: function(mobject, prop) {
      if(!mobject._private.handlers.propertyChanged.hasOwnProperty(prop)) {
        mobject._private.handlers.propertyChanged[prop] = "model::" + mobject._private.type.toLowerCase() + "::" + mobject.rid + "::" + prop + "::" + "changed";
      }
    },

    /**
     * Print the list of all registered handlers for the model object.
     */
    listHandlers: function(mobject) {
      core.debug.show("Property change handlers:");
      core.object.each(mobject._private.handlers.propertyChanged, function(key, value) {
        core.debug.show(key + " -> " + value);
      });
    },

    /**
     * @see ModelImpl
     */
    resolveUrls: function(mobject, urls) {
      var filledUrls = {},
          rid = core.object.is(mobject.rid)? "[rid]": mobject.rid;

      //Attach base url and replace [rid] with given resourceId.
      core.object.each(urls, function(key, url) {
        filledUrls[key] = _modelImpl.baseUrl + url.replace("[rid]", rid);
      });

      return filledUrls;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var ModelImpl = {
   /**
    * Create a model object for Model module and return it.
    *
    * @param {String} rid ResourceId of the Community you want to instantiate.
    * @param {Object} [model=(CommunityModel,VocabularyModel)] Model module object that you want to create model objects out of.
    * @param {Function} [onFinish=function(){}] Callback function to be executed when creating finishes.
    * @param {Boolean} [noRestCall=false] Flag to hinder automatic update of the model object upon creation.
    * @returns {Object} Return created model object.
    * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
    */
    create: function(rid, model, onFinish, noRestCall) {
      return _modelImpl.create(rid, model, onFinish, noRestCall);
    },

    /**
     * Create a model object from prefetched data.
     *
     * @param {Object} mobject Created parent model object.
     * @param {String} type Key name of the object in the returned json data.
     * @param {Object} model Model module object that you want to create mode object out of.
     * @returns {Object} Return created model object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    createFromData: function(data, type, model, fetchFull) {
      return _modelImpl.createFromData(data, type, model, fetchFull);
    },
    
    /**
     * Delete/remove object from database. This also removes model object from Model Repository.
     *
     * @param {Object} mobject Created model object to be removed.
     * @param {Function} [callback] Callback function to be executed when deleting finishes.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    remove: function(mobject, callback) {
      _modelImpl.remove(mobject, callback);
    },
    
    /**
     * Check if a model is in consistent state.
     * That means if there is no any unsaved data waiting to be saved.
     *
     * @param {Object} mobject Created model object.
     * @returns {Object} Return true if model object is consistent, false otherwise.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    isConsistent: function(mobject) {
      return _modelImpl.isConsistent(mobject);
    },
    
    /**
     * Get value of the property from the model object.
     * This is done asynchronously, so callback is mandatory.
     *
     * @param {Object} mobject Created model object.
     * @param {String} property Name of the property to be retrieved.
     * @param {Function} callback Callback function to be executed with arguments data containg value of the property.
     * @param {Boolean forget=true Should callback be triggered only once(forget=true) or every time property changes(forget=false).
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getProperty : function(mobject, property, callback, forget) {
      _modelImpl.getProperty(mobject, property, callback, forget);
    },
    
    /**
     * Find the data in the model object using REST /find resource.
     *
     * @param {Object} model Reference to the Model type module.
     * @param {String} query The expression to search in entity names.
     * @param {Function} callback Callback function to be triggered when searching finish. Provide an object with the data found.
     * @param {Object} [options] Options for the find method.
     *  @option {Boolean} [full=false] Whether to fetch full information about object or just only references.
     *  @option {Boolean} [offset=0] The position of the first result to return.
     *  @option {Boolean} [number=0] The maximum number of communities to retrieve; if 0 then all results will be retrieved.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    find : function(model, query, callback, options) {
      _modelImpl.find(model, query, callback, options);
    }.defaults({}, "", function(){}, {
      full: false,
      offset: 0,
      number: 0
    }),
    
    /**
     * Fill model object with given json data(this can be either stringified JSON or object).
     *
     * @param {Object} mobject Created model object.
     * @param {String/Object} data Data in the JSON or stringified format.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    fromJSON : function(mobject, data) {
      _modelImpl.fromJSON(mobject, data);
    },
    
    /**
     * Register the handler to the change event of the property.
     * This is also done on ModelImpl.get method.
     *
     * @param {Object} mobject Created model object.
     * @param {String} property The name of the property that you want to attach onChange handler on.
     * @param {Function} handler Function that is triggered when changing propery occur. Passes new and old values of the property that is changed.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    onChange : function(mobject, property, handler) {
      _modelImpl.onChange(mobject, property, handler);
    },
    
    /**
     * Set the property value of the model object.
     *
     * @param {Object} mobject Created model object.
     * @param {String/Object} property Name of the property to be set. This can be either string or an object.
     * If string 'property' caontains the name of the property while 'value' value of it.
     * If object each property is set using key:value notation.
     * @param {String} value New value for the property.
     * @param {Boolean} [forceRest=false] Flag to force invoking save() method imediately after set is made.
     * @param {Function} [callback=function(){}] Callback function to be executed when saving finishes.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @example
     *  mo.set("prop", "val");
     *  mo.set("prop2", "val2");
     *  or
     *  mo.set({prop: "val", prop2: "val2"});
     */
    setProperty : function(mobject, property, value, forceRest, callback) {
      _modelImpl.setProperty(mobject, property, value, forceRest, callback);
    },
    
    /**
     * Make a REST Update call to save all the previously set data.
     * For now this creates queue of calls for separated saving of each property.
     *
     * @param {Object} mobject Created model object.
     * @param {Function} [callback=function(){}] Callback function to be executed when saving finishes(when all calls are done if more than one).
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    save : function(mobject, callback) {
      _modelImpl.save(mobject, callback);
    },
    
    /**
     * Make a REST Get call to fetch all the model object data and fill it with this data.
     * This is done upon: manual update, save and create of the model object.
     *
     * @param {Object} mobject Created model object.
     * @param {Function} [callback=function(){}] Callback function to be executed when saving finishes.
     * @param {String} [customURL] Custom URL to replace Model module defined CRUD url.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    update : function(mobject, callback, customURL) {
      _modelImpl.update(mobject, callback, customURL);
    },
    
    /**
     * Return model object data in stringified JSON format.
     *
     * @param {Object} mobject Created model object.
     * @returns {String} Return stringified JSON data.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    toJSON : function(mobject) {
      return _modelImpl.toJSON(mobject);
    }
  };

  return ModelImpl;
});

core.module.alias.add('ModelImpl', 'model/modelImpl');
core.module.use('ModelImpl');