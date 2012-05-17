package com.collibra.dgc.core.service;

import java.security.Permissions;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;

/**
 * 
 * @author amarnath
 * 
 */
public interface RightsService {

	/**
	 * Add member.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param resource The {@link Resource}.
	 * @return Added {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, String) for error codes and exceptions.
	 */
	Member addMember(String ownerId, String roleName, Resource resource);

	/**
	 * Add member.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}
	 * @param resource The {@link Resource}.
	 * @return Added {@link Member}.
	 * @see RightsService#addMember(String, Role, String) for error codes and exceptions.
	 */
	Member addMember(String ownerId, Role role, Resource resource);

	/**
	 * Add member.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param resourceId The resource UUID.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#TERM_NULL}, {@link DGCErrorCodes#BFTF_NULL},
	 *             {@link DGCErrorCodes#CF_NULL}, {@link DGCErrorCodes#NAME_NULL}, {@link DGCErrorCodes#VOCABULARY_NULL}
	 *             , {@link DGCErrorCodes#COMMUNITY_NULL},
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException
	 */
	Member addMember(String ownerId, Role role, String resourceId);

	/**
	 * Add member.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param resourceId The resource UUID.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, UUID) for error codes and exceptions.
	 */
	Member addMember(String ownerId, String roleName, String resourceId);

	/**
	 * Add member to {@link Term}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param term The {@link Term}.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, Term) for error codes and exceptions
	 */
	Member addMember(String ownerId, String roleName, Term term);

	/**
	 * Add member to {@link Term}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param term The {@link Term}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#TERM_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException for {@link Permissions#TERM_ADD_MEMBER}
	 */
	Member addMember(String ownerId, Role role, Term term);

	/**
	 * Add member to {@link BinaryFactTypeForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param bftf The {@link BinaryFactTypeForm}.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, BinaryFactTypeForm) for error codes and exceptions
	 */
	Member addMember(String ownerId, String roleName, BinaryFactTypeForm bftf);

	/**
	 * Add member to {@link BinaryFactTypeForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param bftf The {@link BinaryFactTypeForm}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#BFTF_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException for {@link Permissions#BFTF_ADD_MEMBER}
	 */
	Member addMember(String ownerId, Role role, BinaryFactTypeForm bftf);

	/**
	 * Add member {@link CharacteristicForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param cf The {@link CharacteristicForm}.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, CharacteristicForm) for error codes and exceptions
	 */
	Member addMember(String ownerId, String roleName, CharacteristicForm cf);

	/**
	 * Add member {@link CharacteristicForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param cf The {@link CharacteristicForm}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#CF_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException for {@link Permissions#CF_ADD_MEMBER}
	 */
	Member addMember(String ownerId, Role role, CharacteristicForm cf);

	/**
	 * Add member to {@link Vocabulary}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param vocabulary The {@link Vocabulary}.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, Vocabulary) for error codes and exceptions.
	 */
	Member addMember(String ownerId, String roleName, Vocabulary vocabulary);

	/**
	 * Add member to {@link Vocabulary}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param vocabulary The {@link Vocabulary}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#VOCABULARY_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException for {@link Permissions#VOCABULARY_ADD_MEMBER}
	 */
	Member addMember(String ownerId, Role role, Vocabulary vocabulary);

	/**
	 * Add member to a {@link Community}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param community The {@link Community}.
	 * @return The {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#addMember(String, Role, Community) for error codes and exceptions.
	 */
	Member addMember(String ownerId, String roleName, Community community);

	/**
	 * Add member to a {@link Community}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param community The {@link Community}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#COMMUNITY_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException for {@link Permissions#COMMUNITY_ADD_MEMBER}
	 */
	Member addMember(String ownerId, Role role, Community community);

	/**
	 * Add a global member.
	 * 
	 * @param owner The owner.
	 * @param role The {@link Role}.
	 * @return The {@link Member}.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_MGMT_MEMBER_REMOVE}
	 */
	Member addMember(String user, Role role);

	/**
	 * Remove member.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param resource The {@link Resource}.
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#RESOURCE_NULL}
	 * @see RightsService#removeMember(String, Role, UUID) for error codes and exceptions
	 */
	Member removeMember(String ownerId, String roleName, Resource resource);

	/**
	 * Remove member.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param resource The {@link Resource}.
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#RESOURCE_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}
	 * @see RightsService#removeMember(String, Role, UUID) for error codes and exceptions
	 */
	Member removeMember(String ownerId, Role role, Resource resource);

