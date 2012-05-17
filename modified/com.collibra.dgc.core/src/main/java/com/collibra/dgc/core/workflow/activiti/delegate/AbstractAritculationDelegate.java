package com.collibra.dgc.core.workflow.activiti.delegate;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * 
 * @author amarnath
 * 
 */
public abstract class AbstractAritculationDelegate extends AbstractDelegate {
	@Autowired
	protected RepresentationService representationService;

	protected void changeState(DelegateExecution execution) {
		Object resourceId = execution.getVariable(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID);
		if (resourceId != null) {
			Representation representation = representationService.findRepresentationByResourceId((String) resourceId);
			if (representation != null) {
				changeState(representation);
			}
		}
	}

	protected void setVariables(DelegateExecution execution, Representation representation, String role,
			String entityLevel, String varNameTaskOwners, String varNameUsersList) {
		Resource resource = AppContext.getWorkflowUtility().getResource(representation, entityLevel);
		Collection<Member> members = AppContext.getRightsService().findMembersWithRole(resource.getId(), role);
		if (members.size() == 0) {
			String message = "No member with role '" + role + "' for '" + representation.verbalise()
					+ "' at entity level '" + entityLevel + "'.";
			String processKey = AppContext.getWorkflowEngine().getProcessInstance(execution.getProcessInstanceId())
					.getBusinessKey();

			throw new WorkflowException(processKey, message, WorkflowErrorCodes.USERS_NOT_FOUND);
		}

		String membersCSV = getMembersAsCsv(members);
		execution.setVariable(varNameTaskOwners, membersCSV);
		execution.setVariable(varNameUsersList, membersCSV);
	}
}
