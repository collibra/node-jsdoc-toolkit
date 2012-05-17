package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class ServiceUtility {
	private static final Logger log = LoggerFactory.getLogger(ServiceUtility.class);

	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private RepresentationService representationService;

	/**
	 * 
	 * @return
	 */
	public Vocabulary makeTemporaryVocabulary() {
		String uuid = UUID.randomUUID().toString();
		Community comm = communityFactory.makeCommunity("Name " + uuid, "URI " + uuid);
		return representationFactory.makeVocabulary(comm, "VOC URI " + uuid, "VOC NAME " + uuid);
	}

	/**
	 * Checks if the vocabulary is a SBVR vocabulary or not.
	 * @param representationService
	 * @param vocabulary
	 * @return
	 */
	public final boolean isSbvrVocabulary(Vocabulary vocabulary) {
		String uri = vocabulary.getUri();
		if (uri == null || uri.length() == 0) {
			return false;
		}

		return Constants.METAMODEL_COMMUNITY_URI.equals(vocabulary.getCommunity().getTopLevelCommunity().getUri());
	}

	/**
	 * Utility method that tries to properly initialize the Hibernate CGLIB proxy.
	 * 
	 * @param <T>
	 * @param maybeProxy -- the possible Hibernate generated proxy
	 * @param baseClass -- the resulting class to be cast to.
	 * @return the object of a class <T>
	 * @throws ClassCastException
	 */
	public static <T> T deproxy(Object maybeProxy, Class<T> baseClass) throws ClassCastException {
		if (maybeProxy instanceof HibernateProxy) {
			return baseClass.cast(((HibernateProxy) maybeProxy).getHibernateLazyInitializer().getImplementation());
		}
		return baseClass.cast(maybeProxy);
	}

	/**
	 * 
	 * @param resources
	 * @return
	 */
	public static List<String> toUUIDCollection(Collection<? extends Resource> resources) {
		List<String> result = new LinkedList<String>();
		for (Resource resource : resources) {
			result.add(resource.getId());
		}
		return result;
	}

	/**
	 * Checks if the owner {@link Representation} of the status {@link Term} needs to be locked based on the status
	 * type.
	 * @param status The status {@link Term}.
	 * @return True if the owner to be locked, otherwise false.
	 */
	public boolean shouldLock(Term statusTerm) {
		// TODO PORT
		// DocumentHandler docHandler = ComponentManagerUtil.getDocumentHandler();
		//
		// // If XWiki is running then get the status information for locking.
		// if (docHandler != null && docHandler.isXWikiRunning()) {
		// Term statusTerm = ComponentManagerUtil.getRepresentationService().findLatestTermByResourceId(
		// UUID.fromString(status.getLongExpression()));
		// if (statusTerm == null) {
		// return false;
		// }
		//
		// return checkShouldLockWikiConfig(statusTerm);
		// } else {
		// Default is 'Accepted' status to lock representation.

		if (MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID.toString().equals(
				statusTerm.getObjectType().getId().toString())) {
			return true;
		}
		// }
		return false;
	}

	//
	// /**
	// *
	// * @param statusTerm
	// * @return
	// */
	// public static boolean shouldLock(Term statusTerm) {
	// if (statusTerm == null) {
	// return false;
	// }
	//
	// DocumentHandler docHandler = ComponentManagerUtil.getDocumentHandler();
	// // If XWiki is running then get the status information for locking.
	// if (docHandler != null && docHandler.isXWikiRunning()) {
	// return checkShouldLockWikiConfig(statusTerm);
	// } else {
	// if (MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID.toString().equals(
	// statusTerm.getObjectType().getResourceId().toString())) {
	// return true;
	// }
	// }
	//
	// return false;
	// }
	//
	// private static boolean checkShouldLockWikiConfig(Term statusTerm) {
	// // Get status values from customization page.
	// DocumentFinder docFinder = ComponentManagerUtil.getDocumentFinder();
	// GlossaryDocument document = docFinder.getDocument(Constants.LOCK_REPRESENATION_CONFIGURATION_DOCUMENT);
	//
	// // No customizations have been defined.
	// if (document == null) {
	// return false;
	// }
	//
	// for (GlossaryObject object : document.getObjects(Constants.LOCK_REPRESENTATION_CLASS)) {
	// String stateValue = object.getStringValue(Constants.LOCK_REPRESENTATION_STATUS_VALUE);
	// if (stateValue != null && stateValue.equals(statusTerm.getSignifier())) {
	// return true;
	// }
	// }
	//
	// return false;
	// }
	//

	/**
	 * Check if both collections have the same content.
	 */
	public static boolean haveSameContent(Collection<String> values1, Collection<String> values2) {

		if (values1 == null || values1.isEmpty()) {

			if (values2 != null && !values2.isEmpty())
				return false;
			else
				return true;

		} else {

			if (values2 == null)
				return false;

			if (values1.size() != values2.size())
				return false;

			for (String value : values1) {

				if (!values2.contains(value))
					return false;
			}

			return true;
		}
	}

	/**
	 * Check that the sub-concept has the super concept in its generalization path. It starts with its general concept.
	 */
	public static boolean hasSuperConcept(final Concept subConcept, final Concept superConcept) {

		final Concept generalConcept = subConcept.getGeneralConcept();

		if (generalConcept == null)
			return false;

		if (generalConcept.equals(superConcept))
			return true;

		return hasSuperConcept(generalConcept, superConcept);
	}

	//
	// public static void printVocabulary(Vocabulary vocabulary2) {
	// System.out.println(vocabulary2.toString());
	//
	// for (Term term : vocabulary2.getTerms()) {
	// Term superterm = null;
	// Term typeterm = null;
	// if (term.getObjectType().getGeneralConcept() != null) {
	// superterm = (Term) term.getObjectType().getGeneralConcept().getRepresentations().iterator().next();
	// }
	// if (term.getObjectType().getType() != null) {
	// typeterm = (Term) term.getObjectType().getType().getRepresentations().iterator().next();
	// }
	// System.out.print(term.getSignifier());
	// while (superterm != null) {
	// System.out.print(" > " + superterm.getSignifier());
	// System.out.print(" (" + (null == typeterm ? "" : typeterm.getSignifier()) + ")");
	// ObjectType objectType = superterm.getObjectType();
	// if (objectType.getGeneralConcept() != null && objectType.getGeneralConcept() != objectType) {
	// superterm = (Term) superterm.getObjectType().getGeneralConcept().getRepresentations().iterator()
	// .next();
	// } else {
	// superterm = null;
	// }
	// }
	// System.out.println();
	// }
	//
	// for (BinaryFactTypeForm bftf : vocabulary2.getBinaryFactTypeForms()) {
	// System.out.println(bftf.verbalise());
	// }
	// }
}
