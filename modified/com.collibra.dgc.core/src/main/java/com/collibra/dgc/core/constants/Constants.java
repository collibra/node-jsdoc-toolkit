package com.collibra.dgc.core.constants;

/**
 * To add all the constants used in the project
 * 
 * @author amarnath
 * 
 */
public interface Constants {

	final String PREFERRED_NAME = "preferredName";
	final String NAME = "name";
	final String VOCABULARY_GUID = "vocabularyGuid";

	final String DEFINITION = "definition";
	final String EXAMPLE = "example";
	final String NOTE = "content";
	final String QUALITY = "content";
	final String DESCRIPTION = "content";
	final String AUTHOR = "author";
	final String DATE = "date";

	final String ENGLISH = "English";

	final Integer INITIAL_VERSION = 1;

	/**
	 * the page that contains the license object (LicenseClass instance)
	 */
	final String LICENSE_PAGE_NAME = "License.LicenseData";

	/**
	 * XWiki Class that holds the license
	 */
	final String LICENSE_CLASS_NAME = "License.LicenseClass";

	/**
	 * property in the LicenseCLass that holds the license information
	 */
	final String LICENSE_CONTENT = "licenseContent";

	// Glossary Error handling //
	/**
	 * location of the i18n messages for the end user in the XWiki execution context
	 */
	final String SESSION_MESSAGES_MAP = "BSG_messages_map";

	/**
	 * currrent conversation Id stored on the request
	 */
	final String REQUEST_CONVERSATION_ID = "conversationId";

	/**
	 * performs license validation after n requests
	 */
	final String REQUEST_COUNTER = "requestCounter";

	/**
	 * Error messages stored in the request.
	 */
	final String REQUEST_MESSAGES = "BSG_messages";

	/**
	 * Conversation Id Parameter
	 */
	final String CONVERSATION_ID_PARAMETER = "cid";

	final String VELOCITY_CTX_MESSAGES_KEY = "bsgMessages";

	/**
	 * key for messages without a key. Will be displayed on top of the page (global messages)
	 */
	final String GLOBAL_MESSAGE_KEY = "global";

	/**
	 * Application scope messages (used for invalid license)
	 */
	final String APPLICATION_MESSAGES = "BSG_messages";

	// License

	/**
	 * i18n Key used for an invalid license, displayed on all pages using the globalMessages macro
	 */
	final String INVALID_LICENSE_MESSAGE = "bsg.license.invalid.message";

	/**
	 * This license status is set on the application scope when the license is not valid. IF the license is valid the
	 * value will not be present.
	 */
	final String LICENSE_STATUS = "licenseStatus";

	/**
	 * value for LICENSE_STATUS (or not set)
	 */
	final String VALID_LICENSE = "valid";
	/**
	 * value for LICENSE_STATUS
	 */
	final String INVALID_LICENSE = "invalid";

	// Rule Set Defaults

	final String DEFAULT_RULE_SET_NAME = "General Rule Set";

	// Glossary Error handling //

	final String KEY = "key";
	final String VALUE = "value";
	final String RIGHTS_PANEL = "rightPanels";

	final String DISABLE_SECURITY = "com.collibra.security.disabled";
	final String ADMIN_USER = "Admin";
	final String GUEST_USER = "Guest";
	final String LANGUAGE_ISO = "iso_639_3";

	/* Notification Object Class constants */
	final String NOTIFICATION_OBJECT_CLASS = "ConfigCode.NotificationObjectClass";
	final String NOC_NOTIFICATION_CONTENT = "notificationContent";
	final String NOC_DOCUMENT = "documentName";
	final String PAGE = "page";

	/* Notification configuration Class constants */
	final String NOTIFICATION_CONFIGURATION_CLASS = "ConfigCode.NotificationConfigurationClass";
	final String NCC_NOTIFICATION_ENABLED = "notificationEnabled";
	final String NCC_NOTIFICATION_JOB = "notificationJob";

	/*
	 * The custom attribute name for keeping preferred name for the concept when exported to pattern.
	 */
	final String PREFERRED_NAME_FOR_EXPORT = "Technical Term";

	/* */
	final String MaR_TYPE_INTEGER = "Integer";
	final String MaR_TYPE_NUMBER = "Number";
	final String MaR_TYPE_TEXT = "Text";
	final String Mar_TYPE_OBJECT_TYPE = "Object Type";

	/* Meta Vocabularies and Custom Attributes */
	final String META_SBVR_URI_PREFIX = "http://www.omg.org/spec/SBVR/";

	// Roles
	final String SYSADMIN = "Sysadmin";
	final String ADMIN = "Admin";
	final String STEWARD = "Steward";
	final String NORMAL = "Normal";
	final String STAKEHOLDER = "Stakeholder";

