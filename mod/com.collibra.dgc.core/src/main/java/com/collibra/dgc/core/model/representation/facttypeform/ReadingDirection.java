package com.collibra.dgc.core.model.representation.facttypeform;

import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.representation.Term;

/**
 * A {@link ReadingDirection} is a designation of a {@link FactTypeRole} marking a place where, in uses of the fact type
 * form, an expression denotes what fills the {@link FactTypeRole}.
 * 
 * @author dtrog
 * 
 */
public interface ReadingDirection extends Verbalisable {
	/**
	 * @return The {@link BinaryFactTypeForm} to which this {@link ReadingDirection} belongs.
	 */
	BinaryFactTypeForm getBinaryFactTypeForm();

	/**
	 * @return The head {@link Term} of {@link ReadingDirection}.
	 */
	Term getHeadTerm();

	/**
	 * @return The tail {@link Term} of {@link ReadingDirection}.
	 */
	Term getTailTerm();

	/**
	 * @return The role of {@link ReadingDirection}.
	 */
	String getRole();

	/**
	 * 
	 * @return
	 */
	boolean getIsLeft();
}
