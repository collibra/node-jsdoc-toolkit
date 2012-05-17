package com.collibra.dgc.core.component;

import java.security.Permissions;
import java.util.Collection;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.RightsService;

/**
 * With the RightsComponent you can edit the rights: add/edit/remove members, roles and permissions.
 * 
 * NOTE: an owner of a Member can be both a {@link User} or a {@link Group}. The ownerId is the resource id of either of
 * one of these entities.
 * 
 */
public interface RightsComponent {

	/*
	 * Handling members
	 */

	/**
	 * Add member.
	 * @param ownerId The id of the entity owning the Membership
	 * @param roleName The role name
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return The {@link Member}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 */
	Member addMember(String ownerId, String roleName, String resourceRId);

	/**
	 * Add a global member.
	 * @param ownerId The id of the entity owning the Membership
	 * @param roleName The {@link Role} name
	 * @return The {@link Member}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_MGMT_MEMBER_REMOVE}
	 */
	Member addMember(String ownerId, String roleName);

	/**
	 * Change {@link Role}.
	 * @param ownerId The id of the entity owning the Membership
	 * @param roleName The role name
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param newRoleName The new role name
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#changeRole(Member, String)
	 */
	boolean changeMemberRole(String ownerId, String roleName, String resourceRId, String newRoleName);

	/**
	 * Get {@link Member}
	 * @param ownerId The id of the entity owning the Membership
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param roleName The {@link Role} name.
	 * @return
	 */
	Member getMember(String ownerId, String resourceRId, String roleName);

	/**
	 * Get {@link Member}s with specified user name and {@link Resource}.
	 * @param ownerId The id of the entity owning the Membership
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return The {@link Member}s
	 */
	Collection<Member> getMemberRoles(String ownerId, String resourceRId);

	/**
	 * Get all the {@link Member}s for the specified {@link Resource}s.
	 * @param resourceRIds The resource ids of {@link Resource}s
	 * @return {@link Member}s
	 */
	Collection<Member> getMembers(Collection<String> resourceRIds);

	/**
	 * Get all the {@link Member}s for the specified {@link Resource} id.
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return {@link Member}s
	 */
	Collection<Member> getMembers(String resourceRId);

	/**
	 * Get {@link Member}s with specified {@link Role} and {@link Resource}.
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param roleName The {@link Role} name
	 * @return {@link Member}s
	 */
	Collection<Member> getMembersByResourceAndRole(String resourceRId, String roleName);

	/**
	 * Get {@link Member}s with specified {@link Role}.
	 * @param roleName The {@link Role} name
	 * @return
	 */
	Collection<Member> getMembersByRole(String roleName);

	/**
	 * Get all {@link Member}s for specified user on all resources with all roles.
	 * @param ownerId The id of the entity owning the Membership
	 * @return The {@link Member}s
	 */
	Collection<Member> getMembersByOwner(String ownerId);

	/**
	 * Remove member.
	 * @param ownerId The id of the entity owning the Membership
	 * @param roleName The role name
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return The removed {@link Member}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#RESOURCE_NULL}
	 */
	Member removeMember(String ownerId, String roleName, String resourceRId);

	/**
	 * Remove member from glossary.
	 * @param ownerId The id of the entity owning the Membership
	 * @param roleName The {@link Role} name
	 * @return The removed {@link Member}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_MGMT_MEMBER_REMOVE}
	 */
	Member removeMember(String user, String roleName);

	/**
	 * Get entity's id's associated with the specified {@link Resource}s.
	 * @param resourceRIds The resource ids of the {@link Resource}s
	 * @return The owner Id's.
	 */
	Collection<String> getOwners(Collection<String> resourceRIds);

	/**
	 * Get owners associated with the specified {@link Resource}.
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return The owner's resource id's.
	 */
	Collection<String> getOwners(String resourceRId);

	/*
	 * Every thing for roles
	 */

	/**
	 * Create {@link Role} with specified name. Note that in this case the {@link Term} describing the role will be
	 * created in Roles and Responsibilities {@link Vocabulary}.
	 * @param roleName The {@link Role} name
	 * @param description The description
	 * @param global Whether this role if global or not (resource)
	 * @return The newly created {@link Role}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#ROLE_ALREADY_EXISTS}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#ROLE_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NAME_EMPTY}
	 */
	Role addRole(String roleName, String description, boolean global);

	/**
	 * Find {@link Role} for specified resource id.
	 * @param resourceRId The resource id of the {@link Resource}
	 * @return The {@link Role}.
	 */
	Role getRole(String resourceRId);

	/**
	 * Find the {@link Role} with given name.
	 * @param name The name of the {@link Role}
	 * @return The {@link Role}
	 */
	Role getRoleByName(String name);

	/**
	 * @return All {@link Role}s defined in the system.
	 */
	Collection<Role> getRoles();

	/**
	 * @return All the global {@link Role}s defined in the system.
	 */
	Collection<Role> getGlobalRoles();

