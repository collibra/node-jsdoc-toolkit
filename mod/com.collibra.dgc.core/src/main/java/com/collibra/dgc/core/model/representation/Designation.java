package com.collibra.dgc.core.model.representation;

import com.collibra.dgc.core.model.meaning.Concept;

/**
 * A Designation is a {@link Representation} of a {@link Concept} by a sign which denotes it.
 * 
 * @author dtrog
 * 
 */
public interface Designation extends Representation {

	/**
	 * @return The {@link Concept} that is designated.
	 */
	Concept getMeaning();

	/**
	 * 
	 * @return The signifier that expresses the {@link Concept}.
	 */
	String getSignifier();

	/**
	 * 
	 * @return <code>true</code> if this {@link Designation} is the preferred one in its {@link Vocabulary} for the
	 *         {@link Concept}.
	 */
	Boolean getIsPreferred();

}
