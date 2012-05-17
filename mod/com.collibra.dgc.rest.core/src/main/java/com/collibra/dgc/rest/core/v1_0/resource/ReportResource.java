package com.collibra.dgc.rest.core.v1_0.resource;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ReportComponent;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.builder.ReportConfigJSONBuilder;
import com.collibra.dgc.core.dto.reporting.builder.ReportJSONBuilder;

/**
 * Report resource of the REST service.
 * 
 * @author fvdmaele
 * 
 */
@Component
@Path("/1.0/report")
public class ReportResource {

	@Autowired
	private ReportComponent reportComponent;

	@Autowired
	private ReportJSONBuilder reportBuilder;

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

		throw new NotImplementedException("The documentation of the report REST service is not implemented yet.");
	}

	/* READ */

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@Path("/")
	public Response getReport(@QueryParam("config") String jsonConfig,
			@QueryParam("sEcho") @DefaultValue("1") int sEcho) {

		try {
			ReportConfig config = reportComponent.buiderReportConfigFromJSON(jsonConfig);
			Report report = reportComponent.buildReport(config);
			
			List<String> header = report.getHeader();
			JSONObject result = new JSONObject();
			result.put("sEcho", sEcho);
			result.put("iTotalRecords", report.size());
			result.put("iTotalDisplayRecords", report.getTotalSize());

			Collection<JSONObject> jsonEntries = new LinkedList<JSONObject>();

			for (Entry<String, LinkedList[]> entry : report.getEntries()) {
				JSONObject jsonEntry = new JSONObject();
				jsonEntries.add(jsonEntry);
				for (int i = 0; i < entry.getValue().length; i++) {
					String column = header.get(i);
					List values = entry.getValue()[i];
					if (!values.isEmpty())
						jsonEntry.put(column, entry.getValue()[i].get(0));
				}
			}

			result.put("aaData", jsonEntries);
			return Response.ok(result).build();
		} catch (org.codehaus.jettison.json.JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
