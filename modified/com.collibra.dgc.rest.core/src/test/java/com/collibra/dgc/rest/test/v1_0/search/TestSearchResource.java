package com.collibra.dgc.rest.test.v1_0.search;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItems;
import com.collibra.dgc.rest.core.v1_0.resource.SearchResource;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestSearchResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestSearchResource.class);
	private static final int defaultMax = 25;

	// WARNING: this test run the full re-index command. It must be the first test to be run.
	// This method is not set as @Before to do not re-index between each test.
	@Test
	public void doFullReIndex() {

		log.info("Starting doFullReIndex()");

		WebResource webResource = resource();
		webResource.path("/1.0/search/re-index").post(String.class);

		log.info("Done doFullReIndex()");
	}

	@Test
	public void testSearchSomethingThatDoesntExist() throws Exception {

		log.info("Starting testSearchSomethingThatDoesntExist");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/")
				.queryParam("query", "somethingThatDoesntExistxxxxxxxxx").accept(MediaType.APPLICATION_XML_TYPE)
				.get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

		log.info("Done testSearchSomethingThatDoesntExist");
	}

	@Test
	public void testSearchTerm() throws Exception {

		log.info("Starting testSearchTerm");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "fact")
				.queryParam("types", "TE").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search(
				"fact", SearchResource.convertTypesStringToCollection("TE"), defaultMax);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchTerm");
	}

	@Test
	public void testSearchAttribute() throws Exception {

		log.info("Starting testSearchAttribute");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "r*")
				.queryParam("types", "AT").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

		// TODO add attribute to the bootstrap to test this or made a REST specific bootstrap

		// SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);
		//
		// Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems =
		// searchComponent.search("r*",
		// SearchResource.convertTypesStringToCollection("AT"), defaultMax);
		//
		// testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchAttribute");
	}

	@Test
	public void testSearchName() throws Exception {

		log.info("Starting testSearchName");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "n*")
				.queryParam("types", "NA").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

		log.info("Done testSearchName");
	}

	@Test
	public void testSearchCommunity() throws Exception {

		log.info("Starting testSearchCommunity");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "sbvr")
				.queryParam("types", "CO").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search(
				"sbvr", SearchResource.convertTypesStringToCollection("CO"), defaultMax);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchCommunity");
	}

	@Test
	public void testSearchVocabulary() throws Exception {

		log.info("Starting testSearchVocabulary");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "sbvr")
				.queryParam("types", "VC").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search(
				"sbvr", SearchResource.convertTypesStringToCollection("VC"), defaultMax);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchVocabulary");
	}

	@Test
	public void testSearchTermAndAttribute() throws Exception {

		log.info("Starting testSearchTermAndAttribute");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "r*")
				.queryParam("types", "TE,AT").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search("r*",
				SearchResource.convertTypesStringToCollection("TE,AT"), defaultMax);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchTermAndAttribute");
	}

	@Test
	public void testSearchTermAndAttributeMax100() throws Exception {

		log.info("Starting testSearchTermAndAttributeMax100");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "r*")
				.queryParam("types", "TE,AT").queryParam("max", "100").accept(MediaType.APPLICATION_XML_TYPE)
				.get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search("r*",
				SearchResource.convertTypesStringToCollection("TE,AT"), 100);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchTermAndAttributeMax100");
	}

	@Test
	public void testSearchAllTypeMax3() throws Exception {

		log.info("Starting testSearchAllTypeMax3");

		/* Search */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/search/").queryParam("query", "r*")
				.queryParam("max", "3").accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

		SearchResultItems sris = clientResponse.getEntity(SearchResultItems.class);

		Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems = searchComponent.search("r*",
				3);

		testSearchResultItems(sris, searchResultItems);

		log.info("Done testSearchAllTypeMax3");
	}
}
