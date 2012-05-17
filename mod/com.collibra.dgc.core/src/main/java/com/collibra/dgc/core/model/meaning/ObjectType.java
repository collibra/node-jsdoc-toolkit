package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.representation.Term;

/**
 * An Object Type is a {@link Concept} that is the {@link Meaning} of a noun or noun phrase that classifies things on
 * the basis of their common properties.
 * 
 * @author dtrog
 * 
 */
public interface ObjectType extends Concept {

	/**
	 * 
	 * @return The {@link Term}s that are used to express the concept.
	 */
	Set<Term> getTerms();

	/**
	 * Add a {@link Term} to this {@link Concept}.
	 * 
	 * @param term The {@link Term} to add.
	 */
	void addTerm(Term term);

	/**
	 * 
	 * @return A field to field copy of this {@link ObjectType}.
	 */
	ObjectType clone();

	/**
	 * Check if default term is set
	 * 
	 * @return true if a default term is found
	 */
	boolean hasDefaultTerm();

	/**
	 * Find a preferred term for the Object Type
	 * 
	 * @return a preferred {@link Term} or null if no preferred term found.
	 */
	Term findPreferredTerm();
}
