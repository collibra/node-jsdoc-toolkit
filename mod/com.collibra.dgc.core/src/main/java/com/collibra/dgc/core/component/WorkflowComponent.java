package com.collibra.dgc.core.component;

import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * 
 * @author amarnath
 * 
 */
public interface WorkflowComponent {
	/**
	 * Complete the task.
	 * @param taskId Task id.
	 * @return True if successful otherwise false.
	 */
	boolean completeTask(String taskId);

	/**
	 * Creates task for the specified user.
	 * @param assignee The task owner.
	 * @param title Task title.
	 * @param status Task status.
	 * @param resolution Task resolution.
	 * @param date Task due date.
	 * @param severity Task severity.
	 * @param description Task description.
	 * @return The {@link Task} id.
	 * @throws AuthorizationException for {@link Permissions#WF_GENERIC_TASKS_START}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#REPRESENTATION_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_NULL}, {@link DGCErrorCodes#USER_NAME_EMPTY}
	 */
	String createTask(String assignee, String title, String status, String resolution, String date, String severity,
			String description);

	/**
	 * Creates task for the specified user.
	 * @param resourceId Resource id of the {@link Representation}.
	 * @param assignee The task owner.
	 * @param title Task title.
	 * @param status Task status.
	 * @param resolution Task resolution.
	 * @param date Task due date.
	 * @param severity Task severity.
	 * @param description Task description.
	 * @return The {@link Task} id.
	 * @throws AuthorizationException for {@link Permissions#WF_GENERIC_TASKS_START}
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#REPRESENTATION_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_NULL}, {@link DGCErrorCodes#USER_NAME_EMPTY}
	 */
	String createTask(String resourceId, String assignee, String title, String status, String resolution, String date,
			String severity, String description);

	/**
	 * Deploy the business process in the zipped archive.
	 * @param zipis The {@link ZipInputStream} of the archive.
	 * @return {@link Deployment} id.
	 * @throws AuthorizationException for {@link Permissions#WF_DEPLOY}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#FILE_INPUT_STREAM_NULL}
	 */
	String deploy(ZipInputStream zipis);

	/**
	 * Undeoply the deployment with specified id.
	 * @param deploymentId The deployment id.
	 * @return True if successful otherwise false.
	 * @see WorkflowService#undeploy(String) for error codes and exceptions.
	 */
	boolean undeploy(String deploymentId);

	Collection<HistoricProcessInstance> findCompletedProcesses(int start, int count);

	Collection<HistoricTaskInstance> findCompletedTasks(int start, int count);

	Collection<HistoricTaskInstance> findDeletedTasks(int start, int count);

	Collection<HistoricProcessInstance> findHistoricUnfinishedProcesses(int start, int count);

	Collection<HistoricTaskInstance> findHistoricUnfinishedTasks(int start, int count);

	/**
	 * Find all {@link Task}s with specified process variables.
	 * @param variables The process variables.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasks(Map<String, Object> variables);

	/**
	 * Find the {@link Task}s for the currently logged in user.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasksForCurrentUser();

	/**
	 * Find {@link Task}s for currently logged in user with specified process variables.
	 * @param variables The process variables.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasksForCurrentUser(Map<String, Object> variables);

	/**
	 * Find {@link Task}s for currently logged in user for specified resource.
	 * @param resourceId The resource id.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasksForCurrentUser(String resourceId);

	/**
	 * Find {@link Task}s for the specified user.
	 * @param username The user name.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasksForUser(String username);

	/**
	 * Find {@link Task}s for the specified user name and process variables.
	 * @param username The user name.
	 * @param variables The process variables.
	 * @return The {@link Task}s
	 */
	Collection<Task> findTasksForUser(String username, Map<String, Object> variables);

	/**
	 * Find {@link Task}s for the specified user name and the resource.
	 * @param username The user name.
	 * @param resourceId The resource id.
	 * @return The {@link Task}s.
	 */
	Collection<Task> findTasksForUser(String username, String resourceId);

	/**
	 * Get all {@link ProcessInstance}s running in the system.
	 * @return The {@link ProcessInstance}s.
	 */
	Collection<ProcessInstance> getAllProcessInstances();

	/**
	 * Get all {@link Task}s in the system.
	 * @return The {@link Task}s.
	 */
	Collection<Task> getAllTasks();

	/**
	 * @see WorkflowService#getDeployedProcesses()
	 */
	Collection<ProcessDefinition> getDeployedProcesses();

	/**
	 * Get all the deployment ids.
	 * @return The deployment ids.
	 */
	Collection<String> getDeploymentIds();

