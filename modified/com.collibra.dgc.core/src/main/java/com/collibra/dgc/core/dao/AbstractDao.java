package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.Resource;

/**
 * This DAO interface groups the common behaviour for each DAO. The business object interface is passed as a generic.
 * @author dtrog
 * 
 * @param <T>
 */
public interface AbstractDao<T> {

	/**
	 * Lookup the business object by its id.
	 * 
	 * @param id Unique identifier for the business object.
	 * @return the business object.
	 */
	T findById(String id);

	/**
	 * Returns all business objects.
	 * @return The list of business objects
	 */
	List<T> findAll();

	/**
	 * Persists this business object.
	 * @param object The business object to persist.
	 */
	T save(T object);

	/**
	 * Deletes the business object.
	 * 
	 * @param object The business object to delete.
	 */
	void delete(T object);

	void flush();

	void evict(T object);

	void refresh(Object object);

	/**
	 * This method will lock a resource based on its resource id. That means that the resource will be locked for
	 * writing or updating
	 * 
	 * @param resource The resource to lock
	 */
	public void lockResource(Class clazz, String resourceId) throws Exception;

	public Resource findById(Class clazz, String resourceId);
}
