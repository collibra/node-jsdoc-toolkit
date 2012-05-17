package com.collibra.dgc.rest.core.v1_0.resource;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.component.UserComponent;

/**
 * Test for file upload
 * @author pmalarme
 * 
 */
@Component
@Path("/1.0/upload")
public class FileUploadResource {

	@Autowired
	private UserComponent userComponent;

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadFile(@Context HttpServletRequest req) {

		if (ServletFileUpload.isMultipartContent(req)) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = null;
			try {
				items = upload.parseRequest(req);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

			if (items != null) {

				for (FileItem item : items) {

					if (!item.isFormField() && item.getSize() > 0) {

						System.out.println("Mime type: " + item.getContentType());
						System.out.print("Current user: " + userComponent.getCurrentUser().getUserName());

						String fileName = processFileName(item.getName());

						try {

							item.write(new File(Application.ATTACHMENTS_DIR, fileName));

						} catch (Exception e) {

							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (item.isFormField()) {

						System.out.println(item.getFieldName() + ": " + item.getString());
					}
				}
			}

			return Response.ok().build();
		}

		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	private String processFileName(String fileName) {
		String slashType = (fileName.lastIndexOf("\\") > 0) ? "\\" : "/";
		return fileName.substring(fileName.lastIndexOf(slashType) + 1, fileName.length());
	}
}
