package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
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
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Relation;
import com.collibra.dgc.rest.core.v1_0.dto.Relations;

/**
 * Relation resource of the REST service
 * 
 * @author fvdmaele
 *
 */
@Component
@Path("/1.0/relation")
public class RelationResource {

	@Autowired
	private RelationComponent relationComponent;
	
	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Relation documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException("The documentation of the relation REST service is not implemented yet.");
	}

	/* READ */

	/**
	 * Get the {@link Relation} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Relation}
	 * @return The {@link Relation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#RELATION_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Relation.class)
	@Transactional
	public Response getRelation(@PathParam("rId") String rId) {

		final Relation r = builder.buildRelation(relationComponent.getRelation(rId));

		return Response.ok(r).build();
	}
	

	/**
	 * Get the all {@link Relation}s with the given relation type.
	 * 
	 * @param typeRId The resource id of the {@code Relation Type} {@link BinaryFactTypeForm}
	 * @return The {@link Relations}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Relation.class)
	@Transactional
	public Response getRelations(@QueryParam("typeRId") String typeRId) {
		
		final Relations r = builder.buildRelations(relationComponent.findRelationsByType(typeRId));

		if (r == null || r.getRelations().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();
		
		return Response.ok(r).build();
	}
	
	/**
	 * Get the all {@link Relation}s with the given relation type.
	 * 
	 * @param typeRId The resource id of the {@code Relation Type} {@link BinaryFactTypeForm}
	 * @return The {@link Relations}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Relation.class)
	@Transactional
	public Response getRelations(@QueryParam("sourceRId") String sourceRId,
			@QueryParam("targetRId") String targetRId,
			@QueryParam("typeRId") String typeRId) {
		
		boolean hasType = (typeRId != null);
		boolean hasSource = (sourceRId != null);
		boolean hasTarget = (targetRId != null);
		
		Collection<com.collibra.dgc.core.model.relation.Relation> relations = new ArrayList(0);
		
		if(hasSource && hasTarget && hasType) {
			relations = relationComponent.findRelationsBySourceAndTargetAndType(typeRId, sourceRId, targetRId);
		} else if(hasSource && hasTarget && !hasType) {
			relations = relationComponent.findRelationsBySourceAndTarget(sourceRId, targetRId);
		} else if(hasSource && hasType && !hasTarget) {
			relations = relationComponent.findRelationsBySourceAndType(typeRId, sourceRId);
		} else if(hasTarget && hasType && !hasSource) {
			relations = relationComponent.findRelationsByTargetAndType(typeRId, targetRId);
		} else if(hasSource && !hasType && !hasTarget) {
			relations = relationComponent.findRelationsBySource(sourceRId);
		} else if(hasTarget && !hasType && !hasSource) {
			relations = relationComponent.findRelationsByTarget(targetRId);
		} else if(hasType && !hasSource && !hasTarget) {
			relations = relationComponent.findRelationsByType(typeRId);
		}
	
		final Relations r = builder.buildRelations(relations);

		if (r == null || r.getRelations().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();
		
		return Response.ok(r).build();
	}
}
