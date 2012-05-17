package com.collibra.dgc.core.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.job.Job;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.sun.xml.bind.v2.schemagen.xmlschema.AttributeType;

/**
 * 
 * @author amarnath
 * 
 */
public interface DGCErrorCodes {

	/* UNKNOWN */

	/**
	 * Generic exception code for exception that doesn't have code (cf. REST interface).
	 */
	final String UNKNOWN_CODE = "unknown";

	/**
	 * The argument(s) of the method is/are null.
	 * 
	 * @param argumentname(s) the argument(s) which are null
	 */
	final String ARGUMENT_NULL = "argumentNull";

	/**
	 * The argument(s) of the method is/are empty.
	 */
	final String ARGUMENT_EMPTY = "argumentEmpty";

	/**
	 * The method argument(s) is/are invalid.
	 * 
	 * @param argumentname(s) the argument(s) which are empty
	 */
	final String ARGUMENT_INVALID = "argumentInvalid";

	/* BOOTSTRAP */

	/**
	 * The bootstrapper's username was null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_USERNAME_NULL = "boostrapUsernameNull";

	/**
	 * The bootstrapper's username was empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_USERNAME_EMPTY = "boostrapUsernameEmpty";

	/**
	 * The bootstrapper's driver was null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_DRIVER_NULL = "boostrapDriverNull";

	/**
	 * The bootstrapper's driver was empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_DRIVER_EMPTY = "boostrapDriverEmpty";

	/**
	 * The bootstrapper's database was null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_DATABASE_NULL = "boostrapDatabaseNull";

	/**
	 * The bootstrapper's database was empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_DATABASE_EMPTY = "boostrapDatabaseEmpty";

	/**
	 * The bootstrapper's url was null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_URL_NULL = "boostrapUrlNull";

	/**
	 * The bootstrapper's url was empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String BOOTSTRAP_URL_EMPTY = "boostrapUrlEmpty";

	/**
	 * The requested bootstrap script seems to be invalid.
	 * 
	 * @param bootstrapScriptName The name of the bootstrap script
	 */
	final String BOOTSTRAP_SCRIPT_INVALID = "bootstrapScriptInvalid";

	/**
	 * The requested bootstrap script was not found.
	 * 
	 * @param bootstrapScriptName The name of the bootstrap script
	 */
	final String BOOTSTRAP_NOT_FOUND = "bootstrapNotFound";

	/**
	 * Bootstrapping process failed.
	 */
	final String BOOTSTRAP_FAILED = "bootstrapFailed";

	/* JOB */

	/**
	 * The requested job was not found.
	 * 
	 * @param rId {@link Job} id
	 */
	final String JOB_NOT_FOUND = "jobNotFound";

	/**
	 * The {@link Job} 's id was null.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String JOB_ID_NULL = "jobIdNull";

	/**
	 * The {@link Job} 's id was empty.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String JOB_ID_EMPTY = "jobIdEmpty";

	/**
	 * The {@link Job} 's message was null.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String JOB_MESSAGE_NULL = "jobMessageNull";

	/**
	 * The {@link Job} 's message was empty.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String JOB_MESSAGE_EMPTY = "jobMessageEmpty";

	/* DATABASE */

	/**
	 * When we could not connect to the database when trying to initialize.
	 * 
	 * @param url The URL of the connection
	 */
	final String COULD_NOT_CONNECT_TO_DATABASE = "couldNotConnectToDatabase";

	/**
	 * The system was unable to detect the hibernate dialect from the given database driver.
	 * 
	 * @param sqlDriver The SQL driver string
	 */
	final String COULD_NOT_DETERMINE_DIALECT = "couldNotDetectDialect";

	/* EMAIL */

	/**
	 * A problem occurred with initializing or rendering the email
	 * 
	 * @param subject The subject of the email
	 */
	final String EMAIL_RENDERING = "emailRendering";

	/* USER */

	/**
	 * User first name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_FIRST_NAME_NULL = "userFirstNameNull";

	/**
	 * User first name cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_FIRST_NAME_EMPTY = "userFirstNameEmpty";

	/**
	 * User last name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_LAST_NAME_NULL = "userLastNameNull";

	/**
	 * User last name cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_LAST_NAME_EMPTY = "userLastNameEmpty";

	/**
	 * User email cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_EMAIL_NULL = "userEmailNull";

	/**
	 * User email cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_EMAIL_EMPTY = "userEmailEmpty";

	/**
	 * User language cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_LANGUAGE_NULL = "userLanguageNull";

	/**
	 * User language cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_LANGUAGE_EMPTY = "userLanguageEmpty";

	/**
	 * User name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_NAME_NULL = "userNameNull";

	/**
	 * User name cannot be empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_NAME_EMPTY = "UserNameEmpty";

	/**
	 * User url cannot be empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_URL_EMPTY = "userUrlEmpty";

	/**
	 * User url cannot be null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_URL_NULL = "userUrlNull";

	/**
	 * The user was not found
	 * 
	 * @param paramater the paramater used to retrieve the user
	 */
	final String USER_NOT_FOUND = "userNotFound";

	/**
	 * User id cannot be null.
	 * 
	 * @param argumentName the name of the arguemnt
	 */
	final String USER_ID_NULL = "userIdNull";

	/**
	 * User id cannot be null.
	 * 
	 * @param argumentName the name of the arguemnt
	 */
	final String USER_ID_EMPTY = "userIdEmpty";

	/**
	 * User website type cannot be empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_WEBSITE_TYPE_EMPTY = "userWebsiteTypeEmpty";

	/**
	 * User website type cannot be null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_WEBSITE_TYPE_NULL = "userWebsiteTypeNull";

	/**
	 * User website was not found
	 * 
	 * @param id the id that wasn't found
	 */
	final String USER_WEBSITE_NOT_FOUND = "userWebsiteNotFound";

	/**
	 * User Phone cannot be null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_PHONE_NULL = "userPhoneNull";

	/**
	 * User Phone cannot be empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_PHONE_EMPTY = "userPhoneEmpty";

	/**
	 * User Phone type cannot be null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_PHONE_TYPE_NULL = "userPhoneTypeNull";

	/**
	 * User Phone type cannot be empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_PHONE_TYPE_EMPTY = "userPhoneTypeEmpty";

	/**
	 * User Phone not found
	 * 
	 * @param id the id not found
	 */
	final String USER_PHONE_NOT_FOUND = "userPhoneNotFound";

	/**
	 * User im account type was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_IM_ACCOUNT_TYPE_NULL = null;

	/**
	 * User im account was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_IM_ACCOUNT_TYPE_EMPTY = null;

	/**
	 * User im account type was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_IM_ACCOUNT_NULL = null;

	/**
	 * User im account was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_IM_ACCOUNT_EMPTY = null;
	/**
	 * User's instant messaging account was not found
	 * 
	 * @param id the id not found
	 */
	final String USER_IM_NOT_FOUND = "userImNotFound";

