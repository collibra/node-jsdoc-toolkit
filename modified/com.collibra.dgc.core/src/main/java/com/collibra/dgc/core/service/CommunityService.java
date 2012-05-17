package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.user.Member;

/**
 * Service provider for managing {@link Community} domain objects.
 * 
 * @author amarnath
 * 
 */
public interface CommunityService {

	/**
	 * Creates {@link Community}.
	 * @param name Name for {@link Community}.
	 * @param uri URI for {@link Community}.
	 * @return Newly created {@link Community}.
	 */
	Community createCommunity(String name, String uri);

	/**
	 * Remove {@link Community}. The operation can be rolled back.
	 * @param community {@link Community} to be removed.
	 * @return True if successful, otherwise false.
	 */
	void remove(Community community);

	/**
	 * Persists the {@link Community} with versioning.
	 * @param community {@link Community} to be persisted.
	 * @return Persisted {@link Community}.
	 */
	Community save(Community community);

	/**
	 * Persists the collection of {@link Community} with versioning.
	 * @param communities {@link Community} collection to be persisted.
	 * @return Persisted collection of {@link Community}.
	 */
	Collection<Community> save(Collection<Community> communities);

	/**
	 * Get {@link Community} with specified URI.
	 * @param uri URI of {@link Community}
	 * @return {@link Community} with the specified URI or null if does not exist.
	 */
	Community findCommunityByUri(String uri);

	/**
	 * Get {@link Community} with specified name.
	 * @param name Name of {@link Community}.
	 * @return The {@link Community} with the specified name.
	 */
	Community findCommunityByName(String name);

	/**
	 * Creates {@link Community}.
	 * @param parent The owning {@link Community}.
	 * @param name Name of {@link Community}
	 * @param uri URI of {@link Community}
	 * @return The newly created {@link Community}.
	 */
	Community createCommunity(Community parent, String name, String uri);

	/**
	 * Get all {@link Community}s without a parent.
	 * @return {@link Collection} of {@link Community}s.
	 */
	Collection<Community> findAllTopLevelCommunities();

	/**
	 * Get all {@link Community}s.
	 * @return {@link Collection} of {@link Community}s.
	 */
	Collection<Community> findCommunities();

	/**
	 * Get all non-SBVR {@link Community}s. The SBVR community is the one with name 'SBVR English Community'
	 * @return {@link Collection} of non-SBVR {@link Community}s.
	 */
	Collection<Community> findNonSBVRCommunities();

	/**
	 * Get all non-meta {@link Community}s. 'Meta' communities are the ones under the 'Metamodel' communtiy.
	 * @return {@link Collection} of non-meta {@link Community}s.
	 */
	Collection<Community> findNonMetaCommunities();

	/**
	 * Find {@link Community} for the specified resource id.
	 * @param resourceId The resource id.
	 * @return The {@link Community}.
	 */
	Community findCommunity(String resourceId);

	/**
	 * To change URI of {@link Community}.
	 * @param community The {@link Community} whose name to be changed.
	 * @param newUri The new name for the {@link Community}
	 * @return The updated {@link Community}
	 */
	Community changeURI(Community community, String newUri);

	/**
	 * Change the language of {@link Community}.
	 * @param community The {@link Community}
	 * @param language The language
	 * @return The updated {@link Community}
	 */
	Community changeLanguage(Community community, String language);

	/**
	 * 
	 * @param expression
	 * @param offset
	 * @param number
	 * @return
	 */
	List<Community> searchCommunity(String expression, int offset, int number);

	/**
	 * Change/Set the parent {@link Community} of specified {@link Community}.
	 * @param parent The parent{@link Community}
	 * @param community The {@link Community} for which the parent must be changed/set
	 */
	Community changeParent(Community parent, Community community);

	/**
	 * Get {@link Member}s of {@link Community}.
	 * @param community The {@link Community}.
	 * @return The {@link Member}s
	 */
	@Deprecated
	Collection<Member> getMembers(Community community);

	/**
	 * Change the name of {@link Community}.
	 * @param community The {@link Community}
	 * @param newName The new name
	 * @return The updated {@link Community}
	 * @throws AuthorizationException with error code {@link DGCErrorCodes#AUTHORIZATION_FAILED}
	 */
	Community changeName(Community community, String newName);

	/**
	 * Get {@link Community}, if not present throw exception.
	 * @param communityResourceId {@link Community} resource id.
	 * @return {@link Community}
	 */
	Community getCommunityWithError(String communityResourceId);
}
