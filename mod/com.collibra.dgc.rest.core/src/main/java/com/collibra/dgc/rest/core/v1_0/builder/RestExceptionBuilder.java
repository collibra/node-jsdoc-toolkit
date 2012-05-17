package com.collibra.dgc.rest.core.v1_0.builder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.i18n.I18nComponent;
import com.collibra.dgc.rest.core.v1_0.dto.AuthorizationException;
import com.collibra.dgc.rest.core.v1_0.dto.DgcException;
import com.collibra.dgc.rest.core.v1_0.dto.EntityNotFoundException;
import com.collibra.dgc.rest.core.v1_0.dto.ErrorParams;
import com.collibra.dgc.rest.core.v1_0.dto.Exception;
import com.collibra.dgc.rest.core.v1_0.dto.IllegalArgumentException;
import com.collibra.dgc.rest.core.v1_0.dto.LicenseKeyException;
import com.collibra.dgc.rest.core.v1_0.dto.NotFoundException;
import com.collibra.dgc.rest.core.v1_0.dto.ObjectFactory;
import com.collibra.dgc.rest.core.v1_0.dto.Permissions;

/**
 * @author pmalarme
 * 
 */
@Service
public class RestExceptionBuilder {

	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private I18nComponent i18nComponent;

	/* EXCEPTION */

	private void setExceptionAttribute(Exception ex, java.lang.Exception exception) {

		ex.setMessage(exception.getLocalizedMessage());
		ex.setStackTrace(ExceptionUtils.getStackTrace(exception));
	}

	public Exception buildException(java.lang.Exception exception) {

		Exception ex = factory.createException();

		setExceptionAttribute(ex, exception);

		return ex;
	}

	/* JERSEY EXCEPTION */

	public NotFoundException buildNotFoundException(com.sun.jersey.api.NotFoundException exception) {

		NotFoundException ex = factory.createNotFoundException();

		setExceptionAttribute(ex, exception);

		ex.setUri(exception.getNotFoundUri().toString());

		return ex;
	}

	/* DGC EXCEPTION */

	private void setDgcExceptionAttribute(DgcException ex, com.collibra.dgc.core.exceptions.DGCException exception,
			HttpServletRequest req) {

		setExceptionAttribute(ex, exception);

		ex.setErrorCode(exception.getErrorCode());
		ex.setMessage(exception.getLocalizedMessage(i18nComponent, req));

		// Set the parameters
		ErrorParams errorParams = factory.createErrorParams();
		List<String> paramList = errorParams.getParams();

		for (Object param : exception.getParams())
			paramList.add(param.toString());

		ex.setParams(errorParams);
	}

	public DgcException buildDgcException(com.collibra.dgc.core.exceptions.DGCException exception,
			HttpServletRequest req) {

		DgcException ex = factory.createDgcException();

		setDgcExceptionAttribute(ex, exception, req);

		return ex;
	}

	public IllegalArgumentException buildIllegalArgumentException(
			com.collibra.dgc.core.exceptions.IllegalArgumentException exception, HttpServletRequest req) {

		IllegalArgumentException ex = factory.createIllegalArgumentException();

		setDgcExceptionAttribute(ex, exception, req);

		return ex;
	}

	public EntityNotFoundException buildEntityNotFoundException(
			com.collibra.dgc.core.exceptions.EntityNotFoundException exception, HttpServletRequest req) {

		EntityNotFoundException ex = factory.createEntityNotFoundException();

		setDgcExceptionAttribute(ex, exception, req);

		return ex;
	}

	public AuthorizationException buildAuthorizationException(
			com.collibra.dgc.core.exceptions.AuthorizationException exception, HttpServletRequest req) {

		AuthorizationException ex = factory.createAuthorizationException();

		setDgcExceptionAttribute(ex, exception, req);

		ex.setUserName(exception.getUserName());

		// Set permissions
		Permissions permissions = factory.createPermissions();
		List<String> permissionList = permissions.getPermissions();

		for (String permission : exception.getPermissions())
			permissionList.add(permission);

		ex.setPermissions(permissions);

		return ex;
	}

	public LicenseKeyException buildLicenseKeyException(com.collibra.dgc.core.exceptions.LicenseKeyException exception,
			HttpServletRequest req) {

		LicenseKeyException ex = factory.createLicenseKeyException();

		setDgcExceptionAttribute(ex, exception, req);

		return ex;
	}

	/* UTILS */

	public MediaType getMediaTypeForResponse(HttpHeaders httpHeaders) {

		List<MediaType> acceptableMediaTypes = httpHeaders.getAcceptableMediaTypes();

		// By default, it return 'application/xml'
		if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE))
			return MediaType.APPLICATION_JSON_TYPE;
		else
			return MediaType.APPLICATION_XML_TYPE;
	}
}