	/**
	 * The user's address city was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_CITY_NULL = "userAddressCityNull";

	/**
	 * The user's address city was Empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_CITY_EMPTY = "userAddressCityEmpty";

	/**
	 * The user's address street was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_STREET_NULL = "userAddressStreetNull";

	/**
	 * The user's address city was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_STREET_EMPTY = "userAddressStreetEmpty";

	/**
	 * The user's address number was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_NUMBER_NULL = "userAddressNumberNull";

	/**
	 * The user's address number was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_NUMBER_EMPTY = "userAddressNumberEmpty";

	/**
	 * The user's address province was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_PROVINCE_NULL = "userAddressProvinceNull";

	/**
	 * The user's address province was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_PROVINCE_EMPTY = "userAddressProvinceEmpty";

	/**
	 * The user's address country was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_COUNTRY_NULL = "userAddressCountryNull";

	/**
	 * The user's address country was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_COUNTRY_EMPTY = "userAddressCountryEmpty";

	/**
	 * The user's address type was null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_TYPE_NULL = "userAddressTypeNull";

	/**
	 * The user's address province was empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_TYPE_EMPTY = "userAddressTypeEmpty";
	/**
	 * The address was not found
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDRESS_NOT_FOUND = "userAddressNotFound";

	/**
	 * The additional email address was not found
	 * 
	 * @param argumentName The name of the argument
	 */
	final String USER_ADDITIONAL_EMAIL_NOT_FOUND = "userAdditionalEmailNotFound";

	/**
	 * The user / group with the given name was not found.
	 * 
	 * @param name The name of the user / group
	 */
	final String USER_OR_GROUP_NOT_FOUND_NAME = "userOrGroupNotFoundName";

	/**
	 * The user / group with the given name doesn't exist.
	 * 
	 * @param name The name of the user / group
	 */
	final String USER_OR_GROUP_ID_DOES_NOT_EXIST = "userOrGroupIdDoesNotExist";

	/**
	 * Cannot delete this member as it is the last one with admin rights.
	 * 
	 * @param name The name of the member
	 * @param rId The resource id of the member
	 */
	final String LAST_MEMBER_WITH_ADMIN_RIGHTS = "lastMemberWithAdminRights";

	/**
	 * Cannot delete this role as it is the last one with admin rights.
	 * 
	 * @param name The name of the role
	 * @param rId The resource id of the role
	 */
	final String LAST_ROLE_WITH_ADMIN_RIGHTS = "lastRoleWithAdminRights";

	/**
	 * Cannot delete this user as it is the last one with admin rights.
	 * 
	 * @param userName The username of the user
	 */
	final String LAST_USER_WITH_ADMIN_RIGHTS = "lastUserWithAdminRights";

	/**
	 * The specified password is too weak.
	 */
	final String PASSWORD_TOO_WEAK = "passwordTooWeak";

	/**
	 * The password is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String PASSWORD_NULL = "passwordNull";

	/**
	 * The password is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String PASSWORD_EMPTY = "passwordEmpty";

	/**
	 * When the requested account was not found during login.
	 */
	final String LOGIN_UNKNOWN_ACCOUNT = "loginUnknownAccount";

	/**
	 * When the given credentials (password) are wrong during login.
	 */
	final String LOGIN_INCORRECT_CREDENTIALS = "loginIncorrectCredentials";

	/**
	 * When the requested account is disabled.
	 */
	final String LOGIN_DISABLED_ACCOUNT = "loginDisabledAccount";

	/**
	 * When the maximum of concurrent users is reached
	 */
	final String LOGIN_TOO_MANY_CONCURRENT_USERS = "loginTooManyConcurrentUsers";

	/**
	 * General fallback message when login failed.
	 */
	final String LOGIN_FAILED = "loginFailed";

	/**
	 * The {@link Member} with the specified user name, role, resource not found.
	 * 
	 * @param userName The name of the {@link User}
	 * @param roleName The name of the {@link Role}
	 * @param resourceRId The {@link Resource} resource id
	 */
	final String MEMBER_NOT_FOUND = "memberNotFound";

	/**
	 * The {@link Member} with the specified user name, role, community not found.
	 * 
	 * @param userName The name of the {@link User}
	 * @param roleName The name of the {@link Role}
	 * @param communityRId The {@link Community} resource id
	 */
	final String MEMBER_NOT_FOUND_COMMUNITY = "memberNotFoundCom";

	/**
	 * {@link Member} with given user name, role and {@link Resource} already exists.
	 */
	final String MEMBER_DUPLICATE = "duplicateMember";

	/**
	 * {@link Member} is null
	 */
	final String MEMBER_NULL = "memberNull";

	/**
	 * Member has no resource specified but the given role is not global.
	 */
	final String MEMBER_WITHOUT_RESOURCE_AND_RESOURCE_ROLE = "memberWithoutResourceAndResourceRole";

	/**
	 * {@link String} userName is null
	 */
	final String USERNAME_NULL = "usernameNull";

	/**
	 * A user with this user name already exists.
	 * 
	 * @param userName The username of the user
	 */
	final String USERNAME_ALREADY_EXISTS = "userNameAlreadyExists";

	/**
	 * {@link Role} with specified resource id not found.
	 * 
	 * @param rId The {@link Role} resource id
	 */
	final String ROLE_NOT_FOUND_ID = "roleNotFoundId";

	/**
	 * {@link Role} with specified name not found.
	 * 
	 * @param name The {@link Role} name
	 */
	final String ROLE_NOT_FOUND_NAME = "roleNotFoundName";

	/**
	 * {@link RightCategory} with specified name or resource id not found.
	 * 
	 * @param rId The {@link RightCategory} resource id
	 */
	final String RIGHTCATEGORY_NOT_FOUND = "rightCategoryNotFound";

	/**
	 * {@link Role} already exists.
	 */
	final String ROLE_ALREADY_EXISTS = "roleAlreadyExists";

	/**
	 * {@link Role} cannot be null.
	 */
	final String ROLE_NULL = "roleNull";

	/**
	 * {@link Role} name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ROLE_NAME_NULL = "roleNameNull";

	/**
	 * {@link Role} name is empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ROLE_NAME_EMPTY = "roleNameEmpty";

	/**
	 * {@link Role} resource id is null.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String ROLE_RESOURCE_ID_NULL = "roleResourceIdNull";

	/**
	 * {@link Role} resource id is empty.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String ROLE_RESOURCE_ID_EMPTY = "roleResourceIdEmpty";
	/**
	 * Permission string cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String PERMISSION_STRING_NULL = "permissionStringNull";

	/**
	 * Permission string cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String PERMISSION_STRING_EMPTY = "permissionStringEmpty";

	/**
	 * Permission string cannot be null.
	 */
	final String NON_GLOBAL_PERMISSION_FOR_GLOBAL_ROLE = "nonGlobalPermissionForGlobalRole";

	/**
	 * {@link Right} id cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_ID_NULL = "rightIdNull";

	/**
	 * {@link Right} id cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_ID_EMPTY = "rightIdEmpty";

	/**
	 * {@link Right} name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_NAME_NULL = "rightNameNull";

	/**
	 * {@link Right} name cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_NAME_EMPTY = "rightNameEmpty";

	/**
	 * {@link RightCategory} cannot be null.
	 */
	final String RIGHT_CATEGORY_ID_NULL = "rightCategoryIdNull";

	/**
	 * {@link RightCategory} cannot be null.
	 */
	final String RIGHT_CATEGORY_ID_EMPTY = "rightCategoryIdEmpty";

	/**
	 * {@link RightCategory} cannot be null.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String RIGHT_CATEGORY_NULL = "rightCategoryNull";

	/**
	 * {@link RightCategory}'s name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_CATEGORY_NAME_NULL = "rightcategoryNameNull";

	/**
	 * {@link RightCategory}'s name cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RIGHT_CATEGORY_NAME_EMPTY = "rightcategoryNameEmpty";

	/* AUTHORIZATION */

