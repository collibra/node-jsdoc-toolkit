package com.collibra.dgc.rest.core.v1_0.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.RightsComponent;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;

/**
 * Public Webservice API to manage the membership's between {@link Role}'s and {@link User} or {@link Group}
 * @author GKDAI63
 * 
 */
@Component
@Path("/1.0/member")
public class MemberResource {
	@Autowired
	private RestModelBuilder builder;
	@Autowired
	private RightsComponent rightsComponent;

	// CREATE

	/**
	 * Create's a new membership to a role
	 * @param ownerId the entity becoming the member (a {@link User} or {@link Group})
	 * @param roleName the role the entity is becoming a member off.
	 * @param resourceRId optional resource which the member will be able to manage
	 * @return
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addMember(@FormParam("ownerId") String ownerId, @FormParam("roleName") String roleName,
			@FormParam("resourceRId") String resourceRId) {
		Member m = null;
		if (resourceRId == null || resourceRId.isEmpty()) {
			m = rightsComponent.addMember(ownerId, roleName);
		} else {
			m = rightsComponent.addMember(ownerId, roleName, resourceRId);
		}
		return Response.status(Status.CREATED).entity(builder.buildMemberReference(m)).build();
	}

	// DELETE

	/**
	 * Removes the membership
	 * @param ownerId the entity becoming the member (a {@link User} or {@link Group})
	 * @param roleName roleName the role the entity is being removed from.
	 * @param resourceRId optional resource which the member will not be able to manage anymore.
	 * @return
	 */
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@DELETE
	@Transactional
	public Response removeMember(@QueryParam("ownerId") String ownerId, @QueryParam("roleName") String roleName,
			@QueryParam("resourceRId") String resourceRId) {
		Member m = null;
		if (resourceRId == null || resourceRId.isEmpty()) {
			m = rightsComponent.removeMember(ownerId, roleName);
		} else {
			m = rightsComponent.removeMember(ownerId, roleName, resourceRId);
		}
		return Response.ok().entity(builder.buildMemberReference(m)).build();
	}

}
