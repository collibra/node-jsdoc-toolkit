package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
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

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.util.Defense;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Attribute;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeType;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeTypes;
import com.collibra.dgc.rest.core.v1_0.dto.MultiValueListAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.SingleValueListAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.StringAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.Term;

/**
 * Public REST service for the management of {@link AttributeType}. An {@link Attribute} stores extra information about
 * a {@link Representation}. For each kind of {@link Attribute} (e.g. {@link StringAttribute},
 * {@link MultiValueListAttribute}), several {@link AttributeType} can be defined in the Collibra <i>Attribute Types
 * Vocabulary</i>. This is a Collibra SBVR extension.
 * 
 * <br />
 * <br />
 * An {@link AttributeType} is represented by a {@code Label} {@link Term} that contains an {@link Attribute} of the
 * type {@code Description}. Each {@link AttributeType} is assigned to a certain kind of {@link Attribute}.
 * 
 * <br />
 * <br />
 * The different kinds of {@link Attribute} are:
 * <ul>
 * <li>{@link StringAttribute}: an {@link Attribute} whose value is a {@link String} long expression</li>
 * <li>{@link SingleValueListAttribute}: an {@link Attribute} whose value is one of the value contained in a list of
 * allowed one</li>
 * <li>{@link MultiValueListAttribute}: an {@link Attribute} whose values could be one or more allowed values</li>
 * </ul>
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@code Label} {@link Term}. The resource id is a UUID. 'RId' at the end
 * of an argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/attribute_type")
public class AttributeTypeResource {

	@Autowired
	private AttributeTypeComponent attributeTypeComponent;

	@Autowired
	private RestModelBuilder builder;

	/* DOCUMENTATION */

	/**
	 * Redirect to the Attribute Type documentation.
	 */
	@GET
	@Path("/doc")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response redirectToDoc() {

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		throw new NotImplementedException(
				"The documentation of the attribute type REST service is not implemented yet.");
	}

	/* CREATE */

	// TODO: Add the description of the kind in the model
	/**
	 * Add an attribute type for a certain kind of attribute.
	 * 
	 * <br />
	 * <br />
	 * For example, if we define an attribute type 'definition', the kind of attribute that can be of this attribute
	 * type is 'StringAttribute'. Therefore, every attribute of the type 'definition' will have a value that is a string
	 * long expression.
	 * 
	 * <br />
	 * <br />
	 * The different kinds of {@link Attribute} are:
	 * <ul>
	 * <li>{@link StringAttribute}: an {@link Attribute} whose value is a {@link String} long expression</li>
	 * <li>{@link SingleValueListAttribute}: an {@link Attribute} whose value is one of the value contained in a list of
	 * allowed one</li>
	 * <li>{@link MultiValueListAttribute}: an {@link Attribute} whose values could be one or more allowed values</li>
	 * </ul>
	 * 
	 * @param signifier The signifier of the {@link AttributeType} (required)
	 * @param description The description of the {@link AttributeType} (optional)
	 * @param kind The kind of {@link Attribute} that can be defined using this {@link AttributeType} (required)
	 * @param allowedValues The {@link Collection} of {@link String} value that are allowed for the {@link Attribute} of
	 *            the following kinds: SingleValueListAttributeType and MultiValueListAttribute. For these both kinds,
	 *            this parameter is required
	 * @return {@link AttributeType} with HTTP 201 (created)
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response addAttributeType(@FormParam("signifier") String signifier,
			@FormParam("description") String description, @FormParam("kind") String kind,
			@FormParam("allowedValues") List<String> allowedValues) {

		AttributeType at = null;

		Defense.notEmpty(kind, DGCErrorCodes.ATTRIBUTE_TYPE_KIND_NULL, DGCErrorCodes.ATTRIBUTE_TYPE_KIND_EMPTY, "kind");

		if (kind.equalsIgnoreCase(com.collibra.dgc.core.model.representation.StringAttribute.class.getSimpleName())) {

			at = builder.buildAttributeType(attributeTypeComponent.addStringAttributeType(signifier, description));

		} else if (kind.equalsIgnoreCase(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class
				.getSimpleName())) {

			at = builder.buildAttributeType(attributeTypeComponent.addValueListAttributeType(signifier, description,
					false, allowedValues));

		} else if (kind.equalsIgnoreCase(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class
				.getSimpleName())) {

			at = builder.buildAttributeType(attributeTypeComponent.addValueListAttributeType(signifier, description,
					true, allowedValues));

		} else
			throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_KIND_UNKNOWN, kind);

		// TODO throw an other exception like creation error ?
		if (at == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED).entity(at).build();
	}

	/* READ */

	/**
	 * Get the {@link AttributeType} for the given resource id.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link AttributeType} (required)
	 * @return {@link AttributeType}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getAttributeType(@PathParam("rId") String rId) {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getAttributeType(rId));

		return Response.ok(at).build();
	}

	/**
	 * Get the {@link AttributeType} for the given signifier.
	 * 
	 * @param signifier The signifier of the {@link Attribute} {@code Type} (required)
	 * @return {@link AttributeType}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getAttributeTypeBySignifier(@QueryParam("signifier") String signifier) {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent
				.getAttributeTypeBySignifier(signifier));

		return Response.ok(at).build();
	}

	/**
	 * Get the {@code Definition} {@link AttributeType}.
	 * 
	 * @return {@link AttributeType}
	 */
	@GET
	@Path("/definition")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getDefinitionAttributeType() {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getDefinitionAttributeType());

		return Response.ok(at).build();
	}

	/**
	 * Get the {@code Description} {@link AttributeType}.
	 * 
	 * @return {@link AttributeType}
	 */
	@GET
	@Path("/description")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getDescriptionAttributeType() {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getDescriptionAttributeType());

		return Response.ok(at).build();
	}

	/**
	 * Get the {@code Example} {@link AttributeType}.
	 * 
	 * @return {@link AttributeType}
	 */
	@GET
	@Path("/example")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getExampleAttributeType() {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getExampleAttributeType());

		return Response.ok(at).build();
	}

	/**
	 * Get the {@code Note} {@link AttributeType}.
	 * 
	 * @return {@link AttributeType}
	 */
	@GET
	@Path("/note")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response getNoteAttributeType() {

		final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getNoteAttributeType());

		return Response.ok(at).build();
	}

	/**
	 * Get the description of an {@link AttributeType} for the given {@code Label} {@link Term} resource id.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type} (required)
	 * @return {@link Attribute} ({@link StringAttribute})
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/description")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Attribute.class)
	@Transactional
	public Response getAttributeTypeDescription(@PathParam("rId") String rId) {

		final Attribute a = builder.buildStringAttribute(attributeTypeComponent.getAttributeTypeDescription(rId));

		if (a == null)
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(a).build();
	}

	/**
	 * Get the allowed values of a {@link SingleValueListAttribute} or a {@link MultiValueListAttribute}
	 * {@link AttributeType}.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link SingleValueListAttribute} or the
	 *            {@link MultiValueListAttribute} {@code Type} (required)
	 * @return A {@link Collection} of {@link String} value allowed for the {@link SingleValueListAttribute} or
	 *         {@link MultiValueListAttribute} {@link AttributeType}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@GET
	@Path("/{rId}/allowed_values")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response getAllowedValues(@PathParam("rId") String rId) {

		Collection<String> allowedValues = attributeTypeComponent.getAllowedValues(rId);

		if (allowedValues == null || allowedValues.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(allowedValues).build();
	}

	/**
	 * Get all the {@link AttributeTypes} of the Collibra <i>Attribute Types Vocabulary</i>.
	 * @return {@link AttributeTypes}
	 */
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeTypes.class)
	@Transactional
	public Response getAttributeTypes() {

		final AttributeTypes ats = builder.buildAttributeTypes(attributeTypeComponent.getAttributeTypes());

		if (ats == null || ats.getAttributeTypes().isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(ats).build();
	}

	/* UPDATE */

	// TODO implement the put method

	/**
	 * Update the signifier, the description or the allowed values of an attribute type. If more than one parameter is
	 * set, HTTP 400 (bad request) is returned.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type} (required)
	 * @param signifier The new signifier
	 * @param description The new description
	 * @param allowedValues The {@link Collection} of new allowed values
	 * @return The updated {@link AttributeType}
	 */
	@POST
	@Path("/{rId}")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(AttributeType.class)
	@Transactional
	public Response changeSignifierDescriptionOrValues(@PathParam("rId") String rId,
			@FormParam("signifier") String signifier, @FormParam("description") String description,
			@FormParam("allowedValues") List<String> allowedValues) {

		if (signifier != null && !signifier.isEmpty()) {

			// If more than one parameter is set
			if ((description != null && !description.isEmpty()) || (allowedValues != null && !allowedValues.isEmpty()))
				throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_UPDATE_MORE_1_ARG_SET, "signifier"
						+ (description != null && !description.isEmpty() ? ", description" : "")
						+ (allowedValues != null && !allowedValues.isEmpty() ? ", allowedValues" : ""));

			final AttributeType at = builder.buildAttributeType(attributeTypeComponent.changeSignifier(rId, signifier));

			return Response.ok(at).build();

		} else if (description != null && !description.isEmpty()) {

			// More than one parameter is set
			if (allowedValues != null && !allowedValues.isEmpty())
				throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_UPDATE_MORE_1_ARG_SET,
						"description, allowedValues");

			attributeTypeComponent.changeDescription(rId, description);
			final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getAttributeType(rId));

			return Response.ok(at).build();

		} else if (allowedValues != null && !allowedValues.isEmpty()) {

			attributeTypeComponent.changeAllowedValues(rId, allowedValues);
			final AttributeType at = builder.buildAttributeType(attributeTypeComponent.getAttributeType(rId));

			return Response.ok(at).build();
		}

		throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_UPDATE_ALL_ARG_NULL_OR_EMPTY,
				"signifier, description, allowedValues");
	}

	/* DELETE */

	/**
	 * Remove the {@link AttributeType} with the given signifier.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link AttributeType} (required)
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	@DELETE
	@Path("/{rId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Transactional
	public Response removeAttributeType(@PathParam("rId") String rId) {

		attributeTypeComponent.removeAttributeType(rId);

		return Response.ok().build();
	}
}
