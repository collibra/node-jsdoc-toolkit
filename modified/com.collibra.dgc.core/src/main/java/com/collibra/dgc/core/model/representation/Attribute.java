package com.collibra.dgc.core.model.representation;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;

/**
 * An attribute stores extra information about a concept. The meaning refers to what the attribute means and the
 * expression is the value of the custom attribute.
 * 
 * @author dtrog
 * 
 */
public interface Attribute extends Resource, Verbalisable {

	/**
	 * 
	 * @return The {@link Term} that represents the label for the attribute.
	 */
	Term getLabel();

	/**
	 * Get the owner of the {Attribute} (The representation that links/created the attribute)
	 * 
	 * @return
	 */
	Representation getOwner();
	
	/**
	 * @return The String value of the attribute
	 */
	String getValue();

	String getDiscriminator();
}
