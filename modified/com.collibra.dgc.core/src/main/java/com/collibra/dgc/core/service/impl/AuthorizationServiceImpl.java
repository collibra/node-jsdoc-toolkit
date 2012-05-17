package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.RoleEventData;
import com.collibra.dgc.core.security.DGCRealm;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.AuthorizationsFactory;
import com.collibra.dgc.core.security.authorization.PermissionKeys;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;

/**
 * Implements {@link AuthorizationService} to provide the authorization service to clients and access to {@link Right}
 * and {@link RightCategory}s
 * 
 * @author amarnath
 * 
 */
@Service
public class AuthorizationServiceImpl extends AbstractService implements AuthorizationService {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

	@Autowired
	private AuthorizationsFactory rightsFactory;
	@Autowired
	private DGCRealm realm;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private RightsService rightsService;

	@Override
	public Right findRight(String id) {
		return rightsFactory.getRight(id);
	}

	@Override
	public Collection<Right> findRights(String name) {
		return rightsFactory.getRights(name);
	}

	@Override
	public Collection<RightCategory> findRightCategories() {
		return rightsFactory.getRightCategories();
	}

	@Override
	public Collection<RightCategory> findRightCategories(String name) {
		return rightsFactory.getRightCategories(name);
	}

	@Override
	public RightCategory findRightCategory(String id) {
		return rightsFactory.getRightCategory(id);
	}

	@Override
	public boolean canAddMemberToCommunity(Community community) {
		return isPermitted(community, Permissions.COMMUNITY_ADD_MEMBER);
	}

	@Override
	public boolean canRemoveMemberFromCommunity(Community community) {
		return isPermitted(community, Permissions.COMMUNITY_REMOVE_MEMBER);
	}

	@Override
	public boolean canChangeRoleInCommunity(Community community) {
		return isPermitted(community, Permissions.COMMUNITY_CHANGE_ROLE);
	}

	@Override
	public void grant(Role role, String permission) {
		List<String> permissions = new ArrayList<String>(1);
		permissions.add(permission);
		grant(role, permissions);
	}

	@Override
	public void grant(Role role, Collection<String> permissions) {

		// Notify the role change.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.CHANGING));

		grantInternal(role, permissions);

		Term roleTerm = role.getTerm();
		representationService.saveTerm(roleTerm);

		// Notify the role change.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.CHANGED));
	}

	@Override
	public void grant(Role role, RightCategory permissionGroup) {

		grant(role, permissionGroup.getCompleteHierarchyPermissionStrings());
	}

	@Override
	public void setPermissions(Role role, Collection<String> permissions) {

		if (!permissions.contains(Permissions.ADMIN) && role.getRights().contains(Permissions.ADMIN)) {
			rightsService.checkLastAdminRole(role);
		}

		// Notify the role change.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.CHANGING));

		// Remove all current permissions.
		revokeInternal(role, role.getRights());

		// Grant the specified permissions.
		grantInternal(role, permissions);

		Term roleTerm = role.getTerm();
		representationService.saveTerm(roleTerm);

		// Notify the role change.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.CHANGED));
	}

	@Override
	public void revoke(Role role, String permission) {

		if (permission.equals(Permissions.ADMIN)) {
			rightsService.checkLastAdminRole(role);
		}

		List<String> permissions = new ArrayList<String>(1);
		permissions.add(permission);
		revoke(role, permissions);
	}

