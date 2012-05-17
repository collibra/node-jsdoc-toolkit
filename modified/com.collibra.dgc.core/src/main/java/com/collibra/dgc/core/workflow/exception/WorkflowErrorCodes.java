package com.collibra.dgc.core.workflow.exception;

import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * Error codes for {@link WorkflowException}.
 * @author amarnath
 * 
 */
public interface WorkflowErrorCodes {
	/**
	 * Workflow process instance not found with specified process instance id.
	 */
	final String PROCESS_INSTANCE_NOT_FOUND = "processInstanceNotFound";

	/**
	 * Users associated with {@link Representation} not found .
	 */
	final String USERS_NOT_FOUND = "usersNotFound";

	/**
	 * The decision variable value {@link WorkflowConstants#VAR_DECISION} is not set.
	 */
	final String DECISION_MISSING = "decisionMissing";

	/**
	 * Accountable Users associated with {@link Representation} not found .
	 */
	final String ACCOUNTABLE_USERS_NOT_FOUND = "accountableUsersNotFound";

	/**
	 * Responsible Users associated with {@link Representation} not found .
	 */
	final String RESPONSIBLE_USERS_NOT_FOUND = "responsibleUsersNotFound";

	/**
	 * Consulted Users associated with {@link Representation} not found .
	 */
	final String CONSULTED_USERS_NOT_FOUND = "consultedUsersNotFound";

	/**
	 * Informed Users associated with {@link Representation} not found .
	 */
	final String INFORMED_USERS_NOT_FOUND = "informedUsersNotFound";

	/**
	 * {@link Representation} not found .
	 */
	final String NO_REPRESENTATION_ASSOCIATED_WITH_PROCESS = "noRepresentationAssociated";
}
