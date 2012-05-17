package com.collibra.dgc.rest.core.filter;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.service.license.impl.LicenseServiceImpl;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

/**
 * {@link ContainerRequestFilter} to check the license.
 * 
 * @author pmalarme
 * 
 */
public class LicenseContainerFilter implements ContainerRequestFilter {

	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {

		if (!LicenseServiceImpl.getInstance().isValid()
				&& !(containerRequest.getPath().endsWith("/license") && containerRequest.getMethod().equalsIgnoreCase(
						"POST")))
			throw new com.collibra.dgc.core.exceptions.LicenseKeyException(DGCErrorCodes.LICENSE_INVALID);

		return containerRequest;
	}
}
