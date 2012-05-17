package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.List;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReferences;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeTypes;
import com.collibra.dgc.rest.core.v1_0.dto.ObjectFactory;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerm;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerms;
import com.collibra.dgc.rest.core.v1_0.dto.TermReference;
import com.collibra.dgc.rest.core.v1_0.dto.TermReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;

/**
 * Private REST service for the management of {@link Representation}.
 * @author pmalarme
 * 
 */
@Component
public class RepresentationResource {

	protected final ObjectFactory factory = new ObjectFactory();

	@Autowired
	protected AttributeComponent attributeComponent;

	@Autowired
	@Qualifier("RepresentationComponentImpl")
	protected RepresentationComponent representationComponent;

	@Autowired
	protected RestModelBuilder builder;

	/* CREATE */

	// Attribute

	/**
	 * 
	 * Add an {@link Attribute} of a certain kind defined by it's {@link Attribute} {@code Type} {@code Label}.
	 * 
	 * @param representationRId The resource id of the owner {@link Representation}
	 * @param lableRId The resource id of the {@link Attribute} {@code} {@code Label} {@link Term}
	 * @param value The {@link List} of the {@link String} representation(s) of the value(s). For certain kind of
	 *            attributes, the value is unique or null
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/attributes")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addAttribute(@PathParam("rId") String rId, @FormParam("labelRId") String labelRId,
			@FormParam("value") List<String> values) {

		final AttributeReference ar = builder.buildAttributeReference(attributeComponent.addAttribute(rId, labelRId,
				values));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/**
	 * Create a new definition {@link StringAttribute} and persist it.
	 * 
	 * @param rId The resource id of the {@link Representation} for which the definition will be added (required)
	 * @param definitionLongExpresseion The definition long expression ({@link String}) for the {@link Representation}
	 *            (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/definitions")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addDefinition(@PathParam("rId") String rId,
			@FormParam("defintionLongExpression") String definitionLongExpression) {

		final AttributeReference ar = builder.buildStringAttributeReference(attributeComponent.addDefinition(rId,
				definitionLongExpression));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/**
	 * Create a new description {@link StringAttribute} and persist it.
	 * 
	 * @param rId The resource id of the {@link Representation} for which the description will be added (required)
	 * @param descriptionLongExpression The description long expression ({@link String}) for the {@link Representation}
	 *            (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/descriptions")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addDescription(@PathParam("rId") String rId,
			@FormParam("defintionLongExpression") String descriptionLongExpression) {

		final AttributeReference ar = builder.buildStringAttributeReference(attributeComponent.addDescription(rId,
				descriptionLongExpression));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/**
	 * Create a new example {@link StringAttribute} and persist it.
	 * 
	 * @param rId The resource id of the {@link Representation} for which the example will be added (required)
	 * @param exampleLongExpression The example long expression ({@link String}) for the {@link Representation}
	 *            (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/examples")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addExample(@PathParam("rId") String rId,
			@FormParam("defintionLongExpression") String exampleLongExpression) {

		final AttributeReference ar = builder.buildStringAttributeReference(attributeComponent.addExample(rId,
				exampleLongExpression));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/**
	 * Create a new note {@link StringAttribute} and persist it.
	 * 
	 * @param rId The resource id of the {@link Representation} for which the example will be added (required)
	 * @param noteLongExpression The note long expression ({@link String}) for the {@link Representation} (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/notes")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addNote(@PathParam("rId") String rId,
			@FormParam("defintionLongExpression") String noteLongExpression) {

		final AttributeReference ar = builder.buildStringAttributeReference(attributeComponent.addNote(rId,
				noteLongExpression));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/* READ */

	// Representation

	/**
	 * Get the {@code Status} {@link Term} of the specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation} (required)
	 * @return The {@code Status} {@link TermReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response getStatus(@PathParam("rId") String rId) {

		final TermReference tr = builder.buildTermReference(representationComponent.getRepresentation(rId).getStatus());

		return Response.ok(tr).build();
	}

	/**
	 * Get the {@link Vocabulary} containing the specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation} (required)
	 * @return The {@link Representation} {@link Vocabulary}
	 */
	@GET
	@Path("/{rId}/vocabulary")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response getVocabulary(@PathParam("rId") String rId) {

		Vocabulary v = builder.buildVocabulary(representationComponent.getRepresentation(rId).getVocabulary());

		return Response.ok(v).build();
	}

	/**
	 * Get the resource id of the {@link Concept} of the {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation} (required)
	 * @return The resource id of the {@link Concept} of the {@link Representation} as a {@link String}
	 */
	@GET
	@Path("/{rId}/conceptRId")
	@Produces({ MediaType.TEXT_PLAIN })
	@Transactional
	public String getConceptRId(@PathParam("rId") String rId) {

		return representationComponent.getRepresentation(rId).getMeaning().getId().toString();
	}

