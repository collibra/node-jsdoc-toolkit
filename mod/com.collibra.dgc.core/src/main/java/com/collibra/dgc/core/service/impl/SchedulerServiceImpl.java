package com.collibra.dgc.core.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.service.SchedulerService;

@Service
public class SchedulerServiceImpl extends AbstractService implements SchedulerService {
	private static final Log log = LogFactory.getLog(SchedulerServiceImpl.class);

	// @Override
	// public void scheduleEmailNotification(String name, String group, String cronExpression, Class<? extends Job>
	// jobClass) {
	// Defense.notNull(cronExpression, GlossaryErrorCodes.CRON_EXPRESSION_NULL);
	// Defense.notEmpty(cronExpression, GlossaryErrorCodes.CRON_EXPRESSION_EMPTY);
	//
	// try {
	// schedulerInternal(name, group, cronExpression, jobClass);
	// } catch (Exception ex) {
	// throw new SchedulingException(ex.getMessage(), GlossaryErrorCodes.SCHEDULING_JOB_FAILED);
	// }
	// }
	//
	// @Override
	// public boolean unscheduleFromEmailNotification(String name, String group) {
	// Scheduler scheduler = getScheduler();
	// if (scheduler == null) {
	// throw new SchedulingException("No scheduler found.", GlossaryErrorCodes.SCHEDULER_NOT_AVAILABLE);
	// }
	//
	// try {
	// return unscheduleInternal(scheduler, name, group);
	// } catch (SchedulerException e) {
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// }
	// }
	//
	// @Override
	// public void scheduleUserEmailNotification(String user, String cronExpression) {
	// Defense.notNull(user, GlossaryErrorCodes.USER_NAME_NULL);
	// Defense.notEmpty(user, GlossaryErrorCodes.USER_NAME_EMPTY);
	// Defense.notNull(cronExpression, GlossaryErrorCodes.CRON_EXPRESSION_NULL);
	// Defense.notEmpty(cronExpression, GlossaryErrorCodes.CRON_EXPRESSION_EMPTY);
	//
	// try {
	// Scheduler scheduler = getScheduler();
	// if (scheduler == null) {
	// throw new SchedulingException("No scheduler found.", GlossaryErrorCodes.SCHEDULER_NOT_AVAILABLE);
	// }
	//
	// // Unschedule old notification.
	// unscheduleInternal(scheduler, user);
	//
	// // Create job
	// JobDetail job = JobBuilder.newJob(UserEmailNotificationJob.class).withIdentity(user,
	// SchedulerConstants.EMAIL_NOTIFICATION_GROUP).build();
	//
	// Map dataMap = job.getJobDataMap();
	// if (!prepareJobDetails(dataMap, user)) {
	// log.warn("Email notifications disabled for user '" + user + "'");
	// return;
	// }
	//
	// // Create trigger
	// Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
	// .withIdentity(user, SchedulerConstants.EMAIL_NOTIFICATION_GROUP).build();
	//
	// // Schedule the job
	// scheduler.scheduleJob(job, trigger);
	// } catch (SchedulerException e) {
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// } catch (ParseException e) {
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// }
	// }
	//
	// @Override
	// public boolean unscheduleFromUserEmailNotification(String user) {
	// Defense.notNull(user, GlossaryErrorCodes.USER_NAME_NULL);
	// Defense.notEmpty(user, GlossaryErrorCodes.USER_NAME_EMPTY);
	// Scheduler scheduler = getScheduler();
	// if (scheduler == null) {
	// throw new SchedulingException("No scheduler found.", GlossaryErrorCodes.SCHEDULER_NOT_AVAILABLE);
	// }
	//
	// try {
	// return unscheduleInternal(scheduler, user);
	// } catch (SchedulerException e) {
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// }
	// }
	//
	// private void schedulerInternal(String name, String group, String cronExpression, Class<? extends Job> jobClass)
	// throws ParseException, SchedulerException {
	// Scheduler scheduler = getScheduler();
	// if (scheduler == null) {
	// throw new SchedulingException("No scheduler found.", GlossaryErrorCodes.SCHEDULER_NOT_AVAILABLE);
	// }
	//
	// // Unschedule the job
	// unscheduleInternal(scheduler, name, group);
	//
	// // Create job
	// JobDetail job = JobBuilder.newJob(jobClass).withIdentity(name,
	// SchedulerConstants.EMAIL_NOTIFICATION_GROUP).build();
	//
	// // Create trigger
	// CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
	// .withIdentity(name, SchedulerConstants.EMAIL_NOTIFICATION_GROUP).build();
	//
	// // Schedule the job
	// scheduler.scheduleJob(job, trigger);
	// }
	//
	// private boolean unscheduleInternal(Scheduler scheduler, String user) throws SchedulerException {
	// Defense.notNull(user, GlossaryErrorCodes.USER_NAME_NULL);
	// Defense.notEmpty(user, GlossaryErrorCodes.USER_NAME_EMPTY);
	//
	// return unscheduleInternal(scheduler, user, SchedulerConstants.EMAIL_NOTIFICATION_GROUP);
	// }
	//
	// private boolean unscheduleInternal(Scheduler scheduler, String name, String group) throws SchedulerException {
	// JobDetail job = scheduler.getJobDetail(new JobKey(name, group));
	// if (job != null) {
	// // If a trigger is already registered for the user then remove it.
	// return scheduler.unscheduleJob(new TriggerKey(name, group));
	// }
	//
	// return false;
	// }
	//
	// @Override
	// public Scheduler getScheduler() {
	// try {
	// HttpServletRequest request = null;
	//
	// if (RequestContextHolder.getRequestAttributes() != null) {
	// request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	// } else {
	// Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	// scheduler.start();
	// return scheduler;
	// }
	// return ((StdSchedulerFactory)
	// request.getSession().getServletContext().getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY)).getScheduler();
	// } catch (SchedulerException e) {
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// }
	// }
	//
	// private boolean prepareJobDetails(Map dataMap, String userName) {
	// // Add user name to map.
	// dataMap.put(SchedulerConstants.USER_NAME, userName);
	//
	// // Add email to map
	// // TODO PORT
	// dataMap.put(SchedulerConstants.NOTIFICATION_USER_EMAIL, "TODO PORT");
	//
	// String contentUrl = getUrl(SchedulerConstants.NOTIFICATION_EMAIL_CONTENT_PARAMS + "&user=" + userName);
	// dataMap.put(SchedulerConstants.NOTIFICATION_EMAIL_CONTENT_URL, contentUrl);
	// String subjectUrl = getUrl(SchedulerConstants.NOTIFICATION_EMAIL_SUBJECT_PARAMS + "&user=" + userName);
	// dataMap.put(SchedulerConstants.NOTIFICATION_EMAIL_SUBJECT_URL, subjectUrl);
	//
	// return true;
	// }
	//
	// private String getUrl(String params) {
	// // TODO PORT
	// return "http://www.collibra.com";
	// }
}
