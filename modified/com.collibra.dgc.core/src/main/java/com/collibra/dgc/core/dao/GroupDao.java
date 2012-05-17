package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.user.Group;

/**
 * Data Access object for {@link Group}
 * @author GKDAI63
 * 
 */
public interface GroupDao extends AbstractDao<Group> {

	/**
	 * Retrieves all groups a user belongs too
	 * @param userId the user
	 * @return the groups he belongs too
	 */
	public abstract List<Group> getGroupsForUser(String userId);

}
