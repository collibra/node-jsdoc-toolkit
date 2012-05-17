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
import com.collibra.dgc.rest.core.v1_0.dto.EntityNotFoundException;

/**
 * Mapper for {@link com.collibra.dgc.core.exceptions.EntityNotFoundException} with HTTP 404 (not found).
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class DgcEntityNotFoundExceptionMapper implements
		ExceptionMapper<com.collibra.dgc.core.exceptions.EntityNotFoundException> {

	@Context
	private HttpHeaders httpHeaders;

	@Context
	private HttpServletRequest req;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(com.collibra.dgc.core.exceptions.EntityNotFoundException exception) {

		EntityNotFoundException ex = builder.buildEntityNotFoundException(exception, req);

		return Response.status(Response.Status.NOT_FOUND).entity(ex).type(builder.getMediaTypeForResponse(httpHeaders))
				.build();
	}

}