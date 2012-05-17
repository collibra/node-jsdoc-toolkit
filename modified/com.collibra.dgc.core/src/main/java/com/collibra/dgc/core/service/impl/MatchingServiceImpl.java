/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dto.cmatch.CMModel;
import com.collibra.dgc.core.dto.cmatch.CMTermEntity;
import com.collibra.dgc.core.dto.cmatch.CMVocabulariesModel;
import com.collibra.dgc.core.dto.cmatch.CMVocabularyModel;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.MatchingService;
import com.collibra.dgc.core.service.ReportService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.matcher.common.algorithm.matcher.JaroSimilarity;
import com.collibra.matcher.common.algorithm.matcher.LevenshteinSimilarity;
import com.collibra.matcher.common.matcher.MappingElement;
import com.collibra.matcher.common.matcher.Matcher;
import com.collibra.matcher.common.matcher.SearchSpace;
import com.collibra.matcher.common.model.DMEntity;
import com.collibra.matcher.common.model.DMModel;

/**
 * @author fvdmaele
 * 
 */
@Service
public class MatchingServiceImpl implements MatchingService {
	@Autowired
	private RepresentationService representationService;
	
	@Autowired
	private ReportService reportService;

	public HashMap<Term, List<MappingElement<CMTermEntity>>> compareVocabularies(Vocabulary v1, Vocabulary v2,
			int numberOfMatches) {

		// create the models
		CMVocabularyModel m1 = new CMVocabularyModel(v1);
		CMVocabularyModel m2 = new CMVocabularyModel(v2);

		// compute the search pace
		SearchSpace<CMTermEntity> alignment = matchGlossaryModels(m1, m2);

		HashMap<Term, List<MappingElement<CMTermEntity>>> result = new HashMap<Term, List<MappingElement<CMTermEntity>>>();

		List<MappingElement<CMTermEntity>> mappings = alignment.getMappingElements();

		for (MappingElement<CMTermEntity> mapping : mappings) {
			Term t = findTerm(v1, mapping.getEntity1().getResourceId());
			List<MappingElement<CMTermEntity>> termMappings;
			if (t == null)
				continue;

			if (!result.containsKey(t)) {
				termMappings = new LinkedList<MappingElement<CMTermEntity>>();
				termMappings.add(mapping);
				result.put(t, termMappings);
			} else {
				termMappings = result.get(t);
				if (termMappings.size() < numberOfMatches)
					termMappings.add(mapping);
			}
		}

		return result;
	}

	protected Term findTerm(Vocabulary v, String termResourceId) {
		for (Term t : v.getTerms())
			if (t.getId().toString().equals(termResourceId))
				return t;
		return null;
	}

	public SearchSpace<CMTermEntity> findSimilarTerms(String signifier) {

		// create the models
		CMTermEntity te1 = new CMTermEntity(signifier, null);

		// get all non-deleted terms from the glossary
		TermColumnConfig tConfig = new TermColumnConfig();
		Report report = reportService.buildReport(new ReportConfig(tConfig));
		// create the CMTermEntities
		List<DMEntity> entities = new ArrayList<DMEntity>(report.size());
		for (String termResourceId : report.getResourceIds()) {
			String termString = (String) report.getEntryValue(termResourceId, tConfig.getContentField());
			if (termString != null)
				entities.add(new CMTermEntity(termString, termResourceId));
		}
		// create CMModels
		CMModel m1 = new CMModel(te1);
		CMModel m2 = new CMModel(entities);

		// compute the alignment model
		SearchSpace<CMTermEntity> alignment = matchGlossaryModels(m1, m2);
		return alignment;
	}

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Vocabulary v) {

		// create the models
		CMTermEntity te1 = new CMTermEntity(t.getSignifier(), t.getId().toString());
		CMModel m1 = new CMModel(te1);
		CMVocabularyModel m2 = new CMVocabularyModel(v);

		// compute the alignment model
		SearchSpace<CMTermEntity> alignment = matchGlossaryModels(m1, m2);
		return alignment;
	}

	public SearchSpace<CMTermEntity> findSimilarTerms(String termSignifier, Vocabulary v) {

		// create the models
		CMTermEntity te1 = new CMTermEntity(termSignifier, null);
		CMModel m1 = new CMModel(te1);
		CMVocabularyModel m2 = new CMVocabularyModel(v);

		// compute the alignment model
		SearchSpace<CMTermEntity> alignment = matchGlossaryModels(m1, m2);
		return alignment;
	}

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Collection<Vocabulary> vocs) {

		// create the models
		CMTermEntity te1 = new CMTermEntity(t.getSignifier().toLowerCase(), t.getId().toString());
		CMModel m1 = new CMModel(te1);
		CMVocabulariesModel m2 = new CMVocabulariesModel(vocs);

		// compute the alignment model
		SearchSpace<CMTermEntity> alignment = matchGlossaryModels(m1, m2);
		return alignment;
	}

	public SearchSpace<CMTermEntity> findSimilarTerms(Term t, Community comm) {

		List<Vocabulary> vocs = new LinkedList<Vocabulary>();
		gatherVocabularies(vocs, comm);
		return findSimilarTerms(t, vocs);
	}

	private void gatherVocabularies(final List<Vocabulary> vocs, final Community comm) {
		if (comm.getVocabularies() != null) {
			vocs.addAll(comm.getVocabularies());
		}
		if (comm.getSubCommunities() != null) {
			for (Community sc : comm.getSubCommunities()) {
				gatherVocabularies(vocs, sc);
			}
		}
	}

	protected SearchSpace<CMTermEntity> matchGlossaryModels(DMModel m1, DMModel m2) {
		// initialize the matcher
		Matcher<CMTermEntity> matcher = new Matcher<CMTermEntity>();

		// Initialize the Algorithms to be used in the matcher
		LevenshteinSimilarity levenshtein = new LevenshteinSimilarity();
		JaroSimilarity jaro = new JaroSimilarity();

		// register the matching algorithms
		matcher.registerAlgorithm(levenshtein);
		matcher.registerAlgorithm(jaro);

		// load the models in the matcher
		matcher.setupMatcher(m1, m2);

		// compute the matching scores in the searchspace
		SearchSpace<CMTermEntity> resultsp = matcher.computeMappings();

		return resultsp;
	}
}
