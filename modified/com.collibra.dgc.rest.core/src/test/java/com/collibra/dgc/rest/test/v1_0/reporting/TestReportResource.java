/**
 * 
 */
package com.collibra.dgc.rest.test.v1_0.reporting;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author fvdmaele
 * 
 */
@Ignore
public class TestReportResource extends AbstractBootstrappedRestTest {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestReportResource.class);

	long start;
	long duration;

	@Test
	public void testGetTermReport() {

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			// {
			// "reportconfig": {
			// "term": {
			// "visible": true,
			// "vocabulary": {
			// "visible": true,
			// "name": "term_voc",
			// "filter": {}
			// },
			// "name": "term",
			// "filter": {
			// "sortDESC": true,
			// "value": "DNS",
			// "operator": "INCLUDES"
			// }
			// }
			// }
			// }
			String reportConfig = "{\"reportconfig\":{\"term\":{\"visible\":true,\"vocabulary\":{\"visible\":true,\"name\":\"term_voc\",\"filter\":{}},\"name\":\"term\"}}}";

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				log.info("Starting testGetTermReport()");

				/* Read */
				WebResource webResource = resource();

				start = System.currentTimeMillis();
				ClientResponse clientResponse = webResource.path("/1.0/report").queryParam("config", reportConfig)
						.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
				duration = (System.currentTimeMillis() - start);
				System.out.println("[testGetTermReport] " + duration + " (ms)");

				/* Test */
				String reportJSON = clientResponse.getEntity(String.class);
				assertEquals(ClientResponse.Status.OK, clientResponse.getClientResponseStatus());
				System.out.println("Result:" + reportJSON);

				try {
					JSONObject result = (JSONObject) new JSONParser().parse(reportJSON);
				} catch (ParseException e) {
					assertTrue(false);
				}
				//
				// VocabularyReferences vrs = clientResponse.getEntity(VocabularyReferences.class);
				//
				// testVocabularyReferences(vrs, vocabularyComponent.findVocabulariesContainingName("sbvr", 0, 0));

				log.info("Done testFindVocabularyReferencesByName()");
			}

			private void assertTrue(boolean b) {
				// TODO Auto-generated method stub

			}
		});
	}
}
