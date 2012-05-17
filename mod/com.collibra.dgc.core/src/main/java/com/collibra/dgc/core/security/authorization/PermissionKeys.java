package com.collibra.dgc.core.security.authorization;

import java.util.HashMap;
import java.util.Map;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.Resource;

/**
 * {@link Resource} independent permission keys are defined here. Also there is a map defined statically to map these
 * keys to actual {@link Permissions} for each resource type.
 * @author amarnath
 * 
 */
public class PermissionKeys {
	public static final String VIEW_MEMBER = "view_member";
	public static final String ADD_MEMBER = "add_member";
	public static final String REMOVE_MEMBER = "remove_member";
	public static final String CHANGE_ROLE = "change_role";
	public static final String ADD_COMMENT = "add_comment";
	public static final String REMOVE_COMMENT = "remove_comment";
	public static final String VIEW_COMMENT = "view_comment";
	public static final String VIEW_ATTACHMENT = "view_attachment";
	public static final String ADD_ATTACHMENT = "add_attachment";
	public static final String REMOVE_ATTACHMENT = "remove_attachment";
	public static final String EDIT = "edit";

	public static final Map<String, String> COMMUNITY_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> VOCABULARY_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> TERM_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> BFTF_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> CF_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> NAME_PERMISSIONS_MAP = new HashMap<String, String>();
	public static final Map<String, String> RULESET_PERMISSIONS_MAP = new HashMap<String, String>();

