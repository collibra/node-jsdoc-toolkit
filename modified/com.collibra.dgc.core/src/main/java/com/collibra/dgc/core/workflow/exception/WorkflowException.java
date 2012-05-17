package com.collibra.dgc.core.workflow.exception;

import org.activiti.engine.task.Task;

import com.collibra.dgc.core.exceptions.DGCException;

/**
 * 
 * @author amarnath
 * 
 */
public class WorkflowException extends DGCException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7552544284153222873L;

	private final String message;
	private final String key;
	private Task task;

	public WorkflowException(String message, String errorCode, Object... params) {
		this(null, message, errorCode);
	}

	public WorkflowException(String key, String message, String errorCode, Object... params) {
		super(errorCode, params);
		this.key = key;
		this.message = message;
	}

	protected String getTaskInformation() {
		if (key != null) {
			// TODO: FIXME
			// this.task = WorkflowEngineImpl.getInstance().getTask(key);
			//
			// if (task != null) {
			// StringBuilder sb = new StringBuilder();
			// sb.append("Task ").append(task.getTaskDefinitionKey()).append(" assigned to ")
			// .append(task.getAssignee());
			// return sb.toString();
			// }
			return "";
		}

		return "";
	}

	public String getKey() {
		return key;
	}

	@Override
	public String getMessage() {
		return getTaskInformation() + ":" + message;
	}
}
