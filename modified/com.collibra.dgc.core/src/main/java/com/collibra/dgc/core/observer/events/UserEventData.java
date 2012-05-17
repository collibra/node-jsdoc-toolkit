package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.User;

/**
 * 
 * @author amarnath
 * 
 */
public class UserEventData extends AbstractEventData implements EventData {

	private final User user;

	public UserEventData(User user, EventType type) {
		super(type);
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
