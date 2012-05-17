package com.collibra.dgc.core.component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * The APIs for categorization scheme.
 * @author amarnath
 * 
 */
public interface CategorizationComponent {

	/**
	 * Create {@link CategorizationType} with specified name for the specified concept.
	 * @param vocabularyRId Resource id of the {@link Vocabulary} containing {@link CategorizationType} {@link Term}.
	 * @param signifier Name.
	 * @param isForConceptTermRId Resource id of the {@link Term} whose {@link Concept} is being classified.
	 * @return The {@link CategorizationType}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term addCategorizationType(String vocabularyRId, String signifier, String isForConceptTermrId);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType}.
	 * @param vocabularyRId Resource id of the {@link Vocabulary} owning {@link Category}.
	 * @param signifier The name.
	 * @param categorizationTypeTermRId Resource id of categorization type {@link Term}.
	 * @return The {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID},
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term addCategory(String vocabularyRId, String signifier, String categorizationTypeTermRId);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType} and for specified concept.
	 * @param signifier The name.
	 * @param isForConceptTermRId Resource id of the {@link Term} whose {@link Concept} is being classified.
	 * @param categorizationTypeTermRId Resource id of the {@link CategorizationType}.
	 * @param vocabularyRId Resource if of the {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID},
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term addCategoryForConcept(String signifier, String isForConceptTermRId, String categorizationTypeTermRId,
			String vocabularyRId);

	/**
	 * Create {@link Category} with specified name of specified {@link CategorizationType} and owned by specified
	 * categorization scheme.
	 * @param signifier The name.
	 * @param categorizationSchemeTermRId The resource id of the categorization scheme {@link Term} owning the
	 *            {@link Category}.
	 * @param categorizationTypeTermRId The resource id of the {@link CategorizationType} {@link Term}.
	 * @param vocabularyRId The resource id of the {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID},
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term addCategory(String signifier, String categorizationSchemeTermRId, String categorizationTypeTermRId,
			String vocabularyRId);

	/**
	 * Find the preferred {@link Term} of the categorization type.
	 * @param vocabularyRId Resource id of the {@link Vocabulary} in which to search for the {@link Term}.
	 * @param categorizationTypeRId Resource id of the {@link CategorizationType}
	 * @return The preferred Term that represents {@link CategorizationType}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term getCategorizationTypeTerm(String vocabularyRId, String categorizationTypeRId);

	/**
	 * Retrieves the categorization types (as {@link Term}s) for a given {@link Concept}.
	 * @param conceptTermRId Resource id of the {@link Term} that represents the {@link Concept} for which to return the
	 *            categorization types.
	 * @return {@link Collection} of {@link Term}s that represent the categorization types.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Collection<Term> getCategorizationTypeTermsForConcept(String conceptTermRId);

	/**
	 * Find the {@link Term}s that represent the {@link Category}s of specified {@link CategorizationType}.
	 * @param categorizationTypeTermRId Resource id of the {@link Term} that represents {@link CategorizationType}.
	 * @param vocabularyRId Resource id of the {@link Vocabulary} to start looking for the terms
	 * @return The Terms that represent the {@link Category}s.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID},
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Collection<Term> getCategoryTerms(String categorizationTypeTermRId, String vocabularyRId);

	/**
	 * Retrieves the categorization types (as {@link Term}s) for a given {@link Concept}.
	 * @param conceptTermRId Resource id of the {@link Term} that represents the {@link Concept} for which to return the
	 *            categories.
	 * @return List of {@link Term}s that represent the categorization types.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	List<Term> getCategoryTermsForConcept(String conceptTermRId);

	/**
	 * Return a Map from {@link Term} that represents a {@link CategorizationType} to a List of {@link Term}s
	 * representing {@link Category}s that belong to the given {@link Concept} and the {@link CategorizationType}.
	 * @param conceptTermRId Resource id of the {@link Term} that represents the {@link Concept}.
	 * @return {@link Map} of {@link CategorizationType} {@link Term} to {@link Category} {@link Term}s.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Map<Term, List<Term>> getCategorizationTypeTermToCategoriesTermMapForConcept(String conceptTermRId);

	/**
	 * Get the {@link CategorizationType} with the given resource id.
	 * @param rId The resource id of the {@link CategorizationType}.
	 * @return The {@link CategorizationType}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	CategorizationType getCategorizationType(String rId);

	/**
	 * Get the preferred {@link Term} of the {@link Category}.
	 * @param vocabularyRId The resource id of the {@link Vocabulary} in which to search for the term.
	 * @param categoryRId The resource id of the {@link Category} to find the {@link Term} for.
	 * @return the preferred Term of the {@link Categorization}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND_ID},
	 *             {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Term getCategoryTerm(String vocabularyRId, String categoryRId);

	/**
	 * Get {@link Category}.
	 * @param rId The resource id of the {@link Category}.
	 * @return The {@link Category}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Category getCategory(String rId);

	/**
	 * Check if the given {@link ObjectType} is of type {@link Category}.
	 * @param objectTypeTermRId The resource id of the {@link Term} for which its {@link ObjectType} is checked.
	 * @return True if {@link Category} otherwise false.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	boolean isCategory(String objectTypeTermRId);

	/**
	 * Check if the given {@link ObjectType} is of type {@link CategorizationType}.
	 * @param objectTypeTermRId The resource id of the {@link Term} for which its {@link ObjectType} is checked.
	 * @return True if {@link CategorizationType} otherwise false.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	boolean isCategorizationType(String objectTypeTermRId);

	/**
	 * Add an existing category to an existing categorization scheme
	 * @param categorizationSchemeTermRId The resource id of the categorization Scheme {@link Term} to which to add the
	 *            category
	 * @param categoryRId The resource id of the {@link Term} that represents the {@link Category} to add
	 * @return The latest version of the categorization scheme {@link Term}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Term addCategoryToCategorizationScheme(String categorizationSchemeTermRId, String categoryTermRId);

	/**
	 * Add an existing {@link Concept} as a categorized concept to a categorization scheme {@link Concept}.
	 * @param categorizationSchemeConceptRId The resource id of the categorization scheme {@link Term} to add the
	 *            concept to.
	 * @param categorizedConceptTermRId The resource id of the {@link Term} for which its {@link Concept} to add to the
	 *            categorization scheme
	 * @return The latest version of the categorization scheme {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term addCategorizedConceptToCategorizationScheme(String categorizationSchemeTermRId,
			String categorizedConceptTermRId);

	/**
	 * Removes the specified {@link Category}. Note that all {@link Term}s having this {@link Category} as meaning will
	 * be removed as well.
	 * @param categoryTermRId The resource id of the {@link Term} that represents the {@link Category} to be removed.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	void removeCategory(String categoryTermRId);

	/**
	 * Removes the specified {@link CategorizationType}. Note that all {@link Term}s having this
	 * {@link CategorizationType} and {@link Category}s of this type will be removed as well.
	 * @param categorizationTypeTermRId The resource id of the {@link Term} that represents {@link CategorizationType}
	 *            to be removed.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	void removeCategorizationType(String categorizationTypeTermRId);

	/**
	 * Remove category from categorization scheme
	 * @param categorizationSchemeTermRId The resource id of the categorization scheme {@link Term}.
	 * @param categoryTermRId The resource id of the {@link Term} that represents the {@link Category} to be removed
	 * @return The categorization scheme concept {@link Term}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Term removeCategoryFromCategorizationScheme(String categorizationSchemeTermRId, String categoryTermRId);

	/**
	 * Removes a categorized concept from a categorization scheme
	 * @param categorizationSchemeTermRId The resource id of the categorization scheme {@link Term}.
	 * @param categorizedConceptTermRId The resource id of the {@link Term} that represents the categorized
	 *            {@link Concept}.
	 * @return The categorization scheme concept {@link Term}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term removeCategorizedConceptFromCategorizationScheme(String categorizationSchemeTermRId,
			String categorizedConceptTermRId);

	/**
	 * Promote the {@link ObjectType} as {@link Category} of specified {@link CategorizationType} and add it to the
	 * specified categorization scheme.
	 * @param objectTypeTermRId Resource id of the {@link Term} for which its {@link ObjectType} is being promoted to
	 *            {@link Category}.
	 * @param categorizationSchemeTermRId The resource id of the categorization scheme {@link Term}.
	 * @param categorizationTypeTermRId The resource id of the {@link Term} that represents the
	 *            {@link CategorizationType}.
	 * @return The {@link Term} representing the {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term promoteObjectTypeToCategory(String objectTypeTermRId, String categorizationSchemeTermRId,
			String categorizationTypeTermRId);

	/**
	 * Promote the {@link ObjectType} as {@link Category} of specified {@link CategorizationType} of specified
	 * classifying {@link Concept}.
	 * @param objectTypeTermRId Resource id of the {@link Term} for which its {@link ObjectType} is being promoted to
	 *            {@link Category}.
	 * @param forconceptTermRId The resource id of the {@link Term} that represents the classifying {@link Concept}.
	 * @param categorizationTypeTermRId The resource id of the {@link Term} that represents the
	 *            {@link CategorizationType}.
	 * @return The {@link Term} representing the {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term promoteObjectTypeToCategoryForConcept(String objectTypeTermRId, String forconceptTermRId,
			String categorizationTypeTermRId);

	/**
	 * Promote the {@link ObjectType} as {@link Category} of specified {@link CategorizationType}.
	 * @param objectTypeTermRId Resource id of the {@link Term} for which its {@link ObjectType} is being promoted to
	 *            {@link Category}.
	 * @param categorizationTypeTermRId The resource id of the {@link Term} that represents the
	 *            {@link CategorizationType}.
	 * @return The {@link Term} representing the {@link Category}.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Term promoteObjectTypeToCategory(String objectTypeTermRId, String categorizationTypeTermRId);

	/**
	 * Demote the {@link Category} to {@link ObjectType} there by losing {@link CategorizationType} information.
	 * @param categoryTermRId The resource id of the {@link Term} that represents the {@link Category} to be demoted.
	 * @return The {@link Term} representing the {@link ObjectType}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term demoteCategoryToObjectType(String categoryTermRId);

	/**
	 * Promote the {@link Term} {@link ObjectType} as {@link CategorizationType} for specified concept.
	 * @param objectTypeTermRId Resource id of the {@link Term} for which its {@link ObjectType} is being promoted to
	 *            {@link CategorizationType}.
	 * @param forconceptTermRId The resource id of the {@link Term} that represents the classifying {@link Concept}.
	 * @return The {@link CategorizationType}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term promoteObjectTypeToCategorizationType(String objectTypeTermRId, String forconceptTermRId);

	/**
	 * Demote the {@link CategorizationType} to {@link ObjectType}. Note all {@link Category}s associated with this type
	 * will become ordinary {@link com.collibra.platform.patternbase.domain.Term}s.
	 * @param categorizationTypeTermRId Resource id of the {@link Term} that represents the {@link CategorizationType}
	 *            to be demoted.
	 * @return The {@link Term}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Term demoteCategorizationTypeToObjectType(String categorizationTypeTermRId);

	/**
	 * Get all {@link Category}s that are of type specified {@link CategorizationType}.
	 * @param categorizationTypeTermRId The resource id of the {@link Term} that represents the
	 *            {@link CategorizationType}.
	 * @return The {@link Category}s.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORIZATION_TYPE_NOT_FOUND}
	 */
	Collection<Category> getCategories(String categorizationTypeTermRId);

