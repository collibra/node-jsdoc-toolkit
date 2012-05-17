package com.collibra.dgc.rest.test.v1_0.attribute;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.rest.core.v1_0.dto.AttributeType;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeTypes;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author pmalarme
 * 
 */
@Ignore
public class TestGetAttributeType extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestGetAttributeType.class);

	/* READ */

	@Test
	public void testGetAttributeType() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAttributeType()");

				/* Init */
				com.collibra.dgc.core.model.representation.Term attributeTypeLabel = attributeTypeComponent
						.getDefinitionAttributeType();

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource
						.path("/1.0/attribute_type/" + attributeTypeLabel.getId().toString())
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at, attributeTypeLabel, false);

				log.info("Done testGetAttributeType()");
			}
		});
	}

	@Test
	public void testGetAttributeTypeBySignifier() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAttributeTypeBySignifier()");

				/* Init */
				com.collibra.dgc.core.model.representation.Term attributeTypeLabel = attributeTypeComponent
						.getDescriptionAttributeType();

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/")
						.queryParam("signifier", attributeTypeLabel.getSignifier())
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at,
						attributeTypeComponent.getAttributeTypeBySignifier(attributeTypeLabel.getSignifier()), false);

				log.info("Starting testGetAttributeTypeBySignifier()");
			}
		});
	}

	@Test
	public void testGetDefinitionAttributeType() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetDefinitionAttributeType()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/definition")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at, attributeTypeComponent.getDefinitionAttributeType(), false);

				log.info("Starting testGetDefinitionAttributeType()");
			}
		});
	}

	@Test
	public void testGetDescriptionAttributeType() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetDescriptionAttributeType()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/description")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at, attributeTypeComponent.getDescriptionAttributeType(), false);

				log.info("Starting testGetDescriptionAttributeType()");
			}
		});
	}

	@Test
	public void testGetExampleAttributeType() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetExampleAttributeType()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/example")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at, attributeTypeComponent.getExampleAttributeType(), false);

				log.info("Starting testGetExampleAttributeType()");
			}
		});
	}

	@Test
	public void testGetNoteAttributeType() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetNoteAttributeType()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/note")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeType at = clientResponse.getEntity(AttributeType.class);

				testAttributeType(at, attributeTypeComponent.getNoteAttributeType(), false);

				log.info("Starting testGetNoteAttributeType()");
			}
		});
	}

	// TODO add a test for the description and allowed values

	@Test
	public void testGetAttributeTypes() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetAttributeTypes()");

				/* Read */
				WebResource webResource = resource();
				ClientResponse clientResponse = webResource.path("/1.0/attribute_type/all")
						.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);

				/* Test */
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());

				AttributeTypes ats = clientResponse.getEntity(AttributeTypes.class);

				testAttributeTypes(ats, attributeTypeComponent.getAttributeTypes());

				log.info("Starting testGetAttributeTypes()");
			}
		});
	}
}
