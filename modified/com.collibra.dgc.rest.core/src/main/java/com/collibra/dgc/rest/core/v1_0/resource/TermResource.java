package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.collibra.dgc.core.component.relation.RelationComponent;
import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeType;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForm;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForms;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicForms;
import com.collibra.dgc.rest.core.v1_0.dto.Relation;
import com.collibra.dgc.rest.core.v1_0.dto.RelationReference;
import com.collibra.dgc.rest.core.v1_0.dto.Relations;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerm;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerms;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.TermReference;
import com.collibra.dgc.rest.core.v1_0.dto.Terms;

/**
 * Term resource for the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/term")
public class TermResource extends RepresentationResource {

	@Autowired
	private BinaryFactTypeFormComponent binaryFactTypeFormComponent;

	@Autowired
	private CharacteristicFormComponent characteristicFormComponent;

	@Autowired
	private TermComponent termComponent;

	@Autowired
	private RelationComponent relationComponent;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Term documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the term REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Create a new {@link Term} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that will contain the {@link Term} (required)
	 * @param signifier The signifier of the {@link Term} (required)
	 * @param objectTypeRId The resource id of the {@link Object Type} that will be the {@link Concept} {@code Type} of
	 *            this {@link Term} (optional)
	 * @return The newly persisted {@link TermReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response addTerm(@FormParam("vocabularyRId") String vocabularyRId, @FormParam("signifier") String signifier,
			@FormParam("objectTypeRId") String objectTypeRId) {

		final TermReference tr;

		if (objectTypeRId == null || objectTypeRId.isEmpty())
			tr = builder.buildTermReference(termComponent.addTerm(vocabularyRId, signifier));
		else
			tr = builder.buildTermReference(termComponent.addTerm(vocabularyRId, signifier, objectTypeRId));

		// TODO throw an other exception like creation error ?
		if (tr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(tr).build();
	}

	/**
	 * Add a new {@link StringAttribute} and persist it.
	 * 
	 * @param rId The resource id of the {@link Representation} that the {@link StringAttribute} is for (required)
	 * @param labelRId The resource id of the {@link AttributeType} {@code Label} {@link Term} (required)
	 * @param longExpression The long expression for the {@link StringAttribute} (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}/attributes/StringAttribute")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeReference.class)
	@Transactional
	public Response addStringAttribute(@PathParam("rId") String rId, @FormParam("labelRId") String labelRId,
			@FormParam("longExpression") String longExpression) {

		final AttributeReference ar = builder.buildStringAttributeReference(attributeComponent.addStringAttribute(rId,
				labelRId, longExpression));

		// TODO throw an other exception like creation error ?
		if (ar == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(ar).build();
	}

	/**
	 * Add a new {@link Relation} and persist it.
	 * 
	 * @param rId The resource id of the {@link Term} that is the source of the {@link Relation} (required)
	 * @param typeRId The resource id of the {@code Relation Type} {@link BinaryFactTypeForm} (required)
	 * @param targetRId The resource id for the {@link Term} that is the target of the {@link Relation} (optional)
	 * @param targetSignifier The signifier for the new {@link Term} that will become the target of the {@link Relation}
	 *            (optional)
	 * @param targetVocabularyRId The resource id of the {@link Vocabulary} in which the new {@link Term} that will
	 *            become the target of the {@link Relation} (optional)
	 * @return The newly persisted {@link AttributeReference} with HTTP 201 (created)
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and {@link DGCErrorCodes#BFTF_NOT_FOUND}.
	 */
	@POST
	@Path("/{rId}/relations")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(RelationReference.class)
	@Transactional
	public Response addRelation(@PathParam("rId") String rId, @FormParam("typeRId") String typeRId,
			@FormParam("targetRId") String targetRId, @FormParam("targetSignifier") String targetSignifier,
			@FormParam("targetVocabularyRId") String targetVocabularyRId) {

		final RelationReference rr;

		if (targetRId != null)
			rr = builder.buildRelationReference(relationComponent.addRelation(rId, typeRId, targetRId));
		else
			rr = builder.buildRelationReference(relationComponent.addRelation(rId, typeRId, targetSignifier,
					targetVocabularyRId));

		// TODO throw an other exception like creation error ?
		if (rr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(rr).build();
	}

	/* READ */

	/**
	 * Get the {@link Term} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return The {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Term.class)
	@Transactional
	public Response getTerm(@PathParam("rId") String rId) {

		final Term t = builder.buildTerm(termComponent.getTerm(rId));

		return Response.ok(t).build();
	}

	/**
	 * Get the {@link Term} of a specified {@link Vocabulary} (and its incorporated vocabularies if it is specified)
	 * with the given {@code signifier}.
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link Term} (or one of its
	 *            incorporated vocabularies)
	 * @param signfifer The signifier of the {@link Term}
	 * @param includeIncorporatedVocabulary Set if the term must be found in the specified {@link Vocabulary} only (
	 *            {@code false}) or in the specified {@link Vocabulary} and all its incorporated {@link Vocabulary} (
	 *            {@code true})
	 * @return The {@link Term}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Term.class)
	@Transactional
	public Response getTermBySignifier(@QueryParam("vocabularyRId") String vocabularyRId,
			@QueryParam("signifier") String signifier,
			@QueryParam("includeIncorporatedVocabulary") boolean includeIncorporatedVocabulary) {

		final Term t;

		if (includeIncorporatedVocabulary)
			t = builder.buildTerm(termComponent.getTermBySignifierInIncorporatedVocabularies(vocabularyRId, signifier));
		else
			t = builder.buildTerm(termComponent.getTermBySignifier(vocabularyRId, signifier));

		return Response.ok(t).build();
	}

	/**
	 * Get the {@link BinaryFactTypeForm} that contain the given {@link Term}.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return {@link BinaryFactTypeFormReferences}
	 */
	@GET
	@Path("/{rId}/binary_fact_type_forms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getBinaryFactTypeFormReferences(@PathParam("rId") String rId) {

		final BinaryFactTypeFormReferences bftfrs = builder
				.buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent
						.getBinaryFactTypeFormsContainingTerm(rId));

		if (bftfrs == null || bftfrs.getBinaryFactTypeFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrs).build();
	}

	/**
	 * Get the {@link BinaryFactTypeForm} that contain the given {@link Term}.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return {@link BinaryFactTypeForms}
	 */
	@GET
	@Path("/{rId}/binary_fact_type_forms/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeForms.class)
	@Transactional
	public Response getBinaryFactTypeForms(@PathParam("rId") String rId) {

		final BinaryFactTypeForms bftfs = builder.buildBinaryFactTypeForms(binaryFactTypeFormComponent
				.getBinaryFactTypeFormsContainingTerm(rId));

		if (bftfs == null || bftfs.getBinaryFactTypeForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfs).build();
	}

	/**
	 * Get all the derived binary fact type forms involving the given {@link Term}.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return {@link BinaryFactTypeFormReferences}
	 */
	@GET
	@Path("/{rId}/derived_facts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getDerivedFactReferences(@PathParam("rId") String rId) {

		final BinaryFactTypeFormReferences bftfrs = builder
				.buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent.getDerivedFacts(rId));

		if (bftfrs == null || bftfrs.getBinaryFactTypeFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrs).build();
	}

	/**
	 * Get all the derived binary fact type forms involving the given {@link Term}.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return {@link BinaryFactTypeForms}
	 */
	@GET
	@Path("/{rId}/derived_facts/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeForms.class)
	@Transactional
	public Response getDerivedFacts(@PathParam("rId") String rId) {

		final BinaryFactTypeForms bftfs = builder.buildBinaryFactTypeForms(binaryFactTypeFormComponent
				.getDerivedFacts(rId));

		if (bftfs == null || bftfs.getBinaryFactTypeForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfs).build();
	}

	/**
	 * Get all the relations involving the given as source {@link Term}.
	 * 
	 * @param rId The resource id of the source {@link Term}
	 * @param rId The resource id of the {@code Relation Type} {@link BinaryFactTypeForm} (optional)
	 * @return {@link Relations}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/source_relations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Relations.class)
	@Transactional
	public Response getSourceRelations(@PathParam("rId") String rId, @QueryParam("typeRId") String typeRId) {

		final Relations relations;

		if (typeRId == null)
			relations = builder.buildRelations(relationComponent.findRelationsBySource(rId));
		else
			relations = builder.buildRelations(relationComponent.findRelationsBySourceAndType(typeRId, rId));

		if (relations == null || relations.getRelations().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(relations).build();
	}

	/**
	 * Get all the relations involving the given as target {@link Term}.
	 * 
	 * @param rId The resource id of the target {@link Term}
	 * @param rId The resource id of the {@code Relation Type} {@link BinaryFactTypeForm} (optional)
	 * @return {@link Relations}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/target_relations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Relations.class)
	@Transactional
	public Response getTargetRelations(@PathParam("rId") String rId, @QueryParam("typeRId") String typeRId) {

		final Relations relations;

		if (typeRId == null)
			relations = builder.buildRelations(relationComponent.findRelationsByTarget(rId));
		else
			relations = builder.buildRelations(relationComponent.findRelationsByTargetAndType(typeRId, rId));

		if (relations == null || relations.getRelations().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(relations).build();
	}

	/**
	 * Get all the characteristic forms involving the given {@link Term}.
	 * 
	 * @param termRId the {@link Term} resource id
	 * @return {@link CharacteristicFormReferences}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/characteristic_form")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReferences.class)
	@Transactional
	public Response getCharacteristicFormReferences(@PathParam("rId") String rId) {

		final CharacteristicFormReferences cfrs = builder.buildCharacteristicFormReferences(characteristicFormComponent
				.getCharacteristicFormsContainingTerm(rId));

		if (cfrs == null || cfrs.getCharacteristicFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfrs).build();
	}

	/**
	 * Get all the characteristic forms involving the given {@link Term}.
	 * 
	 * @param termRId the {@link Term} resource id
	 * @return {@link CharacteristicForms}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/characteristic_form/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicForms.class)
	@Transactional
	public Response getCharacteristicForms(@PathParam("rId") String rId) {

		final CharacteristicForms cfrs = builder.buildCharacteristicForms(characteristicFormComponent
				.getCharacteristicFormsContainingTerm(rId));

		if (cfrs == null || cfrs.getCharacteristicForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfrs).build();
	}

	/**
	 * Get the preferred {@link Term} of the general {@link Concept} for the {@link Concept} of a specified {@link Term}
	 * from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Term} for which you want to get the general {@link Concept} of its
	 *            {@link Concept} as a {@link SimpleTerm}
	 * @return The {@link SimpleTerm} {@link Representation} of the general {@link Concept}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/general_concept")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerm.class)
	@Transactional
	public Response getGeneralConcept(@PathParam("rId") String rId) {

		final SimpleTerm st = builder.buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) termComponent
				.getGeneralConcept(rId));

		return Response.ok(st).build();
	}

	/**
	 * Get specialized {@link Concept}'s {@link Term}s for the specified {@link Term}'s {@link Concept}.
	 * 
	 * @param rId The {@link Term} resource id
	 * @param limit Maximum number of results
	 * @return {@link SimpleTerms}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/specialized_concepts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerms.class)
	@Transactional
	public Response getSpecializedConcepts(@PathParam("rId") String rId,
			@DefaultValue("10") @QueryParam("limit") int limit) {

		SimpleTerms sts = factory.createSimpleTerms();
		List<SimpleTerm> stList = sts.getSimpleTerms();

		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = termComponent
				.getSpecializedConcepts(rId, limit);

		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			final SimpleTerm st = builder
					.buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) specializedConcept);
			stList.add(st);
		}

		if (stList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(sts).build();
	}

	/**
	 * Get synonyms of the {@link Concept} represented by the specified {@link Term}.
	 * 
	 * @param rId The {@link Term} resource id
	 * @return {@link SimpleTerms}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/synonyms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerms.class)
	@Transactional
	public Response getSynonyms(@PathParam("rId") String rId) {

		SimpleTerms sts = factory.createSimpleTerms();
		List<SimpleTerm> stList = sts.getSimpleTerms();

		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = termComponent
				.getSynonyms(rId);

		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			final SimpleTerm st = builder.buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) synonym);
			stList.add(st);
		}

		if (stList.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(sts).build();
	}

	/**
	 * Retrieve the {@link Terms} which match the specified signifier.
	 * 
	 * @param searchSignifier The expression to search in term's signifier
	 * @param offset The position of the first result to return ({@code 0} based), to allow, together with
	 *            {@code number}, pagination
	 * @param number The maximum number of terms to retrieve. If it is set to <code>0</code> then all results will be
	 *            returned
	 * @return {@link SimpleTerms}
	 */
	@GET
	@Path("/find")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SimpleTerms.class)
	@Transactional
	public Response findSimpleTermsContainingSignifier(@QueryParam("searchSignifier") String searchSignifier,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final SimpleTerms sts = builder.buildSimpleTerms(termComponent.findTermsContainingSignifier(searchSignifier,
				offset, number));

		if (sts == null || sts.getSimpleTerms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(sts).build();
	}

	/**
	 * Retrieve the {@link Terms} which match the specified signifier.
	 * 
	 * @param searchSignifier The expression to search in term's signifier
	 * @param offset The position of the first result to return ({@code 0} based), to allow, together with
	 *            {@code number}, pagination
	 * @param number The maximum number of terms to retrieve. If it is set to <code>0</code> then all results will be
	 *            returned
	 * @return {@link Terms}
	 */
	@GET
	@Path("/find/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Terms.class)
	@Transactional
	public Response findSTermsContainingSignifier(@QueryParam("searchSignifier") String searchSignifier,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final Terms ts = builder
				.buildTerms(termComponent.findTermsContainingSignifier(searchSignifier, offset, number));

		if (ts == null || ts.getTerms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ts).build();
	}

	/* UPDATE */

	/**
	 * Update the {@link Term} signifier.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @param newSignifier The new signifier
	 * @return The updated {@link TermReference}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReference.class)
	@Transactional
	public Response changeSignifier(@PathParam("rId") String rId, @FormParam("signifier") String signifier) {

		final TermReference tr = builder.buildTermReference(termComponent.changeSignifier(rId, signifier));

		return Response.ok(tr).build();
	}

	/* DELETE */

	/**
	 * Remove a {@link List} of {@link Term} specified by their resource id.
	 * 
	 * @param termRIds The {@link List} of {@link Term} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@DELETE
	@Transactional
	public Response removeTerms(@QueryParam("rId") List<String> rIds) {

		termComponent.removeTerms(rIds);

		return Response.ok().build();
	}
}
