package com.collibra.dgc.core.component;

import java.util.Collection;

import com.collibra.dgc.core.dto.search.SearchResultItem;
import com.collibra.dgc.core.service.SearchService.EType;

/**
 * Search API
 * 
 * @author pmalarme
 * 
 */
public interface SearchComponent {

	/**
	 * Search for resources matching the given search query string. Search only for the specified types.
	 * 
	 * @param search The string to search for in the index
	 * @param types The types of object to search for
	 * @param max The maximum number of hits we want to retrieve
	 * @return A list of {@link SearchResultItem} that corresponds to the matching resources
	 * @throws Exception
	 */
	Collection<SearchResultItem> search(String search, final Collection<EType> types, int max) throws Exception;

	/**
	 * Search for resources matching the given search query string. Search for all the types.
	 * 
	 * @param search The string to search for in the index
	 * @param max The maximum number of hits we want to retrieve
	 * @return A list of {@link SearchResultItem} that corresponds to the matching resources
	 * @throws Exception
	 */
	Collection<SearchResultItem> search(String search, int max) throws Exception;

	/**
	 * Trigger a full re-index of DGC index engine.
	 */
	public void doFullReIndex();

}