	/**
	 * Any {@link AuthorizationException} will use this code. To know more details check the
	 * {@link AuthorizationException#getPermissions()}.
	 */
	final String AUTHORIZATION_FAILED = "authorizationFailed";

	/**
	 * Any {@link AuthorizationException} will use this code. To know more details check the
	 * {@link AuthorizationException#getPermissions()}.
	 */
	final String AUTHORIZATION_FAILED_INACTIVE_PERMISSION = "authorizationFailedInactivePermission";

	/**
	 * Public registrations are not allowed. Only admin can create new users now.
	 */
	final String PUBLIC_REGISTRATIONS_NOT_ALLOWED = "permissionPublicRegistrationsNotAllowed";

	/**
	 * The user has not specified permissions at global level.
	 * 
	 * @param userName The user name (already set)
	 * @param permissionsString The permissions string (already set)
	 * @param
	 */
	final String GLOBAL_NO_PERMISSION = "globalNoPermission";

	/**
	 * The user isn't an admin therefore can't access licensing.
	 */
	final String NO_PERMISSION_TO_ACCESS_LICENSING = "noPermissionToAccessLicensing";

	/**
	 * The user isn't an admin therefore can't access the concurrent users.
	 */
	final String NO_PERMISSION_TO_ACCESS_CONCURRENT_USERS = "noPermissionToAccessConcurrentUsers";

	/**
	 * The user has not specified permissions on the resource.
	 * 
	 * @param userName The user name (already set)
	 * @param permissionsString The permissions string (already set)
	 * @param resourceVerbalise The verbalise of the {@link resource} (already set)
	 * @param resourceRId The resource id of the {@link Resource} (already set)
	 */
	final String RESOURCE_NO_PERMISSION = "resourceNoPermission";

	/**
	 * The user has not specified permissions on the attribute.
	 * 
	 * @param userName The user name (already set)
	 * @param permissionsString The permissions string (already set)
	 * @param attributeLabelVerbalise The verbalise of the {@link Attribute} label {@link Term} (already set)
	 * @param resourceRId The resource id of the {@link Attribute} label {@link Term} (already set)
	 * @param resourceVerbalise The verbalise of the {@link resource} (already set)
	 * @param resourceRId The resource id of the {@link Resource} (already set)
	 */
	final String ATTRIBUTE_NO_PERMISSION = "attributeNoPermission";

	// Glossary administration authorization messages.
	final String REMOVE_USER = "managementRemoveUser";
	final String CHANGE_USER = "managementChangeUser";
	final String CHANGE_PASSWORD = "managementChangePassword";
	final String CONFIGURATION_EDIT_NO_PERMISSION = "managementConfigurationEdit";

	// Authorization Others
	/**
	 * The permission for the resource are not defined.
	 * 
	 * @param userName The user name (already set)
	 * @param permissionsString The permissions string (already set)
	 * @param attributeLabelVerbalise The verbalise of the {@link Attribute} label {@link Term} (already set)
	 * @param resourceRId The resource id of the {@link Attribute} label {@link Term} (already set)
	 * @param resourceVerbalise The verbalise of the {@link resource} (already set)
	 * @param resourceRId The resource id of the {@link Resource} (already set)
	 */
	final String REPRESENTATION_NO_PERMISSIONS_DEFINED = "authorizationPermissionsUndefined";

	// Workflow authorization messages
	final String START_WORKFLOW_NO_PERMISSION = "permissionWorkflowStart";
	final String STOP_WORKFLOW_NO_PERMISSION = "permissionWorkflowStop";

	/* RULES */

	/**
	 * {@link RuleSet} is null
	 */
	final String RULESET_NULL = "rulesetNull";

	/**
	 * {@link RuleSet} already exists.
	 */
	final String RULESET_ALREADY_EXISTS = "ruleSetAlreadyExists";

	/**
	 * {@link RuleSet} cannot be null.
	 * 
	 * @param rId The {@link RuleSet} resource id.
	 */
	final String RULESET_NOT_FOUND = "ruleSetNotFound";

	/**
	 * {@link RuleSet} id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULESET_ID_NULL = "ruleSetIdNull";

	/**
	 * {@link RuleSet} id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULESET_ID_EMPTY = "ruleSetIdEmpty";

	/**
	 * {@link RuleSet} name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULESET_NAME_NULL = "rulesetNameNull";

	/**
	 * {@link RuleSet} name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULESET_NAME_EMPTY = "rulesetNameEmpty";

	/**
	 * {@link RuleStatement} cannot be null.
	 */
	final String RULE_STATEMENT_NULL = "ruleStatementNull";

	/**
	 * {@link RuleStatement} cannot be null.
	 * 
	 * @param rId The {@link RuleStatement} resource id.
	 */
	final String RULE_STATEMENT_NOT_FOUND = "ruleStatementNotFound";

	/**
	 * The {@link RuleStatement} resource id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULE_STATEMENT_ID_NULL = "ruleStatementIdNull";

	/**
	 * The {@link RuleStatement} resource id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String RULE_STATEMENT_ID_EMPTY = "ruleStatementIdEmpty";

	/**
	 * {@link SimpleStatement} not found.
	 * 
	 * @param rId The {@link SimpleStatement} resource id.
	 */
	final String SIMPLE_STATEMENT_NOT_FOUND = "simpleStatementNotFound";

	/**
	 * {@link SimpleStatement} resource id null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String SIMPLE_STATEMENT_ID_NULL = "simpleStatementIdNull";

	/**
	 * {@link SimpleStatement} resource id empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String SIMPLE_STATEMENT_ID_EMPTY = "simpleStatementIdEmpty";

	/**
	 * {@link SimpleStatement} was null.
	 */
	final String SIMPLE_STATEMENT_NULL = "simpleStatementNull";

	/* MEANING */

	/**
	 * The meaning of the representation is not a concept.
	 * 
	 * @param signifier The signifier of the representation
	 * @param rId The resource id of the representation
	 */
	final String MEANING_NOT_A_CONCEPT = "meaningNotAConcept";

	/**
	 * the {@link Meaning} was null
	 */
	final String MEANING_NULL = "meaningNull";
	/* TERM */

	/**
	 * Signifier length is more than 255 characters.
	 * 
	 * @param signifier The signifier that is too large
	 * @param signifierLength The length of the too large signifier
	 * @param rId The resource id of the term
	 */
	final String TERM_SIGNIFIER_TOO_LARGE = "termSignifierTooLarge";

	/**
	 * {@link Term} already exists.
	 * 
	 * @param signifier Term signifier
	 * @param vocabularyName Vocabulary name
	 * @param vocabularyRId Vocabulary resource id
	 * 
	 */
	final String TERM_ALREADY_EXISTS = "termAlreadyExists";

	/**
	 * {@link Term} with specified resource id not found.
	 * 
	 * @param rId The {@link Term} resource id
	 */
	final String TERM_NOT_FOUND_ID = "termNotFoundId";

	/**
	 * {@link Term} with specified signifier not found in the given vocabulary.
	 * 
	 * @param signifier The {@link Term} signifier
	 * @param vocabularyRId The {@link Vocabulary} resource id
	 */
	final String TERM_NOT_FOUND_SIGNIFIER = "termNotFoundSignifier";

	/**
	 * {@link Term} with specified signifier not found in the given vocabulary and its incorporated vocabularies.
	 * 
	 * @param signifier The {@link Term} signifier
	 * @param vocabularyRId The {@link Vocabulary} resource id
	 */
	final String TERM_NOT_FOUND_SIGNIFIER_INC_VOC = "termNotFoundSignifierInVoc";

