package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.representation.Attribute;

/**
 * 
 * @author pmalarme
 * 
 */
public class AttributeEventData extends AbstractEventData implements EventData {

	private final Attribute attribute;

	public AttributeEventData(Attribute attribute, EventType eventType) {
		super(eventType);

		this.attribute = attribute;
	}

	public Attribute getAttribute() {
		return attribute;
	}
}
