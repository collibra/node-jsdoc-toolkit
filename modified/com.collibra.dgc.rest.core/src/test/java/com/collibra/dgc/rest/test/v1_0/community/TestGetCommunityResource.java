package com.collibra.dgc.rest.test.v1_0.community;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.rest.core.v1_0.dto.Communities;
import com.collibra.dgc.rest.core.v1_0.dto.Community;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestGetCommunityResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestGetCommunityResource.class);

	/* READ */

	@Test
	public void testGetCommunity() throws UniformInterfaceException, UnsupportedEncodingException {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetCommunity()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByUri("http://www.collibra.com/SBVR_ENGLISH_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + community.getId().toString())
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Community c = clientResponse.getEntity(Community.class);

				testCommunity(c, community, false);

				log.info("Done testGetCommunity()");
			}
		});
	}

	@Test
	public void testGetCommunityByUri() throws UniformInterfaceException, UnsupportedEncodingException {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetCommunityByUri()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByUri("http://www.collibra.com/SBVR_ENGLISH_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/")
						.queryParam("uri", "http://www.collibra.com/SBVR_ENGLISH_COMMUNITY")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Community c = clientResponse.getEntity(Community.class);

				testCommunity(c, community, false);

				log.info("Done testGetCommunityByUri()");
			}
		});
	}

	@Test
	public void testGetCommunityByName() throws UniformInterfaceException, UnsupportedEncodingException {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetCommunityByName()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/")
						.queryParam("name", "English SBVR Community").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Community c = clientResponse.getEntity(Community.class);

				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByName("English SBVR Community");

				testCommunity(c, community, false);

				log.info("Done testGetCommunityByName()");
			}
		});
	}

	@Test
	public void testGetCommunityByNameAndUri() throws UniformInterfaceException, UnsupportedEncodingException {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetCommunityByNameAndUri()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/")
						.queryParam("name", "English SBVR Community").queryParam("uri", "some uri here please !")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.BAD_REQUEST, clientResponse.getClientResponseStatus());

				log.info("Done testGetCommunityByNameAndUri()");
			}
		});
	}

	@Test
	public void testGetCommunityParent() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetCommunityParent()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByUri("http://www.collibra.com/SBVR_ENGLISH_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + community.getId().toString() + "/parent")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				if (clientResponse.getClientResponseStatus() == ClientResponse.Status.NO_CONTENT) {

					assertEquals(null, community.getParentCommunity());

				} else {

					assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

					Community sm = clientResponse.getEntity(Community.class);

					testCommunity(sm, community.getParentCommunity(), true);
				}

				log.info("Done testGetCommunityParent()");
			}
		});
	}

	@Test
	public void testGetSubCommunityReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetSubCommunityReferences()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByUri("http://www.collibra.com/METAMODEL_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + community.getId().toString() + "/sub-communities")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				CommunityReferences crs = clientResponse.getEntity(CommunityReferences.class);

				testCommunityReferences(crs, communityComponent.getCommunity(community.getId().toString())
						.getSubCommunities());

				log.info("Done testGetSubCommunityReferences()");
			}
		});
	}

	@Test
	public void testGetSubCommunities() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetSubCommunities()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community community = communityComponent
						.getCommunityByUri("http://www.collibra.com/METAMODEL_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + community.getId().toString() + "/sub-communities/full")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Communities cs = clientResponse.getEntity(Communities.class);

				testCommunities(cs, communityComponent.getCommunity(community.getId().toString())
						.getSubCommunities());

				log.info("Done testGetSubCommunities()");
			}
		});
	}

	@Test
	public void testGetVocabularyReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetVocabularyReferences()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community Community = communityComponent
						.getCommunityByUri("http://www.collibra.com/SBVR_ENGLISH_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + Community.getId().toString() + "/vocabularies")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs, communityComponent.getCommunity(Community.getId().toString())
						.getVocabularies());

				log.info("Done testGetVocabularyReferences()");
			}
		});
	}

	@Test
	public void testGetVocabularies() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetVocabularies()");

				/* Init */
				// We get the SBVR Community by URI to have its resourceId and get a REST community by
				// id. I do that cause the resourceId can change but the URI would be the same among various DGC
				// versions.
				com.collibra.dgc.core.model.community.Community Community = communityComponent
						.getCommunityByUri("http://www.collibra.com/SBVR_ENGLISH_COMMUNITY");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/community/" + Community.getId().toString() + "/vocabularies/full")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs, communityComponent.getCommunity(Community.getId().toString())
						.getVocabularies());

				log.info("Done testGetVocabularies()");
			}
		});
	}

	@Test
	public void testGetAllCommunityReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunityReferences()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

				log.info("Done testGetAllCommunityReferences()");
			}
		});
	}

	@Test
	public void testGetAllCommunityReferencesIncludeMetaEcxludeSBVR() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunityReferencesIncludeSBVR()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all")
						.queryParam("excludeMeta", "false").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				CommunityReferences crs = clientResponse.getEntity(CommunityReferences.class);

				testCommunityReferences(crs, communityComponent.getCommunities(true, false));

				log.info("Done testGetAllCommunityReferencesIncludeSBVR()");
			}
		});
	}

	@Test
	public void testGetAllCommunityReferencesIncludeMetaIncludeSBVR() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunityReferencesIncludeSBVR()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all")
						.queryParam("excludeMeta", "false").queryParam("excludeSBVR", "false")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				CommunityReferences crs = clientResponse.getEntity(CommunityReferences.class);

				testCommunityReferences(crs, communityComponent.getCommunities(false, false));

				log.info("Done testGetAllCommunityReferencesIncludeSBVR()");
			}
		});
	}

	@Test
	public void testGetAllCommunities() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunities()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all/full")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.NO_CONTENT, clientResponse.getClientResponseStatus());

				log.info("Done testGetAllCommunities()");
			}
		});
	}

	@Test
	public void testGetAllCommunitiesIncludeMetaEcxludeSBVR() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunitiesIncludeSBVR()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all/full")
						.queryParam("excludeMeta", "false").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Communities cs = clientResponse.getEntity(Communities.class);

				testCommunities(cs, communityComponent.getCommunities(true, false));

				log.info("Done testGetAllCommunitiesIncludeSBVR()");
			}
		});
	}

	@Test
	public void testGetAllCommunitiesIncludeMetaIncludeSBVR() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllCommunitiesIncludeSBVR()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/community/all/full")
						.queryParam("excludeMeta", "false").queryParam("excludeSBVR", "false")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Communities cs = clientResponse.getEntity(Communities.class);

				testCommunities(cs, communityComponent.getCommunities(false, false));

				log.info("Done testGetAllCommunitiesIncludeSBVR()");
			}
		});
	}
}
