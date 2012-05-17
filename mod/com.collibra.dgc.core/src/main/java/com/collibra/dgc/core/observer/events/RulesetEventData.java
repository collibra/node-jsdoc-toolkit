package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.rules.RuleSet;

/**
 * 
 * @author amarnath
 * 
 */
public class RulesetEventData extends AbstractEventData implements EventData {
	private final RuleSet rs;

	public RulesetEventData(RuleSet rs, EventType type) {
		super(type);
		this.rs = rs;
	}

	public RuleSet getRuleSet() {
		return rs;
	}
}
