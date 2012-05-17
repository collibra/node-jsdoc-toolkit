package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Attribute;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReference;

/**
 * Attribute resource of the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/attribute")
public class AttributeResource {

	@Autowired
	private AttributeComponent attributeComponent;

	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Attribute documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the attribute REST service is not implemented yet.");
	}

	/* READ */

	/**
	 * Get the {@link Attribute} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Attribute}
	 * @return The {@link Attribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Attribute.class)
	@Transactional
	public Response getAttribute(@PathParam("rId") String rId) {

		final Attribute a = builder.buildAttribute(attributeComponent.getAttribute(rId));

		return Response.ok(a).build();
	}

	/* UPDATE */

	/**
	 * Update an {@link Attribute} of a certain kind defined by it's {@link Attribute} {@code Type} {@code Label}.
	 * 
	 * @param rId The resource id of the {@link Attribute}
	 * @param value The {@link Collection} of the {@link String} representation(s) of the new value(s). For certain kind
	 *            of attributes, the new value is unique or null
	 * @return The updated {@link AttributeReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response changeAttribute(@PathParam("rId") String rId, @FormParam("value") List<String> values) {

		final AttributeReference ar = builder.buildAttributeReference(attributeComponent.changeAttribute(rId, values));

		return Response.ok(ar).build();
	}

	/**
	 * Check if the {@link Attribute} is in violation with any value constraint.
	 * 
	 * @param rId Resource id of the {@link Attribute} to be checked
	 * @return {@code true} of if value constraint is satisfied, {@code false} otherwise
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/validate_constraint")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public boolean validateMultiValueListConstraint(@FormParam("rId") String rId) {
		return attributeComponent.validateConstraint(rId);
	}

	/* DELETE */

	/**
	 * Remove the specified {@link Attribute}
	 * 
	 * @param rId The {@link Attribute} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@DELETE
	@Path("/{rId}")
	@Transactional
	public Response removeAttribute(@PathParam("rId") String rId) {

		attributeComponent.removeAttribute(rId);

		return Response.ok().build();
	}
}
