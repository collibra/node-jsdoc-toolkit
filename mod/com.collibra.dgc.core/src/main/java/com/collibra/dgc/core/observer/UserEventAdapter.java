package com.collibra.dgc.core.observer;

import com.collibra.dgc.core.observer.events.UserEventData;

/**
 * 
 * @author amarnath
 * 
 */
public abstract class UserEventAdapter implements Listener<UserEventData> {

	public void onAdd(UserEventData data) {
	}

	public void onAdding(UserEventData data) {
	}

	public void onChanged(UserEventData data) {
	}

	public void onChaning(UserEventData data) {
	}

	public void onRemove(UserEventData data) {
	}

	public void onRemoving(UserEventData data) {
	}
}
