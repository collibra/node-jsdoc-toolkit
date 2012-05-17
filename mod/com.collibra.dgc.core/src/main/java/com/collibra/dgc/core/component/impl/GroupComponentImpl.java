package com.collibra.dgc.core.component.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.GroupComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.impl.GroupImpl;
import com.collibra.dgc.core.service.GroupService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.util.Defense;

@Component
@Transactional
public class GroupComponentImpl implements GroupComponent {

	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;

	@Override
	public List<Group> getGroupsForUser(String userRId) {
		Defense.notEmpty(userRId, DGCErrorCodes.USER_ID_NULL, DGCErrorCodes.USER_ID_EMPTY, "userRId");
		return groupService.getGroupsForUser(userRId);
	}

	@Override
	public void removeGroup(String groupRId) {
		Defense.notEmpty(groupRId, DGCErrorCodes.GROUP_ID_NULL, DGCErrorCodes.GROUP_ID_EMPTY, "groupRId");
		groupService.removeGroup(groupService.getGroup(groupRId));
	}

	@Override
	public Group addUserToGroup(String groupRId, String userRId) {
		Defense.notEmpty(userRId, DGCErrorCodes.USER_ID_NULL, DGCErrorCodes.USER_ID_EMPTY, "userRId");
		Defense.notEmpty(groupRId, DGCErrorCodes.GROUP_ID_NULL, DGCErrorCodes.GROUP_ID_EMPTY, "groupRId");
		Group group = groupService.getGroup(groupRId);
		User user = userService.getUserByIdWithError(userRId);
		return groupService.addUserToGroup(group, user);
	}

	@Override
	public Group addGroup(String groupName) {
		Defense.notEmpty(groupName, DGCErrorCodes.GROUP_NAME_NULL, DGCErrorCodes.GROUP_NAME_EMPTY, "groupName");
		return groupService.addGroup(new GroupImpl(groupName));
	}

	@Override
	public Group getGroup(String groupRId) {
		Defense.notEmpty(groupRId, DGCErrorCodes.GROUP_ID_NULL, DGCErrorCodes.GROUP_ID_EMPTY, "groupRId");
		return groupService.getGroup(groupRId);
	}

	@Override
	public Group removeUserFromGroup(String groupId, String userRId) {
		Defense.notEmpty(userRId, DGCErrorCodes.USER_ID_NULL, DGCErrorCodes.USER_ID_EMPTY, "userRId");
		Defense.notEmpty(groupId, DGCErrorCodes.GROUP_ID_NULL, DGCErrorCodes.GROUP_ID_EMPTY, "groupId");
		Group group = groupService.getGroup(groupId);
		User user = userService.getUserByIdWithError(userRId);
		return groupService.removeUserFromGroup(group, user);
	}
}
