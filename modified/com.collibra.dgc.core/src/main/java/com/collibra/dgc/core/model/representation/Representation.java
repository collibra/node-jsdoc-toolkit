package com.collibra.dgc.core.model.representation;

import java.util.Set;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.meaning.Meaning;

/**
 * Representation is an actuality that a given expression represents a given {@link Meaning}.
 * @author dtrog
 * 
 */
public interface Representation extends Resource, Verbalisable, Comparable<Representation> {

	/**
	 * 
	 * @return The {@link Meaning} that is being represented.
	 */
	Meaning getMeaning();

	/**
	 * 
	 * @return The {@link Vocabulary} in which the {@link Representation} is stored.
	 */
	Vocabulary getVocabulary();

	/**
	 * Change the vocabulary to which this representation belongs.
	 * @param The new {@link Vocabulary} in which the {@link Representation} is stored.
	 */
	void setVocabulary(final Vocabulary vocabulary);

	/**
	 * 
	 * @return <code>true</code> if this {@link Representation} is the preferred one in its {@link Vocabulary} for the
	 *         {@link Meaning}.
	 */
	Boolean getIsPreferred();

	/**
	 * Set a new {@link Meaning}.
	 * @param meaning The {@link Meaning} that is represented by the {@link Representation}.
	 */
	void setMeaning(Meaning meaning);

	/**
	 * @return A field per field copy of the {@link Representation}.
	 */
	Representation clone();

	/**
	 * Get {Attribute}s for the representation
	 * 
	 * @return
	 */
	Set<Attribute> getAttributes();

	/**
	 * Add the given attribute to this representation.
	 * @param attribute The attribute to be added
	 */
	void addAttribute(Attribute attribute);

	/**
	 * Retrieve the {@link Term} representing the status of this representation.
	 * @return The {@link Term} representing the status of this representation.
	 */
	Term getStatus();

	/**
	 * Set the new status for this representation.
	 * @param status The new status for this representation.
	 */
	void setStatus(final Term status);
}
