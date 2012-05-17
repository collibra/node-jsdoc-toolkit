package com.collibra.dgc.rest.core.v1_0.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.component.LicenseComponent;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.LicenseKeyException;

/**
 * Jersey controller for uploading a license
 * @author GKDAI63
 * 
 */
@Component
@Path("/1.0/license")
public class LicenseResource {

	@Autowired
	LicenseComponent licenseComponent;

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response changeLicense(@Context HttpServletRequest req) {

		if (ServletFileUpload.isMultipartContent(req)) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try {
				items = upload.parseRequest(req);
			} catch (FileUploadException e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}

			if (items != null) {

				for (FileItem item : items) {

					if (!item.isFormField() && item.getSize() > 0) {

						try {
							licenseComponent.setLicense(item.getInputStream());
						} catch (LicenseKeyException ex) {
							return Response.status(Status.FORBIDDEN).build();
						} catch (DGCException ex) {
							return Response.status(Status.INTERNAL_SERVER_ERROR).build();
						} catch (IOException ex) {
							return Response.status(Status.INTERNAL_SERVER_ERROR).build();
						}
					}
				}
			}

			return Response.ok().build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
}
