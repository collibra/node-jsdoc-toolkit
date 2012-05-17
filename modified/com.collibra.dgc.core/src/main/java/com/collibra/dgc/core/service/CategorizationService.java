package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Service for persisting and querying {@link CategorizationScheme}s and related objects.
 * @author dtrog
 * 
 */
public interface CategorizationService {

	/**
	 * Retrieves the latest version of the {@link CategorizationType} with the given UUID.
	 * @param resourceId The resource id
	 * @return the latest {@link CategorizationType} with this UUID.
	 */
	CategorizationType findCategorizationTypeByResourceId(String resourceId);

	/**
	 * Retrieves the categorization types (as {@link Term}s) for a given concept
	 * @param concept The concept for which to return the categorization types.
	 * @return a List of Terms that represent the categorization types
	 */
	List<Term> findCategorizationTypeTermsForConcept(Concept concept);

	/**
	 * Find all representations for the root categorized concepts for a given categorization scheme
	 * @param categorizationScheme the categorization scheme {@link Concept}.
	 * @param voc The {@link Vocabulary} in which to look for the preferred representations for the returned concepts
	 * @return a List of {@link Term}s that represent the root categorized concepts for the given categorization scheme.
	 */
	List<Term> findRootCategorizedConceptsForCategorizationScheme(Concept categorizationScheme, Vocabulary voc);

	/**
	 * Find the {@link Term}s that represent the {@link Category}s of specified {@link CategorizationType}.
	 * @param catType The {@link CategorizationType}.
	 * @param voc The {@link Vocabulary} to start looking for the terms
	 * @return The Terms that represent the {@link Category}s.
	 */
	List<Term> findCategoryTerms(CategorizationType catType, Vocabulary voc);

	/**
	 * Find the {@link Term}s that represent the {@link Category}s that belong to the given {@link Concept} forConcept
	 * @param forConcept The {@link Concept} to which the categories should belong
	 * @return a list of {@link Term}s that represent the {@link Category}s
	 */
	List<Term> findCategoriesTermsForConcept(Concept forConcept);

	/**
	 * Return a Map from {@link Term} that represents a {@link CategorizationType} to a List of {@link Term}s
	 * representing {@link Category}s that belong to the given {@link Concept} and the {@link CategorizationType}.
	 * @param concept
	 * @return
	 */
	Map<Term, List<Term>> getCategorizationTypeTermToCategoriesTermMapForConcept(Concept concept);

	/**
	 * Retrieves the latest version of Categorization Scheme {@link Concept} with the given resource id.
	 * @param resourceId The resource id
	 * @return The Categorization Scheme {@link Concept} with this resource id.
	 */
	Concept findSchemeByResourceId(String resourceId);

	/**
	 * Retrieves the latest version of the {@link Category} with the given UUID.
	 * @param resourceId The resource id
	 * @return the latest {@link Category} with this UUID.
	 */
	Category findCategoryByResourceId(String resourceId);

	/**
	 * Add an existing category to an existing categorization scheme
	 * @param categorizationSchemeTerm The categorization Scheme {@link Term} to which to add the category
	 * @param category The category {@link Category} to add
	 * @return The latest version of the categorization scheme {@link Term}
	 */
	Term addCategoryToCategorizationScheme(Term categorizationSchemeTerm, Category category);

	/**
	 * Add an existing {@link Concept} as a categorized concept to a categorization scheme {@link Concept}.
	 * @param categorizationSchemeTerm The categorization Scheme {@link Term} to which to add the categorized concept
	 * @param categorizedConcept The concept to add to the categorization scheme
	 * @return The latest version of the categorization scheme {@link Term}
	 */
	Term addCategorizedConceptToCategorizationScheme(Term categorizationSchemeTerm, Concept categorizedConcept);

	/**
	 * Persists a new version of the Categorization Scheme {@link Term}.
	 * @param catSchemeTerm The categorization scheme {@link Term}.
	 * @return The {@link Term}.
	 */
	Term commitScheme(Term catSchemeTerm);

	/**
	 * Find the preferred Term of the categorization scheme.
	 * @param vocabulary The {@link Vocabulary} in which to search for the term.
	 * @param categorizationType The {@link CategorizationType} to find the {@link Term} for.
	 * @return the preferred Term of the {@link CategorizationType}
	 */
	Term findCategorizationTypeTerm(Vocabulary vocabulary, CategorizationType categorizationType);

