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

import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicForm;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;

/**
 * CharacteristicForm resource for the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/characteristic_form")
public class CharacteristicFormResource extends RepresentationResource {

	@Autowired
	private CharacteristicFormComponent characteristicFormComponent;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Characteristic Form Documentation documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException(
				"The documentation of the characteristic form REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Create a new {@link CharacteristicForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link CharacteristicForm}
	 * @param termSignifier The signifier of the {@link Term} of the {@link CharacteristicForm}
	 * @param role The name for the role of the {@link CharacteristicForm}
	 * @return The newly persisted {@link CharacteristicFormReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReference.class)
	@Transactional
	public Response addCharacteristicForm(@FormParam("vocabularyRId") String vocabularyRId,
			@FormParam("termSignifier") String termSignifier, @FormParam("role") String role) {

		final CharacteristicFormReference cfr = builder.buildCharacteristicFormReference(characteristicFormComponent
				.addCharacteristicForm(vocabularyRId, termSignifier, role));

		// TODO throw an other exception like creation error ?
		if (cfr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(cfr).build();
	}

	/* READ */

	/**
	 * Get the {@link CharacteristicForm} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link CharacteristicForm}
	 * @return The {@link CharacteristicForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#CF_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicForm.class)
	@Transactional
	public Response getCharacteristicForm(@PathParam("rId") String rId) {

		final CharacteristicForm cf = builder.buildCharacteristicForm(characteristicFormComponent
				.getCharacteristicForm(rId));

		return Response.ok(cf).build();
	}

	/**
	 * Get the preferred {@link CharacteristicForm} of the general {@link Concept} for the {@link Concept} of a
	 * specified {@link CharacteristicForm} from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link CharacteristicForm} for which you want to get the general
	 *            {@link Concept} of its {@link Concept} as a {@link CharacteristicForm}
	 * @return The {@link CharacteristicForm} {@link Representation} of the general {@link Concept}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/general_concept")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReference.class)
	@Transactional
	public Response getGeneralConcept(@PathParam("rId") String rId) {

		final CharacteristicFormReference cfr = builder
				.buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) characteristicFormComponent
						.getGeneralConcept(rId));

		return Response.ok(cfr).build();
	}

	/**
	 * Get specialized {@link Concept}'s {@link CharacteristicForm}s for the specified {@link CharacteristicForm}'s
	 * {@link Concept}.
	 * 
	 * @param rId The {@link CharacteristicForm} resource id
	 * @param limit Maximum number of results
	 * @return {@link CharacteristicFormReferences}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/specialized_concepts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReferences.class)
	@Transactional
	public Response getSpecializedConcepts(@PathParam("rId") String rId,
			@DefaultValue("10") @QueryParam("limit") int limit) {

		CharacteristicFormReferences cfrs = factory.createCharacteristicFormReferences();
		List<CharacteristicFormReference> cfrList = cfrs.getCharacteristicFormReferences();

		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = characteristicFormComponent
				.getSpecializedConcepts(rId, limit);

		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			final CharacteristicFormReference cfr = builder
					.buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) specializedConcept);
			cfrList.add(cfr);
		}

		if (cfrList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfrList).build();
	}

	/**
	 * Get synonyms of the {@link Concept} represented by the specified {@link CharacteristicForm}.
	 * 
	 * @param rId The {@link CharacteristicForm} resource id
	 * @return {@link CharacteristicFormReferences}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/synonyms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReferences.class)
	@Transactional
	public Response getSynonyms(@PathParam("rId") String rId) {

		CharacteristicFormReferences cfrs = factory.createCharacteristicFormReferences();
		List<CharacteristicFormReference> cfrList = cfrs.getCharacteristicFormReferences();

		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = characteristicFormComponent
				.getSynonyms(rId);

		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			final CharacteristicFormReference cfr = builder
					.buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) synonym);
			cfrList.add(cfr);
		}

		if (cfrList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfrList).build();
	}

	/* UPDATE */

	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReference.class)
	@Transactional
	public Response changeCharacteristicForm(@PathParam("rId") String rId,
			@FormParam("termSignifier") String termSignifiert, @FormParam("role") String role) {

		final CharacteristicFormReference cfr = builder.buildCharacteristicFormReference(characteristicFormComponent
				.changeCharacteristicForm(rId, termSignifiert, role));

		return Response.ok(cfr).build();
	}
}
