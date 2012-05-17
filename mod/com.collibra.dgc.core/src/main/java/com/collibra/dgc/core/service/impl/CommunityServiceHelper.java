package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.DefaultPersistenceStrategy;

/**
 * Helper class for {@link CommunityServiceImpl}
 * @author amarnath
 * 
 */
@Service
class CommunityServiceHelper extends AbstractService {

	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private RepresentationService representationService;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ConstraintChecker constraintChecker;

	/**
	 * 
	 * @param community
	 * @return
	 */
	Collection<Community> save(Collection<Community> communities) {
		List<Community> result = new LinkedList<Community>();

		for (Community sc : communities)
			result.add(save(sc, false));

		// flush only after committing all communities
		sessionFactory.getCurrentSession().flush();

		return result;
	}

	/**
	 * 
	 * @param community
	 * @return
	 */
	Community save(Community community) {
		return save(community, true);
	}

	/**
	 * 
	 * @param community
	 * @return
	 */
	Community save(Community community, boolean flush) {

		constraintChecker.checkConstraints(community);

		// Notify adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.ADDING));

		communityDao.save(community);

		for (Community subComm : new LinkedList<Community>(community.getSubCommunities())) {
			save(subComm, false);
		}

		// Persist the vocabularies.
		DefaultPersistenceStrategy strategy = new DefaultPersistenceStrategy(representationService);
		strategy.createVocabularies(new LinkedList<Vocabulary>(community.getVocabularies()));

		// Notify added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.COMMUNITY,
				new CommunityEventData(community, EventType.ADDED));

		return community;
	}

}
