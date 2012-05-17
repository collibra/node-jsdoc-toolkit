package com.collibra.dgc.core.service.bootstrapper.impl.meanings;

import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;

public class SbvrBootstrapperOptionsProvider {

	private final MeaningService meaningService;
	private final ObjectTypeDao objectTypeDao;

	public SbvrBootstrapperOptionsProvider(MeaningService meaningService, ObjectTypeDao objectTypeDao) {
		this.meaningService = meaningService;
		this.objectTypeDao = objectTypeDao;
	}

	public SbvrImporterOptions prepareOptions() {
		SbvrImporterOptions options = new SbvrImporterOptions();
		options.setShouldRemoveSuffixIndices(true);
		options.setShouldPersistVocabulary(true);
		// set this to true for making a minimal bootstrap suited for unit tests
		options.setShouldMinimizeImport(false);

		options.mapSignifierToObjectType("Thing",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_THING_UUID));
		options.mapSignifierToObjectType("Meaning",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_MEANING_UUID));
		options.mapSignifierToObjectType("Concept",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CONCEPT_UUID));
		options.mapSignifierToObjectType("Noun Concept",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_NOUN_CONCEPT_UUID));
		options.mapSignifierToObjectType("Object Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_OBJECT_TYPE_UUID));
		options.mapSignifierToObjectType("Concept Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CONCEPT_TYPE_UUID));
		options.mapSignifierToObjectType("Role",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_ROLE_UUID));
		options.mapSignifierToObjectType("Fact Type Role",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_FACT_TYPE_ROLE_UUID));
		options.mapSignifierToObjectType("Fact Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_FACT_TYPE_UUID));
		options.mapSignifierToObjectType("Characteristic",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CHARACTERISTIC_UUID));
		options.mapSignifierToObjectType("Binary Fact Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_BINARY_FACT_TYPE_UUID));
		options.mapSignifierToObjectType("Individual Concept",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_INDIVIDUAL_CONCEPT_UUID));
		options.mapSignifierToObjectType("Proposition",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_PROPOSITION_UUID));
		options.mapSignifierToObjectType("Fact",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000021"));
		options.mapSignifierToObjectType("Question",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000026"));

		options.mapSignifierToObjectType("Expression",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000100"));
		options.mapSignifierToObjectType("Signifier",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000101"));
		options.mapSignifierToObjectType("Text",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000102"));
		options.mapSignifierToObjectType("URI",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000103"));

		options.mapSignifierToObjectType("Representation",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000200"));
		options.mapSignifierToObjectType("Designation",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000201"));
		options.mapSignifierToObjectType("Definition",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_DEFINITION_UUID));
		options.mapSignifierToObjectType("Statement",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000203"));
		options.mapSignifierToObjectType("Fact Type Form",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000204"));
		options.mapSignifierToObjectType("Sentenial Form",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000205"));
		options.mapSignifierToObjectType("Reading Direction",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000206"));
		options.mapSignifierToObjectType("Namespace",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000207"));
		options.mapSignifierToObjectType("Vocabulary Namespace",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000208"));
		options.mapSignifierToObjectType("Language",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000211"));

		options.mapSignifierToObjectType("State Of Affairs",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000500"));
		options.mapSignifierToObjectType("Actuality",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000501"));
		options.mapSignifierToObjectType("Extension",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000502"));
		options.mapSignifierToObjectType("Instance",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000503"));

		options.mapSignifierToObjectType("Set",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000601"));
		options.mapSignifierToObjectType("Cardinality",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000602"));
		options.mapSignifierToObjectType("Quantity",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000603"));
		options.mapSignifierToObjectType("Number",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000604"));
		options.mapSignifierToObjectType("Integer",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000605"));
		options.mapSignifierToObjectType("Nonnegative Integer",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000606"));
		options.mapSignifierToObjectType("Positive Integer",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000000607"));

		options.mapSignifierToObjectType("Community",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000003001"));
		options.mapSignifierToObjectType("Semantic Community",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000003002"));
		options.mapSignifierToObjectType("Speech Community",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000003003"));
		options.mapSignifierToObjectType("Term",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000003102"));
		options.mapSignifierToObjectType("Name",
				meaningService.findObjectTypeByResourceId("00000000-0000-0000-0000-000000003103"));
		options.mapSignifierToObjectType("Description",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_DESCRIPTION_UUID));
		options.mapSignifierToObjectType("Descriptive Example",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_EXAMPLE_UUID));
		options.mapSignifierToObjectType("Note",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_NOTE_UUID));

		options.mapSignifierToObjectType("Rule",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_RULE_UUID));

		options.mapSignifierToObjectType("Simple Proposition",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_SIMPLE_PROPOSITION_UUID));
		options.mapSignifierToObjectType("Constraint Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CONSTRAINT_TYPE_UUID));
		options.mapSignifierToObjectType("Mandatory",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_MANDATORY_CONSTRAINT_UUID));
		options.mapSignifierToObjectType("Uniqueness",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_UNIQUENESS_CONSTRAINT_UUID));
		options.mapSignifierToObjectType("Semiparsed",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_SEMIPARSED_CONSTRAINT_TYPE_UUID));
		options.mapSignifierToObjectType("Frequency",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_FREQUENCY_CONSTRAINT_TYPE_UUID));

		options.mapSignifierToObjectType("Status Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STATUS_TYPE_UUID));

		options.mapSignifierToObjectType("Attribute Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_ATTRIBUTE_TYPE_UUID));
		options.mapSignifierToObjectType("Candidate",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CANDIDATE_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Accepted",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Standard",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STANDARD_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Obsolete",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_OBSOLETE_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("In Progress",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_IN_PROGRESS_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Under Review",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_UNDER_REVIEW_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Reviewed",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_REVIEWED_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Rejected",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_REJECTED_STATUS_TYPE_UUID));
		options.mapSignifierToObjectType("Approval Pending",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_APPROVAL_PENDING_STATUS_TYPE_UUID));

		options.mapSignifierToObjectType("Category",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CATEGORY_UUID));

		options.mapSignifierToObjectType("More General Concept",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_MORE_GENERAL_CONCEPT));

		options.mapSignifierToObjectType("Categorization Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CATEGORIZATION_TYPE_UUID));

		options.mapSignifierToObjectType("String Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STRING_TYPE_UUID));

		options.mapSignifierToObjectType("User Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_USER_TYPE_UUID));
		options.mapSignifierToObjectType("Date Time Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_DATE_TIME_TYPE_UUID));
		options.mapSignifierToObjectType("Static List Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID));
		options.mapSignifierToObjectType("Multi Static List Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_MULTI_STATIC_LIST_TYPE_UUID));

		options.mapSignifierToObjectType("Static List Allowed Values",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STATIC_LIST_ALLOWED_VALUES_TYPE_UUID));

		options.mapSignifierToObjectType("Role Type",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_ROLE_TYPE_UUID));

		options.mapSignifierToObjectType("Admin",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_ADMIN_ROLE_UUID));

		options.mapSignifierToObjectType("Sysadmin",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_SYSADMIN_ROLE_UUID));

		options.mapSignifierToObjectType("Steward",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STEWARD_ROLE_UUID));

		options.mapSignifierToObjectType("Normal",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_NORMAL_ROLE_UUID));

		options.mapSignifierToObjectType("Stakeholder",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_STAKEHOLDER_ROLE_UUID));

		options.mapSignifierToGeneralConcept("Attribute Type", objectTypeDao.getMetaObjectType());

		// meta model extensions
		options.mapSignifierToObjectType("Business Asset",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_BUSINESS_ASSET_UUID));
		options.mapSignifierToObjectType("Business Term",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_BUSINESS_TERM_UUID));
		options.mapSignifierToObjectType("Technical Asset",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_TECHNICAL_ASSET_UUID));
		options.mapSignifierToObjectType("Code Value",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_CODE_UUID));
		options.mapSignifierToObjectType("Quality Asset",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_QUALITY_ASSET_UUID));

		// vocabularies

		options.mapSignifierToObjectType("Vocabulary",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_VOCABULARY));
		options.mapSignifierToObjectType("Business Vocabulary",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_BUSINESS_VOCABULARY));
		options.mapSignifierToObjectType("Glossary",
				meaningService.findObjectTypeByResourceId(MeaningConstants.META_GLOSSARY_VOCABULARY));

		return options;
	}
}
