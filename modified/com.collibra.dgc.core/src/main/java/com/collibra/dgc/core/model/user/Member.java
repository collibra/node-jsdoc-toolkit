package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * Member defines the relationship between a {@link User} and its {@link Role}s. Optionally a {@link Resource} limits
 * the role to be valid for a specific resource.
 * 
 * @author amarnath
 * 
 */
public interface Member {
	/**
	 * Get user name.
	 * 
	 * @return
	 */
	String getOwnerId();

	/**
	 * Returns whether or not the member is linked to a group or a user
	 * 
	 * @return true if group
	 */
	boolean isGroup();

	/**
	 * Get {@link Role}.
	 * 
	 * @return
	 */
	Role getRole();

	/**
	 * Get the optional {@link Resource} ID for which the {@link Member} is associated.
	 * 
	 * @return
	 */
	String getResourceId();

	/**
	 * For cloning
	 * 
	 * @return
	 */
	Member clone();
}