	@Override
	public void revoke(Role role, Collection<String> permissions) {

		if (permissions.contains(Permissions.ADMIN)) {
			rightsService.checkLastAdminRole(role);
		}

		// Notify
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.REMOVING));

		revokeInternal(role, permissions);
		getCurrentSession().flush();

		// Notify
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.REMOVED));
	}

	@Override
	public void revoke(Role role, RightCategory permissionGroup) {

		final Collection<String> permissions = permissionGroup.getCompleteHierarchyPermissionStrings();
		if (permissions.contains(Permissions.ADMIN)) {
			rightsService.checkLastAdminRole(role);
		}

		revoke(role, permissions);
	}

	@Override
	public boolean isAuthorized(Role role, String permission) {

		return role.getRights().contains(permission);
	}

	@Override
	public boolean isPermitted(Vocabulary vocabulary, String permission) {

		if (!isPermitted(vocabulary.getId(), permission)) {
			if (vocabulary.getCommunity() != null) {
				return isPermitted(vocabulary.getCommunity(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPermitted(String user, Vocabulary vocabulary, String permission) {

		if (!isPermitted(user, vocabulary.getId(), permission)) {
			if (vocabulary.getCommunity() != null) {
				return isPermitted(user, vocabulary.getCommunity(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPermitted(Representation representation, String permission) {

		boolean allowed = isPermitted(representation.getId(), permission);
		if (!allowed && representation.getVocabulary() != null) {
			allowed = isPermitted(representation.getVocabulary(), permission);
		}
		if (allowed) {
			canPerform(representation, permission);
		}
		return allowed;
	}

	@Override
	public boolean isPermitted(String user, Representation representation, String permission) {

		boolean allowed = isPermitted(user, representation.getId(), permission);
		if (!allowed && representation.getVocabulary() != null) {
			allowed = isPermitted(user, representation.getVocabulary(), permission);
		}
		if (allowed) {
			canPerform(representation, permission);
		}
		return allowed;
	}

	/**
	 * Checks if the operation with specified permission is possible. If the operation has impact on semantic state of
	 * the resource then it will be blocked in case the resource is locked.
	 * 
	 * @param representation The {@link Representation}.
	 * @param permission The permission associated with the operation being performed.
	 */
	private void canPerform(Representation representation, String permission) {
		if (representation.isLocked() && !isStatusChangePermission(permission)) {
			Right right = rightsFactory.getRight(permission);
			if (right != null && right.hasImpactOnLock()) {
				throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_LOCKED, representation.verbalise(),
						representation.getId());
			}
		}
	}

	/**
	 * The status change must be allowed, because if the user has permission to unlock the representation will be
	 * unlocked.
	 */
	private final boolean isStatusChangePermission(String permission) {
		if (Permissions.TERM_STATUS_MODIFY.equals(permission) || Permissions.BFTF_STATUS_MODIFY.equals(permission)
				|| Permissions.CF_STATUS_MODIFY.equals(permission)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isPermitted(Community community, String permission) {

		if (!isPermitted(community.getId(), permission)) {
			if (community.getParentCommunity() != null) {
				return isPermitted(community.getParentCommunity(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPermitted(String user, Community community, String permission) {

		if (!isPermitted(user, community.getId(), permission)) {
			if (community.getParentCommunity() != null) {
				return isPermitted(user, community.getParentCommunity(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPermitted(RuleSet ruleSet, String permission) {

		if (!isPermitted(ruleSet.getId(), permission)) {
			if (ruleSet.getVocabulary() != null) {
				return isPermitted(ruleSet.getVocabulary(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPermitted(String user, RuleSet ruleSet, String permission) {
		if (!isPermitted(user, ruleSet.getId(), permission)) {
			if (ruleSet.getVocabulary() != null) {
				return isPermitted(user, ruleSet.getVocabulary(), permission);
			}
			return false;
		}
		return true;
	}

	@Override
	public String getPermission(String resourceType, String key) {
		return PermissionKeys.getPermission(resourceType, key);
	}

	private final void checkIsValidPermission(String permission) {
		// check if the permission is valid and active.
		Right right = rightsFactory.getRight(permission);
		if (right == null || !right.isActive()) {
			log.error("'" + permission + "' is not active or invalid");
			throw new AuthorizationException(DGCErrorCodes.AUTHORIZATION_FAILED_INACTIVE_PERMISSION, getCurrentUser(),
					permission);
		}
	}

	private void revokeInternal(Role role, Collection<String> permissions) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		role.getRights().removeAll(permissions);
		getCurrentSession().save(role);
	}

	private void grantInternal(Role role, Collection<String> permissions) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		for (String permission : permissions) {

			if (isAuthorized(role, permission)) {
				log.warn("Role '" + role.getTerm().getSignifier() + "' already has right '" + permission + "'");
				continue;
			}

			final Right right = findRight(permission);
			if (!role.isGlobal() && right.isGlobal()) {
				throw new IllegalArgumentException(DGCErrorCodes.NON_GLOBAL_PERMISSION_FOR_GLOBAL_ROLE);
			}

			role.getRights().add(permission);
		}
		getCurrentSession().save(role);
	}

	@Override
	@Transactional
	public boolean isPermitted(String right) {
		checkIsValidPermission(right);
		String permission = "*:" + right + ":" + "*";
		return checkPermission(getCurrentUser(), permission);
	}

	@Override
	public boolean isPermitted(Resource resource, String right) {
		return isPermitted(resource == null ? null : resource.getId(), right);
	}

	@Override
	public boolean isPermitted(String username, Resource resource, String right) {
		return isPermitted(username, resource == null ? null : resource.getId(), right);
	}

	private boolean isPermitted(String username, String resourceId, String right) {
		checkIsValidPermission(right);
		String permission = "*:" + right + ":" + (resourceId == null ? "*" : resourceId);
		return checkPermission(username, permission);
	}

	private boolean checkPermission(String username, String permission) {
		if ("true".equals(System.getProperty(Constants.DISABLE_SECURITY))) {
			return true;
		}
		return realm.isPermitted(new SimplePrincipalCollection(username, realm.getName()), permission);
	}

	@Override
	public boolean isPermitted(String user, String right) {
		return isPermitted(user, (String) null, right);
	}
}
