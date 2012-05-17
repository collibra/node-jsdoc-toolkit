package com.collibra.dgc.core.security.authorization;

/**
 * 
 * @author amarnath
 * 
 */
public interface Permissions {
	final String ADMIN = "admin";
	final String CUSTOMIZE_CONFIG = "customizeconfig";

	// Community permissions
	final String COMMUNITY_CREATE = "115";
	final String COMMUNITY_REMOVE = "16";
	final String COMMUNITY_EDIT = "14";

	final String COMMUNITY_VIEW_MEMBER = "1141";
	final String COMMUNITY_ADD_MEMBER = "1142";
	final String COMMUNITY_REMOVE_MEMBER = "1143";
	final String COMMUNITY_CHANGE_ROLE = "1144";

	final String COMMUNITY_ATTACHMENT_VIEW = "1121";
	final String COMMUNITY_ATTACHMENT_ADD = "1122";
	final String COMMUNITY_ATTACHMENT_REMOVE = "1123";

	final String COMMUNITY_COMMENT_VIEW = "1131";
	final String COMMUNITY_COMMENT_ADD = "1132";
	final String COMMUNITY_COMMENT_REMOVE = "1133";

	// Vocabulary permissions
	final String COMMUNITY_ADD_VOCABULARY = "182";
	final String VOCABULARY_REMOVE = "183";
	final String VOCABULARY_EDIT = "184";

	final String VOCABULARY_ADD_FACT_TYPE = "1862";
	final String VOCABULARY_ADD_CHARACTERISTIC_FORM = "1872";
	final String VOCABULARY_ADD_TERM = "1852";

	final String VOCABULARY_VIEW_MEMBER = "18131";
	final String VOCABULARY_ADD_MEMBER = "18132";
	final String VOCABULARY_REMOVE_MEMBER = "18133";
	final String VOCABULARY_CHANGE_ROLE = "18134";

	final String VOCABULARY_ATTACHMENT_VIEW = "1891";
	final String VOCABULARY_ATTACHMENT_ADD = "1892";
	final String VOCABULARY_ATTACHMENT_REMOVE = "1893";

	final String VOCABULARY_COMMENT_VIEW = "18101";
	final String VOCABULARY_COMMENT_ADD = "18102";
	final String VOCABULARY_COMMENT_REMOVE = "18103";

	final String VOCABULARY_INCORPORATE = "1811";

	final String VOCABULARY_ADD_CAT = "18171";
	final String VOCABULARY_REMOVE_CAT = "18172";

	// Representation permissions
	final String REPRESENTATION_CONCEPT_TYPE_UPDATE = "rep_concept_type_update";

	// Term permissions
	final String TERM_REMOVE = "1853";
	final String TERM_EDIT = "1854";

	final String TERM_CREATE_SYNONYM = "185162";
	final String TERM_REMOVE_SYNONYM = "185163";

	final String TERM_STATUS_MODIFY = "185142";

	final String TERM_ADD_ATTRIBUTE = "185152";
	final String TERM_EDIT_ATTRIBUTE = "185153";
	final String TERM_REMOVE_ATTRIBUTE = "185154";
	final String TERM_ADD_DEFINITION = "1851532";
	final String TERM_EDIT_DEFINITION = "1851533";
	final String TERM_REMOVE_DEFINITION = "1851534";
	final String TERM_ADD_DESCRIPTION = "1851542";
	final String TERM_EDIT_DESCRIPTION = "1851543";
	final String TERM_REMOVE_DESCRIPTION = "1851544";
	final String TERM_ADD_EXAMPLE = "1851552";
	final String TERM_EDIT_EXAMPLE = "1851553";
	final String TERM_REMOVE_EXAMPLE = "1851554";
	final String TERM_ADD_NOTE = "1851562";
	final String TERM_EDIT_NOTE = "1851563";
	final String TERM_REMOVE_NOTE = "1851564";
	final String TERM_ADD_CUSTOM_ATTRIBUTE = "1851572";
	final String TERM_EDIT_CUSTOM_ATTRIBUTE = "1851573";
	final String TERM_REMOVE_CUSTOM_ATTRIBUTE = "1851574";

	final String TERM_VIEW_MEMBER = "185111";
	final String TERM_ADD_MEMBER = "185112";
	final String TERM_REMOVE_MEMBER = "185113";
	final String TERM_CHANGE_ROLE = "185114";

