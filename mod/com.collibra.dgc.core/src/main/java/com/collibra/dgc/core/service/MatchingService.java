/**
 * 
 */
package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.collibra.dgc.core.dto.cmatch.CMTermEntity;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.matcher.common.matcher.MappingElement;
import com.collibra.matcher.common.matcher.SearchSpace;

/**
 * Supports various matching and comparison api's for glossary entries (vocabularies, communities, terms, ...).
 * @author fvdmaele
 * 
 */
public interface MatchingService {

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Vocabulary v);

	public SearchSpace<CMTermEntity> findSimilarTerms(String termSignifier, Vocabulary v);

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Collection<Vocabulary> vocs);

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Community spc);

	public SearchSpace<CMTermEntity> findSimilarTerms(String signifier);

	public HashMap<Term, List<MappingElement<CMTermEntity>>> compareVocabularies(Vocabulary v1, Vocabulary v2,
			int numberOfMatches);
}
