package com.collibra.dgc.core.application;

import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import com.collibra.dgc.core.service.ConfigurationService;

/**
 * Factory for creating activiti workflow engine.
 * @author amarnath
 * 
 */
public class DGCWorkflowFactory {
	private static final Logger log = LoggerFactory.getLogger(DGCWorkflowFactory.class);

	@Autowired
	ConfigurationService dgcConfig;

	@Autowired
	DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	private ProcessEngine engine;

	/**
	 * Creates workflow {@link ProcessEngine}.
	 * @return {@link ProcessEngine}.
	 */
	public ProcessEngine createEngine() {
		if (engine != null) {
			return engine;
		}

		final String databaseSchemaUpdate = dgcConfig.getString("core/workflow/engine/database-schema-update");
		final Boolean jobExecutorActivate = dgcConfig.getBoolean("core/workflow/engine/job-executor-activate");
		final Integer mailServerPort = dgcConfig.getInteger("core/mail/port");
		final String mailServerHost = dgcConfig.getString("core/mail/host");
		final String mailServerUsername = dgcConfig.getString("core/mail/username");
		final String mailServerPassword = dgcConfig.getString("core/mail/password");

		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		try {
			if (dataSource != null) {
				config.setDataSource(dataSource);
			}
			config.setTransactionManager(transactionManager);
			config.setDatabaseSchemaUpdate(databaseSchemaUpdate);

			config.setJobExecutorActivate(jobExecutorActivate);

			if (mailServerHost != null)
				config.setMailServerHost(mailServerHost);

			if (mailServerPort != null)
				config.setMailServerPort(mailServerPort);

			if (mailServerUsername != null)
				config.setMailServerUsername(mailServerUsername);

			if (mailServerPassword != null)
				config.setMailServerPassword(mailServerPassword);

			engine = config.buildProcessEngine();
			return engine;
		} catch (Exception ex) {
			log.error("Error creating workflow engine.", ex);
		}
		return null;
	}

	public boolean isEngineCreated() {
		return engine != null;
	}

	public void destroy() {
		if (engine != null) {
			engine.close();
			engine = null;
		}
	}
}
