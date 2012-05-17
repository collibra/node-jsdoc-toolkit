/**
 * 
 */
package com.collibra.dgc.core.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.observer.GroupEventAdapter;
import com.collibra.dgc.core.observer.GroupUserEventAdapter;
import com.collibra.dgc.core.observer.MemberEventAdapter;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.RoleEventAdapter;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.GroupEventData;
import com.collibra.dgc.core.observer.events.GroupUserEventData;
import com.collibra.dgc.core.observer.events.MemberChangeEventData;
import com.collibra.dgc.core.observer.events.MemberEventData;
import com.collibra.dgc.core.observer.events.RoleEventData;
import com.collibra.dgc.core.service.GroupService;
import com.collibra.dgc.core.service.impl.RightsServiceHelper;
import com.collibra.dgc.core.util.HashUtil;

/**
 * The security (Shiro) realm for DGC. This will wrap another realm to do the real authentication.
 * 
 * @author dieterwachters
 */
public class DGCRealm extends AuthorizingRealm {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RightsServiceHelper rightsService;
	@Autowired
	private GroupService groupService;
	// private static final String SYSTEM_ADMIN_PERMISSION = "*:*:*";

	private static final Logger log = LoggerFactory.getLogger(DGCRealm.class);

	private final Map<String, DGCAuthorizationInfo> userData = new HashMap<String, DGCAuthorizationInfo>();

	public DGCRealm() {
		ObservationManager.getInstance().register(new RoleEventsListener(), 0, GlossaryEventCategory.ROLE);
		ObservationManager.getInstance().register(new MemberEventListener(), 0, GlossaryEventCategory.MEMBER);
		ObservationManager.getInstance().register(new GroupEventListener(), 0, GlossaryEventCategory.GROUP);
		ObservationManager.getInstance().register(new GroupUserEventListener(), 0, GlossaryEventCategory.GROUP_USERS);

		setCacheManager(new MemoryConstrainedCacheManager());

		setCredentialsMatcher(new CredentialsMatcher() {
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				if ("true".equals(System.getProperty(Constants.DISABLE_SECURITY))) {
					return true;
				}

				final ByteSource salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();

				String hashedPassword = HashUtil.hash(new String(((UsernamePasswordToken) token).getPassword()), salt)[0];
				return info.getCredentials().equals(hashedPassword);
			}
		});
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		final Object availablePrincipal = getAvailablePrincipal(principals);
		if (availablePrincipal == null) {
			return null;
		}
		final String userName = availablePrincipal.toString();

		if (!userData.containsKey(userName)) {
			initializeUser(userName);
		}
		AuthorizationInfo info = userData.get(userName);

		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String userName = token.getPrincipal().toString();
		final User user = userDao.findByUserName(userName);
		if (user != null) {
			return new DGCAuthenticationInfo(user, getName());
		}
		return null;
	}

	private void resetUserRoles(final Role role) {
		log.info("resetting user roles" + role.getName() + role.getRights());
		String roleUUID = role.getTerm().getId().toString();
		final Set<String> usersToReset = new HashSet<String>();
		for (final String user : userData.keySet()) {
			final DGCAuthorizationInfo authd = userData.get(user);
			if (authd != null && authd.getRoles().contains(roleUUID)) {
				usersToReset.add(user);
			}
		}

		for (final String user : usersToReset) {
			initializeUser(user);
		}
	}

	private void addMember(final Member member) {
		DGCAuthorizationInfo authd = userData.get(member.getOwnerId());
		if (authd == null) {
			initializeUser(member.getOwnerId());
		}
		authd = userData.get(userDao.get(member.getOwnerId()).getUserName());
		if (authd == null) {
			log.info("Unable to owner '" + member.getOwnerId() + "'.");
			return;
		}

		synchronized (authd) {
			authd.getStringPermissions().add(getMemberRightsString(member));
			authd.getRoles().add(member.getRole().getTerm().getId().toString());
		}
	}

	private void removeMember(final String resourceId) {
		log.info("removing user with id " + resourceId + " from security cache");
		userData.remove(userDao.get(resourceId).getUserName());
	}

	private void initializeUser(final String userName) {

		// Gather permissions for the user.
		final Set<String> roles = new HashSet<String>();
		final Set<String> permissions = new HashSet<String>();

		User u = userDao.findByUserName(userName);
		if (u == null) {
			return;
		}
		Collection<Member> members = rightsService.findMembers(u.getId());
		for (Member member : members) {
			roles.add(member.getRole().getTerm().getId().toString());
			permissions.add(getMemberRightsString(member));
		}

		List<Group> groups = groupService.getGroupsForUser(u.getId());

		for (Group group : groups) {
			members = rightsService.findMembers(group.getId());
			for (Member member : members) {
				roles.add(member.getRole().getTerm().getId().toString());
				permissions.add(getMemberRightsString(member));
			}
		}

		userData.put(userName, new DGCAuthorizationInfo(permissions, roles));
	}

	private String getMemberRightsString(Member member) {
		StringBuilder sb = new StringBuilder("*:");
		Set<String> rights = member.getRole().getRights();

		int i = 0;
		for (final String right : rights) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(right);
			i++;
		}

		sb.append(":").append(member.getResourceId() == null ? "*" : member.getResourceId());
		return sb.toString();
	}

	private class RoleEventsListener extends RoleEventAdapter {
		@Override
		public void onChanged(RoleEventData data) {
			resetUserRoles(data.getRole());
		}

		@Override
		public void onRemove(RoleEventData data) {
			resetUserRoles(data.getRole());
		}
	}

	private void clearCache() {
		log.info("Clearing security cache");
		userData.clear();
	}

	private class MemberEventListener extends MemberEventAdapter {
		@Override
		public void onAdd(MemberEventData data) {
			if (data.getMember().isGroup()) {
				clearCache();
			} else {
				addMember(data.getMember());
			}
		}

		@Override
		public void onRemove(MemberEventData data) {
			removeMember(data.getMember().getOwnerId());
		}

		@Override
		public void onChanged(MemberEventData data) {
			MemberChangeEventData changedData = (MemberChangeEventData) data;
			if (changedData.getOldMember().isGroup() || changedData.getMember().isGroup()) {
				clearCache();
			} else {
				removeMember(changedData.getOldMember().getOwnerId());
				addMember(changedData.getMember());
			}
		}

	}

	private class GroupUserEventListener extends GroupUserEventAdapter {

		@Override
		public void onChanged(GroupUserEventData data) {
			super.onChaning(data);
			removeMember(data.getUser().getId());
		}

	}

	private class GroupEventListener extends GroupEventAdapter {

		@Override
		public void onRemove(GroupEventData data) {
			clearCache();
			super.onRemove(data);
		}
	}

}