	/**
	 * Get form key for specified {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The form key.
	 */
	String getFormKey(String taskId);

	/**
	 * Get {@link ProcessDefinition}.
	 * @param businessProcessKey The business process key.
	 * @return The {@link ProcessDefinition}.
	 */
	ProcessDefinition getProcessDefinition(String businessProcessKey);

	/**
	 * Get {@link ProcessDefinition} by its id.
	 * @param processDefId {@link ProcessDefinition} id.
	 * @return The {@link ProcessDefinition}.
	 */
	ProcessDefinition getProcessDefinitionById(String processDefId);

	/**
	 * Get process key for the {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The process key.
	 */
	String getProcessKey(String taskId);

	/**
	 * Get glossary right for starting process with specified business key.
	 * @param processKey The workflow process business key.
	 * @return The right.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String getRightForStartingProcess(String processKey);

	/**
	 * Get glossary right for stopping process instance with specified business key.
	 * @param processKey The workflow process business key.
	 * @return The right.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String getRightForStoppingProcess(String processKey);

	/**
	 * Get glossary right for stopping process instance with specified process instance.
	 * @param processInstanceId The workflow process instance id.
	 * @return The right.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 */
	String getRightForStoppingProcessInstance(String processInstanceId);

	/**
	 * Get workflow start form key.
	 * @param businessProcessKey The business process key.
	 * @return The start form key.
	 */
	String getStartFormKey(String businessProcessKey);

	/**
	 * Get {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The {@link Task}.
	 */
	Task getTask(String taskId);

	/**
	 * Get users who are owners of the {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return Users.
	 */
	Collection<String> getTaskOwners(String taskId);

	/**
	 * Get process variable with specified name.
	 * @param taskId The task id.
	 * @param varName The variable name.
	 * @return The process variable value.
	 */
	Object getVariable(String taskId, String varName);

	/**
	 * Remove {@link Task}. Note that this will stop the {@link ProcessInstance} of the {@link Task} there by removing
	 * all {@link Task}s associated with that {@link ProcessInstance}.
	 * @param taskId The {@link Task} id.
	 * @param reason The reason for removing {@link Task}.
	 */
	boolean removeTask(String taskId, String reason);

	/**
	 * Set process variable for the specified task.
	 * @param taskId The task id.
	 * @param varName The variable name.
	 * @param value The variable value.
	 */
	void setVariable(String taskId, String varName, Object value);

	/**
	 * Start the business process specified by key with given process variables.
	 * @param key The business process key.
	 * @param variables The process variables.
	 * @return Process id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcess(String key, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Representation}.
	 * @param key The workflow process key.
	 * @param resourceId Resource id of the {@link Representation}.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessForRepresentation(String key, String resourceId);

	/**
	 * Start workflow process for specified {@link Representation}.
	 * @param key The workflow process key.
	 * @param resourceId Resource id of the {@link Representation}.
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessForRepresentation(String key, String resourceId, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Community}.
	 * @param key The workflow process key.
	 * @param communityResourceId The {@link Community} resource id.
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessForCommunity(String key, String communityResourceId, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Vocabulary}.
	 * @param key The workflow process key.
	 * @param vocabularyResourceId {@link Vocabulary} Resource id.
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessForVocabulary(String key, String vocabularyResourceId, Map<String, Object> variables);

	/**
	 * Delete specified {@link ProcessInstance}.
	 * @param processInstanceId The {@link ProcessInstance} id.
	 * @param reason The reason for deletion.
	 * @return True if successful otherwise false.
	 * @see WorkflowService#stopProcessInstance(String, String) for error codes and exceptions.
	 */
	boolean stopProcessInstance(String processInstanceId, String reason);

	// HISTORY

	/**
	 * Stop the running process instance for specified {@link Representation}.
	 * @param processInstanceId The process instance id.
	 * @param resourceId The {@link Representation} resource id.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	boolean stopProcessInstanceForRrepresentation(String processInstanceId, String resourceId, String reason);

	/**
	 * Stop the running process instance for specified {@link Community}.
	 * @param processInstanceId The process instance id.
	 * @param community The {@link Community} resource id.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	boolean stopProcessInstanceForCommunity(String processInstanceId, String communityResourceId, String reason);

	/**
	 * Stop the running process instance for specified {@link Vocabulary}.
	 * @param processInstanceId The process instance id.
	 * @param vocabularyResourceId The {@link Vocabulary} resource id.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	boolean stopProcessInstanceForVocabulary(String processInstanceId, String vocabularyResourceId, String reason);
}
