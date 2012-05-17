package com.collibra.dgc.core.component.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.SchedulerComponent;

/**
 * Implementation for {@link SchedulerComponent}.
 * @author amarnath
 * 
 */
@Service
public class SchedulerComponentImpl implements SchedulerComponent {
	// private static final Logger log = LoggerFactory.getLogger(SchedulerComponentImpl.class);
	//
	// private static boolean initialized;
	//
	// @Autowired
	// private SchedulerService schedulerService;
	//
	// /**
	// * TODO PORT
	// * @return
	// */
	// private String getCurrentUser() {
	// return Constants.ADMIN;
	// }
	//
	// /**
	// * Schedules notifications for all users based on their configuration. NOTE: In future we can think of moving to
	// * persisted job store if the performance at the start up is an issue due to RamJobStore. In that case we do not
	// * need the initialization, however performance will be the big difference.
	// */
	// @PostConstruct
	// synchronized void initialize() {
	// if (!initialized) {
	// try {
	// // If mail server configuration fails do not proceed.
	// if (!configureMailServer()) {
	// return;
	// }
	//
	// scheduleDailyEmailNotification();
	// log.info("Email notification job started");
	// } catch (Exception e) {
	// initialized = false;
	// log.error(e.getMessage(), e);
	// throw new RuntimeException(e);
	// }
	//
	// initialized = true;
	// }
	// }
	//
	// @Override
	// public boolean configureMailServer() {
	// try {
	// // TODO PORT
	// // getScheduler().getContext().put(SchedulerConstants.SMTP_SERVER_CONFIGURATION,
	// // new EmailConfiguration(docHandler.getXWikiContext()));
	//
	// // TODO PORT
	// String URL = "http://www.collibra.com";
	// getScheduler().getContext().put(SchedulerConstants.NOTIFICATION_EMAIL_PAGE_XWIKI_URL, URL);
	// return true;
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// return false;
	// }
	// }
	//
	// @Transactional
	// @Override
	// public boolean scheduleDailyEmailNotification() {
	// // TODO PORT
	// return false;
	// }
	//
	// @Transactional
	// @Override
	// public boolean scheduleUserEmailNotification(String user, String cronExpression) {
	// try {
	// schedulerService.scheduleUserEmailNotification(user, cronExpression);
	// return true;
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
	//
	// @Override
	// @Transactional
	// public boolean scheduleUserEmailNotification(String user) {
	// try {
	// // TODO PORT
	// String cronExpression = "NO CRON STRING";
	// if (cronExpression != null) {
	// return scheduleUserEmailNotification(user, cronExpression);
	// }
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// }
	//
	// return false;
	// }
	//
	// @Override
	// @Transactional
	// public boolean scheduleUserEmailNotification() {
	// try {
	// return scheduleUserEmailNotification(getCurrentUser());
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
	//
	// @Override
	// @Transactional
	// public Scheduler getScheduler() {
	// try {
	// return schedulerService.getScheduler();
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return null;
	// }
	// }
	//
	// @Override
	// public boolean unscheduleFromDailyEmailNotification() {
	// try {
	// return schedulerService.unscheduleFromEmailNotification(SchedulerConstants.EMAIL_NOTIFICATION_GROUP,
	// SchedulerConstants.EMAIL_NOTIFICATION_GROUP);
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
	//
	// @Override
	// public boolean unscheduleFromUserEmailNotification(String user) {
	// try {
	// return schedulerService.unscheduleFromUserEmailNotification(user);
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
	//
	// @Override
	// public boolean unscheduleFromUserEmailNotification() {
	// try {
	// return unscheduleFromUserEmailNotification(getCurrentUser());
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
	//
	// @Override
	// public synchronized boolean reinitialize() {
	// try {
	// initialized = false;
	// initialize();
	// return initialized;
	// } catch (Exception ex) {
	// log.error(ex.getMessage(), ex);
	// return false;
	// }
	// }
}
