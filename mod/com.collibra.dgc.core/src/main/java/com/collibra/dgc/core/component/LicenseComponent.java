package com.collibra.dgc.core.component;

import java.io.InputStream;
import java.util.Date;

import com.collibra.dgc.core.exceptions.LicenseKeyException;

/**
 * Component to check license details and set new licenses.
 * @author GKDAI63
 * 
 */
public interface LicenseComponent {
	/**
	 * retrieves the maximum amount of concurrent users
	 * @return the maximum amount of concurrent users.
	 */
	public Integer getMaximumConcurrentUsers();

	/**
	 * Retrieves whether or not the license which is being used is valid
	 * @return validity of the license
	 */
	public Boolean hasValidLicense();

	/**
	 * Retrieves the date on which the current license expires
	 * @return the expiration date
	 */
	public Date getExpirationDate();

	/**
	 * Retrieves whether or not guest access is allowed
	 * @return guest access allowed or not
	 */
	public Boolean isGuestAccessAllowed();

	/**
	 * Sets a new license
	 * 
	 * @param is the license
	 * @throws LicenseKeyException when license is invalid or expired
	 */
	public void setLicense(InputStream is) throws LicenseKeyException;
}
