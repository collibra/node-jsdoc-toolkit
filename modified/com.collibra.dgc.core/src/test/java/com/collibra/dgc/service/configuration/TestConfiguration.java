/**
 * 
 */
package com.collibra.dgc.service.configuration;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserFactory;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author fvdmaele
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestConfiguration extends AbstractServiceTest {

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	protected UserFactory userFactory;

	private Vocabulary vocabulary;

	@Test
	public void testConfigurationServiceInheritance() throws Exception {
		setup();

		// Find the term
		Term t1 = representationService.findTermBySignifier(vocabulary, "Term1");
		assertNotNull(t1);

		User user = userFactory.createUser("fvdmaele", "fvdmaele", "Felix", "Van de Maele", "felix@collibra.com");

		// test string configuration

		configurationService.setProperty("test1/test2", "DefaultValue");
		configurationService.setProperty("test1/test2", "DefaultResourceValue", t1.getId().toString(), null);
		configurationService.setProperty("test1/test2", "DefaultUserValue", null, user);
		configurationService.setProperty("test1/test2", "ResourceUserSpecificValue", t1.getId().toString(), user);
		configurationService.setProperty("test1/test3", "DefaultResourceValue", t1.getId().toString(), null);
		configurationService.setProperty("test1/test4", "DefaultValue");

		assertEquals("DefaultValue", configurationService.getString("test1/test2", null, null));
		assertEquals("DefaultValue", configurationService.getString("test1/test2"));
		assertEquals("DefaultResourceValue", configurationService.getString("test1/test2", t1.getId().toString(), null));
		assertEquals("DefaultUserValue", configurationService.getString("test1/test2", null, user));
		assertEquals("ResourceUserSpecificValue",
				configurationService.getString("test1/test2", t1.getId().toString(), user));
		assertNull(configurationService.getString("test1/test3", t1.getId().toString(), user));
		assertEquals("DefaultResourceValue",
				configurationService.getLocalizedString("test1/test3", t1.getId().toString(), user));
		assertEquals("DefaultValue",
				configurationService.getLocalizedString("test1/test4", t1.getId().toString(), user));

		// test integer config
		configurationService.setProperty("test1/test2/integer", 1);
		configurationService.setProperty("test1/test2/integer", 2, t1.getId().toString(), null);
		configurationService.setProperty("test1/test2/integer", 3, null, user);
		configurationService.setProperty("test1/test2/integer", 4, t1.getId().toString(), user);
		configurationService.setProperty("test1/test2/integer2", 5, t1.getId().toString());
		configurationService.setProperty("test1/test2/integer3", 6);

		assertEquals(new Integer(1), configurationService.getInteger("test1/test2/integer", null, null));
		assertEquals(new Integer(1), configurationService.getInteger("test1/test2/integer"));
		assertEquals(new Integer(2),
				configurationService.getInteger("test1/test2/integer", t1.getId().toString(), null));
		assertEquals(new Integer(3), configurationService.getInteger("test1/test2/integer", null, user));
		assertEquals(new Integer(4),
				configurationService.getInteger("test1/test2/integer", t1.getId().toString(), user));
		assertNull(configurationService.getInteger("test1/test2/integer2", t1.getId().toString(), user));
		assertEquals(new Integer(5),
				configurationService.getLocalizedInteger("test1/test2/integer2", t1.getId().toString(), user));
		assertEquals(new Integer(6),
				configurationService.getLocalizedInteger("test1/test2/integer3", t1.getId().toString(), user));
		assertNull(configurationService.getInteger("test1/test2/integer3", t1.getId().toString(), user));
	}

	// @Test
	// public void testConfigurationServicePerformance() throws Exception {
	//
	// String value =
	// "Sabam maakt zich op om juridische stappen te ondernemen tegen de Belgische internet service providers. De enige vraag die nog rest is of de auteursrechtenvereniging één of meerdere isp’s voor de rechter zal slepen. Sabam eist 3,4 procent van de jaarlijkse abonnementsprijs die breedbandgebruikers betalen in ons land. Nu het Europees Hof van Justitie geoordeeld heeft dat je sociale netwerken (Netlog) en internet service providers (Scarlet) niet kan verplichten om op eigen kosten een filter te installeren die de transfer van illegale muziek- en filmbestanden blokkeert, en nu het duidelijk is dat de Belgische ips’s weigeren om uit eigen beweging 3,4 procent van de prijs voor een breedbandabonnement op te hoesten ter compensatie van de auteurs, wil Sabam een stapje verder gaan door juridische stappen te ondernemen tegen één of meerdere internetleveranciers.";
	//
	// long start = System.currentTimeMillis();
	// for(int i = 0; i < 25; i++) {
	// for(int j = 0; j < 15; j++) {
	// for(int k = 0; k < 50; k++)
	// configurationService.setString("test/test" + i + "/btest" + j + "/ctest" + k, value);
	// }
	// }
	// long stop = System.currentTimeMillis();
	// System.out.println("[Loading time:] " + (stop - start));
	//
	// // just to write it to the xml and check manually if needed
	// configurationService.flush();
	//
	// testStringQueryTime("test/test2/btest2/ctest9");
	// testStringQueryTime("test/test8/btest9/ctest39");
	// testStringQueryTime("test/test18/btest7/ctest25");
	// testStringQueryTime("test/test21/btest12/ctest29");
	// testStringQueryTime("test/test24/btest12/ctest45");
	// }

	@Test
	public void testConfigurationBatch() {
		setup();

		// Find the term
		Term t1 = representationService.findTermBySignifier(vocabulary, "Term1");
		assertNotNull(t1);

		User user = userFactory.createUser("fvdmaele", "fvdmaele", "Felix", "Van de Maele", "felix@collibra.com");

		configurationService.setProperty("test2/test2", "DefaultValue");
		configurationService.setProperty("test2/test2", "DefaultResourceValue", t1.getId().toString(), null);
		configurationService.setProperty("test2/test2", "DefaultUserValue", null, user);
		configurationService.setProperty("test2/test2", "ResourceUserSpecificValue", t1.getId().toString(), user);
		configurationService.setProperty("test2/test3", "DefaultValue2");

		Map<String, String> results = null;
		results = configurationService.getConfigurationSection("test2");
		assertEquals("DefaultValue", results.get("test2/test2"));
		assertEquals("DefaultValue2", results.get("test2/test3"));

		results = configurationService.getConfigurationSection("test2", t1.getId().toString(), user);
		assertEquals("ResourceUserSpecificValue", results.get("test2/test2"));
		assertNull(results.get("test2/test3"));
	}

	private void setup() {
		vocabulary = representationFactory.makeVocabulary(sp, "My Test", "Synonyms Test");
		representationFactory.makeTerm(vocabulary, "Term1");
		representationFactory.makeTerm(vocabulary, "Term2");

		communityService.save(sp);
		resetTransaction();
	}

	private void testStringQueryTime(String path) {
		long start = System.currentTimeMillis();
		configurationService.getString(path);
		long stop = System.currentTimeMillis();
		assertTrue((stop - start) < 10);
		System.out.println("[Query time:] " + (stop - start));
	}
}
