package com.collibra.dgc.core.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * 
 * @author amarnath
 * 
 */
public class DefaultPersistenceStrategy implements PersistenceStrategy {
	private final RepresentationService representationService;

	// TODO is this still needed?

	/**
	 * 
	 * @param representationService
	 */
	public DefaultPersistenceStrategy(final RepresentationService representationService) {
		this.representationService = representationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.util.PersistanceStrategy#createVocabularies(java.util.Collection)
	 */
	public Collection<Vocabulary> createVocabularies(Collection<Vocabulary> vocabularies) {
		List<Vocabulary> persistedVocabularies = new LinkedList<Vocabulary>();
		while (vocabularies.size() > 1) {
			Vocabulary voc = getNondependentVocabulary(vocabularies);
			if (voc == null) {
				throw new RuntimeException("Failed to find independent vocabulary.");
			}

			persistedVocabularies.add(createVocabulary(voc));
			vocabularies.remove(voc);
		}

		if (vocabularies.size() > 0) {
			persistedVocabularies.add(createVocabulary(vocabularies.iterator().next()));
		}

		return persistedVocabularies;
	}

	private Vocabulary createVocabulary(Vocabulary vocabulary) {
		return representationService.saveVocabulary(vocabulary);
	}

	private final Vocabulary getNondependentVocabulary(final Collection<Vocabulary> vocabularies) {
		for (Vocabulary voc : vocabularies) {
			if (isIndependent(voc, vocabularies)) {
				return voc;
			}
		}
		return null;
	}

	private final boolean isIndependent(final Vocabulary voc, final Collection<Vocabulary> vocabularies) {
		for (Vocabulary innerVoc : vocabularies) {
			if (!voc.equals(innerVoc)) {
				// If the vocabulary incorporates any of the other vocabulary then it is not independent.
				if (voc.getAllIncorporatedVocabularies().contains(innerVoc)) {
					return false;
				}

				for (BinaryFactTypeForm bftf : voc.getBinaryFactTypeForms()) {
					if (isUsingTerms(bftf, innerVoc)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private final boolean isUsingTerms(final BinaryFactTypeForm bftf, final Vocabulary vocabulary) {
		if (vocabulary.getTerms().contains(bftf.getHeadTerm()) || vocabulary.getTerms().contains(bftf.getTailTerm())) {
			return true;
		}

		return false;
	}

}
