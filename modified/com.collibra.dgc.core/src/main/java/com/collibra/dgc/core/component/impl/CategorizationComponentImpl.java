package com.collibra.dgc.core.component.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.CategorizationComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * Categorization APIs implementation.
 * @author amarnath
 * 
 */
@Service
public class CategorizationComponentImpl implements CategorizationComponent {

	@Autowired
	private CategorizationService categorizationService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private MeaningService meaningService;

	@Override
	@Transactional
	public Term addCategorizationType(String vocabularyRId, String signifier, String isForConceptTermRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(signifier, DGCErrorCodes.CATEGORIZATION_TYPE_NAME_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_NAME_EMPTY, "signifier");

		Defense.notEmpty(isForConceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"isForConceptTermRId");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		Term isForConceptTerm = representationService.getTermWithError(isForConceptTermRId);

		return categorizationService.createCategorizationType(signifier, isForConceptTerm, vocabulary).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term addCategory(String vocabularyRId, String signifier, String categorizationTypeTermRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(signifier, DGCErrorCodes.CATEGORY_NAME_NULL, DGCErrorCodes.CATEGORY_NAME_EMPTY, "signifier");

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, "categorizationTypeTermRId");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		Term categorizationTypeTerm = representationService.getTermWithError(categorizationTypeTermRId);
		CategorizationType categorizationType = categorizationService
				.getCategorizationTypeWithError(categorizationTypeTerm.getObjectType().getId());
		return categorizationService.createCategory(signifier, categorizationType, vocabulary).getTerms().iterator()
				.next();
	}

	@Override
	@Transactional
	public Term addCategoryForConcept(String signifier, String isForConceptTermRId, String categorizationTypeTermRId,
			String vocabularyRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, "categorizationTypeTermRId");

		Defense.notEmpty(signifier, DGCErrorCodes.CATEGORY_NAME_NULL, DGCErrorCodes.CATEGORY_NAME_EMPTY, "signifier");

		Defense.notEmpty(isForConceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"isForConceptTermRId");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		Term categorizationTypeTerm = representationService.getTermWithError(categorizationTypeTermRId);
		CategorizationType categorizationType = categorizationService
				.getCategorizationTypeWithError(categorizationTypeTerm.getObjectType().getId());
		ObjectType forConcept = representationService.getTermWithError(isForConceptTermRId).getObjectType();

