package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.Group;

public class GroupEventData extends AbstractEventData implements EventData {
	private final Group group;

	public GroupEventData(Group group, EventType eventType) {
		super(eventType);
		this.group = group;
	}

}
