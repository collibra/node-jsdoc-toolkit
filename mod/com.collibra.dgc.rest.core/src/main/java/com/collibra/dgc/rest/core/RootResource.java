package com.collibra.dgc.rest.core;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ApplicationComponent;

/**
 * Root of the DGC REST service.
 * 
 * @author felix
 * @author pmalarme
 * 
 */
@Component
@Path("/")
public class RootResource {

	@Autowired
	ApplicationComponent applicationComponent;

	/* VERSIONED DOCUMENTATION OF THE REST SERVICE */

	/**
	 * Welcome page of the REST service documentation for the user defined version (current version).
	 * @return The welcome page.
	 * @throws Exception
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	public Response readRoot() throws Exception {
		return readRestServiceVersion("current");
	}

	/**
	 * Redirect to the documentation of the specified version of the REST service.
	 * 
	 * @param restServiceVersion The version of the REST service.
	 * @return {@link Response}
	 * @throws Exception
	 */
	@GET
	@Path("/{restServiceVersion}")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	@Transactional
	public Response readRestServiceVersion(@PathParam("restServiceVersion") String restServiceVersion) throws Exception {

		return redirectToVersionedPage(restServiceVersion, "welcome");
	}

	/**
	 * Redirect to the versioned page.
	 * @param restServiceVersion The version of the REST service.
	 * @param page The page required.
	 * @return {@link Response}
	 * @throws Exception
	 */
	private Response redirectToVersionedPage(String restServiceVersion, String page) throws Exception {

		if (restServiceVersion == null || restServiceVersion.isEmpty()
				|| restServiceVersion.equalsIgnoreCase("current"))
			restServiceVersion = getCurrentVersion();

		else if (restServiceVersion.equalsIgnoreCase("latest"))
			restServiceVersion = getLatestVersion();

		// TODO set the path of the rest service (e.g. rest)
		else if (restServiceVersion.equalsIgnoreCase("rest"))
			return Response.temporaryRedirect(new URI("/rest/current")).build();

		// TODO throw an unknown version error
		else if (!isVersionPattern(restServiceVersion))
			throw new NotImplementedException("This is not a valid version");

		// TODO Set the URI to the documentation and remove the current message.
		// Response.seeOther( new URI("...")).build();

		// throw new NotImplementedException("The root of of the REST service version (" + restServiceVersion
		// + ") is not implemented yet.");
		return Response.temporaryRedirect(
				new URI(restServiceVersion + ((page == null || page.isEmpty()) ? "" : "/" + page))).build();
	}

	/**
	 * Get the REST service current (user defined) version.
	 * 
	 * @return REST service current version.
	 * @throws Exception
	 */
	private String getCurrentVersion() {

		// TODO Get the current version from the configuration.
		return "1.0";
	}

	/**
	 * Get the REST service latest version.
	 * 
	 * @return REST service latest version.
	 */
	private String getLatestVersion() {

		// TODO Get the last version.
		return "1.1";
	}

	/**
	 * Check if the version {@link String} correspond to the pattern of a version (e.g. 1.0).
	 * @param version The string to check.
	 * @return True if it correspond to a version pattern or to 'current' or to 'latest'. It returns false otherwise.
	 */
	private boolean isVersionPattern(String version) {

		if (version != null && !version.isEmpty()
				&& (version.equals("current") || version.equals("latest") || version.matches("[0-9]*.[0-9]*")))
			return true;

		return false;

	}

	/* OTHER PAGES */

	/**
	 * Welcome page of the REST service documentation for a specified version.
	 * @return The welcome page.
	 */
	@GET
	@Path("{restServiceVersion}/welcome")
	@Transactional
	public String readWelcome(@PathParam("restServiceVersion") String restServiceVersion) {

		// TODO change this to the welcome page of the documentation
		String welcome = "";

		welcome += "<html><head></head><body>";
		welcome += "<p>Welcome to the Data Governance Center REST service.</p>";
		welcome += "<p>We are still hard at work, expect more soon !</p>";

		if (isVersionPattern(restServiceVersion))
			welcome += "<p>Version: " + restServiceVersion + "</p>";
		// TODO throw an unknown version error
		else
			throw new NotImplementedException("This is not a valid version");

		welcome += "</body></html>";

		return welcome;
	}

	/**
	 * Welcome page of the REST service documentation for the user (current) version.
	 * @return {@link Response}
	 */
	@GET
	@Path("/welcome")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	@Transactional
	public Response readWelcomeCurrentVersion() throws Exception {
		return redirectToVersionedPage("current", "welcome");
	}

	/**
	 * About page.
	 * 
	 * @return The about page.
	 * @throws Exception
	 */
	@GET
	@Path("/about")
	@Produces({ MediaType.APPLICATION_XHTML_XML })
	@Transactional
	public Response readAbout() throws Exception {
		return redirectToVersionedPage("current", "about");
	}

	/**
	 * About page.
	 * 
	 * @return The about page.
	 */
	@GET
	@Path("/{restServiceVersion}/about")
	@Transactional
	public String readAboutVersion(@PathParam("restServiceVersion") String restServiceVersion) {
		return "<html><header><title>DGC about</title></head>"
				+ "<body><p><b>About:</b></p><p>Data Governance Center version " + applicationComponent.getVersion()
				+ " (build:" + applicationComponent.getBuildNumber() + ")</p>" + "<p>REST service version "
				+ restServiceVersion + " </body></html>";
	}
}
