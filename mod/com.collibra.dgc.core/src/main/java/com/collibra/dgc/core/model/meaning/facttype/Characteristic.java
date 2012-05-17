package com.collibra.dgc.core.model.meaning.facttype;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * A Characteristic is a {@link FactType} that has exactly one role, also known as a Unary Fact Type. An example: Person
 * is married
 * 
 * @author dtrog
 * 
 */
public interface Characteristic extends Concept {

	/**
	 * 
	 * @return The FactTypeRole that contains the role and concept for this characteristic.
	 */
	FactTypeRole getFactTypeRole();

	/**
	 * 
	 * @param factTypeRole The FactTypeRole that contains the role and concept for this characteristic.
	 */
	void setFactTypeRole(FactTypeRole factTypeRole);

	/**
	 * 
	 * @return The {@link CharacteristicForm}s that represent this {@link Characteristic}.
	 */
	Set<CharacteristicForm> getCharacteristicForms();

	/**
	 * Adds a {@link CharacteristicForm} that represent this {@link Characteristic}.
	 * 
	 * @param cForm A {@link CharacteristicForm} that represent this {@link Characteristic}.
	 */
	void addCharacteristicForm(CharacteristicForm cForm);

	/**
	 * Check if default characteristicForm is set
	 * 
	 * @return true if a default characteristicForm is found
	 */
	boolean hasDefaultCharacteristicForm();
}
