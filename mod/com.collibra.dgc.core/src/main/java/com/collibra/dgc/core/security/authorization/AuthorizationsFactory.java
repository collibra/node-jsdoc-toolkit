package com.collibra.dgc.core.security.authorization;

import java.util.Collection;

/**
 * Factory to load and access the predefined {@link Right}s in BSG.
 * 
 * @author amarnath
 * 
 */
public interface AuthorizationsFactory {
	/**
	 * Get all {@link RightCategory}s.
	 * 
	 * @return The {@link RightCategory}s.
	 */
	Collection<RightCategory> getRightCategories();

	/**
	 * Get {@link RightCategory} for specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The {@link RightCategory}.
	 */
	RightCategory getRightCategory(String id);

	/**
	 * Get {@link RightCategory}s with specified name.
	 * 
	 * @param name
	 *            The name
	 * @return The {@link RightCategory}s.
	 */
	Collection<RightCategory> getRightCategories(String name);

	/**
	 * Get {@link Right}s with specified name.
	 * 
	 * @param name
	 *            The name
	 * @return The {@link Right}s.
	 */
	Collection<Right> getRights(String name);

	/**
	 * Get {@link Right} with specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The {@link Right}.
	 */
	Right getRight(String id);
}
