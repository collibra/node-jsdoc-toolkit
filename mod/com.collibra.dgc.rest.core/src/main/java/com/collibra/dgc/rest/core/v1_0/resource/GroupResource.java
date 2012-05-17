package com.collibra.dgc.rest.core.v1_0.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.GroupComponent;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.GroupReference;

/**
 * Public Webservice to manage groups and their users.
 * @author GKDAI63
 * 
 */
@Component
@Path("/1.0/group")
public class GroupResource {

	@Autowired
	private RestModelBuilder builder;

	@Autowired
	private GroupComponent groupComponent;

	// Create
	/**
	 * Creates a group
	 * @param groupName the groups name
	 * @return a {@link GroupReference} with HTTP code 201 created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addGroup(@FormParam("groupName") String groupName) {
		Group g = groupComponent.addGroup(groupName);
		return Response.status(Status.CREATED).entity(builder.buildGroupReference(g)).build();
	}

	/**
	 * Adds a user to a group
	 * @param groupRId the group to add the user too
	 * @param userRId the user to be added too the group
	 * @return a {@link com.collibra.dgc.rest.core.v1_0.dto.Group} with http code 201 Created
	 */
	@POST
	@Path("/user")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addUserToGroup(@FormParam("groupRId") String groupRId, @FormParam("userRId") String userRId) {
		Group g = groupComponent.addUserToGroup(groupRId, userRId);
		return Response.status(Status.CREATED).entity(builder.buildGroup(g)).build();
	}

	// Read
	/**
	 * Retrieves a group
	 * @param groupRId the group's id
	 * @return
	 */
	@Path("/{groupRId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response getGroup(@PathParam("groupRId") String groupRId) {
		Group g = groupComponent.getGroup(groupRId);
		return Response.ok().entity(builder.buildGroupReference(g)).build();
	}

	// Delete

	/**
	 * Removes a group
	 * @param groupRId the group's id
	 * @return Http Code 200 ok
	 */
	@Path("/{groupRId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response removeGroup(@PathParam("groupRId") String groupRId) {
		groupComponent.removeGroup(groupRId);
		return Response.ok().build();
	}

	/**
	 * Removes a user from a group
	 * @param groupRId the group to add the user too
	 * @param userRId the user to be added too the group
	 * @return a {@link com.collibra.dgc.rest.core.v1_0.dto.Group}
	 */
	@DELETE
	@Path("/user")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response removeUserFromGroup(@QueryParam("groupRId") String groupRId, @QueryParam("userRId") String userRId) {
		Group g = groupComponent.removeUserFromGroup(groupRId, userRId);
		return Response.ok().entity(builder.buildGroup(g)).build();
	}

}
