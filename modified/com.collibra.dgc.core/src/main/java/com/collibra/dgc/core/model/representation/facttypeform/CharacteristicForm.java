package com.collibra.dgc.core.model.representation.facttypeform;

import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;

/**
 * The {@link Representation} for a {@link Characteristic}.
 * 
 * @author dtrog
 * 
 */
public interface CharacteristicForm extends Representation {

	/**
	 * @return the {@link Term}
	 */
	Term getTerm();

	/**
	 * 
	 * @return The signifier for the role.
	 */
	String getRole();

	/**
	 * @return the {@link Characteristic} that this {@link CharacteristicForm} represents.
	 */
	Characteristic getCharacteristic();

	/**
	 * Set the new {@link Term}.
	 * @param term the {@link Term} to set
	 */
	void setTerm(Term term);

	/**
	 * Set the new expression for the role.
	 * @param role The role expression to change
	 */
	void setRole(String role);

	/**
	 * Set a new {@link Characteristic}.
	 * @param characteristic The {@link Characteristic} to change
	 */
	void setCharacteristic(Characteristic characteristic);

	/**
	 * Changes the {@link CharacteristicForm}.
	 * @param term The term.
	 * @param role The role.
	 */
	void update(Term term, String role);
}
