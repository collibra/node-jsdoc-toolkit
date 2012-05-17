package com.collibra.dgc.rest.test.v1_0.community;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.rest.core.v1_0.dto.Communities;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReferences;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestFindCommunityResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestFindCommunityResource.class);

	/* Read */

	@Test
	public void testFindCommunityReferencesByName() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindCommunityReferencesByName()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/find/")
						.queryParam("searchName", "sbvr").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				CommunityReferences smrs = clientResponse.getEntity(CommunityReferences.class);

				testCommunityReferences(smrs, communityComponent.findCommunitiesContainingName("sbvr", 0, 0));

				log.info("Done testFindCommunityReferencesByName()");
			}
		});
	}

	@Test
	public void testFindCommunityReferencesByNameOffsetAndNumber() {

		log.info("Starting testFindCommunityReferencesByNameOffsetAndNumber()");

		/* Read */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/community/find").queryParam("searchName", "sbvr")
				.queryParam("offset", "1").queryParam("number", "2").accept(MediaType.APPLICATION_XML_TYPE)
				.get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

		log.info("Done testFindCommunityReferencesByNameOffsetAndNumber()");
	}

	@Test
	public void testFindCommunitiesByName() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindCommunityReferencesByName()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/find/full")
						.queryParam("searchName", "sbvr").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Communities sms = clientResponse.getEntity(Communities.class);

				testCommunities(sms, communityComponent.findCommunitiesContainingName("sbvr", 0, 0));

				log.info("Done testFindCommunityReferencesByName()");
			}
		});
	}

	@Test
	public void testFindCommunitiesByNameOffsetAndNumber() {

		log.info("Starting testFindCommunitiesByNameOffsetAndNumber()");

		/* Read */
		WebResource webResource = resource();
		ClientResponse clientResponse = webResource.path("/1.0/community/find/full").queryParam("searchName", "sbvr")
				.queryParam("offset", "1").queryParam("number", "1").accept(MediaType.APPLICATION_XML_TYPE)
				.get(ClientResponse.class);

		/* Test */
		assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

		log.info("Done testFindCommunityReferencesByNameOffsetAndNumber()");
	}

}