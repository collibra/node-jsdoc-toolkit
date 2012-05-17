package com.collibra.dgc.core.dao;

import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.GroupMembership;
import com.collibra.dgc.core.model.user.User;

public interface GroupMembershipDao extends AbstractDao<GroupMembership> {

	public abstract GroupMembership getByGroupAndUser(Group group, User user);

}