	final String TERM_ATTACHMENT_VIEW = "18581";
	final String TERM_ATTACHMENT_ADD = "18582";
	final String TERM_ATTACHMENT_REMOVE = "18583";

	final String TERM_COMMENT_VIEW = "18591";
	final String TERM_COMMENT_ADD = "18592";
	final String TERM_COMMENT_REMOVE = "18593";

	final String TERM_ASSIGN_CAT = "185171";
	final String TERM_REMOVE_ASSIGNED_CAT = "185172";

	final String TERM_LOCK = "18518";

	// Binary fact type form permissions
	final String BFTF_REMOVE = "1863";
	final String BFTF_EDIT = "1864";

	final String BFTF_CREATE_SYNONYM = "186162";
	final String BFTF_REMOVE_SYNONYM = "186163";

	final String BFTF_STATUS_MODIFY = "186142";

	final String BFTF_ADD_ATTRIBUTE = "186152";
	final String BFTF_EDIT_ATTRIBUTE = "186153";
	final String BFTF_REMOVE_ATTRIBUTE = "186154";
	final String BFTF_ADD_DEFINITION = "1861532";
	final String BFTF_REMOVE_DEFINITION = "1861534";
	final String BFTF_EDIT_DEFINITION = "1861533";
	final String BFTF_ADD_DESCRIPTION = "1851642";
	final String BFTF_REMOVE_DESCRIPTION = "1851644";
	final String BFTF_EDIT_DESCRIPTION = "1851643";
	final String BFTF_ADD_EXAMPLE = "1861552";
	final String BFTF_REMOVE_EXAMPLE = "1861554";
	final String BFTF_EDIT_EXAMPLE = "1861553";
	final String BFTF_ADD_NOTE = "1861562";
	final String BFTF_REMOVE_NOTE = "1861564";
	final String BFTF_EDIT_NOTE = "1861563";
	final String BFTF_ADD_CUSTOM_ATTRIBUTE = "1861572";
	final String BFTF_REMOVE_CUSTOM_ATTRIBUTE = "1861574";
	final String BFTF_EDIT_CUSTOM_ATTRIBUTE = "1861573";

	final String BFTF_VIEW_MEMBER = "186111";
	final String BFTF_ADD_MEMBER = "186112";
	final String BFTF_REMOVE_MEMBER = "186113";
	final String BFTF_CHANGE_ROLE = "186114";

	final String BFTF_ATTACHMENT_VIEW = "18681";
	final String BFTF_ATTACHMENT_ADD = "18682";
	final String BFTF_ATTACHMENT_REMOVE = "18683";

	final String BFTF_COMMENT_VIEW = "18691";
	final String BFTF_COMMENT_ADD = "18692";
	final String BFTF_COMMENT_REMOVE = "18693";

	final String BFTF_LOCK = "18617";

	// Characteristic form permissions
	final String CF_REMOVE = "1873";
	final String CF_EDIT = "1874";

	final String CF_CREATE_SYNONYM = "187162";
	final String CF_REMOVE_SYNONYM = "187163";

	final String CF_STATUS_MODIFY = "187142";

	final String CF_ADD_ATTRIBUTE = "187152";
	final String CF_EDIT_ATTRIBUTE = "187153";
	final String CF_REMOVE_ATTRIBUTE = "187154";
	final String CF_ADD_DEFINITION = "1871532";
	final String CF_REMOVE_DEFINITION = "1871534";
	final String CF_EDIT_DEFINITION = "1871533";
	final String CF_ADD_DESCRIPTION = "1871542";
	final String CF_REMOVE_DESCRIPTION = "1871544";
	final String CF_EDIT_DESCRIPTION = "1871543";
	final String CF_ADD_EXAMPLE = "1871552";
	final String CF_REMOVE_EXAMPLE = "1871554";
	final String CF_EDIT_EXAMPLE = "1871553";
	final String CF_ADD_NOTE = "1871562";
	final String CF_REMOVE_NOTE = "1871564";
	final String CF_EDIT_NOTE = "1871563";
	final String CF_ADD_CUSTOM_ATTRIBUTE = "1871572";
	final String CF_REMOVE_CUSTOM_ATTRIBUTE = "1871574";
	final String CF_EDIT_CUSTOM_ATTRIBUTE = "1871573";

	final String CF_VIEW_MEMBER = "187111";
	final String CF_ADD_MEMBER = "187112";
	final String CF_REMOVE_MEMBER = "187113";
	final String CF_CHANGE_ROLE = "187114";

