/**
 * 
 */
package com.collibra.dgc.core.model.user;

/**
 * The generic interface for user data. Multiple implementation make sure the data can come from different places.
 * @author dieterwachters
 */
public interface UserData {
	public String getId();

	public String getUserName();

	public String getFirstName();

	public String getLastName();

	public String getEmailAddress();

	public String getLanguage();
}
