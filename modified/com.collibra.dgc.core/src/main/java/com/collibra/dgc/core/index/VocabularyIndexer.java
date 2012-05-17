/**
 * 
 */
package com.collibra.dgc.core.index;

import com.collibra.dgc.core.observer.VocabularyEventAdapter;
import com.collibra.dgc.core.observer.events.VocabularyEventData;
import com.collibra.dgc.core.service.impl.SearchServiceImpl;

/**
 * The indexer for vocabularies
 * @author dieterwachters
 */
public class VocabularyIndexer extends VocabularyEventAdapter {
	private final SearchServiceImpl searchService;

	public VocabularyIndexer(final SearchServiceImpl searchService) {
		this.searchService = searchService;
	}

	@Override
	public void onAdd(VocabularyEventData data) {
		// We also first delete it to be sure (sometimes an add is called instead of update).
		IndexHelper.deleteResource(data.getVocabulary(), searchService);
		IndexHelper.addVocabulary(data.getVocabulary(), searchService);
	}

	@Override
	public void onChanged(VocabularyEventData data) {
		IndexHelper.deleteResource(data.getVocabulary(), searchService);
		IndexHelper.addVocabulary(data.getVocabulary(), searchService);
	}

	@Override
	public void onRemove(VocabularyEventData data) {
		IndexHelper.deleteResource(data.getVocabulary(), searchService);
	}
}
