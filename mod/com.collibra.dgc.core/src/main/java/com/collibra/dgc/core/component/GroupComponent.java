package com.collibra.dgc.core.component;

import java.util.List;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.user.Group;

//TODO further documentation

/**
 * Public api for group management
 * @author GKDAI63
 * 
 */
public interface GroupComponent {

	/**
	 * Retrieves all groups for a user
	 * @param userRId the user
	 * @return the groups be belongs too
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_ID_NULL },
	 *             {@link DGCErrorCodes#USER_ID_EMPTY }
	 */
	public List<Group> getGroupsForUser(String userRId);

	/**
	 * Remove a group
	 * @param groupRId the id of group to remove
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#GROUP_ID_NULL},
	 *             {@link DGCErrorCodes#GROUP_ID_EMPTY}
	 */
	public void removeGroup(String groupRId);

	/**
	 * Adds a user to a group
	 * @param groupRId the group to add the user too
	 * @param userRId the user to be added too the group
	 * @return the group the user was added too
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#GROUP_ID_NULL},
	 *             {@link DGCErrorCodes#GROUP_ID_EMPTY}, {@link DGCErrorCodes#USER_ID_NULL },
	 *             {@link DGCErrorCodes#USER_ID_EMPTY }
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#GROUP_NOT_FOUND},
	 *             {@link DGCErrorCodes#USER_NOT_FOUND},
	 */
	public Group addUserToGroup(String groupRId, String userRId);

	/**
	 * Removes a user from a group
	 * @param groupRId the group to remove the user from
	 * @param userRId the user to be removed from the group
	 * @return the group the user was removed from
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#GROUP_ID_NULL},
	 *             {@link DGCErrorCodes#GROUP_ID_EMPTY}, {@link DGCErrorCodes#USER_ID_NULL },
	 *             {@link DGCErrorCodes#USER_ID_EMPTY }
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#GROUP_NOT_FOUND},
	 *             {@link DGCErrorCodes#USER_NOT_FOUND},
	 */
	public Group removeUserFromGroup(String groupRId, String userRId);

	/**
	 * Add's a group
	 * @param groupName the group's name
	 * @return the group
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#GROUP_NAME_NULL},
	 *             {@link DGCErrorCodes#GROUP_NAME_EMPTY}
	 */
	public Group addGroup(String groupName);

	/**
	 * Retrieves a group by it's id
	 * @param groupRId the id of the group
	 * @return the group
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#GROUP_ID_NULL},
	 *             {@link DGCErrorCodes#GROUP_ID_EMPTY}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#GROUP_NOT_FOUND}
	 */
	public Group getGroup(String groupRId);

}
