/**
 * 
 */
package com.collibra.dgc.rest.core.v1_0.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.component.bootstrap.BootstrapComponent;
import com.collibra.dgc.core.service.restart.IRestartListener;
import com.collibra.dgc.core.service.restart.RestartService;
import com.collibra.dgc.rest.core.DGCJerseyServlet;

/**
 * REST interface for initializing DGC.
 * @author dieterwachters
 */
@Component
@Path("/1.0/initialize")
public class InitializeResource implements InitializingBean {
	@Autowired
	private BootstrapComponent bootstrapComponent;
	@Autowired
	private RestartService restartService;

	/**
	 * Initialize Collibra Data Governance Center.
	 * 
	 * This will only work when the there is no database configured yet.
	 * @param driver The Java driver class to use to connect to the database.
	 * @param url The connection (JDBC) URL for the database.
	 * @param username The optional user name.
	 * @param password The optional password.
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/init")
	public void initialize(@FormParam("driver") String driver, @FormParam("url") String url,
			@FormParam("username") String username, @FormParam("password") String password,
			@FormParam("database") String database) {

		bootstrapComponent.initialize(driver, url, username, password, database);
	}

	/**
	 * Bootstrap the Collibra Data Governance Center.
	 * 
	 * This can only be done when the database is still empty or the user has full admin rights.
	 * @param bootstrap The optional bootstrap script to run.
	 * @return The unique identifier of the job that is running the bootstrapping
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/bootstrap")
	public String bootstrap(@FormParam("bootstrap") String bootstrap) {
		return bootstrapComponent.bootstrap(bootstrap);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		restartService.addRestartListener(new IRestartListener() {
			@Override
			public void restart() {
				DGCJerseyServlet.reloadInstance();
			}
		});
	}
}
