package com.collibra.dgc.api.workflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.component.WorkflowComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowEngine;
import com.collibra.dgc.core.workflow.WorkflowProcesses;

/**
 * Test {@link WorkflowComponent} APIs.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestWorkflowComponent extends AbstractDGCBootstrappedApiTest {

	private static final String USER_1 = "user1";
	private static final String USER_2 = "user2";
	private static final String USER_3 = "user3";

	@Autowired
	private WorkflowEngine workflowEngine;
	@Autowired
	private UserComponent userComponent;

	@Test
	public void testDeployment() {
		// Deploy the zip file with work flows.
		String deploymentId = workflowComponent.deploy(new ZipInputStream(getClass().getResourceAsStream(
				"/com/collibra/dgc/workflow/workflow.zip")));
		Assert.assertNotNull(deploymentId);

		// Assert for process definitions
		ProcessDefinition processDef = workflowComponent.getProcessDefinition("Articulation_Process");
		Assert.assertNotNull(processDef);

		processDef = workflowComponent.getProcessDefinition("Decommissioning");
		Assert.assertNotNull(processDef);

		Collection<ProcessDefinition> defs = workflowComponent.getDeployedProcesses();
		Assert.assertEquals(6, defs.size());
		assertProcessKeys(defs, WorkflowProcesses.APPROVAL, WorkflowProcesses.ARTICULATION,
				WorkflowProcesses.DECOMMISSIONING, WorkflowProcesses.REVIEW, WorkflowProcesses.TASK_MANAGEMENT);

		Collection<String> deploymentIds = workflowComponent.getDeploymentIds();
		Assert.assertEquals(1, deploymentIds.size());
		Assert.assertEquals(deploymentId, deploymentIds.iterator().next());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
		processDef = workflowComponent.getProcessDefinition("Articulation_Process");
		Assert.assertNull(processDef);
	}

	@Test
	public void testCreateTask() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		Assert.assertNotNull(taskId);

		Term term = termComponent.addTerm(createVocabulary().getId().toString(), "Term");
		taskId = workflowComponent.createTask(term.getId().toString(), USER_1, "Test Task", "Assigned", "",
				"31/01/2012", "Critical", "For testing...");
		Assert.assertNotNull(taskId);

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindCompletedProcesses() {
		Collection<HistoricProcessInstance> instances = workflowComponent.findCompletedProcesses(0, 100);
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		workflowComponent.setVariable(taskId, "closed", Boolean.TRUE);
		workflowComponent.completeTask(taskId);

		instances = workflowComponent.findCompletedProcesses(0, 100);
		Assert.assertEquals(1, instances.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindCompletedTasks() {
		Collection<HistoricTaskInstance> instances = workflowComponent.findCompletedTasks(0, 100);
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		workflowComponent.setVariable(taskId, "closed", Boolean.TRUE);
		workflowComponent.completeTask(taskId);

		instances = workflowComponent.findCompletedTasks(0, 100);
		Assert.assertEquals(1, instances.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindDeletedTasks() {
		Collection<HistoricTaskInstance> instances = workflowComponent.findDeletedTasks(0, 100);
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing ...");
		workflowComponent.stopProcessInstance(workflowComponent.getTask(taskId).getProcessInstanceId(), null);
		instances = workflowComponent.findDeletedTasks(0, 100);
		Assert.assertEquals(1, instances.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindTasks() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_ASSIGNEE, USER_1);
		variables.put(WorkflowConstants.VAR_TASK_TITLE, "Test Task");

		Collection<Task> instances = workflowComponent.findTasks(variables);
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");

		instances = workflowComponent.findTasks(variables);
		Assert.assertEquals(1, instances.size());
		Assert.assertEquals(taskId, instances.iterator().next().getId());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindTasksForCurrentUser() {
		Term term = termComponent.addTerm(createVocabulary().getId().toString(), "Term");
		Collection<Task> instances = workflowComponent.findTasksForCurrentUser();
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(term.getId().toString(), "Admin", "Test Task", "Assigned", "",
				"31/01/2012", "Critical", "For testing...");

		instances = workflowComponent.findTasksForCurrentUser();
		Assert.assertEquals(1, instances.size());
		Assert.assertEquals(taskId, instances.iterator().next().getId());

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_TASK_STATUS, "Assigned");
		variables.put(WorkflowConstants.VAR_TASK_TITLE, "Test Task");

		instances = workflowComponent.findTasksForCurrentUser(variables);
		Assert.assertEquals(1, instances.size());
		Assert.assertEquals(taskId, instances.iterator().next().getId());

		workflowComponent.createTask(term.getId().toString(), "Admin", "Test Task2", "Assigned", "", "31/01/2012",
				"Critical", "For testing...");
		instances = workflowComponent.findTasksForCurrentUser(term.getId().toString());
		Assert.assertEquals(2, instances.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testFindTasksForUser() {
		Term term = termComponent.addTerm(createVocabulary().getId().toString(), "Term");
		Collection<Task> instances = workflowComponent.findTasksForCurrentUser();
		Assert.assertEquals(0, instances.size());

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		String taskId = workflowComponent.createTask(term.getId().toString(), USER_1, "Test Task", "Assigned", "",
				"31/01/2012", "Critical", "For testing...");

		instances = workflowComponent.findTasksForUser(USER_1);
		Assert.assertEquals(1, instances.size());
		Assert.assertEquals(taskId, instances.iterator().next().getId());

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_TASK_STATUS, "Assigned");
		variables.put(WorkflowConstants.VAR_TASK_TITLE, "Test Task");

		instances = workflowComponent.findTasksForUser(USER_1, variables);
		Assert.assertEquals(1, instances.size());
		Assert.assertEquals(taskId, instances.iterator().next().getId());

		workflowComponent.createTask(term.getId().toString(), USER_1, "Test Task2", "Assigned", "", "31/01/2012",
				"Critical", "For testing...");
		instances = workflowComponent.findTasksForUser(USER_1, term.getId().toString());
		Assert.assertEquals(2, instances.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testGetAllProcessInstancesAndTasks() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");

		workflowComponent.createTask(USER_1, "Test Task 1", "Assigned", "", "31/01/2012", "Critical", "For testing...");
		workflowComponent.createTask(USER_2, "Test Task 2", "Assigned", "", "31/01/2012", "Critical", "For testing...");
		workflowComponent.createTask(USER_3, "Test Task 3", "Assigned", "", "31/01/2012", "Critical", "For testing...");

		Collection<ProcessInstance> instances = workflowComponent.getAllProcessInstances();
		Assert.assertEquals(3, instances.size());
		for (ProcessInstance instace : instances) {
			ProcessDefinition def = workflowComponent.getProcessDefinitionById(instace.getProcessDefinitionId());
			Assert.assertEquals(WorkflowProcesses.TASK_MANAGEMENT, def.getKey());
		}

		Collection<Task> tasks = workflowComponent.getAllTasks();
		Assert.assertEquals(3, tasks.size());

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testGetFormKey() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");
		String startFormKey = workflowComponent.getStartFormKey("ProcessWithStartFormKey");
		Assert.assertEquals("startformkey.form", startFormKey);

		workflowComponent.startProcess("ProcessWithStartFormKey", new HashMap<String, Object>());
		Collection<Task> tasks = workflowComponent.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());

		String formKey = workflowComponent.getFormKey(tasks.iterator().next().getId());
		Assert.assertNull(formKey);

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testGetRightForProcess() {
		String key = workflowComponent.getRightForStartingProcess(WorkflowProcesses.APPROVAL);
		Assert.assertNotNull(key);
		Assert.assertEquals("WF_APPROVAL_START", key);

		key = workflowComponent.getRightForStoppingProcess(WorkflowProcesses.APPROVAL);
		Assert.assertNotNull(key);
		Assert.assertEquals("WF_APPROVAL_STOP", key);

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		String taskId = workflowComponent.createTask(USER_1, "Test Task 1", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		key = workflowComponent.getRightForStoppingProcessInstance(workflowComponent.getTask(taskId)
				.getProcessInstanceId());
		Assert.assertNotNull(key);
		Assert.assertEquals("WF_GTP_STOP", key);

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testGetTaskOwners() {
		UserData u1 = userComponent.addUser(USER_1, USER_1);
		UserData u2 = userComponent.addUser(USER_2, USER_2);
		UserData u3 = userComponent.addUser(USER_3, USER_3);

		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_URI,
				VOCABULARY_NAME);
		Term term = termComponent.addTerm(voc.getId().toString(), "Term");

		rightsComponent.addMember(u1.getId(), Constants.ADMIN, community.getId().toString()); // Responsible
		rightsComponent.addMember(u2.getId(), Constants.STEWARD, community.getId().toString()); // Accountable
		rightsComponent.addMember(u3.getId(), Constants.ADMIN, community.getId().toString()); // Reassignment

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ArticulationProcess.bpmn20.xml");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowComponent.startProcess(WorkflowProcesses.ARTICULATION, variables);

		Collection<Task> tasks = workflowComponent.getAllTasks();
		Assert.assertEquals(1, tasks.size());

		Collection<String> owners = workflowComponent.getTaskOwners(tasks.iterator().next().getId());
		Assert.assertEquals(3, owners.size());
		Assert.assertTrue(owners.contains(USER_1));
		Assert.assertTrue(owners.contains(USER_2));
		Assert.assertTrue(owners.contains(USER_3));

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testGetVariable() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");

		String value = (String) workflowComponent.getVariable(taskId, WorkflowConstants.VAR_TASK_STATUS);
		Assert.assertEquals("Assigned", value);
		value = (String) workflowComponent.getVariable(taskId, WorkflowConstants.VAR_TASK_TITLE);
		Assert.assertEquals("Test Task", value);
		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testRemoveTask() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		Assert.assertNotNull(workflowComponent.getTask(taskId));

		workflowComponent.removeTask(taskId, "Testing...");
		Assert.assertNull(workflowComponent.getTask(taskId));

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testSetVariable() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		String taskId = workflowComponent.createTask(USER_1, "Test Task", "Assigned", "", "31/01/2012", "Critical",
				"For testing...");
		String value = (String) workflowComponent.getVariable(taskId, "MyVar");
		Assert.assertNull(value);

		workflowComponent.setVariable(taskId, "MyVar", "MyVal");
		Assert.assertEquals("MyVal", workflowComponent.getVariable(taskId, "MyVar"));

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	@Test
	public void testStartStopProcess() {
		Community community = createCommunity();
		Community subCommunity = communityComponent.addCommunity(community.getId().toString(), "sp", "sp-uri");
		Vocabulary voc = vocabularyComponent.addVocabulary(subCommunity.getId().toString(), VOCABULARY_URI,
				VOCABULARY_NAME);
		Term term = termComponent.addTerm(voc.getId().toString(), "Term");

		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		String procInstId = workflowComponent.startProcess("ProcessWithStartFormKey", new HashMap<String, Object>());
		Assert.assertNotNull(workflowEngine.getProcessInstance(procInstId));

		workflowComponent.stopProcessInstance(procInstId, "Testing...");
		Assert.assertNull(workflowEngine.getProcessInstance(procInstId));

		procInstId = workflowComponent
				.startProcessForRepresentation("ProcessWithStartFormKey", term.getId().toString());
		Assert.assertNotNull(workflowEngine.getProcessInstance(procInstId));

		workflowComponent.stopProcessInstanceForRrepresentation(procInstId, term.getId().toString(), "Testing...");
		Assert.assertNull(workflowEngine.getProcessInstance(procInstId));

		procInstId = workflowComponent.startProcessForVocabulary("ProcessWithStartFormKey", voc.getId().toString(),
				new HashMap<String, Object>());
		Assert.assertNotNull(workflowEngine.getProcessInstance(procInstId));

		workflowComponent.stopProcessInstanceForVocabulary(procInstId, voc.getId().toString(), "Testing...");
		Assert.assertNull(workflowEngine.getProcessInstance(procInstId));

		procInstId = workflowComponent.startProcessForCommunity("ProcessWithStartFormKey", subCommunity.getId()
				.toString(), new HashMap<String, Object>());
		Assert.assertNotNull(workflowEngine.getProcessInstance(procInstId));

		workflowComponent.stopProcessInstanceForCommunity(procInstId, subCommunity.getId().toString(), "Testing...");
		Assert.assertNull(workflowEngine.getProcessInstance(procInstId));

		procInstId = workflowComponent.startProcessForCommunity("ProcessWithStartFormKey",
				community.getId().toString(), new HashMap<String, Object>());
		Assert.assertNotNull(workflowEngine.getProcessInstance(procInstId));

		workflowComponent.stopProcessInstanceForCommunity(procInstId, community.getId().toString(), "Testing...");
		Assert.assertNull(workflowEngine.getProcessInstance(procInstId));

		// Undeploy
		workflowComponent.undeploy(deploymentId);
	}

	private String deploy(String fileName) {
		ProcessEngine engine = workflowEngine.getInternalEngine();
		String deploymentId = engine.getRepositoryService().createDeployment().addClasspathResource(fileName).deploy()
				.getId();
		Assert.assertNotNull(deploymentId);

		return deploymentId;
	}

	private void assertProcessKeys(Collection<ProcessDefinition> procDefs, String... keys) {
		for (String key : keys) {
			boolean found = false;
			for (ProcessDefinition def : procDefs) {
				if (key.equals(def.getKey())) {
					found = true;
					break;
				}
			}

			if (!found) {
				Assert.fail("Proces '" + key + "' not found.");
			}
		}
	}
}
