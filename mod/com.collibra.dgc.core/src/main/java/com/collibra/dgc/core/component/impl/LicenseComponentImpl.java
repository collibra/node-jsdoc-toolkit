package com.collibra.dgc.core.component.impl;

import java.io.InputStream;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.LicenseComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.LicenseKeyException;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.license.LicenseService;

/**
 * Component to handle licensing.
 * @author GKDAI63
 * 
 */
@Service
public class LicenseComponentImpl implements LicenseComponent {

	@Autowired
	private LicenseService licenseService;

	@Autowired
	private AuthorizationHelper authorizationHelper;

	private static final Logger log = LoggerFactory.getLogger(LicenseComponentImpl.class);

	@Override
	public Integer getMaximumConcurrentUsers() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_LICENSING);
		return licenseService.getConcurrentUserCount();
	}

	@Override
	public Boolean hasValidLicense() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_LICENSING);
		return licenseService.isValid();
	}

	@Override
	public Date getExpirationDate() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_LICENSING);
		return licenseService.getExpireDate();
	}

	@Override
	public Boolean isGuestAccessAllowed() {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_LICENSING);
		return licenseService.isGuestAccessAllowed();
	}

	@Override
	@Transactional
	public void setLicense(InputStream is) throws LicenseKeyException {
		authorizationHelper.checkAuthorization(SecurityUtils.getSubject().getPrincipal().toString(), Permissions.ADMIN,
				DGCErrorCodes.NO_PERMISSION_TO_ACCESS_LICENSING);
		licenseService.setLicense(is);
	}

}
