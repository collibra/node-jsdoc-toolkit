package com.collibra.dgc.core.observer;

import com.collibra.dgc.core.observer.events.VocabularyEventData;

/**
 * 
 * @author amarnath
 * 
 */
public abstract class VocabularyEventAdapter implements Listener<VocabularyEventData> {

	public void onAdd(VocabularyEventData data) {
	}

	public void onAdding(VocabularyEventData data) {
	}

	public void onChanged(VocabularyEventData data) {
	}

	public void onChaning(VocabularyEventData data) {
	}

	public void onRemove(VocabularyEventData data) {
	}

	public void onRemoving(VocabularyEventData data) {
	}
}
