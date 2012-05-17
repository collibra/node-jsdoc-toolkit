package com.collibra.dgc.core.security.authorization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.impl.ServiceUtility;
import com.collibra.dgc.core.util.Pair;

/**
 * Provides methods for checking authorization based on domain objects hierarchy.
 * 
 * @author amarnath
 * 
 */
@Service
public class AuthorizationHelper {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);
	// Attribute operations for authorization checks.
	public static final String OPERATION_ATTRIBUTE_ADD = "ATT_ADD";
	public static final String OPERATION_ATTRIBUTE_EDIT = "ATT_EDIT";
	public static final String OPERATION_ATTRIBUTE_REMOVE = "ATT_REMOVE";

	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private ObjectTypeDao objectTypeDao;

	private Map<Class<?>, Map<String, Map<String, Pair<String, List<String>>>>> permissionsMap = null;
	private Set<String> attributeTypes = null;

	public synchronized void initialize() {

		if (permissionsMap == null) {

			permissionsMap = new HashMap<Class<?>, Map<String, Map<String, Pair<String, List<String>>>>>();
			attributeTypes = new HashSet<String>();

			String metaDefinition = objectTypeDao.getMetaDefinition().getId().toString();
			String metaDescription = objectTypeDao.getMetaDescription().getId().toString();
			String metaExample = objectTypeDao.getMetaExample().getId().toString();
			String metaNote = objectTypeDao.getMetaNote().getId().toString();
			String metaCustom = MeaningConstants.META_ATTRIBUTE_TYPE_UUID.toString();
			attributeTypes.add(metaDefinition);
			attributeTypes.add(metaDescription);
			attributeTypes.add(metaExample);
			attributeTypes.add(metaNote);
			attributeTypes.add(metaCustom);

			// Term -> Definition -> ADD, REMOVE, EDIT
			add(Term.class, metaDefinition, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_ADD_ATTRIBUTE, Permissions.TERM_ADD_DEFINITION);
			add(Term.class, metaDefinition, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_REMOVE_ATTRIBUTE, Permissions.TERM_REMOVE_DEFINITION);
			add(Term.class, metaDefinition, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_EDIT_ATTRIBUTE, Permissions.TERM_EDIT_DEFINITION);

			// Term -> Description -> ADD, REMOVE, EDIT
			add(Term.class, metaDescription, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_ADD_ATTRIBUTE, Permissions.TERM_ADD_DESCRIPTION);
			add(Term.class, metaDescription, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_REMOVE_ATTRIBUTE, Permissions.TERM_REMOVE_DESCRIPTION);
			add(Term.class, metaDescription, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_EDIT_ATTRIBUTE, Permissions.TERM_EDIT_DESCRIPTION);

			// Term -> Example -> ADD, REMOVE, EDIT
			add(Term.class, metaExample, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_ADD_ATTRIBUTE, Permissions.TERM_ADD_EXAMPLE);
			add(Term.class, metaExample, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_REMOVE_ATTRIBUTE, Permissions.TERM_REMOVE_EXAMPLE);
			add(Term.class, metaExample, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_EDIT_ATTRIBUTE, Permissions.TERM_EDIT_EXAMPLE);

			// Term -> NOTE -> ADD, REMOVE, EDIT
			add(Term.class, metaNote, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_ADD_ATTRIBUTE, Permissions.TERM_ADD_NOTE);
			add(Term.class, metaNote, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_REMOVE_ATTRIBUTE, Permissions.TERM_REMOVE_NOTE);
			add(Term.class, metaNote, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_EDIT_ATTRIBUTE, Permissions.TERM_EDIT_NOTE);

			// Term -> Custom Attribute -> ADD, REMOVE, EDIT
			add(Term.class, metaCustom, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_ADD_ATTRIBUTE, Permissions.TERM_ADD_CUSTOM_ATTRIBUTE);
			add(Term.class, metaCustom, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_REMOVE_ATTRIBUTE, Permissions.TERM_REMOVE_CUSTOM_ATTRIBUTE);
			add(Term.class, metaCustom, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.TERM_EDIT_ATTRIBUTE, Permissions.TERM_EDIT_CUSTOM_ATTRIBUTE);

			// BFTF -> Definition -> ADD, REMOVE, EDIT
			add(BinaryFactTypeForm.class, metaDefinition, OPERATION_ATTRIBUTE_ADD,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_ADD_ATTRIBUTE,
					Permissions.BFTF_ADD_DEFINITION);
			add(BinaryFactTypeForm.class, metaDefinition, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_REMOVE_ATTRIBUTE,
					Permissions.BFTF_REMOVE_DEFINITION);
			add(BinaryFactTypeForm.class, metaDefinition, OPERATION_ATTRIBUTE_EDIT,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_EDIT_ATTRIBUTE,
					Permissions.BFTF_EDIT_DEFINITION);

			// BFTF -> Description -> ADD, REMOVE, EDIT
			add(BinaryFactTypeForm.class, metaDescription, OPERATION_ATTRIBUTE_ADD,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_ADD_ATTRIBUTE,
					Permissions.BFTF_ADD_DESCRIPTION);
			add(BinaryFactTypeForm.class, metaDescription, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_REMOVE_ATTRIBUTE,
					Permissions.BFTF_REMOVE_DESCRIPTION);
			add(BinaryFactTypeForm.class, metaDescription, OPERATION_ATTRIBUTE_EDIT,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_EDIT_ATTRIBUTE,
					Permissions.BFTF_EDIT_DESCRIPTION);

			// BFTF -> Example -> ADD, REMOVE, EDIT
			add(BinaryFactTypeForm.class, metaExample, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_ADD_ATTRIBUTE, Permissions.BFTF_ADD_EXAMPLE);
			add(BinaryFactTypeForm.class, metaExample, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_REMOVE_ATTRIBUTE,
					Permissions.BFTF_REMOVE_EXAMPLE);
			add(BinaryFactTypeForm.class, metaExample, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_EDIT_ATTRIBUTE, Permissions.BFTF_EDIT_EXAMPLE);

			// BFTF -> NOTE -> ADD, REMOVE, EDIT
			add(BinaryFactTypeForm.class, metaNote, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_ADD_ATTRIBUTE, Permissions.BFTF_ADD_NOTE);
			add(BinaryFactTypeForm.class, metaNote, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_REMOVE_ATTRIBUTE, Permissions.BFTF_REMOVE_NOTE);
			add(BinaryFactTypeForm.class, metaNote, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_EDIT_ATTRIBUTE, Permissions.BFTF_EDIT_NOTE);

			// BFTF -> Custom Attribute -> ADD, REMOVE, EDIT
			add(BinaryFactTypeForm.class, metaCustom, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_ADD_ATTRIBUTE, Permissions.BFTF_ADD_CUSTOM_ATTRIBUTE);
			add(BinaryFactTypeForm.class, metaCustom, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.BFTF_REMOVE_ATTRIBUTE,
					Permissions.BFTF_REMOVE_CUSTOM_ATTRIBUTE);
			add(BinaryFactTypeForm.class, metaCustom, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.BFTF_EDIT_ATTRIBUTE, Permissions.BFTF_EDIT_CUSTOM_ATTRIBUTE);

			// CF -> Definition -> ADD, REMOVE, EDIT
			add(CharacteristicForm.class, metaDefinition, OPERATION_ATTRIBUTE_ADD,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_ADD_ATTRIBUTE, Permissions.CF_ADD_DEFINITION);
			add(CharacteristicForm.class, metaDefinition, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_REMOVE_ATTRIBUTE,
					Permissions.CF_REMOVE_DEFINITION);
			add(CharacteristicForm.class, metaDefinition, OPERATION_ATTRIBUTE_EDIT,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_EDIT_ATTRIBUTE,
					Permissions.CF_EDIT_DEFINITION);

			// CF -> Description -> ADD, REMOVE, EDIT
			add(CharacteristicForm.class, metaDescription, OPERATION_ATTRIBUTE_ADD,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_ADD_ATTRIBUTE, Permissions.CF_ADD_DESCRIPTION);
			add(CharacteristicForm.class, metaDescription, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_REMOVE_ATTRIBUTE,
					Permissions.CF_REMOVE_DESCRIPTION);
			add(CharacteristicForm.class, metaDescription, OPERATION_ATTRIBUTE_EDIT,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_EDIT_ATTRIBUTE,
					Permissions.CF_EDIT_DESCRIPTION);

			// CF -> Example -> ADD, REMOVE, EDIT
			add(CharacteristicForm.class, metaExample, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_ADD_ATTRIBUTE, Permissions.CF_ADD_EXAMPLE);
			add(CharacteristicForm.class, metaExample, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_REMOVE_ATTRIBUTE,
					Permissions.CF_REMOVE_EXAMPLE);
			add(CharacteristicForm.class, metaExample, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_EDIT_ATTRIBUTE, Permissions.CF_EDIT_EXAMPLE);

			// CF -> NOTE -> ADD, REMOVE, EDIT
			add(CharacteristicForm.class, metaNote, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_ADD_ATTRIBUTE, Permissions.CF_ADD_NOTE);
			add(CharacteristicForm.class, metaNote, OPERATION_ATTRIBUTE_REMOVE, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_REMOVE_ATTRIBUTE, Permissions.CF_REMOVE_NOTE);
			add(CharacteristicForm.class, metaNote, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_EDIT_ATTRIBUTE, Permissions.CF_EDIT_NOTE);

			// CF -> Custom Attribute -> ADD, REMOVE, EDIT
			add(CharacteristicForm.class, metaCustom, OPERATION_ATTRIBUTE_ADD, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_ADD_ATTRIBUTE, Permissions.CF_ADD_CUSTOM_ATTRIBUTE);
			add(CharacteristicForm.class, metaCustom, OPERATION_ATTRIBUTE_REMOVE,
					DGCErrorCodes.ATTRIBUTE_NO_PERMISSION, Permissions.CF_REMOVE_ATTRIBUTE,
					Permissions.CF_REMOVE_CUSTOM_ATTRIBUTE);
			add(CharacteristicForm.class, metaCustom, OPERATION_ATTRIBUTE_EDIT, DGCErrorCodes.ATTRIBUTE_NO_PERMISSION,
					Permissions.CF_EDIT_ATTRIBUTE, Permissions.CF_EDIT_CUSTOM_ATTRIBUTE);

		}
	}

	private void add(Class<?> repClass, String attrType, String operation, String errorCode, String... permissions) {

		Map<String, Map<String, Pair<String, List<String>>>> attributeTypeToPermissions = permissionsMap.get(repClass);

		if (attributeTypeToPermissions == null) {
			attributeTypeToPermissions = new HashMap<String, Map<String, Pair<String, List<String>>>>();
			permissionsMap.put(repClass, attributeTypeToPermissions);
		}

		Map<String, Pair<String, List<String>>> commandToPermissions = attributeTypeToPermissions.get(attrType);

		if (commandToPermissions == null) {
			commandToPermissions = new HashMap<String, Pair<String, List<String>>>();
			attributeTypeToPermissions.put(attrType, commandToPermissions);
		}

		commandToPermissions.put(operation, new Pair<String, List<String>>(errorCode, Arrays.asList(permissions)));
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param community The {@link Community}
	 * @param permission The permission string
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorization(String userName, Community community, String permission, String errorCode,
			Object... otherParams) {

		if (!authorizationService.isPermitted(userName, community, permission)) {

			log.error(userName + " has no " + permission + " on community " + community.getName());

			throw new AuthorizationException(errorCode, userName, permission, community.verbalise(), community.getId(),
					otherParams);
		}
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param community The {@link Community}
	 * @param permissions The permissions array
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationAtleastForOne(String userName, Community community, String[] permissions,
			String errorCode, Object... otherParams) {

		for (String permission : permissions) {
			if (permission != null && authorizationService.isPermitted(userName, community, permission)) {
				return;
			}
		}

		log.error(userName + " should have at least one of these " + permissions + " on community "
				+ community.verbalise());

		throw new AuthorizationException(errorCode, userName, permissions, community.verbalise(), community.getId(),
				otherParams);
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param permissions The permissions array
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationAtleastForOne(String userName, String[] permissions, String errorCode,
			Object... otherParams) {

		for (String permission : permissions) {
			if (permission != null && authorizationService.isPermitted(userName, permission)) {
				return;
			}
		}

		log.error(userName + " should have at least one of these global " + permissions + ".");

		throw new AuthorizationException(errorCode, userName, permissions, otherParams);
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param vocabulary The {@link Vocabulary}
	 * @param permission The permission string
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorization(String userName, Vocabulary vocabulary, String permission, String errorCode,
			Object... otherParams) {

		if (!authorizationService.isPermitted(userName, vocabulary, permission)) {

			log.error(userName + " has no " + permission + " on vocabulary " + vocabulary.getName());

			throw new AuthorizationException(errorCode, userName, permission, vocabulary.verbalise(),
					vocabulary.getId(), otherParams);
		}
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param vocabulary The {@link Vocabulary}
	 * @param permissions The permissions array
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationAtleastForOne(String userName, Vocabulary vocabulary, String[] permissions,
			String errorCode, Object... otherParams) {

		for (String permission : permissions) {
			if (permission != null && authorizationService.isPermitted(userName, vocabulary, permission)) {
				return;
			}
		}

		log.error(userName + " should have at least one of these " + permissions + " on vocabulary "
				+ vocabulary.verbalise());

		throw new AuthorizationException(errorCode, userName, permissions, vocabulary.verbalise(), vocabulary.getId(),
				otherParams);
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param representation The {@link representation}
	 * @param permission The permission string
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorization(String userName, Representation representation, String permission, String errorCode,
			Object... otherParams) {

		if (!authorizationService.isPermitted(userName, representation, permission)) {

			log.error(userName + " has no " + permission + " on representation " + representation.verbalise());

			throw new AuthorizationException(errorCode, userName, permission, representation.verbalise(),
					representation.getId(), otherParams);
		}
	}

	/**
	 * Performs authorization check.
	 * 
	 * @param user The user.
	 * @param representation {@link Representation}.
	 * @param permissionKey The permission key for {@link Permission}.
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationForPermissionKey(String user, Representation representation, String permissionKey,
			String errorCode, Object... otherParams) {

		Representation deproxedRepresentation = ServiceUtility.deproxy(representation, Representation.class);

		if (deproxedRepresentation instanceof Term) {

			checkAuthorization(user, deproxedRepresentation, PermissionKeys.TERM_PERMISSIONS_MAP.get(permissionKey),
					errorCode, otherParams);

		} else if (deproxedRepresentation instanceof BinaryFactTypeForm) {

			checkAuthorization(user, deproxedRepresentation, PermissionKeys.BFTF_PERMISSIONS_MAP.get(permissionKey),
					errorCode, otherParams);

		} else if (deproxedRepresentation instanceof CharacteristicForm) {

			checkAuthorization(user, deproxedRepresentation, PermissionKeys.CF_PERMISSIONS_MAP.get(permissionKey),
					errorCode, otherParams);

		} else {

			throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN, representation.verbalise(),
					representation.getId());
		}
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param representation The {@link representation}
	 * @param permissions The permissions array
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationAtleastForOne(String userName, Representation representation, String[] permissions,
			String errorCode, Object... otherParams) {

		for (String permission : permissions) {
			if (permission != null && authorizationService.isPermitted(userName, representation, permission)) {
				return;
			}
		}

		log.error(userName + " should have at least one of these " + permissions + " on representation "
				+ representation.verbalise());

		throw new AuthorizationException(errorCode, userName, permissions, representation.verbalise(),
				representation.getId(), otherParams);
	}

	/**
	 * Performs authorization check.
	 * 
	 * @param user The user.
	 * @param attribute {@link Attribute}.
	 * @param operation Operation type (add/edit/remove)
	 */
	public void checkAuthorization(String userName, Attribute attribute, String operation) {

		initialize();

		Concept meaning = attribute.getLabel().getMeaning();
		Set<Meaning> done = new HashSet<Meaning>();

		while (meaning != null && !attributeTypes.contains(meaning.getId().toString()) && !done.contains(meaning)) {
			done.add(meaning);
			meaning = meaning.getType();
		}

		if (meaning == null || !attributeTypes.contains(meaning.getId().toString()))
			return;

		String attType = meaning.getId().toString();

		Representation owner = ServiceUtility.deproxy(attribute.getOwner(), Representation.class);

		Class<?> repClass = null;
		if (owner instanceof Term) {
			repClass = Term.class;
		} else if (owner instanceof BinaryFactTypeForm) {
			repClass = BinaryFactTypeForm.class;
		} else if (owner instanceof CharacteristicForm) {
			repClass = CharacteristicForm.class;
		}

		Pair<String, List<String>> pair = permissionsMap.get(repClass).get(attType).get(operation);
		List<String> permissions = pair.second;
		String errorCode = pair.first;

		if (permissions == null || permissions.size() == 0) {

			log.error(userName + " is not allowed to perform the operation on " + attribute.verbalise());

			throw new AuthorizationException(DGCErrorCodes.REPRESENTATION_NO_PERMISSIONS_DEFINED, userName, "null",
					attribute.getLabel().verbalise(), attribute.getLabel().getId(), owner.verbalise(), owner.getId());
		}

		for (String permission : permissions) {
			if (authorizationService.isPermitted(userName, attribute.getOwner(), permission)) {
				return;
			}
		}

		log.error(userName + " has no permission to perform this operation on " + attribute.verbalise());
		throw new AuthorizationException(errorCode, userName, permissions.toArray(new String[permissions.size()]),
				attribute.getLabel().verbalise(), attribute.getLabel().getId(), owner.verbalise(), owner.getId());
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param resource The {@link Resource}
	 * @param permission The permission string
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorization(String userName, Resource resource, String permission, String errorCode,
			Object... otherParams) {

		if (!authorizationService.isPermitted(userName, resource, permission)) {

			log.error(userName + " has no " + permission + " on the resource " + resource.toString());

			throw new AuthorizationException(errorCode, userName, permission, resource.toString(), resource.getId(),
					otherParams);
		}
	}

	/**
	 * Perform global authorization check.
	 * 
	 * @param userName The user name
	 * @param permission The permission string
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorization(String userName, String permission, String errorCode, Object... otherParams) {

		if (!authorizationService.isPermitted(userName, permission)) {

			log.error(userName + " has no global permission " + permission + ".");

			throw new AuthorizationException(errorCode, userName, permission, otherParams);
		}
	}

	/**
	 * Perform authorization check.
	 * 
	 * @param userName The user name
	 * @param resource The {@link Resource}
	 * @param permissions The permissions array
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param otherParams Other parameters needed for the translation
	 */
	public void checkAuthorizationAtleastForOne(String userName, Resource resource, String[] permissions,
			String errorCode, Object... otherParams) {

		for (String permission : permissions) {
			if (authorizationService.isPermitted(userName, resource, permission)) {
				return;
			}
		}

		log.error(userName + " should have at least one of these " + permissions + " on resource "
				+ resource.toString());
		throw new AuthorizationException(errorCode, userName, permissions, resource.toString(), resource.getId(),
				otherParams);
	}

	protected String getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() == null) {
			return Constants.GUEST_USER;
		}
		return currentUser.getPrincipal().toString();
	}
}
