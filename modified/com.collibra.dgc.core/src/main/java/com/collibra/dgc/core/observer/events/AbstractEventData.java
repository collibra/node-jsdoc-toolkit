package com.collibra.dgc.core.observer.events;

import java.util.Calendar;
import java.util.Date;

public abstract class AbstractEventData {

	private final EventType eventType;
	private final Date time;
	
	AbstractEventData(EventType eventType) {
		this.eventType = eventType;
		this.time = Calendar.getInstance().getTime();
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public Date getTime() {
		return time;
	}
}
