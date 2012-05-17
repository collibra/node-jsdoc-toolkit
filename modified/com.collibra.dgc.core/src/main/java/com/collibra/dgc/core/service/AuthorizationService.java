package com.collibra.dgc.core.service;

import java.util.Collection;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;

/**
 * Authorization service interface. Provides methods to access the {@link Right} s and {@link RightCategory}s.
 * 
 * @author amarnath
 */
public interface AuthorizationService {
	/**
	 * Get all {@link RightCategory}s.
	 * 
	 * @return The {@link RightCategory}s.
	 */
	Collection<RightCategory> findRightCategories();

	/**
	 * Get {@link RightCategory} for specified id.
	 * 
	 * @param id The id.
	 * @return The {@link RightCategory}.
	 */
	RightCategory findRightCategory(String id);

	/**
	 * Get {@link RightCategory}s with specified name.
	 * 
	 * @param name The name
	 * @return The {@link RightCategory}s.
	 */
	Collection<RightCategory> findRightCategories(String name);

	/**
	 * Get {@link Right}s with specified name.
	 * 
	 * @param name The name
	 * @return The {@link Right}s.
	 */
	Collection<Right> findRights(String name);

	/**
	 * Get {@link Right} with specified id.
	 * 
	 * @param id The id.
	 * @return The {@link Right}.
	 */
	Right findRight(String id);

	/**
	 * Checks if current user is authorized for specified right on specified {@link Resource}.
	 * 
	 * @param resource The {@link Resource}.
	 * @param right The right.
	 * @return True if authorized otherwise false.
	 */
	boolean isPermitted(Resource resource, String right);

	/**
	 * Checks if current user is authorized for specified right.
	 * 
	 * @param permission The right.
	 * @return True if authorized otherwise false.
	 */
	boolean isPermitted(String right);

	/**
	 * Checks if specified user is authorized for specified right on glossary.
	 * 
	 * @param user The user.
	 * @param right The right.
	 * @return True if authorized otherwise false.
	 */
	boolean isPermitted(String user, String right);

	/**
	 * Checks if specified user is authorized for specified right on the {@link Resource}. Note that it is a very simple
	 * check on whether user has right on the current resource and does not check on inherited rights.
	 * 
	 * @param user The user.
	 * @param resource The {@link Resource}.
	 * @param right The right.
	 * @return True if authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#USER_NAME_NULL}
	 */
	boolean isPermitted(String user, Resource resource, String right);

	/**
	 * To check if the current user is authorized to add members.
	 * 
	 * @return True if authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_NULL}
	 */
	boolean canAddMemberToCommunity(Community community);

	/**
	 * To check if the current user is authorized to remove members.
	 * 
	 * @return True if authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_NULL}
	 */
	boolean canRemoveMemberFromCommunity(Community community);

	/**
	 * Checks if the current user is authorized to change the {@link Role} of a {@link Member}.
	 * 
	 * @param community The {@link Community}.
	 * @return True if authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_NULL}
	 */
	boolean canChangeRoleInCommunity(Community community);

	/**
	 * Grant specified right to the {@link Role}.
	 * 
	 * @param role The {@link Role}.
	 * @param permission The right.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 */
	void grant(Role role, String permission);

	/**
	 * Grant specified rights to the {@link Role}.
	 * 
	 * @param role The {@link Role}.
	 * @param permissions The permission strings.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 */
	void grant(Role role, Collection<String> permissions);

	/**
	 * Grant complete hierarchy of rights for the specified {@link RightCategory}.
	 * 
	 * @param role The {@link Role}.
	 * @param permissionGroup The {@link RightCategory}.
	 * @see AuthorizationService#grant(Role, Collection)
	 */
	void grant(Role role, RightCategory permissionGroup);

	/**
	 * Checks if the {@link Role} has given right.
	 * 
	 * @param role The {@link Role}.
	 * @param permission The right.
	 * @return True if the {@link Role} is authorized otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ROLE_NULL}
	 */
	boolean isAuthorized(Role role, String permission);

	/**
	 * Revokes the right from {@link Role}.
	 * 
	 * @param role The {@link Role}.
	 * @param permission The right.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_REMOVE_RIGHT}
	 */
	void revoke(Role role, String permission);

	/**
	 * Revoke the rights from {@link Role}.
	 * 
	 * @param role The {@link Role}.
	 * @param permissions The permission strings.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_REMOVE_RIGHT}
	 */
	void revoke(Role role, Collection<String> permissions);

	/**
	 * Revoke complete hierarchy of rights for the specified {@link RightCategory}.
	 * 
	 * @param role The {@link Role}.
	 * @param permissionGroup The {@link RightCategory}.
	 * @see AuthorizationService#revoke(Role, Collection)
	 */
	void revoke(Role role, RightCategory permissionGroup);

	/**
	 * Check for authorization for current user at {@link Vocabulary} level and above.
	 * 
	 * @param vocabulary The {@link Vocabulary}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_NULL}
	 */
	boolean isPermitted(Vocabulary vocabulary, String permission);

	/**
	 * Check for authorization user at {@link Vocabulary} level and above.
	 * 
	 * @param user User name.
	 * @param vocabulary The {@link Vocabulary}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#COMMUNITY_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_NULL}
	 */
	boolean isPermitted(String user, Vocabulary vocabulary, String permission);

	/**
	 * Check for authorization for current user at {@link Representation} level and above.
	 * 
	 * @param representation The {@link Representation}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#REPRESENTATION_NULL}
	 */
	boolean isPermitted(Representation representation, String permission);

	/**
	 * Check for authorization for user at {@link Representation} level and above.
	 * 
	 * @param user User name.
	 * @param representation The {@link Representation}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#REPRESENTATION_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_NULL}
	 */
	boolean isPermitted(String user, Representation representation, String permission);

	/**
	 * Check for authorization for current user at {@link Community} level and above.
	 * 
	 * @param community The {@link Community}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_NULL}
	 */
	boolean isPermitted(Community community, String permission);

	/**
	 * Check for authorization for user at {@link Community} level and above.
	 * 
	 * @param user User name.
	 * @param community The {@link Community}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#COMMUNITY_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_NULL}
	 */
	boolean isPermitted(String user, Community community, String permission);

	/**
	 * Check for authorization for current user at {@link RuleSet} level and above.
	 * 
	 * @param ruleSet The {@link RuleSet}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 */
	boolean isPermitted(RuleSet ruleSet, String permission);

	/**
	 * Check for authorization for user at {@link RuleSet} level and above.
	 * 
	 * @param user User name.
	 * @param ruleSet The {@link RuleSet}.
	 * @param permission The right.
	 * @return True if authorized, otherwise false.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_NAME_NULL}
	 */
	boolean isPermitted(String user, RuleSet ruleSet, String permission);

	/**
	 * Get {@link Permissions} string for the specified {@link Resource} type (as defined in the {@link Constants}) and
	 * the global key.
	 * 
	 * @param resourceType The {@link Resource} type, as defined in the {@link Constants} .
	 * @param key The global permission key independent of {@link Resource} type.
	 * @return The {@link Permissions} string.
	 */
	String getPermission(String resourceType, String key);

	/**
	 * Set specified permissions on {@link Role} and remove all existing permissions.
	 * 
	 * @param role The {@link Role}.
	 * @param permissions The permission strings.
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#ROLE_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_NULL}
	 * @throws IllegalArgumentException with {@link DGCErrorCodes#PERMISSION_STRING_EMPTY}
	 * @throws AuthorizationException for {@link Permissions#GLOSSARY_REMOVE_RIGHT}
	 */
	void setPermissions(Role role, Collection<String> permissions);
}
