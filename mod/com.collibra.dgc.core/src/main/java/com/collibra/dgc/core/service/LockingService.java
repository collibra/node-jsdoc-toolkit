package com.collibra.dgc.core.service;

import com.collibra.dgc.core.model.Resource;

/**
 * @author fvdmaele
 * 
 *         Service that facilitates locking of resources
 * 
 */
public interface LockingService {

	/**
	 * Lock every version of this resource based on its resourceId. The lock will be released when the database
	 * transaction is committed
	 * 
	 * @param clazz The class of the resource to lock
	 * @param resourceId The resourceId of the resource to lock
	 * @return The locked resource.
	 * @throws Exception
	 */
	public Resource lock(Class<? extends Resource> clazz, String resourceId) throws Exception;

	/**
	 * Lock every version of this resource based on its class and resourceId. The lock will be released when the
	 * database transaction is committed
	 * 
	 * @param clazz The class of the resource to lock
	 * @param resourceId The resourceId of the resource to lock
	 * @throws Exception
	 */
	public void lockResource(Class<? extends Resource> clazz, String resourceId) throws Exception;
}
