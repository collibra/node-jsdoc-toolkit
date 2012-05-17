package com.collibra.dgc.core.workflow.activiti.executionlistener;

import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.util.CsvUtility;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Articulation process initializer.
 * @author amarnath
 * 
 */
public class ArticulationProcessRACIInitializer extends RACIProcessInitializer {
	private String reassignRole;

	public void setReassignRole(FixedValue value) {
		this.reassignRole = value.getExpressionText();
	}

	@Override
	@Transactional
	protected void notifyInternal(DelegateExecution execution) throws Exception {
		// Perform the base class initialization.
		super.notifyInternal(execution);

		// Initialize the process variables
		if (reassignRole != null) {
			execution.setVariable(WorkflowConstants.VAR_REASSIGN_ROLES, CsvUtility.getList(reassignRole));
		}

		// Get the representation being articulated.
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		if (representation == null) {
			throw new WorkflowException(WorkflowProcesses.ARTICULATION,
					"No representation associated with the workflow process" + WorkflowProcesses.ARTICULATION
							+ " to start.", WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}

		// There should be at least one accountable user
		Set<String> accountable = AppContext.getWorkflowUtility().getUsers(execution, representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES);

		// At least one accountable user must be present.
		if (accountable.size() == 0) {
			throw new WorkflowException(WorkflowProcesses.ARTICULATION,
					"No user with accountable role available for the representation to start "
							+ WorkflowProcesses.ARTICULATION, WorkflowErrorCodes.ACCOUNTABLE_USERS_NOT_FOUND);
		}

		// Set users for notification (Set toEmail list)
		AppContext.getWorkflowUtility().setUsersToNotify(execution, representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES, WorkflowConstants.VAR_CONSULTED_ROLES);
	}
}
