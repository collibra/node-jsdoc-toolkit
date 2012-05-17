package com.collibra.dgc.core.component.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ConcurrentUsersComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.ConcurrentUsersService;

/**
 * Public API to retrieve the users active on the system.
 * @author GKDAI63
 * 
 */
@Component
public class ConcurrentUsersComponentImpl implements ConcurrentUsersComponent {

	@Autowired
	private ConcurrentUsersService concurrentUsersService;
	@Autowired
	private AuthorizationHelper authorizationHelper;

	/**
	 * Retrieve the number of active sessions
	 */
	@Override
	@Transactional
	public Integer getNumerOfActiveSessions() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_CONCURRENT_USERS);
		return concurrentUsersService.getNumerOfActiveSessions();
	}

	/**
	 * Retrieves the active users themselves Duplicate users means 2 people are logged in with the same account
	 */
	@Override
	@Transactional
	public List<String> getActiveUsers() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_CONCURRENT_USERS);
		return concurrentUsersService.getActiveUsers();
	}

}
