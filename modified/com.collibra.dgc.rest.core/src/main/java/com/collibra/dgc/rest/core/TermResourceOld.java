/**
 * 
 */
package com.collibra.dgc.rest.core;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

/**
 * @author fvdmaele
 * 
 */
@Component
@Path("/term")
public class TermResourceOld {

	// private static final Logger log = LoggerFactory.getLogger(TermResourceOld.class);
	// private final ObjectFactory factory = new ObjectFactory();
	//
	// @Autowired
	// private CommunityFactory communityFactory;
	//
	// @Autowired
	// private RepresentationFactory representationFactory;
	//
	// @Autowired
	// private RepresentationComponent representationComponent;
	//
	// @Autowired
	// private RestModelBuilder builder;
	//
	// /** Term services **/
	//
	// @GET
	// @Produces({ MediaType.APPLICATION_XHTML_XML })
	// @Transactional
	// public Response root() {
	// try {
	// return Response.seeOther(new URI("../docs/rest/resource_TermResource.html")).build();
	// } catch (Exception e) {
	// return Response.status(Status.ERROR).build();
	// }
	// }
	//
	// /**
	// * @param resourceId The resource id of the Term to return
	// * @return The latest version of the {@link Term}
	// */
	// @GET
	// @Path("{resourceId}")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @org.codehaus.enunciate.jaxrs.TypeHint(Term.class)
	// @Transactional
	// public Term get(@PathParam("resourceId") String resourceId, @Context Request request) {
	//
	// return builder.buildTerm(representationComponent.getTerm(resourceId));
	// }
	//
	// /**
	// * @param r The {@link Representation} to be created
	// * @return The updated {@link Representation}
	// */
	// @POST
	// @Consumes(MediaType.APPLICATION_XML)
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @TypeHint(Term.class)
	// @Transactional
	// public Term create(Term t) {
	// log.debug("create > " + t);
	//
	// return null;
	// }
	//
	// /**
	// * @param termSignifier The {@link String} signifier of the term to be created
	// * @param vocResourceId The resource id of the Vocabulary in which the term should be created
	// * @return The created {@link Term} object
	// */
	// @POST
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// // TODO Set ? @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// @TypeHint(Term.class)
	// @Transactional
	// public Term create(@QueryParam("termSignifier") String termSignifier,
	// @QueryParam("vocabularyResourceId") String vocResourceId) {
	//
	// return builder.buildTerm(representationComponent.addTerm(vocResourceId, termSignifier));
	// }
	//
	// /**
	// * @param vocResourceIds a List of vocabulary resource id's to which the returned terms must belong
	// * @param conceptTypeResourceIds a List of concept type resource id's to which the returned terms' concept type
	// must
	// * belong to
	// * @param statusResourceIds a List of status term resource id's to which the returned terms' status must belong
	// to.
	// * @param attributeFilters a List of Status Filters that follow the following pattern:
	// * <b>statusLabel@operator@string</b>
	// * <p>
	// * an operator is one of the following allowed values: includes, startswith, endswith, equals.
	// * </p>
	// * <p>
	// * For example:
	// * <ul>
	// * <li><b>definition@includes@customer</b>,</li>
	// * <li><b>example@startswith@test</b>.</li>
	// * </ul>
	// * </p>
	// * @param limit the maximum number of {@link TermReference}s that will be returned. Default is 50
	// * @return a {@link TermReferenceSet}
	// */
	// @GET
	// @Path("/filter")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @org.codehaus.enunciate.jaxrs.TypeHint(TermReferenceSet.class)
	// @Transactional
	// public Response list(@QueryParam("vocabularyResourceId") List<String> vocResourceIds,
	// @QueryParam("conceptTypeResourceId") List<String> conceptTypeResourceIds,
	// @QueryParam("statusResourceId") List<String> statusResourceIds,
	// @QueryParam("attributeFilter") List<String> attributeFilters,
	// @DefaultValue("50") @QueryParam("limit") Integer limit, @Context Request request) {
	//
	// log.debug("filter > " + vocResourceIds + " > " + conceptTypeResourceIds + " > " + statusResourceIds + " > "
	// + attributeFilters + " > " + limit);
	//
	// return Response.ok().build();
	// }
	//
	// @GET
	// @Path("/vocabulary/{vocResourceId}/signifier/{termSignifier}")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @Transactional
	// public Term getTermBySignifier(@PathParam("vocResourceId") String vocResourceId,
	// @PathParam("termSignifier") String termSignifier) {
	//
	// return builder.buildTerm(representationComponent.getTermBySignifier(vocResourceId, termSignifier));
	// }
}
