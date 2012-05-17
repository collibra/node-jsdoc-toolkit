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

import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Communities;
import com.collibra.dgc.rest.core.v1_0.dto.Community;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReference;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;

/**
 * Community resource of the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/community")
public class CommunityResource {

	@Autowired
	private CommunityComponent communityComponent;

	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Community documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the community REST service is not implemented yet.");
	}

	/* CREATE */

	/**
	 * Create a new community.
	 * 
	 * @param name The name for the {@link Community} (required)
	 * @param uri URI for the {@link Community} (required)
	 * @param language The language of the {@link Community} (optional)
	 * @return {@link CommunityReference} with HTTP 201 (created)
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReference.class)
	@Transactional
	public Response addCommunity(@FormParam("name") String name, @FormParam("uri") String uri,
			@FormParam("language") String language) {

		final CommunityReference cr;

		if (language != null && !language.isEmpty())
			cr = builder.buildCommunityReference(communityComponent.addCommunity(name, uri, language));
		else
			cr = builder.buildCommunityReference(communityComponent.addCommunity(name, uri));

		// TODO throw an other exception like creation error ?
		if (cr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(cr).build();
	}

	/**
	 * Create a new sub-community for the specified community.
	 * 
	 * @param rId The resource id of the parent {@link Community} (required)
	 * @param name The name for the {@link Community} (required)
	 * @param uri URI for the {@link Community} (required)
	 * @param language The language of the {@link Community} (optional)
	 * @return {@link CommunityReference} with HTTP 201 (created)
	 */
	@POST
	@Path("/{rId}/sub-communities")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReference.class)
	@Transactional
	public Response addSubCommunity(@PathParam("rId") String rId, @FormParam("name") String name,
			@FormParam("uri") String uri, @FormParam("language") String language) {

		final CommunityReference cr;

		if (language != null && !language.isEmpty())
			cr = builder.buildCommunityReference(communityComponent.addSubCommunity(rId, name, uri, language));
		else
			cr = builder.buildCommunityReference(communityComponent.addSubCommunity(rId, name, uri));

		// TODO throw an other exception like creation error ?
		if (cr == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(cr).build();
	}

	/* READ */

	/**
	 * Get a community by resource id.
	 * 
	 * @param rId The resource id of the {@link Community} (required)
	 * @return {@link Community} if the community is found, HTTP 404 (not found) otherwise
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Community.class)
	@Transactional
	public Response getCommunity(@PathParam("rId") String rId) {

		final Community c = builder.buildCommunity(communityComponent.getCommunity(rId));

		return Response.ok(c).build();
	}

	/**
	 * Get a community by name or by URI. If both URI and name are set HTTP 400 (bad request) is returned.
	 * 
	 * @param name The name of the {@link Community} (required if URI is not set)
	 * @param uri The URI of the {@link Community} (required if name is not set)
	 * @return {@link Community} if the community is found, HTTP 404 (not found) otherwise
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Community.class)
	@Transactional
	public Response getCommunityByUriOrName(@QueryParam("name") String name, @QueryParam("uri") String uri) {

		if (name != null && !name.isEmpty()) {

			if (uri != null && !uri.isEmpty())
				throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_NAME_AND_URI_SET, "name", "uri");

			final Community c = builder.buildCommunity(communityComponent.getCommunityByName(name));

			return Response.ok(c).build();

		} else if (uri != null && !uri.isEmpty()) {

			final Community c = builder.buildCommunity(communityComponent.getCommunityByUri(uri));

			return Response.ok(c).build();
		}

		throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_NAME_AND_URI_NULL, "name", "uri");
	}

	/**
	 * Get the parent of the community.
	 * 
	 * @param rId The resource id of the {@link Community} (required)
	 * @return {@link Community} if the parent community is found, HTTP 404 (not found) otherwise
	 */
	@GET
	@Path("/{rId}/parent")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Community.class)
	@Transactional
	public Response getParentCommunity(@PathParam("rId") String rId) {

		final Community c = builder.buildCommunity(communityComponent.getCommunity(rId).getParentCommunity());

		if (c == null)
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(c).build();
	}

	/**
	 * Get the sub-communities of a specified community.
	 * 
	 * @param rId The resource id of the parent {@link Community}
	 * @return {@link CommunityReferences} if sub-communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/sub-communities")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReferences.class)
	@Transactional
	public Response getSubCommunityReferences(@PathParam("rId") String rId) {

		final CommunityReferences crs = builder.buildCommunityReferences(communityComponent.getCommunity(rId)
				.getSubCommunities());

		// TODO remove the check and return directly the collection ?
		if (crs == null || crs.getCommunityReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(crs).build();
	}

	/**
	 * Get the sub-communities of a specified community.
	 * 
	 * @param rId The resource id of the parent {@link Community}
	 * @return {@link Communities} if sub-communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/sub-communities/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Communities.class)
	@Transactional
	public Response getSubCommunities(@PathParam("rId") String rId) {

		final Communities cs = builder.buildCommunities(communityComponent.getCommunity(rId).getSubCommunities());

		if (cs == null || cs.getCommunities().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cs).build();
	}

	/**
	 * Get the vocabularies of the community.
	 * 
	 * @param rId The resource id of the {@link Community} (required)
	 * @return {@link VocabularyReferences} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/vocabularies")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(VocabularyReferences.class)
	@Transactional
	public Response getVocabularyReferences(@PathParam("rId") String rId) {

		final VocabularyReferences vrs = builder.buildVocabularyReferences(communityComponent.getCommunity(rId)
				.getVocabularies());

		if (vrs == null || vrs.getVocabularyReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vrs).build();
	}

	/**
	 * Get the vocabularies of the community.
	 * 
	 * @param rId The resource id of the {@link Community} (required)
	 * @return {@link Vocabularies} if vocabularies are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/{rId}/vocabularies/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Vocabularies.class)
	@Transactional
	public Response getVocabularies(@PathParam("rId") String rId) {

		final Vocabularies vs = builder.buildVocabularies(communityComponent.getCommunity(rId).getVocabularies());

		if (vs == null || vs.getVocabularies().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(vs).build();
	}

	/**
	 * Get all communities of Data Governance Center.
	 * 
	 * @param excludeMeta <code>true</code> if meta communities must be excluded. <code>false</code> otherwise.
	 *            (optional)
	 * @param excludeSBVR <code>true</code> if the SBVR communities must be excluded. <code>false</code> otherwise.
	 *            (optional)
	 * @return {@link CommunityReferences} if communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReferences.class)
	@Transactional
	public Response getAllCommunityReferences(
			@DefaultValue(value = "true") @QueryParam("excludeMeta") boolean excludeMeta,
			@DefaultValue(value = "true") @QueryParam("excludeSBVR") boolean excludeSBVR) {

		final CommunityReferences crs = builder.buildCommunityReferences(communityComponent.getCommunities(excludeSBVR,
				excludeMeta));

		if (crs == null || crs.getCommunityReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(crs).build();
	}

	/**
	 * Get all communities of Data Governance Center.
	 * 
	 * @param excludeMeta <code>true</code> if the meta speech community must be excluded. <code>false</code> otherwise.
	 *            (optional)
	 * @param excludeSBVR <code>true</code> if the SBVR speech community must be excluded. <code>false</code> otherwise.
	 *            (optional)
	 * @return {@link Communities} if communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/all/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Communities.class)
	@Transactional
	public Response getAllCommunities(@DefaultValue(value = "true") @QueryParam("excludeMeta") boolean excludeMeta,
			@DefaultValue(value = "true") @QueryParam("excludeSBVR") boolean excludeSBVR) {

		final Communities cs = builder.buildCommunities(communityComponent.getCommunities(excludeSBVR, excludeMeta));

		if (cs == null || cs.getCommunities().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cs).build();
	}

	/**
	 * Retrieve communities that match the specified name.
	 * 
	 * @param searchName The expression to search in community's names (required)
	 * @param offset The position of the first result to return (optional)
	 * @param number The maximum number of communities to retrieve; if 0 then all results will be retrieved (optional)
	 * @return {@link CommunityReferences} if communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/find")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReferences.class)
	@Transactional
	public Response findCommunityReferencesContainingName(@QueryParam("searchName") String searchName,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final CommunityReferences crs = builder.buildCommunityReferences(communityComponent
				.findCommunitiesContainingName(searchName, offset, number));

		if (crs == null || crs.getCommunityReferences().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(crs).build();
	}

	/**
	 * Retrieve communities that match the specified name.
	 * 
	 * @param searchName The expression to search in community's names (required)
	 * @param offset The position of the first result to return (optional)
	 * @param number The maximum number of communities to retrieve; if 0 then all results will be retrieved (optional)
	 * @return {@link Communities} if communities are found, HTTP 204 (no content) otherwise
	 */
	@GET
	@Path("/find/full")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Communities.class)
	@Transactional
	public Response findCommunitiesContainingName(@QueryParam("searchName") String searchName,
			@QueryParam("offset") int offset, @QueryParam("number") int number) {

		final Communities cs = builder.buildCommunities(communityComponent.findCommunitiesContainingName(searchName,
				offset, number));

		if (cs == null || cs.getCommunities().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cs).build();
	}

	/* UPDATE */

	// TODO implement the PUT method

	/**
	 * Update the name, the URI or the language of a community or Set/Update the parent community. If more than one
	 * parameter is set, HTTP 400 (bad request) is returned.
	 * 
	 * @param rId The rId of the {@link Community} (required)
	 * @param name The name of the {@link Community}
	 * @param uri The URI of the {@link Community}
	 * @param language The language of the {@link Community}
	 * @param parentRId The resource id of the parent {@link Community}
	 * @return {@link CommunityReference} of the updated {@link Community}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(CommunityReference.class)
	@Transactional
	public Response changeNameUriLanguageOrParent(@PathParam("rId") String rId, @FormParam("name") String name,
			@FormParam("uri") String uri, @FormParam("language") String language,
			@FormParam("parentRId") String parentRId) {

		if (name != null && !name.isEmpty()) {

			// If more than one parameter is set
			if ((uri != null && !uri.isEmpty()) || (language != null && !language.isEmpty())
					|| (parentRId != null && !parentRId.isEmpty()))
				throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_UPDATE_MORE_1_ARG_SET, "name"
						+ (uri != null && !uri.isEmpty() ? ", uri" : "")
						+ (language != null && !language.isEmpty() ? ", language" : "")
						+ (parentRId != null && !parentRId.isEmpty() ? ", parentRId" : ""));

			final CommunityReference cr = builder.buildCommunityReference(communityComponent.changeName(rId, name));

			return Response.ok(cr).build();

		} else if (uri != null && !uri.isEmpty()) {

			// If more than one parameter is set
			if ((language != null && !language.isEmpty()) || (parentRId != null && !parentRId.isEmpty()))
				throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_UPDATE_MORE_1_ARG_SET, "uri"
						+ (language != null && !language.isEmpty() ? ", language" : "")
						+ (parentRId != null && !parentRId.isEmpty() ? ", parentRId" : ""));

			final CommunityReference cr = builder.buildCommunityReference(communityComponent.changeUri(rId, uri));

			return Response.ok(cr).build();

		} else if (language != null && !language.isEmpty()) {

			// If more than one parameter is set
			if (parentRId != null && !parentRId.isEmpty())
				throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID, "language, parentRId");

			final CommunityReference cr = builder.buildCommunityReference(communityComponent.changeLanguage(rId,
					language));

			return Response.ok(cr).build();

		} else if (parentRId != null && !parentRId.isEmpty()) {

			final CommunityReference cr = builder.buildCommunityReference(communityComponent.changeParentCommunity(
					parentRId, rId));

			return Response.ok(cr).build();
		}

		throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_UPDATE_ALL_ARG_NULL_OR_EMPTY,
				"name, uri, language, parent RId");
	}

	/* DELETE */

	/**
	 * Delete a community.
	 * 
	 * @param rId The resource id of the {@link Community}
	 * @return HTTP 200 (ok)
	 */
	@DELETE
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response removeCommunity(@PathParam("rId") String rId) {

		communityComponent.removeCommunity(rId);

		return Response.ok().build();
	}

	/**
	 * Remove the parent of the specified community (i.e. set the specified community as a top level community).
	 * 
	 * @param rId The resource id of the {@link Community}
	 * @return The {@link Community} for which the parent was removed with HTTP 200 (ok)
	 */
	@DELETE
	@Path("/{rId}/parent")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	@org.codehaus.enunciate.jaxrs.TypeHint(Community.class)
	public Response removeParentCommunity(@PathParam("rId") String rId) {

		final Community c = builder.buildCommunity(communityComponent.removeParentCommunity(rId));

		return Response.ok(c).build();
	}
}
