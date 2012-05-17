package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;


/**
 * @author wouter
 *
 */
public class CharacteristicFormEventData extends AbstractEventData implements EventData {
	private final CharacteristicForm characteristicForm;

	public CharacteristicFormEventData(CharacteristicForm characteristicForm, EventType eventType) {
		super(eventType);
		this.characteristicForm = characteristicForm;
	}

	public CharacteristicForm getCharacteristicForm() {
		return characteristicForm;
	}
}
