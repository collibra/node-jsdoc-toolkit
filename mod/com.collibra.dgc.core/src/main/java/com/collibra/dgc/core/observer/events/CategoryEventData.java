package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.categorizations.Category;

public class CategoryEventData extends AbstractEventData implements EventData {
	private final Category category;

	public CategoryEventData(Category category, EventType eventType) {
		super(eventType);
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}
}