	/**
	 * Remove member.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param resourceId The resource id.
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, UUID) for error codes and exceptions
	 */
	Member removeMember(String ownerId, String roleName, String resourceId);

	/**
	 * Remove member.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param resourceId The resource id.
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#TERM_NULL}, {@link DGCErrorCodes#BFTF_NULL},
	 *             {@link DGCErrorCodes#CF_NULL}, {@link DGCErrorCodes#NAME_NULL}, {@link DGCErrorCodes#VOCABULARY_NULL}
	 *             , {@link DGCErrorCodes#COMMUNITY_NULL}
	 * @throws AuthorizationException
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 */
	Member removeMember(String ownerId, Role role, String resourceId);

	/**
	 * Remove member from {@link Term}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param term The {@link Term}
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, Term) for error codes and exceptions
	 */
	Member removeMember(String ownerId, String roleName, Term term);

	/**
	 * Remove member from {@link Term}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param term The {@link Term}
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#TERM_NULL}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#TERM_REMOVE_MEMBER}
	 */
	Member removeMember(String ownerId, Role role, Term term);

	/**
	 * Remove member from {@link BinaryFactTypeForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param bftf The {@link BinaryFactTypeForm}
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, BinaryFactTypeForm) for error codes and exceptions.
	 */
	Member removeMember(String ownerId, String roleName, BinaryFactTypeForm bftf);

	/**
	 * Remove member from {@link BinaryFactTypeForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param bftf The {@link BinaryFactTypeForm}
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#BFTF_NULL}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#BFTF_REMOVE_MEMBER}
	 */
	Member removeMember(String ownerId, Role role, BinaryFactTypeForm bftf);

	/**
	 * Remove member from {@link CharacteristicForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param cf The {@link CharacteristicForm}
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, CharacteristicForm) for error codes and exceptions
	 */
	Member removeMember(String ownerId, String roleName, CharacteristicForm cf);

	/**
	 * Remove member from {@link CharacteristicForm}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param cf The {@link CharacteristicForm}
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#CF_NULL}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#CF_REMOVE_MEMBER}
	 */
	Member removeMember(String ownerId, Role role, CharacteristicForm cf);

	/**
	 * Remove member from {@link Vocabulary}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param vocabulary The {@link Vocabulary}
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, Vocabulary) for error codes and exceptions.
	 */
	Member removeMember(String ownerId, String roleName, Vocabulary vocabulary);

	/**
	 * Remove member from {@link Vocabulary}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param vocabulary The {@link Vocabulary}
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#VOCABULARY_NULL}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#VOCABULARY_REMOVE_MEMBER}
	 */
	Member removeMember(String ownerId, Role role, Vocabulary vocabulary);

	/**
	 * Remove member from {@link Community}.
	 * 
	 * @param ownerId The owner's id.
	 * @param roleName The role name.
	 * @param semanticCommunity The {@link Community}.
	 * @return The removed {@link Member}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see RightsService#removeMember(String, Role, Community) for error codes and exceptions.
	 */
	Member removeMember(String ownerId, String roleName, Community semanticCommunity);

	/**
	 * Remove member from {@link Community}.
	 * 
	 * @param ownerId The owner's id.
	 * @param role The {@link Role}.
	 * @param semanticCommunity The {@link Community}.
	 * @return The removed {@link Member}.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#COMMUNITY_NULL}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#MEMBER_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#COMMUNITY_REMOVE_MEMBER}
	 */
	Member removeMember(String ownerId, Role role, Community semanticCommunity);

	/**
	 * Remove member from glossary.
	 * 
	 * @param owner The owner.
	 * @param role The {@link Role}.
	 * @return The removed {@link Member}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_MGMT_MEMBER_REMOVE}
	 */
	Member removeMember(String owner, Role role);

	/**
	 * Get {@link Role} with specified name.
	 * 
	 * @param name The role name.
	 * @return The {@link Role}.
	 */
	Role findRoleByName(String name);

	/**
	 * Find the {@link Role}s that have a specific permission.
	 * @param permission The permission to look for
	 * @return The collection of {@link Role}s with the given permission.
	 */
	Collection<Role> findRoles(String permission);

	/**
	 * Find {@link Role} for specified resource id.
	 * 
	 * @param resourceId The resource id.
	 * @return The {@link Role}.
	 */
	Role findRole(String resourceId);

