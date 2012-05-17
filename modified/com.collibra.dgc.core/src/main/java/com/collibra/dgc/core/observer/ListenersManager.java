package com.collibra.dgc.core.observer;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.collibra.dgc.core.observer.events.EventData;

/**
 * 
 * @author amarnath
 * 
 * @param <C>
 */
public class ListenersManager<C extends EventData> {
	protected final SortedMap<Integer, Set<Listener<C>>> priorityToListeners = new TreeMap<Integer, Set<Listener<C>>>();

	public boolean register(Listener<C> listener, int priority) {
		Set<Listener<C>> listeners = priorityToListeners.get(priority);
		if (listeners == null) {
			listeners = new HashSet<Listener<C>>();
			priorityToListeners.put(priority, listeners);
		}

		return listeners.add(listener);
	}

	public boolean unregister(Listener<C> listener) {
		boolean result = false;
		for (Set<Listener<C>> listeners : priorityToListeners.values()) {
			if (listeners.remove(listener)) {
				result = true;
			}
		}
		return result;
	}

	public void invoke(C data) {
		switch (data.getEventType()) {
		case ADDED:
			onAdded(data);
			break;
		case ADDING:
			onAdding(data);
			break;
		case REMOVING:
			onRemoving(data);
			break;
		case REMOVED:
			onRemoved(data);
			break;
		case CHANGED:
			onChanged(data);
			break;
		case CHANGING:
			onChaning(data);
			break;

		default:
			// TODO: log warning / error
			break;
		}
	}

	private void onAdded(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : new HashSet<Listener<C>>(priorityToListeners.get(priority))) {
				listener.onAdd(data);
			}
		}
	}

	private void onAdding(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : new HashSet<Listener<C>>(priorityToListeners.get(priority))) {
				listener.onAdding(data);
			}
		}
	}

	private void onRemoved(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : new HashSet<Listener<C>>(priorityToListeners.get(priority))) {
				listener.onRemove(data);
			}
		}
	}

	private void onRemoving(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : priorityToListeners.get(priority)) {
				listener.onRemoving(data);
			}
		}
	}

	private void onChaning(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : new HashSet<Listener<C>>(priorityToListeners.get(priority))) {
				listener.onChaning(data);
			}
		}
	}

	private void onChanged(C data) {
		for (int priority : priorityToListeners.keySet()) {
			for (Listener<C> listener : new HashSet<Listener<C>>(priorityToListeners.get(priority))) {
				listener.onChanged(data);
			}
		}
	}
}
