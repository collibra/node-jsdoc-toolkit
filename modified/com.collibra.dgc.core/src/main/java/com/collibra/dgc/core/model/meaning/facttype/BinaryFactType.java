package com.collibra.dgc.core.model.meaning.facttype;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * A Binary Fact Type is a fact type that has exactly 2 roles. For example: Person drives / driven by Car.
 * 
 * @author dtrog
 * 
 */
public interface BinaryFactType extends Concept {

	/**
	 * 
	 * @return The FactTypeRole read from head to tail.
	 */
	FactTypeRole getHeadFactTypeRole();

	/**
	 * 
	 * @return The FactTypeRole read from tail to head.
	 */
	FactTypeRole getTailFactTypeRole();

	/**
	 * 
	 * @return The {@link BinaryFactTypeForm}s that represent this {@link BinaryFactType}.
	 */
	Set<BinaryFactTypeForm> getBinaryFactTypeForms();

	/**
	 * Adds a {@link BinaryFactTypeForm} that represents this {@link BinaryFactType}.
	 * 
	 * @param bftf A {@link BinaryFactTypeForm} that represents this {@link BinaryFactType}.
	 */
	void addBinaryFactTypeForm(BinaryFactTypeForm bftf);

	/**
	 * Check if default binaryFactTypeForm is set
	 * 
	 * @return true if a default binaryFactTypeForm is found
	 */
	boolean hasDefaultBinaryFactTypeForm();

}
