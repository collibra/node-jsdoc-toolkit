package com.collibra.dgc.core.component.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.RightsComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.util.Defense;

/**
 * Rights API implementation.
 * @author amarnath
 * 
 */
@Component
public class RightsComponentImpl implements RightsComponent {
	@Autowired
	private RightsService rightsService;
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private CommunityService communityService;

	@Override
	@Transactional
	public Member addMember(String userName, String roleName, String resourceRId) {

		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		Defense.notEmpty(resourceRId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_EMPTY, "resourceRId");

		return rightsService.addMember(userName, roleName, resourceRId);
	}

	@Override
	@Transactional
	public Member addMember(String ownerId, String roleName) {
		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "ownerId");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.addMember(ownerId, rightsService.getRoleWithError(roleName));
	}

	@Override
	@Transactional
	public boolean changeMemberRole(String ownerId, String roleName, String resourceRId, String newRoleName) {

		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "ownerId");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		Defense.notEmpty(resourceRId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_EMPTY, "resourceRId");
		Defense.notEmpty(newRoleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "newRoleName");

		rightsService.changeRole(rightsService.getMemberWithError(ownerId, roleName, resourceRId), newRoleName);
		return true;
	}

	@Override
	@Transactional
	public Role addRole(String roleName, String description, boolean global) {

		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.createRole(roleName, description, global);
	}

	@Override
	@Transactional
	public Member getMember(String ownerId, String resourceRId, String roleName) {

		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "name");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");

		return rightsService.findMember(ownerId, resourceRId == null ? null : resourceRId, roleName);
	}

	@Override
	@Transactional
	public Collection<Member> getMemberRoles(String ownerId, String RId) {
		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "name");
		Defense.notEmpty(RId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_NULL, "RId");

		return rightsService.findMemberRoles(ownerId, RId);
	}

	@Override
	@Transactional
	public Collection<Member> getMembers(Collection<String> resourceRIds) {

		List<String> list = new ArrayList<String>(resourceRIds.size());
		for (String id : resourceRIds) {
			if (id != null && !id.isEmpty()) {
				list.add(id);
			}
		}
		return rightsService.findMembers(list.toArray(new String[0]));
	}

	@Override
	@Transactional
	public Collection<Member> getMembers(String RId) {
		Defense.notEmpty(RId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_NULL, "RId");

		return rightsService.findMembers(RId);
	}

	@Override
	@Transactional
	public Collection<Member> getMembersByResourceAndRole(String RId, String roleName) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		Defense.notEmpty(RId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_NULL, "RId");

		return rightsService.findMembersWithRole(RId, roleName);
	}

	@Override
	@Transactional
	public Collection<Member> getMembersByRole(String roleName) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.findMembersWithRole(rightsService.getRoleWithError(roleName));
	}

	@Override
	@Transactional
	public Collection<Member> getMembersByOwner(String ownerId) {
		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return rightsService.findMembers(ownerId);
	}

	@Override
	@Transactional
	public Role getRole(String RId) {
		Defense.notEmpty(RId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_NULL, "RId");
		return rightsService.findRoleByName(RId);
	}

	@Override
	@Transactional
	public Role getRoleByName(String roleName) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.findRoleByName(roleName);
	}

	@Override
	@Transactional
	public Collection<Role> getRoles() {
		return rightsService.getRoles();
	}

	@Override
	@Transactional
	public Collection<String> getOwners(Collection<String> resourceRIds) {
		List<String> list = new ArrayList<String>(resourceRIds.size());
		for (String id : resourceRIds) {
			if (id != null && !id.isEmpty()) {
				list.add(id);
			}
		}

		return rightsService.findOwners(list.toArray(new String[0]));
	}

	@Override
	@Transactional
	public Collection<String> getOwners(String RId) {
		Defense.notEmpty(RId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_EMPTY, "RId");
		return rightsService.findOwners(RId);
	}

	@Override
	@Transactional
	public Member removeMember(String userName, String roleName, String resourceRId) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		Defense.notEmpty(resourceRId, DGCErrorCodes.RESOURCE_ID_NULL, DGCErrorCodes.RESOURCE_ID_EMPTY, "resourceRId");
		return rightsService.removeMember(userName, roleName, resourceRId);
	}

	@Override
	@Transactional
	public Member removeMember(String ownerId, String roleName) {
		Defense.notEmpty(ownerId, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.removeMember(ownerId, rightsService.getRoleWithError(roleName));
	}

	@Override
	@Transactional
	public Role removeRole(String roleName) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.removeRole(roleName);
	}

	@Override
	@Transactional
	public Role removeRole(String roleName, boolean removeMembers, boolean removeRoleTerm) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.removeRole(rightsService.getRoleWithError(roleName), removeMembers, removeRoleTerm);
	}

	@Override
	@Transactional
	public Role removeRoleIfNoMembersExist(String roleName) {
		Defense.notEmpty(roleName, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "roleName");
		return rightsService.removeRoleIfNoMembersExist(roleName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getGlobalRoles()
	 */
	@Override
	@Transactional
	public Collection<Role> getGlobalRoles() {
		return rightsService.getGlobalRoles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getResourceRoles()
	 */
	@Override
	@Transactional
	public Collection<Role> getResourceRoles() {
		return rightsService.getResourceRoles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getRightCategories()
	 */
	@Override
	@Transactional
	public Collection<RightCategory> getRightCategories() {
		return authorizationService.findRightCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getRightCategory(java.lang.String)
	 */
	@Override
	@Transactional
	public RightCategory getRightCategory(String id) {
		Defense.notEmpty(id, DGCErrorCodes.RIGHT_CATEGORY_ID_NULL, DGCErrorCodes.RIGHT_CATEGORY_ID_EMPTY, "id");
		return authorizationService.findRightCategory(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getRightCategories(java.lang.String)
	 */
	@Override
	@Transactional
	public Collection<RightCategory> getRightCategories(String name) {
		Defense.notEmpty(name, DGCErrorCodes.RIGHT_CATEGORY_NAME_NULL, DGCErrorCodes.RIGHT_CATEGORY_NAME_EMPTY, "name");
		return authorizationService.findRightCategories(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.RightsComponent#getRights(java.lang.String)
	 */
	@Override
	@Transactional
	public Collection<Right> getRights(String name) {
		Defense.notEmpty(name, DGCErrorCodes.RIGHT_NAME_NULL, DGCErrorCodes.RIGHT_NAME_EMPTY, "name");
		return authorizationService.findRights(name);
	}

	@Override
	@Transactional
	public Right getRight(String id) {
		Defense.notEmpty(id, DGCErrorCodes.RIGHT_ID_NULL, DGCErrorCodes.RIGHT_ID_EMPTY, "id");
		return authorizationService.findRight(id);
	}

	@Override
	@Transactional
	public boolean isPermitted(String permission) {
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		return authorizationService.isPermitted(permission);
	}

	@Override
	@Transactional
	public boolean isPermittedOnRepresentation(String representationRId, String permission) {
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		Defense.notEmpty(representationRId, DGCErrorCodes.REPRESENTATION_ID_NULL,
				DGCErrorCodes.REPRESENTATION_ID_EMPTY, "representationRId");

		final Representation rep = representationService.findRepresentationByResourceId(representationRId);
		if (rep == null) {
			throw new EntityNotFoundException(DGCErrorCodes.REPRESENTATION_NOT_FOUND, representationRId);
		}
		return authorizationService.isPermitted(rep, permission);
	}

	@Override
	@Transactional
	public boolean isPermittedOnCommunity(String communityRId, String permission) {

		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		Defense.notEmpty(communityRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY,
				"communityRId");

		final Community comm = communityService.findCommunity(communityRId);
		if (comm == null) {
			throw new EntityNotFoundException(DGCErrorCodes.COMMUNITY_NOT_FOUND_ID, communityRId);
		}
		return authorizationService.isPermitted(comm, permission);
	}

	@Override
	@Transactional
	public boolean isPermittedOnVocabulary(String vocabularyRId, String permission) {
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		final Vocabulary voc = representationService.getVocabularyWithError(vocabularyRId);
		if (voc == null) {
			throw new EntityNotFoundException(DGCErrorCodes.VOCABULARY_NOT_FOUND_ID, vocabularyRId);
		}
		return authorizationService.isPermitted(voc, permission);
	}

	/*
	 * Granting/revoking rights for roles.
	 */

	private final Role getRoleWithError(String roleRId) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");

		final Role role = rightsService.findRoleByName(roleRId);
		if (role == null) {
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_ID, roleRId);
		}
		return role;
	}

	private final RightCategory getCategoryWithError(String rightCategoryId) {
		Defense.notEmpty(rightCategoryId, DGCErrorCodes.CATEGORY_ID_NULL, DGCErrorCodes.CATEGORY_ID_EMPTY,
				"rightCategoryId");

		final RightCategory category = authorizationService.findRightCategory(rightCategoryId);
		if (category == null) {
			throw new EntityNotFoundException(DGCErrorCodes.RIGHTCATEGORY_NOT_FOUND, rightCategoryId);
		}
		return category;
	}

	@Override
	@Transactional
	public void grant(String roleRId, String permission) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		authorizationService.grant(getRoleWithError(roleRId), permission);
	}

	@Override
	@Transactional
	public void grant(String roleRId, Collection<String> permissions) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permissions, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permissions");

		authorizationService.grant(getRoleWithError(roleRId), permissions);
	}

	@Override
	@Transactional
	public void grantCategory(String roleRId, String rightCategoryId) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(rightCategoryId, DGCErrorCodes.CATEGORY_ID_NULL, DGCErrorCodes.CATEGORY_ID_EMPTY,
				"rightCategoryId");

		authorizationService.grant(getRoleWithError(roleRId), getCategoryWithError(rightCategoryId));
	}

	@Override
	@Transactional
	public boolean isAuthorized(String roleRId, String permission) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		return authorizationService.isAuthorized(getRoleWithError(roleRId), permission);
	}

	@Override
	@Transactional
	public void revoke(String roleRId, String permission) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permission, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permission");
		authorizationService.revoke(getRoleWithError(roleRId), permission);
	}

	@Override
	@Transactional
	public void revoke(String roleRId, Collection<String> permissions) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permissions, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permissions");
		authorizationService.revoke(getRoleWithError(roleRId), permissions);
	}

	@Override
	@Transactional
	public void revokeCategory(String roleRId, String rightCategoryId) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(rightCategoryId, DGCErrorCodes.CATEGORY_ID_NULL, DGCErrorCodes.CATEGORY_ID_EMPTY,
				"rightCategoryId");
		authorizationService.revoke(getRoleWithError(roleRId), getCategoryWithError(rightCategoryId));
	}

	@Override
	@Transactional
	public void setPermissions(String roleRId, Collection<String> permissions) {
		Defense.notEmpty(roleRId, DGCErrorCodes.ROLE_RESOURCE_ID_NULL, DGCErrorCodes.ROLE_RESOURCE_ID_EMPTY, "roleRId");
		Defense.notEmpty(permissions, DGCErrorCodes.PERMISSION_STRING_NULL, DGCErrorCodes.PERMISSION_STRING_EMPTY,
				"permissions");
		authorizationService.setPermissions(getRoleWithError(roleRId), permissions);
	}
}