	/**
	 * The resource id of the {@link Term} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String TERM_ID_NULL = "termIdNull";

	/**
	 * The resource id of the {@link Term} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String TERM_ID_EMPTY = "termIdEmpty";

	/**
	 * {@link Term} cannot be null.
	 */
	final String TERM_NULL = "termNull";

	/**
	 * {@link Term} signifier is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String TERM_SIGNIFIER_NULL = "termSignifierNull";

	/**
	 * {@link Term} signifier is empty
	 * 
	 * @param argumentName The name of the argument
	 */
	final String TERM_SIGNIFIER_EMPTY = "termSignifierEmpty";

	/**
	 * @{link Term} concept type is not valid
	 * 
	 * @param currentTermSignifier the Signifier of the term for which to change the concept type
	 * @param selectedTermSignifier the Signifier of the term selected to become the new concept type
	 */
	final String TERM_CONCEPTTYPE_INCOMPATIBLE = "termConceptTypeIncompatible";

	/* Object Type */

	/**
	 * {@link Term} object type cannot be null.
	 */
	final String OBJECT_TYPE_NULL = "objectTypeNull";

	/**
	 * The {@link ObjectType} cannot be found.
	 * 
	 * @param rId The object type resource id
	 */
	final String OBJECT_TYPE_NOT_FOUND = "objectTypeNotFound";

	/**
	 * The object for business term was not found.
	 */
	final String OBJECT_TYPE_NOT_FOUND_BUSINESS_TERM = "objectTypeNotFoundBusinessTerm";

	/**
	 * The resource id of the {@link ObjectType} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String OBJECT_TYPE_ID_NULL = "objectTypeIdNull";

	/**
	 * The resource id of the {@link ObjectType} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String OBJECT_TYPE_ID_EMPTY = "objectTypeIdEmpty";

	/* Concept Type */

	/**
	 * The resource id of the concept type label term is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONCEPT_TYPE_TERM_ID_NULL = "conceptTypeTermIdNull";

	/**
	 * The resource id of the concept type label term is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONCEPT_TYPE_TERM_ID_EMPTY = "conceptTypeTermIdEmpty";

	/* Characteristic Form */

	/**
	 * the characteristic of the {@link CharacteristicForm} is null
	 */
	final String CF_CHARACTERISTIC_NULL = "cfCharacteristicNull";

	/**
	 * {@link CharacteristicForm} already exists.
	 * 
	 * @param termSignifier Term signifier
	 * @param termRId Term resource id
	 * @param role Role
	 */
	final String CF_ALREADY_EXISTS = "cfAlreadyExists";

	/**
	 * {@link CharacteristicForm} already exists but only signifier of the term is checked (not its resource id).
	 * 
	 * @param termSignifier Term signifier
	 * @param role Role
	 */
	final String CF_ALREADY_EXISTS_SIGNIFIER = "cfAlreadyExistsSignifier";

	/**
	 * {@link CharacteristicForm} cannot be null.
	 */
	final String CF_NULL = "cfNull";

	/**
	 * The {@link CharacteristicForm} cannot be found.
	 * 
	 * @param rId The {@link CharacteristicForm} resource id.
	 */
	final String CF_NOT_FOUND = "cfNotFound";

	/**
	 * The resource id of the {@link CharacteristicForm} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_ID_NULL = "cfIdNull";

	/**
	 * The resource id of the {@link CharacteristicForm} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_ID_EMPTY = "cfIdEmpty";

	/**
	 * The term's signifier of the characteristic form is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_TERM_SIGNIFIER_NULL = "cfTermSignifierNull";

	/**
	 * The term's signifier of the characteristic form is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_TERM_SIGNIFIER_EMPTY = "cfTermSignifierEmpty";

	/**
	 * {@link CharacteristicForm} role name cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_ROLE_NULL = "cfRoleNull";

	/**
	 * {@link CharacteristicForm} role name cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CF_ROLE_EMPTY = "cfRoleEmpty";

	/* Binary Fact Type Form */

	/**
	 * {@link BinaryFactTypeForm} already exists.
	 * 
	 * @param headTermSignifier Head term signifier
	 * @param headTermRId Head term resource id
	 * @param role Role
	 * @param coRole coRole
	 * @param tailTermSignifier Tail term signifier
	 * @param tailTermRId Tail term resource id
	 * 
	 */
	final String BFTF_ALREADY_EXISTS = "bftfAlreadyExists";

	/**
	 * {@link BinaryFactTypeForm} already exists but only signifier of head and tail terms are checked and not their
	 * resource id.
	 * 
	 * @param headTermSignifier Head term signifier
	 * @param role Role
	 * @param coRole coRole
	 * @param tailTermSignifier Tail term signifier
	 * 
	 */
	final String BFTF_ALREADY_EXISTS_SIGNIFIER = "bftfAlreadyExistsSignifier";

	/**
	 * {@link BinaryFactTypeForm} cannot be null.
	 */
	final String BFTF_NULL = "bftfNull";

	/**
	 * The {@link BinaryFactTypeForm} cannot be found.
	 * 
	 * @param rId The {@link BinaryFactTypeForm} resource id.
	 */
	final String BFTF_NOT_FOUND = "bftfNotFound";

	/**
	 * The resource id of the {@link BinaryFactTypeForm} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_ID_NULL = "bftfIdNull";

	/**
	 * The resource id of the {@link BinaryFactTypeForm} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_ID_EMPTY = "bftfIdEmpty";

	/**
	 * The head term's resource id of the binary fact type form is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_HEAD_ID_NULL = "bftfHeadIdNull";

	/**
	 * The head term's resource id of the binary fact type form is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_HEAD_ID_EMPTY = "bftfHeadIdEmpty";

	/**
	 * The head term's signifier of the binary fact type form is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_HEAD_SIGNIFIER_NULL = "bftfHeadSignifierNull";

	/**
	 * The head term's signifier of the binary fact type form is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_HEAD_SIGNIFIER_EMPTY = "bftfHeadSignifierEmpty";

	/**
	 * {@link BinaryFactTypeForm} role cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_ROLE_NULL = "bftfRoleNull";

	/**
	 * {@link BinaryFactTypeForm} role cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_ROLE_EMPTY = "bftfRoleEmpty";

	/**
	 * {@link BinaryFactTypeForm} corole cannot be null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_COROLE_NULL = "bftfCoRoleNull";

	/**
	 * {@link BinaryFactTypeForm} corole cannot be empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_COROLE_EMPTY = "bftfCoRoleEmpty";

	/**
	 * The tail term's resource id of the binary fact type form is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_TAIL_ID_NULL = "bftfTailIdNull";

	/**
	 * The tail term's resource id of the binary fact type form is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_TAIL_ID_EMPTY = "bftfTailIdEmpty";

	/**
	 * The tail term's signifier of the binary fact type form is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_TAIL_SIGNIFIER_NULL = "bftfTailSignifierNull";

	/**
	 * The tail term's signifier of the binary fact type form is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String BFTF_TAIL_SIGNIFIER_EMPTY = "bftfTailSignifierEmpty";

	/* Categorization */

	/**
	 * The {@link Term} with signifier for {@link CategorizationType} already exists.
	 * 
	 * @param signifier Categorization type label term signifier
	 * @param vocabularyName Vocabulary name (cat. type label term vocabulary)
	 * @param vocabularyRId Vocabulary resource id (cat. type label term vocabulary)
	 */
	final String CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS = "catTypeTermAlreadyExists";

