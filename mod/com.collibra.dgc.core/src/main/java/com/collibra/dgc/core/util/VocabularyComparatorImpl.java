package com.collibra.dgc.core.util;

import java.util.Set;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * @author wouter
 * 
 */
public class VocabularyComparatorImpl implements VocabularyComparator {

	private Vocabulary from;
	private Vocabulary to;

	public VocabularyComparatorImpl(Vocabulary from, Vocabulary to) {
		this.from = from;
		this.to = to;
	}

	public Set<Term> addedTerms() {
		Set<Term> diffTerms = to.getTerms();
		diffTerms.removeAll(from.getTerms());
		return diffTerms;
	}

	public Set<Term> removedTerms() {
		Set<Term> diffTerms = from.getTerms();
		diffTerms.removeAll(to.getTerms());
		return diffTerms;
	}

	public Set<CharacteristicForm> addedCharacteristicForms() {
		Set<CharacteristicForm> diffCharacteristicForms = to.getCharacteristicForms();
		diffCharacteristicForms.removeAll(from.getCharacteristicForms());
		return diffCharacteristicForms;
	}

	public Set<CharacteristicForm> removedCharacteristicForms() {
		Set<CharacteristicForm> diffCharacteristicForms = from.getCharacteristicForms();
		diffCharacteristicForms.removeAll(to.getCharacteristicForms());
		return diffCharacteristicForms;
	}

	public Set<BinaryFactTypeForm> addedBinaryFactTypeForms() {
		Set<BinaryFactTypeForm> diffBinaryFactTypeForms = to.getBinaryFactTypeForms();
		diffBinaryFactTypeForms.removeAll(from.getBinaryFactTypeForms());
		return diffBinaryFactTypeForms;
	}

	public Set<BinaryFactTypeForm> removedBinaryFactTypeForms() {
		Set<BinaryFactTypeForm> diffBinaryFactTypeForms = from.getBinaryFactTypeForms();
		diffBinaryFactTypeForms.removeAll(to.getBinaryFactTypeForms());
		return diffBinaryFactTypeForms;
	}

}
