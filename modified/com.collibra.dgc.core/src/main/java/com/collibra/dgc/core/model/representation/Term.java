package com.collibra.dgc.core.model.representation;

import com.collibra.dgc.core.model.meaning.ObjectType;

/**
 * A Term is a verbal {@link Designation} of an {@link ObjectType} in a specific subject field
 * 
 * @author dtrog
 * 
 */
public interface Term extends Designation {

	/**
	 * 
	 * @return The {@link ObjectType} that is designated.
	 */
	ObjectType getObjectType();

	/**
	 * 
	 * @return The signifier that expresses the {@link ObjectType}.
	 */
	String getSignifier();

	/**
	 * Set the new signifier.
	 * @param signifier The signifier to change.
	 */
	void setSignifier(String signifier);

	/**
	 * Set the new {@link ObjectType}.
	 * @param objectType The {@link ObjectType} to change.
	 */
	void setObjectType(ObjectType objectType);

	Term clone();
}
