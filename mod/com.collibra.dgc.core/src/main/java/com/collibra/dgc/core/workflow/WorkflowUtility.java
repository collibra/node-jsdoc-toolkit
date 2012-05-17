package com.collibra.dgc.core.workflow;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.GroupMembership;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.service.GroupService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.service.impl.AbstractService;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Utility methods needed for Workflow related functionality.
 * @author amarnath
 * 
 */
@Service
public final class WorkflowUtility extends AbstractService {
	private static final Logger log = LoggerFactory.getLogger(WorkflowUtility.class);

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private WorkflowEngine workflowEngine;

	@Autowired
	private RightsService rightsService;

	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;

	/**
	 * Get the {@link Resource} of specified entity level (Vocabulary/Community) type from given {@link Representation}.
	 * @param representation The {@link Representation}.
	 * @param entityLevel The entity level.
	 * @return The {@link Resource}.
	 */
	public Resource getResource(Representation representation, String entityLevel) {
		if (WorkflowConstants.ENTITY_LEVEL_COMMUNITY.equals(entityLevel)) {
			return representation.getVocabulary().getCommunity();
		} else if (WorkflowConstants.ENTITY_LEVEL_VOCABULARY.equals(entityLevel)) {
			return representation.getVocabulary();
		} else {
			return representation;
		}
	}

	/**
	 * Get map of role name to entity levels from the configuration string. This string usually configured in the
	 * workflow process defnition.
	 * <p>
	 * Format: role(entityLevel), role, role(entityLevel),...
	 * <p>
	 * Example: Admin, Steward, Steward(Community).
	 * @param roleAndEntitylevels
	 * @return The map of role name to entity levels.
	 */
	public MultiValueMap getRoleToEntityLevelsMap(List<String> roleAndEntitylevels) {
		MultiValueMap roleToEntities = new MultiValueMap();
		for (String roleEntity : roleAndEntitylevels) {
			String role = roleEntity.trim();
			String entityLevel = null;
			int startIndex = roleEntity.indexOf('(');
			if (startIndex > 0) {
				role = roleEntity.substring(0, startIndex).trim();
				entityLevel = roleEntity.substring(startIndex + 1, roleEntity.indexOf(')')).trim();
			}

			if (role != null) {
				roleToEntities.put(role, entityLevel);
			}
		}

		return roleToEntities;
	}

	/**
	 * Get users of specified {@link Resource} based on the role to entity level mappings.
	 * @param resource The {@link Resource}.
	 * @param roleToEntityLevels Map of role name to entity levels.
	 * @return The user names.
	 */
	public Set<String> getUsers(Resource resource, MultiValueMap roleToEntityLevels) {
		Set<String> users = new HashSet<String>();
		for (Object role : roleToEntityLevels.keySet()) {
			Collection<Member> members = rightsService.findMembersWithRole(resource.getId(), (String) role);
			users.addAll(getUserNames(members));
		}

		return users;
	}

	/**
	 * Get users of specified {@link Representation} based on the role to entity level mappings.
	 * @param representation The {@link Representation}/
	 * @param roleToEntityLevels The map of role name to entity levels.
	 * @return The user names.
	 */
	public Set<String> getUsers(Representation representation, MultiValueMap roleToEntityLevels) {
		Set<String> users = new HashSet<String>();
		for (Object role : roleToEntityLevels.keySet()) {
			for (Object entityLevel : roleToEntityLevels.getCollection(role)) {
				Collection<Member> members = null;
				if (entityLevel == null) {
					members = rightsService.findMembersWithRole(representation.getId(), (String) role);
				} else {
					Resource entity = getResource(representation, (String) entityLevel);
					members = rightsService.findMembersWithRole(entity.getId(), (String) role);
				}

				if (members != null) {
					users.addAll(getUserNames(members));
				}
			}
		}
		return users;
	}

	/**
	 * Get users from {@link Member}s list.
	 * @param members The {@link Member}s.
	 * @return The user names.
	 */
	// TODO Port : member no longer returns a username, it has combinations of both userid's and group id's
	// You can use member.isGroup() to check which of both it is.
	public Set<String> getUserNames(Collection<Member> members) {
		Set<String> users = new HashSet<String>();
		for (Member member : members) {
			if (member.isGroup()) {
				Group group = groupService.getGroup(member.getOwnerId());
				for (GroupMembership gm : group.getGroupMemberships()) {
					addUserToUsernames(users, gm.getUser().getId());
				}
			} else {
				addUserToUsernames(users, member.getOwnerId());
			}
		}
		return users;
	}

	private void addUserToUsernames(Set<String> users, String rId) {
		UserData u = userService.getUserById(rId);
		if (u != null) {
			users.add(u.getUserName());
		} else {
			log.info("user with id" + rId + " not found");
		}
	}

	/**
	 * Get users of specified {@link Representation} for given role and glossary entity type.
	 * @param execution The {@link DelegateExecution}.
	 * @param representation The {@link Representation}.
	 * @param roleDetails The role and glossary entity type.
	 * @return
	 */
	public Set<String> getUsers(DelegateExecution execution, Representation representation, String roleDetails) {
		List<String> roleAndEntities = (List<String>) execution.getVariable(roleDetails);
		if (roleAndEntities == null) {
			return new HashSet<String>();
		}

		MultiValueMap roleToEntityLevels = getRoleToEntityLevelsMap(roleAndEntities);
		return getUsers(representation, roleToEntityLevels);
	}

