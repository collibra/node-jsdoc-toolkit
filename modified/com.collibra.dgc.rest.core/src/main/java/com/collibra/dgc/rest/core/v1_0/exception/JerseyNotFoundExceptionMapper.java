package com.collibra.dgc.rest.core.v1_0.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.rest.core.v1_0.builder.RestExceptionBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.NotFoundException;

/**
 * Mapper for {@link com.sun.jersey.api.NotFoundException}.
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class JerseyNotFoundExceptionMapper implements ExceptionMapper<com.sun.jersey.api.NotFoundException> {

	@Context
	private HttpHeaders httpHeaders;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(com.sun.jersey.api.NotFoundException exception) {

		NotFoundException ex = builder.buildNotFoundException(exception);

		return Response.status(Response.Status.NOT_FOUND).entity(ex).type(builder.getMediaTypeForResponse(httpHeaders))
				.build();
	}

}
