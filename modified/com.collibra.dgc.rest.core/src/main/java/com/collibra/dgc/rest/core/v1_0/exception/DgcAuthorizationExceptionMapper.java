package com.collibra.dgc.rest.core.v1_0.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.rest.core.v1_0.builder.RestExceptionBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.AuthorizationException;

/**
 * Mapper for {@link com.collibra.dgc.core.exceptions.AuthorizationException} with HTTP 401 (unauthorized).
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class DgcAuthorizationExceptionMapper implements
		ExceptionMapper<com.collibra.dgc.core.exceptions.AuthorizationException> {

	@Context
	private HttpHeaders httpHeaders;

	@Context
	private HttpServletRequest req;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(com.collibra.dgc.core.exceptions.AuthorizationException exception) {

		AuthorizationException ex = builder.buildAuthorizationException(exception, req);

		return Response.status(Response.Status.UNAUTHORIZED).entity(ex)
				.type(builder.getMediaTypeForResponse(httpHeaders)).build();
	}
}
