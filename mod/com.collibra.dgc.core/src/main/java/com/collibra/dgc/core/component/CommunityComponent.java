package com.collibra.dgc.core.component;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;

/**
 * Public API for the management of {@link Community}. A {@link Community} is a group of people having a particular
 * unifying characteristic in common.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of a {@link Community}. The resource id is a uuid. 'RId' at the end of an
 * argument name represents the resource id of the named resource.
 * 
 * @author amar
 * @author pmalarme
 * 
 */
public interface CommunityComponent {

	/**
	 * Create a new {@link Community} and persist it.
	 * 
	 * @param name The name of the {@link Community}
	 * @param uri The URI of the {@link Community}
	 * @return The newly persisted {@link Community}
	 */
	Community addCommunity(String name, String uri);

	/**
	 * Create a new {@link Community} and persit it.
	 * 
	 * @param name The name of the {@link Community}
	 * @param uri The URI of the {@link Community}
	 * @param language The language of the {@link Community}
	 * @return The newly persisted {@link Community}
	 */
	Community addCommunity(String name, String uri, String language);

	/**
	 * Create a new sub {@link Community} and persist it.
	 * 
	 * @param parentRId The resource id of the parent {@link Community}
	 * @param name The name of the {@link Community}
	 * @param uri The URI of the {@link Community}
	 * @return The newly persisted {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community addSubCommunity(String parentRId, String name, String uri);

	/**
	 * Create a new sub {@link Community} and persist it.
	 * 
	 * @param parentRId The resource id of the parent {@link Community}.
	 * @param name The name of the {@link Community}
	 * @param uri The URI of the {@link Community}
	 * @param language The language of the {@link Community}
	 * @return The newly persisted {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community addSubCommunity(String parentRId, String name, String uri, String language);

	/**
	 * Get the {@link Community} with the given resource id.
	 * 
	 * @param The resource id of the {@link Community}
	 * @return The {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community getCommunity(String rId);

	/**
	 * Get the {@link Community} with the given URI.
	 * 
	 * @param uri The URI identifier of the {@link Community}
	 * @return The {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community getCommunityByUri(String uri);

	/**
	 * Get the {@link Community} with the given name.
	 * 
	 * @param name The name of the {@link Community}
	 * @return The {@link Community} that has the given name
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community getCommunityByName(String name);

	/**
	 * Get all the {@link Community} of the Data Governance Center.
	 * 
	 * @return A {@link Collection} of the {@link Community}
	 */
	Collection<Community> getCommunities();

	/**
	 * Get all {@link Community}.
	 * 
	 * @param excludeSBVR If <code>true</code> the SBVR {@link Community} will be excluded
	 * @param excludeMeta If <code>true</code> all 'meta' {@link Community} will be excluded
	 * @return A {@link Collection} of {@link Community}
	 */
	Collection<Community> getCommunities(boolean excludeSBVR, boolean excludeMeta);

	/**
	 * Retrieve the communities which match the specified name.
	 * 
	 * @param searchName The expression to search in semantic community's names
	 * @param offset The position of the first result to return ({@code 0} based), to allow, together with
	 *            {@code number}, pagination
	 * @param number The maximum number of communities to retrieve. If it is set to <code>0</code> then all results will
	 *            be returned
	 * @return A {@link Collection} of {@link Community} whose names match {@code searchName}
	 */
	Collection<Community> findCommunitiesContainingName(String searchName, int offset, int number);

	/**
	 * Set/update the parent of a {@link Community}. In effect makes the given community a sub community of the parent
	 * community.
	 * 
	 * @param parentRId The parent {@link Community} resource id
	 * @param communityRId The child {@link Community} resource id
	 * @return The updated {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community changeParentCommunity(String parentRId, String communityRId);

	/**
	 * Update the name of a {@link Community}.
	 * 
	 * @param rId The {@link Community} resource id
	 * @param newName The new name
	 * @return The updated {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community changeName(String rId, String newName);

	/**
	 * Updates the URI of a {@link Community}.
	 * 
	 * @param rId The resource id of the {@link Community} which URI has to be updated.
	 * @param newUri The new URI
	 * @return The updated {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community changeUri(String rId, String newUri);

	/**
	 * Change the language of {@link Community}.
	 * 
	 * @param rId The {@link Community} resource id
	 * @param newLanguage The new language
	 * @return The updated {@link Community}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community changeLanguage(String rId, String newLanguage);

	/**
	 * Remove specified {@link Community}.
	 * 
	 * @param rId The {@link Community} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	void removeCommunity(String rId);

	/**
	 * Remove the parent {@link Community} for the specified {@link Community} (i.e. set the parent {@link Community} as
	 * a null object).
	 * 
	 * @param rId The resource id of the {@link Community} for which the parent must be removed
	 * @return The {@link Community} for which the parent was removed
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Community removeParentCommunity(String rId);
}
