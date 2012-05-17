package com.collibra.dgc.core.model.representation.facttypeform.impl;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.Placeholder;
import com.collibra.dgc.core.model.representation.impl.DesignationImpl;

public class PlaceholderImpl extends DesignationImpl implements Placeholder {

	private FactTypeRole factTypeRole;
	private Term term;

	public PlaceholderImpl(FactTypeRole factTypeRole, Term term) {
		this.factTypeRole = factTypeRole;
		this.term = term;
	}

	public PlaceholderImpl(PlaceholderImpl placeholderImpl) {
		this(placeholderImpl.getFactTypeRole(), placeholderImpl.getTerm());
		setLock(placeholderImpl.isLocked());
	}

	public String verbalise() {
		return term.verbalise();
	}

	public FactTypeRole getMeaning() {
		return getFactTypeRole();
	}

	public FactTypeRole getFactTypeRole() {
		return factTypeRole;
	}

	public void setFactTypeRole(FactTypeRole factTypeRole) {
		checkConstraint();
		this.factTypeRole = factTypeRole;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		checkConstraint();
		this.term = term;
	}

	public void setMeaning(Meaning meaning) {
		checkConstraint();
		if (!(meaning instanceof FactTypeRole)) {
			return;
		}
		setFactTypeRole((FactTypeRole) meaning);
	}

	@Override
	public PlaceholderImpl clone() {
		return new PlaceholderImpl(this);
	}

	@Override
	public String getRepresentationType() {
		return "Placeholder";
	}
}
