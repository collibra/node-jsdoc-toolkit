/**
 * 
 */
package com.collibra.dgc.core.service;


/**
 * Supports the methods for the history daos with extra business logic for validation. It allows clients to query the
 * history and log entries that are created for every change in the repository.
 * 
 * @author fvdmaele
 * 
 */
public interface HistoryService {
	//
	// /**
	// *
	// * @param id
	// * @return
	// */
	// HistoryEntry getEntry(long id);
	//
	// /**
	// * Persists this business object with a new version number.
	// *
	// * @param object The business object to persist.
	// */
	// HistoryEntry save(HistoryEntry historyEntry);
	//
	// /**
	// * Deletes the business object.
	// *
	// * @param object The business object to delete.
	// */
	// void delete(HistoryEntry historyEntry);
	//
	// List<HistoryEntry> findHistoryEntryByResourceId(UUID resourceId);
	//
	// List<HistoryEntry> findHistoryEntry(UUID resourceId, Long versionedId);
	//
	// List<HistoryEntry> listAllHistoryEntriesByDate(int limit);
	//
	// /**
	// * Get {@link HistoryEntry}s by date with log level less than or equal to specified level and restricted to
	// * specified number of entries.
	// * @param limit The maximum number of entries.
	// * @param level The log level.
	// * @return The {@link HistoryEntry}s.
	// */
	// List<HistoryEntry> listAllHistoryEntriesByDate(int limit, int level);
	//
	// /**
	// * This will return the id associated with the resource from this history entry
	// *
	// * @param historyEntry
	// * @return the id of the versionedResource associated with the given history entry.
	// */
	// long getIdOfHistoryEntry(HistoryEntry historyEntry);
	//
	// Vocabulary findVocabularyForHistoryEntry(HistoryEntry historyEntry);
	//
	// Representation findRepresentationForHistoryEntry(HistoryEntry historyEntry);
	//
	// /**
	// * @param limit
	// * @param fullUserName
	// * @return a list of history entries for which the given user is a stakeholder for the representation of the
	// history
	// * entry.
	// */
	// List<HistoryEntry> listAllHistoryEntriesForStakeholderByDate(int limit, String fullUserName);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified user and role.
	// * @param username The user name.
	// * @param roleName The role name.
	// * @param limit Number of results.
	// * @return {@link List} of {@link HistoryEntry}s.
	// */
	// List<HistoryEntry> listAllHistoryEntriesByDateForUserWithRole(String username, String roleName, int limit);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified user and {@link Role}.
	// * @param username The user name.
	// * @param role The {@link Role}.
	// * @param limit Number of results.
	// * @return {@link List} of {@link HistoryEntry}s.
	// */
	// List<HistoryEntry> listAllHistoryEntriesByDate(String username, Role role, int limit);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified user.
	// * @param username The user name.
	// * @param limit Number of results.
	// * @return {@link List} of {@link HistoryEntry}s.
	// */
	// List<HistoryEntry> listAllHistoryEntriesByDate(String username, int limit);
	//
	// /**
	// * Lists all the history entries by kind and by user
	// *
	// * @return Lists of tuples with the following form <userName, #creations, #delete, #changes>
	// */
	// List<Object[]> listAllHistoryEntriesByKindAndUser();
	//
	// /**
	// * List all {@link HistoryEntry}s from specified {@link Calendar} date.
	// * @param startDate The starting {@link Calendar} date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s from the specified date.
	// */
	// List<HistoryEntry> listHistoryEntriesFromDate(Calendar startDate, int count, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s until specified {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s until the specified date.
	// */
	// List<HistoryEntry> listHistoryEntriesUntilDate(Calendar endDate, int count, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s between specified {@link Calendar} dates inclusive of the start and end dates.
	// * @param startDate From date.
	// * @param endDate To date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s between the specified dates.
	// */
	// List<HistoryEntry> listHistoryEntriesBetweenDates(Calendar startDate, Calendar endDate, int count, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param count Limit on results applied if the value is greater than or equal to zero.
	// * @param level The history log level.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate, Calendar endDate,
	// int count, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate, Calendar endDate);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param level History log level.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate, Calendar endDate,
	// int level);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param count Limit on results applied if the value is greater than or equal to zero.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> listHistoryEntriesOfInterestForUserWithLimit(String user, Calendar startDate,
	// Calendar endDate, int count);
	//
	// /**
	// *
	// * @param id
	// * @return
	// */
	// HistoryEntry getEntryWithError(long id);
}
