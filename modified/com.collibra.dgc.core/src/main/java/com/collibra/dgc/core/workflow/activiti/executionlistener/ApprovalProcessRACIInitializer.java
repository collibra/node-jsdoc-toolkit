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
 * RACI initializer for Approval process.
 * @author amarnath
 * 
 */
public class ApprovalProcessRACIInitializer extends RACIProcessInitializer {
	@Override
	@Transactional
	protected void notifyInternal(DelegateExecution execution) throws Exception {
		// Perform the base class initialization.
		super.notifyInternal(execution);

		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		if (representation == null) {
			throw new WorkflowException(WorkflowProcesses.APPROVAL,
					"Representation is not associated with the workflow '" + WorkflowProcesses.APPROVAL + " to start",
					WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}

		// At least one accountable user must be present to approve.
		Set<String> accountableUsers = AppContext.getWorkflowUtility().getUsers(execution, representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES);
		if (accountableUsers.size() == 0) {
			throw new WorkflowException(WorkflowProcesses.APPROVAL,
					"No user with accountable role available for the representation to start "
							+ WorkflowProcesses.APPROVAL, WorkflowErrorCodes.ACCOUNTABLE_USERS_NOT_FOUND);
		}

		// Set the target state.
		changeState(representation);

		// Set information for email.
		AppContext.getWorkflowUtility().setInformationForEmail(execution, representation);
	}
}