	/**
	 * Find the preferred Term of the {@link Category}.
	 * @param vocabulary The {@link Vocabulary} in which to search for the term.
	 * @param category The {@link Category} to find the {@link Term} for.
	 * @return the preferred Term of the {@link Categorization}
	 */
	Term findCategoryTerm(Vocabulary vocabulary, Category category);

	/**
	 * Check if the given {@link Term} is of type {@link Category}.
	 * @param term The {@link Term}
	 * @return True if {@link Category} otherwise false.
	 */
	boolean isCategory(Term term);

	/**
	 * Check if the given {@link ObjectType} is of type {@link Category}.
	 * @param ot The {@link ObjectType}.
	 * @return True if {@link Category} otherwise false.
	 */
	boolean isCategory(ObjectType ot);

	/**
	 * Check if the given {@link Term} is of type {@link CategorizationType}.
	 * @param term The {@link Term}.
	 * @return True if {@link CategorizationType} otherwise false.
	 */
	boolean isCategorizationType(Term term);

	/**
	 * Check if the given {@link ObjectType} is of type {@link CategorizationType}.
	 * @param ot The {@link ObjectType}.
	 * @return True if {@link CategorizationType} otherwise false.
	 */
	boolean isCategorizationType(ObjectType ot);

	/**
	 * Get all categorization scheme {@link Concept}s that include the specified {@link Category}.
	 * @param category The {@link Category}.
	 * @return The categorization scheme {@link Concept}s.
	 */
	Collection<Concept> findSchemes(Category category);

	/**
	 * Get {@link Category}s for all {@link Term}s in the specified {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary}.
	 * @return The {@link Category}s.
	 */
	Collection<Term> findCategoryTerms(Vocabulary vocabulary);

	/**
	 * Get all {@link Category}s that are of type specified {@link CategorizationType}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}s.
	 */
	Collection<Category> findCategories(CategorizationType catType);

	/**
	 * Get all {@link CategorizationType}s that classify the specified general {@link Concept}.
	 * @param categorizedConcept The general {@link Concept} that is classified.
	 * @return The {@link CategorizationType}s.
	 */
	Collection<CategorizationType> findCategorizationTypes(Concept categorizedConcept);

	/**
	 * Get all categorization scheme {@link Concept}s for the specified classified {@link Concept}.
	 * @param categorizedConcept The classified {@link Concept}.
	 * @return The categorization scheme {@link Concept}s.
	 */
	Collection<Concept> findSchemesForConcept(Concept categorizedConcept);

	/**
	 * Removes the specified {@link Category}. Note that all {@link Term}s having this {@link Category} as meaning will
	 * be removed as well.
	 * @param categoryTerm The {@link Term} that represents the {@link Category} to be removed
	 */
	void removeCategory(Term categoryTerm);

	/**
	 * Removes the specified {@link CategorizationType}. Note that all {@link Term}s having this
	 * {@link CategorizationType} and {@link Category}s of this type will be removed as well.
	 * @param catTypeTerm The{@link Term} that represents the {@link CategorizationType} to be removed
	 */
	void removeCategorizationType(Term catTypeTerm);

	/**
	 * Removes a category from a categorization scheme
	 * @param categorizationSchemeTerm The categorization scheme {@link Term}.
	 * @param category The category {@link Category}.
	 * @return The categorization scheme {@link Term}.
	 */
	Term removeCategoryFromCategorizationScheme(Term categorizationSchemeTerm, Category category);

	/**
	 * Remove categorized concept from categorization scheme.
	 * @param categorizationSchemeTerm The categorization scheme {@link Term}.
	 * @param categorizedConcept The categorized concept {@link Concept}.
	 * @return The categorization scheme {@link Term}.
	 */
	Term removeCategorizedConceptFromCategorizationScheme(Term categorizationSchemeTerm, Concept categorizedConcept);

