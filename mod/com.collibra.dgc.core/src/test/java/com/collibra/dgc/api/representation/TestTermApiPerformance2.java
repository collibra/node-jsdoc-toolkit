package com.collibra.dgc.api.representation;

import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.AbstractDGCBootstrappedApiPerformanceTest;
import com.collibra.dgc.annotation.Bootstrap;
import com.collibra.dgc.annotation.PerformanceTest;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * {@link TermComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@Bootstrap("basic")
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermApiPerformance2 extends AbstractDGCBootstrappedApiPerformanceTest {

	@PerformanceTest(repetitions = 3)
	public void testAddTerm() {
		long start = System.currentTimeMillis();
		Vocabulary voc = vocabularyComponent.getVocabulary("212c3fda-697d-423a-82e7-21cbf4c77aa1");
		Term term = termComponent.addTerm(voc.getId().toString(), "Country  " + randomText());
		report("testAddTerm", System.currentTimeMillis() - start);
	}
}
