/**
 * 
 */
package com.collibra.dgc.service.history;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author fvdmaele
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestHistoryLogInManager extends AbstractServiceTest {
	//
	// @Test
	// public void testCreateHistoryEntries() {
	// String userName = "Felix";
	//
	// // create vocabulary and term
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	// HistoryEntry e1 = historyFactory.makeHistoryEntry(car, userName, MessageKeys.BSG_LOG_TERM_CREATED);
	//
	// communityService.save(sc);
	// vocabulary = representationService.findLatestVocabularyByResourceId(vocabulary.getResourceId());
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	//
	// // create a couple of history entriesAttribute
	// Attribute note = representationFactory.makeNote(vocabulary, car, "A note for a car");
	// HistoryEntry e2 = historyFactory.makeHistoryEntry(car, userName, MessageKeys.BSG_LOG_NOTE_ADDED);
	// car = representationService.saveTerm(car);
	// resetTransaction();
	// // validate
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// assertEquals(2, historyService.findHistoryEntry(car.getResourceId(), car.getId()).size());
	// assertEquals(2, historyService.findHistoryEntryByResourceId(car.getResourceId()).size());
	// }
}