	/**
	 * Removes all categorization relations (Category, CategorizationType, scheme, classified concept, etc) for the
	 * specified term being removed if this is the only {@link Term} available its {@link ObjectType} in the glossary.
	 * This is the possibility when removing a term without sending notifications.
	 * @param term The {@link Term} whose categorization relations need to be removed.
	 */
	void removeCategorizationRelations(Term term);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType}.
	 * @param name The signifier.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The vocabulary owning {@link Category}.
	 * @return The {@link Category}.
	 * @throws ConstraintViolationException if the {@link Term} with name exists in vocabulary with some other general
	 *             {@link Concept}.
	 */
	Category createCategory(String name, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType} and owned by specified
	 * categorization scheme.
	 * @param name The signifier.
	 * @param catScheme The categorization scheme {@link Term} owning the {@link Category}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The vocabulary owning {@link Category}.
	 * @return The {@link Category}.
	 * @throws ConstraintViolationException if the {@link Term} with name exists in vocabulary with some other general
	 *             {@link Concept}.
	 */
	Category createCategory(String name, Term catSchemeTerm, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType} and for specified concept.
	 * @param name The signifier.
	 * @param forConcept The classified {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The vocabulary owning {@link Category}.
	 * @return The {@link Category}.
	 * @throws ConstraintViolationException if the for {@link Concept} is not a taxonomical child of the
	 *             {@link CategorizationType}'s classifying {@link Concept}.
	 */
	Category createCategoryForConcept(String name, Concept forConcept, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Promote the {@link ObjectType} as {@link Category} of specified {@link CategorizationType}.
	 * @param otTerm The {@link Term} for which its {@link ObjectType} is being promoted to {@link Category}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}.
	 */
	Category promoteObjectTypeToCategory(Term otTerm, CategorizationType catType);

	/**
	 * Promote the {@link Term} {@link ObjectType} as {@link Category} of specified {@link CategorizationType} and add
	 * it to the specified categorization scheme.
	 * @param otTerm The {@link Term} for which its {@link ObjectType} is being promoted to {@link Category}.
	 * @param catScheme The categorization scheme {@link Term}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}.
	 */
	Category promoteObjectTypeToCategory(Term otTerm, Term catSchemeTerm, CategorizationType catType);

	/**
	 * Promote the {@link ObjectType} as {@link Category} of specified {@link CategorizationType} of specified
	 * classifying {@link Concept}.
	 * @param otTerm The {@link Term} for which its {@link ObjectType} is being promoted to {@link Category}.
	 * @param forConcept The classifying {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}.
	 * @throws ConstraintViolationException if the for {@link Concept} is not a taxonomical child of the
	 *             {@link CategorizationType}'s classifying {@link Concept}.
	 */
	Category promoteObjectTypeToCategoryForConcept(Term otTerm, Concept forConcept, CategorizationType catType);

	/**
	 * Demote the {@link Category} to {@link ObjectType} there by losing {@link CategorizationType} information.
	 * @param category The {@link Term} that represents the {@link Category} to be demoted.
	 * @return The {@link ObjectType}.
	 */
	ObjectType demoteCategoryToObjectType(Term categoryTerm);

	/**
	 * Create {@link CategorizationType} with specified name for the specified concept.
	 * @param name The signifier.
	 * @param forConcept The {@link Term} whose {@link Concept} is being classified.
	 * @param vocabulary The {@link Vocabulary} containing {@link CategorizationType} {@link Term}.
	 * @return The {@link CategorizationType}.
	 * @throws ConstraintViolationException if the {@link Term} with the {@link CategorizationType} name to be created
	 *             already exists in the {@link Vocabulary} and it a {@link CategorizationType} or has some other
	 *             {@link Concept} type.
	 */
	CategorizationType createCategorizationType(String name, Term forConcept, Vocabulary vocabulary);

	/**
	 * Promote the {@link Term} {@link ObjectType} as {@link CategorizationType} for specified concept.
	 * @param otTerm The {@link Term} for which its {@link ObjectType} is being promoted to {@link CategorizationType}.
	 * @param forConcept The concept being classified.
	 * @return The {@link CategorizationType}.
	 */
	CategorizationType promoteObjectTypeToCategorizationType(Term otTerm, ObjectType forConcept);

	/**
	 * Demote the {@link CategorizationType} to {@link ObjectType}. Note all {@link Category}s associated with this type
	 * will become ordinary {@link com.collibra.platform.patternbase.domain.Term}s.
	 * @param catType The {@link Term} that represents the {@link CategorizationType} to be demoted.
	 * @return The {@link ObjectType}.
	 */
	ObjectType demoteCategorizationTypeToObjectType(Term catTypeTerm);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	CategorizationType getCategorizationTypeWithError(String resourceId);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	Category getCategoryWithError(String resourceId);

	/**
	 * Persist a category.
	 * 
	 * <br />
	 * <br />
	 * Note: This method doesn't check permission and must not be used directly in the component.
	 */
	Category commit(Category category);

	/**
	 * Persist a categorization type.
	 * 
	 * <br />
	 * <br />
	 * Note: This method doesn't check permission and must not be used directly in the component.
	 */
	CategorizationType commit(CategorizationType catType);
}
