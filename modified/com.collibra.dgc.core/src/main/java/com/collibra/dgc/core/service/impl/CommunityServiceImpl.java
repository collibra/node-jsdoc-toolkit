package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;

/**
 * {@link CommunityService} implementation.
 * 
 * @author amarnath
 * 
 */
@Service
public class CommunityServiceImpl extends AbstractService implements CommunityService {
	private static final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);

	@Autowired
	private RepresentationService representationService;
	@Autowired
	private RightsService rightsService;
	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private CommunityServiceHelper communityServiceHelper;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private ConstraintChecker constraintChecker;

	// This is needed to load the domain object listener
	@SuppressWarnings("unused")
	@Autowired
	private DomainObjectListener domainObjectListener;

	public CommunityServiceImpl() {
	}

	@Override
	public Community save(Community community) {

		// Check authorization
		if (community.getParentCommunity() != null) {
			authorizationHelper.checkAuthorization(getCurrentUser(), community.getParentCommunity(),
					Permissions.COMMUNITY_CREATE, DGCErrorCodes.RESOURCE_NO_PERMISSION);
		} else {
			// TODO: Once the administrator right comes in check for the admin
			// permission.
			authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		return communityServiceHelper.save(community);
	}

	@Override
	public Collection<Community> save(Collection<Community> communities) {
		return communityServiceHelper.save(communities);
	}

	@Override
	public Community createCommunity(String name, String uri) {

		Community community = communityFactory.makeCommunity(name, uri);
		return save(community);
	}

	@Override
	public Community createCommunity(Community parent, String name, String uri) {
		Community community = communityFactory.makeCommunity(parent, name, uri);
		return save(community);
	}

	@Override
	public Collection<Community> findAllTopLevelCommunities() {
		return communityDao.findTopLevelCommmunities();
	}

	@Override
	public Collection<Community> findCommunities() {
		return communityDao.findAll();
	}

	@Override
	public Community findCommunityByUri(String uri) {
		return communityDao.findByURI(uri);
	}

	@Override
	public Community findCommunity(String resourceId) {
		return communityDao.find(resourceId);
	}

	@Override
	public Community findCommunityByName(String name) {
		return communityDao.findByName(name);
	}

	@Override
	public Community changeURI(Community community, String newUri) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// If the URIs are same then no changes needed.
		if (community.getUri().equals(newUri)) {
			return community;
		}

		constraintChecker.checkCommunityWithUriAlreadyExists(newUri, community);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGING));

		community.setUri(newUri);
		communityDao.save(community);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGED));

		return community;
	}

	@Override
	public Community changeLanguage(Community community, String language) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGING));

		((CommunityImpl) community).setLanguage(language);
		communityDao.save(community);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGED));

		return community;
	}

	@Override
	public List<Community> searchCommunity(String expression, int offset, int number) {
		return communityDao.searchByName(expression, offset, number);
	}

	@Override
	public Community changeParent(Community parent, Community community) {
		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (parent != null) {

			parent.addSubCommunity(community);
			community.setParentCommunity(parent);

		} else if (community.getParentCommunity() != null) {

			parent = community.getParentCommunity();
			parent.getSubCommunities().remove(community);
			community.setParentCommunity(null);

		} else {

			// If the parent is already null, return the community object cause
			// there is no need to save it
			return community;
		}

		// Save the changes.
		community = save(community);
		return community;
	}

	@Override
	@Deprecated
	public Collection<Member> getMembers(Community community) {
		if (community != null) {
			return rightsService.findMembers(community.getId());
		}

		return new LinkedList<Member>();
	}

	@Override
	public Community changeName(Community community, String newName) {

		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (community.getName().equals(newName))
			return community;

		// Constraint check
		constraintChecker.checkCommunityWithNameAlreadyExists(newName, community);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGING));

		community.setName(newName);
		communityDao.save(community);

		// Notify event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.CHANGED));

		return community;
	}

	@Override
	public Community getCommunityWithError(String communityResourceId) {

		Community community = findCommunity(communityResourceId);

		if (community == null) {
			String message = "Community with id '" + communityResourceId + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.COMMUNITY_NOT_FOUND_ID, communityResourceId);
		}

		return community;
	}

	/***************************
	 * All remove functionality
	 ***************************/

	@Override
	public void remove(Community community) {

		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_REMOVE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Send removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.REMOVING));

		communityDao.save(community);
		getCurrentSession().flush();
		// Delete all sub-communities.
		for (Community seComm : new LinkedList<Community>(community.getSubCommunities())) {
			remove(seComm);
		}
		// Delete all vocabularies
		for (Vocabulary vocabulary : new LinkedList<Vocabulary>(community.getVocabularies())) {
			representationService.remove(vocabulary);
		}

		communityDao.delete(community);

		getCurrentSession().flush();

		// Send deleted event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.REMOVED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.CommunityService#findNonSBVRCommunities()
	 */
	@Override
	public Collection<Community> findNonSBVRCommunities() {
		return communityDao.findCommunities(true, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.CommunityService#findNonMetaCommunities()
	 */
	@Override
	public Collection<Community> findNonMetaCommunities() {
		return communityDao.findCommunities(true, true);
	}
}
