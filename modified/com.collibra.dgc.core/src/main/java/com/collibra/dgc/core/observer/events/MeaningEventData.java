package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.meaning.Meaning;

/**
 * 
 * @author amarnath
 * 
 */
public class MeaningEventData extends AbstractEventData implements EventData {
	private final Meaning meaning;

	public MeaningEventData(Meaning meaning, EventType eventType) {
		super(eventType);
		this.meaning = meaning;
	}

	public Meaning getMeaning() {
		return meaning;
	}
}
