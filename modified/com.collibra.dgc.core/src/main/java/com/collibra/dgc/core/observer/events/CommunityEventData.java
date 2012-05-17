package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.community.Community;

/**
 * 
 * @author amarnath
 * 
 */
public class CommunityEventData extends AbstractEventData implements EventData {
	private final Community comm;
	public CommunityEventData(Community comm, EventType type) {
		super(type);
		this.comm = comm;
	}

	public Community getCommunity() {
		return comm;
	}
}
