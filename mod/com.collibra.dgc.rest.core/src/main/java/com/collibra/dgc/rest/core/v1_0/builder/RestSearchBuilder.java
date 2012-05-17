package com.collibra.dgc.rest.core.v1_0.builder;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.collibra.dgc.rest.core.v1_0.dto.ObjectFactory;
import com.collibra.dgc.rest.core.v1_0.dto.ResourceTypeType;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItem;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItems;

/**
 * REST model builder for the search resource.
 * @author pmalarme
 * 
 */
@Service
public class RestSearchBuilder {

	private final ObjectFactory factory = new ObjectFactory();

	public SearchResultItem buildSearchResultItem(com.collibra.dgc.core.dto.search.SearchResultItem searchResultItem) {

		if (searchResultItem == null)
			return null;

		SearchResultItem sri = factory.createSearchResultItem();

		sri.setResourceId(searchResultItem.getResourceID().toString());
		sri.setName(searchResultItem.getName());
		sri.setType(ResourceTypeType.valueOf(searchResultItem.getType()));
		sri.setScore(searchResultItem.getScore());
		sri.setAttribute(searchResultItem.getAttribute());
		sri.setParent(buildSearchResultItem(searchResultItem.getParent()));

		return sri;
	}

	public SearchResultItems buildSearchResultItems(
			Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems) {

		SearchResultItems sris = factory.createSearchResultItems();
		List<SearchResultItem> searchResultItemList = sris.getSearchResultItems();

		for (com.collibra.dgc.core.dto.search.SearchResultItem searchResultItem : searchResultItems) {

			SearchResultItem sri = buildSearchResultItem(searchResultItem);
			searchResultItemList.add(sri);
		}

		return sris;
	}
}
