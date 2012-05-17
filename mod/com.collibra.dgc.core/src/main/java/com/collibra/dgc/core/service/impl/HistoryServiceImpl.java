/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.service.HistoryService;

/**
 * @author fvdmaele
 * 
 */
@Service
public class HistoryServiceImpl implements HistoryService {
	// private static final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);
	//
	// @Autowired
	// public RepresentationService representationService;
	// @Autowired
	// public RulesService rulesService;
	// @Autowired
	// private RightsService rightsService;
	// @Autowired
	// private HistoryEntryDao historyEntryDao;
	// @Autowired
	// private RepresentationDao representationDao;
	// @Autowired
	// private VocabularyDao vocabularyDao;
	// @Autowired
	// private RuleSetDao ruleSetDao;
	//
	// public void delete(HistoryEntry historyEntry) {
	// historyEntryDao.delete(historyEntry);
	// }
	//
	// public HistoryEntry save(HistoryEntry historyEntry) {
	// historyEntryDao.delete(historyEntry);
	// return historyEntry;
	// }
	//
	// public List<HistoryEntry> findHistoryEntryByResourceId(UUID resourceId) {
	//
	// Defense.notNull(resourceId, "resourceId");
	// return historyEntryDao.findHistoryEntryByResourceId(resourceId);
	// }
	//
	// public List<HistoryEntry> findHistoryEntry(UUID resourceId, Long versionedId) {
	//
	// Defense.notNull(resourceId, "resourceId");
	// Defense.notNull(versionedId, "versionedId");
	// return historyEntryDao.findHistoryEntry(resourceId, versionedId);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesByDate(int limit) {
	//
	// Defense.notNull(limit, "limit");
	// return historyEntryDao.listAllHistoryEntriesByDate(limit);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesByDate(int limit, int level) {
	// Defense.notNull(limit, "limit");
	// return historyEntryDao.listAllHistoryEntriesByDate(limit, level);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesForStakeholderByDate(int limit, String fullUserName) {
	// // Filter the username.
	// String username = UserUtility.filterUserName(fullUserName);
	// return listAllHistoryEntriesByDateForUserWithRole(username, Constants.STAKEHOLDER, limit);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesByDateForUserWithRole(String username, String roleName, int limit)
	// {
	// Role role = rightsService.findRole(roleName);
	//
	// if (role == null) {
	// log.warn("Role '" + roleName + "' not found.");
	// return new LinkedList<HistoryEntry>();
	// }
	//
	// return listAllHistoryEntriesByDate(username, role, limit);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesByDate(String username, int limit) {
	// return historyEntryDao.listAllHistoryEntriesByDate(username, limit);
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesByDate(String username, Role role, int limit) {
	// return historyEntryDao.listAllHistoryEntriesByDate(username, role, limit);
	// }
	//
	// public long getIdOfHistoryEntry(HistoryEntry historyEntry) {
	// Defense.notNull(historyEntry, "historyEntry");
	// String resourceType = historyEntry.getResourceType();
	//
	// if (resourceType.equals(EntityDiscriminators.TERM)
	// || resourceType.equals(EntityDiscriminators.BINARY_FACT_TYPE_FORM)
	// || resourceType.equals("characteristicform") || resourceType.equals(EntityDiscriminators.NAME)) {
	// Representation rep = findRepresentationForHistoryEntry(historyEntry);
	// if (rep != null)
	// return rep.getId();
	// } else if (resourceType.equals(EntityDiscriminators.VOCABULARY)) {
	// Vocabulary voc = findVocabularyForHistoryEntry(historyEntry);
	// if (voc != null)
	// return voc.getId();
	// }
	// return -1;
	// }
	//
	// public Representation findRepresentationForHistoryEntry(HistoryEntry historyEntry) {
	// Defense.notNull(historyEntry, "historyEntry");
	// String resourceType = historyEntry.getResourceType();
	// if (EntityDiscriminators.ATTRIBUTE.equals(resourceType)
	// || EntityDiscriminators.BINARY_FACT_TYPE_FORM.equals(resourceType)
	// || EntityDiscriminators.CHARACTERISTIC_FORM.equals(resourceType)
	// || EntityDiscriminators.NAME.equals(resourceType) || EntityDiscriminators.TERM.equals(resourceType)
	// || EntityDiscriminators.FREQUENCY_RULE_STATEMENT.equals(resourceType)
	// || EntityDiscriminators.SEMI_PARSED_RULE_STATEMENT.equals(resourceType)
	// || EntityDiscriminators.SIMPLE_RULE_STATEMENT.equals(resourceType)) {
	// return representationDao.findById(historyEntry.getId());
	// }
	//
	// return null;
	// }
	//
	// public Vocabulary findVocabularyForHistoryEntry(HistoryEntry historyEntry) {
	// Defense.notNull(historyEntry, "historyEntry");
	// if (EntityDiscriminators.VOCABULARY.equals(historyEntry.getResourceType())) {
	// return vocabularyDao.findById(historyEntry.getId());
	// }
	//
	// return null;
	// }
	//
	// public RuleSet findRuleSetForHistoryEntry(HistoryEntry historyEntry) {
	// Defense.notNull(historyEntry, "historyEntry");
	// if (EntityDiscriminators.RULESET.equals(historyEntry.getResourceType())) {
	// return ruleSetDao.findById(historyEntry.getId());
	// }
	//
	// return null;
	// }
	//
	// public List<HistoryEntry> listAllHistoryEntriesBetweenVersions(UUID resourceId, Long startVersionedId,
	// Long endVersionedId) {
	// Defense.notNull(resourceId, "resourceId");
	// Defense.notNull(startVersionedId, "startVersionedId");
	// Defense.notNull(endVersionedId, "endVersionedId");
	// Defense.assertTrue((startVersionedId < endVersionedId), "(startVersionedId < endVersionedId)");
	//
	// return historyEntryDao.listAllHistoryEntriesBetweenVersions(resourceId, startVersionedId, endVersionedId);
	// }
	//
	// public List<Object[]> listAllHistoryEntriesByKindAndUser() {
	// return historyEntryDao.listAllHistoryEntriesByKindAndUser();
	// }
	//
	// public List<HistoryEntry> listHistoryEntriesFromDate(Calendar startDate, int count, int level) {
	// return historyEntryDao.listHistoryEntriesFromDate(startDate, count, level);
	// }
	//
	// public List<HistoryEntry> listHistoryEntriesUntilDate(Calendar endDate, int count, int level) {
	// return historyEntryDao.listHistoryEntriesUntilDate(endDate, count, level);
	// }
	//
	// public List<HistoryEntry> listHistoryEntriesBetweenDates(Calendar startDate, Calendar endDate, int count, int
	// level) {
	// return historyEntryDao.listHistoryEntriesBetweenDates(startDate, endDate, count, level);
	// }
	//
	// public Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate,
	// Calendar endDate, int count, int level) {
	// if (user == null || startDate == null || endDate == null) {
	// return new LinkedList<HistoryEntry>();
	// }
	//
	// user = UserUtility.filterUserName(user);
	// return historyEntryDao.listHistoryEntriesOfInterestForUser(user, startDate, endDate, count, level);
	// }
	//
	// public Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate,
	// Calendar endDate) {
	// return listHistoryEntriesOfInterestForUser(user, startDate, endDate, -1, HistoryEntry.DEFAULT_LEVEL);
	// }
	//
	// public Collection<HistoryEntry> listHistoryEntriesOfInterestForUser(String user, Calendar startDate,
	// Calendar endDate, int level) {
	// return listHistoryEntriesOfInterestForUser(user, startDate, endDate, -1, level);
	// }
	//
	// public Collection<HistoryEntry> listHistoryEntriesOfInterestForUserWithLimit(String user, Calendar startDate,
	// Calendar endDate, int count) {
	// return listHistoryEntriesOfInterestForUser(user, startDate, endDate, count, HistoryEntry.DEFAULT_LEVEL);
	// }
	//
	// public HistoryEntry getEntry(long id) {
	// return historyEntryDao.find(id);
	// }
	//
	// public HistoryEntry getEntryWithError(long id) {
	// HistoryEntry entry = getEntry(id);
	// if (entry == null) {
	// String message = "";
	// log.error(message);
	// throw new EntityNotFoundException(message, id, HistoryEntry.class.getName(),
	// GlossaryErrorCodes.HISTORY_ENTRY_NULL);
	// }
	// return entry;
	// }
}
