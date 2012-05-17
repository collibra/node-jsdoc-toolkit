package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author amarnath
 * 
 */
public class VocabularyEventData extends AbstractEventData implements EventData {

	private final Vocabulary vocabulary;
	
	public VocabularyEventData(Vocabulary vocabulary, EventType type) {
		super(type);
		this.vocabulary = vocabulary;
	}

	public Vocabulary getVocabulary() {
		return vocabulary;
	}
}
