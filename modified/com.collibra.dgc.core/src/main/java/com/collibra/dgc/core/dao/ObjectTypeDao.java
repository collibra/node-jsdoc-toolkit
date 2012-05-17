package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.Proposition;
import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.Rule;

public interface ObjectTypeDao extends AbstractDao<ObjectType> {

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR Thing.
	 */
	ObjectType getMetaThing();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR {@link ObjectType}.
	 */
	ObjectType getMetaObjectType();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR {@link IndividualConcept}.
	 */
	ObjectType getMetaIndividualConcept();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR {@link BinaryFactType}.
	 */
	ObjectType getMetaBinaryFactType();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR {@link Characteristic}.
	 */
	ObjectType getMetaCharacteristic();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR Definition {@link Attribute}.
	 */
	ObjectType getMetaDefinition();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR Description {@link Attribute}.
	 */
	ObjectType getMetaDescription();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR Example {@link Attribute}.
	 */
	ObjectType getMetaExample();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR Note {@link Attribute}.
	 */
	ObjectType getMetaNote();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the SBVR {@link Proposition}.
	 */
	ObjectType getMetaPropostion();

	/**
	 * @return The meta {@link ObjectType} for the SBVR {@link Rule}.
	 */
	ObjectType getMetaRule();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the Collibra {@link SimpleProposition}
	 */
	ObjectType getMetaSimpleProposition();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the Collibra mandatory constraint type.
	 */
	ObjectType getMetaMandatoryConstraintType();

	/**
	 * 
	 * @return @return The meta {@link ObjectType} for the Collibra uniqueness constraint type.
	 */
	ObjectType getMetaUniquenessConstraintType();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the Collibra semi-parsed constraint type.
	 */
	ObjectType getMetaSemiparsedConstraintType();

	/**
	 * 
	 * @return The meta {@link ObjectType} for the Collibra frequency constraint type.
	 */
	ObjectType getMetaFrequencyConstraintType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra status attribute type.
	 */
	ObjectType getMetaStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard candidate status attribute type.
	 */
	ObjectType getMetaCandidateStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard accepted status attribute type.
	 */
	ObjectType getMetaAcceptedStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard standard status attribute type.
	 */
	ObjectType getMetaStandardStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard obsolete status attribute type.
	 */
	ObjectType getMetaObsoleteStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard 'in progress' status attribute type.
	 */
	ObjectType getMetaInprogressStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard 'under review' status attribute type.
	 */
	ObjectType getMetaUnderReviewStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard 'reviewed' status attribute type.
	 */
	ObjectType getMetaReviewedStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard 'rejected' status attribute type.
	 */
	ObjectType getMetaRejectedStatusType();

	/**
	 * @return The meta {@link ObjectType} for the Collibra standard 'approval pending' status attribute type.
	 */
	ObjectType getMetaApprovalPendingStatusType();

	/**
	 * @return The meta {@link ObjectType} for the SBVR {@link Category}.
	 */
	ObjectType getMetaCategory();

	/**
	 * @return The meta {@link ObjectType} for the SBVR {@link CategorizationType}.
	 */
	ObjectType getMetaCategorizationType();

	/**
	 * @return The meta {@link ObjectType} for the UserType value constraint.
	 */
	ObjectType getMetaUserType();

	/**
	 * @return The meta {@link ObjectType} for the StaticListType value constraint.
	 */
	ObjectType getMetaStaticListType();

	/**
	 * @return The meta {@link ObjectType} for the RoleType.
	 */
	ObjectType getMetaRoleType();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset.
	 */
	ObjectType getMetaBusinessAsset();

	/**
	 * @return The meta {@link ObjectType} for the Business Term.
	 */
	ObjectType getMetaBusinessTerm();

	/**
	 * @return The meta {@link ObjectType} for the Technical Asset.
	 */
	ObjectType getMetaTechnicalAsset();

	/**
	 * @return The meta {@link ObjectType} for the Quality Asset.
	 */
	ObjectType getMetaQualityAsset();

	/**
	 * @return The meta {@link ObjectType} for the Code.
	 */
	ObjectType getMetaCode();
	
	ObjectType getMetaVocabularyType();

	ObjectType getMetaGlossaryType();

	ObjectType getMetaBusinessVocabularyType();
	
	/**
	 * Finds all the {@link ObjectType}s that are represented by the {@link Term}s in the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} to get all represented {@link ObjectType}s for.
	 * @return The {@link ObjectType}s in the {@link Vocabulary}
	 */
	List<ObjectType> findAllObjectTypesRepresentedInVocabulary(Vocabulary vocabulary);


}
