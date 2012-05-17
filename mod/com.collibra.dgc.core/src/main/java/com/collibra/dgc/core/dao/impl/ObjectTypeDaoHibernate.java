package com.collibra.dgc.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

@Service
public class ObjectTypeDaoHibernate extends AbstractDaoHibernate<ObjectType, ObjectTypeImpl> implements ObjectTypeDao {

	@Autowired
	public ObjectTypeDaoHibernate(SessionFactory sessionFactory) {
		super(ObjectType.class, ObjectTypeImpl.class, sessionFactory);
	}

	public ObjectType getMetaThing() {
		return findById(MeaningConstants.META_THING_UUID);
	}

	// Concepts
	public ObjectType getMetaObjectType() {
		return findById(MeaningConstants.META_OBJECT_TYPE_UUID);
	}

	public ObjectType getMetaIndividualConcept() {
		return findById(MeaningConstants.META_INDIVIDUAL_CONCEPT_UUID);
	}

	public ObjectType getMetaBinaryFactType() {
		return findById(MeaningConstants.META_BINARY_FACT_TYPE_UUID);
	}

	public ObjectType getMetaCharacteristic() {
		return findById(MeaningConstants.META_CHARACTERISTIC_UUID);
	}

	// Attributes
	public ObjectType getMetaDefinition() {
		return findById(MeaningConstants.META_DEFINITION_UUID);
	}

	public ObjectType getMetaDescription() {
		return findById(MeaningConstants.META_DESCRIPTION_UUID);
	}

	public ObjectType getMetaExample() {
		return findById(MeaningConstants.META_EXAMPLE_UUID);
	}

	public ObjectType getMetaNote() {
		return findById(MeaningConstants.META_NOTE_UUID);
	}

	// Propositions
	public ObjectType getMetaPropostion() {
		return findById(MeaningConstants.META_PROPOSITION_UUID);
	}

	public ObjectType getMetaSimpleProposition() {
		return findById(MeaningConstants.META_SIMPLE_PROPOSITION_UUID);
	}

	// Rules & Constraints
	public ObjectType getMetaRule() {
		return findById(MeaningConstants.META_RULE_UUID);
	}

	public ObjectType getMetaMandatoryConstraintType() {
		return findById(MeaningConstants.META_MANDATORY_CONSTRAINT_UUID);
	}

	public ObjectType getMetaUniquenessConstraintType() {
		return findById(MeaningConstants.META_UNIQUENESS_CONSTRAINT_UUID);
	}

	public ObjectType getMetaSemiparsedConstraintType() {
		return findById(MeaningConstants.META_SEMIPARSED_CONSTRAINT_TYPE_UUID);
	}

	public ObjectType getMetaFrequencyConstraintType() {
		return findById(MeaningConstants.META_FREQUENCY_CONSTRAINT_TYPE_UUID);
	}

	// Statuses
	public ObjectType getMetaStatusType() {
		return findById(MeaningConstants.META_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaCandidateStatusType() {
		return findById(MeaningConstants.META_CANDIDATE_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaAcceptedStatusType() {
		return findById(MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaStandardStatusType() {
		return findById(MeaningConstants.META_STANDARD_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaObsoleteStatusType() {
		return findById(MeaningConstants.META_OBSOLETE_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaInprogressStatusType() {
		return findById(MeaningConstants.META_IN_PROGRESS_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaUnderReviewStatusType() {
		return findById(MeaningConstants.META_UNDER_REVIEW_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaReviewedStatusType() {
		return findById(MeaningConstants.META_REVIEWED_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaRejectedStatusType() {
		return findById(MeaningConstants.META_REJECTED_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaApprovalPendingStatusType() {
		return findById(MeaningConstants.META_APPROVAL_PENDING_STATUS_TYPE_UUID);
	}

	public ObjectType getMetaCategory() {
		return findById(MeaningConstants.META_CATEGORY_UUID);
	}

	public ObjectType getMetaCategorizationType() {
		return findById(MeaningConstants.META_CATEGORIZATION_TYPE_UUID);
	}

	public ObjectType getMetaUserType() {
		return findById(MeaningConstants.META_USER_TYPE_UUID);
	}

	public ObjectType getMetaStaticListType() {
		return findById(MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID);
	}

	public ObjectType getMetaRoleType() {
		return findById(MeaningConstants.META_ROLE_TYPE_UUID);
	}

	public ObjectType getMetaAdminRoleType() {
		return findById(MeaningConstants.META_ADMIN_ROLE_UUID);
	}

	public ObjectType getMetaStewardRoleType() {
		return findById(MeaningConstants.META_STEWARD_ROLE_UUID);
	}

	public ObjectType getMetaNormalRoleType() {
		return findById(MeaningConstants.META_NORMAL_ROLE_UUID);
	}

	public ObjectType getMetaStakeholderRoleType() {
		return findById(MeaningConstants.META_STAKEHOLDER_ROLE_UUID);
	}

	// vocabulary

	@Override
	public ObjectType getMetaVocabularyType() {
		return findById(MeaningConstants.META_VOCABULARY);
	}

	@Override
	public ObjectType getMetaGlossaryType() {
		return findById(MeaningConstants.META_GLOSSARY_VOCABULARY);
	}

	@Override
	public ObjectType getMetaBusinessVocabularyType() {
		return findById(MeaningConstants.META_BUSINESS_VOCABULARY);
	}

	// metamodel

	@Override
	public ObjectType getMetaBusinessAsset() {
		return findById(MeaningConstants.META_BUSINESS_ASSET_UUID);
	}

	@Override
	public ObjectType getMetaBusinessTerm() {
		return findById(MeaningConstants.META_BUSINESS_TERM_UUID);
	}

	@Override
	public ObjectType getMetaTechnicalAsset() {
		return findById(MeaningConstants.META_TECHNICAL_ASSET_UUID);
	}

	@Override
	public ObjectType getMetaCode() {
		return findById(MeaningConstants.META_CODE_UUID);
	}

	@Override
	public ObjectType getMetaQualityAsset() {
		return findById(MeaningConstants.META_QUALITY_ASSET_UUID);
	}

	@SuppressWarnings("unchecked")
	public List<ObjectType> findAllObjectTypesRepresentedInVocabulary(Vocabulary vocabulary) {
		// FIXME write this in one query
		List<Term> terms = getSession().createCriteria(TermImpl.class).add(Restrictions.eq("vocabulary", vocabulary))
				.setFlushMode(FlushMode.MANUAL).list();
		List<ObjectType> objectTypes = new ArrayList<ObjectType>();
		for (Term term : terms) {
			if (objectTypes.contains(term.getObjectType())) {
				continue;
			}
			objectTypes.add(term.getObjectType());
		}
		return objectTypes;
	}

	@Override
	public ObjectType save(ObjectType objectType) {

		if (objectType.getType() == null) {
			objectType.setType(getMetaObjectType());
		}

		if (objectType.getGeneralConcept() == null && !objectType.getId().equals(MeaningConstants.META_THING_UUID)) {
			objectType.setGeneralConcept(getMetaThing());
		}

		return super.save(objectType);
	}

}
