package com.collibra.dgc.core.service.license;

import java.io.InputStream;
import java.util.Date;

import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.LicenseKeyException;

/**
 * License validator
 * @author GKDAI63
 * 
 */
public interface LicenseService {

	public static final String PUBLIC_KEY_FILE = "/public.key";
	public static final String LICENSE_FILE = "/license.lic";

	public static final String CONCURRENT_USER_FEATURE = "NumberOfConcurrentUsers";
	public static final String EXPIRE_KEY = "Expire";
	public static final String GUEST_ACCESS = "GuestAccess";

	/**
	 * Validates the current used license, this method is used by a quartz cronjob
	 */
	void validateLicense();

	/**
	 * Sets the new license and deletes the old one if it's valid.
	 * @param license the license
	 * @throws DGCException when something goes wrong
	 * @throws LicenseKeyException when there are issues with the license
	 */
	void setLicense(InputStream license) throws DGCException, LicenseKeyException;

	public abstract boolean isValid();

	public abstract Boolean isGuestAccessAllowed();

	public abstract Date getExpireDate();

	public abstract Integer getConcurrentUserCount();

}