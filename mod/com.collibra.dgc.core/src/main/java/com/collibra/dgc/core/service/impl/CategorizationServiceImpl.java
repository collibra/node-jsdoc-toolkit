package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CategorizationTypeDao;
import com.collibra.dgc.core.dao.CategoryDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.categorizations.CategorizationFactory;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.TermEventAdapter;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;
import com.collibra.dgc.core.util.SortUtil;

@Service
public class CategorizationServiceImpl extends AbstractService implements CategorizationService {
	private final Logger log = LoggerFactory.getLogger(CategorizationServiceImpl.class);

	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategorizationTypeDao categorizationTypeDao;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private MeaningService meaningService;
	@Autowired
	private CategorizationFactory categorizationFactory;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private CategorizationServiceHelperImpl categorizationHelper;
	@Autowired
	private MeaningFactory meaningFactory;

	public CategorizationServiceImpl() {
		ObservationManager.getInstance().register(new TermEventHandler(), 0, GlossaryEventCategory.TERM);
	}

	@Override
	public CategorizationType createCategorizationType(String name, Term forConcept, Vocabulary vocabulary) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Find categorization types for the concept if exists with the same signifier for reuse.
		Term existingTerm = null;
		for (Term term : findCategorizationTypeTermsForConcept(forConcept.getObjectType())) {
			if (term.getSignifier().equals(name)) {
				// Categorization type already exists...
				throw new ConstraintViolationException("Categorization type '" + name + "' for concept '" + forConcept
						+ "' already exists", name, CategorizationType.class.getName(),
						DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
			}
		}

		// Now try finding the categorization types from all the taxonomical general concepts of the concept being
		// classified to see if the type with same signifier can be reused.
		if (existingTerm == null) {
			// get a list of all general concepts
			for (Representation rep : representationService.findAllGeneralConceptRepresentation(forConcept)) {
				rep = ServiceUtility.deproxy(rep, Representation.class);
				if (rep instanceof Term) {
					// If the categorization type for the concept is already available then reuse it.
					for (Term term : findCategorizationTypeTermsForConcept(((Term) rep).getObjectType())) {
						if (term.getSignifier().equals(name)) {
							// Found type, reuse it.
							return (CategorizationType) ServiceUtility.deproxy(term.getObjectType(), ObjectType.class);
						}
					}
				}
			}
		}

		// Since no categorization type found that can be reused. Next is to check if there exists a term with the
		// signifier in the current vocabulary.
		// Rule 1: If the term with same signifier does not exist then create new categorization type.
		// Rule 2: If the term exists and is already a categorization type or it has some other type associated then the
		// new categorization scheme cannot be created.
		// Rule 3: If the term exists and is not a categorization type and has no other type associated then promote it
		// to categorization type and reuse.
		existingTerm = vocabulary.getTerm(name);
		if (existingTerm == null) {
			// Check for authorization.
			authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_TERM,
					DGCErrorCodes.RESOURCE_NO_PERMISSION);

			// Rule 1: Create new categorization type.
			CategorizationType type = categorizationFactory.createCategorizationType(vocabulary, name,
					forConcept.getObjectType());

			// Add history entry to the term.
			return commit(type);
		} else {
			if (existingTerm.equals(forConcept)) {
				String message = "The term '" + forConcept.verbalise() + "' cannot be categorization type for itself.";
				log.error(message);
				throw new ConstraintViolationException(message, existingTerm.getId(),
						CategorizationType.class.getName(), DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
			}

			ObjectType metaot = objectTypeDao.getMetaCategorizationType();
			if (metaot.equals(existingTerm.getObjectType().getType())) {
				// Rule 2: Cannot reuse and create new type.
				String message = "CategorizationType with name '" + name + "' exists in current vocabulary.";
				log.error(message);
				throw new ConstraintViolationException(message, existingTerm.getId(),
						CategorizationType.class.getName(), DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
			} else {
				ObjectType metaThing = objectTypeDao.getMetaThing();
				ObjectType metaOT = objectTypeDao.getMetaObjectType();

				ObjectType existingType = existingTerm.getObjectType().getType();
				if (existingType != null && (!metaThing.equals(existingType) && !metaOT.equals(existingType))) {
					// Rule 2: Cannot reuse and create new type.
					String message = "Term with name '" + name
							+ "' exists in current vocabulary and associated with another type.";
					log.error(message);
					throw new ConstraintViolationException(message, existingTerm.getId(),
							CategorizationType.class.getName(), DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
				}

				// Rule 3: Promote existing term to become categorization type.
				return promoteObjectTypeToCategorizationTypeInternal(existingTerm, forConcept.getObjectType());
			}
		}
	}

	@Override
	public Category createCategory(String name, CategorizationType catType, Vocabulary vocabulary) {

		return createCategoryInternal(name, null, catType, vocabulary);
	}

	@Override
	public Category createCategory(String name, Term catSchemeTerm, CategorizationType catType, Vocabulary vocabulary) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), catSchemeTerm, Permissions.TERM_ASSIGN_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return createCategoryInternal(name, catSchemeTerm, catType, vocabulary);
	}

