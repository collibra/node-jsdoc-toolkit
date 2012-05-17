package com.collibra.dgc.core.workflow;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

/**
 * 
 * @author amarnath
 * 
 */
public interface WorkflowHistory {
	/**
	 * @return Activiti {@link HistoryService}.
	 */
	HistoryService getInternalService();

	/**
	 * Get completed historic tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	List<HistoricTaskInstance> getCompletedTasks(int start, int count);

	/**
	 * Get deleted tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	List<HistoricTaskInstance> getDeletedTasks(int start, int count);

	/**
	 * Get unfinished tasks list for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricTaskInstance}s.
	 */
	List<HistoricTaskInstance> getUnfinishedTasks(int start, int count);

	/**
	 * Get completed process instances for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricProcessInstance}s
	 */
	List<HistoricProcessInstance> getCompletedProcesses(int start, int count);

	/**
	 * Get unfinished process instances for specified range.
	 * @param start Starting of result entries.
	 * @param count The number of results.
	 * @return {@link HistoricProcessInstance}s
	 */
	List<HistoricProcessInstance> getUnfinishedProcesses(int start, int count);
}
