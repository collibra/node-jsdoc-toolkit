package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * Representation of an email address
 * @author GKDAI63
 * 
 */
public interface Email extends Resource {

	/**
	 * Sets the email address
	 * @param email
	 */
	public abstract void setEmailAddress(String email);

	/**
	 * Retrieves the email address
	 * @return
	 */
	public abstract String getEmailAddress();

}
