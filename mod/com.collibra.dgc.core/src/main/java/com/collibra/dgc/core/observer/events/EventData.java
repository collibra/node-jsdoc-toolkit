package com.collibra.dgc.core.observer.events;

import java.util.Date;


public interface EventData {
	
	EventType getEventType();
	
	Date getTime();
}
