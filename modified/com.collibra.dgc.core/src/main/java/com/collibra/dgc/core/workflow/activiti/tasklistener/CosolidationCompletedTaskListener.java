package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.activiti.delegate.AbstractDelegate;

/**
 * {@link TaskListener} for handling 'Consolidation' task completed event on Review process to change the status and set
 * users to be notified.
 * @author amarnath
 * 
 */
public class CosolidationCompletedTaskListener extends AbstractDelegate implements TaskListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		notifyInternal(execution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		notifyInternal(delegateTask.getExecution());
	}

	private void notifyInternal(DelegateExecution execution) {
		// Set users to be notified.
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		AppContext.getWorkflowUtility().setUsersToNotify(execution, representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES, WorkflowConstants.VAR_CONSULTED_ROLES);

		// Set information for email.
		AppContext.getWorkflowUtility().setInformationForEmail(execution, representation);
		AppContext.getWorkflowUtility().setCurrentUserAsSender(execution);

		// Change the state
		changeState(representation);
	}
}