	private Category createCategoryInternal(String name, Term catSchemeTerm, CategorizationType catType,
			Vocabulary vocabulary) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Term existingTerm = findCategoryTermIfExistsWithValidation(name, catType, vocabulary);

		if (existingTerm != null) {
			if (catType.getTerms().contains(existingTerm)) {
				String message = "The term '" + existingTerm.verbalise() + "' cannot be category for itself.";
				log.error(message);
				throw new ConstraintViolationException(message, existingTerm.getId(), Category.class.getName(),
						DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF);
			}

			// Promote the existing term to category.
			if (catSchemeTerm == null) {
				return promoteObjectTypeToCategoryInternal(existingTerm, catType);
			} else {
				return promoteObjectTypeToCategoryInternal(existingTerm, catSchemeTerm, catType);
			}
		}

		// Check for authorization.
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_TERM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Create new one.
		Category category = null;
		if (catSchemeTerm == null) {
			category = categorizationFactory.createCategory(name, catType, vocabulary);
		} else {
			category = categorizationFactory.createCategory(name, catSchemeTerm.getMeaning(), catType, vocabulary);
		}

		return commit(category);
	}

	@Override
	public Category createCategoryForConcept(String name, Concept forConcept, CategorizationType catType,
			Vocabulary vocabulary) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Term existingTerm = findCategoryTermIfExistsWithValidation(name, catType, vocabulary);

		if (existingTerm != null) {
			if (forConcept.equals(existingTerm.getObjectType())) {
				String message = "The term '" + existingTerm.verbalise() + "' cannot be category for itself.";
				log.error(message);
				throw new ConstraintViolationException(message, existingTerm.getId(), Category.class.getName(),
						DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF);
			}

			// Promote the existing term to category.
			return promoteObjectTypeToCategoryForConceptInternal(existingTerm, forConcept, catType);
		}

		checkConstraint(forConcept, catType);

		// Check for authorization.
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_TERM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Create new one.
		Category category = categorizationFactory.createCategoryForConcept(name, forConcept, catType, vocabulary);
		return commit(category);
	}

	private void checkConstraint(Concept forConcept, CategorizationType catType) {
		if (!isTaxonomicalParent(forConcept, catType)) {
			String message = "Concept '" + forConcept.getRepresentations().iterator().next().verbalise()
					+ "' must be taxonomical child of categorization type's classifying concept '"
					+ catType.getIsForConcept().getRepresentations().iterator().next().verbalise() + "'";
			log.error(message);
			throw new ConstraintViolationException(message, forConcept.getId(), Concept.class.getName(),
					DGCErrorCodes.CATEGORY_GENERAL_CONCEPT_NOT_TAXONOMICAL_CHILD_OF_CAT_TYPE_FOR_CONCEPT);
		}
	}

	private boolean isTaxonomicalParent(Concept forConcept, CategorizationType catType) {
		boolean isTaxonomicalParent = false;
		Concept catTypeCalssificationConcept = catType.getIsForConcept();
		if (catTypeCalssificationConcept.equals(forConcept)) {
			isTaxonomicalParent = true;
		}

		if (!isTaxonomicalParent) {
			// Check if the categorization type's classifying concept is taxonomical parent of for concept.
			for (Concept parentConcept : meaningService.findTaxonomicalParentConcepts(forConcept)) {
				if (parentConcept.equals(catTypeCalssificationConcept)) {
					isTaxonomicalParent = true;
					break;
				}
			}
		}

		return isTaxonomicalParent;
	}

	private Term findCategoryTermIfExistsWithValidation(String name, CategorizationType catType, Vocabulary vocabulary) {
		// If there is a category with specified signifier exists for the categorization type then return it.
		for (Term term : findCategoryTerms(catType, vocabulary)) {
			if (term.getSignifier().equals(name)) {
				return term;
			}
		}

		// Get existing term with same signifier from the vocabulary.
		Term existingTerm = vocabulary.getTerm(name);
		if (existingTerm != null) {
			ObjectType metaThing = objectTypeDao.getMetaThing();
			ObjectType metaOT = objectTypeDao.getMetaObjectType();

			if (!metaThing.equals(existingTerm.getObjectType().getGeneralConcept())
					|| !metaOT.equals(existingTerm.getObjectType().getType())) {
				String message = "Term with name '" + name
						+ "' exists in current vocabulary and associated with another type.";
				log.error(message);
				throw new ConstraintViolationException(message, name, Category.class.getName(),
						DGCErrorCodes.CATEGORY_TERM_ALREADY_EXISTS);
			}
		}

		return existingTerm;
	}

	@Override
	public Category commit(Category category) {
		category = meaningService.saveAndCascade(category);
		commitRepresentations(category);
		return category;
	}

	@Override
	public CategorizationType commit(CategorizationType catType) {
		catType = meaningService.saveAndCascade(catType);
		commitRepresentations(catType);
		return catType;
	}

	@Override
	public Term commitScheme(Term catSchemeTerm) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), catSchemeTerm, Permissions.TERM_ASSIGN_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Concept catScheme = meaningService.saveConcept(catSchemeTerm.getMeaning());

		Set<Representation> representations = new HashSet<Representation>();
		for (Category category : catScheme.getCategories()) {
			representations.addAll(category.getTerms());
			representations.addAll(category.getType().getTerms());
		}

		representations.addAll(catScheme.getRepresentations());

		Set<Vocabulary> vocabularies = new HashSet<Vocabulary>();
		for (Representation rep : representations) {
			vocabularies.add(rep.getVocabulary());
		}

		for (Vocabulary voc : vocabularies) {
			representationService.saveVocabulary(voc);
		}

		return catSchemeTerm;
	}

	private void commitRepresentations(ObjectType ot) {

		Set<Vocabulary> vocabularies = new HashSet<Vocabulary>();
		for (Term term : ot.getTerms()) {
			vocabularies.add(term.getVocabulary());
		}

		for (Vocabulary voc : vocabularies) {
			representationService.saveVocabulary(voc);
		}
	}

	@Override
	public Category promoteObjectTypeToCategory(Term otTerm, CategorizationType catType) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), otTerm.getVocabulary(), Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return promoteObjectTypeToCategoryInternal(otTerm, catType);
	}

	private Category promoteObjectTypeToCategoryInternal(Term otTerm, CategorizationType catType) {

		final ObjectType ot = otTerm.getObjectType();

		if (catType.getIsForConcept().equals(ot)) {
			String message = "The concept '" + ot + "' cannot be category for itself.";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), Category.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF);
		}

		// check if ot is not yet a categorization type or category
		if (findCategorizationTypeByResourceId(ot.getId()) != null) {
			String message = "Term exists as categorization type";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
		}

		if (findCategoryByResourceId(ot.getId()) != null) {
			String message = "Term exists as category";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_ALREADY_EXISTS);
		}

		Category category = categorizationFactory.createCategory(ot, catType);
		commit(category);
		final Set<Term> terms = ot.getTerms();
		for (final Term term : terms) {
			term.setMeaning(category);
			representationService.save(term);
		}
		meaningService.remove(ot);
		return category;
	}

	@Override
	public Category promoteObjectTypeToCategory(Term otTerm, Term catSchemeTerm, CategorizationType catType) {

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), otTerm.getVocabulary(), Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);
		authorizationHelper.checkAuthorization(getCurrentUser(), catSchemeTerm, Permissions.TERM_ASSIGN_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return promoteObjectTypeToCategoryInternal(otTerm, catSchemeTerm, catType);
	}

	private Category promoteObjectTypeToCategoryInternal(Term otTerm, Term catSchemeTerm, CategorizationType catType) {

		final ObjectType ot = otTerm.getObjectType();

		if (catType.getIsForConcept().equals(ot)) {
			String message = "The concept '" + ot + "' cannot be category for itself.";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), Category.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF);
		}

		// check if ot is not yet a categorization type or category
		Defense.assertTrue(findCategorizationTypeByResourceId(ot.getId()) == null,
				DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
		Defense.assertTrue(findCategoryByResourceId(ot.getId()) == null, DGCErrorCodes.CATEGORY_TERM_ALREADY_EXISTS);

		Category category = categorizationFactory.createCategory(ot, catSchemeTerm.getMeaning(), catType);
		commit(category);
		final Set<Term> terms = ot.getTerms();
		for (final Term term : terms) {
			term.setMeaning(category);
			representationService.save(term);
		}
		meaningService.remove(ot);
		return category;
	}

	@Override
	public Category promoteObjectTypeToCategoryForConcept(Term otTerm, Concept forConcept, CategorizationType catType) {

		// Check Authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), otTerm.getVocabulary(), Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return promoteObjectTypeToCategoryForConceptInternal(otTerm, forConcept, catType);
	}

	private Category promoteObjectTypeToCategoryForConceptInternal(Term otTerm, Concept forConcept,
			CategorizationType catType) {

		final ObjectType ot = otTerm.getObjectType();

		if (forConcept.equals(ot)) {
			String message = "The concept '" + ot + "' cannot be category for itself.";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), Category.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF);
		}

		// check if ot is not yet a categorization type or category
		if (findCategorizationTypeByResourceId(ot.getId()) != null) {
			String message = "Term exists as categorization type";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
		}

		if (findCategoryByResourceId(ot.getId()) != null) {
			String message = "Term exists as category";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_ALREADY_EXISTS);
		}

		checkConstraint(forConcept, catType);
		Category category = categorizationFactory.createCategory(ot, forConcept, catType);
		commit(category);
		final Set<Term> terms = ot.getTerms();
		for (final Term term : terms) {
			term.setMeaning(category);
			representationService.save(term);
		}
		meaningService.remove(ot);
		return category;
	}

	@Override
	public ObjectType demoteCategoryToObjectType(Term categoryTerm) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categoryTerm.getVocabulary(),
				Permissions.VOCABULARY_REMOVE_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Category category = findCategoryByResourceId(categoryTerm.getObjectType().getId());

		ObjectType ot = meaningFactory.makeObjectType(category);
		ot.setType(null);

		ot = meaningService.saveAndCascade(ot);

		// From each of the scheme remove the category.
		for (Concept scheme : findSchemes(category)) {
			scheme.removeCategory(category);
			conceptDao.save(scheme);
		}

		// For all terms set the type to ObjectType
		for (Term term : category.getTerms()) {

			term.setObjectType(ot);
			representationService.save(term);
		}

		meaningService.remove(category);
		return ot;
	}

	@Override
	public CategorizationType promoteObjectTypeToCategorizationType(Term otTerm, ObjectType forConcept) {

		// Check for authorization.
		authorizationHelper.checkAuthorization(getCurrentUser(), otTerm.getVocabulary(), Permissions.VOCABULARY_ADD_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return promoteObjectTypeToCategorizationTypeInternal(otTerm, forConcept);
	}

	private CategorizationType promoteObjectTypeToCategorizationTypeInternal(Term otTerm, ObjectType forConcept) {

		final ObjectType ot = otTerm.getObjectType();

		if (ot.equals(forConcept)) {
			String message = "The concpet '" + forConcept + "' cannot be categorization type for itself.";
			log.error(message);
			throw new ConstraintViolationException(message, forConcept.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CONCEPT_CANNOT_BE_CATEGORIZATION_TYPE_FOR_SELF);
		}

		// check if ot is not yet a categorization type or category
		if (findCategorizationTypeByResourceId(ot.getId()) != null) {
			String message = "Term exists as categorization type";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS);
		}

		if (findCategoryByResourceId(ot.getId()) != null) {
			String message = "Term exists as category";
			log.error(message);
			throw new ConstraintViolationException(message, ot.getId(), CategorizationType.class.getName(),
					DGCErrorCodes.CATEGORY_TERM_ALREADY_EXISTS);
		}

		CategorizationType type = categorizationFactory.createCategorizationType(ot, forConcept);
		commit(type);
		final Set<Term> terms = ot.getTerms();
		for (final Term term : terms) {
			term.setMeaning(type);
			representationService.save(term);
		}
		meaningService.remove(ot);
		return type;
	}

	@Override
	public ObjectType demoteCategorizationTypeToObjectType(Term catTypeTerm) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), catTypeTerm.getVocabulary(),
				Permissions.VOCABULARY_REMOVE_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		CategorizationType catType = findCategorizationTypeByResourceId(catTypeTerm.getObjectType().getId());

		ObjectType ot = meaningFactory.makeObjectType(catType);
		ot.setType(null);

		ot = meaningService.saveAndCascade(ot);

		// For all terms set the type to ObjectType
		for (Term term : catType.getTerms()) {

			term.setObjectType(ot);
			representationService.save(term);
		}
		removeInternal(catType);
		return ot;
	}

	@Override
	public Term addCategoryToCategorizationScheme(Term categorizationSchemeTerm, Category category) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categorizationSchemeTerm, Permissions.TERM_ASSIGN_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Concept categorizationScheme = categorizationSchemeTerm.getMeaning();

		// we need to move up in the tree. If a category is selected, all it's parents - if they are also categories -
		// should also be selected
		addParentCategoriesToCategorizationScheme(categorizationScheme, category);

		categorizationScheme.addCategory(category);
		conceptDao.save(categorizationScheme);

		return categorizationSchemeTerm;
	}

	protected void addParentCategoriesToCategorizationScheme(Concept categorizationScheme, Category category) {

		Concept parent = category.getGeneralConcept();

		Category parentCat = this.findCategoryByResourceId(parent.getId());

		if (parentCat != null) {
			categorizationScheme.addCategory(parentCat);
			addParentCategoriesToCategorizationScheme(categorizationScheme, parentCat);
		}
	}

	@Override
	public Term removeCategoryFromCategorizationScheme(Term categorizationSchemeTerm, Category category) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categorizationSchemeTerm,
				Permissions.TERM_REMOVE_ASSIGNED_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Concept categorizationScheme = categorizationSchemeTerm.getMeaning();

		boolean removed = false;

		// also remove all child-categories
		for (Concept c : meaningService.findAllSpecializedConcepts(category)) {
			Category subCategory = findCategoryByResourceId(c.getId());
			if (subCategory != null)
				if (categorizationScheme.removeCategory(subCategory))
					removed = true;
		}

		if (categorizationScheme.removeCategory(category))
			removed = true;

		if (removed)
			categorizationScheme = conceptDao.save(categorizationScheme);

		return categorizationSchemeTerm;
	}

	@Override
	public Term addCategorizedConceptToCategorizationScheme(Term categorizationSchemeTerm, Concept categorizedConcept) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categorizationSchemeTerm, Permissions.TERM_ASSIGN_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Concept categorizationScheme = categorizationSchemeTerm.getMeaning();

		categorizationScheme.addCategorizedConcept(categorizedConcept);
		conceptDao.save(categorizationScheme);

		return categorizationSchemeTerm;
	}

	@Override
	public Term removeCategorizedConceptFromCategorizationScheme(Term categorizationSchemeTerm,
			Concept categorizedConcept) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categorizationSchemeTerm,
				Permissions.TERM_REMOVE_ASSIGNED_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Concept categorizationScheme = categorizationSchemeTerm.getMeaning();

		boolean removed = false;
		// call remove on all "children" of the categorized Concept and remove selected categories that belong to the
		// removed categorized concepts
		for (Concept c : meaningService.findAllSpecializedConcepts(categorizedConcept)) {
			categorizationScheme.removeCategorizedConcept(c);
			Category category = findCategoryByResourceId(c.getId());
			// if the concept is also a category, it might be part of the scheme under the categorized concept that we
			// removed. So try to remove the category.
			if (category != null)
				categorizationScheme.removeCategory(category);
		}

		// only remove when it is contained
		categorizationScheme.removeCategorizedConcept(categorizedConcept);

		conceptDao.save(categorizationScheme);

		return categorizationSchemeTerm;
	}

	@Override
	public Concept findSchemeByResourceId(String resourceId) {
		return conceptDao.findCategorizationSchemeByResourceId(resourceId);
	}

	@Override
	public Category findCategoryByResourceId(String resourceId) {
		return categoryDao.findById(resourceId);
	}

	@Override
	public Term findCategorizationTypeTerm(Vocabulary vocabulary, CategorizationType categorizationType) {
		return representationService.findPreferredTermInAllIncorporatedVocabularies(categorizationType, vocabulary);
	}

	@Override
	public Term findCategoryTerm(Vocabulary vocabulary, Category category) {
		return representationService.findPreferredTermInAllIncorporatedVocabularies(category, vocabulary);
	}

	@Override
	public CategorizationType findCategorizationTypeByResourceId(String resourceId) {
		return categorizationTypeDao.findById(resourceId);
	}

	@Override
	public Collection<CategorizationType> findCategorizationTypes(Concept categorizedConcept) {
		return categorizationTypeDao.findForConcept(categorizedConcept);
	}

	@Override
	public Collection<Concept> findSchemesForConcept(Concept categorizedConcept) {
		return conceptDao.findCategorizationSchemesForConcept(categorizedConcept);
	}

	@Override
	public List<Term> findCategorizationTypeTermsForConcept(Concept concept) {
		return categorizationHelper.findCategorizationTypeTermsForConcept(concept);
	}

	@Override
	public List<Term> findCategoriesTermsForConcept(Concept concept) {
		return categorizationHelper.findCategoriesTermsForConcept(concept);
	}

	@Override
	public Map<Term, List<Term>> getCategorizationTypeTermToCategoriesTermMapForConcept(Concept concept) {
		HashMap<CategorizationType, Term> categoryTypeToTypeTermMap = new HashMap<CategorizationType, Term>();
		Map<Term, List<Term>> result = new TreeMap<Term, List<Term>>();

		for (CategorizationType catType : findCategorizationTypes(concept)) {
			Term ct = representationService.findPreferredTerm(catType);
			result.put(ct, new LinkedList<Term>());
		}

		List<Term> categoryTerms = findCategoriesTermsForConcept(concept);
		for (Term t : categoryTerms) {
			ObjectType catType = t.getObjectType().getType();
			Term ct = categoryTypeToTypeTermMap.get(catType);
			if (ct == null) {
				ct = representationService.findPreferredTerm(catType);
				categoryTypeToTypeTermMap.put(findCategorizationTypeByResourceId(catType.getId()), ct);
				List<Term> catList = new LinkedList<Term>();
				catList.add(t);
				result.put(ct, catList);
			} else {
				result.get(ct).add(t);
			}
		}

		// sort results ascending
		SortUtil su = new SortUtil();
		for (Entry<Term, List<Term>> entry : result.entrySet()) {
			su.sortRepresentations(entry.getValue(), true);
		}

		return result;
	}

	@Override
	public boolean isCategory(Term term) {
		if (term == null) {
			return false;
		}

		return isCategory(term.getObjectType());
	}

	@Override
	public boolean isCategory(ObjectType ot) {
		if (ot == null) {
			return false;
		}

		ObjectType concreteObject = ServiceUtility.deproxy(ot, ObjectType.class);
		return concreteObject instanceof Category;
	}

	@Override
	public boolean isCategorizationType(Term term) {
		if (term == null) {
			return false;
		}

		return isCategorizationType(term.getObjectType());
	}

	@Override
	public boolean isCategorizationType(ObjectType ot) {
		if (ot == null) {
			return false;
		}

		ObjectType concreteObject = ServiceUtility.deproxy(ot, ObjectType.class);
		return concreteObject instanceof CategorizationType;
	}

	@Override
	public Collection<Term> findCategoryTerms(Vocabulary vocabulary) {
		List<Term> categoryTerms = new LinkedList<Term>();
		if (vocabulary != null) {
			for (Term term : vocabulary.getTerms()) {
				if (isCategory(term)) {
					categoryTerms.add(term);
				}
			}
		}

		return (Collection<Term>) new SortUtil().sortRepresentations(categoryTerms, true);
	}

	@Override
	public Collection<Concept> findSchemes(Category category) {
		return conceptDao.findCategorizationSchemes(category);
	}

	@Override
	public Collection<Category> findCategories(CategorizationType catType) {
		return categoryDao.find(catType);
	}

	@Override
	public List<Term> findCategoryTerms(CategorizationType catType, Vocabulary voc) {
		if (voc == null) {
			return new LinkedList<Term>();
		}

		Set<Vocabulary> vocs = voc.getAllNonSbvrIncorporatedVocabularies();
		vocs.add(voc);

		List<Term> terms = categoryDao.findCategoryTerms(catType, vocs);
		return (List<Term>) new SortUtil().sortRepresentations(terms, true);
	}

	// TODO: This should be optimized - ideally in one hibernate query
	@Override
	public List<Term> findRootCategorizedConceptsForCategorizationScheme(Concept categorizationScheme, Vocabulary voc) {

		Set<Concept> categorizedConcepts = categorizationScheme.getConceptsCategorized();
		Set<String> resultConcepts = new HashSet<String>();
		for (Concept c : categorizedConcepts)
			resultConcepts.add(c.getId());

		for (Concept categorizedConcept : categorizedConcepts) {
			for (Concept specializedConcept : meaningService.findAllSpecializedConcepts(categorizedConcept))
				resultConcepts.remove(specializedConcept.getId());
		}

		List<Term> result = new ArrayList<Term>();
		for (String categorizedConceptRID : resultConcepts) {
			ObjectType categorizedConcept = (ObjectType) meaningService.findConceptByResourceId(categorizedConceptRID);
			Term term = categorizedConcept.findPreferredTerm();

			if (term != null)
				result.add(term);
		}

		return (List<Term>) new SortUtil().sortRepresentations(result, true);
	}

	@Override
	public void removeCategory(Term categoryTerm) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), categoryTerm.getVocabulary(),
				Permissions.VOCABULARY_REMOVE_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Category category = findCategoryByResourceId(categoryTerm.getObjectType().getId());

		// Remove all terms associated with the meaning Category.
		for (Term term : new LinkedList<Term>(category.getTerms())) {
			representationService.remove(term);
		}

		if (findCategoryByResourceId(category.getId()) != null) {
			categorizationHelper.removeInternal(category);
		}

		// Removed all the categorization types if the concept is further categorized.
		removeCategorizationForConcept(category);
	}

	@Override
	public void removeCategorizationType(Term catTypeTerm) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), catTypeTerm.getVocabulary(),
				Permissions.VOCABULARY_REMOVE_CAT, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		CategorizationType catType = findCategorizationTypeByResourceId(catTypeTerm.getObjectType().getId());

		final Set<ObjectType> meanings = new HashSet<ObjectType>();
		// Remove all terms.
		for (Term term : new LinkedList<Term>(catType.getTerms())) {
			if (representationService.findTermByResourceId(term.getId()) != null) {
				meanings.add(term.getObjectType());
				representationService.remove(term);
			}
		}

		removeInternal(catType);
	}

	private void removeInternal(CategorizationType catType) {
		if (categorizationTypeDao.findById(catType.getId()) == null) {
			return;
		}

		categorizationHelper.cleanupCategories(catType);

		getCurrentSession().delete(catType);

		getCurrentSession().flush();
	}

	private void removeSchemeInternal(Concept catScheme) {
		if (conceptDao.findById(catScheme.getId()) == null) {
			return;
		}
		getCurrentSession().delete(catScheme);
	}

	private void removeCategorizationForConcept(Concept categorizedConcept) {
		// Remove categorization types.
		for (CategorizationType catType : findCategorizationTypes(categorizedConcept)) {
			removeCategorizationType(catType.findPreferredTerm());
		}

		categorizationHelper.cleanupSchemes(categorizedConcept);
	}

	@Override
	public void removeCategorizationRelations(Term term) {

		// Check for authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE_ASSIGNED_CAT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		ObjectType ot = term.getObjectType();

		// If there are other terms referring to this meaning then don't need any action.
		if (ot.getTerms().size() > 0) {
			return;
		}

		ot = ServiceUtility.deproxy(ot, ObjectType.class);
		if (ot instanceof Category) {
			categorizationHelper.removeInternal((Category) ot);
		} else if (ot instanceof CategorizationType) {
			removeInternal((CategorizationType) ot);
		}

		getCurrentSession().flush();

		// If it is a categorization scheme and no other terms are are referring to this meaning then just set as
		// not latest.
		if (ot.isCategorizationScheme()) {
			removeSchemeInternal(ot);
		}

		// If the removed term is classified then remove all categorization types.
		removeCategorizationForConcept(ot);
	}

	@Override
	public CategorizationType getCategorizationTypeWithError(String resourceId) {

		CategorizationType catType = findCategorizationTypeByResourceId(resourceId);

		if (catType == null) {
			String message = "Categorization type with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.CATEGORIZATION_TYPE_NOT_FOUND, resourceId);
		}

		return catType;
	}

	@Override
	public Category getCategoryWithError(String resourceId) {

		Category cat = findCategoryByResourceId(resourceId);
		if (cat == null) {
			String message = "Category with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.CATEGORY_NOT_FOUND, resourceId);
		}

		return cat;
	}

	/**
	 * {@link Term} event listner to handle the removal/deletion of the term.
	 * 
	 */
	class TermEventHandler extends TermEventAdapter {
		@Override
		public void onRemove(TermEventData data) {
			Term term = data.getTerm();
			if (term != null) {
				removeCategorizationRelations(term);
			}
		}
	}
}