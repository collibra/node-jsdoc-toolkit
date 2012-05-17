/**
 * 
 */
package com.collibra.dgc.rest.core.v1_0.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.job.JobComponent;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Job;
import com.collibra.dgc.rest.core.v1_0.dto.Jobs;

/**
 * @author dieterwachters
 * 
 */
@Component
@Path("/1.0/job")
public class JobResource {
	@Autowired
	private JobComponent jobComponent;

	@Autowired
	private RestModelBuilder builder;

	/**
	 * Retrieve the job with the given unique identifier.
	 * @param rId The unique identifier of the job to get.
	 * @return The requested job.
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Job.class)
	@Transactional
	public Response getJob(@PathParam("rId") String rId) {
		final Job job = builder.buildJob(jobComponent.getJob(rId));

		return Response.ok(job).build();
	}

	/**
	 * Cancel the job with the given unique identifier.
	 * @param rId The unique identifier of the job to cancel.
	 * @param message An optional message to add to the job as reason for the cancelation.
	 * @return HTTP 200 (ok)
	 */
	@DELETE
	@Path("/{rId}")
	@Transactional
	public Response cancelJob(@PathParam("rId") String rId, @QueryParam("message") String message) {
		jobComponent.cancelJob(rId, message);

		return Response.ok().build();
	}

	/**
	 * Get the list of all jobs that are currently running on the system.
	 * @return The list of all jobs that are currently running on the system.
	 */
	@GET
	@Path("/running")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Jobs.class)
	@Transactional
	public Response getRunningJobs() {
		return Response.ok(builder.buildJobs(jobComponent.getRunningJobs())).build();
	}

	/**
	 * Get the list of all jobs that are currently waiting in the queue to be ran on the system.
	 * @return The list of all jobs that are currently waiting in the queue to be ran on the system.
	 */
	@GET
	@Path("/waiting")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Jobs.class)
	@Transactional
	public Response getWaitingJobs() {
		return Response.ok(builder.buildJobs(jobComponent.getWaitingJobs())).build();
	}
}
