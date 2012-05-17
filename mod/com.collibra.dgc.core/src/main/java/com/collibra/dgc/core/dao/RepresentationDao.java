package com.collibra.dgc.core.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * DAO for all kinds of {@link Representation} business objects.
 * 
 * @author dtrog
 * 
 */
public interface RepresentationDao extends AbstractDao<Representation> {

	/**
	 * Given a {@link Representation} this returns the {@link Representation}s of the specialized concepts of its
	 * {@link Concept} as preferred in its {@link Vocabulary}. Limits results to only specialized concepts represesented
	 * in the same {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @return The list of {@link Representation}s of the specialized concepts in the same vocabulary as the given
	 *         {@link Representation}.
	 */
	List<Representation> findSpecializedConceptRepresentationsInSameVocabulary(Representation representation);

	/**
	 * Given a {@link Representation} this returns the {@link Representation}s of the specialized concepts of its
	 * {@link Concept} as preferred in this {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @return The list of {@link Representation}s of the specialized concepts
	 */
	List<Representation> findSpecializedConceptRepresentations(Representation representation);

	/**
	 * Find the preferred {@link Representation} in the {@link Vocabulary} for the given {@link Concept}.
	 * @param concept {@link Concept} to find the {@link Representation} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Representation}.
	 * @return The preferred {@link Representation}
	 */
	Representation findPreferredRepresentation(Concept concept, Vocabulary vocabulary);

	/**
	 * Finds all the synonyms for the given {@link Representation}.
	 * @param representation The representation to find synonyms for.
	 * @return A list of synonymous representations.
	 */
	List<Representation> findSynonyms(Representation representation);

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	List<Representation> find(Attribute attribute);

	/**
	 * 
	 * @param voc
	 * @return
	 */
	Collection<Representation> findByVocabulary(Vocabulary voc);

	/**
	 * @param signifier
	 * @return A list of Designations that match (as a SQL LIKE statement) with the given signifier string.
	 */
	List<Designation> searchInDesignationSignifiers(String signifier);

	List<Term> findStatusTerms();

	/**
	 * 
	 * @return
	 */
	List<Representation> findAllWithoutVocabulary();

	/**
	 * 
	 * @return
	 */
	MultiValueMap findRepresentationsWithoutAttributes();

	/**
	 * 
	 * @param representation
	 * @return
	 */
	List<Representation> findSpecializedRepresentations(Representation representation, int limit);

	// /**
	// * Check if the representation is persisted or not.
	// * @param representation The {@link Representation}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(final Representation representation);
}
