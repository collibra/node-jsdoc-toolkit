package com.collibra.dgc.service.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Test for Approval process.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestApprovalProcess extends AbstractWorkflowServiceTests {
	private String deploymentId;

	@Test
	public void testNormalScenarioApprove() {
		testNormalFlow(true, "Accepted");
	}

	@Test
	public void testNormalScenarioReject() {
		testNormalFlow(false, "Rejected");
	}

	@Test
	public void testNoAccountableUsers() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_3_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_4_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ApprovalProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		try {
			workflowService.startProcessByKey(WorkflowProcesses.APPROVAL, variables);
			Assert.fail();
		} catch (WorkflowException ex) {
			// Success
			rollback();
		}

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testMissingDecisionVariable() {
		Term term = startProcess();

		// Assert for status.
		assertForStatus(term.getId(), "Approval Pending");

		// STEP 1: Approval pending for accountable.
		List<Task> tasks = workflowService.findTasksForUser(USER_2);
		Assert.assertEquals(1, tasks.size());

		try {
			workflowService.completeTask(tasks.get(0).getId());
			Assert.fail();
		} catch (WorkflowException ex) {
			// Success
			Assert.assertEquals(WorkflowErrorCodes.DECISION_MISSING, ex.getErrorCode());
			rollback();
		}

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	private void testNormalFlow(boolean decision, String finalState) {
		Term term = startProcess();

		// Assert for status.
		assertForStatus(term.getId(), "Approval Pending");

		// STEP 1: Approval pending for accountable.
		List<Task> tasks = workflowService.findTasksForUser(USER_2);
		Assert.assertEquals(1, tasks.size());

		// Approve
		workflowService.setVariable(tasks.get(0).getId(), WorkflowConstants.VAR_DECISION, decision);
		workflowService.setVariable(tasks.get(0).getId(), WorkflowConstants.VAR_DECISION_REASON, "Testing...");
		workflowService.completeTask(tasks.get(0).getId());

		// Assert for status.
		assertForStatus(term.getId(), finalState);

		// STEP 2: Notification send to all users.

		// Assert no more pending tasks.
		tasks = workflowService.getAllTasks();
		Assert.assertEquals(0, tasks.size());

		resetTransaction();

		// Assert for history tasks to have a assignee.
		assertForHisotry();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	private Term startProcess() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		rightsService.addMember(USER_3_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_4_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		resetTransaction();

		deploymentId = deploy("com/collibra/dgc/test/workflow/ApprovalProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.APPROVAL));

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowService.startProcessByKey(WorkflowProcesses.APPROVAL, variables);

		return term;
	}
}
