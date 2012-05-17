package com.collibra.dgc.core.service.bootstrapper.impl.meanings;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;

public class SbvrMeaningBootstrapper {
	private final Session session;

	private final MeaningFactory meaningFactory;
	private ObjectTypeImpl metaStaticListType;

	public SbvrMeaningBootstrapper(final MeaningFactory meaningFactory, Session session) {
		this.session = session;
		this.meaningFactory = meaningFactory;
	}

	public void bootStrapMeaning() {

		// 1: Thing (the most abstract concept)
		ObjectTypeImpl metaThing = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_THING_UUID, null, null);

		// 002: Meaning
		ObjectTypeImpl metaMeaning = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_MEANING_UUID, null,
				metaThing);

		// 003: Concept
		ObjectTypeImpl metaConcept = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_CONCEPT_UUID, null,
				metaThing);

		// 004: Noun Concept
		ObjectTypeImpl metaNounConcept = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_NOUN_CONCEPT_UUID, null,
				metaConcept);

		// 005: Object Type
		ObjectTypeImpl metaObjectType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_OBJECT_TYPE_UUID, null,
				metaNounConcept);

		// 006: Concept Type
		ObjectTypeImpl metaConceptType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_CONCEPT_TYPE_UUID,
				metaThing, metaObjectType);

		metaThing.setType(metaObjectType);
		metaMeaning.setType(metaObjectType);
		metaConcept.setType(metaObjectType);
		metaNounConcept.setType(metaConceptType);
		metaObjectType.setType(metaConceptType);

		session.saveOrUpdate(metaThing);
		session.saveOrUpdate(metaMeaning);
		session.saveOrUpdate(metaConcept);
		session.saveOrUpdate(metaNounConcept);
		session.saveOrUpdate(metaObjectType);
		metaThing.saved();
		metaMeaning.saved();
		metaConcept.saved();
		metaNounConcept.saved();
		metaObjectType.saved();

		// 007: Role
		ObjectTypeImpl metaRole = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_ROLE_UUID, metaConceptType,
				metaNounConcept);

		// 008: Fact Type Role
		saveObjectType(MeaningConstants.META_FACT_TYPE_ROLE_UUID, metaConceptType, metaRole);

		// 009: Fact Type
		ObjectTypeImpl metaFactType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_FACT_TYPE_UUID,
				metaConceptType, metaConcept);

		// 010: Characteristic
		saveObjectType(MeaningConstants.META_CHARACTERISTIC_UUID, metaObjectType, metaFactType);

		// 011: Binary Fact Type
		saveObjectType(MeaningConstants.META_BINARY_FACT_TYPE_UUID, metaObjectType, metaFactType);

		// 012: Individual Concept
		saveObjectType(MeaningConstants.META_INDIVIDUAL_CONCEPT_UUID, metaConceptType, metaNounConcept);

		// 013: Concept specializes / specialized by Concept
		// 014: Concept is coextensive with / is coextensive with Concept
		// 015: Concept incorporates / incorporated by Characteristic
		// 016: Role ranges over / ranged over by Object Type
		// 017: Fact Type has / of Role

		// 018: Proposition
		ObjectTypeImpl metaProposition = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_PROPOSITION_UUID,
				metaObjectType, metaMeaning);

		// 019: Proposition is true
		// 020: Proposition is false

		// 021: Fact
		saveObjectType("00000000-0000-0000-0000-000000000021", metaObjectType, metaProposition);

		// 026: Question
		saveObjectType("00000000-0000-0000-0000-000000000026", metaObjectType, metaMeaning);

		// 100: Expression
		ObjectTypeImpl metaExpression = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000100",
				metaObjectType, metaThing);

		// 101: Signifier
		saveObjectType("00000000-0000-0000-0000-000000000101", metaRole, metaExpression);

		// 102: Text
		ObjectTypeImpl metaText = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000102",
				metaObjectType, metaExpression);

		// 102: Starting Character Position

		// 103: URI
		saveObjectType("00000000-0000-0000-0000-000000000103", metaObjectType, metaText);

		// Representations fragment of Meaning and Representation Vocabulary

		// 200: Representation // will set general concept later
		ObjectTypeImpl metaRepresentation = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000200",
				metaObjectType, null);

		// 201: Designation
		ObjectTypeImpl metaDesignation = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000201",
				metaObjectType, metaRepresentation);

		// 203: Statement
		saveObjectType("00000000-0000-0000-0000-000000000203", metaObjectType, metaRepresentation);

		// 204: Fact Type Form
		ObjectTypeImpl metaFactTypeForm = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000204",
				metaObjectType, metaRepresentation);

		// 205: Sentenial Form
		saveObjectType("00000000-0000-0000-0000-000000000205", metaObjectType, metaFactTypeForm);

		// 206: ReadingDirection
		saveObjectType("00000000-0000-0000-0000-000000000206", metaObjectType, metaDesignation);

		// 207: Namespace
		saveObjectType("00000000-0000-0000-0000-000000000207", metaObjectType, metaThing);

		// 208: Vocabulary Namespace
		saveObjectType("00000000-0000-0000-0000-000000000208", metaObjectType, metaThing);

		// 209: Attributive Namespace
		// 210: Subject Concept

		// 211: Language
		saveObjectType("00000000-0000-0000-0000-000000000211", metaObjectType, metaThing);

		// Reference Schemes fragment of Meaning and Representation Vocabulary
		// 300
		// Conceptual Schemas and Models fragment of Meaning and Representation Vocabulary
		// 400
		// Extensions fragment of Meaning and Representation Vocabulary
		// 500

		// 500: State of Affairs
		ObjectTypeImpl metaStateAffairs = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000500",
				metaObjectType, metaMeaning);

		// 501: Actuality
		ObjectTypeImpl metaActuality = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000501",
				metaObjectType, metaStateAffairs);

		metaRepresentation.setGeneralConcept(metaActuality);
		session.saveOrUpdate(metaRepresentation);

		// 502: Extension
		ObjectTypeImpl metaExtension = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000502",
				metaObjectType, metaThing);

		// 503: Instance
		saveObjectType("00000000-0000-0000-0000-000000000503", metaRole, metaThing);

		// Elementary Concepts fragment of Meaning and Representation Vocabulary

		// 601: Set
		ObjectTypeImpl metaSet = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000601",
				metaObjectType, metaThing);

		metaExtension.setGeneralConcept(metaSet);
		session.saveOrUpdate(metaExtension);

		// 602: Cardinality
		ObjectTypeImpl metaCardinality = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000602",
				metaRole, metaThing);

		// 603: Quantity
		ObjectTypeImpl metaQuantity = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000603",
				metaObjectType, metaNounConcept);

		// 604: Number
		ObjectTypeImpl metaNumber = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000604",
				metaObjectType, metaQuantity);

		// 605: Integer
		saveObjectType("00000000-0000-0000-0000-000000000605", metaObjectType, metaNumber);

		// 606: Nonnegative Integer
		ObjectTypeImpl metaNonnegativeInteger = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000000606",
				metaObjectType, metaNumber);

		metaCardinality.setGeneralConcept(metaNonnegativeInteger);
		session.saveOrUpdate(metaCardinality);

		// 607: Positive Integer
		saveObjectType("00000000-0000-0000-0000-000000000607", metaObjectType, metaNonnegativeInteger);

		// 3000
		// Business Meaning Fragment of Describing Business Vocabularies

		// 3001: Community
		ObjectTypeImpl metaCommunity = (ObjectTypeImpl) saveObjectType("00000000-0000-0000-0000-000000003001",
				metaObjectType, metaThing);

		// 3002: Semantic Community
		saveObjectType("00000000-0000-0000-0000-000000003002", metaObjectType, metaCommunity);

		// 3003: Speech Community
		saveObjectType("00000000-0000-0000-0000-000000003003", metaObjectType, metaCommunity);

		// 3004: Body of Shared Meanings
		// 3005: body of shared concepts
		// 3006: elementary fact type

		// 3007: Vocabulary
		ObjectTypeImpl metaVocabulary = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_VOCABULARY,
				metaObjectType, metaSet);

		// 3008: Business Vocabulary
		ObjectTypeImpl businessVocabulary = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_BUSINESS_VOCABULARY,
				metaObjectType, metaVocabulary);

		// 3009: Terminological Dictionary
		saveObjectType(MeaningConstants.META_GLOSSARY_VOCABULARY, metaObjectType, businessVocabulary);

		// 3010: Category
		saveObjectType(MeaningConstants.META_CATEGORY_UUID, metaRole, metaConcept);

		// 3011: More General Concept
		saveObjectType(MeaningConstants.META_MORE_GENERAL_CONCEPT, metaRole, metaConcept);

		// 3013: Categorization Type
		saveObjectType(MeaningConstants.META_CATEGORIZATION_TYPE_UUID, metaObjectType, metaConcept);

		// Business Representation Fragment of Describing Business Vocabularies -->

		// 3100 Subject Field
		// 3101 Designation Context

		// 3102 Term
		saveObjectType("00000000-0000-0000-0000-000000003102", metaObjectType, metaDesignation);

		// 3103 Name
		saveObjectType("00000000-0000-0000-0000-000000003103", metaObjectType, metaDesignation);

		// 3104 nonverbal designation
		// 3105 icon
		// 3106 fact symbol
		// 3107 fact type role designation
		// 3108 res
		// 3109 preferred designation
		// 3110 prohibited designation
		// 3111 Representation Formality
		// 3112 informal representation
		// 3113 formal representation

		// 4000
		// Vocabulary for describing Business Rules
		// 4005 Rule
		saveObjectType("00000000-0000-0000-0000-000000004005", metaObjectType, metaProposition);

		// 5000
		// Vocabulary for describing Collibra extensions to SBVR
		// 5001 SimpleProposition
		saveObjectType(MeaningConstants.META_SIMPLE_PROPOSITION_UUID, metaObjectType, metaProposition);

		// 5002 Constraint type
		ObjectTypeImpl metaConstraintType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_CONSTRAINT_TYPE_UUID,
				metaObjectType, metaThing);

		// 5003 Mandatory constraint type
		saveObjectType(MeaningConstants.META_MANDATORY_CONSTRAINT_UUID, metaObjectType, metaConstraintType);

		// 5004 Uniqueness constraint type
		saveObjectType(MeaningConstants.META_UNIQUENESS_CONSTRAINT_UUID, metaObjectType, metaConstraintType);

		// 5005 Semi-parsed constraint type
		saveObjectType(MeaningConstants.META_SEMIPARSED_CONSTRAINT_TYPE_UUID, metaObjectType, metaConstraintType);

		// 5005 Frequency Constraint type
		saveObjectType(MeaningConstants.META_FREQUENCY_CONSTRAINT_TYPE_UUID, metaObjectType, metaConstraintType);

		// 5100 Attribute Type
		ObjectTypeImpl metaAttributeType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_ATTRIBUTE_TYPE_UUID,
				metaObjectType, metaThing);

		// 5007 Status Type
		ObjectTypeImpl metaStatusType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_STATUS_TYPE_UUID,
				metaObjectType, metaThing);

		// 5008 Candidate
		saveObjectType(MeaningConstants.META_CANDIDATE_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5009 Accepted
		saveObjectType(MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5010 Standard
		saveObjectType(MeaningConstants.META_STANDARD_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5011 Obsolete
		saveObjectType(MeaningConstants.META_OBSOLETE_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5019 In progress
		saveObjectType(MeaningConstants.META_IN_PROGRESS_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5020 Under review
		saveObjectType(MeaningConstants.META_UNDER_REVIEW_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5021 Reviewed
		saveObjectType(MeaningConstants.META_REVIEWED_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5022 Rejected
		saveObjectType(MeaningConstants.META_REJECTED_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5023 Approved
		saveObjectType(MeaningConstants.META_APPROVAL_PENDING_STATUS_TYPE_UUID, metaStatusType, metaThing);

		// 5115 String attribute type
		ObjectType stringAttrType = saveObjectType(MeaningConstants.META_STRING_TYPE_UUID, metaObjectType,
				metaAttributeType);

		// 5012 UserType
		saveObjectType(MeaningConstants.META_USER_TYPE_UUID, metaObjectType, metaAttributeType);

		// 5013 Static List Type
		metaStaticListType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID,
				metaObjectType, metaAttributeType);

		// 5213 Multi Static List Type
		saveObjectType(MeaningConstants.META_MULTI_STATIC_LIST_TYPE_UUID, metaObjectType, metaAttributeType);

		// 5113 Attribute Type Allowed Values (only use to define allowed values of 5013 and 5213)
		ObjectTypeImpl metaStaticListAllowedValues = (ObjectTypeImpl) saveObjectType(
				MeaningConstants.META_STATIC_LIST_ALLOWED_VALUES_TYPE_UUID, metaObjectType, metaAttributeType);

		// 5114 Date Time Type
		saveObjectType(MeaningConstants.META_DATE_TIME_TYPE_UUID, metaObjectType, metaAttributeType);

		// 5014 RoleType
		ObjectTypeImpl metaRoleType = (ObjectTypeImpl) saveObjectType(MeaningConstants.META_ROLE_TYPE_UUID,
				metaObjectType, metaThing);

		// 5015 RoleType
		saveObjectType(MeaningConstants.META_ADMIN_ROLE_UUID, metaRoleType, metaThing);

		// 5016 RoleType
		saveObjectType(MeaningConstants.META_STEWARD_ROLE_UUID, metaRoleType, metaThing);

		// 5017 RoleType
		saveObjectType(MeaningConstants.META_NORMAL_ROLE_UUID, metaRoleType, metaThing);

		// 5017 RoleType
		saveObjectType(MeaningConstants.META_STAKEHOLDER_ROLE_UUID, metaRoleType, metaThing);

		// 5027 Sysadmin
		saveObjectType(MeaningConstants.META_SYSADMIN_ROLE_UUID, metaRoleType, metaThing);

		ObjectType businessAsset = saveObjectType(MeaningConstants.META_BUSINESS_ASSET_UUID, metaObjectType,
				metaObjectType);
		saveObjectType(MeaningConstants.META_BUSINESS_TERM_UUID, metaObjectType, businessAsset);
		ObjectType technicalAsset = saveObjectType(MeaningConstants.META_TECHNICAL_ASSET_UUID, metaObjectType,
				metaObjectType);
		saveObjectType(MeaningConstants.META_CODE_UUID, metaObjectType, technicalAsset);
		saveObjectType(MeaningConstants.META_QUALITY_ASSET_UUID, metaObjectType, metaObjectType);

		// 202: Definition
		saveObjectType("00000000-0000-0000-0000-000000000202", stringAttrType, metaRepresentation);

		// 3114 Description
		saveObjectType("00000000-0000-0000-0000-000000003114", stringAttrType, metaRepresentation);

		// 3115 Descriptive Example
		saveObjectType("00000000-0000-0000-0000-000000003115", stringAttrType, metaRepresentation);

		// 3116 Note
		saveObjectType("00000000-0000-0000-0000-000000003116", stringAttrType, metaRepresentation);

		// 9999 Reserved for

		session.flush();
	}

	public ObjectType getMetaStaticListType() {
		return metaStaticListType;
	}

	private ObjectType getObjectTypeByUUID(String uuid) {

		String queryString = "select ot from ObjectTypeImpl as ot where ot.id = :uuid and ot.class = 'ObjectTypeImpl'";
		Query q = session.createQuery(queryString);
		q.setParameter("uuid", uuid);
		q.setFlushMode(FlushMode.MANUAL);
		return (ObjectType) q.uniqueResult();
	}

	private ObjectType saveObjectType(String uuid, ObjectType type, ObjectType generalConcept) {
		ObjectTypeImpl objectType = (ObjectTypeImpl) getObjectTypeByUUID(uuid);
		if (objectType == null) {
			objectType = (ObjectTypeImpl) meaningFactory.makeObjectType(uuid);

			objectType.setType(type);
			objectType.setGeneralConcept(generalConcept);
			session.save(objectType);
		}
		return objectType;
	}

}
