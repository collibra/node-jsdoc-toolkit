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
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestGetVocabularyResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestGetVocabularyResource.class);

	/* READ */

	@Test
	public void testGetVocabulary() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetVocabulary()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/SBVR.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString())
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabulary v = clientResponse.getEntity(Vocabulary.class);

				testVocabulary(v, vocabulary, false);

				log.info("Done testGetVocabulary()");
			}
		});
	}

	@Test
	public void testGetVocabularyByUri() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetVocabularyByUri()");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/")
						.queryParam("uri", "http://www.omg.org/spec/SBVR/20070901/SBVR.xml")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabulary v = clientResponse.getEntity(Vocabulary.class);

				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/SBVR.xml");

				testVocabulary(v, vocabulary, false);

				log.info("Done testGetVocabularyByUri()");
			}
		});
	}

	// TODO find a way to implement test for getVocabularyReferencesToIncorporate() and getVocabulariesToIncorporate()
	// TODO implement the test method for the preferred term and representation

	@Test
	public void testGetIncorporatedVocabularyReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetIncorporatedVocabularyReferences()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/SBVR.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString() + "/incorporated_vocabularies")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs,
						vocabularyComponent.getIncorporatedVocabularies(vocabulary.getId().toString(), false));

				log.info("Done testGetIncorporatedVocabularyReferences()");
			}
		});
	}

	@Test
	public void testGetIncorporatedVocabularyReferencesExcludeSbvr() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetIncorporatedVocabularyReferencesExcludeSbvr()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/SBVR.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString() + "/incorporated_vocabularies")
						.queryParam("excludeSBVR", "true").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs,
						vocabularyComponent.getIncorporatedVocabularies(vocabulary.getId().toString(), true));

				log.info("Done testGetIncorporatedVocabularyReferencesExcludeSbvr()");
			}
		});
	}

	@Test
	public void testGetIncorporatedVocabularies() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetIncorporatedVocabularies()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/SBVR.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString()
								+ "/incorporated_vocabularies/full").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs,
						vocabularyComponent.getIncorporatedVocabularies(vocabulary.getId().toString(), false));

				log.info("Done testGetIncorporatedVocabularies()");
			}
		});
	}

	@Test
	public void testGetIncorporatingVocabularyReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetIncorporatingVocabularyReferences()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/DescribingBusinessRules.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString()
								+ "/incorporating_vocabularies").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs,
						vocabularyComponent.getIncorporatingVocabularies(vocabulary.getId().toString()));

				log.info("Done testGetIncorporatingVocabularyReferences()");
			}
		});
	}

	@Test
	public void testGetIncorporatingVocabularies() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetIncorporatingVocabularies()");

				/* Init */
				// We get the SBVR vocabulary by URI to have its resourceId and therefore get the REST DTO vocabulary
				// object through the REST interface
				com.collibra.dgc.core.model.representation.Vocabulary vocabulary = vocabularyComponent
						.getVocabularyByUri("http://www.omg.org/spec/SBVR/20070901/DescribingBusinessRules.xml");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/vocabulary/" + vocabulary.getId().toString()
								+ "/incorporating_vocabularies/full").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs,
						vocabularyComponent.getIncorporatingVocabularies(vocabulary.getId().toString()));

				log.info("Done testGetIncorporatingVocabularies()");
			}
		});
	}

	// TODO find a way to implemet test for getPreferredTerm()

	// TODO implement get Terms

	@Test
	public void testGetAllVocabularyReferences() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllVocabularyReferences()");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/all")
						.queryParam("excludeMeta", "false").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);

				testVocabularyReferences(vrs, vocabularyComponent.getVocabularies());

				log.info("Done testGetAllVocabularyReferences()");
			}
		});
	}

	@Test
	public void testGetAllVocabularies() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAllVocabularies()");

				/* READ */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/vocabulary/all/full")
						.queryParam("excludeMeta", "false").accept(MediaType.APPLICATION_XML_TYPE)
						.get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				Vocabularies vs = clientResponse.getEntity(Vocabularies.class);

				testVocabularies(vs, vocabularyComponent.getVocabularies());

				log.info("Done testGetAllVocabularies()");
			}
		});
	}
}
