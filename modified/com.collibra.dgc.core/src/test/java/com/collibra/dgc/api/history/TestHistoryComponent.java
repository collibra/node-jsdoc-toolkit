package com.collibra.dgc.api.history;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.HistoryComponent;

/**
 * Tests for {@link HistoryComponent} APIs.
 * @author amarnath
 * 
 */
@Ignore
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestHistoryComponent extends AbstractDGCBootstrappedApiTest {
	// @Test
	// public void testFindBetweenDates() throws Exception {
	// SpeechCommunity community = createSpeechCommunity();
	//
	// Vocabulary voc = vocabularyComponent.addVocabulary(VOCABULARY_URI, VOCABULARY_NAME, community.getResourceId()
	// .toString());
	//
	// Calendar c1 = Calendar.getInstance();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	//
	// Calendar c2 = Calendar.getInstance();
	//
	// // Now check how many entries you will find for each date.
	// Collection<HistoryEntry> entries = historyComponent.findBetweenDates(c1, c2, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(2, entries.size());
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// representationComponent.addTerm(voc.getResourceId().toString(), "Term2");
	//
	// Calendar c3 = Calendar.getInstance();
	// entries = historyComponent.findBetweenDates(c2, c3, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(2, entries.size());
	//
	// entries = historyComponent.findBetweenDates(c1, c3, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(4, entries.size());
	// }
	//
	// @Test
	// public void testFindByDate() {
	// createVocabulary();
	// Collection<HistoryEntry> entriesByDate = historyComponent.findByDate(100);
	// Iterator<HistoryEntry> iterator = entriesByDate.iterator();
	// while (iterator.hasNext()) {
	// HistoryEntry current = iterator.next();
	// if (iterator.hasNext()) {
	// HistoryEntry next = iterator.next();
	// Assert.assertTrue(current.getLogTime().after(next.getLogTime()));
	// current = next;
	// }
	// }
	// }
	//
	// @Test
	// public void testFindByUser() {
	// createVocabulary();
	// Collection<HistoryEntry> entries = historyComponent.findByUser("admin", 100);
	// for (HistoryEntry entry : entries) {
	// Assert.assertEquals("admin", entry.getUserName());
	// }
	//
	// entries = historyComponent.findByUser("unknown", 100);
	// Assert.assertEquals(0, entries.size());
	// }
	//
	// @Test
	// public void testFindByUserAndRole() {
	// Vocabulary voc = createVocabulary();
	// rightsComponent.addMember("admin", Constants.STEWARD, voc.getResourceId().toString());
	// Collection<HistoryEntry> entries = historyComponent.findByUserAndRole("admin", Constants.STEWARD, 100);
	// Assert.assertEquals(1, entries.size());
	// Assert.assertEquals(voc.getResourceId(), entries.iterator().next().getResourceId());
	//
	// // Changed role will not fetch any history entries.
	// entries = historyComponent.findByUserAndRole("admin", Constants.ADMIN, 100);
	// Assert.assertEquals(0, entries.size());
	// }
	//
	// @Test
	// public void testFindFromDate() throws Exception {
	// SpeechCommunity community = createSpeechCommunity();
	//
	// Vocabulary voc = vocabularyComponent.addVocabulary(VOCABULARY_URI, VOCABULARY_NAME, community.getResourceId()
	// .toString());
	//
	// Calendar c1 = Calendar.getInstance();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	// Collection<HistoryEntry> entries = historyComponent.findFromDate(c1, 100, HistoryEntry.DEFAULT_LEVEL);
	// Assert.assertEquals(2, entries.size());
	// }
	//
	// @Test
	// public void testFindOfInterestForUser() throws Exception {
	// Vocabulary voc = createVocabulary();
	// rightsComponent.addMember("user1", Constants.ADMIN, voc.getResourceId().toString());
	//
	// Calendar c1 = Calendar.getInstance();
	// Term term = representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c2 = Calendar.getInstance();
	//
	// // Between c1 and c2 only one entry of interest for user1 because of voc change.
	// Collection<HistoryEntry> entries = historyComponent.findOfInterestForUser("user1", c1, c2);
	// Assert.assertEquals(1, entries.size());
	// Assert.assertEquals(voc.getResourceId(), entries.iterator().next().getResourceId());
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c3 = Calendar.getInstance();
	// rightsComponent.addMember("user2", Constants.ADMIN, term.getResourceId().toString());
	//
	// // Between c2 and c3 no entry of interest for user2.
	// entries = historyComponent.findOfInterestForUser("user2", c2, c3, 100, HistoryEntry.DEFAULT_LEVEL);
	// Assert.assertEquals(0, entries.size());
	//
	// // Between c1 and c2 only one entry of interest for user2 because of term got created.
	// entries = historyComponent.findOfInterestForUserWithLimit("user2", c1, c3, 100);
	// Assert.assertEquals(1, entries.size());
	// Assert.assertEquals(term.getResourceId(), entries.iterator().next().getResourceId());
	//
	// representationComponent.changeSignifier(term.getResourceId().toString(), "Term Renamed");
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c4 = Calendar.getInstance();
	//
	// // Between c3 and c4 no entry of interest for user1.
	// entries = historyComponent.findOfInterestForUser("user1", c3, c4, HistoryEntry.DEFAULT_LEVEL);
	// Assert.assertEquals(0, entries.size());
	//
	// // Between c3 and c4 only one entry of interest for user2 because of adding definition to term.
	// entries = historyComponent.findOfInterestForUser("user2", c3, c4);
	// Assert.assertEquals(1, entries.size());
	// Assert.assertEquals(term.getResourceId(), entries.iterator().next().getResourceId());
	// }
	//
	// @Test
	// public void testFindUntilDate() throws Exception {
	// Vocabulary voc = createVocabulary();
	// Calendar c1 = Calendar.getInstance();
	// Collection<HistoryEntry> entries = historyComponent.findUntilDate(c1, 100, HistoryEntry.DEFAULT_LEVEL);
	// Assert.assertTrue(entries.size() > 0);
	// int size = entries.size();
	//
	// representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c2 = Calendar.getInstance();
	// entries = historyComponent.findUntilDate(c2, 100, HistoryEntry.DEFAULT_LEVEL);
	// Assert.assertEquals(2, entries.size() - size);
	// }
	//
	// @Test
	// public void testGetTuplesByKindAndUser() {
	// Vocabulary voc = createVocabulary();
	// Term term = representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	// representationComponent.remove(term.getResourceId().toString());
	//
	// Collection<Object[]> entries = historyComponent.getTuplesByKindAndUser();
	// Assert.assertEquals(1, entries.size());
	// Assert.assertEquals("admin", entries.iterator().next()[0]); // User
	// Assert.assertEquals(new Long(1), entries.iterator().next()[2]); // Delete
	// }
	//
	// @Test
	// public void testGetForResource() {
	// Vocabulary voc = createVocabulary();
	// Collection<HistoryEntry> entries = historyComponent.getForResource(voc.getResourceId().toString());
	// Assert.assertEquals(1, entries.size());
	//
	// Term term = representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	// entries = historyComponent.getForResource(term.getResourceId().toString());
	// Assert.assertEquals(1, entries.size());
	//
	// entries = historyComponent.getForResource(voc.getResourceId().toString());
	// Assert.assertEquals(2, entries.size());
	//
	// entries = historyComponent.getForResource(voc.getResourceId().toString(), voc.getVersionedId());
	// Assert.assertEquals(1, entries.size());
	// }
	//
	// @Test
	// public void testGetRepresentation() {
	// Vocabulary voc = createVocabulary();
	// Term term = representationComponent.addTerm(voc.getResourceId().toString(), "Term");
	// Collection<HistoryEntry> entries = historyComponent.getForResource(term.getResourceId().toString());
	//
	// Representation rep = historyComponent.getRepresentation(entries.iterator().next().getId());
	// Assert.assertNotNull(rep);
	// Assert.assertEquals(term.getResourceId(), rep.getResourceId());
	// }
	//
	// @Test
	// public void testGetVocabulary() {
	// Vocabulary voc = createVocabulary();
	// Collection<HistoryEntry> entries = historyComponent.getForResource(voc.getResourceId().toString());
	// Vocabulary result = historyComponent.getVocabulary(entries.iterator().next().getId());
	// Assert.assertNotNull(result);
	// Assert.assertEquals(voc.getResourceId(), result.getResourceId());
	// }
}
