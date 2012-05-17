package com.collibra.dgc.core.model.categorizations;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * The factory for creating categorization scheme related domain objects.
 * 
 * @author dtrog
 * 
 */
@Service
public interface CategorizationFactory {

	/**
	 * Creates a {@link CategorizationType} for a specific {@link ObjectType}.
	 * @param vocabulary The {@link Vocabulary} in which to add the {@link Term} that represents the
	 *            {@link CategorizationType}.
	 * @param signifier The signifier of the {@link Term} that represents the {@link CategorizationType}.
	 * @param isForConcept The objectType that the categorization type is for.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS}
	 * @return The {@link CategorizationType}.
	 */
	CategorizationType createCategorizationType(Vocabulary vocabulary, String signifier, ObjectType isForConcept);

	/**
	 * Changes specified {@link ObjectType} to {@link CategorizationType} for specified concept.
	 * @param ot The {@link ObjectType} to be changed.
	 * @param forConcept The concept.
	 * @return The {@link CategorizationType}.
	 */
	CategorizationType createCategorizationType(ObjectType ot, ObjectType forConcept);

	/**
	 * Creates a Categorization Scheme {@link Concept} for the specified general {@link Concept} being classified.
	 * @param concept The {@link Concept} being classified.
	 * @param vocabulary The {@link Vocabulary}
	 * @param name The name for categorization scheme.
	 * @return The Categorization Scheme {@link Concept}.
	 */
	Concept createCategorizationScheme(Concept concept, Vocabulary vocabulary, String name);

	/**
	 * Creates a {@link Category} with specified name, {@link CategorizationType} and Categorization Scheme
	 * {@link Concept} owing this {@link Category}.
	 * @param name The name for {@link Category}.
	 * @param catScheme The Categorization Scheme {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 */
	Category createCategory(String name, Concept catScheme, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Create {@link Category} with specified name and {@link CategorizationType}.
	 * @param name The name for {@link Category}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 */
	Category createCategory(String name, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Change specified {@link ObjectType} to {@link Category} of the specified {@link CategorizationType}.
	 * @param ot The {@link ObjectType}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}.
	 */
	Category createCategory(ObjectType ot, CategorizationType catType);

	/**
	 * Change specified {@link ObjectType} to {@link Category} of the specified {@link CategorizationType} and adds it
	 * to specified categorization scheme.
	 * @param ot The {@link ObjectType}.
	 * @param catScheme The categorization scheme {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @return The {@link Category}.
	 */
	Category createCategory(ObjectType ot, Concept catScheme, CategorizationType catType);

	/**
	 * Create {@link Category} with specified name and {@link CategorizationType} and for specified classifying
	 * {@link Concept}. Note that the classifying {@link Concept} must be taxonomical child of the
	 * {@link CategorizationType}'s classification {@link Concept}.
	 * @param name The name for {@link Category}.
	 * @param forConcept The classifying {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 */
	Category createCategoryForConcept(String name, Concept forConcept, CategorizationType catType, Vocabulary vocabulary);

	/**
	 * Creates a {@link Category} with specified name, {@link CategorizationType} and Categorization Scheme
	 * {@link Concept} owing this {@link Category} with specified classifying {@link Concept}. Note that the classifying
	 * {@link Concept} must be taxonomical child of the {@link CategorizationType}'s classification {@link Concept}.
	 * @param name The name for {@link Category}.
	 * @param forConcept The classifying {@link Concept}.
	 * @param catScheme The Categorization Scheme {@link Concept}.
	 * @param catType The {@link CategorizationType}.
	 * @param vocabulary The {@link Vocabulary} owning {@link Category}.
	 * @return The {@link Category}.
	 */
	Category createCategoryForConcept(String name, Concept forConcept, Concept catScheme, CategorizationType catType,
			Vocabulary vocabulary);
}
