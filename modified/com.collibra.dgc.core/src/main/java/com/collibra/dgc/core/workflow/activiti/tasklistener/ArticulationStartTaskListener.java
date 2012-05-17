package com.collibra.dgc.core.workflow.activiti.tasklistener;

import java.util.LinkedList;
import java.util.List;

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
 * Start {@link TaskListener} for 'Articulation' user task.
 * @author amarnath
 * 
 */
public class ArticulationStartTaskListener implements TaskListener {
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

		// Set information for email.
		AppContext.getWorkflowUtility().setInformationForEmail(delegateTask.getExecution(), representation);

		// Set the current user for email.
		AppContext.getWorkflowUtility().setCurrentUserAsSender(delegateTask.getExecution());

		// Set candiate users
		String reassignToUser = (String) delegateTask.getExecution()
				.getVariable(WorkflowConstants.VAR_REASSIGN_TO_USER);
		if (reassignToUser == null) {
			AppContext.getWorkflowUtility().setCandidateUsers(delegateTask, representation,
					WorkflowConstants.VAR_RESPONSIBLE_ROLES);
		} else {
			delegateTask.setAssignee(reassignToUser);

			// Notify the task assignment
			List<String> users = new LinkedList<String>();
			users.add(reassignToUser);
			AppContext.getWorkflowUtility().notifyTaskAssignment(delegateTask, users);
		}
	}
}
