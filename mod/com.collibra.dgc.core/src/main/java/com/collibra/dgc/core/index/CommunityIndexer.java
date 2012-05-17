/**
 * 
 */
package com.collibra.dgc.core.index;

import com.collibra.dgc.core.observer.CommunityEventAdapter;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.service.impl.SearchServiceImpl;

/**
 * The indexer for communities
 * @author dieterwachters
 */
public class CommunityIndexer extends CommunityEventAdapter {
	private final SearchServiceImpl searchService;

	public CommunityIndexer(final SearchServiceImpl searchService) {
		this.searchService = searchService;
	}

	@Override
	public void onAdd(CommunityEventData data) {
		// We also first delete it to be sure (sometimes an add is called instead of update).
		IndexHelper.deleteResource(data.getCommunity(), searchService);
		IndexHelper.addCommunity(data.getCommunity(), searchService);
	}

	@Override
	public void onChanged(CommunityEventData data) {
		IndexHelper.deleteResource(data.getCommunity(), searchService);
		IndexHelper.addCommunity(data.getCommunity(), searchService);
	}

	@Override
	public void onRemove(CommunityEventData data) {
		IndexHelper.deleteResource(data.getCommunity(), searchService);
	}
}
