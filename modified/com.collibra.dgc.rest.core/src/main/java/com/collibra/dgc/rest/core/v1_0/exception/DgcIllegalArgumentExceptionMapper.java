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
import com.collibra.dgc.rest.core.v1_0.dto.IllegalArgumentException;

/**
 * Mapper for {@link com.collibra.dgc.core.exceptions.EntityNotFoundException} with HTTP 400 (bad request).
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class DgcIllegalArgumentExceptionMapper implements
		ExceptionMapper<com.collibra.dgc.core.exceptions.IllegalArgumentException> {

	@Context
	private HttpHeaders httpHeaders;

	@Context
	private HttpServletRequest req;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(com.collibra.dgc.core.exceptions.IllegalArgumentException exception) {

		IllegalArgumentException ex = builder.buildIllegalArgumentException(exception, req);

		return Response.status(Response.Status.BAD_REQUEST).entity(ex)
				.type(builder.getMediaTypeForResponse(httpHeaders)).build();
	}
}