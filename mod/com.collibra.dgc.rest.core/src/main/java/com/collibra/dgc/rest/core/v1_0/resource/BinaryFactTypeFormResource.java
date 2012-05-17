package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForm;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForms;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;

/**
 * BinaryFactTypeForm resource for the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/binary_fact_type_form")
public class BinaryFactTypeFormResource extends RepresentationResource {

	@Autowired
	private BinaryFactTypeFormComponent binaryFactTypeFormComponent;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Binary Fact Type Form documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException(
				"The documentation of the binary fact type form REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Create a new {@link BinaryFactTypeForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 *            (required)
	 * @param headSignifier The signifier for the head {@link Term} of the {@link BinaryFactTypeForm} (required if
	 *            {@code headTermRId} is not set)
	 * @param headTermRId Head {@link Term} resource id (required if {@code headSignifier} is not set)
	 * @param role The name of the role of the {@link BinaryFactTypeForm} (required)
	 * @param coRole The name of the coRole of the {@link BinaryFactTypeForm} (required)
	 * @param tailSignifier The signifier for the tail {@link Term} of the {@link BinaryFactTypeForm} (required if
	 *            {@code tailTermRId} is not set)
	 * @param tailTermRId Tail {@link Term} resource id (required if {@code tailSignifier} is not set)
	 * @return The newly persisted {@link BinaryFactTypeFormReference}
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReference.class)
	@Transactional
	public Response addBinaryFactTypeForm(@FormParam("vocabularyRId") String vocabularyRId,
			@FormParam("headSignifier") String headSignifier, @FormParam("headTermRId") String headTermRId,
			@FormParam("role") String role, @FormParam("coRole") String coRole,
			@FormParam("tailSignifier") String tailSignifier, @FormParam("tailTermRId") String tailTermRId) {

		final BinaryFactTypeFormReference bftfr;

		if (headTermRId != null && !headTermRId.isEmpty()) {

			if (tailTermRId != null && !tailTermRId.isEmpty())
				bftfr = builder.buildBinaryFactTypeFormReference(binaryFactTypeFormComponent
						.addBinaryFactTypeFormOnExistingTerms(vocabularyRId, headTermRId, role, coRole, tailTermRId));
			else
				bftfr = builder.buildBinaryFactTypeFormReference(binaryFactTypeFormComponent
						.addBinaryFactTypeFormOnExistingHeadTerm(vocabularyRId, headTermRId, role, coRole,
								tailSignifier));

		} else if (tailTermRId != null && !tailTermRId.isEmpty())
			bftfr = builder.buildBinaryFactTypeFormReference(binaryFactTypeFormComponent
					.addBinaryFactTypeFormOnExistingTailTerm(vocabularyRId, headSignifier, role, coRole, tailTermRId));

		else
			bftfr = builder.buildBinaryFactTypeFormReference(binaryFactTypeFormComponent.addBinaryFactTypeForm(
					vocabularyRId, headSignifier, role, coRole, tailSignifier));

		// TODO throw an other exception like creation error ?
		if (bftfr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(bftfr).build();
	}

	/* READ */

	/**
	 * Get the {@link BinaryFactTypeForm} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link BinaryFactTypeForm}
	 * @return The {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeForm.class)
	@Transactional
	public Response getBinaryFactTypeForm(@PathParam("rId") String rId) {

		final BinaryFactTypeForm bftf = builder.buildBinaryFactTypeForm(binaryFactTypeFormComponent
				.getBinaryFactTypeForm(rId));

		return Response.ok(bftf).build();
	}

	/**
	 * Get the binary fact type forms that contain the given {@link Term} that can be the head term ({@code headOrTail}
	 * = 'head'), the tail term ({@code headOrTail} = 'tail') or any of the two ({@code headOrTail} not set).
	 * 
	 * @param termRId The resource id of the {@link Term}
	 * @param headOrTail Set if the {@link Term} correspond to the head {@link Term} ({@code headOrTail} = 'head') or
	 *            the tail term {@link Term} ({@code headOrTail} = 'tail') or any of the two ({@code headOrTail} not
	 *            set)
	 * @return {@link BinaryFactTypeFormReferences}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getBinaryFactTypeFormReferencesContainingTerm(@QueryParam("termRId") String termRId,
			@QueryParam("headOrTail") String headOrTail) {

		final BinaryFactTypeFormReferences bftfrs;

		if (headOrTail != null && headOrTail.equalsIgnoreCase("head"))
			bftfrs = builder.buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingHeadTerm(termRId));

		else if (headOrTail != null && headOrTail.equalsIgnoreCase("tail"))
			bftfrs = builder.buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingTailTerm(termRId));

		else
			bftfrs = builder.buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingTerm(termRId));

		if (bftfrs == null || bftfrs.getBinaryFactTypeFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrs).build();
	}

	/**
	 * Get the binary fact type forms that contain the given {@link Term} that can be the head term ({@code headOrTail}
	 * = 'head'), the tail term ({@code headOrTail} = 'tail') or any of the two ({@code headOrTail} not set).
	 * 
	 * @param termRId The resource id of the {@link Term}
	 * @param headOrTail Set if the {@link Term} correspond to the head {@link Term} ({@code headOrTail} = 'head') or
	 *            the tail term {@link Term} ({@code headOrTail} = 'tail') or any of the two ({@code headOrTail} not
	 *            set)
	 * @return {@link BinaryFactTypeForms}
	 */
	@GET
	@Path("/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeForms.class)
	@Transactional
	public Response getBinaryFactTypeFormsContainingTerm(@QueryParam("termRId") String termRId,
			@QueryParam("headOrTail") String headOrTail) {

		final BinaryFactTypeForms bftfs;

		if (headOrTail != null && headOrTail.equalsIgnoreCase("head"))
			bftfs = builder.buildBinaryFactTypeForms(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingHeadTerm(termRId));

		else if (headOrTail != null && headOrTail.equalsIgnoreCase("tail"))
			bftfs = builder.buildBinaryFactTypeForms(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingTailTerm(termRId));

		else
			bftfs = builder.buildBinaryFactTypeForms(binaryFactTypeFormComponent
					.getBinaryFactTypeFormsContainingTerm(termRId));

		if (bftfs == null || bftfs.getBinaryFactTypeForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfs).build();
	}

	/**
	 * Get the preferred {@link BinaryFactTypeForm} of the general {@link Concept} for the {@link Concept} of a
	 * specified {@link BinaryFactTypeForm} from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link BinaryFactTypeForm} for which you want to get the general
	 *            {@link Concept} of its {@link Concept} as a {@link BinaryFactTypeForm}
	 * @return The {@link BinaryFactTypeForm} {@link Representation} of the general {@link Concept}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/general_concept")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReference.class)
	@Transactional
	public Response getGeneralConcept(@PathParam("rId") String rId) {

		final BinaryFactTypeFormReference bftfr = builder
				.buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) binaryFactTypeFormComponent
						.getGeneralConcept(rId));

		return Response.ok(bftfr).build();
	}

	/**
	 * Get specialized {@link Concept}'s {@link BinaryFactTypeForm}s for the specified {@link BinaryFactTypeForm}'s
	 * {@link Concept}.
	 * 
	 * @param rId The {@link BinaryFactTypeForm} resource id
	 * @param limit Maximum number of results
	 * @return {@link BinaryFactTypeFormReferences}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/specialized_concepts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getSpecializedConcepts(@PathParam("rId") String rId,
			@DefaultValue("10") @QueryParam("limit") int limit) {

		BinaryFactTypeFormReferences bftfrs = factory.createBinaryFactTypeFormReferences();
		List<BinaryFactTypeFormReference> bftfrList = bftfrs.getBinaryFactTypeFormReferences();

		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = binaryFactTypeFormComponent
				.getSpecializedConcepts(rId, limit);

		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			final BinaryFactTypeFormReference bftfr = builder
					.buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) specializedConcept);
			bftfrList.add(bftfr);
		}

		if (bftfrList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrList).build();
	}

	/**
	 * Get synonyms of the {@link Concept} represented by the specified {@link BinaryFactTypeForm}.
	 * 
	 * @param rId The {@link BinaryFactTypeForm} resource id
	 * @return {@link BinaryFactTypeFormReferences}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/synonyms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getSynonyms(@PathParam("rId") String rId) {

		BinaryFactTypeFormReferences bftfrs = factory.createBinaryFactTypeFormReferences();
		List<BinaryFactTypeFormReference> bftfrList = bftfrs.getBinaryFactTypeFormReferences();

		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = binaryFactTypeFormComponent
				.getSynonyms(rId);

		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			final BinaryFactTypeFormReference bftfr = builder
					.buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) synonym);
			bftfrList.add(bftfr);
		}

		if (bftfrList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrList).build();
	}

	/* UPDATE */

	/**
	 * Update the specified {@link BinaryFactTypeForm}.
	 * 
	 * @param rId The {@link BinaryFactTypeForm} resource id
	 * @param headSignifier The new head {@link Term} signifier
	 * @param role The new role
	 * @param coRole The new coRole
	 * @param tailSignifier The new tail {@link Term} signifier.
	 * @return The updated {@link BinaryFactTypeFormReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReference.class)
	@Transactional
	public Response changeBinaryFactTypeForm(@PathParam("rId") String rId,
			@FormParam("headSignifier") String headSignifier, @FormParam("role") String role,
			@FormParam("coRole") String coRole, @FormParam("tailSignifier") String tailSignifier) {

		final BinaryFactTypeFormReference bftfr = builder.buildBinaryFactTypeFormReference(binaryFactTypeFormComponent
				.changeBinaryFactTypeForm(rId, headSignifier, role, coRole, tailSignifier));

		return Response.ok(bftfr).build();
	}
}
