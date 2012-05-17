package com.collibra.dgc.core.workflow.activiti.delegate;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.util.CsvUtility;
import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * 
 * @author amarnath
 * 
 */
public class PostStakeholderReview extends AbstractAritculationDelegate {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		// Get the current task
		Task task = AppContext.getWorkflowEngine().getInternalEngine().getTaskService().createTaskQuery()
				.executionId(execution.getId()).singleResult();
		if (task == null) {
			execution.setVariable(WorkflowConstants.VAR_ENOUGH_REVIEWERS_REVIEWED, Boolean.FALSE);
			return;
		}

		// Get the execution of the parent process that triggered this process execution. Because the articulation
		// review by community members process should be triggered by articulation process when user completes
		// articulation.
		Object executionIdString = execution.getVariable(WorkflowConstants.VAR_PARENT_PROCESS_EXECUTION_ID);
		if (executionIdString == null) {
			execution.setVariable(WorkflowConstants.VAR_ENOUGH_REVIEWERS_REVIEWED, Boolean.FALSE);
			return;
		}

		String executionId = (String) executionIdString;
		ProcessEngine engine = AppContext.getWorkflowEngine().getInternalEngine();

		Execution parentExecution = engine.getRuntimeService().createExecutionQuery().executionId(executionId)
				.singleResult();
		if (parentExecution == null || parentExecution.isEnded()) {
			execution.setVariable(WorkflowConstants.VAR_ENOUGH_REVIEWERS_REVIEWED, Boolean.FALSE);
			return;
		}

		// Check if all the community members from the parent process have reviewed the articulation, if yes proceed
		// otherwise stop.
		List<String> stakeholders = CsvUtility.getList((String) engine.getRuntimeService().getVariable(executionId,
				WorkflowConstants.VAR_LIST_COMMUNITY_USERS));

		List<String> reviewedStakeholders = new LinkedList<String>();
		Object reviewedStakeholdersString = engine.getRuntimeService().getVariable(executionId,
				WorkflowConstants.VAR_REVIEWED_STAKEHOLDERS);
		if (reviewedStakeholdersString != null) {
			CsvUtility.getList((String) reviewedStakeholdersString);
		}

		reviewedStakeholders.add(task.getAssignee());

		if (reviewedStakeholders.size() == stakeholders.size()) {
			execution.setVariable(WorkflowConstants.VAR_ENOUGH_REVIEWERS_REVIEWED, Boolean.TRUE);
		} else {
			execution.setVariable(WorkflowConstants.VAR_ENOUGH_REVIEWERS_REVIEWED, Boolean.FALSE);
		}

		// Change the state of the representation.
		changeState(execution);
	}
}