	/**
	 * The {@link Term} cannot be {@link Category} of itself.
	 */
	final String CONCEPT_CANNOT_BE_CATEGORIZATION_TYPE_FOR_SELF = "conceptCannotBeCategorizationTypeOfSelf";

	/**
	 * The {@link Term} for the categorization type is null.
	 */
	final String CATEGORIZATION_TYPE_TERM_NULL = "catTypeTermNull";

	/**
	 * The {@link Term} for the categorization type is empty.
	 */
	final String CATEGORIZATION_TYPE_TERM_EMPTY = "catTypeTermEmpty";

	/**
	 * The {@link CategorizationType} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_TYPE_NULL = "catTypeNull";

	/**
	 * The {@link CategorizationType} is Empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_TYPE_EMPTY = "catTypeEmpty";

	/**
	 * The {@link CategorizationType} cannot be found for the given resource id.
	 * 
	 * @param rId The {@link CategorizationType} resource id
	 */
	final String CATEGORIZATION_TYPE_NOT_FOUND = "catTypeNotFound";

	/**
	 * The categorization type name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_TYPE_NAME_NULL = "catTypeNameNull";

	/**
	 * The categorization type name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_TYPE_NAME_EMPTY = "catTypeNameEmpty";

	/**
	 * Categorization scheme concept is null.
	 */
	final String CATEGORIZATION_SCHEME_CONCEPT_NULL = "catSchemeConceptEmpty";

	/**
	 * Categorization scheme concept id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_CONCEPT_ID_NULL = "catSchemeConceptIdNull";

	/**
	 * Categorization scheme concept id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_CONCEPT_ID_EMPTY = "catSchemeConceptIdEmpty";
	/**
	 * Categorization scheme id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_ID_NULL = "catSchemeIdNull";
	/**
	 * Categorization scheme id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_ID_EMPTY = "catSchemeIdEmpty";
	/**
	 * Categorization scheme name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_NAME_NULL = "catSchemeNameNull";

	/**
	 * Categorization scheme name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORIZATION_SCHEME_NAME_EMPTY = "catSchemeNameEmpty";

	/**
	 * The {@link Term} with signifier for {@link Category} already exists.
	 */
	final String CATEGORY_TERM_ALREADY_EXISTS = "catTermAlreadyExists";

	/**
	 * The {@link Term} cannot be {@link Category} of itself.
	 */
	final String CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF = "categoryTermCannotBeCategoryOfSelf";

	/**
	 * The general {@link Concept} of {@link Category} must be taxonomical child of the {@link CategorizationType}'s
	 * classifying {@link Concept}.
	 */
	final String CATEGORY_GENERAL_CONCEPT_NOT_TAXONOMICAL_CHILD_OF_CAT_TYPE_FOR_CONCEPT = "catGeneralConceptIsNotTaxonomicalChildOfCatTypeForConcept";

	/**
	 * The {@link Category} must have the {@link Concept} being classified as general concept.
	 */
	final String CATEGORY_NO_GENERAL_CONCEPT = "catNoGeneralConcept";

	/**
	 * {@link Category} is null.
	 */
	final String CATEGORY_NULL = "catNull";
	/**
	 * {@link Category} is id is empty.
	 * 
	 * @param argumentName the name of the argument
	 */
	final String CATEGORY_ID_NULL = "catIdNull";

	/**
	 * {@link Category} is id is empty.
	 * @param argumentName The name of the argument
	 */
	final String CATEGORY_ID_EMPTY = "catIdEmpty";

	/**
	 * The {@link Category} name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORY_NAME_NULL = "catNameNull";

	/**
	 * The {@link Category} name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CATEGORY_NAME_EMPTY = "catNameEmpty";

	/**
	 * The {@link Category} cannot be found for the given resource id.
	 * 
	 * @param rId The {@link Category} resource id
	 */
	final String CATEGORY_NOT_FOUND = "catNotFound";

	/**
	 * Invalid operation/API call on a categorization representation.
	 */
	final String CATEGORIZATION_INVALID_OPERATION = "categorizationInvalidOperation";

	/* Concept */

	/**
	 * {@link Concept} is null.
	 */
	final String CONCEPT_NULL = "conceptNull";

	/**
	 * The {@link Concept} cannot be found.
	 * 
	 * @param rId The {@link Concept} resource id
	 */
	final String CONCEPT_NOT_FOUND = "conceptNotFound";

	/**
	 * The id of the {@link Concept} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONCEPT_ID_NULL = "conceptIdNull";

	/**
	 * The id of the concept is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONCEPT_ID_EMPTY = "conceptIdEmpty";

	/**
	 * The concept already exists in the taxonomy
	 * @param specializedRepresentationSignifier The signifier of the representation that will become the specialized
	 *            concept
	 */
	final String CONCEPT_ALREADY_IN_TAXONOMY = "conceptAlreadyInTaxonomy";

	/* Attribute */

	/**
	 * {@link StringAttribute} with same content already exists for the owner {@link Representation}.
	 * 
	 * @param attributeLabelSignifier attribute label signifier
	 * @param longExpression attribute long expression
	 */
	final String STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS = "stringAttWithSameContentExists";

	/**
	 * {@link SingleValueListAttribute} with same content already exists for the owner {@link Representation}.
	 * 
	 * @param attributeLabelSignifier attribute label signifier
	 * @param value attribute value
	 */
	final String SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS = "svlAttWithSameContentExists";

	/**
	 * {@link MultiValueListAttribute} with same content already exists for the owner {@link Representation}.
	 * 
	 * @param attributeLabelSignifier attribute label signifier
	 * @param values attribute values
	 */
	final String MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS = "mvlAttWithSameContentExists";

	/**
	 * {@link DateTimeAttribute} with same content already exists for the owner {@link Representation}.
	 * 
	 * @param attributeLabelSignifier attribute label signifier
	 * @param value {@link Calendar} value
	 */
	final String DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS = "dtAttWithSameContentExists";

	/**
	 * The {@link Attribute} is null.
	 * 
	 */
	final String ATTRIBUTE_NULL = "attributeNull";

	/**
	 * The resource id of the {@link Attribute} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_ID_NULL = "attributeIdNull";

	/**
	 * The resource id of the {@link Attribute} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_ID_EMPTY = "attributeIdEmpty";

	/**
	 * The {@link Attribute} cannot be found for the given resource id.
	 * 
	 * @param rId The resource id
	 */
	final String ATTRIBUTE_NOT_FOUND_ID = "attributeNotFoundId";

	/**
	 * The {@link StringAttribute} cannot be found for the given resource id.
	 * 
	 * @param rId The resource id
	 */
	final String STRING_ATTRIBUTE_NOT_FOUND_ID = "stringAttributeNotFoundId";

	/**
	 * The {@link SingleValueListAttribute} cannot be found for the given resource id.
	 * 
	 * @param rId The resource id
	 */
	final String SVL_ATTRIBUTE_NOT_FOUND_ID = "singleValueListAttributeNotFoundId";

	/**
	 * The {@link MultiValueListAttribute} cannot be found for the given resource id.
	 * 
	 * @param rId The resource id
	 */
	final String MVL_ATTRIBUTE_NOT_FOUND_ID = "multiValueListAttributeNotFoundId";

