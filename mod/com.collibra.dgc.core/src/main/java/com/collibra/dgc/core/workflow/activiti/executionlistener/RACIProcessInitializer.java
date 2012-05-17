package com.collibra.dgc.core.workflow.activiti.executionlistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.util.CsvUtility;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.activiti.delegate.AbstractDelegate;

/**
 * RACI (Responsible, Accountable, Consulted, Informed) initializer for workflow processes.
 * @author amarnath
 * 
 */
public class RACIProcessInitializer extends AbstractDelegate implements ExecutionListener {
	private String responsibleRole;

	private String accountableRole;

	private String informedRole;

	private String consultedRole;

	public void setResponsibleRole(FixedValue value) {
		this.responsibleRole = value.getExpressionText();
	}

	public void setAccountableRole(FixedValue value) {
		this.accountableRole = value.getExpressionText();
	}

	public void setInformedRole(FixedValue value) {
		this.informedRole = value.getExpressionText();
	}

	public void setConsultedRole(FixedValue value) {
		this.consultedRole = value.getExpressionText();
	}

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
	 * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void notify(DelegateExecution execution) throws Exception {
		notifyInternal(execution);
	}

	protected void notifyInternal(DelegateExecution execution) throws Exception {
		// Initialize the process variables
		if (responsibleRole != null) {
			execution.setVariable(WorkflowConstants.VAR_RESPONSIBLE_ROLES, CsvUtility.getList(responsibleRole));
		}

		if (accountableRole != null) {
			execution.setVariable(WorkflowConstants.VAR_ACCOUNTABLE_ROLES, CsvUtility.getList(accountableRole));
		}

		if (informedRole != null) {
			execution.setVariable(WorkflowConstants.VAR_INFORMED_ROLES, CsvUtility.getList(informedRole));
		}

		if (consultedRole != null) {
			execution.setVariable(WorkflowConstants.VAR_CONSULTED_ROLES, CsvUtility.getList(consultedRole));
		}

		// Change the state to target state if defined.
		Representation representation = AppContext.getWorkflowUtility().getRepresentation(execution);
		if (representation != null) {
			changeState(representation);
		}
	}
}
