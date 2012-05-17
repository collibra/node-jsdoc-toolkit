/**
 * 
 */
package com.collibra.dgc.core.index;

import com.collibra.dgc.core.observer.TermEventAdapter;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.service.impl.SearchServiceImpl;

/**
 * The indexer for Terms
 * @author dieterwachters
 */
public class TermIndexer extends TermEventAdapter {
	private final SearchServiceImpl searchService;

	public TermIndexer(final SearchServiceImpl searchService) {
		this.searchService = searchService;
	}

	@Override
	public void onAdd(TermEventData data) {
		// We also first delete it to be sure (sometimes an add is called instead of update).
		IndexHelper.deleteResource(data.getTerm(), searchService);
		IndexHelper.addTerm(data.getTerm(), searchService);
	}

	@Override
	public void onChanged(TermEventData data) {
		IndexHelper.deleteResource(data.getTerm(), searchService);
		IndexHelper.addTerm(data.getTerm(), searchService);
	}

	@Override
	public void onRemove(TermEventData data) {
		IndexHelper.deleteResource(data.getTerm(), searchService);
	}
}
