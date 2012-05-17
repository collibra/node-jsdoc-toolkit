package com.collibra.dgc.core.security.authorization;

import java.util.Collection;

/**
 * Forms the logical grouping of category of {@link Right}s. A {@link RightCategory} can have sub {@link RightCategory}
 * s.
 * 
 * @author amarnath
 * 
 */
public interface RightCategory {
	/**
	 * Get parent {@link RightCategory}, null in case there is no parent.
	 * 
	 * @return The {@link RightCategory}.
	 */
	RightCategory getParentCategory();

	/**
	 * Get name.
	 * 
	 * @return The name
	 */
	String getName();

	/**
	 * Get identifier
	 * 
	 * @return The id.
	 */
	String getId();

	/**
	 * Check if the {@link RightCategory} is active or not. This is mainly for UI when we BSG developers want to add the
	 * future rights also into the system but UI should not show this category to end users if not active.
	 * 
	 * @return
	 */
	boolean isActive();

	/**
	 * Check if this is a global or resource category. By default a category is not global. This value can be inherited
	 * from the parent category.
	 * 
	 * @return True if this category is a global right and does not apply to resources but to the entire system. False
	 *         if this is a resource category.
	 */
	boolean isGlobal();

	/**
	 * Get {@link Right}s
	 * 
	 * @return The {@link Right}s.
	 */
	Collection<Right> getRights();

	/**
	 * Get sub {@link RightCategory}s.
	 * 
	 * @return The {@link RightCategory}s.
	 */
	Collection<RightCategory> getSubcategories();

	/**
	 * @return The permission strings in this {@link RightCategory}.
	 */
	Collection<String> getPermissionStrings();

	/**
	 * @return All permission strings from complete hierarchy for this {@link RightCategory}.
	 */
	Collection<String> getCompleteHierarchyPermissionStrings();

	// /**
	// * @return Get all {@link Right}s on which the {@link Right}s in this
	// * {@link RightCategory} depends directly or indirectly.
	// */
	// Collection<String> getAllDependencies();
}
