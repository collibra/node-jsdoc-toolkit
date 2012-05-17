package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.workflow.WorkflowEngine;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Workflow API.
 * <p>
 * NOTE:
 * <p>
 * Additional Error codes that are possible based on the workflow process used. They are documented on
 * {@link WorkflowErrorCodes}.
 * @author amarnath
 */
public interface WorkflowService {
	/**
	 * Get {@link WorkflowEngine}.
	 * @return
	 */
	WorkflowEngine getEngine();

	/**
	 * Deploy the business process in the zipped archive.
	 * @param zipis The {@link ZipInputStream} of the archive.
	 * @return {@link Deployment} id.
	 * @throws AuthorizationException for {@link Permissions#WF_DEPLOY}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#FILE_INPUT_STREAM_NULL}
	 */
	String deploy(ZipInputStream zipis);

	/**
	 * Undeploy the business process.
	 * @param deploymentId The {@link Deployment} id.
	 * @throws AuthorizationException for {@link Permissions#WF_UNDEPLOY}
	 */
	void undeploy(String deploymentId);

	/**
	 * Get {@link ProcessDefinition} id.
	 * @param processDefKey The {@link ProcessDefinition} key.
	 * @return {@link ProcessDefinition} id.
	 */
	String getProcessDefinitionIdByKey(String processDefKey);

	/**
	 * Get {@link ProcessDefinition} by its id.
	 * @param processDefId {@link ProcessDefinition} id.
	 * @return The {@link ProcessDefinition}.
	 */
	ProcessDefinition getProcessDefinitionById(String processDefId);

	/**
	 * Get all {@link Deployment} ids.
	 * @return
	 */
	List<String> getDeploymentIds();

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
	String startProcessByKey(String key, Map<String, Object> variables);

	/**
	 * Complete the task.
	 * @param taskId Task id.
	 * @return True if successful otherwise false.
	 */
	void completeTask(String taskId);

	/**
	 * Get process variable with specified name.
	 * @param taskId The task id.
	 * @param varName The variable name.
	 * @return The process variable value.
	 */
	Object getVariable(String taskId, String varName);

	/**
	 * Set process variable for the specified task.
	 * @param taskId The task id.
	 * @param varName The variable name.
	 * @param value The variable value.
	 */
	void setVariable(String taskId, String varName, Object value);

	/**
	 * Find {@link Task}s for the specified user.
	 * @param username The user name.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasksForUser(String username);

	/**
	 * Find {@link Task}s for the specified user name and process variables.
	 * @param username The user name.
	 * @param variables The process variables.
	 * @return The {@link Task}s
	 */
	List<Task> findTasksForUser(String username, Map<String, Object> variables);

	/**
	 * Find {@link Task}s for the specified user name and the resource.
	 * @param username The user name.
	 * @param resourceId The resource id.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasksForUser(String username, String resourceId);

	/**
	 * Find the {@link Task}s for the currently logged in user.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasksForCurrentUser();

	/**
	 * Find {@link Task}s for currently logged in user with specified process variables.
	 * @param variables The process variables.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasksForCurrentUser(Map<String, Object> variables);

	/**
	 * Find {@link Task}s for currently logged in user for specified resource.
	 * @param resourceId The resource id.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasksForCurrentUser(String resourceId);

	/**
	 * Find all {@link Task}s with specified process variables.
	 * @param variables The process variables.
	 * @return The {@link Task}s.
	 */
	List<Task> findTasks(Map<String, Object> variables);

	/**
	 * Get all {@link Task}s in the system.
	 * @return The {@link Task}s.
	 */
	List<Task> getAllTasks();

	/**
	 * Get all {@link ProcessInstance}s running in the system.
	 * @return The {@link ProcessInstance}s.
	 */
	List<ProcessInstance> getAllProcessInstances();

	/**
	 * Delete specified {@link ProcessInstance}.
	 * @param processInstanceId The {@link ProcessInstance} id.
	 * @param reason The reason for deletion.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	void stopProcessInstance(String processInstanceId, String reason);

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
	 * @param representation The {@link Representation} to which task is associated.
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
	String createTask(Representation representation, String assignee, String title, String status, String resolution,
			String date, String severity, String description);

	/**
	 * Get {@link ProcessDefinition}.
	 * @param businessProcessKey The business process key.
	 * @return The {@link ProcessDefinition}.
	 */
	ProcessDefinition getProcessDefinition(String businessProcessKey);

	/**
	 * Get workflow start form key.
	 * @param businessProcessKey The business process key.
	 * @return The start form key.
	 */
	String getStartFormKey(String businessProcessKey);

