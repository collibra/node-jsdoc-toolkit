package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.Role;

/**
 * 
 * @author amarnath
 * 
 */
public class RoleEventData extends AbstractEventData implements EventData {
	private final Role role;

	public RoleEventData(Role role, EventType type) {
		super(type);
		this.role = role;
	}

	public Role getRole() {
		return role;
	}
}
