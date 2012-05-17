package com.collibra.dgc.core.dao;

import java.util.Collection;
import java.util.List;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author amarnath
 * 
 */
public interface CategoryDao extends AbstractDao<Category> {
	/**
	 * Find {@link Category}s of specified {@link CategorizationType}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}s.
	 */
	List<Category> find(CategorizationType catType);

	/**
	 * Find the {@link Term}s that represent the {@link Category}s of specified {@link CategorizationType}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabularies The {@link Vocabulary}s in which to look for the {@link Term}s
	 * @return The Terms that represent the {@link Category}s.
	 */
	List<Term> findCategoryTerms(CategorizationType catType, Collection<Vocabulary> vocabularies);

	/**
	 * Return a list of {@link Category}s belonging to the given {@link Concept} forConcept
	 * @param forConcept The concept to which the returned categories should belong
	 * @return a list of {@link Category}s
	 */
	List<Category> findCategoriesForConcept(Concept forConcept);
}
