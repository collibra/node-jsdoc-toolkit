package com.collibra.dgc.rest.core.v1_0.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.rest.core.v1_0.builder.RestExceptionBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Exception;

/**
 * Mapper for generic exception.
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<java.lang.Exception> {

	@Context
	private HttpHeaders httpHeaders;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(java.lang.Exception exception) {

		Exception ex = builder.buildException(exception);

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex)
				.type(builder.getMediaTypeForResponse(httpHeaders)).build();
	}

}
