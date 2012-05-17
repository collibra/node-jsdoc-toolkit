package com.collibra.dgc.core.workflow.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * {@link JavaDelegate} for changing the state of the {@link Representation} in the process to specified target state.
 * @author amarnath
 * 
 */
public class StateChanger extends AbstractDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		if (representation == null) {
			String processKey = AppContext.getWorkflowEngine().getProcessInstance(execution.getProcessInstanceId())
					.getBusinessKey();

			throw new WorkflowException(processKey, "Representation is not associated with the workflow '"
					+ AppContext.getWorkflowEngine().getProcessDefinitionById(execution.getProcessInstanceId())
							.getName(), WorkflowErrorCodes.NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS);
		}

		// Set the target state.
		changeState(representation);
	}
}
