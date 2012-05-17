package com.collibra.dgc.api.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.SearchService.EType;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestSearchComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final Collection<EType> NOT_EMPTY_COLLECTION = Arrays.asList(EType.values());
	private static final Collection<EType> NULL_COLLECTION = null;
	private static final Collection<EType> EMPTY_COLLECTION = new ArrayList<SearchService.EType>();

	@Test
	public void testSearchDefenseSearchNull() throws Exception {
		try {
			searchComponent.search(NULL, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SEARCH_QUERY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSearchDefenseSearchEmpty() throws Exception {
		try {
			searchComponent.search(EMPTY, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SEARCH_QUERY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSearch2DefenseSearchNull() throws Exception {
		try {
			searchComponent.search(NULL, NOT_EMPTY_COLLECTION, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SEARCH_QUERY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSearch2DefenseSearchEmpty() throws Exception {
		try {
			searchComponent.search(EMPTY, NOT_EMPTY_COLLECTION, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SEARCH_QUERY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSearch2DefenseTypesNull() throws Exception {
		try {
			searchComponent.search(NOT_EMPTY, NULL_COLLECTION, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ARGUMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSearch2DefenseTypesEmpty() throws Exception {
		try {
			searchComponent.search(NOT_EMPTY, EMPTY_COLLECTION, 1);
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ARGUMENT_EMPTY, ex.getErrorCode());
		}
	}

}
