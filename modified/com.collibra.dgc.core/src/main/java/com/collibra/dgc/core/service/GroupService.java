package com.collibra.dgc.core.service;

import java.util.List;

import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.User;

/**
 * Service class to manage groups
 * @author GKDAI63
 * 
 */
public interface GroupService {

	public abstract List<Group> getGroupsForUser(String userId);

	/**
	 * Removes a group
	 * @param group the group to remove
	 */
	public abstract void removeGroup(Group group);

	/**
	 * Adds a user to a group
	 * @param group the group to add the user to
	 * @param user the user to add to the group
	 * @return the group the user was added to
	 */
	public abstract Group addUserToGroup(Group group, User user);

	/**
	 * Add a group
	 * @param group the group to add
	 * @return the group
	 */
	public abstract Group addGroup(Group group);

	/**
	 * Retrieves a group based on it's id
	 * @param groupId the id
	 * @return the group
	 */
	public abstract Group getGroup(String groupId);

	/**
	 * Removes a user from the group
	 * @param group the group to remove a user from
	 * @param user the user to be removed
	 * @return the group
	 */
	Group removeUserFromGroup(Group group, User user);

}
