package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.representation.Term;

/**
 * 
 * @author amarnath
 * 
 */
public class TermEventData extends AbstractEventData implements EventData {
	private final Term term;

	public TermEventData(Term term, EventType eventType) {
		super(eventType);
		this.term = term;
	}

	public Term getTerm() {
		return term;
	}
}
