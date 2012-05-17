package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * Meanings is what is meant by a word, sign, statement, or description; what someone intends to express or what someone
 * understands.
 * 
 * The implementation is assumed to be immutable (also for the interfaces that specify this one).
 * 
 * @author dtrog
 * 
 */
public interface Meaning extends Resource, Verbalisable {

	/**
	 * 
	 * @return The collection of all kinds of representations that express the {@link Meaning}.
	 */
	Set<? extends Representation> getRepresentations();

	/**
	 * Adds the given {@link Representation} to the {@link Meaning}. Throws an {@link IllegalArgumentException} for
	 * unsupported {@link Representation}s.
	 * @param representation The {@link Representation} to add.
	 */
	void addRepresentation(Representation representation);

	boolean hasDefaultRepresentation();
}
