package com.collibra.dgc.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.GroupDao;
import com.collibra.dgc.core.dao.GroupMembershipDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.impl.GroupMembershipImpl;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.GroupEventData;
import com.collibra.dgc.core.observer.events.GroupUserEventData;
import com.collibra.dgc.core.service.GroupService;

/**
 * 
 * @author GKDAI63
 * 
 */
@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private GroupMembershipDao groupMembershipDao;

	@Override
	public Group addGroup(Group group) {
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP,
				new GroupEventData(group, EventType.ADDING));
		Group g = groupDao.save(group);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP,
				new GroupEventData(group, EventType.ADDED));
		return g;
	}

	@Override
	public Group addUserToGroup(Group group, User user) {

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP_USERS,
				new GroupUserEventData(group, user, EventType.CHANGING));
		GroupMembershipImpl gm = new GroupMembershipImpl();
		gm.setUser(user);
		gm.setGroup(group);
		groupMembershipDao.save(gm);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP_USERS,
				new GroupUserEventData(group, user, EventType.CHANGED));
		return group;
	}

	@Override
	public Group removeUserFromGroup(Group group, User user) {

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP_USERS,
				new GroupUserEventData(group, user, EventType.CHANGING));
		groupMembershipDao.delete(groupMembershipDao.getByGroupAndUser(group, user));
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP_USERS,
				new GroupUserEventData(group, user, EventType.CHANGED));
		return group;
	}

	@Override
	public void removeGroup(Group group) {
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP,
				new GroupEventData(group, EventType.REMOVING));
		groupDao.delete(group);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.GROUP,
				new GroupEventData(group, EventType.REMOVED));
	}

	@Override
	public List<Group> getGroupsForUser(String userId) {
		return groupDao.getGroupsForUser(userId);
	}

	@Override
	public Group getGroup(String groupId) {
		Group g = groupDao.findById(groupId);
		if (g == null) {
			throw new EntityNotFoundException(DGCErrorCodes.GROUP_NOT_FOUND, groupId);
		}
		return g;
	}
}
