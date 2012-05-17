/**
 * 
 */
package com.collibra.dgc.core.index;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.LockObtainFailedException;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.SearchService.EType;
import com.collibra.dgc.core.service.impl.SearchServiceImpl;

/**
 * Helper methods for indexing.
 * @author dieterwachters
 */
public class IndexHelper {
	private static final Log log = LogFactory.getLog(IndexHelper.class);

	protected static boolean isPaused() {
		String paused = System.getProperty(SearchService.PROPERTY_PAUSE_INDEXER);
		if (paused != null && paused.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Add a new term to the index together with its attributes.
	 */
	protected static void addTerm(final Term term, final SearchServiceImpl searchService) {
		if (isPaused()) {
			return;
		}
		addDesignation(term, EType.TE, term.getVocabulary().getId().toString(), searchService);
	}

	/**
	 * Add a new vocabulary to the index. It will also add all children (terms, names) to the index if present.
	 */
	protected static void addVocabulary(final Vocabulary voc, final SearchServiceImpl searchService) {
		if (isPaused()) {
			return;
		}

		try {
			final IndexWriter writer = searchService.createIndexWriter();

			try {
				final String parent = voc.getCommunity().getId().toString();
				addVocabulary(voc, parent, writer, searchService);
				log.debug("Vocabulary added: " + voc.getName() + " (" + voc.getId().toString() + ")");
			} finally {
				writer.close();
			}
		} catch (CorruptIndexException e) {
			log.error("Index corrupt while adding new vocabulary " + voc.getName() + " ("
					+ voc.getId().toString() + ")", e);
		} catch (LockObtainFailedException e) {
			log.error("Failed to obtain lock while adding new vocabulary " + voc.getName() + " ("
					+ voc.getId().toString() + ")", e);
		} catch (IOException e) {
			log.error("IOException while adding new vocabulary " + voc.getName() + " ("
					+ voc.getId().toString() + ")", e);
		}
	}

	/**
	 * Add a new community to the index. It will also add all children (sub-communities, vocabularies, terms, names) to
	 * the index if present.
	 */
	protected static void addCommunity(final Community sc, final SearchServiceImpl searchService) {
		if (isPaused()) {
			return;
		}

		try {
			final IndexWriter writer = searchService.createIndexWriter();
			try {
				final String parent = sc.getParentCommunity() == null ? null : sc.getParentCommunity().getId()
						.toString();
				addCommunity(sc, parent, writer, searchService);
				log.debug("Community added: " + sc.getName() + " (" + sc.getId().toString() + ")");
			} finally {
				writer.close();
			}
		} catch (CorruptIndexException e) {
			log.error("Index corrupt while adding new community " + sc.getName() + " (" + sc.getId().toString()
					+ ")", e);
		} catch (LockObtainFailedException e) {
			log.error("Failed to obtain lock while adding new community " + sc.getName() + " ("
					+ sc.getId().toString() + ")", e);
		} catch (IOException e) {
			log.error("IOException while adding new community " + sc.getName() + " (" + sc.getId().toString()
					+ ")", e);
		}
	}

	/**
	 * Deletes the given resource from the index together with all the ones that have this resource as ancestor
	 * (recursive).
	 */
	protected static void deleteResource(final Resource resource, final SearchServiceImpl searchService) {
		if (isPaused()) {
			return;
		}

		final String uuid = resource.getId().toString();
		try {
			final IndexWriter writer = searchService.createIndexWriter();
			final IndexReader reader = searchService.createIndexReader();
			try {
				deleteDocument(uuid, searchService, writer, reader);
			} finally {
				writer.close();
				reader.close();
			}
		} catch (CorruptIndexException e) {
			log.error("Corrupt index while deleting resource '" + resource.getId().toString() + "'.", e);
		} catch (IOException e) {
			log.error("Error while deleting resource '" + resource.getId().toString() + "'.", e);
		}
	}

	/***********************************************************
	 * Helper methods *
	 ***********************************************************/

	private static void addDesignation(final Designation designation, final EType type, final String parent,
			final SearchServiceImpl searchService) {
		try {
			final IndexWriter writer = searchService.createIndexWriter();

			try {
				addDesignation(designation, type, parent, writer, searchService);
				log.debug("Designation added: " + designation.getSignifier() + " ("
						+ designation.getId().toString() + ")");
			} finally {
				writer.close();
			}
		} catch (CorruptIndexException e) {
			log.error("Index corrupt while adding new designation " + designation.getId().toString(), e);
		} catch (LockObtainFailedException e) {
			log.error("Failed to obtain lock while adding new designation " + designation.getId().toString(), e);
		} catch (IOException e) {
			log.error("IOException while adding new designation " + designation.getId().toString(), e);
		}
	}

	/**
	 * Helper method to delete a specific document and all that have this document as his parent.
	 */
	private static void deleteDocument(final String uuid, final SearchServiceImpl searchService,
			final IndexWriter writer, final IndexReader reader) {
		try {
			// Delete this document
			writer.deleteDocuments(new org.apache.lucene.index.Term(SearchServiceImpl.FIELD_UUID, uuid));

			// Now delete all that have us as parent (recursive).
			final TermDocs parentDocs = reader.termDocs(new org.apache.lucene.index.Term(
					SearchServiceImpl.FIELD_PARENT, uuid));
			while (parentDocs.next()) {
				final Document doc = reader.document(parentDocs.doc());
				if (doc != null) {
					deleteDocument(doc.get(SearchServiceImpl.FIELD_UUID), searchService, writer, reader);
				}
			}
			log.debug("Document with uuid '" + uuid + "' deleted.");
		} catch (IOException e) {
			log.error("IOException while removing document with uuid " + uuid + "'.", e);
		}
	}

	/**
	 * Helper method to store a community.
	 */
	private static final void addDocument(final String uuid, final EType nodeType, final String name,
			final String parent, final IndexWriter writer) throws IOException {
		addDocument(uuid, nodeType, name, parent, null, writer);
	}

	/**
	 * Helper method to store a community.
	 */
	private static final void addDocument(final String uuid, final EType nodeType, final String name,
			final String parent, final String attributeName, final IndexWriter writer) throws IOException {
		if (uuid == null || nodeType == null || name == null) {
			log.warn("UUID, nodeType and name cannot be null when adding index to Lucene.");
			return;
		}
		Document doc = new Document();
		doc.add(new Field(SearchServiceImpl.FIELD_UUID, uuid, Field.Store.YES, Field.Index.NOT_ANALYZED));
		final Field typeField = new Field(SearchServiceImpl.FIELD_NODE_TYPE, nodeType == EType.AT ? "ATT"
				: nodeType.name(), Field.Store.YES, Field.Index.ANALYZED);
		typeField.setBoost(0);
		doc.add(typeField);
		doc.add(new Field(SearchServiceImpl.FIELD_VALUE, name, Field.Store.YES, Field.Index.ANALYZED));
		if (parent != null) {
			doc.add(new Field(SearchServiceImpl.FIELD_PARENT, parent, Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		if (attributeName != null) {
			doc.add(new Field(SearchServiceImpl.FIELD_ATTRIBUTE, attributeName, Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		writer.addDocument(doc);
	}

	/**
	 * Helper method to store a vocabulary.
	 */
	private static final void addVocabulary(final Vocabulary voc, final String parent, final IndexWriter writer,
			final SearchService searchService) throws IOException {
		final String uuid = voc.getId().toString();
		addDocument(uuid, EType.VC, voc.getName(), parent, writer);

		// Add all the already existing terms.
		final Set<Term> terms = voc.getTerms();
		for (final Term term : terms) {
			try {
				addDesignation(term, EType.TE, uuid, writer, searchService);
			} catch (Exception e) {
				log.error("Error while adding term '" + (term == null ? "null" : term.getSignifier()) + "'.", e);
			}
		}
	}

	/**
	 * Helper method to store a community.
	 */
	private static final void addCommunity(final Community community, final String parent, final IndexWriter writer,
			final SearchService searchService) throws IOException {
		final String uuid = community.getId().toString();
		addDocument(uuid, EType.CO, community.getName(), parent, writer);

		// Add all the existing sub communities
		final Set<Community> sub = community.getSubCommunities();
		for (final Community sc : sub) {
			addCommunity(sc, uuid, writer, searchService);
		}

		// Add all the existing vocabularies
		final Set<Vocabulary> vocabularies = community.getVocabularies();
		for (final Vocabulary voc : vocabularies) {
			addVocabulary(voc, uuid, writer, searchService);
		}
	}

	private static void addDesignation(final Designation designation, final EType type, final String parent,
			final IndexWriter writer, final SearchService searchService) throws IOException {
		final String uuid = designation.getId().toString();
		addDocument(uuid, type, designation.getSignifier(), parent, writer);

		// Add all the existing attributes
		final Set<Attribute> attributes = designation.getAttributes();
		for (final Attribute attribute : attributes) {
				// Cannot use the EntityDiscriminators here because AT is a predefined value in Lucene queries.
				addDocument(attribute.getId().toString(), EType.AT, attribute.getValue(), uuid,
						attribute.getLabel().getSignifier(), writer);
		}
	}
}