	/**
	 * Get all {@link CategorizationType}s that classify the specified general {@link Concept}.
	 * @param conceptTermRId The resource id of the general {@link Concept} that is classified.
	 * @return The {@link CategorizationType}s.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Collection<CategorizationType> getCategorizationTypes(String conceptTermRId);

	/**
	 * Get all categorization scheme {@link Concept}s that include the specified {@link Category}.
	 * @param categoryTermRId Resource id of the {@link Term} that represents the {@link Category}.
	 * @return The categorization scheme {@link Concept}s represented by their preferred terms.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Collection<Term> getSchemesForCategory(String categoryTermRId);

	/**
	 * Get all categorization scheme {@link Concept}s for the specified classified {@link Concept}.
	 * @param categorizedConceptRId Resource id of the classified {@link Concept}.
	 * @return The categorization scheme {@link Concept}s represented by their preferred terms.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND_ID}
	 */
	Collection<Term> getSchemesForConcept(String categorizedConceptTermRId);

	/**
	 * Find all representations for the root categorized concepts for a given categorization scheme
	 * @param categorizationSchemeRId The resource id of the categorization scheme {@link Concept}.
	 * @param vocabularyRId The resource id of the {@link Vocabulary} to search the preferred representations.
	 * @return a List of preferred {@link Term}s that represent the root categorized {@link Concept}s for the given
	 *         categorization scheme.
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND_ID} or
	 *             {@link DGCErrorCodes#CATEGORY_NOT_FOUND}
	 */
	Collection<Term> getRootCategorizedConceptsForCategorizationScheme(String categorizationSchemeRId,
			String vocabularyRId);
}