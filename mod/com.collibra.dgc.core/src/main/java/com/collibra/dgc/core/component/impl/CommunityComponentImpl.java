package com.collibra.dgc.core.component.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Community} API implementation.
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Service
public class CommunityComponentImpl implements CommunityComponent {
	private static final Logger log = LoggerFactory.getLogger(CommunityComponentImpl.class);

	@Autowired
	private CommunityService communityService;
	@Autowired
	private CommunityFactory communityFactory;

	@Transactional
	private void checkCommunityName(String name) {

		Defense.notEmpty(name, DGCErrorCodes.COMMUNITY_NAME_NULL, DGCErrorCodes.COMMUNITY_NAME_EMPTY, "name");

		// Check if a semantic community with the same name already exists
		Community existingCommunity = communityService.findCommunityByName(name);

		if (existingCommunity != null) {

			String message = "Semantic community '" + name + "' already exists.";
			log.error(message);

			throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_WITH_NAME_ALREADY_EXISTS, name);
		}
	}

	@Override
	@Transactional
	public Community addCommunity(String name, String uri) {

		Defense.notEmpty(uri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "uri");

		checkCommunityName(name);

		Community community = communityFactory.makeCommunity(name, uri);

		return communityService.save(community);
	}

	@Override
	@Transactional
	public Community addCommunity(String name, String uri, String language) {

		Defense.notEmpty(uri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "uri");
		Defense.notEmpty(language, DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY,
				"language");

		checkCommunityName(name);

		Community community = communityFactory.makeCommunity(name, uri, language);

		return communityService.save(community);
	}

	@Override
	@Transactional
	public Community addSubCommunity(String parentRId, String name, String uri) {

		Defense.notEmpty(uri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "uri");
		Defense.notEmpty(parentRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY, "parentRId");
		checkCommunityName(name);

		final Community parent = communityService.getCommunityWithError(parentRId);

		Community community = communityFactory.makeCommunity(parent, name, uri);

		return communityService.save(community);
	}

	@Override
	@Transactional
	public Community addSubCommunity(String parentRId, String name, String uri, String language) {

		Defense.notEmpty(uri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "uri");
		Defense.notEmpty(parentRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY, "parentRId");
		Defense.notEmpty(language, DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY,
				"language");
		checkCommunityName(name);

		final Community parent = communityService.getCommunityWithError(parentRId);

		Community community = communityFactory.makeCommunity(parent, name, uri, language);

		return communityService.save(community);
	}

	@Override
	@Transactional
	public Community getCommunity(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY, "rId");

		return communityService.getCommunityWithError(rId);
	}

	@Override
	@Transactional
	public Community getCommunityByUri(String uri) {

		Defense.notEmpty(uri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "uri");

		Community community = communityService.findCommunityByUri(uri);

		if (community == null) {
			String message = "Community with URI '" + uri + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.COMMUNITY_NOT_FOUND_URI, uri);
		}

		return community;
	}

	@Override
	@Transactional
	public Community getCommunityByName(String name) {

		Defense.notEmpty(name, DGCErrorCodes.COMMUNITY_NAME_NULL, DGCErrorCodes.COMMUNITY_NAME_EMPTY, "name");

		Community community = communityService.findCommunityByName(name);

		if (community == null) {
			String message = "Community with name '" + name + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.COMMUNITY_NOT_FOUND_NAME, name);
		}

		return community;
	}

	@Override
	@Transactional
	public Collection<Community> getCommunities() {
		return communityService.findCommunities();
	}

	@Override
	@Transactional
	public Collection<Community> getCommunities(boolean excludeSBVR, boolean excludeMeta) {

		if (excludeMeta) {

			return communityService.findNonMetaCommunities();

		} else if (excludeSBVR) {

			return communityService.findNonSBVRCommunities();
		}

		return communityService.findCommunities();
	}

	@Override
	@Transactional
	public Collection<Community> findCommunitiesContainingName(String searchName, int offset, int number) {

		Defense.notEmpty(searchName, DGCErrorCodes.COMMUNITY_NAME_NULL, DGCErrorCodes.COMMUNITY_NAME_EMPTY,
				"searchName");

		return communityService.searchCommunity(searchName, offset, number);
	}

	@Override
	@Transactional
	public Community changeParentCommunity(String parentRId, String communityRId) {

		return communityService.changeParent(getCommunity(parentRId), getCommunity(communityRId));
	}

	@Override
	@Transactional
	public Community changeName(String rId, String newName) {

		checkCommunityName(newName);

		return communityService.changeName(getCommunity(rId), newName);
	}

	@Override
	@Transactional
	public Community changeUri(String rId, String newUri) {

		Defense.notEmpty(newUri, DGCErrorCodes.COMMUNITY_URI_NULL, DGCErrorCodes.COMMUNITY_URI_EMPTY, "newUri");

		return communityService.changeURI(getCommunity(rId), newUri);
	}

	@Override
	@Transactional
	public Community changeLanguage(String rId, String newLanguage) {

		Defense.notEmpty(newLanguage, DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY,
				"newLanguage");

		return communityService.changeLanguage(getCommunity(rId), newLanguage);
	}

	@Override
	@Transactional
	public void removeCommunity(String rId) {

		communityService.remove(getCommunity(rId));
	}

	@Override
	@Transactional
	public Community removeParentCommunity(String rId) {

		return communityService.changeParent(null, getCommunity(rId));
	}
}