	/**
	 * Final all {@link Role}s for specified user and {@link Resource} id.
	 * 
	 * @param owner The owner's id.
	 * @param resourceId The {@link Resource} id.
	 * @return The {@link Role}s.
	 */
	Collection<Member> findMemberRoles(String owner, String resourceId);

	/**
	 * Get {@link Member}.
	 * 
	 * @param owner The owner's id.
	 * @param resource The {@link Resource}.
	 * @param role {@link Role}.
	 * @return The {@link Member}.
	 */
	Member findMember(String owner, Resource resource, Role role);

	/**
	 * Get {@link Member}.
	 * 
	 * @param owner The owner's id.
	 * @param resource The {@link Resource}.
	 * @param roleName The {@link Role} name.
	 * @return
	 */
	Member findMember(String owner, Resource resource, String roleName);

	/**
	 * Get {@link Member}
	 * 
	 * @param owner The owner's id.
	 * @param resourceId The {@link Resource} id.
	 * @param roleName The {@link Role} name.
	 * @return
	 */
	Member findMember(String owner, String resourceId, String roleName);

	/**
	 * Get {@link Member}.
	 * 
	 * @param owner The owner's id.
	 * @param resourceId The {@link Resource} id.
	 * @param role The {@link Role}.
	 * @return
	 */
	Member findMember(String owner, String resourceId, Role role);

	/**
	 * Get {@link Member}s for specified {@link Resource} id.
	 * 
	 * @param resourceIds The {@link Resource} id.
	 * @return The {@link Member}s.
	 */
	Collection<Member> findMembers(String... resourceIds);

	/**
	 * Get {@link Member}s for glossary resource
	 * 
	 * @return The {@link Member}s.
	 */
	Collection<Member> findGlossaryMembers();

	/**
	 * Get owner's ids associated with the {@link Resource}s.
	 * 
	 * @param resourceIds The resource ids.
	 * @return The owner's ids.
	 */
	Set<String> findOwners(String... resourceIds);

	/**
	 * Get {@link Member}s
	 * 
	 * @param resourceId
	 * @param startingWith
	 * @param maxNumber
	 * @return
	 */
	Collection<Member> findMembers(String resourceId, String startingWith, int maxNumber);

	/**
	 * Get {@link Member}s with specified {@link Resource} id and {@link Role}.
	 * 
	 * @param resourceId The {@link Resource} id.
	 * @param role The {@link Role}.
	 * @return The {@link Member}s.
	 */
	Collection<Member> findMembers(String resourceId, Role role);

	/**
	 * Get {@link Member}s with specified {@link Role} and {@link Resource}.
	 * 
	 * @param resourceId The {@link Resource} id.
	 * @param roleName The {@link Role} name.
	 * @return
	 */
	Collection<Member> findMembersWithRole(String resourceId, String roleName);

	/**
	 * Get all members with specified {@link Role}.
	 * 
	 * @param role The {@link Role}.
	 * @return All {@link Member}s having the specified {@link Role}.
	 */
	Collection<Member> findMembersWithRole(Role role);

	/**
	 * Get all {@link Member}s for specified user on all resources with all roles.
	 * 
	 * @param ownerId The owner's id.
	 * @return The {@link Member}s.
	 */
	Collection<Member> findMembers(String ownerId);

	/**
	 * Get {@link Member}s with specified {@link Role} name and owner's id.
	 * 
	 * @param roleName The {@link Role} name.
	 * @param owner The owner's id.
	 * @return The {@link Member}s.
	 */
	Collection<Member> findMembers(String roleName, String owner);

	/**
	 * Get {@link Member}s with specified {@link Role} and owner's id.
	 * 
	 * @param role The {@link Role}.
	 * @param owner The owner's id.
	 * @return The {@link Member}s.
	 */
	Collection<Member> findMembers(Role role, String owner);

	/**
	 * Get all {@link Role}s defined in glossary.
	 * 
	 * @return The {@link Role}s.
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
	 * Changes the role of {@link Member}.
	 * 
	 * @param member The {@link Member}.
	 * @param roleName The new {@link Role} name
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#MEMBER_NULL}
	 * @throw {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see #changeRole(Member, Role) for more error codes and exceptions.
	 */
	void changeRole(Member member, String roleName);

