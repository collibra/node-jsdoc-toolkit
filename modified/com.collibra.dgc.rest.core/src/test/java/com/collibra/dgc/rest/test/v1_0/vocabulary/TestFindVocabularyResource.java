package com.collibra.dgc.rest.test.v1_0.vocabulary;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestFindVocabularyResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestFindVocabularyResource.class);

	/* Read */

	@Test
	public void testFindVocabularyReferencesByName() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindVocabularyReferencesByName()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/find/")
						.queryParam("searchName", "sbvr").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs, vocabularyComponent.findVocabulariesContainingName("sbvr", 0, 0));

				log.info("Done testFindVocabularyReferencesByName()");
			}
		});
	}

	@Test
	public void testFindVocabularyReferencesByNameOffsetAndNumber() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindVocabularyReferencesByNameOffsetAndNumber()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/find/")
						.queryParam("searchName", "sbvr").queryParam("offset", "1").queryParam("number", "2")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs, vocabularyComponent.findVocabulariesContainingName("sbvr", 1, 2));

				log.info("Done testFindVocabularyReferencesByNameOffsetAndNumber()");
			}
		});
	}

	@Test
	public void testFindVocabulariesByName() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindVocabulariesByName()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/find/full")
						.queryParam("searchName", "sbvr").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs, vocabularyComponent.findVocabulariesContainingName("sbvr", 0, 0));

				log.info("Done testFindVocabulariesByName()");
			}
		});
	}

	@Test
	public void testFindVocabulariesByNameOffsetAndNumber() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testFindVocabulariesByNameOffsetAndNumber()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/find/full")
						.queryParam("searchName", "sbvr").queryParam("offset", "1").queryParam("number", "1")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs, vocabularyComponent.findVocabulariesContainingName("sbvr", 1, 1));

				log.info("Done testFindVocabulariesByNameOffsetAndNumber()");
			}
		});
	}
}
