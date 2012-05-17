package com.collibra.dgc.core.component;


/**
 * History API.
 * @author amarnath
 * 
 */
public interface HistoryComponent {
	//
	// /**
	// * List all {@link HistoryEntry}s between specified {@link Calendar} dates inclusive of the start and end dates.
	// * @param startDate From date.
	// * @param endDate To date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s between the specified dates.
	// */
	// Collection<HistoryEntry> findBetweenDates(Calendar startDate, Calendar endDate, int count, int level);
	//
	// /**
	// * Get {@link HistoryEntry}s ordered by date.
	// * @param limit Number of entries.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findByDate(int limit);
	//
	// /**
	// * Get {@link HistoryEntry}s ordered by date and of level greater than or below that of specified level.
	// * @param limit Number of entries.
	// * @param level The entry level.
	// * @return {@link HistoryEntry}s
	// */
	// Collection<HistoryEntry> findByDate(int limit, int level);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified user.
	// * @param username The user name.
	// * @param limit Number of results.
	// * @return {@link List} of {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findByUser(String username, int limit);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified user and role.
	// * @param username The user name.
	// * @param roleName The role name.
	// * @param limit Number of results.
	// * @return {@link List} of {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findByUserAndRole(String username, String roleName, int limit);
	//
	// /**
	// * List all {@link HistoryEntry}s from specified {@link Calendar} date.
	// * @param startDate The starting {@link Calendar} date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s from the specified date.
	// */
	// Collection<HistoryEntry> findFromDate(Calendar startDate, int count, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param level History log level.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate, int level);
	//
	// /**
	// * List all {@link HistoryEntry}s of interest to specified user and between specified {@link Calendar} dates.
	// * @param user The user name.
	// * @param startDate The start {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param count Limit on results applied if the value is greater than or equal to zero.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> findOfInterestForUser(String user, Calendar startDate, Calendar endDate, int count,
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
	// Collection<HistoryEntry> findOfInterestForUserWithLimit(String user, Calendar startDate, Calendar endDate, int
	// count);
	//
	// /**
	// * List all {@link HistoryEntry}s until specified {@link Calendar} date.
	// * @param endDate The end {@link Calendar} date.
	// * @param count Max number of results.
	// * @param level The history log level.
	// * @return List of {@link HistoryEntry}s until the specified date.
	// */
	// Collection<HistoryEntry> findUntilDate(Calendar endDate, int count, int level);
	//
	// /**
	// * Lists all the history entries by kind and by user
	// *
	// * @return Lists of tuples with the following form <userName, #creations, #delete, #changes>
	// */
	// Collection<Object[]> getTuplesByKindAndUser();
	//
	// /**
	// * Get {@link HistoryEntry}s for specified resource.
	// * @param resourceId Resource id.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> getForResource(String resourceId);
	//
	// /**
	// * Get {@link HistoryEntry}s for specified version of the resource.
	// * @param resourceId Resource id.
	// * @param versionedId Versioned id.
	// * @return {@link HistoryEntry}s.
	// */
	// Collection<HistoryEntry> getForResource(String resourceId, long versionedId);
	//
	// /**
	// * Get {@link Representation} of the history entry.
	// * @param id The {@link HistoryEntry} id.
	// * @return {@link Representation}.
	// */
	// Representation getRepresentation(long id);
	//
	// /**
	// * Get {@link Vocabulary} of the history entry.
	// * @param id The {@link HistoryEntry} id.
	// * @return {@link Vocabulary}.
	// */
	// Vocabulary getVocabulary(long id);
}
