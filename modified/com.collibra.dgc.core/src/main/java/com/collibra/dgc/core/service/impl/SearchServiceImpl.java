package com.collibra.dgc.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.dto.search.SearchResultItem;
import com.collibra.dgc.core.index.CommunityIndexer;
import com.collibra.dgc.core.index.TermIndexer;
import com.collibra.dgc.core.index.VocabularyIndexer;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.ObservationManagerEventAdapter;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.EventListenerPriority;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.SearchService;

@Service
@Lazy(false)
public class SearchServiceImpl extends ObservationManagerEventAdapter implements SearchService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

	public static final String FIELD_VALUE = "value";
	public static final String FIELD_UUID = "uuid";
	public static final String FIELD_PARENT = "parent";
	public static final String FIELD_NODE_TYPE = "node_type";
	public static final String FIELD_ATTRIBUTE = "attribute";

	@Autowired
	private CommunityService communityService;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private PlatformTransactionManager txManager;

	private File indexLocation;
	private Directory indexDirectory;
	private Analyzer analyzer;
	public static final Version kLUCENE_VERSION = Version.LUCENE_34;

	private final CommunityIndexer communityIndexer = new CommunityIndexer(this);
	private final VocabularyIndexer vocabularyIndexer = new VocabularyIndexer(this);
	private final TermIndexer termIndexer = new TermIndexer(this);

	private static final String[] SPECIAL_CHARACTERS = new String[] { "\\", "+", "-", "&&", "||", "!", "(", ")", "{",
			"}", "[", "]", "^", "\"", "~", ":" };

	public RepresentationService getRepresentationService() {
		return representationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.SearchService#search(java.lang.String, int)
	 */
	@Override
	public List<SearchResultItem> search(String search, int max) throws Exception {
		return search(search, Arrays.asList(ALL_TYPES), max);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.SearchService#doFullReIndex()
	 */
	@Override
	public synchronized void doFullReIndex() {
		if ("true".equalsIgnoreCase(System.getProperty(PROPERTY_FULL_REINDEX_SKIP))) {
			return;
		}

		final long t1 = System.currentTimeMillis();
		log.info("Full reindex started.");

		// Clear everything
		if (indexLocation.exists()) {
			try {
				FileUtils.deleteDirectory(indexLocation);
			} catch (IOException e) {
				log.error("Error while deleting index directory.", e);
			}
		}
		indexLocation.mkdirs();

		try {
			final IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(kLUCENE_VERSION, analyzer));
			writer.close();
		} catch (Exception e) {
		}

		final CommunityIndexer scIndexer = new CommunityIndexer(this);
		final Collection<Community> communities = communityService.findAllTopLevelCommunities();
		for (final Community sc : communities) {
			scIndexer.onAdd(new CommunityEventData(sc, EventType.ADDED));
		}

		log.info("Full reindex took " + (System.currentTimeMillis() - t1) + "ms.");
	}

	private static final String escapeSpecialCharacters(String value) {
		for (final String ch : SPECIAL_CHARACTERS) {
			value = value.replace(ch, "\\" + ch);
		}
		return value;
	}

	@Override
	public List<SearchResultItem> search(String search, final Collection<EType> types, int max) throws Exception {
		final IndexSearcher searcher = new IndexSearcher(indexDirectory, true);
		try {
			final List<SearchResultItem> result = new ArrayList<SearchResultItem>();
			QueryParser parser = new QueryParser(kLUCENE_VERSION, "value", analyzer);
			parser.setMultiTermRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);

			search = search.trim();

			// Construct the query.
			final StringBuilder sb = new StringBuilder("(value:");
			if (search.length() > 1 && search.charAt(0) == '"' && search.charAt(search.length() - 1) == '"') {
				// We cut of the double quotes first.
				search = search.substring(1, search.length() - 1);
				search = "\"" + escapeSpecialCharacters(search) + "\"";
			} else {
				search = escapeSpecialCharacters(search);
			}
			if (search.endsWith("*")) {
				sb.append(search.substring(0, search.length() - 1));
				sb.append("^2 OR value:").append(search);
			} else {
				sb.append(search);
			}
			sb.append(") AND (");

			for (Iterator<EType> iter = types.iterator(); iter.hasNext();) {
				EType type = iter.next();
				// Cannot use the EntityDiscriminators here because AT is a predefined value in Lucene queries.
				sb.append(FIELD_NODE_TYPE).append(":").append(type == EType.AT ? "ATT" : type.name());
				if (iter.hasNext()) {
					sb.append(" OR ");
				}
			}

			sb.append(")");
			log.debug("Lucene query: " + sb.toString());
			Query query = parser.parse(sb.toString());

			// Execute the query
			final ScoreDoc[] hits = searcher.search(query, null, max).scoreDocs;
			// Iterate through the results:
			// Some caching to speed this up
			final Map<String, SearchResultItem> cache = new HashMap<String, SearchResultItem>();
			final IndexReader indexReader = createIndexReader();
			final Set<String> added = new HashSet<String>();
			try {
				for (int i = 0; i < hits.length; i++) {
					final Document hitDoc = searcher.doc(hits[i].doc);
					final SearchResultItem found = createSearchResultItem(hitDoc, searcher, parser, indexReader, cache,
							Math.max(0, hits[i].score));
					if (found != null && !added.contains(found.getResourceID().toString())) {
						result.add(found);
						added.add(found.getResourceID().toString());
					}
				}
			} finally {
				indexReader.close();
			}

			Collections.sort(result, new Comparator<SearchResultItem>() {
				@Override
				public int compare(SearchResultItem o1, SearchResultItem o2) {
					if (o1.getScore() == o2.getScore()) {
						return o1.getName().length() - o2.getName().length();
					} else if (o1.getScore() < o2.getScore()) {
						return 1;
					}
					return -1;
				}
			});

			return result;
		} catch (CorruptIndexException e) {
			log.error("Corrupt index found while searching for '" + search + "'.", e);
			throw e;
		} catch (ParseException e) {
			log.error("Illegal parse query '" + search + "'.", e);
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error while searching the index for '" + search + "'.", e);
			throw e;
		} finally {
			searcher.close();
		}
	}

	/**
	 * Recursive method to build the search result item and its parents.
	 */
	private SearchResultItem createSearchResultItem(final Document hitDoc, final IndexSearcher searcher,
			final QueryParser parser, final IndexReader indexReader, final Map<String, SearchResultItem> cache,
			final double score) throws ParseException, IOException {
		final String uuid = hitDoc.get(FIELD_UUID);
		// If the score is >= 0, we should reread and not take the cached one.
		if (score < 0 && cache.containsKey(uuid)) {
			return cache.get(uuid);
		}

		final String parentUUID = hitDoc.get(FIELD_PARENT);
		SearchResultItem parent = null;
		if (parentUUID != null) {
			// Construct the query.
			final TermDocs parentDocs = indexReader.termDocs(new Term(FIELD_UUID, parentUUID));
			if (parentDocs != null && parentDocs.next()) {
				final Document parentDoc = searcher.doc(parentDocs.doc());
				parent = createSearchResultItem(parentDoc, searcher, parser, indexReader, cache, -1);
			}
		}
		final String name = hitDoc.get(FIELD_VALUE);
		String type = hitDoc.get(FIELD_NODE_TYPE);
		if (type.equals("ATT")) {
			type = "AT";
		}
		final String attr = hitDoc.get(FIELD_ATTRIBUTE);
		final SearchResultItem result = new SearchResultItem(name, type, uuid, attr, score, parent);

		// Only cache if score < 0
		if (score < 0) {
			cache.put(uuid, result);
		}
		return result;
	}

	public IndexWriter createIndexWriter() throws IOException {
		return new IndexWriter(indexDirectory, new IndexWriterConfig(Version.LUCENE_34, analyzer));
	}

	public IndexReader createIndexReader() throws IOException {
		return IndexReader.open(indexDirectory, true);
	}

	private void listenToEvents() {
		ObservationManager.getInstance().unregister(communityIndexer, GlossaryEventCategory.COMMUNITY);
		ObservationManager.getInstance().unregister(vocabularyIndexer, GlossaryEventCategory.VOCABULARY);
		ObservationManager.getInstance().unregister(termIndexer, GlossaryEventCategory.TERM);

		ObservationManager.getInstance().register(communityIndexer, 0, GlossaryEventCategory.COMMUNITY);
		ObservationManager.getInstance().register(vocabularyIndexer, 0, GlossaryEventCategory.VOCABULARY);
		ObservationManager.getInstance().register(termIndexer, 0, GlossaryEventCategory.TERM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			boolean newIndex = false;
			indexLocation = new File(Application.USER_HOME, "index");
			if (!indexLocation.exists()) {
				newIndex = true;
				indexLocation.mkdirs();
			}
			try {
				indexDirectory = new NIOFSDirectory(indexLocation);
			} catch (IOException e) {
				log.error("Unable to create the index directory in '" + indexLocation.getAbsolutePath() + "'.", e);
				return;
			}

			analyzer = new StandardAnalyzer(kLUCENE_VERSION);

			// In case there is no index directory yet, we do a full re-index.
			if (newIndex && !"true".equalsIgnoreCase(System.getProperty(PROPERTY_FULL_REINDEX_SKIP))) {
				final TransactionTemplate tt = new TransactionTemplate(txManager);
				tt.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						doFullReIndex();
					}
				});
			} else {
				final IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(kLUCENE_VERSION,
						analyzer));
				writer.close();
			}

			ObservationManager.getInstance().register(this, EventListenerPriority.DEFAULT,
					GlossaryEventCategory.OBSERVATION_MANAGER);
			listenToEvents();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Anknown error occurred while creating the index manager.", e);
		}
	}
}
