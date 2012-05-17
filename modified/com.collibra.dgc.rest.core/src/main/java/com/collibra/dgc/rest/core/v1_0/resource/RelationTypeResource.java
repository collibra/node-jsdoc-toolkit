/**
 * 
 */
package com.collibra.dgc.rest.core.v1_0.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.relation.RelationTypeComponent;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForm;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReference;

/**
 * @author fvdmaele
 *
 */
@Component
@Path("/1.0/relation_type")
public class RelationTypeResource {

	@Autowired
	private RelationTypeComponent relationTypeComponent;
	
	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Attribute Type documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException(
				"The documentation of the relation type REST service is not implemented yet.");
	}

	/* CREATE */

	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReference.class)
	@Transactional
	public Response addAttributeType(@FormParam("sourceSignifier") String sourceSignifier,
			@FormParam("role") String role, @FormParam("corole") String coRole,
			@FormParam("targetSignifier") String targetSignifier) {

		BinaryFactTypeFormReference bftf = null;
		
		bftf = builder.buildBinaryFactTypeFormReference(relationTypeComponent.addRelationType(sourceSignifier, role, coRole, targetSignifier));

		// TODO throw an other exception like creation error ?
		if (bftf == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(bftf).build();
	}
}