	/**
	 * The {@link DateTimeAttribute} cannot be found for the given resource id.
	 * 
	 * @param rId The resource id
	 */
	final String DATE_TIME_ATTRIBUTE_NOT_FOUND_ID = "dateTimeAttributeNotFoundId";

	/**
	 * {@link Attribute} label is null.
	 */
	final String ATTRIBUTE_LABEL_NULL = "attributeLabelNull";

	/* ATTRIBUTE_TYPE */

	/**
	 * The Term for the given resource id is not an Attribute Type Label Term
	 * 
	 * @param signifier The term signifier
	 * @param argumentName The name of the argument (resource id)
	 * @param labelRId The Attribute Type Label Term resource id
	 * @param labelVocabularyURI The Attribute Type Label Term Vocabulary URI
	 * @param attrTypeCollibraVocabularyName The name of the Collibra Attribute Type Vocabulary
	 * @param attrTypeCollibraVocabularyUri The URI of the Collibra Attribute Type Vocabulary
	 * @param definitionAttTypeSignifier The signifier of the definition meta attribute type
	 * @param descriptionAttTypeSignifier The signifier of the description meta attribute type
	 * @param exampleAttTypeSignifier The signifier of the example meta attribute type
	 * @param noteAttTypeSignifier The signifier of the note meta attribute type
	 */
	final String ATTRIBUTE_TYPE_NOT_ATTRIBUTE_TYPE_LABEL_FOR_RESOURCE_ID = "attTypeNotAttTypeLabelForRId";

	/**
	 * The signifier of the {@link Attribute} {@code Type} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_SIGNIFIER_NULL = "attTypeSignifierNull";

	/**
	 * The signifier of the {@link Attribute} {@code Type} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_SIGNIFIER_EMPTY = "attTypeSignifierEmpty";

	/**
	 * The signifier of the {@link Attribute} {@code Type} is empty.
	 * 
	 * @param signifier Attribute type's signifier
	 */
	final String ATTRIBUTE_TYPE_SIGNIFIER_EXISTS = "attTypeSignifierExists";

	/**
	 * The value list of the {@link Attribute} {@code Type} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_VALUE_LIST_NULL = "attTypeValueListNull";

	/**
	 * The resource id of the label term of the attribute type is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_ID_NULL = "attTypeIdNull";

	/**
	 * The resource id of the label term of the attribute type is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_ID_EMPTY = "attTypeIdEmpty";

	/**
	 * The value list of the {@link Attribute} {@code Type} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_VALUE_LIST_EMPTY = "attTypeValueListEmpty";

	/**
	 * The {@link Attribute} {@code Type} cannot be found for the given signifier.
	 * 
	 * @param signifier The signifier
	 */
	final String ATTRIBUTE_TYPE_NOT_FOUND_SIGNIFIER = "attTypeNotFoundSignifier";

	/**
	 * The kind of {@link Attribute} that can be defined using this {@link AttributeType} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_KIND_NULL = "attTypeKindNull";

	/**
	 * The kind of {@link Attribute} that can be defined using this {@link AttributeType} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String ATTRIBUTE_TYPE_KIND_EMPTY = "attTypeKindEmpty";

	/**
	 * The kind of {@link Attribute} that can be defined using this {@link AttributeType} is unknown.
	 * 
	 * @param kind The kind
	 */
	final String ATTRIBUTE_TYPE_KIND_UNKNOWN = "attKindUnknown";

	/**
	 * More than one argument is set.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String ATTRIBUTE_TYPE_UPDATE_MORE_1_ARG_SET = "attTypeUpdateMore1ArgSet";

	/**
	 * All arguments are null.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String ATTRIBUTE_TYPE_UPDATE_ALL_ARG_NULL_OR_EMPTY = "attTypeUpdateAllArgNullOrEmpty";

	/* Vocabulary */

	/**
	 * {@link Vocabulary} must be owned by a {@link Community}.
	 * 
	 * @param name Vocabulary name
	 * @param rId Vocabulary resource id
	 */
	final String VOCABULARY_NO_COMMUNITY_OWNS = "vocNoCommunity";

	/**
	 * {@link Vocabulary} already exists with same name and URI.
	 * 
	 * @param name Vocabulary name
	 * @param uri Vocabulary URI
	 */
	final String VOCABULARY_ALREADY_EXISTS = "vocAlreadyExists";

	/**
	 * {@link Vocabulary} already exists with same name.
	 * 
	 * @param name Vocabulary name
	 */
	final String VOCABULARY_WITH_NAME_ALREADY_EXISTS = "vocWithNameAlreadyExists";

	/**
	 * {@link Vocabulary} already exists with same URI.
	 * 
	 * @param uri Vocabulary URI
	 */
	final String VOCABULARY_WITH_URI_ALREADY_EXISTS = "vocWithUriAlreadyExists";

	/**
	 * {@link Vocabulary} already exists with same name and URI.
	 * 
	 * @param vocTypeVerbalise Vocabulary type verbalise
	 * @param vocTypeRId Vocabulary type resource id
	 * @param vocName Vocabulary's name
	 * @param vocRId Vocabulary's resource id
	 */
	final String VOCABULARY_TYPE_INCONSISTENT = "vocTypeInconsistent";

	/**
	 * {@link Vocabulary} cannot incorporate itself.
	 */
	final String VOCABULARY_CANNOT_INCORPORATE_ITSELF = "vocCannotIncorporateItself";

	/**
	 * {@link Vocabulary} id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_ID_NULL = "vocIdNull";

	/**
	 * {@link Vocabulary} id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_ID_EMPTY = "vocIdEmpty";

	/**
	 * {@link Vocabulary} name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_NAME_EMPTY = "vocNameEmpty";

	/**
	 * {@link Vocabulary} name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_NAME_NULL = "vocNameNull";

	/**
	 * {@link Vocabulary} is null.
	 */
	final String VOCABULARY_NULL = "vocNull";

	/**
	 * {@link Vocabulary} with the given resource id was not found.
	 * 
	 * @param rId The {@link Vocabulary} resource id
	 */
	final String VOCABULARY_NOT_FOUND_ID = "vocNotFoundId";

	/**
	 * {@link Vocabulary} with the given URI was not found.
	 * 
	 * @param uri The {@link Vocabulary} URI
	 */
	final String VOCABULARY_NOT_FOUND_URI = "vocNotFoundUri";

	/**
	 * {@link Vocabulary} with the given name was not found.
	 * 
	 * @param rId The {@link Vocabulary} name
	 */
	final String VOCABULARY_NOT_FOUND_NAME = "vocNotFoundName";

	/**
	 * {@link Vocabulary} URI is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_URI_NULL = "vocUriNull";

	/**
	 * {@link Vocabulary} URI is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_URI_EMPTY = "vocUriEmpty";

	/**
	 * The resource id of the vocabulary type is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_TYPE_ID_NULL = "vocTypeIdNull";

	/**
	 * The resource id of the vocabulary type is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String VOCABULARY_TYPE_ID_EMPTY = "vocTypeIdEmpty";

	/**
	 * The name and the URI of the vocabulary are both set.
	 * 
	 * @param nameArgumentName The name of the argument name
	 * @param uriArgumentName The name of the argument URI
	 */
	final String VOCABULARY_NAME_AND_URI_SET = "vocNameAndUriSet";

	/**
	 * The name and the URI of the vocabulary are both null.
	 * 
	 * @param nameArgumentName The name of the argument name
	 * @param uriArgumentName The name of the argument URI
	 */
	final String VOCABULARY_NAME_AND_URI_NULL = "vocNameAndUriNull";

