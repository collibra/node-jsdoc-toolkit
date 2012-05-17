package com.collibra.dgc.service.workflow;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;

/**
 * 
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAdditionalWorkflows extends AbstractWorkflowServiceTests {

	@Test
	public void testTimerAsVariable() {
		// Deploy file with work flows.
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TimerAsVariableTest.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		// Start the articulation process
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("owner", USER_1);
		variables.put("timer", "PT3M");
		workflowEngine.startProcessByKey("TimerAsVariableTest", variables);
		resetTransaction();

		List<Task> tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());
		Assert.assertEquals("FirstTask", tasks.get(0).getName());

		// After 7 days the timer event should fire.
		List<Job> jobs = workflowService.getEngine().getInternalEngine().getManagementService().createJobQuery()
				.processInstanceId(tasks.get(0).getProcessInstanceId()).list();
		Assert.assertEquals(1, jobs.size());
		ClockUtil.setCurrentTime(new Date(new Date().getTime() + (4 * 60 * 1000) + 5000));
		waitForJobExecutorToProcessAllJobs(5000, 1000);

		// resetTransaction();

		// After the timeout accountable gets the task.
		tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());
		Assert.assertEquals("DummyTask", tasks.get(0).getName());

		// Reset clock
		ClockUtil.reset();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testUnfinishedProcessesHistory() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		// Process with start form key.
		ProcessDefinition processDefinition = workflowService.getProcessDefinition("ProcessWithStartFormKey");
		Assert.assertNotNull(processDefinition);

		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);
		resetTransaction();

		// Delete the process
		workflowService.getEngine().getInternalEngine().getRuntimeService()
				.deleteProcessInstance(processInstanceId, "Testing...");
		resetTransaction();

		// FIXME: The deleted process is not marked as delete and there is no query to find it. It is also considered as
		// completed process. Need to check in the next version of activiti. Also what is an 'Unfinsihed' process?
		// Nothing much is implemented in the currently used version.
		Collection<HistoricProcessInstance> processes = workflowService.findCompletedProcesses(0, 10);
		// Assert.assertEquals(1, processes.size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testDeletedUser() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		rightsService.addMember(USER_3_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Reassignment

		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ArticulationProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);
		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.ARTICULATION));

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		String processInstanceId = workflowService.startProcessByKey(WorkflowProcesses.ARTICULATION, variables);
		resetTransaction();
		workflowService.stopProcessInstance(processInstanceId, null);
		resetTransaction();

		Collection<HistoricTaskInstance> tasks = workflowService.findDeletedTasks(0, 10);
		Assert.assertTrue(tasks.size() > 0);

		for (HistoricTaskInstance task : tasks) {
			Assert.assertNotNull(task.getAssignee());
			System.out.println(task.getAssignee());
		}

		// Undeploy
		workflowService.undeploy(deploymentId);
	}
}
