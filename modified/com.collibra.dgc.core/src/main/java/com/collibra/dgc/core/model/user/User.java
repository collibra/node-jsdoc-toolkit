/**
 * 
 */
package com.collibra.dgc.core.model.user;

import java.util.List;

/**
 * Represents a user in the system with all its information stored in the database.
 * @author dieterwachters
 */
public interface User extends UserData {

	void setUserName(String userName);

	void setFirstName(String firstName);

	void setLastName(String lastName);

	void setEmailAddress(String emailAddress);

	void setPhoneNumbers(List<PhoneNumber> phoneNumbers);

	void setPassword(String password);

	void setLanguage(String langauge);

	void setInstantMessagingAccounts(List<InstantMessagingAccount> instantMessagingAccounts);

	void setAditionalEmailAddresses(List<Email> aditionalEmailAddresses);

	void setWebsites(List<Website> websites);

	boolean checkPassword(String password);

	String getPasswordHash();

	String getSalt();

	List<PhoneNumber> getPhoneNumbers();

	List<InstantMessagingAccount> getInstantMessagingAccounts();

	List<Website> getWebsites();

	List<Email> getAditionalEmailAddresses();

	List<Address> getAddresses();

}
