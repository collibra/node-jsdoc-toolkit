package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.activiti.delegate.AbstractDelegate;

/**
 * {@link TaskListener} fired after decommission of terms.
 * @author amarnath
 * 
 */
public class CompleteDecommissionTaskCompletedListener extends AbstractDelegate implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		execute(delegateTask.getExecution());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void execute(DelegateExecution execution) {
		Object decommissionApproved = execution.getVariable(WorkflowConstants.VAR_DECOMMISSION_TERM_APPROVED);
		if (decommissionApproved != null) {
			boolean approved = (Boolean) decommissionApproved;
			String resourceId = (String) execution.getVariable(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID);
			Term term = AppContext.getRepresentationService().findTermByResourceId(resourceId);

			if (approved) {
				changeState(term);
			} else {
				// Restore the original state
				String stateSignifier = (String) execution.getVariable(WorkflowConstants.VAR_PREVIOUS_STATE_PREFIX
						+ resourceId);
				changeState(term, stateSignifier);
			}
			addConsolidationComment(execution, term);
		}
	}

	/**
	 * Consolidate comments for the specified {@link Term} and add to its page.
	 * @param execution The {@link DelegateExecution}.
	 * @param term The {@link Term}.
	 */
	private void addConsolidationComment(DelegateExecution execution, Term term) {
		// TODO PORT
	}
}
