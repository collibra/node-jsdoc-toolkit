package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.categorizations.Category;

/**
 * A Concept is a unit of knowledge created by a unique combination of characteristics.
 * 
 * @author dtrog
 * 
 */
public interface Concept extends Meaning {

	/**
	 * 
	 * @return The {@link Concept} that is a generalization of this {@link Concept}.
	 */
	Concept getGeneralConcept();
	
	/**
	 * 
	 * @param generalConcept The {@link Concept} to set as a generalisation of this {@link Concept}.
	 */
	void setGeneralConcept(Concept generalConcept);

	/**
	 * 
	 * @return The type of this {@link Concept}, represented by an {@link ObjectType}.
	 */
	ObjectType getType();

	/**
	 * Set the type of this {@link Concept}.
	 * 
	 * @param type Type of this {@link Concept}, represented by an {@link ObjectType}.
	 */
	void setType(ObjectType type);

	/**
	 * 
	 * @return The {@link ReferenceScheme} that uniquely identifies the {@link Concept}.
	 */
	Set<ReferenceScheme> getReferenceSchemes();

	/**
	 * 
	 * @param referenceScheme The {@link ReferenceScheme} to add.
	 */
	void addReferenceScheme(ReferenceScheme referenceScheme);

	/**
	 * 
	 * @return The categories to which this {@link IndividualConcept} belongs.
	 */
	Set<Category> getCategories();

	/**
	 * Adds a category.
	 * @param category The {@link Category} to add.
	 */
	void addCategory(Category category);

	/**
	 * Removes a category.
	 * @param category The {@link Category} to remove.
	 * @return true if the category has been removed. false if not (e.g. the categorization scheme did not contain the
	 *         category to be removed)
	 */
	boolean removeCategory(Category category);

	/**
	 * @return True if the {@link Concept} is categorization scheme otherwise false.
	 */
	boolean isCategorizationScheme();

	/**
	 * @return {@link Set} of {@link Concept}s being classified by this {@link Concept}.
	 * @see Concept#isCategorizationScheme().
	 */
	Set<Concept> getConceptsCategorized();

	/**
	 * Adds a categorized {@link Concept}.
	 * @param concept The {@link Concept}.
	 */
	void addCategorizedConcept(Concept concept);

	/**
	 * Removes a categorized {@link Concept}.
	 * @param concept The {@link Concept}.
	 * @return true if the categorized concept has been removed. false if not (e.g. the categorization scheme did not
	 *         contain the categorized concept to be removed)
	 */
	boolean removeCategorizedConcept(Concept concept);
}
