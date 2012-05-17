package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * DAO interface for {@link Community} domain object.
 * @author amarnath
 * 
 */
public interface CommunityDao extends AbstractDao<Community> {
	/**
	 * Find a {@link Community} by name.
	 * @param name The name of the {@link Community}
	 * @return The {@link Community}s with specified name.
	 */
	Community findByName(String name);

	/**
	 * Find {@link Community}s with specified URI.
	 * @param uri The URI of the {@link Community}.
	 * @return The {@link Community} with specified URI.
	 */
	Community findByURI(String uri);

	/**
	 * Search {@link Community}s with names matching the specified expression.
	 * @param expression The expression.
	 * @param offset Starting index in the search.
	 * @param number The number of results to be returned.
	 * @return The {@link List} of {@link Community}s with matching name.
	 */
	List<Community> searchByName(String expression, int offset, int number);

	/**
	 * Find all {@link Community}s.
	 * @return {@link List} of all {@link Community}s.
	 */
	@Override
	List<Community> findAll();

	/**
	 * Find {@link Community} with specified resource id.
	 * @param resourceId The {@link Community} resource id.
	 * @return The {@link Community}
	 */
	Community find(String resourceId);

	/**
	 * 
	 * @param vocabulary
	 * @return
	 */
	List<Community> findAll(Vocabulary vocabulary);

	/**
	 * 
	 * @return
	 */
	List<Community> findTopLevelCommmunities();

	/**
	 * @return {@link Community}s without {@link Vocabulary}s.
	 */
	List<Community> findCommunitiesWithoutVocabularies();

	/**
	 * 
	 * @param excludeSBVR
	 * @param excludeMeta
	 * @return
	 */
	List<Community> findCommunities(boolean excludeSBVR, boolean excludeMeta);

	/**
	 * @return All SBVR communities (sorted by name).
	 */
	List<Community> findSBVRCommunities();

	/**
	 * @return All Meta communities (sorted by name).
	 */
	List<Community> findMetaCommunities();
}
