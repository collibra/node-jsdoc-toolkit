package com.collibra.dgc.core.service;

import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.dto.stats.RepresentationStatsEntry;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;

/**
 * Service that groups queries that are clearly only intended for statistics and would otherwise pollute our other
 * services.
 * 
 * @author dtrog
 * 
 */
public interface StatisticsService {

	/**
	 * Finds the number of BFTFs that each term has as connections. It builds up a map of results. Terms with 0 are not
	 * in the map.
	 * 
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm();

	/**
	 * @param vocabulary Limiting search to only that {@link Vocabulary}
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Vocabulary vocabulary);

	/**
	 * @param community Limiting search to only that {@link Community}
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Community community);

	/**
	 * Find all {@link Term}s with count of {@link Member}s with specified {@link Role}.
	 * @param roleName The {@link Role} name.
	 * @param asc The sorting order.
	 * @return {@link RepresentationStatsEntry} list.
	 */
	List<RepresentationStatsEntry> findTermsWithMemberCount(String roleName, boolean asc);

	/**
	 * Find {@link Term}s belonging to specified {@link Vocabulary} with count of {@link Member}s with specified
	 * {@link Role}.
	 * @param vocabulary The {@link Vocabulary}.
	 * @param roleName The {@link Role} name
	 * @param asc The sorting order.
	 * @return {@link RepresentationStatsEntry} list.
	 */
	List<RepresentationStatsEntry> findTermsWithMemberCount(Vocabulary vocabulary, String roleName, boolean asc);

	/**
	 * 
	 * @param vocabulary
	 * @param asc
	 * @return
	 */
	List<RepresentationStatsEntry> findTermsWithMemberCount(Vocabulary vocabulary, boolean asc);

	/**
	 * Find all {@link Term}s with count of {@link Member}s.
	 * @param asc The sorting order.
	 * @return {@link RepresentationStatsEntry} list.
	 */
	List<RepresentationStatsEntry> findTermsWithMemberCount(boolean asc);

	List<Object[]> findNumberOfSpecializedConceptsPerTerm(Vocabulary vocabulary);

}
