package com.collibra.dgc.core.model.user;

import java.util.Collection;

import com.collibra.dgc.core.model.Resource;

/**
 * The representation of a Group, it groups {@link User}'s so you can assign a {@link Role} to a Group using a
 * {@link Member}, and the users in this group inherit the roles of the group.
 * @author GKDAI63
 * 
 */
public interface Group extends Resource {
	/**
	 * Retrieves memberships of the group
	 * @return the users
	 */
	Collection<GroupMembership> getGroupMemberships();

	/**
	 * Retrieves the group name
	 * @return the group's name
	 */
	public abstract String getGroupName();

}
