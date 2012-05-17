package com.collibra.dgc.rest.core.v1_0.resource;

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

import com.collibra.dgc.core.component.StatusComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.TermReference;
import com.collibra.dgc.rest.core.v1_0.dto.TermReferences;

/**
 * Status resource of the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/status")
public class StatusResource {

	@Autowired
	private StatusComponent statusComponent;

	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Status documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the status REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Add a {@code Status} to the Collibra <i>Statuses Vocabulary</i> and persist it.
	 * 
	 * @param signifier The signifier of the {@code Label} {@link Term} of the {@code Status}
	 * @return The newly persisted {@code Status} {@code Label} {@link TermReference}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and
	 *             {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response addStatus(@FormParam("signifier") String signifier) {

		final TermReference tr = builder.buildTermReference(statusComponent.addStatus(signifier));

		// TODO throw an other exception like creation error ?
		if (tr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(tr).build();
	}

	/* READ */

	/**
	 * Get the {@code Label} {@link Term} of the {@code Status} for the given resource id.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @return The {@code Status} {@code Label} {@link TermReference}
	 * @throws EntityNotFoundException with error {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response getStatus(@PathParam("rId") String rId) {

		final TermReference tr = builder.buildTermReference(statusComponent.getStatus(rId));

		return Response.ok(tr).build();
	}

	/**
	 * Get the {@code Label} {@link Term} of the {@code Status} for the given signifier.
	 * 
	 * @param signifier The signifier of the {@code Label} {@link Term} of the {@code Status}
	 * @return The {@code Status} {@code Label} {@link TermReference}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response getStatusBySignifier(@PathParam("signifier") String signifier) {

		final TermReference tr = builder.buildTermReference(statusComponent.getStatusBySignifier(signifier));

		return Response.ok(tr).build();
	}

	/**
	 * Get all statuses from the Collibra <i>Statuses Vocabulary</i>.
	 * 
	 * @return {@link TermReferences}
	 */
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReferences.class)
	@Transactional
	public Response getStatuses(@PathParam("signifier") String signifier) {

		final TermReferences trs = builder.buildTermReferences(statusComponent.getStatuses());

		if (trs == null || trs.getTermReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(trs).build();
	}

	/* UPDATE */

	/**
	 * Update the signifier of a {@code Status} {@link Term}.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @param signifier The new signifier
	 * @return The updated {@code Status} {@code Label} {@link TermReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response changeConceptType(@PathParam("rId") String rId, @FormParam("signifier") String signifier) {

		final TermReference tr = builder.buildTermReference(statusComponent.changeSignifier(rId, signifier));

		return Response.ok(tr).build();
	}

	/* DELETE */

	/**
	 * Remove the {@code Status} {@link Term} from the Collibra <i>Statuses Vocabulary</i>.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@DELETE
	@Path("/{rId}")
	@Transactional
	public Response removeStatus(@PathParam("rId") String rId) {

		statusComponent.removeStatus(rId);

		return Response.ok().build();
	}
}
