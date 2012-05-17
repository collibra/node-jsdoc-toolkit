package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.relation.Relation;

public class RelationEventData extends AbstractEventData implements EventData {

	private final Relation relation;
	
	public RelationEventData(Relation rel, EventType eventType) {
		super(eventType);
		this.relation = rel;
	}
	
	public Relation getRelation() {
		return this.relation;
	}
}
