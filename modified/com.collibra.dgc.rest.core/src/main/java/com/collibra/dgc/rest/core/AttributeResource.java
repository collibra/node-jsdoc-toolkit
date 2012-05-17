/**
 * 
 */
package com.collibra.dgc.rest.core;

import javax.ws.rs.Path;

/**
 * @author fvdmaele
 * 
 */
@Path("/attribute")
public class AttributeResource {

	// private static final Logger log = LoggerFactory.getLogger(AttributeResource.class);
	// private final ObjectFactory factory = new ObjectFactory();
	//
	// @Autowired
	// private RepresentationFactory representationFactory;
	//
	// /** Attribute Services **/
	//
	// @GET
	// @Path("{attrResourceId}")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @TypeHint(Attribute.class)
	// public Response getAttribute(@PathParam("attrResourceId") String attrResourceId, @Context Request request) {
	//
	// log.debug("get > " + attrResourceId);
	//
	// Attribute attr = factory.createAttribute();
	// attr.setId(attrResourceId);
	// // attr.setVersionedId(1L);
	// // attr.setVerbalised("Attribute Value");
	// attr.setValue("Attribute Value");
	//
	// SimpleTermReference attrLabel = factory.createSimpleTermReference();
	// attrLabel.setId("attrLabelResourceId");
	// attrLabel.setSignifier("AttributeLabel");
	// // attrLabel.setVerbalised(attrLabel.getSignifier());
	//
	// attr.setLabelReference(attrLabel);
	//
	// return Response.ok().entity(attr).build();
	// }
	//
	// /**
	// * @param r The {@link Attribute} to be created or updated
	// * @return The updated {@link Attribute}
	// */
	// @POST
	// @Consumes(MediaType.APPLICATION_XML)
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @TypeHint(Attribute.class)
	// public Response createAttribute(Attribute a) {
	//
	// log.debug("create > " + a);
	//
	// return Response.ok().build();
	// }
	//
	// /**
	// * @param reprResourceId The Resource Id of the Representation for which to create the attribute
	// * @param attrLabelSignifier The signifier (name) of the type of attribute to create (e.g. 'Definition',
	// * 'Descriptive Example', ...). This is capital insensitive
	// * @param attrLongExpression The content of the attribute to create
	// * @return The {@link Attribute}.
	// */
	// @POST
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// @TypeHint(Attribute.class)
	// public Response createAttribute(@QueryParam("reprResourceId") String reprResourceId,
	// @QueryParam("attrLabelSignifier") String attrLabelSignifier,
	// @QueryParam("attrLongExpression") String attrLongExpression) {
	//
	// log.debug("create > " + reprResourceId + " > " + attrLabelSignifier + " > " + attrLongExpression);
	//
	// return Response.ok().build();
	// }
	//
	// /**
	// * @param attrResourceId The resource id of the attribute to update.
	// * @param attrLongExpression The new content of the attribute to update.
	// * @return The updated {@link Attribite}.
	// */
	// @POST
	// @Path("{attrResourceId}")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// @TypeHint(Attribute.class)
	// public Response updateAttribute(@PathParam("attrResourceId") String attrResourceId,
	// @QueryParam("attrLongExpression") String attrLongExpression) {
	//
	// log.debug("create > " + attrResourceId + " > " + attrLongExpression);
	//
	// return Response.ok().build();
	// }
	//
	// /**
	// * Returns all the attribute types (labels) as {@link TermReferenceType}s based on the given constraints
	// * @param vocabularyResourceId The vocabulary resource id in which the attribute types must be accessible (can be
	// * null).
	// * @param speechCommunityResourceId The speech community resource id in which the attribute types must be
	// contained
	// * (can be null).
	// * @param semanticCommunityResourceId The semantic community resource id in which the attribute types must be
	// * contained (can be null).
	// * @return
	// */
	// @GET
	// @Path("/attributetypes")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @TypeHint(SimpleTermReferenceSet.class)
	// public Response getAttributeTypes(@QueryParam("vocabularyResourceId") String vocabularyResourceId,
	// @QueryParam("speechCommunityResourceId") String speechCommunityResourceId,
	// @QueryParam("semanticCommunityResourceId") String semanticCommunityResourceId) {
	//
	// log.debug("getAttributeTypes > " + vocabularyResourceId + " > " + speechCommunityResourceId + " > "
	// + semanticCommunityResourceId);
	//
	// // dummy object test
	// SimpleTermReference termRef = factory.createSimpleTermReference();
	// termRef.setId("resourceId");
	// termRef.setSignifier("Term signifier");
	// // termRef.setVerbalised(termRef.getSignifier());
	//
	// SimpleTermReference termRef2 = factory.createSimpleTermReference();
	// termRef2.setId("resourceId2");
	// termRef2.setSignifier("Term signifier2");
	// // termRef2.setVerbalised(termRef2.getSignifier());
	//
	// // SimpleTermReferenceSet termSet = factory.createSimpleTermReferenceSet();
	// // termSet.getTerms().add(termRef);
	// // termSet.getTerms().add(termRef2);
	//
	// return Response.ok().build();
	// }
}
