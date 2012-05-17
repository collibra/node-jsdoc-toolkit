package com.collibra.dgc.core.observer.events;

/**
 * Priority for event listeners. Lower the number higher the priority. For example document handlers should have lowest
 * priority because the roll back on documents not possible. Hence if they run at the end then chances of losing them
 * due to exceptions is less.
 * @author amarnath
 * 
 */
public interface EventListenerPriority {
	public int DEFAULT = 0; // Default priority for all listeners
	public int DOCUMENT_HANDLERS = 3; // Priority for document handlers.
}
