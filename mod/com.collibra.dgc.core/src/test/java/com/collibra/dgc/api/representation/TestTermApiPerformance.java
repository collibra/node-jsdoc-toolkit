package com.collibra.dgc.api.representation;

import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.AbstractDGCBootstrappedApiPerformanceTest;
import com.collibra.dgc.annotation.Bootstrap;
import com.collibra.dgc.annotation.PerformanceTest;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

@Bootstrap("22000rep")
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermApiPerformance extends AbstractDGCBootstrappedApiPerformanceTest {

	@PerformanceTest(repetitions = 3)
	public void testAddTerm() {
		long start = System.currentTimeMillis();
		Vocabulary voc = vocabularyComponent.getVocabulary("186bbf42-8819-46ed-86f7-373edf36ec60");
		Term term = termComponent.addTerm(voc.getId().toString(), "Country  " + randomText());
		report("testAddTerm", System.currentTimeMillis() - start);
	}
}
