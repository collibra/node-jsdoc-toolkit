package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * @author wouter
 * 
 */
public class BinaryFactTypeFormEventData extends AbstractEventData implements EventData {
	private final BinaryFactTypeForm binaryFactTypeForm;
	private boolean derived;
	private Term derivedForTerm;

	public BinaryFactTypeFormEventData(BinaryFactTypeForm binaryFactTypeForm, EventType eventType) {
		super(eventType);
		this.binaryFactTypeForm = binaryFactTypeForm;
	}

	public BinaryFactTypeForm getBinaryFactTypeForm() {
		return binaryFactTypeForm;
	}

	public boolean isDerived() {
		return derived;
	}

	public void setIsDerived(boolean derived) {
		this.derived = derived;
	}

	public Term getDerivedForTerm() {
		return derivedForTerm;
	}

	public void setDerivedForTerm(Term term) {
		this.derivedForTerm = term;
	}
}
