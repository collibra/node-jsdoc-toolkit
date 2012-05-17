package com.collibra.dgc.core.observer;

import com.collibra.dgc.core.observer.events.EventData;

public interface Listener<I extends EventData> {
	void onAdding(I data);

	void onAdd(I data);

	void onRemove(I data);

	void onRemoving(I data);

	void onChaning(I data);

	void onChanged(I data);
}
