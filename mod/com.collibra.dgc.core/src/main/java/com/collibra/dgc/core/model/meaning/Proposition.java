package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.representation.Statement;

/**
 * A Proposition is a Meaning that is true or false
 * 
 * @author dtrog
 * 
 */
public interface Proposition extends Meaning {

	/**
	 * 
	 * @return The statements that are used to express the proposition.
	 */
	Set<Statement> getStatements();

	// not supported yet

	// /**
	// *
	// * @return true if the proposition corresponds to an actuality
	// */
	// boolean isTrue();
	//
	// /**
	// *
	// * @return true if the proposition always corresponds to an actuality
	// */
	// boolean isNecessarilyTrue();
	//
	// /**
	// *
	// * @return true if it is possible that the proposition corresponds to an actuality
	// */
	// boolean isPossiblyTrue();
	//
	// /**
	// *
	// * @return true if the proposition corresponds to an actuality in all acceptable worlds.
	// */
	// boolean isObligatedToBeTrue();
	//
	// /**
	// *
	// * @return true if the proposition corresponds to an actuality in at least one acceptable world.
	// */
	// boolean isPermittedToBeTrue();

}
