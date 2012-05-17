package com.collibra.dgc.rest.core.v1_0.resource;

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
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.WebsiteType;
import com.collibra.dgc.rest.core.v1_0.builder.RestModelBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.InstantMessagingAccountType;
import com.collibra.dgc.rest.core.v1_0.dto.UserReference;

/**
 * User resource for the REST service.
 * 
 * @author GKDAI63
 */
@Component
@Path("/1.0/user")
public class UserResource {
	@Autowired
	private RestModelBuilder builder;

	@Autowired
	private UserComponent userComponent;

	// Create
	/**
	 * Creates a user
	 * @param userName the username
	 * @param password the password
	 * @param firstName the user's first name (optional if self)
	 * @param lastName the user's last name (optional if self)
	 * @param email the user's email (optional if self)
	 * @return the user
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	public Response addUser(@FormParam("userName") String userName, @FormParam("password") String password,
			@FormParam("firstName") String firstName, @FormParam("lastName") String lastName,
			@FormParam("email") String email) {
		User u = (User) userComponent.addUser(userName, password, firstName, lastName, email);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	/**
	 * Adds a phone to the user
	 * @param userName the user to add the phone to (optional if self)
	 * @param phoneNumber the phone number
	 * @param phoneType the {@link PhoneType} in string representation
	 * @return the user
	 */
	@Path("/{userName}/phone")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addPhone(@PathParam("userName") String userName, @FormParam("phoneNumber") String phoneNumber,
			@FormParam("phoneType") String phoneType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.addPhone(userName, phoneNumber, phoneType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	/**
	 * Adds an adress to a user
	 * @param userName the user to add the address to (optional if self)
	 * @param city the city
	 * @param street the street
	 * @param number the house number
	 * @param province the province or state
	 * @param country the country
	 * @param addressType the {@link AddressType} in string representation
	 * @return the user
	 */
	@Path("/{userName}/address")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addAddress(@PathParam("userName") String userName, @FormParam("city") String city,
			@FormParam("street") String street, @FormParam("number") String number,
			@FormParam("province") String province, @FormParam("country") String country,
			@FormParam("addressType") String addressType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.addAddress(userName, city, street, number, province, country, addressType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	/**
	 * Adds a website to a user
	 * @param userName the user to add the website to (optional if self)
	 * @param url the url
	 * @param websiteType the {@link WebsiteType} in string representation
	 * @return the user
	 */
	@Path("/{userName}/website")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addWebsite(@PathParam("userName") String userName, @FormParam("url") String url,
			@FormParam("websiteType") String websiteType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.addWebsite(userName, url, websiteType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	/**
	 * Adds an instantmessagingaccount to a user
	 * @param userName the user to add the account to
	 * @param account the account
	 * @param accountType the {@link InstantMessagingAccountType} in string representation
	 * @return the user
	 */
	@Path("/{userName}/im")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response addInstantMessagingAccount(@PathParam("userName") String userName,
			@FormParam("account") String account, @FormParam("accountType") String accountType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}

		User u = (User) userComponent.addInstantMessagingAccount(userName, account, accountType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	// Read
	/**
	 * Retrieves a user by it's user name
	 * @param userName
	 * @return the user
	 */
	@Path("/{userName}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response getUser(@PathParam("userName") String userName) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}

		User u = (User) userComponent.getUser(userName);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.status(Status.CREATED).entity(user).build();
	}

	// Update

	/**
	 * Updates a user
	 * @param userName the username (optional if self)
	 * @param language the new language
	 * @param password the new password
	 * @param firstName the new first name
	 * @param lastName the new last name
	 * @param email the new email
	 * @return the upadted user
	 */
	@Path("/{userName}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.UserReference.class)
	@Transactional
	public Response changeUser(@PathParam("userName") String userName, @FormParam("language") String language,
			@FormParam("password") String password, @FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName, @FormParam("email") String email) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = null;
		if (language != null && !language.isEmpty()) {
			u = (User) userComponent.changeLanguage(userName, language);
		}
		if (password != null && !password.isEmpty()) {
			u = (User) userComponent.changePassword(userName, password);
		}
		if (userName != null && !userName.isEmpty() && firstName != null && !firstName.isEmpty() && lastName != null
				&& !lastName.isEmpty() && email != null && !email.isEmpty()) {
			u = (User) userComponent.changeUser(userName, firstName, lastName, email);
		}
		UserReference user = builder.buildUserReference(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * Updates a user's phone
	 * @param rId the RId of the phone
	 * @param userName the user the phone belongs too
	 * @param phoneNumber the new phone number
	 * @param phoneType the new {@link PhoneType} in string representation
	 * @return the updated user
	 */
	@Path("/{userName}/phone/{phoneRId}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response changePhone(@PathParam("phoneRId") String rId, @PathParam("userName") String userName,
			@FormParam("phoneNumber") String phoneNumber, @FormParam("phoneType") String phoneType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.changePhone(userName, rId, phoneNumber, phoneType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * Updates an address of a user
	 * @param rId the resource id of the address
	 * @param userName the username of the user the address belongs too (optional if self)
	 * @param city the new city
	 * @param street the new street
	 * @param number the new house number
	 * @param province the new province or state
	 * @param country the new country
	 * @param addressType the new {@link AddressType} in string representation
	 * @return
	 */
	@Path("/{userName}/address/{addressRId}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response changeAddress(@PathParam("addressRId") String rId, @PathParam("userName") String userName,
			@FormParam("city") String city, @FormParam("street") String street, @FormParam("number") String number,
			@FormParam("province") String province, @FormParam("country") String country,
			@FormParam("addressType") String addressType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent
				.changeAddress(userName, rId, city, street, number, province, country, addressType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * updates a user's website
	 * @param rId the rid of the website
	 * @param userName the username of the user whose website will be edited (optional if self)
	 * @param url the new url
	 * @param websiteType the new {@link WebsiteType} in string representation.
	 * @return
	 */
	@Path("/{userName}/website/{websiteRId}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response changeWebsite(@PathParam("websiteRId") String rId, @PathParam("userName") String userName,
			@FormParam("url") String url, @FormParam("websiteType") String websiteType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.changeWebsite(userName, rId, url, websiteType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * Changes an instant messaging account for a user
	 * @param rId the rid of the im account (optional if self)
	 * @param userName the user's name
	 * @param account the new account name
	 * @param accountType the new {@link InstantMessagingAccountType} in string representation
	 * @return the updates user
	 */
	@Path("/{userName}/im/{imRId}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response changeInstantMessagingAccount(@PathParam("imRId") String rId,
			@PathParam("userName") String userName, @FormParam("account") String account,
			@FormParam("accountType") String accountType) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.changeInstantMessagingAccount(userName, rId, account, accountType);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	// Delete

	/**
	 * Removes a phone from a user
	 * @param rId the rId of the phone
	 * @param userName the name f the user to remove the phone from (optional if self)
	 * @return the updated user
	 */
	@Path("/{userName}/phone")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response removePhone(@QueryParam("phoneRId") String rId, @PathParam("userName") String userName) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.removePhone(userName, rId);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * removes an address from a user
	 * @param rId the resource id of a user
	 * @param userName the user's name (optional if self)
	 * @return the updated user
	 */
	@Path("/{userName}/address")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response removeAddress(@QueryParam("addressRId") String rId, @PathParam("userName") String userName) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.removeAddress(userName, rId);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * removes a website frm a user
	 * @param rId the rid of the website
	 * @param userName the user's name (optional if self)
	 * @return the updated user
	 */
	@Path("/{userName}/website")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response removeWebsite(@QueryParam("websiteRId") String rId, @PathParam("userName") String userName) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.removeWebsite(userName, rId);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}

	/**
	 * Removes a user
	 * @param userName the user's name
	 * @return ok if successful
	 */
	@Path("/{userName}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	public Response removeUser(@PathParam("userName") String userName) {
		userComponent.removeUser(userName);
		return Response.ok().build();
	}

	/**
	 * deletes an im account from a user
	 * @param rId the rid of the im account
	 * @param userName the user's name (optional if self)
	 * @return the updated user
	 */
	@Path("/{userName}/im")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@org.codehaus.enunciate.jaxrs.TypeHint(com.collibra.dgc.rest.core.v1_0.dto.User.class)
	@Transactional
	public Response removeInstantMessagingAccount(@QueryParam("imRId") String rId,
			@PathParam("userName") String userName) {
		if (userName == null || userName.isEmpty()) {
			userName = userComponent.getCurrentUser().getUserName();
		}
		User u = (User) userComponent.removeInstantMessagingAccount(userName, rId);
		com.collibra.dgc.rest.core.v1_0.dto.User user = builder.buildUser(u);
		return Response.ok().entity(user).build();
	}
}