	/**
	 * Get form key for specified {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The form key.
	 */
	String getFormKey(String taskId);

	/**
	 * Get all deployed workflows.
	 * @return {@link ProcessDefinition}s.
	 */
	List<ProcessDefinition> getDeployedProcesses();

	/**
	 * Start workflow process for specified {@link Representation}.
	 * @param key The workflow process key.
	 * @param representation The {@link Representation}.
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessByKey(String key, Representation representation, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Vocabulary}.
	 * @param key The workflow process key.
	 * @param vocabulary
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessByKey(String key, Vocabulary vocabulary, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Community}.
	 * @param key The workflow process key.
	 * @param community The {@link Community}
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessByKey(String key, Community community, Map<String, Object> variables);

	/**
	 * Start workflow process for specified {@link Resource}.
	 * @param key The workflow process key.
	 * @param resource The {@link Resource}.
	 * @param variables The process variables.
	 * @return The process instance id.
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_START} and the permission specific to the
	 *             workflow process specified by key.
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#WF_PROCESS_KEY_EMPTY},
	 *             {@link DGCErrorCodes#WF_PROCESS_KEY_NULL}
	 */
	String startProcessByKey(String key, Resource resource, Map<String, Object> variables);

	/**
	 * Stop the running process instance for specified {@link Representation}.
	 * @param processInstanceId The process instance id.
	 * @param representation The {@link Representation}.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	void stopProcessInstance(String processInstanceId, Representation representation, String reason);

	/**
	 * Stop the running process instance for specified {@link Vocabulary}.
	 * @param processInstanceId The process instance id.
	 * @param vocabulary The {@link Vocabulary}.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	void stopProcessInstance(String processInstanceId, Vocabulary vocabulary, String reason);

	/**
	 * Stop the running process instance for specified {@link Community}.
	 * @param processInstanceId The process instance id.
	 * @param community The {@link Community}.
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	void stopProcessInstance(String processInstanceId, Community community, String reason);

	/**
	 * Stop the running process instance for specified {@link Resource}.
	 * @param processInstanceId The process instance id.
	 * @param resource The {@link Resource}
	 * @param reason The reason for stopping.
	 * @throws WorkflowException with error code {@link WorkflowErrorCodes#PROCESS_INSTANCE_NOT_FOUND}
	 * @throws AuthorizationException for {@link Permissions#WF_PROCESS_STOP} and the permission specific to the
	 *             workflow process specified by key.
	 */
	void stopProcessInstance(String processInstanceId, Resource resource, String reason);

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
	 * Get users who are owners of the {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return Users.
	 */
	Collection<String> getTaskOwners(String taskId);

	/**
	 * Remove the user as task owner.
	 * @param taskId The task id.
	 * @param user The user.
	 * @return True if successful otherwise false.
	 */
	boolean removeTaskOwner(String taskId, String user);

	/**
	 * Get process key for the {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The process key.
	 */
	String findProcessKey(String taskId);

	/**
	 * Get process key for the {@link Task}.
	 * @param task The {@link Task}.
	 * @return The process key.
	 */
	String findProcessKey(Task task);

	/**
	 * Get {@link Task}.
	 * @param taskId The {@link Task} id.
	 * @return The {@link Task}.
	 */
	Task findTask(String taskId);

	/**
	 * Remove {@link Task}. Note that this will stop the {@link ProcessInstance} of the {@link Task} there by removing
	 * all {@link Task}s associated with that {@link ProcessInstance}.
	 * @param taskId The {@link Task} id.
	 * @param reason The reason for removing {@link Task}.
	 */
	void removeTask(String taskId, String reason);

	/**
	 * 
	 * @param variables
	 * @return
	 */
	Collection<ProcessInstance> findProcessInstances(Map<String, Object> variables);

	// HISTORY

	/**
	 * Get completed historic tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	Collection<HistoricTaskInstance> findCompletedTasks(int start, int count);

	/**
	 * Get deleted tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	Collection<HistoricTaskInstance> findDeletedTasks(int start, int count);

	/**
	 * Get unfinished tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	Collection<HistoricTaskInstance> findHistoricUnfinishedTasks(int start, int count);

	/**
	 * Get completed process instances for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricProcessInstance}s
	 */
	Collection<HistoricProcessInstance> findCompletedProcesses(int start, int count);

	/**
	 * Get unfinished process instances for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricProcessInstance}s
	 */
	Collection<HistoricProcessInstance> findHistoricUnfinishedProcesses(int start, int count);
}