	static {
		COMMUNITY_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.COMMUNITY_VIEW_MEMBER);
		COMMUNITY_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.COMMUNITY_ADD_MEMBER);
		COMMUNITY_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.COMMUNITY_REMOVE_MEMBER);
		COMMUNITY_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.COMMUNITY_CHANGE_ROLE);
		COMMUNITY_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.COMMUNITY_COMMENT_ADD);
		COMMUNITY_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.COMMUNITY_COMMENT_REMOVE);
		COMMUNITY_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.COMMUNITY_COMMENT_VIEW);
		COMMUNITY_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.COMMUNITY_ATTACHMENT_VIEW);
		COMMUNITY_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.COMMUNITY_ATTACHMENT_ADD);
		COMMUNITY_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.COMMUNITY_ATTACHMENT_REMOVE);
		COMMUNITY_PERMISSIONS_MAP.put(EDIT, Permissions.COMMUNITY_EDIT);

		VOCABULARY_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.VOCABULARY_VIEW_MEMBER);
		VOCABULARY_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.VOCABULARY_ADD_MEMBER);
		VOCABULARY_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.VOCABULARY_REMOVE_MEMBER);
		VOCABULARY_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.VOCABULARY_CHANGE_ROLE);
		VOCABULARY_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.VOCABULARY_COMMENT_ADD);
		VOCABULARY_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.VOCABULARY_COMMENT_REMOVE);
		VOCABULARY_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.VOCABULARY_COMMENT_VIEW);
		VOCABULARY_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.VOCABULARY_ATTACHMENT_VIEW);
		VOCABULARY_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.VOCABULARY_ATTACHMENT_ADD);
		VOCABULARY_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.VOCABULARY_ATTACHMENT_REMOVE);
		VOCABULARY_PERMISSIONS_MAP.put(EDIT, Permissions.VOCABULARY_EDIT);

		TERM_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.TERM_VIEW_MEMBER);
		TERM_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.TERM_ADD_MEMBER);
		TERM_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.TERM_REMOVE_MEMBER);
		TERM_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.TERM_CHANGE_ROLE);
		TERM_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.TERM_COMMENT_ADD);
		TERM_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.TERM_COMMENT_REMOVE);
		TERM_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.TERM_COMMENT_VIEW);
		TERM_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.TERM_ATTACHMENT_VIEW);
		TERM_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.TERM_ATTACHMENT_ADD);
		TERM_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.TERM_ATTACHMENT_REMOVE);
		TERM_PERMISSIONS_MAP.put(EDIT, Permissions.TERM_EDIT);

		BFTF_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.BFTF_VIEW_MEMBER);
		BFTF_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.BFTF_ADD_MEMBER);
		BFTF_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.BFTF_REMOVE_MEMBER);
		BFTF_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.BFTF_CHANGE_ROLE);
		BFTF_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.BFTF_COMMENT_ADD);
		BFTF_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.BFTF_COMMENT_REMOVE);
		BFTF_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.BFTF_COMMENT_VIEW);
		BFTF_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.BFTF_ATTACHMENT_VIEW);
		BFTF_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.BFTF_ATTACHMENT_ADD);
		BFTF_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.BFTF_ATTACHMENT_REMOVE);
		BFTF_PERMISSIONS_MAP.put(EDIT, Permissions.BFTF_EDIT);

		CF_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.CF_VIEW_MEMBER);
		CF_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.CF_ADD_MEMBER);
		CF_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.CF_REMOVE_MEMBER);
		CF_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.CF_CHANGE_ROLE);
		CF_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.CF_COMMENT_ADD);
		CF_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.CF_COMMENT_REMOVE);
		CF_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.CF_COMMENT_VIEW);
		CF_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.CF_ATTACHMENT_VIEW);
		CF_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.CF_ATTACHMENT_ADD);
		CF_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.CF_ATTACHMENT_REMOVE);
		CF_PERMISSIONS_MAP.put(EDIT, Permissions.CF_EDIT);

		RULESET_PERMISSIONS_MAP.put(VIEW_MEMBER, Permissions.RULESET_VIEW_MEMBER);
		RULESET_PERMISSIONS_MAP.put(ADD_MEMBER, Permissions.RULESET_ADD_MEMBER);
		RULESET_PERMISSIONS_MAP.put(REMOVE_MEMBER, Permissions.RULESET_REMOVE_MEMBER);
		RULESET_PERMISSIONS_MAP.put(CHANGE_ROLE, Permissions.RULESET_CHANGE_ROLE);
		RULESET_PERMISSIONS_MAP.put(ADD_COMMENT, Permissions.RULESET_COMMENT_ADD);
		RULESET_PERMISSIONS_MAP.put(REMOVE_COMMENT, Permissions.RULESET_COMMENT_REMOVE);
		RULESET_PERMISSIONS_MAP.put(VIEW_COMMENT, Permissions.RULESET_COMMENT_VIEW);
		RULESET_PERMISSIONS_MAP.put(VIEW_ATTACHMENT, Permissions.RULESET_ATTACHMENT_VIEW);
		RULESET_PERMISSIONS_MAP.put(ADD_ATTACHMENT, Permissions.RULESET_ATTACHMENT_ADD);
		RULESET_PERMISSIONS_MAP.put(REMOVE_ATTACHMENT, Permissions.RULESET_ATTACHMENT_REMOVE);
		RULESET_PERMISSIONS_MAP.put(EDIT, Permissions.RULESET_EDIT);
	}

	/**
	 * Get the {@link Permissions} string.
	 * @param resourceType The {@link Resource} type.
	 * @param key The {@link Resource} independent key.
	 * @return The {@link Permissions} string.
	 */
	public static String getPermission(String resourceType, String key) {
		if (Constants.RESOURCE_TYPE_TERM.equals(resourceType)) {
			return TERM_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_VOCABULARY.equals(resourceType)) {
			return VOCABULARY_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_COMMUNITY.equals(resourceType)) {
			return COMMUNITY_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_FACT_TYPE.equals(resourceType)) {
			return BFTF_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_CHARACTERISTIC_FORM.equals(resourceType)) {
			return CF_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_NAME.equals(resourceType)) {
			return NAME_PERMISSIONS_MAP.get(key);
		} else if (Constants.RESOURCE_TYPE_RULESET.equals(resourceType)) {
			return RULESET_PERMISSIONS_MAP.get(key);
		}

		throw new IllegalArgumentException("Invalid resource type '" + resourceType + "'");
	}
}
