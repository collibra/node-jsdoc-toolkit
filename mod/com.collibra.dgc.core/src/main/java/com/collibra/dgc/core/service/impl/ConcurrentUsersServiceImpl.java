package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.security.TooManyConcurrentUsersException;
import com.collibra.dgc.core.security.authentication.RememberMeLoginListener;
import com.collibra.dgc.core.service.ConcurrentUsersService;
import com.collibra.dgc.core.service.license.LicenseService;

/**
 * Internal service class to keep track of the active users in the system.
 * @author GKDAI63
 * 
 */
public class ConcurrentUsersServiceImpl implements ConcurrentUsersService, AuthenticationListener,
		RememberMeLoginListener {

	@Autowired
	private DefaultWebSessionManager defaultWebSessionManager;
	@Autowired
	private LicenseService licenseService;

	private final List<String> activeUsers = new LinkedList<String>();

	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
		removeInactiveUsers();
		if (activeUsers.size() + 1 > licenseService.getConcurrentUserCount()) {
			throw new TooManyConcurrentUsersException();
		}
		activeUsers.add(token.getPrincipal().toString());
	}

	/**
	 * Calculates the current active users.
	 */
	// Shiro lazily notifies about expired sessions causing them to be listed as 'active sessions' while they are in
	// fact expired, this method will make sure only real active users are counted.
	// In short it removes the users who are still counted in shiro as active sessions removed from the active users
	private void removeInactiveUsers() {
		final Set<String> toRemoveFromActiveUsers = new HashSet<String>();
		Collection<Session> sessions = defaultWebSessionManager.getSessionDAO().getActiveSessions();
		for (String user : activeUsers) {
			boolean found = false;
			boolean timedOut = false;
			// Loop over connected users and sessions
			for (Session session : sessions) {

				Object principals = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if (principals != null) {
					SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;
					String username = (String) principalCollection.asList().get(0);

					if (user.equals(username)) {
						// We found a user that is both in the active users set, and has a session according to shiro
						found = true;
					}

					if (session.getLastAccessTime().getTime() + session.getTimeout() > System.currentTimeMillis()) {
						// We found a user that is both in the active users set, and has a session according to shiro,
						// but really it has expired, we should disregard this "active session" / "concurrent user"
						timedOut = true;
					}
				}
			}
			if (found && timedOut) {
				// If we found a user which has timed out, but is still regarded as active, we mark him for deletion.
				toRemoveFromActiveUsers.add(user);
			}
		}
		activeUsers.removeAll(toRemoveFromActiveUsers);
	}

	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {
		// NO-OP
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		activeUsers.remove(principals.getPrimaryPrincipal().toString());
	}

	@Override
	public void onSuccessfulRememberMeLogin(PrincipalCollection pc) {
		removeInactiveUsers();
		if (activeUsers.size() + 1 > licenseService.getConcurrentUserCount()) {
			throw new TooManyConcurrentUsersException();
		}
		activeUsers.add(pc.getPrimaryPrincipal().toString());
	}

	@Override
	public Integer getNumerOfActiveSessions() {
		removeInactiveUsers();
		return activeUsers.size();
	}

	@Override
	public List<String> getActiveUsers() {
		removeInactiveUsers();
		return activeUsers;
	}
}