	/**
	 * @return All the resource (non-global) {@link Role}s defined in the system.
	 */
	Collection<Role> getResourceRoles();

	/**
	 * Remove specified {@link Role} from glossary. Note that this can have a big impact on the glossary as all
	 * {@link Member}s having this role will also be removed
	 * @param roleName The {@link Role} to be removed
	 * @return The removed {@link Role}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 */
	Role removeRole(String roleName);

	/**
	 * Remove specified {@link Role} from glossary.
	 * @param roleName The {@link Role} name
	 * @param removeMembers Flag to remove all members with the specified {@link Role}
	 * @param removeRoleTerm Flag to remove the {@link Term} associated with the {@link Role}
	 * @return Removed {@link Role} if success otherwise null
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 */
	Role removeRole(String roleName, boolean removeMembers, boolean removeRoleTerm);

	/**
	 * Remove specified {@link Role} from glossary. If there exist any {@link Member} with the specified {@link Role}
	 * then this operation will fail
	 * @param roleName The {@link Role} to be removed
	 * @return The removed {@link Role} if successful otherwise null
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 */
	Role removeRoleIfNoMembersExist(String roleName);

	/*
	 * Everything for rights
	 */

	/**
	 * Get all {@link RightCategory}s.
	 * 
	 * @return The {@link RightCategory}s
	 */
	Collection<RightCategory> getRightCategories();

	/**
	 * Get {@link RightCategory} for specified id.
	 * 
	 * @param id The id of the {@link RightCategory}
	 * @return The {@link RightCategory}
	 */
	RightCategory getRightCategory(String id);

	/**
	 * Get {@link RightCategory}s with specified name.
	 * 
	 * @param name The name of the {@link RightCategory}
	 * @return The {@link RightCategory}s
	 */
	Collection<RightCategory> getRightCategories(String name);

	/**
	 * Get {@link Right}s with specified name.
	 * 
	 * @param name The name of the {@link Right}
	 * @return The {@link Right}s
	 */
	Collection<Right> getRights(String name);

	/**
	 * Get {@link Right} with specified id.
	 * 
	 * @param id The id of the {@link Right}
	 * @return The {@link Right}
	 */
	Right getRight(String id);

	/*
	 * Granting/revoking of permissions
	 */

	/**
	 * Grant specified right to the {@link Role}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permission The right
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 */
	void grant(String roleRId, String permission);

	/**
	 * Grant specified rights to the {@link Role}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permissions The permission strings
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 */
	void grant(String roleRId, Collection<String> permissions);

	/**
	 * Grant complete hierarchy of rights for the specified {@link RightCategory}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param rightCategoryId The resourceRId of the {@link RightCategory}
	 * @see AuthorizationService#grant(Role, Collection)
	 */
	void grantCategory(String roleRId, String rightCategoryId);

	/**
	 * Checks if the {@link Role} has given right.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permission The right
	 * @return True if the {@link Role} is authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NULL}
	 */
	boolean isAuthorized(String roleRId, String permission);

	/**
	 * Revokes the right from {@link Role}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permission The right
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_REMOVE_RIGHT}
	 */
	void revoke(String roleRId, String permission);

	/**
	 * Revoke the rights from {@link Role}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permissions The permission strings
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_REMOVE_RIGHT}
	 */
	void revoke(String roleRId, Collection<String> permissions);

	/**
	 * Revoke complete hierarchy of rights for the specified {@link RightCategory}.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param rightCategoryId The resourceRId of the {@link RightCategory}
	 * @see AuthorizationService#revoke(Role, Collection)
	 */
	void revokeCategory(String roleRId, String rightCategoryId);

	/**
	 * Set specified permissions on {@link Role} and remove all existing permissions.
	 * 
	 * @param roleRId The resource id of the {@link Role}
	 * @param permissions The permission strings
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 */
	void setPermissions(String roleRId, Collection<String> permissions);

	/*
	 * Checking of permissions
	 */

	/**
	 * Check if the current user has the given permission.
	 * @param permission The permission to check for
	 * @return True if the current user has the given permission. False otherwise
	 */
	boolean isPermitted(String permission);

	/**
	 * Check if the current user has the given permission for this representation.
	 * @param permission The permission to check for
	 * @param representationRId The UUID of the representation to check for
	 * @return True if the current user has the given permission for this representation. False otherwise
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	public boolean isPermittedOnRepresentation(String representationRId, String permission);

	/**
	 * Check if the current user has the given permission for this vocabulary.
	 * @param permission The permission to check for
	 * @param vocabularyRId The UUID of the vocabulary to check for
	 * @return True if the current user has the given permission for this vocabulary. False otherwise
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	public boolean isPermittedOnVocabulary(String vocabularyRId, String permission);

	/**
	 * Check if the current user has the given permission for this community.
	 * @param permission The permission to check for
	 * @param communityRId The UUID of the community to check for
	 * @return True if the current user has the given permission for this community. False otherwise
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	public boolean isPermittedOnCommunity(String communityRId, String permission);
}