	/**
	 * Set candidate users (users who can perform the task) for specified {@link Task} for {@link Representation} with
	 * specified role and glossary entity details.
	 * @param delegateTask The {@link DelegateTask}.
	 * @param representation The {@link Representation}.
	 * @param roleDetails The role and glossary entity details.
	 */
	public void setCandidateUsers(DelegateTask delegateTask, Representation representation, String roleDetails) {
		// Get all users with given role details and add them as candidate user
		Set<String> users = getUsers(delegateTask.getExecution(), representation, roleDetails);

		if (users == null || users.size() == 0) {
			String error = "No users found with role configuration - '" + roleDetails + "'";
			log.error(error);
			String processKey = workflowEngine.getProcessDefinition(delegateTask.getExecution().getProcessInstanceId())
					.getKey();
			throw new WorkflowException(processKey, error, WorkflowErrorCodes.USERS_NOT_FOUND);
		}

		delegateTask.addCandidateUsers(users);

		// Notify users about task assignment.
		notifyTaskAssignment(delegateTask, new LinkedList<String>(users));
	}

	/**
	 * Set candidate users (users who can perform the task) for specified {@link Task} for {@link Representation} with
	 * specified role and glossary entity details. Note that here {@link Representation} is not passed as parameter, it
	 * will be obtained from the {@link Task} {@link Execution} context.
	 * @param delegateTask The {@link DelegateTask}.
	 * @param roleDetails The role and glossary entity details.
	 */
	public void setCandidateUsers(DelegateTask delegateTask, String roleDetails) {
		Representation representation = getRepresentation(delegateTask.getExecution());
		if (representation == null) {
			String processKey = workflowEngine.getProcessDefinition(delegateTask.getExecution().getProcessInstanceId())
					.getKey();
			throw new WorkflowException(processKey, "No representation associated with the process instance of '"
					+ delegateTask.getProcessDefinitionId() + "'",
					WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}
		setCandidateUsers(delegateTask, representation, roleDetails);
	}

	/**
	 * Get {@link Representation} from the {@link Execution} context of {@link Task}.
	 * @param execution The {@link DelegateExecution}.
	 * @return The {@link Representation}.
	 */
	public Representation getRepresentation(DelegateExecution execution) {
		String resourceId = (String) execution.getVariable(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID);
		if (resourceId != null) {
			return representationService.findRepresentationByResourceId(resourceId);
		}

		return null;
	}

	/**
	 * Set URL and {@link Representation} name for sending mails. Note this information can also be used by any other
	 * user interface element.
	 * @param execution The {@link DelegateExecution}.
	 * @param representation The {@link Representation}.
	 */
	public void setInformationForEmail(DelegateExecution execution, Representation representation) {
		// FIXME PORT Representation URL
		execution.setVariable(WorkflowConstants.VAR_MESSAGE_REPRESENTATION_URL, "FIXME Representation URL");
		execution.setVariable(WorkflowConstants.VAR_MESSAGE_REPRESENTATION_NAME, representation.verbalise());
	}

	/**
	 * Set the user email addresses for notification for the specified role configuration.
	 * @param execution The {@link DelegateExecution}.
	 * @param representation The {@link Representation}.
	 * @param userRoleConfigurations The user role configurations.
	 */
	public void setUsersToNotify(DelegateExecution execution, Representation representation,
			String... userRoleConfigurations) {
		// Set toEmail list
		Set<String> users = new HashSet<String>();

		for (String userRole : userRoleConfigurations) {
			Set<String> usersWithRole = getUsers(execution, representation, userRole);
			users.addAll(usersWithRole);
		}

		StringBuilder sb = new StringBuilder();
		for (String user : users) {
			final UserData userData = userService.getUser(user);
			if (userData != null && userData.getEmailAddress() != null) {
				sb.append(userData.getEmailAddress()).append(",");
			}
		}
		execution.setVariable(WorkflowConstants.VAR_MESSAGE_TO, sb.length() == 0 ? null : sb.toString());
	}

	/**
	 * Set currently logged in user as email sender.
	 * @param execution The activiti {@link DelegateExecution}
	 * @param representation The {@link Representation}
	 */
	public void setCurrentUserAsSender(DelegateExecution execution) {
		execution.setVariable(WorkflowConstants.VAR_MESSAGE_SENDER, getCurrentUser());
	}

	/**
	 * Add comment to the XWiki document.
	 * @param resourceId The resource id.
	 * @param comment The comment string.
	 * @return XWiki comment object id.
	 */
	public int addComment(String resourceId, String comment) {
		// FIXME PORT
		return 0;
	}

	/**
	 * Add comment to the XWiki document as response to specified comment id.
	 * @param resourceId The resource id.
	 * @param parentCommentKey The parent comment key.
	 * @param comment The comment string.
	 * @return XWiki comment object id.
	 */
	public int addThreadedComment(String resourceId, int parentCommentKey, String comment) {
		// FIXME
		return 0;
	}

	/**
	 * Notify users about task assignment.
	 * @param delegateTask The assigned {@link Task}.
	 * @param users The task owners.
	 */
	public void notifyTaskAssignment(DelegateTask delegateTask, List<String> users) {
		// FIXME PORT
	}
}
