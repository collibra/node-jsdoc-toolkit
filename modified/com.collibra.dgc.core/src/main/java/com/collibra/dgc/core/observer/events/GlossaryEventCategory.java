package com.collibra.dgc.core.observer.events;

public enum GlossaryEventCategory implements EventCategory {
	ATTRIBUTE, TERM, // For 'Term' related events.
	NAME, // For 'Name' related events.
	CHARACTERISTIC_FORM, // For 'Characteristic Form' events
	BINARY_FACT_TYPE_FORM, // For 'Binary Fact Type Form' events
	MEANING, // For 'Meaning' related events.
	RELATION, // For Relation related events.
	VOCABULARY, // For 'Vocabulary' releated events.
	COMMUNITY, //
	RULESET, //
	RULE_STATEMENT, //
	SERVICE_BFTF, // Provide service for BFTF related events for VocabularyComponent/RepresentationService.
	CATEGORY, //
	MEMBER_COMMUNITY, // Members
	ROLE, // Role
	MEMBER, // Member
	OBSERVATION_MANAGER, // When observation manager is cleared from all event handlers.
	USER, // Users
	GROUP, // groups
	GROUP_USERS // when a person is added or removed from a group
}
