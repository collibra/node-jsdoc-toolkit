package com.collibra.dgc.core.util;

import java.util.Collection;

import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author amarnath
 * 
 */
public interface PersistenceStrategy {
	public Collection<Vocabulary> createVocabularies(Collection<Vocabulary> vocabularies);
}