	/**
	 * Changes the role of {@link Member}.
	 * 
	 * @param member The {@link Member}.
	 * @param role The new {@link Role}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#MEMBER_NULL},
	 *             {@link DGCErrorCodes#ROLE_NULL}, {@link DGCErrorCodes#TERM_NULL}, {@link DGCErrorCodes#BFTF_NULL},
	 *             {@link DGCErrorCodes#CF_NULL}, {@link DGCErrorCodes#NAME_NULL}, {@link DGCErrorCodes#VOCABULARY_NULL}
	 *             , {@link DGCErrorCodes#COMMUNITY_NULL}
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#MEMBER_DUPLICATE}
	 * @throws AuthorizationException
	 */
	void changeRole(Member member, Role role);

	/**
	 * Create {@link Role} with specified {@link Term}.
	 * 
	 * @param term The {@link Term} describing the role.
	 * @param global Whether this role if global or not (resource).
	 * @return The newly created {@link Role}.
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#ROLE_ALREADY_EXISTS}
	 */
	Role createRole(Term term, boolean global);

	/**
	 * Create {@link Role} with specified name. Note that in this case the {@link Term} describing the role will be
	 * created in Roles and Responsibilities {@link Vocabulary}.
	 * 
	 * @param roleName The {@link Role} name.
	 * @param description The description.
	 * @param global Whether this role if global or not (resource).
	 * @return The newly created {@link Role}.
	 * @throws ConstraintViolationException with error code {@link DGCErrorCodes#ROLE_ALREADY_EXISTS}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NAME_EMPTY}
	 */
	Role createRole(String roleName, String description, boolean global);

	/**
	 * Remove specified {@link Role} from glossary. Note that this can have a big impact on the glossary as all
	 * {@link Member}s having this role will also be removed.
	 * 
	 * @param role The {@link Role} to be removed.
	 * @return The removed {@link Role}.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NULL}
	 */
	Role removeRole(Role role);

	/**
	 * Remove specified {@link Role} from glossary. Note that this can have a big impact on the glossary as all
	 * {@link Member}s having this role will also be removed.
	 * 
	 * @param roleName The {@link Role} to be removed.
	 * @return The removed {@link Role}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see #removeRole(Role) for error codes and exceptions.
	 */
	Role removeRole(String roleName);

	/**
	 * Remove specified {@link Role} from glossary. If there exist any {@link Member} with the specified {@link Role}
	 * then this operation will fail.
	 * 
	 * @param roleName The {@link Role} to be removed.
	 * @return The removed {@link Role} if successful otherwise null.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see #removeRoleIfNoMembersExist(Role) for error codes and exceptions.
	 */
	Role removeRoleIfNoMembersExist(String roleName);

	/**
	 * Remove specified {@link Role} from glossary. If there exist any {@link Member} with the specified {@link Role}
	 * then this operation will fail.
	 * 
	 * @param role The {@link Role} to be removed.
	 * @return The removed {@link Role} if successful otherwise null.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NULL}
	 */
	Role removeRoleIfNoMembersExist(Role role);

	/**
	 * Remove specified {@link Role} from glossary.
	 * 
	 * @param roleName The {@link Role} name.
	 * @param removeMembers Flag to remove all members with the specified {@link Role}.
	 * @param removeRoleTerm Flag to remove the {@link Term} associated with the {@link Role}.
	 * @return Removed {@link Role} if success otherwise null.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ROLE_NOT_FOUND}
	 * @see #removeRole(Role, boolean, boolean) for error codes and exceptions.
	 */
	Role removeRole(String roleName, boolean removeMembers, boolean removeRoleTerm);

	/**
	 * Remove specified {@link Role} from glossary.
	 * 
	 * @param role The {@link Role}.
	 * @param removeMembers Flag to remove all members with the specified {@link Role}.
	 * @param removeRoleTerm Flag to remove the {@link Term} associated with the {@link Role}.
	 * @return Removed {@link Role} if success otherwise null.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NULL}
	 */
	Role removeRole(Role role, boolean removeMembers, boolean removeRoleTerm);

	/**
	 * To remove all {@link Member}s of the specified user/group.
	 * 
	 * @param owner The user/group id.
	 */
	void removeAllMembers(String owner);

	/**
	 * 
	 * @param name
	 * @return
	 */
	Role getRoleWithError(String name);

	/**
	 * 
	 * @param ownerId
	 * @param roleName
	 * @param resourceId
	 * @return
	 */
	Member getMemberWithError(String ownerId, String roleName, String resourceId);

	/**
	 * Checks whether this is the last role that supplies admin access (and is assigned with a member).
	 * @param role
	 */
	void checkLastAdminRole(Role role);
}