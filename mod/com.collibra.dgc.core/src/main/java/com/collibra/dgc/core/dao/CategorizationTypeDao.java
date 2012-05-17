package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.meaning.Concept;

/**
 * 
 * @author amarnath
 * 
 */
public interface CategorizationTypeDao extends AbstractDao<CategorizationType> {

	/**
	 * Returns a list of {@link CategorizationType}s for a given concept
	 * @param concept The concept for which to return the categorization types
	 * @return a list of {@link CategorizationType}s for a given concept
	 */
	List<CategorizationType> findForConcept(Concept concept);
}
