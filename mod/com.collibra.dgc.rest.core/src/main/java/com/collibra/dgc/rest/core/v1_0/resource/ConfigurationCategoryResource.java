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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ConfigurationCategoryComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeAndRelationTypesConfigurationCategories;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeAndRelationTypesConfigurationCategory;

/**
 * Public REST Service / API for the management of {@link ConfigurationCategory}s. A {@link ConfigurationCatgory} is a
 * grouping of {@link Representation}s in a specific order, that is given a unique category name and description. The
 * grouped representations can represent different things. </br> </br> In the case of a
 * {@link AttributeAndRelationTypesConfigurationCategory}, they represent attribute types or relation types. </br> A
 * Configuration Category is uniquely referenced by its name.
 * 
 * @author fvdmaele
 * 
 */
@Component
@Path("/1.0/configuration_category")
public class ConfigurationCategoryResource {

	@Autowired
	private ConfigurationCategoryComponent confCategoryComponent;

	@Autowired
	protected RestModelBuilder builder;

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
	 * Create a new {@link AttributeAndRelationTypesConfigurationCategory} and persist it
	 * 
	 * @param name The name of the category. Must be unique!
	 * @param description The description of the category. Can be null (optional)
	 * @param reprRIds The list of resource id's of {@link Representation}s that represent Attribute Types (Terms) or
	 *            Relation Types (BinaryFactTypeForms)
	 * @return {@link AttributeAndRelationTypesConfigurationCategory}
	 */
	@POST
	@Path("/attribute_relation_category")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response addAttributeAndRelationTypesConfigurationCategoryResource(@FormParam("name") String name,
			@FormParam("description") String description, @FormParam("representationRIDs") List<String> reprRIds) {

		final AttributeAndRelationTypesConfigurationCategory cat;

		cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
				.addAttributeAndRelationTypesConfigurationCategory(name, description, reprRIds));

		return Response.status(Response.Status.CREATED).entity(cat).build();
	}

	/* READ */

	/**
	 * Get the {@link AttributeAndRelationTypesConfigurationCategory} by its unique name
	 * 
	 * @param name The name of the category
	 * @return {@link AttributeAndRelationTypesConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND}
	 */
	@GET
	@Path("/attribute_relation_category/{name}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response getAttributeAndRelationTypesConfigurationCategory(@PathParam("name") String name) {

		final AttributeAndRelationTypesConfigurationCategory cat;

		cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
				.getAttributeAndRelationTypesConfigurationCategory(name));

		return Response.ok(cat).build();
	}

	@GET
	@Path("/attribute_relation_category/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response getAllAttributeAndRelationTypesCategories() {

		final AttributeAndRelationTypesConfigurationCategories cats = builder
				.buildAttributeAndRelationTypesConfigurationCategories(confCategoryComponent
						.getAllAttributeAndRelationTypesCategories());

		if (cats == null || cats.getAttributeAndRelationTypesConfigurationCategories().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(cats).build();
	}

	/* UPDATE */

	/**
	 * Add a new {@link Representation} to the given {@link AttributeAndRelationConfigurationCategory} optionally on the
	 * specified position.
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param reprRId the resource id of the {@link Representation} (mandatory)
	 * @param position the new position index. Can be null (optional)
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * 
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 * @throws {@link DGCException} with error codes {@link DGCErrorCodes#CONFCAT_COULD_NOT_ADD_ON_POSITIONG}
	 */
	@POST
	@Path("/attribute_relation_category/{name}/add")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response addRepresentationToAttributeRelationTypesConfigurationCategory(@PathParam("name") String name,
			@FormParam("reprRId") String reprRId, @FormParam("position") Integer position) {
		final AttributeAndRelationTypesConfigurationCategory cat;

		if (position == null)
			cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
					.addRepresentationToAttributeAndRelationConfigurationCategory(name, reprRId));
		else
			cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
					.addRepresentationToAttributeAndRelationConfigurationCategory(name, reprRId, position));

		return Response.ok(cat).build();
	}

	/**
	 * Change the order of the given {@link Representation} in the given
	 * {@link AttributeAndRelationConfigurationCategory} to the given position
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param reprRId the resource id of the {@link Representation} (mandatory)
	 * @param position the new position index (mandatory)
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * 
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 * @throws {@link DGCException} with error codes {@link DGCErrorCodes#CONFCAT_COULD_NOT_CHANGE_ORDER}
	 */
	@POST
	@Path("/attribute_relation_category/{name}/changeOrder")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response changeRepresentationOrderInAttributeAndRelationConfigurationCategory(
			@PathParam("name") String name, @FormParam("reprRId") String reprRId,
			@FormParam("position") Integer position) {
		final AttributeAndRelationTypesConfigurationCategory cat;

		cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
				.changeRepresentationOrderInAttributeAndRelationConfigurationCategory(name, reprRId, position));

		return Response.ok(cat).build();
	}

	@POST
	@Path("/attribute_relation_category/{name}/description")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeAndRelationTypesConfigurationCategory.class)
	@Transactional
	public Response changeAttributeAndRelationConfigurationCategoryDescription(@PathParam("name") String name,
			@FormParam("description") String description) {

		final AttributeAndRelationTypesConfigurationCategory cat;

		cat = builder.buildAttributeAndRelationTypesConfigurationCategory(confCategoryComponent
				.changeAttributeAndRelationConfigurationCategoryDescription(name, description));

		return Response.ok(cat).build();
	}

	/* DELETE */

	/**
	 * Remove the given {@link AttributeAndRelationTypesConfigurationCategory}.
	 * 
	 * @param name The name that uniquely references the {@link ConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND}
	 */
	@DELETE
	@Path("/attribute_relation_category/{name}")
	@Transactional
	public Response removeAttributeAndRelationTypesConfigurationCategory(@PathParam("name") String name) {

		confCategoryComponent.removeConfigurationCategory(name);
		return Response.ok().build();
	}
}