	final String CF_ATTACHMENT_VIEW = "18781";
	final String CF_ATTACHMENT_ADD = "18782";
	final String CF_ATTACHMENT_REMOVE = "18783";

	final String CF_COMMENT_VIEW = "18791";
	final String CF_COMMENT_ADD = "18792";
	final String CF_COMMENT_REMOVE = "18793";

	final String CF_LOCK = "18717";

	// Rule set permissions
	final String RULESET_VIEW_MEMBER = "1961";
	final String RULESET_ADD_MEMBER = "1962";
	final String RULESET_REMOVE_MEMBER = "1963";
	final String RULESET_CHANGE_ROLE = "1964";

	final String RULESET_ADD = "192";
	final String RULESET_REMOVE = "193";
	final String RULESET_EDIT = "194";

	final String RULESET_ATTACHMENT_VIEW = "1971";
	final String RULESET_ATTACHMENT_ADD = "1972";
	final String RULESET_ATTACHMENT_REMOVE = "1973";

	final String RULESET_COMMENT_VIEW = "1981";
	final String RULESET_COMMENT_ADD = "1982";
	final String RULESET_COMMENT_REMOVE = "1983";

	// Rule statement permissions

	final String RULE_STATEMENT_ADD = "1952";
	final String RULE_STATEMENT_REMOVE = "1953";
	final String RULE_STATEMENT_EDIT = "1954";

	final String RULE_STATEMENT_VIEW_MEMBER = "19551";
	final String RULE_STATEMENT_ADD_MEMBER = "19552";
	final String RULE_STATEMENT_REMOVE_MEMBER = "19553";
	final String RULE_STATEMENT_CHANGE_ROLE = "19554";

	final String RULE_STATEMENT_ATTACHMENT_VIEW = "19561";
	final String RULE_STATEMENT_ATTACHMENT_ADD = "19562";
	final String RULE_STATEMENT_ATTACHMENT_REMOVE = "19563";

	final String RULE_STATEMENT_COMMENT_VIEW = "19571";
	final String RULE_STATEMENT_COMMENT_ADD = "19572";
	final String RULE_STATEMENT_COMMENT_REMOVE = "19573";

	// Workflow permissions
	final String WF_DEPLOY = "WF_DEPLOY";
	final String WF_UNDEPLOY = "WF_UNDEPLOY";

	final String WF_PROCESS_VIEW = "WF_PROCESS_VIEW";
	final String WF_PROCESS_START = "WF_PROCESS_START";
	final String WF_PROCESS_STOP = "WF_PROCESS_STOP";

	final String WF_ARTICULATION_VIEW = "WF_ARTICULATION_VIEW";
	final String WF_ARTICULATION_START = "WF_ARTICULATION_START";
	final String WF_ARTICULATION_STOP = "WF_ARTICULATION_STOP";

	final String WF_DECOMMISSION_VIEW = "WF_DECOMMISSION_VIEW";
	final String WF_DECOMMISSION_START = "WF_DECOMMISSION_START";
	final String WF_DECOMMISSION_STOP = "WF_DECOMMISSION_STOP";

	final String WF_FEEDBACK_VIEW = "WF_FEEDBACK_VIEW";
	final String WF_FEEDBACK_START = "WF_FEEDBACK_START";
	final String WF_FEEDBACK_STOP = "WF_FEEDBACK_STOP";

	final String WF_INTAKE_VIEW = "WF_INTAKE_VIEW";
	final String WF_INTAKE_START = "WF_INTAKE_START";
	final String WF_INTAKE_STOP = "WF_INTAKE_STOP";

	final String WF_GENERIC_TASKS_VIEW = "WF_GTP_VIEW";
	final String WF_GENERIC_TASKS_START = "WF_GTP_START";
	final String WF_GENERIC_TASKS_STOP = "WF_GTP_STOP";

	final String WF_REVIEW_VIEW = "WF_REVIEW_VIEW";
	final String WF_REVIEW_START = "WF_REVIEW_START";
	final String WF_REVIEW_STOP = "WF_REVIEW_STOP";

	final String WF_APPROVAL_VIEW = "WF_APPROVAL_VIEW";
	final String WF_APPROVAL_START = "WF_APPROVAL_START";
	final String WF_APPROVAL_STOP = "WF_APPROVAL_STOP";
}
