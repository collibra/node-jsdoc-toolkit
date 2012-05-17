package com.collibra.dgc.rest.core.v1_0.resource;

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

import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForms;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicForms;
import com.collibra.dgc.rest.core.v1_0.dto.Community;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.TermReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Terms;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReference;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;

/**
 * Vocabulary resource for the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/vocabulary")
public class VocabularyResource {

	@Autowired
	private VocabularyComponent vocabularyComponent;

	@Autowired
	private RestModelBuilder builder;

	/**
	 * Redirect to the Vocabulary documentation.
	 * 
	 * @return {@link Response}
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	@Transactional
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the vocabulary REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Create a new vocabulary.
	 * 
	 * @param communityRId The resource id of the {@link Community} of the {@link Vocabulary} (required)
	 * @param name The name of the {@link Vocabulary} (required)
	 * @param uri The URI of the {@link Vocabulary} (required)
	 * @return {@link VocabularyReference} with the HTTP code 201 (created)
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReference.class)
	@Transactional
	public Response addVocabulary(@FormParam("communityRId") String communityRId, @FormParam("name") String name,
			@FormParam("uri") String uri) {

		final VocabularyReference vr = builder.buildVocabularyReference(vocabularyComponent.addVocabulary(communityRId,
				name, uri));

		// TODO throw another exception like creation error (?)
		if (vr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.ok(vr).build();
	}

	/* READ */

	/**
	 * Get a vocabulary by resource id.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} (required)
	 * @return {@link Vocabulary} if the vocabulary is found, HTTP 404 (not found) otherwise
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response getVocabulary(@PathParam("rId") String rId) {

		final Vocabulary v = builder.buildVocabulary(vocabularyComponent.getVocabulary(rId));

		return Response.ok(v).build();
	}

	/**
	 * Get a vocabulary by name or URI. If both URI and name are set HTTP 400 (bad request) is returned.
	 * 
	 * @param name The name of the {@link Vocabulary} (required if URI is not set)
	 * @param uri The URI of the {@link Vocabulary} (required if name is not set)
	 * @return {@link Vocabulary} if the vocabulary is found, HTTP 404 (not found) otherwise
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response getVocabularyByUriOrName(@QueryParam("name") String name, @QueryParam("uri") String uri) {

		if (name != null && !name.isEmpty()) {

			if (uri != null && !uri.isEmpty())
				throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_NAME_AND_URI_SET, "name", "uri");

			final Vocabulary v = builder.buildVocabulary(vocabularyComponent.getVocabularyByName(name));

			return Response.ok(v).build();

		} else if (uri != null && !uri.isEmpty()) {

			final Vocabulary v = builder.buildVocabulary(vocabularyComponent.getVocabularyByUri(uri));

			return Response.ok(v).build();
		}

		throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_NAME_AND_URI_NULL, "name", "uri");
	}

	/**
	 * Get all the vocabularies that are incorporated in the vocabulary reference by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @param excludeSBVR <code>true</code> if SBVR vocabularies must be excluded, <code>false</code> otherwise
	 * @return {@link VocabularyReferences}
	 */
	@GET
	@Path("/{rId}/incorporated_vocabularies")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response getIncorporatedVocabularyReferences(@PathParam("rId") String rId,
			@DefaultValue(value = "false") @QueryParam("excludeSBVR") boolean excludeSBVR) {

		final VocabularyReferences vrs = builder.buildVocabularyReferences(vocabularyComponent
				.getIncorporatedVocabularies(rId, excludeSBVR));

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Get all the vocabularies that are incorporated in the vocabulary reference by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @param excludeSBVR <code>true</code> if SBVR vocabularies must be excluded, <code>false</code> otherwise
	 * @return {@link Vocabularies}
	 */
	@GET
	@Path("/{rId}/incorporated_vocabularies/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response getIncorporatedVocabularies(@PathParam("rId") String rId,
			@DefaultValue(value = "false") @QueryParam("excludeSBVR") boolean excludeSBVR) {

		final Vocabularies vs = builder.buildVocabularies(vocabularyComponent.getIncorporatedVocabularies(rId,
				excludeSBVR));

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	/**
	 * Get all the vocabularies that incorporate the vocabulary referenced by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} that is incorporated in other vocabularies (required)
	 * @return {@link VocabularyReferences} if there are vocabularies to be incorporated, HTTP 204 (no content)
	 *         otherwise
	 */
	@GET
	@Path("/{rId}/incorporating_vocabularies")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response getIncorporatingVocabularyReferences(@PathParam("rId") String rId) {

		final VocabularyReferences vrs = builder.buildVocabularyReferences(vocabularyComponent
				.getIncorporatingVocabularies(rId));

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Get all the vocabularies that incorporate the vocabulary referenced by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} that is incorporated in other vocabularies (required)
	 * @return {@link Vocabularies} if there are incorporating vocabularies, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/incorporating_vocabularies/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response getIncorporatingVocabularies(@PathParam("rId") String rId) {

		final Vocabularies vs = builder.buildVocabularies(vocabularyComponent.getIncorporatingVocabularies(rId));

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	/**
	 * Find all the possible vocabularies that can be incorporated into the vocabulary referenced by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} in which you want to incorporate other vocabularies
	 *            (required)
	 * @return {@link VocabularyReferences} if there are vocabularies to be incorporated, HTTP 204 (no content)
	 *         otherwise
	 */
	@GET
	@Path("/{rId}/find_possible_vocabularies_to_incorporate")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response getVocabularyReferencesToIncorporate(@PathParam("rId") String rId) {

		final VocabularyReferences vrs = builder.buildVocabularyReferences(vocabularyComponent
				.findPossibleVocabulariesToInCorporate(rId));

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Find all the possible vocabularies that can be incorporated into the vocabulary referenced by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} in which you want to incorporate other vocabularies
	 *            (required)
	 * @return {@link Vocabularies} if there are vocabularies to be incorporated, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/find_possible_vocabularies_to_incorporate/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response getVocabulariesToIncorporate(@PathParam("rId") String rId) {

		final Vocabularies vs = builder.buildVocabularies(vocabularyComponent
				.findPossibleVocabulariesToInCorporate(rId));

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	// TODO add direct access to the object members

	/**
	 * Get the community of the specified vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link Community} or HTTP 404 (not found) if the vocabulary is not found
	 */
	@GET
	@Path("/{rId}/community")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Community.class)
	@Transactional
	public Response getCommunity(@PathParam("rId") String rId) {

		final Community c = builder.buildCommunity(vocabularyComponent.getVocabulary(rId).getCommunity());

		return Response.ok(c).build();
	}

	/**
	 * Get all the terms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link TermReferences} if there are terms in the vocabulary, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/terms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReferences.class)
	@Transactional
	public Response getTermReferences(@PathParam("rId") String rId) {

		final TermReferences trs = builder.buildTermReferences(vocabularyComponent.getVocabulary(rId).getTerms());

		if (trs == null || trs.getTermReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(trs).build();
	}

	/**
	 * Get all the terms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link Terms} if there are terms in the vocabulary, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/terms/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Terms.class)
	@Transactional
	public Response getTerms(@PathParam("rId") String rId) {

		final Terms ts = builder.buildTerms(vocabularyComponent.getVocabulary(rId).getTerms());

		if (ts == null || ts.getTerms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ts).build();
	}

	/**
	 * Get all the characteristic forms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link CharacteristicFormReferences} if there are characteristic forms in the vocabulary, HTTP 204 (no
	 *         content) otherwise
	 */
	@GET
	@Path("/{rId}/characteristic_forms/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicFormReferences.class)
	@Transactional
	public Response getCharacteristicFormReferences(@PathParam("rId") String rId) {

		final CharacteristicFormReferences cfrs = builder.buildCharacteristicFormReferences(vocabularyComponent
				.getVocabulary(rId).getCharacteristicForms());

		if (cfrs == null || cfrs.getCharacteristicFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfrs).build();
	}

	/**
	 * Get all the characteristic forms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link CharacteristicForms} if there are characteristic forms in the vocabulary, HTTP 204 (no content)
	 *         otherwise
	 */
	@GET
	@Path("/{rId}/characteristic_forms/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CharacteristicForms.class)
	@Transactional
	public Response getCharacteristicForms(@PathParam("rId") String rId) {

		final CharacteristicForms cfs = builder.buildCharacteristicForms(vocabularyComponent.getVocabulary(rId)
				.getCharacteristicForms());

		if (cfs == null || cfs.getCharacteristicForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cfs).build();
	}

	/**
	 * Get all the binary fact type forms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link BinaryFactTypeFormReferences} if there are binary fact type forms in the vocabulary, HTTP 204 (no
	 *         content) otherwise
	 */
	@GET
	@Path("/{rId}/binary_fact_type_forms/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeFormReferences.class)
	@Transactional
	public Response getBinaryFactTypeFormReferences(@PathParam("rId") String rId) {

		final BinaryFactTypeFormReferences bftfrs = builder.buildBinaryFactTypeFormReferences(vocabularyComponent
				.getVocabulary(rId).getBinaryFactTypeForms());

		if (bftfrs == null || bftfrs.getBinaryFactTypeFormReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfrs).build();
	}

	/**
	 * Get all the binary fact type forms of a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return {@link BinaryFactTypeForms} if there are binary fact type forms in the vocabulary, HTTP 204 (no content)
	 *         otherwise
	 */
	@GET
	@Path("/{rId}/binary_fact_type_forms/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(BinaryFactTypeForms.class)
	@Transactional
	public Response getBinaryFactTypeForms(@PathParam("rId") String rId) {

		final BinaryFactTypeForms bftfs = builder.buildBinaryFactTypeForms(vocabularyComponent.getVocabulary(rId)
				.getBinaryFactTypeForms());

		if (bftfs == null || bftfs.getBinaryFactTypeForms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(bftfs).build();
	}

	/**
	 * Get all the vocabularies.
	 * 
	 * @param excludeMeta {@code true} if 'meta' vocabularies must be excluded, {@code false} otherwise (optional)
	 * @return {@link VocabularyReferences} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response getAllVocabularyReferences(@DefaultValue("true") @QueryParam("excludeMeta") boolean excludeMeta) {

		final VocabularyReferences vrs;

		if (excludeMeta)
			vrs = builder.buildVocabularyReferences(vocabularyComponent.getNonMetaVocabularies());
		else
			vrs = builder.buildVocabularyReferences(vocabularyComponent.getVocabularies());

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Get all the vocabularies.
	 * 
	 * @param excludeMeta {@code true} if 'meta' vocabularies must be excluded, {@code false} otherwise (optional)
	 * @return {@link Vocabularies} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/all/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response getAllVocabularies(@DefaultValue("true") @QueryParam("excludeMeta") boolean excludeMeta) {

		final Vocabularies vs;

		if (excludeMeta)
			vs = builder.buildVocabularies(vocabularyComponent.getNonMetaVocabularies());
		else
			vs = builder.buildVocabularies(vocabularyComponent.getVocabularies());

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	/**
	 * Retrieve vocabularies that match the specified name.
	 * 
	 * @param searchName The expression to search in vocabulary's names (required)
	 * @param offset The position of the first result to return (optional)
	 * @param number The maximum number of communities to retrieve; if 0 then all results will be retrieved (optional)
	 * @return {@link VocabularyReferences} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/find")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response findVocabularyReferencesContainingName(@QueryParam("searchName") String searchName,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final VocabularyReferences vrs = builder.buildVocabularyReferences(vocabularyComponent
				.findVocabulariesContainingName(searchName, offset, number));

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Retrieve vocabularies that match the specified name.
	 * 
	 * @param searchName The expression to search in vocabulary's names (required)
	 * @param offset The position of the first result to return (optional)
	 * @param number The maximum number of communities to retrieve; if 0 then all results will be retrieved (optional)
	 * @return {@link Vocabularies} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/find/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response findVocabulariesContainingName(@QueryParam("searchName") String searchName,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final Vocabularies vs = builder.buildVocabularies(vocabularyComponent.findVocabulariesContainingName(
				searchName, offset, number));

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	/**
	 * Get the preferred term of a vocabulary for a given object type.
	 * 
	 * @param rId The rId of the {@link Vocabulary} (required)
	 * @param objectTypeRId The resource id of {@link ObjecType} of the {@link Term} (i.e. the resource id of the
	 *            {@link Concept} of a {@link Term})
	 * @param includeIncorporatedVocabularies {@code true} if the preferred term can come also from the incorporated
	 *            vocabularies, {@code false} otherwise
	 * @return {@link Term} if a term is found for the given object type, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/preferred_term")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Term.class)
	@Transactional
	public Response getPreferredTerm(
			@PathParam("rId") String rId,
			@QueryParam("objectTypeRId") String objectTypeRId,
			@DefaultValue("false") @QueryParam("includeIncorporatedVocabularies") boolean includeIncorporatedVocabularies) {

		final Term t;

		if (includeIncorporatedVocabularies)
			t = builder.buildTerm(vocabularyComponent.getPreferredTermInIncorporatedVocabularies(rId, objectTypeRId));
		else
			t = builder.buildTerm(vocabularyComponent.getPreferredTerm(rId, objectTypeRId));

		if (t == null)
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(t).build();
	}

	/**
	 * Get the preferred term of a vocabulary for a given concept.
	 * 
	 * @param rId The rId of the {@link Vocabulary} (required)
	 * @param conceptRId The resource id of the {@link Concept} of the {@link Representation}
	 * @param includeIncorporatedVocabularies {@code true} if the preferred representation can come also from the
	 *            incorporated vocabularies, {@code false} otherwise
	 * @return {@link Term} if a term is found for the given object type, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/preferred_representation")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Representation.class)
	@Transactional
	public Response getPreferredRepresentation(
			@PathParam("rId") String rId,
			@QueryParam("conceptRId") String conceptRId,
			@DefaultValue("false") @QueryParam("includeIncorporatedVocabularies") boolean includeIncorporatedVocabularies) {

		final Representation r;

		if (includeIncorporatedVocabularies)
			r = builder.buildRepresentation(vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(
					rId, conceptRId));
		else
			r = builder.buildRepresentation(vocabularyComponent.getPreferredRepresentation(rId, conceptRId));

		if (r == null)
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(r).build();
	}

	/**
	 * Get all the preferred {@link Term}s in the {@link Vocabulary} for all {@link ObjectType} ({@link Concept})
	 * 
	 * @param rId The resource id of the {@link Vocabulary} (required)
	 * @param includeIncorporatedVocabularies {@code true} if the preferred term can come also from the incorporated
	 *            vocabularies, {@code false} otherwise
	 * @return {@link TermReferences} if preferred terms are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/preferred_terms")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(TermReferences.class)
	@Transactional
	public Response getPreferredTermReferences(
			@PathParam("rId") String rId,
			@DefaultValue("false") @QueryParam("includeIncorporatedVocabularies") boolean includeIncorporatedVocabularies) {

		final TermReferences trs;

		if (includeIncorporatedVocabularies)
			trs = builder.buildTermReferences(vocabularyComponent.getPreferredTermsInIncorporatedVocabularies(rId));
		else
			trs = builder.buildTermReferences(vocabularyComponent.getPreferredTerms(rId));

		if (trs == null || trs.getTermReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(trs).build();
	}

	/**
	 * Get all the preferred {@link Term}s in the {@link Vocabulary} for all {@link ObjectType} ({@link Concept})
	 * 
	 * @param rId The resource id of the {@link Vocabulary} (required)
	 * @param includeIncorporatedVocabularies {@code true} if the preferred term can come also from the incorporated
	 *            vocabularies, {@code false} otherwise
	 * @return {@link Terms} if preferred terms are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/preferred_terms/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Terms.class)
	@Transactional
	public Response getPreferredTerms(
			@PathParam("rId") String rId,
			@DefaultValue("false") @QueryParam("includeIncorporatedVocabularies") boolean includeIncorporatedVocabularies) {

		final Terms ts;

		if (includeIncorporatedVocabularies)
			ts = builder.buildTerms(vocabularyComponent.getPreferredTermsInIncorporatedVocabularies(rId));
		else
			ts = builder.buildTerms(vocabularyComponent.getPreferredTerms(rId));

		if (ts == null || ts.getTerms().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ts).build();
	}

	/* UPDATE */

	/**
	 * Update the name or the URI of a vocabulary. If more than one parameter is set, HTTP 400 (bad request) is
	 * returned.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} (required)
	 * @param name The name of the {@link Vocabulary}
	 * @param uri The URI of the {@link Vocabulary}
	 * @return {@link VocabularyReference} of the updated {@link Vocabulary}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReference.class)
	@Transactional
	public Response changeNameOrUri(@PathParam("rId") String rId, @FormParam("name") String name,
			@FormParam("uri") String uri) {

		final VocabularyReference vr;

		if (name != null && !name.isEmpty()) {

			// If more than one parameter is set
			if (uri != null && !uri.isEmpty())
				throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_UPDATE_MORE_1_ARG_SET, "name, uri");

			vr = builder.buildVocabularyReference(vocabularyComponent.changeName(rId, name));

		} else if (uri != null && !uri.isEmpty()) {

			vr = builder.buildVocabularyReference(vocabularyComponent.changeUri(rId, uri));

		} else {

			// If both are null
			throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_UPDATE_ALL_ARG_NULL_OR_EMPTY, "name, uri");
		}

		if (vr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.ok(vr).build();
	}

	/**
	 * Change the community of a specified vocabulary.
	 * 
	 * @param rId The resourceId of the {@link Vocabulary}
	 * @param communityRId The resource id of the new {@link Community}
	 * @return The updated {@link Vocabulary}
	 */
	@POST
	@Path("/{rId}/community")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response changeCommunity(@PathParam("rId") String rId, @FormParam("communityRId") String communityRId) {

		final Vocabulary v = builder.buildVocabulary(vocabularyComponent.changeCommunity(rId, communityRId));

		return Response.ok(v).build();
	}

	/**
	 * Add an incorporated vocabulary to the vocabulary referenced by its resource id.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} in which you want to incorporate an other vocabulary
	 *            (required)
	 * @param vocabularyToIncorporateRId The resource id of the {@link Vocabulary} that will be incorporated (required)
	 * @return The incorporating {@link Vocabulary}
	 */
	@POST
	@Path("/{rId}/incorporated_vocabularies")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response addIncorporatedVocabulary(@PathParam("rId") String rId,
			@FormParam("vocabularyToIncorporateRId") String vocabularyToIncorporateRId) {

		final Vocabulary v = builder.buildVocabulary(vocabularyComponent.addIncorporatedVocabulary(rId,
				vocabularyToIncorporateRId));

		return Response.ok(v).build();
	}

	/* DELETE */
	/**
	 * Remove an incorporated vocabulary from the vocabulary referenced by its rId.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} from which you want to disincorporate an other vocabulary
	 *            (required)
	 * @param incorporatedVocabularyRId The resource id of the {@link Vocabulary} that will be disincorporated
	 *            (required)
	 * @return The incorporating {@link Vocabulary}
	 */
	@DELETE
	@Path("/{rId}/incorporated_vocabularies")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabulary.class)
	@Transactional
	public Response removeIncorporatedVocabulary(@PathParam("rId") String rId,
			@QueryParam("incorporatedVocabularyRId") String incorporatedVocabularyRId) {

		final Vocabulary v = builder.buildVocabulary(vocabularyComponent.removeIncorporatedVocabulary(rId,
				incorporatedVocabularyRId));

		return Response.ok(v).build();
	}

	/**
	 * Delete a vocabulary.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return HTTP 200 (ok)
	 */
	@DELETE
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response removeVocabulary(@PathParam("rId") String rId) {

		vocabularyComponent.removeVocabulary(rId);

		return Response.ok().build();
	}
}
