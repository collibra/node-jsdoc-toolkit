package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;

public interface ConceptDao extends AbstractDao<Concept> {

	/**
	 * Retrieves the direct children of the given {@link Concept}.
	 * @param concept The {@link Concept} that is the general {@link Concept}.
	 * @return The list of specialized {@link Concept}s
	 */
	List<Concept> findSpecializedConcepts(Concept concept);

	/**
	 * Retrieves all the specialized {@link Concept}s for the given {@link Concept}. Recursively creates the list by
	 * going again to the each specialized concept till there are no specialized concepts.
	 * @param concept The {@link Concept} that is the general {@link Concept}.
	 * @return The list of specialized {@link Concept}s
	 */
	public List<Concept> findAllSpecializedConcepts(Concept concept);

	/**
	 * Retrieves all the {@link Concept}s that have as concept type the given {@link Concept}.
	 * @param concept The concept
	 * @return The list of {@link Concepts} that have as type the given {@link Concept}.
	 */
	List<Concept> findAllConceptsWithConceptType(Concept concept);

	/**
	 * Retrieves categorization scheme {@link Concept} for the specified resource id.
	 * @param resourceId The resource id.
	 * @return The categorization scheme {@link Concept}.
	 */
	Concept findCategorizationSchemeByResourceId(String resourceId);

	/**
	 * Retrieves all categorization scheme {@link Concept}s that include specified {@link Category}.
	 * @param category The {@link Category}.
	 * @return The categorization scheme {@link Concept}s.
	 */
	List<Concept> findCategorizationSchemes(Category category);

	/**
	 * Retrieves all categorization scheme {@link Concept}s that categorize the specified {@link Concept}.
	 * @param categorizedConcept The classified {@link Concept}.
	 * @return The categorization scheme {@link Concept}s.
	 */
	List<Concept> findCategorizationSchemesForConcept(Concept categorizedConcept);

	// /**
	// * Check if the concept is persisted or not.
	// *
	// * @param concept The {@link Concept}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(Concept concept);
}
