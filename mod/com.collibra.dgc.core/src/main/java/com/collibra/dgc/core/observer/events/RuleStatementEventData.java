package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;

/**
 * 
 * @author amarnath
 * 
 */
public class RuleStatementEventData extends AbstractEventData implements EventData {
	private final RuleSet ruleSet;
	private final RuleStatement ruleStatement;

	public RuleStatementEventData(RuleSet ruleSet, RuleStatement ruleStatement, EventType type) {
		super(type);
		this.ruleSet = ruleSet;
		this.ruleStatement = ruleStatement;
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public RuleStatement getRuleStatement() {
		return ruleStatement;
	}
}
