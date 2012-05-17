package com.collibra.dgc.core.security.authorization;

import java.util.Collection;

import com.collibra.dgc.core.model.Resource;

/**
 * Provides the interface for 'Right' object defined in glossary.
 * 
 * @author amarnath
 * 
 */
public interface Right {
	/**
	 * Get parent {@link RightCategory}.
	 * 
	 * @return The {@link RightCategory}.
	 */
	RightCategory getParentCategory();

	/**
	 * Get {@link Right} name.
	 * 
	 * @return The name.
	 */
	String getName();

	/**
	 * Get {@link Right} identifier.
	 * 
	 * @return The id.
	 */
	String getId();

	/**
	 * Check if this is a global or resource right. By default a right is not global. This value can be inherited from
	 * the owning category.
	 * 
	 * @return True if this right is a global right and does not apply to resources but to the entire system. False if
	 *         this is a resource right.
	 */
	boolean isGlobal();

	/**
	 * @return True if the {@link Right} has impact on the lock status of the {@link Resource}, otherwise false.
	 */
	boolean hasImpactOnLock();

	/**
	 * @return The {@link Right}s on which this {@link Right} depends directly.
	 */
	Collection<String> getDependencies();

	// /**
	// * @return Get all {@link Right}s on which this {@link Right} depends
	// * directly or indirectly.
	// */
	// Collection<String> getAllDependencies();

	/**
	 * Check if the {@link Right} is active or not. This is mainly for UI when we BSG developers want to add the future
	 * rights also into the system but UI should not show this category to end users if not active.
	 * 
	 * @return
	 */
	boolean isActive();
}
