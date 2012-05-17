package com.collibra.dgc.core.component.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.SearchComponent;
import com.collibra.dgc.core.dto.search.SearchResultItem;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.SearchService.EType;
import com.collibra.dgc.core.util.Defense;

/**
 * Search API implementation
 * 
 * @author pmalarme
 * 
 */
@Service
public class SearchComponentImpl implements SearchComponent {

	@Autowired
	private SearchService searchService;

	@Override
	@Transactional
	public Collection<SearchResultItem> search(String search, Collection<EType> types, int max) throws Exception {
		Defense.notEmpty(search, DGCErrorCodes.SEARCH_QUERY_NULL, DGCErrorCodes.SEARCH_QUERY_EMPTY, "search");
		Defense.notEmpty(types, DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_EMPTY, "types");

		return searchService.search(search, types, max);
	}

	@Override
	@Transactional
	public Collection<SearchResultItem> search(String search, int max) throws Exception {
		Defense.notEmpty(search, DGCErrorCodes.SEARCH_QUERY_NULL, DGCErrorCodes.SEARCH_QUERY_EMPTY, "search");
		return searchService.search(search, max);
	}

	@Override
	@Transactional
	public void doFullReIndex() {
		searchService.doFullReIndex();
	}

}
