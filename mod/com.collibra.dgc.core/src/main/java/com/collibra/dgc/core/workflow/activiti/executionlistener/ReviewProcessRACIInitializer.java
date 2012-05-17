package com.collibra.dgc.core.workflow.activiti.executionlistener;

import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Review process RACI initializer.
 * @author amarnath
 * 
 */
public class ReviewProcessRACIInitializer extends RACIProcessInitializer {
	@Override
	@Transactional
	protected void notifyInternal(DelegateExecution execution) throws Exception {
		// Perform the base class initialization.
		super.notifyInternal(execution);

		// Get the representation being articulated.
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		if (representation == null) {
			throw new WorkflowException(WorkflowProcesses.REVIEW,
					"No representation associated with the workflow process" + WorkflowProcesses.REVIEW + " to start.",
					WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}

		// At least one consulted user must be present.
		Set<String> consultedUsers = AppContext.getWorkflowUtility().getUsers(execution, representation,
				WorkflowConstants.VAR_CONSULTED_ROLES);
		if (consultedUsers.size() == 0) {
			throw new WorkflowException(
					WorkflowProcesses.REVIEW,
					"No user with consulted role available for the representation to start " + WorkflowProcesses.REVIEW,
					WorkflowErrorCodes.CONSULTED_USERS_NOT_FOUND);
		}

		// At least one responsible user must be present.
		Set<String> responsbileUsers = AppContext.getWorkflowUtility().getUsers(execution, representation,
				WorkflowConstants.VAR_RESPONSIBLE_ROLES);
		if (responsbileUsers.size() == 0) {
			throw new WorkflowException(WorkflowProcesses.REVIEW,
					"No user with responsible role available for the representation to start "
							+ WorkflowProcesses.REVIEW, WorkflowErrorCodes.RESPONSIBLE_USERS_NOT_FOUND);
		}

		// Set all the reviewers.
		execution.setVariable(WorkflowConstants.VAR_ALL_REVIEWERS, consultedUsers);

		// Set information for email.
		AppContext.getWorkflowUtility().setInformationForEmail(execution, representation);
	}
}
