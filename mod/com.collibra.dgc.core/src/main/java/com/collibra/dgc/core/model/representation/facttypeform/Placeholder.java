package com.collibra.dgc.core.model.representation.facttypeform;

import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;

/**
 * Placeholder is a designation of a fact type role within a fact type form marking a place where, in uses of the fact
 * type form, an expression denotes what fills the fact type role.
 * 
 * These objects are not stored in the database, but are generated dynamically.
 * 
 * Note: not to be confused with the fact type reading which represents a reading direction of a fact type
 * @author damien
 * 
 */
public interface Placeholder extends Representation {

	FactTypeRole getFactTypeRole();

	void setFactTypeRole(FactTypeRole factTypeRole);

	Term getTerm();

	void setTerm(Term term);

	FactTypeRole getMeaning();
}