	// Mail configuration keys
	final String BSG_FROM_EMAIL_ID_KEY = "bsg.mailing.from.email";
	final String BSG_FROM_EMAIL_NAME_KEY = "bsg.mailing.from.name";

	// Locking representation - Status values
	final String LOCK_REPRESENATION_CONFIGURATION_DOCUMENT = "Customizations.LockTermConfig";
	final String LOCK_REPRESENTATION_CLASS = "ConfigCode.LockTermConfig";
	final String LOCK_REPRESENTATION_STATUS_VALUE = "StatusValue";

	// Workflow configuration constants
	final String WF_CONFIG_DOCUMENT = "Customizations.WorkflowConfig";
	final String WF_CONFIG_CLASS = "ConfigCode.WorkflowConfig";
	final String WF_CONFIG_PROP_EXCLUSIVE = "exclusive";
	final String WF_CONFIG_PROP_MUTUALLY_EXCLUSIVE = "mutuallyExclusive";

	// Scheduler configuration constants
	final String SCHEDULER_NOTIFICATION_CONFIG_OBJECT_CLASS = "ConfigCode.Notifications";
	final String SCHEDULER_PROP_NOTIFICATION_ENABLED = "enabled";
	final String SCHEDULER_PROP_NOTIFICATION_EMAIL_CLASS = "emailclass";
	final String SCHEDULER_PROP_NOTIFICATION_EMAIL_OBJECT_NO = "emailobjectno";
	final String SCHEDULER_PROP_NOTIFICATION_PROP_EMAIL = "email";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_MINS = "cronminutes";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_HOURS = "cronhour";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_DAY_OF_MONTH = "crondayofmonth";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_MONTH = "cronmonth";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_DAY_OF_WEEK = "crondayofweek";
	final String SCHEDULER_PROP_NOTIFICATION_CRON_YEAR = "cronyear";

	// SBVR Vocabularies
	final String BUSINESS_RULES_VOC = "http://www.omg.org/spec/SBVR/20070901/DescribingBusinessRules.xml";
	final String BUSINESS_VOC = "http://www.omg.org/spec/SBVR/20070901/DescribingBusinessVocabularies.xml";
	final String MEANING_AND_REPRESENTATION_VOC = "http://www.omg.org/spec/SBVR/20070901/MeaningAndRepresentation.xml";
	final String SBVR_EXTENSIONS_VOC = "http://www.collibra.com/glossary/sbvrextensions";
	final String SBVR_VOC = "http://www.omg.org/spec/SBVR/20070901/SBVR.xml";
	final String LOGICAL_FORMULATIONS_VOC = "http://www.omg.org/spec/SBVR/20070901/LogicalFormulationOfSemantics.xml";
	final String COLLIBRA_ROLE_AND_RESPONSIBILITIES_VOCABULARY = "http://www.collibra.com/glossary/collibrauserroles";

	final String ATTRIBUTETYPES_VOCABULARY_URI = "http://www.collibra.com/glossary/attribute_types";
	final String ATTRIBUTETYPES_VOCABULARY_NAME = "Attribute Types Vocabulary";

	final String STATUSES_VOCABULARY_URI = "http://www.collibra.com/glossary/statuses";
	final String STATUSES_VOCABULARY_NAME = "Statuses Vocabulary";

	final String METAMODEL_EXTENSIONS_VOCABULARY_URI = "http://www.collibra.com/glossary/metamodelextensions";
	final String METAMODEL_EXTENSIONS_VOCABULARY_NAME = "Metamodel Extensions Vocabulary";

	// SBVR Communities
	final String RESOURCE_TYPE_TERM = "term";
	final String RESOURCE_TYPE_NAME = "name";
	final String RESOURCE_TYPE_FACT_TYPE = "fact";
	final String RESOURCE_TYPE_CHARACTERISTIC_FORM = "characteristic";
	final String RESOURCE_TYPE_VOCABULARY = "vocabulary";
	final String RESOURCE_TYPE_RULESET = "ruleset";
	final String RESOURCE_TYPE_COMMUNITY = "community";

	public static final String METAMODEL_COMMUNITY_URI = "http://www.collibra.com/METAMODEL_COMMUNITY";
	public static final String METAMODEL_COMMUNITY_NAME = "Metamodel Community";
	public static final String SBVR_ENGLISH_COMMUNITY_URI = "http://www.collibra.com/SBVR_ENGLISH_COMMUNITY";
	public static final String SBVR_ENGLISH_COMMUNITY_NAME = "English SBVR Community";

	public static final String ADMIN_COMMUNITY_URI = "http://www.collibra.com/admin_community";
	public static final String ADMIN_COMMUNITY_NAME = "Admin Community";
}
