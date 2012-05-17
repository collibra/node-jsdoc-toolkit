package com.collibra.dgc.core.workflow.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.workflow.WorkflowHistory;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class WorkflowHistoryImpl implements WorkflowHistory {
	@Autowired(required = false)
	private ProcessEngine processEngine;

	public HistoryService getInternalService() {
		return processEngine == null ? null : processEngine.getHistoryService();
	}

	public List<HistoricTaskInstance> getCompletedTasks(int start, int count) {
		return getTasksWithReason(start, count, "completed");
	}

	public List<HistoricTaskInstance> getDeletedTasks(int start, int count) {
		return getTasksWithReason(start, count, "deleted");
	}

	public List<HistoricTaskInstance> getUnfinishedTasks(int start, int count) {
		if (getInternalService() == null)
			return new ArrayList<HistoricTaskInstance>();
		return getInternalService().createHistoricTaskInstanceQuery().unfinished().orderByHistoricTaskInstanceEndTime()
				.desc().listPage(start, count);
	}

	public List<HistoricProcessInstance> getCompletedProcesses(int start, int count) {
		if (getInternalService() == null)
			return new ArrayList<HistoricProcessInstance>();
		return getInternalService().createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime()
				.desc().listPage(start, count);
	}

	public List<HistoricProcessInstance> getUnfinishedProcesses(int start, int count) {
		if (getInternalService() == null)
			return new ArrayList<HistoricProcessInstance>();
		return getInternalService().createHistoricProcessInstanceQuery().unfinished().orderByProcessInstanceEndTime()
				.desc().listPage(start, count);
	}

	private List<HistoricTaskInstance> getTasksWithReason(int start, int count, String reason) {
		if (getInternalService() == null)
			return new ArrayList<HistoricTaskInstance>();
		return getInternalService().createHistoricTaskInstanceQuery().taskDeleteReason(reason)
				.orderByHistoricTaskInstanceEndTime().desc().listPage(start, count);
	}
}