	/**
	 * Get the preferred {@link Term} that represents the type of the {@link Concept} (i.e. ConceptType) of a
	 * {@link Representation} from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the representation for which you want to get the concept type as a {@link Term}
	 *            (required)
	 * @return The {@link SimpleTerm} that represents the concept type.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/concept_type")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerm.class)
	@Transactional
	public Response getConceptType(@PathParam("rId") String rId) {

		final SimpleTerm st = builder.buildSimpleTerm(representationComponent.getConceptType(rId));

		return Response.ok(st).build();
	}

	// Attributes

	/**
	 * Get the {@link Attribute} of a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation} (required)
	 * @param labelRIds The resource id of the {@link AttributeType} for wich {@link Attribute} must be retrieved
	 *            (optional)
	 * @return {@link AttributeReferences}
	 */
	@GET
	@Path("/{rId}/attributes")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReferences.class)
	@Transactional
	public Response getAttributeReferences(@PathParam("rId") String rId, @QueryParam("labelRId") List<String> labelRIds) {

		final AttributeReferences ars;

		// Get all the attributes
		if (labelRIds == null || labelRIds.isEmpty()) {

			ars = builder.buildAttributeReferences(representationComponent.getRepresentation(rId).getAttributes());

			// Get all the attributes of a certain type
		} else if (labelRIds.size() == 1) {

			ars = builder.buildAttributeReferences(attributeComponent.getAttributesOfTypeForRepresentation(rId,
					labelRIds.get(0)));

			// Get all the attributes corresponding to the given types
		} else {

			ars = builder.buildAttributeReferences(attributeComponent
					.getAttributesOfTypesForRepresentationInCollection(rId, labelRIds.toArray(new String[0])));
		}

		if (ars == null || ars.getAttributeReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ars).build();
	}

	/**
	 * Get the definitions of a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link AttributeReferences}
	 */
	@GET
	@Path("/{rId}/definitions")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReferences.class)
	@Transactional
	public Response getDefinitions(@PathParam("rId") String rId) {

		final AttributeReferences ars = builder.buildStringAttributeReferences(attributeComponent
				.getDefinitionsForRepresentation(rId));

		if (ars == null || ars.getAttributeReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ars).build();
	}

	/**
	 * Get the descriptions of a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link AttributeReferences}
	 */
	@GET
	@Path("/{rId}/descriptions")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReferences.class)
	@Transactional
	public Response getDescriptions(@PathParam("rId") String rId) {

		final AttributeReferences ars = builder.buildStringAttributeReferences(attributeComponent
				.getDescriptionsForRepresentation(rId));

		if (ars == null || ars.getAttributeReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ars).build();
	}

	/**
	 * Get the examples of a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link AttributeReferences}
	 */
	@GET
	@Path("/{rId}/examples")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReferences.class)
	@Transactional
	public Response getExamples(@PathParam("rId") String rId) {

		final AttributeReferences ars = builder.buildStringAttributeReferences(attributeComponent
				.getExamplesForRepresentation(rId));

		if (ars == null || ars.getAttributeReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ars).build();
	}

	/**
	 * Get the notes of a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link AttributeReferences}
	 */
	@GET
	@Path("/{rId}/notes")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReferences.class)
	@Transactional
	public Response getNotes(@PathParam("rId") String rId) {

		final AttributeReferences ars = builder.buildStringAttributeReferences(attributeComponent
				.getNotesForRepresentation(rId));

		if (ars == null || ars.getAttributeReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ars).build();
	}

	// Suggestion

	/**
	 * Find all possible {@code Status} {@link Term} for a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link TermReferences}
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/possible_statuses")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReferences.class)
	@Transactional
	public Response findPossibleStatuses(@PathParam("rId") String rId) {

		final TermReferences trs = builder.buildTermReferences(representationComponent.findPossibleStatuses(rId));

		if (trs == null || trs.getTermReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(trs).build();
	}

	/**
	 * Find all possible {@link AttributeType} represented as {@link Term} for a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link AttributeTypes}
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/possible_attribute_types")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeTypes.class)
	@Transactional
	public Response findPossibleAttributeTypes(@PathParam("rId") String rId) {

		final AttributeTypes ats = builder.buildAttributeTypes(representationComponent.findPossibleAttributeTypes(rId));

		if (ats == null || ats.getAttributeTypes().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ats).build();
	}

	/**
	 * Find all possible {@link Concept} types represented as {@link Term} for a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link SimpleTerms}
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/possible_concept_types")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerms.class)
	@Transactional
	public Response findPossibleConceptTypes(@PathParam("rId") String rId) {

		final SimpleTerms sts = builder.buildSimpleTerms(representationComponent.findPossibleConceptTypes(rId));

		if (sts == null || sts.getSimpleTerms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(sts).build();
	}

	/* UPDATE */

	// Representation

	/**
	 * Set the {@link Concept} type of a specified {@link Representation} to the {@link Concept} of the conceptType
	 * {@link Term}.
	 * 
	 * @param rId The {@link Representation} resource id for which the type of the {@link Concept} (i.e. ConceptType)
	 *            must to be changed
	 * @param conceptTypeTermRId The resource id of the {@link Term} for which the {@link Concept} will be set as the
	 *            ConceptType of the specified {@link Representation}
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/concept_type")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response changeConceptType(@PathParam("rId") String rId,
			@FormParam("conceptTypeTermRId") String conceptTypeTermRId) {

		final Representation r = builder.buildRepresentation(representationComponent.changeConceptType(rId,
				conceptTypeTermRId));

		return Response.ok(r).build();
	}

	/**
	 * Change the general {@link Concept} from the {@link Concept} of the specialized {@link Representation} to the
	 * {@link Concept} of the general {@link Representation}.
	 * 
	 * @param specializedRepresentationRId The resource id of the specialized {@link Representation} whose general
	 *            {@link Concept} must be changed
	 * @param generalRepresentationRId The general {@link Representation} resource whose {@link Concept} will be set as
	 *            the general {@link Concept} of the specialized {@link Representation}
	 * @return The updated specialized {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{specializedRepresentationRId}/general_concept")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response changeGeneralConcept(
			@PathParam("specializedRepresentationRId") String specializedRepresentationRId,
			@FormParam("generalRepresentationResourceId") String generalRepresentationResourceId) {

		final Representation r = builder.buildRepresentation(representationComponent.changeGeneralConcept(
				specializedRepresentationRId, generalRepresentationResourceId));

		return Response.ok(r).build();
	}

	/**
	 * Change {@link Representation} status.
	 * 
	 * @param rId The {@link Representation} resource id
	 * @param statusTermRId The resource id of the {@link Term} that represents the status
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/status")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response changeStatus(@PathParam("rId") String rId, @FormParam("statusTermRId") String statusTermRId) {

		final Representation r = builder.buildRepresentation(representationComponent.changeStatus(rId, statusTermRId));

		return Response.ok(r).build();
	}

	/**
	 * Set the {@link Concept} of the current {@link Representation} to the {@link Concept} of the selected
	 * {@link Representation} (i.e. set both representations as synonyms for the {@link Concept} of the selected
	 * {@link Representation}).
	 * 
	 * @param currentRepresentationRId The resource id of the current {@link Representation} whose {@link Meaning} (i.e.
	 *            {@link Concept}) is changed to the selected {@link Representation}'s {@link Meaning} (i.e.
	 *            {@link Concept})
	 * @param selectedRepresentationRId The resource id of the selected {@link Representation} that keeps its original
	 *            {@link Meaning} (i.e. {@link Concept})
	 * @return The updated selected {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{selectedRepresentationRId}/synonyms")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response addSynonym(@PathParam("selectedRepresentationRId") String selectedRepresentationRId,
			@FormParam("currentRepresentationRId") String currentRepresentationRId) {

		representationComponent.addSynonym(currentRepresentationRId, selectedRepresentationRId);

		final Representation r = builder.buildRepresentation(representationComponent
				.getRepresentation(selectedRepresentationRId));

		return Response.ok(r).build();
	}

	/* DELETE */

	// Representation

	/**
	 * Remove the general {@link Concept} of the {@link Concept} of the specified {@link Representation} (i.e. set the
	 * general {@link Concept} of the specified {@link Representation} as {@code Thing}).
	 * 
	 * @param rId The resourceId of the {@link Representation} for which the general concept has to be removed
	 * @return The updated {@link Representation}
	 */
	@DELETE
	@Path("/{rId}/general_concept")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response removeGeneralConcept(@PathParam("rId") String rId) {

		final Representation r = builder.buildRepresentation(representationComponent.removeGeneralConcept(rId));

		return Response.ok(r).build();
	}

	/**
	 * Remove the {@link Representation} as a synonym (i.e. change its {@link Concept} to a new {@link Concept}).
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return The updated {@link Representation}
	 */
	@DELETE
	@Path("/{rId}/synonyms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response removeSynonym(@PathParam("rId") String rId) {

		final Representation r = builder.buildRepresentation(representationComponent.removeSynonym(rId));

		return Response.ok(r).build();
	}

	/**
	 * Remove the specified {@link Representation}
	 * 
	 * @param rId The {@link Representation} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@DELETE
	@Path("/{rId}")
	@Transactional
	public Response remove(@PathParam("rId") String rId) {

		representationComponent.remove(rId);

		return Response.ok().build();
	}
}
