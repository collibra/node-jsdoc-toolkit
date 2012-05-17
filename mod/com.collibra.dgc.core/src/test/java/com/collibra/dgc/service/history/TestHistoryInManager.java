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
public class TestHistoryInManager extends AbstractServiceTest {

	// private static final String USER_1 = "user1";
	//
	// @Test
	// public void testHistoryEntriesByUserAndType() {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// vocabulary = representationService.findLatestVocabularyByResourceId(vocabulary.getResourceId());
	// Term vehicle = representationFactory.makeTerm(vocabulary, "Vehicle");
	//
	// sc = communityService.findLatestAvailableSemanticCommunity(sc.getResourceId());
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// List<Object[]> tuples = historyService.listAllHistoryEntriesByKindAndUser();
	// for (Object[] tuple : tuples) {
	// System.out.println(tuple);
	// }
	// }
	//
	// @Test
	// public void testHistoryEntries() {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// vocabulary = representationService.findLatestVocabularyByResourceId(vocabulary.getResourceId());
	//
	// // we expect car and vocabulary version to be 1 because both are just created.
	// assertEquals(1, car.getVersion().intValue());
	// assertEquals(1, vocabulary.getVersion().intValue());
	// // assertEquals(1, car.getMeaning().getVersion().intValue());
	//
	// // check history dao methods
	// List<HistoryEntry> resList = historyService.findHistoryEntryByResourceId(car.getResourceId());
	// assertEquals(2, resList.size());
	//
	// List<HistoryEntry> vidList = historyService.findHistoryEntry(car.getResourceId(), car.getId());
	// assertEquals(2, vidList.size());
	// }
	//
	// @Test
	// public void testGetLogEntriesForUserWithGivenRole() {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	// Term bus = representationFactory.makeTerm(vocabulary, "Bus");
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// historyFactory.makeHistoryEntry(bus, "User1", "Comment 1");
	// communityService.save(sc);
	// resetTransaction();
	//
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// bus = representationService.findLatestTermByResourceId(bus.getResourceId());
	//
	// // User 1 as steward for car.
	// rightsService.addMember(USER_1, Constants.STEWARD, car);
	// rightsService.addMember(USER_1, Constants.STAKEHOLDER, car);
	// rightsService.addMember(USER_1, Constants.STEWARD, bus);
	//
	// List<HistoryEntry> entries = historyService.listAllHistoryEntriesByDateForUserWithRole(USER_1,
	// Constants.STEWARD, 10);
	// assertEquals(2, entries.size());
	//
	// entries = historyService.listAllHistoryEntriesByDateForUserWithRole(USER_1, Constants.STAKEHOLDER, 10);
	// assertEquals(1, entries.size());
	//
	// entries = historyService.listAllHistoryEntriesByDate(USER_1, 10);
	// Assert.assertTrue(entries.size() > 0);
	// }
	//
	// @Test
	// public void testHistoryLevel() {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 1").setLevel(1);
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 2").setLevel(2);
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 3").setLevel(2);
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 4").setLevel(3);
	// historyFactory.makeHistoryEntry(car, "User1", "Comment 5").setLevel(3);
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// vocabulary = representationService.findLatestVocabularyByResourceId(vocabulary.getResourceId());
	//
	// // we expect car and vocabulary version to be 1 because both are just created.
	// assertEquals(1, car.getVersion().intValue());
	// assertEquals(1, vocabulary.getVersion().intValue());
	// // assertEquals(1, car.getMeaning().getVersion().intValue());
	//
	// // Additional 3 history entries come from creation of communities and vocabulary itself.
	// // check history dao methods
	// // NOTE: Number of history entries can be a lot more because history is added to vocabulary and community
	// // creations.
	// List<HistoryEntry> resList = historyService.listAllHistoryEntriesByDate(10, 1);
	// assertForHistoryLevel(resList, 1);
	//
	// resList = historyService.listAllHistoryEntriesByDate(10, 2);
	// assertForHistoryLevel(resList, 2);
	//
	// resList = historyService.listAllHistoryEntriesByDate(10, 3);
	// assertForHistoryLevel(resList, 3);
	// }
	//
	// @Test
	// public void testListFromDate() throws Exception {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c1 = Calendar.getInstance();
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// HistoryEntry entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// getCurrentSession().update(entry);
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c2 = Calendar.getInstance();
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	// getCurrentSession().update(entry);
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c3 = Calendar.getInstance();
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 3");
	// getCurrentSession().update(entry);
	//
	// resetTransaction();
	//
	// // Now check how many entries you will find for each date.
	// List<HistoryEntry> entries = historyService.listHistoryEntriesFromDate(c3, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(1, entries.size());
	//
	// entries = historyService.listHistoryEntriesFromDate(c2, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(2, entries.size());
	//
	// entries = historyService.listHistoryEntriesFromDate(c1, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(3, entries.size());
	// }
	//
	// @Test
	// public void testListUntilDate() throws Exception {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// List<HistoryEntry> entries = historyService.listAllHistoryEntriesByDate(100);
	// int size = entries.size();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// HistoryEntry entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// getCurrentSession().update(entry);
	// Calendar c1 = Calendar.getInstance();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	// getCurrentSession().update(entry);
	// Calendar c2 = Calendar.getInstance();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 3");
	// getCurrentSession().update(entry);
	// Calendar c3 = Calendar.getInstance();
	//
	// resetTransaction();
	//
	// // Now check how many entries you will find for each date.
	// entries = historyService.listHistoryEntriesUntilDate(c3, 100, 10);
	// assertEquals(size + 3, entries.size());
	//
	// entries = historyService.listHistoryEntriesUntilDate(c2, 100, 10);
	// assertEquals(size + 2, entries.size());
	//
	// entries = historyService.listHistoryEntriesUntilDate(c1, 100, 10);
	// assertEquals(size + 1, entries.size());
	// }
	//
	// @Test
	// public void testListBetweenDates() throws Exception {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c1 = Calendar.getInstance();
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// HistoryEntry entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// getCurrentSession().update(entry);
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c2 = Calendar.getInstance();
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	// getCurrentSession().update(entry);
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c3 = Calendar.getInstance();
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 3");
	// getCurrentSession().update(entry);
	//
	// resetTransaction();
	//
	// // Now check how many entries you will find for each date.
	// List<HistoryEntry> entries = historyService.listHistoryEntriesBetweenDates(c1, c3, 100,
	// HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(3, entries.size());
	//
	// entries = historyService.listHistoryEntriesBetweenDates(c1, c2, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(2, entries.size());
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// Calendar c4 = Calendar.getInstance();
	// entries = historyService.listHistoryEntriesBetweenDates(c3, c4, 100, HistoryEntry.DEFAULT_LEVEL);
	// assertEquals(1, entries.size());
	// }
	//
	// @Test
	// public void testListOrderedByDate() throws Exception {
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// HistoryEntry entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 1");
	// getCurrentSession().update(entry);
	//
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 2");
	// getCurrentSession().update(entry);
	//
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 3");
	// getCurrentSession().update(entry);
	//
	// entry = historyFactory.makeHistoryEntry(car, "User1", "Comment 4");
	// getCurrentSession().update(entry);
	//
	// resetTransaction();
	//
	// // Now check how many entries you will find based on limit
	// List<HistoryEntry> entries = historyService.listAllHistoryEntriesByDate(3);
	// assertEquals(3, entries.size());
	//
	// // Now all entries should be found.
	// entries = historyService.listAllHistoryEntriesByDate(10);
	// Assert.assertEquals(10, entries.size());
	// }
	//
	// @Ignore
	// @Test
	// public void testListHistoryEntriesOfInterestForUser() throws InterruptedException {
	// // Get the starting date
	// Calendar startDate = Calendar.getInstance();
	//
	// // Create resources
	// Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	// Term bus = representationFactory.makeTerm(vocabulary, "Bus");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// // Create memberships
	// String USER = "user";
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// bus = representationService.findLatestTermByResourceId(bus.getResourceId());
	// rightsService.addMember(USER, Constants.ADMIN, car);
	// rightsService.addMember(USER, Constants.STEWARD, car);
	// rightsService.addMember(USER, Constants.STEWARD, bus);
	// resetTransaction();
	//
	// // Create history entry
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// HistoryEntry entry = historyFactory.makeHistoryEntry(car, USER, "Comment 1");
	// getCurrentSession().update(entry);
	// resetTransaction();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// // Take the time in between for query later
	// Calendar inbetween = Calendar.getInstance();
	//
	// // Create history entry
	// bus = representationService.findLatestTermByResourceId(bus.getResourceId());
	// entry = historyFactory.makeHistoryEntry(bus, USER, "Comment 2");
	// getCurrentSession().update(entry);
	// resetTransaction();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// // Total 2 history entries between start time and current time.
	// Collection<HistoryEntry> entries = historyService.listHistoryEntriesOfInterestForUser(USER, startDate,
	// Calendar.getInstance());
	// Assert.assertEquals(2, entries.size());
	//
	// // Total 1 history entry between start time and in between.
	// entries = historyService.listHistoryEntriesOfInterestForUser(USER, startDate, inbetween);
	// Assert.assertEquals(1, entries.size());
	//
	// // Check after adding attribute
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// representationFactory.makeDefinition(car.getVocabulary(), car, "Testing...");
	// car = representationService.saveTerm(car);
	// entry = historyFactory.makeHistoryEntry(car, USER, "added definition");
	// getCurrentSession().update(entry);
	// resetTransaction();
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// // Check after rename
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// representationService.changeSignifier(car, "New Car");
	//
	// // Wait for 1 second
	// Thread.currentThread().sleep(1000);
	//
	// entries = historyService.listHistoryEntriesOfInterestForUser(USER, startDate, Calendar.getInstance());
	// for (HistoryEntry historyEntry : entries) {
	// System.out.println(historyEntry.getComment() + "     " + historyEntry.getLogTime().getTime());
	// }
	//
	// List<HistoryEntry> listEntries = new LinkedList<HistoryEntry>(entries);
	// for (int i = 0; i < listEntries.size() - 1; i++) {
	// Assert.assertTrue(listEntries.get(i).getLogTime().compareTo(listEntries.get(i + 1).getLogTime()) >= 0);
	// }
	// }
	//
	// private void assertForHistoryLevel(List<HistoryEntry> resList, int level) {
	// for (HistoryEntry entry : resList) {
	// Assert.assertTrue(entry.getLevel() <= level);
	// }
	// }
}
