package com.collibra.dgc.core.util;

import java.util.Set;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * @author wouter
 * 
 */
public interface VocabularyComparator {

	Set<Term> addedTerms();

	Set<Term> removedTerms();

	Set<CharacteristicForm> addedCharacteristicForms();

	Set<CharacteristicForm> removedCharacteristicForms();

	Set<BinaryFactTypeForm> addedBinaryFactTypeForms();

	Set<BinaryFactTypeForm> removedBinaryFactTypeForms();

}
