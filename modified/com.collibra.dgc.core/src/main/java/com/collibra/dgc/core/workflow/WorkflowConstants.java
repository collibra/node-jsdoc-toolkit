package com.collibra.dgc.core.workflow;

public interface WorkflowConstants {
	// Variables
	final String VAR_REPRESENTATION_RESOURCE_ID = "representationResourceId";
	final String VAR_REPRESENTATION_TYPE = "representationType";
	final String VAR_DELEGATEE = "delegatee";
	final String VAR_DELEGATEE_ROLE = "delegateeRole";
	final String VAR_DELEGATEE_ENTITY_LEVEL = "delegateeEntityLevel";
	final String VAR_REVIEWER = "reviewer";
	final String VAR_LIST_REVIEWERS = "bsginternal_list_reviewers";
	final String VAR_DECISION = "accepted";
	final String VAR_DECISION_VALUE = "decisionValue";
	final String VAR_LIST_COMMUNITY_USERS = "bsginternal_list_community_users";
	final String VAR_COMMUNITY_USERS = "communityUsers";
	final String VAR_COMMUNITY_USER_ROLE = "communityUserRole";
	final String VAR_COMMUNITY_USER_ENTITY_LEVEL = "communityUserEntityLevel";
	final String VAR_REVIEWED_STAKEHOLDERS = "bsginternal_reviewed_stakeholders";
	final String VAR_LIST_DELEGATES = "bsginternal_list_delegates";
	final String VAR_PARENT_PROCESS_EXECUTION_ID = "bsginternal_parentProcessExectuionId";
	final String VAR_ENOUGH_REVIEWERS_REVIEWED = "enoughReviewed";
	final String VAR_COMMUNITY_REVIEWER = "communityReviewer";
	final String VAR_TARGET_STATE = "targetState";

	final String VAR_USER_RESPONSE_TIMEOUT = "userResponseTimeout";

	final String VAR_CLOSE = "close";

	final String VAR_OWNER = "owner";
	final String VAR_OWNER_EMAIL = "ownerEmail";
	final String VAR_SELECTED_TERM_RESOURCEIDS_LIST = "selectedTermResourceIds";
	final String VAR_USERS_EMAIL_LIST = "usersEmailList";
	final String VAR_RESPONSIBLE_LIST = "responsibleList";
	final String VAR_RESPONSIBLE_REPRESENTATION_PREFIX = "responsibleForRepresentation_";
	final String VAR_OTHERS_LIST = "othersList";
	final String VAR_OTHERS_REPRESENTATION_PREFIX = "otherForRepresentation_";
	final String VAR_TERMS_BEING_DECOMMISSIONED_MAIL_CONTENT = "termsBeingDecommissionedMailContent";
	final String VAR_DECOMMISSION_TERM_APPROVED = "decommissionTermApproved";
	final String VAR_PREVIOUS_STATE_PREFIX = "PreviousState_";
	final String VAR_COMMENT_ID_PREFIX = "Comment_ID_";
	final String VAR_DECOMMISSION_CONSOLIDATION_COMMENT_PREFIX = "decommission_consolidation_comment_";
	final String VAR_RESPONSIBLE_ROLES = "responsibleRoles";
	final String VAR_ACCOUNTABLE_ROLES = "accountableRoles";
	final String VAR_INFORMED_ROLES = "informedRoles";
	final String VAR_CONSULTED_ROLES = "consultedRoles";
	final String VAR_REASSIGN_ROLES = "reassignRoles";
	final String VAR_REASSIGN_TO_USER = "reassignToUser";
	final String VAR_ALL_USERS_FOR_REASSIGNMENT = "usersForReassignment";
	final String VAR_ALL_RESPONSIBLE_USERS_RESPONDED = "allResponsibleUsersResponded";

	final String VAR_ALL_REVIEWERS = "reviewers";

	// Message variables
	final String VAR_MESSAGE_TO = "toEmail";
	final String VAR_MESSAGE_FROM = "fromEmail";
	final String VAR_MESSAGE_REPRESENTATION_URL = "representationURL";
	final String VAR_MESSAGE_REPRESENTATION_NAME = "representationName";
	final String VAR_MESSAGE_SENDER = "sender";

	// Exceptions
	final String EXCEPTION_DELEGATEE_INJECTION_FAILED = "Failed_Delegate_Injection";
	final String EXCEPTION_STAKEHOLDERS_INJECTION_FAILED = "Failed_Stakeholder_Injection";
	final String EXCEPTION_REVIEWER_INJECTION_FAILED = "Failed_Reviewer_Injection";

	// Entity levels
	final String ENTITY_LEVEL_COMMUNITY = "Community";
	final String ENTITY_LEVEL_VOCABULARY = "Vocabulary";
	final String ENTITY_LEVEL_REPRESENTATION = "Representation";

	// Task management
	final String TASK_MANAGEMENT_PROCESS_KEY = "BSG_Task_Management";
	final String VAR_STATUSES = "statuses";
	final String VAR_SEVERITIES = "severities";
	final String VAR_RESOLUTIONS = "resolutions";
	final String VAR_NOTIFY_AFTER_DUE_DATE = "notifyAfterDueDate";
	final String VAR_ASSIGNEE = "assignee";
	final String VAR_TASK_TITLE = "title";
	final String VAR_TASK_STATUS = "status";
	final String VAR_TASK_RESOLUTION = "resolution";
	final String VAR_TASK_DUE_DATE = "dueDate";
	final String VAR_TASK_SEVERITY = "severity";
	final String VAR_TASK_DESCRIPTION = "description";
	final String VAR_TASK_REPORTER = "reporter";
	final String VAR_TASK_CLOSED = "closed";

	final String DUMMY_TASK_OWNER = "DUMMY TASK OWNER";

	final String DECISION_VALUE_APPROVED = "approved";
	final String DECISION_VALUE_REJECTED = "rejected";
	final String VAR_DECISION_REASON = "decisionReason";

	// Message Keys
	final String DECOMMISSION_COMMENT_KEY = "bsg.workflow.term.decommission";

	// Translation keys for email
	final String KEY_SUFFIX_ASSIGNMENT_EMAIL_CONTENT = ".assignment.email.content";
	final String KEY_SUFFIX_ASSIGNMENT_EMAIL_SUBJECT = ".assignment.email.subject";

	// Workflow customizations NS
	final String CUSTOMIZATIONS_NS = "http://www.collibra.com/bsg/workflow";
}
