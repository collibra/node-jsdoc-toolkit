package com.collibra.dgc.core.model;

/**
 * <p>
 * A Resource in the Glossary.
 * </p>
 * @author dtrog
 * 
 */
public interface Resource extends Cloneable {
	/**
	 * 
	 * @return The identifier for this Vocabulary.
	 */
	String getId();

	void saved();

	boolean isPersisted();

	/**
	 * @return True if the resource is locked otherwise false.
	 */
	boolean isLocked();

	void checkConstraint();

	String getLastModifiedBy();

	Long getLastModified();

	String getCreatedBy();

	Long getCreatedOn();

	void updateLastModified();
}
