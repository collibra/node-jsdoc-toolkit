package com.collibra.dgc.core.model.categorizations;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;

/**
 * 
 * @author amarnath
 * 
 */
public interface CategorizationType extends ObjectType {

	/**
	 * @return The {@link Concept} being classified.
	 */
	ObjectType getIsForConcept();

	/**
	 * Set the {@link Concept} being classified.
	 * @param forConcept The {@link Concept}.
	 */
	void setIsForConcept(ObjectType forConcept);
}
