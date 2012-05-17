package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.SearchComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.service.SearchService.EType;
import com.collibra.dgc.rest.core.v1_0.builder.RestSearchBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItems;

/**
 * Search resource of the REST service.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/search")
public class SearchResource {

	@Autowired
	private SearchComponent searchComponent;

	@Autowired
	private RestSearchBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the search service documentation.
	 * 
	 * @return {@link Response}
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the search REST service is not implemented yet.");
	}

	/* READ */

	/**
	 * Search for resources matching the given search query string.
	 * 
	 * @param query The string to search for in the index (required)
	 * @param types The types of object to search for. If no types are set, the search engine will look for all type of
	 *            resources. Each type must be separd with a ',' (optional)
	 * @param max The maximum number of hits we want to retrieve (optional)
	 * @return {@link SearchResultItems} if the search returns results, HTTP 204 (no content) otherwise
	 * @throws Exception
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(SearchResultItems.class)
	@Transactional
	public Response find(@QueryParam("query") String query, @QueryParam("types") String types,
			@DefaultValue(value = "25") @QueryParam("max") int max) throws Exception {

		final SearchResultItems sris;

		// If not types => search all types
		if (types == null || types.isEmpty()) {

			sris = builder.buildSearchResultItems(searchComponent.search(query, max));

			// If types string defined
		} else {

			final Collection<EType> typeCollection = convertTypesStringToCollection(types);

			if (typeCollection.isEmpty())
				sris = builder.buildSearchResultItems(searchComponent.search(query, max));
			else
				sris = builder.buildSearchResultItems(searchComponent.search(query, typeCollection, max));
		}

		if (sris == null || sris.getSearchResultItems().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(sris).build();
	}

	/**
	 * Trigger a full re-index of DGC index engine.
	 * 
	 * @return HTTP 200 (ok)
	 */
	@POST
	@Path("/re-index")
	@Transactional
	public Response doFullReIndex() {

		searchComponent.doFullReIndex();

		return Response.ok().build();
	}

	/**
	 * Convert types string to a collection of EType of the search service.
	 */
	public static final Collection<EType> convertTypesStringToCollection(String types) {

		final Collection<EType> typeCollection = new HashSet<EType>();
		final String[] typeArray = types.split(",");

		for (final String type : typeArray) {

			if (type != null && !type.isEmpty()) {
				final EType t = EType.valueOf(type.toUpperCase().trim());
				if (t == null) {
					throw new IllegalArgumentException(DGCErrorCodes.SEARCH_RESOURCE_TYPE_NULL, type, "types");
				}
				typeCollection.add(t);
			}
		}

		return typeCollection;
	}

}