	/**
	 * More than one argument is set.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String VOCABULARY_UPDATE_MORE_1_ARG_SET = "vocUpdateMore1ArgSet";

	/**
	 * All arguments are null.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String VOCABULARY_UPDATE_ALL_ARG_NULL_OR_EMPTY = "vocTypeUpdateAllArgNullOrEmpty";

	/* Status */

	/**
	 * The candidate status cannot be removed.
	 */
	final String CANNOT_REMOVE_CANDIDATE_STATUS = "cannotRemoveCandidateStatus";

	/**
	 * The term for the given resource id is not a label term of a status.
	 * @param signifier The term signifier
	 * @param argumentName The name of the argument (resource id)
	 * @param rId The status label resource id
	 * @param labelVocabularyUri The URI of the vocabulary of the term retrieved
	 * @param collibraStatusVocName The name of the Collibra status vocabulary
	 * @param collibraStatusVocUri The uri of the Collibra status vocabulary
	 */
	final String STATUS_NOT_STATUS_LABEL_FOR_RID = "statusNotStatusLabelForRId";

	/**
	 * The status term with the given signifier cannot be found.
	 * 
	 * @param signifier The status term signifier
	 */
	final String STATUS_NOT_FOUND_SIGNIFIER = "statusNotFoundSignifier";

	/**
	 * The resource id of the status label term is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String STATUS_TERM_ID_NULL = "statusTermIdNull";

	/**
	 * The resource id of the status label term is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String STATUS_TERM_ID_EMPTY = "statusTermIdEmpty";

	/**
	 * The signifier of the status label term is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String STATUS_TERM_SIGNIFIER_NULL = "statusTermSignifierNull";

	/**
	 * The signifier of the status label term is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String STATUS_TERM_SIGNIFIER_EMPTY = "statusTermSignifierEmpty";
	/* Representation */

	/**
	 * Representation locked, no modifications possible.
	 * 
	 * @param name The representation name (verbalise)
	 * @param rId The {@link Representation} resource id
	 */
	final String REPRESENTATION_LOCKED = "repLocked";

	/**
	 * Representation is null.
	 */
	final String REPRESENTATION_NULL = "repNull";

	/**
	 * The id of the {@link Representation} is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String REPRESENTATION_ID_NULL = "repIdNull";

	/**
	 * The id of the {@link Representation} is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String REPRESENTATION_ID_EMPTY = "repIdEmpty";

	/**
	 * The {@link Representation} cannot be found.
	 * 
	 * @param rId The {@link Representation} resource id
	 */
	final String REPRESENTATION_NOT_FOUND = "repNotFound";

	/**
	 * Representation kind is not known.
	 * 
	 * @param signifier The signifier of the representation
	 * @param rId The resource id of the representation
	 */
	final String REPRESENTATION_UKNOWN = "repUknown";

	/**
	 * The syonym {@link Representation} should be of same type as the original {@link Representation}.
	 */
	final String REPRESENTATION_SYNONYM_NOT_SAME_TYPE_AS_ORIGINAL = "repSynNotSameTypeAsOriginal";

	/**
	 * The {@link Term} being moved already exists in destination {@link Vocabulary}
	 */
	final String REPRESENTATION_MOVE_TERM_ALREADY_EXISTS_IN_DESTINATION_VOCABULARY = "repTermAlreadyExistsInDestVocabulary";

	/* Configuration Category */

	/**
	 * The name of the {@link ConfigurationCategory} is null;
	 */
	final String CONFCAT_NAME_NULL = "confCatNameNull";

	/**
	 * The name of the {@link ConfigurationCategory} is null;
	 */
	final String CONFCAT_NAME_EMPTY = "confCatNameEmpty";

	/**
	 * A {@link ConfigurationCategory} with the given name already exists;
	 * 
	 * @param catName The name of the category
	 */
	final String CONFCAT_ALREADY_EXISTS = "confCatNameAlreadyExists";

	/**
	 * A {@link ConfigurationCategory} with the given name already exists;
	 * 
	 */
	final String CONFCAT_SAVE_ERROR = "confCatSaveError";

	/**
	 * The {@link ConfigurationCategory} was not found;
	 * 
	 * @param catName The name of the category
	 */
	final String CONFCAT_NOT_FOUND = "confCatNotFound";

	/**
	 * The order of the given Term could not be changed.
	 * 
	 * @param catName The name of the category
	 * @param reprVerbalised The verbalisation of the representation for which to change the order
	 * @param position the position to change the order of the given term to
	 * 
	 */
	final String CONFCAT_COULD_NOT_CHANGE_ORDER = "confCatCouldNotChangeOrder";

	/**
	 * The term could not be added at the given position.
	 * 
	 * @param catName The name of the category
	 * @param reprVerbalised The verbalisation of the representation for which to change the order
	 * @param position the position to change the order of the given term to
	 * 
	 */
	final String CONFCAT_COULD_NOT_ADD_ON_POSITIONG = "confCatCouldNotAddOnPosition";

	/* Community */

	/**
	 * {@link Community} with URI already exists.
	 * 
	 * @param uri The URI of the community
	 */
	final String COMMUNITY_WITH_URI_ALREADY_EXISTS = "comWithUriAlreadyExists";

	/**
	 * {@link Community} with given name already exists.
	 * 
	 * @param name The name of the community
	 */
	final String COMMUNITY_WITH_NAME_ALREADY_EXISTS = "comWithNameAlreadyExists";

	/**
	 * {@link Community} with specified resource id cannot be found.
	 * 
	 * @param rId The {@link Community} resource id
	 */
	final String COMMUNITY_NOT_FOUND_ID = "comNotFoundId";

	/**
	 * {@link Community} with specified URI or name not found.
	 * 
	 * @param uri The {@link Community} uri
	 */
	final String COMMUNITY_NOT_FOUND_URI = "comNotFoundUri";

	/**
	 * {@link Community} with specified URI or name not found.
	 * 
	 * @param name The {@link Community} name
	 */
	final String COMMUNITY_NOT_FOUND_NAME = "comNotFoundName";

	/**
	 * {@link Community} is null.
	 */
	final String COMMUNITY_NULL = "comNull";

	/**
	 * {@link Community} resource id is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_ID_NULL = "comIdNull";

	/**
	 * {@link Community} resource id is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_ID_EMPTY = "comIdEmpty";

	/**
	 * {@link Community} name is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_NAME_NULL = "comNameNull";

	/**
	 * {@link Community} name is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_NAME_EMPTY = "comNameEmpty";

	/**
	 * {@link Community} URI is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_URI_NULL = "comUriNull";

	/**
	 * {@link Community} URI is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_URI_EMPTY = "comUriEmpty";

	/**
	 * {@link Community} language is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_LANGUAGE_NULL = "comLanguageNull";

	/**
	 * {@link Community} language is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String COMMUNITY_LANGUAGE_EMPTY = "comLanguageEmpty";

	/**
	 * The name and the URI of the community are both set.
	 * 
	 * @param nameArgumentName The name of the argument name
	 * @param uriArgumentName The name of the argument URI
	 */
	final String COMMUNITY_NAME_AND_URI_SET = "comNameAndUriSet";

	/**
	 * The name and the URI of the community are both null.
	 * 
	 * @param nameArgumentName The name of the argument name
	 * @param uriArgumentName The name of the argument URI
	 */
	final String COMMUNITY_NAME_AND_URI_NULL = "comNameAndUriNull";

