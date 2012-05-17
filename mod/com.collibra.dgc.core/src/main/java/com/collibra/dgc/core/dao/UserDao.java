package com.collibra.dgc.core.dao;

import java.util.Collection;

import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;

public interface UserDao {

	User findByUserName(String userName);

	Class getOwnerType(String userName);

	User save(User user);

	User get(String id);

	void remove(User user);

	Collection<UserData> findUsers(ResourceFilter filter, int offset, int max);
}
