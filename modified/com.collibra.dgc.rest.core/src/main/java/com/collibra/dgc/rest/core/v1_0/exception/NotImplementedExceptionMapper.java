package com.collibra.dgc.rest.core.v1_0.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.rest.core.v1_0.builder.RestExceptionBuilder;
import com.collibra.dgc.rest.core.v1_0.dto.Exception;

/**
 * Mapper for {@link NotImplementedException}.
 * 
 * @author pmalarme
 * 
 */
@Component
@Provider
public class NotImplementedExceptionMapper implements ExceptionMapper<NotImplementedException> {

	@Context
	private HttpHeaders httpHeaders;

	@Autowired
	private RestExceptionBuilder builder;

	@Override
	public Response toResponse(NotImplementedException exception) {

		Exception ex = builder.buildException(exception);

		return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ex)
				.type(builder.getMediaTypeForResponse(httpHeaders)).build();
	}

}
