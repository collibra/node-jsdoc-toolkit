package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ConfigurationComponent;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Configuration;

/**
 * Community resource of the REST service.
 * 
 * @author fvdmaele
 * 
 */
@Component
@Path("/1.0/configuration")
public class ConfigurationResource {

	@Autowired
	private ConfigurationComponent configurationComponent;

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

	/* STRINGS */

	/**
	 * Create new string properties
	 * 
	 * @param pathvalues the values submitted by the form. For example <code>path1=value1&path2=value2& ...</code>
	 * @param rId The resource id of the {@link Resource} for which the properties will be localized. (optional)
	 * @param userName The username of the {@link User} for which the properties will be localized. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 */
	@POST
	@Path("/string")
	@Consumes("application/x-www-form-urlencoded")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response setStringProperty(MultivaluedMap<String, String> pathvalues, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		for (Entry<String, List<String>> p : pathvalues.entrySet()) {
			List<String> values = p.getValue();
			if (values.size() > 1) {
				// TODO felix configuration set list property
			} else if (!values.isEmpty()) {
				configurationComponent.setProperty(p.getKey(), values.get(0), rId, userName);
			}
		}

		final Configuration c = builder.buildConfiguration(pathvalues);

		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	/**
	 * Get string values for property
	 * 
	 * @param path The path for which to get the {@link String} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/string")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getString(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder.buildConfiguration(path, configurationComponent.getString(path, rId, userName));

		return Response.ok(c).build();
	}

	/**
	 * Get the most localized string values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * 
	 * @param path The path for which to get the {@link String} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/localizedstring")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getLocalizedString(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder.buildConfiguration(path, configurationComponent.getLocalizedString(path, rId, userName));

		return Response.ok(c).build();
	}

	/* INTEGER */

	/**
	 * Get integer values for property
	 * 
	 * @param path The path for which to get the {@link Integer} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/integer")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getInteger(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder
				.buildConfiguration(path, configurationComponent.getInteger(path, rId, userName));

		return Response.ok(c).build();
	}
	
	/**
	 * Get the most localized integer values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * 
	 * @param path The path for which to get the {@link Integer} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/localizedinteger")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getLocalizedInteger(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder
				.buildConfiguration(path, configurationComponent.getLocalizedInteger(path, rId, userName));

		return Response.ok(c).build();
	}

	/**
	 * Create new integer properties
	 * 
	 * @param pathvalues the values submitted by the form. For example <code>path1=value1&path2=value2& ...</code>
	 * @param rId The resource id of the {@link Resource} for which the properties will be localized. (optional)
	 * @param userName The username of the {@link User} for which the properties will be localized. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 */
	@POST
	@Path("/integer")
	@Consumes("application/x-www-form-urlencoded")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response setIntegerProperties(MultivaluedMap<String, Integer> pathvalues, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		for (Entry<String, List<Integer>> p : pathvalues.entrySet()) {
			List<Integer> values = p.getValue();
			if (!values.isEmpty()) {
				configurationComponent.setProperty(p.getKey(), values.get(0), rId, userName);
			}
		}

		final Configuration c = builder.buildIntegerConfiguration(pathvalues);

		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	/* BOOLEAN */

	/**
	 * Get boolean values for property
	 * 
	 * @param path The path for which to get the {@link Boolean} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/boolean")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getBoolean(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder
				.buildConfiguration(path, configurationComponent.getBoolean(path, rId, userName));

		return Response.ok(c).build();
	}

	/**
	 * Get the most localized boolean values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * 
	 * @param path The path for which to get the {@link Boolean} value
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/localizedboolean")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getLocalizedBoolean(@QueryParam("path") String path, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder
				.buildConfiguration(path, configurationComponent.getLocalizedBoolean(path, rId, userName));

		return Response.ok(c).build();
	}
	
	/**
	 * Create new boolean properties
	 * 
	 * @param pathvalues the values submitted by the form. For example <code>path1=value1&path2=value2& ...</code>
	 * @param rId The resource id of the {@link Resource} for which the properties will be localized. (optional)
	 * @param userName The username of the {@link User} for which the properties will be localized. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 */
	@POST
	@Path("/boolean")
	@Consumes("application/x-www-form-urlencoded")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response setBooleanProperties(MultivaluedMap<String, Boolean> pathvalues, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		for (Entry<String, List<Boolean>> p : pathvalues.entrySet()) {
			List<Boolean> values = p.getValue();
			if (!values.isEmpty()) {
				configurationComponent.setProperty(p.getKey(), values.get(0), rId, userName);
			}
		}

		final Configuration c = builder.buildBooleanConfiguration(pathvalues);

		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	/* SECTION */
	/**
	 * Get string values for all properties that start with the given path
	 * 
	 * @param startPath The start part of all paths for which to get the {@link String} values
	 * @param rId The resource id of the {@link Resource} for which to get the value. (optional)
	 * @param userName The username of the {@link User} for which to get the value. (optional)
	 * @return {@link Configuration} with HTTP 201 (created)
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	@GET
	@Path("/section")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@org.codehaus.enunciate.jaxrs.TypeHint(Configuration.class)
	@Transactional
	public Response getConfigSection(@QueryParam("startPath") String startPath, @QueryParam("rId") String rId,
			@QueryParam("userName") String userName) {

		final Configuration c = builder.buildConfiguration(configurationComponent.getConfigurationSection(startPath,
				rId, userName));

		return Response.ok(c).build();
	}
}