		Category cat = categorizationService.createCategoryForConcept(signifier, forConcept, categorizationType,
				vocabulary);
		return cat.getTerms().iterator().next();
	}

	@Override
	@Transactional
	public Term addCategory(String signifier, String categorizationSchemeTermRId, String categorizationTypeTermRId,
			String vocabularyRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				vocabularyRId);

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, "categorizationTypeTermRId");

		Defense.notEmpty(signifier, DGCErrorCodes.CATEGORY_NAME_NULL, DGCErrorCodes.CATEGORY_NAME_EMPTY, "signifier");

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		Term categorizationTypeTerm = representationService.getTermWithError(categorizationTypeTermRId);
		CategorizationType categorizationType = categorizationService
				.getCategorizationTypeWithError(categorizationTypeTerm.getObjectType().getId());
		Term catSchemeTerm = representationService.getTermWithError(categorizationSchemeTermRId);

		Category cat = categorizationService.createCategory(signifier, catSchemeTerm, categorizationType, vocabulary);
		return cat.getTerms().iterator().next();
	}

	@Override
	@Transactional
	public Term getCategorizationTypeTerm(String vocabularyRId, String categorizationTypeRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(categorizationTypeRId, DGCErrorCodes.CATEGORIZATION_TYPE_NAME_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_NAME_EMPTY, "categorizationTypeRId");

		return categorizationService.findCategorizationTypeTerm(
				representationService.getVocabularyWithError(vocabularyRId),
				categorizationService.getCategorizationTypeWithError(categorizationTypeRId));
	}

	@Override
	@Transactional
	public List<Term> getCategorizationTypeTermsForConcept(String conceptTermRId) {

		Defense.notEmpty(conceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "conceptTermRId");

		Concept concept = representationService.getTermWithError(conceptTermRId).getMeaning();

		return categorizationService.findCategorizationTypeTermsForConcept(concept);
	}

	@Override
	@Transactional
	public List<Term> getCategoryTerms(String categorizationTypeTermRId, String vocabularyRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		return categorizationService.findCategoryTerms(
				categorizationService.getCategorizationTypeWithError(representationService
						.getTermWithError(categorizationTypeTermRId).getObjectType().getId()),
				representationService.getVocabularyWithError(vocabularyRId));
	}

	@Override
	@Transactional
	public List<Term> getCategoryTermsForConcept(String conceptTermRId) {

		Defense.notEmpty(conceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "conceptTermRId");

		Concept forConcept = representationService.getTermWithError(conceptTermRId).getMeaning();

		if (forConcept == null) {
			return new LinkedList<Term>();
		}

		return categorizationService.findCategoriesTermsForConcept(forConcept);
	}

	@Override
	@Transactional
	public Map<Term, List<Term>> getCategorizationTypeTermToCategoriesTermMapForConcept(String conceptTermRId) {

		Defense.notEmpty(conceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "conceptTermRId");

		Concept forConcept = representationService.getTermWithError(conceptTermRId).getMeaning();

		if (forConcept == null) {
			return new HashMap<Term, List<Term>>();
		}

		return categorizationService.getCategorizationTypeTermToCategoriesTermMapForConcept(forConcept);
	}

	@Override
	@Transactional
	public CategorizationType getCategorizationType(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.CATEGORIZATION_TYPE_NULL, DGCErrorCodes.CATEGORIZATION_TYPE_EMPTY, "RId");

		return categorizationService.getCategorizationTypeWithError(rId);
	}

	@Override
	@Transactional
	public Term getCategoryTerm(String vocabularyRId, String categoryRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		Defense.notEmpty(categoryRId, DGCErrorCodes.CATEGORY_ID_NULL, DGCErrorCodes.CATEGORY_ID_EMPTY, "categoryRId");

		return categorizationService.findCategoryTerm(representationService.getVocabularyWithError(vocabularyRId),
				categorizationService.getCategoryWithError(categoryRId));
	}

	@Override
	@Transactional
	public Category getCategory(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.CATEGORY_ID_NULL, DGCErrorCodes.CATEGORY_ID_EMPTY, "RId");

		return categorizationService.getCategoryWithError(rId);
	}

	@Override
	@Transactional
	public boolean isCategory(String objectTypeTermRId) {

		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");

		return categorizationService.isCategory(representationService.getTermWithError(objectTypeTermRId)
				.getObjectType());
	}

	@Override
	@Transactional
	public boolean isCategorizationType(String objectTypeTermRId) {

		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");

		return categorizationService.isCategorizationType(representationService.getTermWithError(objectTypeTermRId)
				.getObjectType());
	}

	@Override
	@Transactional
	public Term addCategoryToCategorizationScheme(String categorizationSchemeTermRId, String categoryTermRId) {

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Defense.notEmpty(categoryTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "categoryTermRId");

		return categorizationService.addCategoryToCategorizationScheme(
				representationService.getTermWithError(categorizationSchemeTermRId),
				categorizationService.getCategoryWithError(representationService.getTermWithError(categoryTermRId)
						.getObjectType().getId()));
	}

	@Override
	@Transactional
	public Term addCategorizedConceptToCategorizationScheme(String categorizationSchemeTermRId,
			String categorizedConceptTermRId) {

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Defense.notEmpty(categorizedConceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizedConceptTermRId");

		return categorizationService.addCategorizedConceptToCategorizationScheme(representationService
				.getTermWithError(categorizationSchemeTermRId),
				representationService.getTermWithError(categorizedConceptTermRId).getMeaning());
	}

	@Override
	@Transactional
	public void removeCategory(String categoryTermRId) {
		Defense.notEmpty(categoryTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "categoryTermRId");

		categorizationService.removeCategory(representationService.getTermWithError(categoryTermRId));
	}

	@Override
	@Transactional
	public void removeCategorizationType(String categorizationTypeTermRId) {

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		categorizationService.removeCategorizationType(representationService
				.getTermWithError(categorizationTypeTermRId));
	}

	@Override
	@Transactional
	public Term removeCategoryFromCategorizationScheme(String categorizationSchemeTermRId, String categoryTermRId) {

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Defense.notEmpty(categoryTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "categoryTermRId");

		return categorizationService.removeCategoryFromCategorizationScheme(
				representationService.getTermWithError(categorizationSchemeTermRId),
				categorizationService.getCategoryWithError(representationService.getTermWithError(categoryTermRId)
						.getObjectType().getId()));
	}

	@Override
	@Transactional
	public Term removeCategorizedConceptFromCategorizationScheme(String categorizationSchemeTermRId,
			String categorizedConceptTermRId) {

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Defense.notEmpty(categorizedConceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizedConceptTermRId");

		return categorizationService.removeCategorizedConceptFromCategorizationScheme(representationService
				.getTermWithError(categorizationSchemeTermRId),
				representationService.getTermWithError(categorizedConceptTermRId).getMeaning());
	}

	@Override
	@Transactional
	public Term promoteObjectTypeToCategory(String objectTypeTermRId, String categorizationSchemeTermRId,
			String categorizationTypeTermRId) {

		Defense.notEmpty(categorizationSchemeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationSchemeTermRId");

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.CATEGORIZATION_TYPE_NULL,
				DGCErrorCodes.CATEGORIZATION_TYPE_EMPTY, "categorizationTypeTermRId");

		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");

		return categorizationService
				.promoteObjectTypeToCategory(
						representationService.getTermWithError(objectTypeTermRId),
						representationService.getTermWithError(categorizationSchemeTermRId),
						categorizationService.getCategorizationTypeWithError(representationService
								.getTermWithError(categorizationTypeTermRId).getObjectType().getId())).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term promoteObjectTypeToCategoryForConcept(String objectTypeTermRId, String forconceptTermRId,
			String categorizationTypeTermRId) {

		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");
		Defense.notEmpty(forconceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"forconceptTermRId");
		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		return categorizationService
				.promoteObjectTypeToCategoryForConcept(
						representationService.getTermWithError(objectTypeTermRId),
						representationService.getTermWithError(forconceptTermRId).getMeaning(),
						categorizationService.getCategorizationTypeWithError(representationService
								.getTermWithError(categorizationTypeTermRId).getObjectType().getId())).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term promoteObjectTypeToCategory(String objectTypeTermRId, String categorizationTypeTermRId) {

		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");
		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		return categorizationService
				.promoteObjectTypeToCategory(
						representationService.getTermWithError(objectTypeTermRId),
						categorizationService.getCategorizationTypeWithError(representationService
								.getTermWithError(categorizationTypeTermRId).getObjectType().getId())).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term demoteCategoryToObjectType(String categoryTermRId) {

		Defense.notEmpty(categoryTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "categoryTermRId");

		return categorizationService
				.demoteCategoryToObjectType(representationService.getTermWithError(categoryTermRId)).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term promoteObjectTypeToCategorizationType(String objectTypeTermRId, String forconceptTermRId) {

		Defense.notEmpty(forconceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"forconceptTermRId");
		Defense.notEmpty(objectTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"objectTypeTermRId");

		return categorizationService
				.promoteObjectTypeToCategorizationType(representationService.getTermWithError(objectTypeTermRId),
						representationService.getTermWithError(forconceptTermRId).getObjectType()).getTerms()
				.iterator().next();
	}

	@Override
	@Transactional
	public Term demoteCategorizationTypeToObjectType(String categorizationTypeTermRId) {

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		return categorizationService
				.demoteCategorizationTypeToObjectType(representationService.getTermWithError(categorizationTypeTermRId))
				.getTerms().iterator().next();
	}

	@Override
	@Transactional
	public Collection<Category> getCategories(String categorizationTypeTermRId) {

		Defense.notEmpty(categorizationTypeTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizationTypeTermRId");

		return categorizationService.findCategories(categorizationService
				.getCategorizationTypeWithError(representationService.getTermWithError(categorizationTypeTermRId)
						.getObjectType().getId()));
	}

	@Override
	@Transactional
	public Collection<CategorizationType> getCategorizationTypes(String conceptTermRId) {

		Defense.notEmpty(conceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "conceptTermRId");

		return categorizationService.findCategorizationTypes(representationService.getTermWithError(conceptTermRId)
				.getMeaning());
	}

	@Override
	@Transactional
	public Collection<Term> getSchemesForCategory(String categoryTermRId) {

		Defense.notEmpty(categoryTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "categoryTermRId");

		Collection<Concept> schemes = categorizationService.findSchemes(categorizationService
				.getCategoryWithError(representationService.getTermWithError(categoryTermRId).getObjectType().getId()));

		if (schemes == null || schemes.isEmpty())
			return new ArrayList<Term>();

		List<Term> schemeTerms = new ArrayList<Term>(schemes.size());

		for (Concept scheme : schemes) {

			ObjectType schemeOT = (ObjectType) scheme;

			schemeTerms.add(schemeOT.findPreferredTerm());
		}

		return schemeTerms;
	}

	@Override
	@Transactional
	public Collection<Term> getSchemesForConcept(String categorizedConceptTermRId) {

		Defense.notEmpty(categorizedConceptTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY,
				"categorizedConceptTermRId");

		Collection<Concept> schemes = categorizationService.findSchemesForConcept(representationService
				.getTermWithError(categorizedConceptTermRId).getMeaning());

		if (schemes == null || schemes.isEmpty())
			return new ArrayList<Term>();

		List<Term> schemeTerms = new ArrayList<Term>(schemes.size());

		for (Concept scheme : schemes) {

			ObjectType schemeOT = (ObjectType) scheme;

			schemeTerms.add(schemeOT.findPreferredTerm());
		}

		return schemeTerms;
	}

	@Override
	@Transactional
	public List<Term> getRootCategorizedConceptsForCategorizationScheme(String categorizationSchemeRId,
			String vocabularyRId) {
		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(categorizationSchemeRId, DGCErrorCodes.CATEGORIZATION_SCHEME_ID_NULL,
				DGCErrorCodes.CATEGORIZATION_SCHEME_ID_EMPTY, "categorizationSchemeRId");

		return categorizationService.findRootCategorizedConceptsForCategorizationScheme(
				meaningService.getConceptWithError(categorizationSchemeRId),
				representationService.getVocabularyWithError(vocabularyRId));
	}
}
