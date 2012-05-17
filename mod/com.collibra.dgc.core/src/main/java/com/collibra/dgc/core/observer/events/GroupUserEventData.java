package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.UserData;

public class GroupUserEventData extends AbstractEventData implements EventData {
	private final Group group;
	private final UserData user;

	public GroupUserEventData(Group group, UserData user, EventType eventType) {
		super(eventType);
		this.group = group;
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public UserData getUser() {
		return user;
	}

}