	/**
	 * More than one argument is set.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String COMMUNITY_UPDATE_MORE_1_ARG_SET = "comUpdateMore1ArgSet";

	/**
	 * All arguments are null.
	 * 
	 * @param argumentNames The names of the arguments separated by a comma ","
	 */
	final String COMMUNITY_UPDATE_ALL_ARG_NULL_OR_EMPTY = "comTypeUpdateAllArgNullOrEmpty";

	/* Taxonomy */

	/**
	 * Circular taxonomy not allowed.
	 */
	final String CIRCULAR_TAXONOMY = "circularTaxonomy";

	/**
	 * Inconsistent taxonomy is not allowed.
	 */
	final String INCONSISTENT_TAXONOMY = "incosistentTaxonomy";

	/* RELATION */

	/**
	 * {@link Relation} is null
	 */
	final String RELATION_NULL = "relationNull";

	/**
	 * {@link Relation} id is null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String RELATION_ID_NULL = "relationIdNull";

	/**
	 * {@link Relation} id is empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String RELATION_ID_EMPTY = "relationIdEmpty";
	/**
	 * {@link Relation} source and target are not compatible.
	 * 
	 * @param signifier the signifier of the source or target of the relation
	 * @param typeTypeSignifier the signifier of the head or tail of the relation type
	 * @param typeSignifier the signifier of the type of the source or target of the relation
	 */
	final String RELATION_SRC_TGT_INCOMPATIBLE = "relationSrcTgtIncompatible";
	/**
	 * The {@link Relation} is not found.
	 * 
	 * @param rId The {@link Relation} resource id
	 */
	final String RELATION_NOT_FOUND = "relationNotFound";

	/**
	 * The relation type term with the given signifier cannot be found.
	 * 
	 * @param signifier The relation type term signifier
	 */
	final String RELATION_TYPE_TERM_NOT_FOUND_SIGNIFIER = "relationTypeTermNotFoundSignifier";

	/* Resource */

	/**
	 * {@link Resource} is null.
	 */
	final String RESOURCE_NULL = "resourceNull";

	/**
	 * Resource id is null;
	 * 
	 * @param argumentName The name of the argument (resource id)
	 */
	final String RESOURCE_ID_NULL = "resourceIdNull";

	/**
	 * Resource id is empty;
	 * 
	 * @param argumentName The name of the argument (resource id)
	 */
	final String RESOURCE_ID_EMPTY = "resourceIdEmpty";

	/**
	 * Resource locked, no modifications possible.
	 * 
	 * @param rId The {@link Resource} resource id
	 */
	final String RESOURCE_LOCKED = "resourceLocked";

	/* Other */

	/**
	 * File input stream / attachment passed to the method is null.
	 */
	final String FILE_INPUT_STREAM_NULL = "fileInputStreamNull";

	/* Workflow */

	/**
	 * Workflow process name (key) cannot be null.
	 */
	final String WF_PROCESS_KEY_NULL = "wfProcessKeyNull";

	/**
	 * Workflow process name (key) cannot be empty.
	 */
	final String WF_PROCESS_KEY_EMPTY = "wfProcessKeyEmpty";

	/**
	 * The {@link Task} not found.
	 */
	final String WF_TASK_NOT_FOUND = "wfTaskNotFound";

	/**
	 * The {@link Task} not found.
	 */
	final String WF_DEPLOY_ERROR = "wfDeployError";

	/**
	 * Cannot start the exclusive process as some other process instances are using the resource.
	 */
	final String WF_CANNOT_START_EXCLUSIVE_PROCESS = "wfCannotStartExclusiveProcess";

	/**
	 * Cannot start the process as another exclusive process instance is using the resource.
	 */
	final String WF_CANNOT_START_ANOTHER_EXCLUSIVE_PROCESS_RUNNING = "wfCannotStartProcessAnotherExclusiveProcessRunning";

	/**
	 * Cannot start the process as some other mutually exclusive process instances are using the resource.
	 */
	final String WF_CANNOT_START_MUTUALLY_EXCLUSIVE_PROCESS = "wfCannotStartMutuallyExclusiveProcess";

	/* Search */

	/**
	 * The search query is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String SEARCH_QUERY_NULL = "searchQueryNull";

	/**
	 * The search query is empty.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String SEARCH_QUERY_EMPTY = "searchQueryEmpty";

	/**
	 * The resource type is null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String SEARCH_RESOURCE_TYPE_NULL = "searchResourceTypeUnknown";

	/* I18n */

	/**
	 * The locale string for the internationalization is null.
	 * 
	 * @param argumentName The name of the argument.
	 */
	final String I18N_LOCALE_STRING_NULL = "i18nLocaleStringNull";

	/**
	 * The locale string for the internationalization is empty.
	 * 
	 * @param argumentName The name of the argument.
	 */
	final String I18N_LOCALE_STRING_EMPTY = "i18nLocaleStringEmpty";

	/**
	 * The key of the localized message is null.
	 * 
	 * @param argumentName The name of the argument.
	 */
	final String I18N_KEY_NULL = "i18nKeyNull";

	/**
	 * The key of the localized message is empty.
	 * 
	 * @param argumentName The name of the argument.
	 */
	final String I18N_KEY_EMPTY = "i18nKeyEmpty";

	/**
	 * The {@link HttpServletRequest} used to get the user localization is null.
	 * 
	 * @param argumentName The name of the argument
	 */
	final String I18N_HTTP_SERVLET_REQUEST_NULL = "i18nHttpServletRequestNull";

	/* Licensing */

	/**
	 * The license uploaded is invalid
	 */
	final String LICENSE_INVALID = "licenseInvalid";

	/**
	 * The license is expired.
	 */
	final String LICENSE_EXPIRED = "licenseExpired";

	/**
	 * While writing the license to disk something went wrong.
	 */
	final String LICENSE_UNWRITABLE = "licenseUnwritable";

	/**
	 * Could not read license.
	 */
	final String LICENSE_UNREADABLE = "licenseUnreadable";

	/**
	 * The system was unable to load the public key in order to verify the license.
	 */
	final String LICENSE_UNINITIALIZED_PUBLIC_KEY = "licenseUninitializedPublicKey";

	/**
	 * A certain field in the license was not present
	 * 
	 * @param fieldName The name of the field
	 */
	final String LICENSE_FIELD_NOT_PRESENT = "licenseFieldNotPresent";

	/* Configuration */

	/**
	 * The path is null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONFIGURATION_PATH_NULL = "configurationPathNull";

	/**
	 * The path is null
	 * 
	 * @param argumentName The name of the argument
	 */
	final String CONFIGURATION_PATH_EMPTY = "configurationPathEmpty";

	/**
	 * The passed value was null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String CONFIGURATION_VALUE_NULL = "configurationValueNull";

	/* Group */

	/**
	 * When the group was not found
	 * 
	 * @param the id of the group
	 */
	final String GROUP_NOT_FOUND = "groupNotFound";

	/**
	 * Group Id can't be null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String GROUP_ID_NULL = "groupIdNull";

	/**
	 * Group Id can't be null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String GROUP_ID_EMPTY = "groupIdEmpty";

	/**
	 * Group name can't be null
	 * 
	 * @param argumentName the name of the argument
	 */
	final String GROUP_NAME_NULL = "groupNameNull";

	/**
	 * Group name can't be empty
	 * 
	 * @param argumentName the name of the argument
	 */
	final String GROUP_NAME_EMPTY = "groupNameEmpty";

}
