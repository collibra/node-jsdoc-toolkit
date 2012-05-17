package com.collibra.dgc.core.model.representation;

import com.collibra.dgc.core.model.meaning.Proposition;

/**
 * A statement is a representation of a proposition by an expression of the proposition.
 * 
 * @author dtrog
 * 
 */
public interface Statement extends Representation {

	/**
	 * @return the {@link Proposition} that is expressed by this {@link Statement}
	 */
	Proposition getProposition();

}
