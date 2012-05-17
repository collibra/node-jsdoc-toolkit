package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dto.stats.RepresentationStatsEntry;
import com.collibra.dgc.core.dto.stats.RepresentationStatsEntry.CountComparator;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.StatisticsService;

@Service
public class StatisticsServiceImpl extends AbstractService implements StatisticsService {

	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	private TermDao termDao;

	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm() {
		return binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm();
	}

	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Vocabulary vocabulary) {
		return binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm(vocabulary);
	}

	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Community community) {
		return binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm(community);
	}

	public List<RepresentationStatsEntry> findTermsWithMemberCount(String roleName, boolean asc) {
		Map<Term, Integer> membersCount = termDao.findMembersCount(roleName);

		return findTermsWithMemberCount(membersCount, asc);
	}

	public List<RepresentationStatsEntry> findTermsWithMemberCount(Vocabulary vocabulary, String roleName, boolean asc) {
		Map<Term, Integer> membersCount = termDao.findMembersCount(vocabulary, roleName);

		return findTermsWithMemberCount(membersCount, asc);
	}

	public List<RepresentationStatsEntry> findTermsWithMemberCount(Vocabulary vocabulary, boolean asc) {
		Map<Term, Integer> membersCount = termDao.findMembersCount(vocabulary);

		return findTermsWithMemberCount(membersCount, asc);
	}

	public List<RepresentationStatsEntry> findTermsWithMemberCount(boolean asc) {
		Map<Term, Integer> membersCount = termDao.findMembersCount();

		return findTermsWithMemberCount(membersCount, asc);
	}

	public List<Object[]> findNumberOfSpecializedConceptsPerTerm(Vocabulary vocabulary) {
		return termDao.findNumberOfSpecializedConceptsPerTerm(vocabulary);
	}

	private List<RepresentationStatsEntry> findTermsWithMemberCount(
			Map<? extends Representation, Integer> membersCount, boolean asc) {
		List<RepresentationStatsEntry> result = new ArrayList<RepresentationStatsEntry>();
		for (Representation representation : membersCount.keySet()) {
			result.add(new RepresentationStatsEntry(representation, membersCount.get(representation)));
		}

		Collections.sort(result, new CountComparator(asc));
		return result;
	}
}
