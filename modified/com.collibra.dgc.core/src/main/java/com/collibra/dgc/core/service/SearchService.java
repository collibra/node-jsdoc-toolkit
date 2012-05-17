package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;

import com.collibra.dgc.core.dto.search.SearchResultItem;

public interface SearchService {
	// Property to pause indexer from updating lucene index.
	public static final String PROPERTY_PAUSE_INDEXER = "com.collibra.index.building.pause";
	public static final String PROPERTY_FULL_REINDEX_SKIP = "com.collibra.index.full.skip";

	public enum EType {
		TE, AT, NA, CO, VC
	};

	public static final EType[] ALL_TYPES = new EType[] { EType.TE, EType.AT, EType.NA, EType.CO, EType.VC };

	/**
	 * Search for resources matching the given search query string. Search only for the specified types.
	 * 
	 * @param search The string to search for in the index.
	 * @param types The types of objects to search for.
	 * @param max The maximum number of hits we want to retrieve
	 * @return The list of matching resources.
	 */
	public List<SearchResultItem> search(String search, final Collection<EType> types, int max) throws Exception;

	/**
	 * Search for resources matching the given search query string. Search for all the types.
	 * 
	 * @param search The string to search for in the index.
	 * @param max The maximum number of hits we want to retrieve
	 * @return The list of matching resources.
	 */
	public List<SearchResultItem> search(String search, int max) throws Exception;

	/**
	 * Trigger a full re-index of the Lucene index.
	 */
	public void doFullReIndex();
}
