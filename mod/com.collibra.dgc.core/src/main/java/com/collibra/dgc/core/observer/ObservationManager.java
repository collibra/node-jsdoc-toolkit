package com.collibra.dgc.core.observer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.CharacteristicFormEventData;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.CommunityMemberEventData;
import com.collibra.dgc.core.observer.events.EventCategory;
import com.collibra.dgc.core.observer.events.EventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.GroupEventData;
import com.collibra.dgc.core.observer.events.GroupUserEventData;
import com.collibra.dgc.core.observer.events.MeaningEventData;
import com.collibra.dgc.core.observer.events.MemberEventData;
import com.collibra.dgc.core.observer.events.ObservationManagerEventData;
import com.collibra.dgc.core.observer.events.RoleEventData;
import com.collibra.dgc.core.observer.events.RuleStatementEventData;
import com.collibra.dgc.core.observer.events.RulesetEventData;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.observer.events.VocabularyEventData;

/**
 * {@link ObservationManager} class provides a framework for registering for events classified by {@link EventCategory}.
 * Each {@link EventCategory} can have various {@link EventType}s. Users are free to extend the interface
 * {@link EventCategory} to define their own classification of events. By default {@link ObservationManager} has
 * {@link ListenersManager} for the classifications defined by enumeration {@link GlossaryEventCategory}. However it
 * does not restrict to these classifications. Users can register their own type of event, in that case have to provide
 * a specific implementation of {@link EventData} while registering.
 * <P>
 * In case users want to have their own {@link ListenersManager}, then can extend the {@link ListenersManager} and
 * override the required behavior and register the custom implementation for the specific {@link EventCategory}.
 * <p>
 * Note that it is possible to have only one {@link ListenersManager} per {@link EventCategory} classification at this
 * point in time. However it is possible to extend that. May be this is something for the future.
 * @author amarnath
 * 
 */
public class ObservationManager {
	private static final Logger log = LoggerFactory.getLogger(ObservationManager.class);
	/**
	 * Singleton instance.
	 */
	private static ObservationManager instance;

	private final Map<EventCategory, ListenersManager<? extends EventData>> eventCategoryToListenersManager = new HashMap<EventCategory, ListenersManager<? extends EventData>>();

	/**
	 * Singleton accessor.
	 * @return
	 */
	public static ObservationManager getInstance() {
		if (instance == null) {
			synchronized (ObservationManager.class) {
				if (instance == null) {
					instance = new ObservationManager();
				}
			}
		}
		return instance;
	}

	/**
	 * This method clears the singleton instance.
	 */
	public synchronized static void clear() {
		if (instance != null) {
			// Do clean up
			ListenersManager<? extends EventData> observerManagerListeners = instance.eventCategoryToListenersManager
					.get(GlossaryEventCategory.OBSERVATION_MANAGER);

			instance = null;

			// Add the observation manager event listeners back.
			getInstance().eventCategoryToListenersManager.put(GlossaryEventCategory.OBSERVATION_MANAGER,
					observerManagerListeners);

			// Notify about the reset of observation manager.
			getInstance().notifyEvent(GlossaryEventCategory.OBSERVATION_MANAGER,
					new ObservationManagerEventData(EventType.REMOVED));
		}

	}

	/**
	 * Private constructor for singleton
	 */
	private ObservationManager() {
		initialize();
	}

	/**
	 * To register a {@link Listener} for the {@link EventCategory}s.
	 * @param listener The {@link Listener} instance.
	 * @param priority The priority for the listener. Lower the value higher is priority.
	 * @param eventTypes The {@link EventCategory}s
	 * @return True is successful, otherwise false.
	 */
	public boolean register(Listener listener, int priority, EventCategory... eventTypes) {
		boolean result = true;
		for (EventCategory eventType : eventTypes) {
			ListenersManager<? extends EventData> manager = eventCategoryToListenersManager.get(eventType);
			if (manager == null) {
				log.warn("Failed to get the listener manager for '" + eventType
						+ "'. Looks like no listener manager is registered.");
				result = false;
				continue;
			}

			if (!manager.register(listener, priority)) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * To unregister a {@link Listener} for the {@link EventCategory}s.
	 * @param listener The {@link Listener} instance.
	 * @param priority The priority for the listener. Lower the value higher is priority.
	 * @param eventTypes The {@link EventCategory}s
	 * @return True is successful, otherwise false.
	 */
	public boolean unregister(Listener listener, EventCategory... eventTypes) {
		boolean result = true;
		for (EventCategory eventType : eventTypes) {
			ListenersManager<? extends EventData> manager = eventCategoryToListenersManager.get(eventType);
			if (manager == null) {
				log.warn("Failed to get the listener manager for '" + eventType
						+ "'. Looks like no listener manager is registered.");
				result = false;
				continue;
			}

			if (!manager.unregister(listener)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * Event source notifies about the events to {@link ObservationManager}.
	 * @param category The {@link EventCategory}.
	 * @param data The {@link EventData}.
	 */
	public void notifyEvent(EventCategory category, EventData data) {
		ListenersManager manager = eventCategoryToListenersManager.get(category);
		if (manager != null) {
			manager.invoke(data);
		}
	}

	/**
	 * To register {@link ListenersManager}.
	 * @param category The {@link EventCategory}
	 * @param manager The {@link ListenersManager}.
	 * @return Old {@link ListenersManager} in case there is one, otherwise null.
	 */
	public ListenersManager<? extends EventData> registerListenersManager(EventCategory category,
			ListenersManager<? extends EventData> manager) {
		return eventCategoryToListenersManager.put(category, manager);
	}

	/**
	 * To unregister {@link ListenersManager}.
	 * @param category The {@link EventCategory}
	 * @param manager The {@link ListenersManager}.
	 * @return The {@link ListenersManager} that is unregistered otherwise null.
	 */
	public ListenersManager<? extends EventData> unregisterListenersManager(EventCategory category,
			ListenersManager<? extends EventData> manager) {
		ListenersManager<? extends EventData> exitingManager = eventCategoryToListenersManager.get(category);
		if (exitingManager != null && exitingManager.equals(manager)) {
			return eventCategoryToListenersManager.remove(category);
		}

		return null;
	}

	/**
	 * Initialize the glossary {@link ListenersManager}s.
	 */
	private void initialize() {
		eventCategoryToListenersManager.put(GlossaryEventCategory.TERM, new ListenersManager<TermEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new ListenersManager<BinaryFactTypeFormEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new ListenersManager<CharacteristicFormEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.MEANING, new ListenersManager<MeaningEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.VOCABULARY,
				new ListenersManager<VocabularyEventData>());
		eventCategoryToListenersManager
				.put(GlossaryEventCategory.COMMUNITY, new ListenersManager<CommunityEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.RULESET, new ListenersManager<RulesetEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.RULE_STATEMENT,
				new ListenersManager<RuleStatementEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.SERVICE_BFTF,
				new ListenersManager<BinaryFactTypeFormEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.MEMBER_COMMUNITY,
				new ListenersManager<CommunityMemberEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.MEMBER, new ListenersManager<MemberEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.ROLE, new ListenersManager<RoleEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.OBSERVATION_MANAGER,
				new ListenersManager<ObservationManagerEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.VOCABULARY,
				new ListenersManager<VocabularyEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.GROUP, new ListenersManager<GroupEventData>());
		eventCategoryToListenersManager.put(GlossaryEventCategory.GROUP_USERS,
				new ListenersManager<GroupUserEventData>());
	}
}
