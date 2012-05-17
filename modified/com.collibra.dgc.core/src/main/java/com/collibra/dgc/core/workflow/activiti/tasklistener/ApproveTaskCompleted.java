package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * {@link TaskListener} handling completion of 'Approve' user task.
 * @author amarnath
 * 
 */
public class ApproveTaskCompleted implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(delegateTask.getExecution());
		// Initialize notification information.
		AppContext.getWorkflowUtility().setUsersToNotify(delegateTask.getExecution(), representation,
				WorkflowConstants.VAR_ACCOUNTABLE_ROLES, WorkflowConstants.VAR_CONSULTED_ROLES,
				WorkflowConstants.VAR_RESPONSIBLE_ROLES, WorkflowConstants.VAR_INFORMED_ROLES);

		// Set information for email.
		AppContext.getWorkflowUtility().setInformationForEmail(delegateTask.getExecution(), representation);
		AppContext.getWorkflowUtility().setCurrentUserAsSender(delegateTask.getExecution());

		Object decision = delegateTask.getExecution().getVariable(WorkflowConstants.VAR_DECISION);
		if (decision == null) {
			throw new WorkflowException("Decision information missing. The task cannot be completed without it",
					WorkflowErrorCodes.DECISION_MISSING);
		}

		boolean approved = (Boolean) decision;
		String decisionValue = WorkflowConstants.DECISION_VALUE_REJECTED;
		if (approved) {
			decisionValue = WorkflowConstants.DECISION_VALUE_APPROVED;
		}

		// Set the decision variable value. If XWiki is not running then it will be untranslated key.
		delegateTask.getExecution().setVariable(WorkflowConstants.VAR_DECISION_VALUE, decisionValue);

		// If no reason specified then set empty variable.
		Object reason = delegateTask.getExecution().getVariable(WorkflowConstants.VAR_DECISION_REASON);
		if (reason == null) {
			delegateTask.getExecution().setVariable(WorkflowConstants.VAR_DECISION_REASON, "");
		} else {
			AppContext.getWorkflowUtility().addComment(representation.getId(), (String) reason);
		}
	}
}
