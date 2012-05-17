package com.collibra.dgc.core.workflow.activiti.tasklistener;

import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Start {@link TaskListener} for articulation not done task.
 * @author amarnath
 * 
 */
public class ArticulationNotDoneStartTaskListener implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(delegateTask.getExecution());
		if (representation == null) {
			throw new WorkflowException(WorkflowProcesses.ARTICULATION,
					"No representation associated with the workflow process" + delegateTask.getProcessDefinitionId()
							+ " to start.", WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}

		// Set candidate users
		AppContext.getWorkflowUtility().setCandidateUsers(delegateTask, representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES);

		// Set all the users who can be re-assigned the articulation task as the original owner(s) did not complete it.
		Set<String> reassignableUsers = AppContext.getWorkflowUtility().getUsers(delegateTask.getExecution(),
				representation, WorkflowConstants.VAR_REASSIGN_ROLES);
		delegateTask.getExecution().setVariable(WorkflowConstants.VAR_ALL_USERS_FOR_REASSIGNMENT, reassignableUsers);
	}
}
