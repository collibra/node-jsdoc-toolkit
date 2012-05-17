package com.collibra.dgc.core.component.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.HistoryComponent;

/**
 * History API implementation.
 * @author amarnath
 * 
 */
@Service
public class HistoryComponentImpl implements HistoryComponent {
	//
	// @Autowired
	// private HistoryService historyService;
	//
	// @Transactional
	// public Collection<HistoryEntry> findBetweenDates(Calendar startDate, Calendar endDate, int count, int level) {
	// return historyService.listHistoryEntriesBetweenDates(startDate, endDate, count, level);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findByDate(int limit) {
	// return historyService.listAllHistoryEntriesByDate(limit);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findByDate(int limit, int level) {
	// return historyService.listAllHistoryEntriesByDate(limit, level);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findByUser(String username, int limit) {
	// return historyService.listAllHistoryEntriesByDate(username, limit);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findByUserAndRole(String username, String roleName, int limit) {
	// return historyService.listAllHistoryEntriesByDateForUserWithRole(username, roleName, limit);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findFromDate(Calendar startDate, int count, int level) {
	// return historyService.listHistoryEntriesFromDate(startDate, count, level);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate) {
	// return historyService.listHistoryEntriesOfInterestForUser(user, startDate, endDate);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate, int
	// level) {
	// return historyService.listHistoryEntriesOfInterestForUser(user, startDate, endDate, level);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate, int
	// count,
	// int level) {
	// return historyService.listHistoryEntriesOfInterestForUser(user, startDate, endDate, count, level);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findOfInterestForUserWithLimit(String user, Calendar startDate, Calendar endDate,
	// int count) {
	// return historyService.listHistoryEntriesOfInterestForUserWithLimit(user, startDate, endDate, count);
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> findUntilDate(Calendar endDate, int count, int level) {
	// return historyService.listHistoryEntriesUntilDate(endDate, count, level);
	// }
	//
	// @Transactional
	// public Collection<Object[]> getTuplesByKindAndUser() {
	// return historyService.listAllHistoryEntriesByKindAndUser();
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> getForResource(String resourceId) {
	// if (resourceId == null) {
	// return new LinkedList<HistoryEntry>();
	// }
	// return historyService.findHistoryEntryByResourceId(UUID.fromString(resourceId));
	// }
	//
	// @Transactional
	// public Collection<HistoryEntry> getForResource(String resourceId, long versionedId) {
	// if (resourceId == null) {
	// return new LinkedList<HistoryEntry>();
	// }
	// return historyService.findHistoryEntry(UUID.fromString(resourceId), versionedId);
	// }
	//
	// @Transactional
	// public Representation getRepresentation(long id) {
	// return historyService.findRepresentationForHistoryEntry(historyService.getEntryWithError(id));
	// }
	//
	// @Transactional
	// public Vocabulary getVocabulary(long id) {
	// return historyService.findVocabularyForHistoryEntry(historyService.getEntryWithError(id));
	// }
}
