package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * A representation of an instant messaging account
 * @author GKDAI63
 * 
 */
public interface InstantMessagingAccount extends Resource {

	/**
	 * Sets the account name
	 * @param account
	 */
	void setAccount(String account);

	/**
	 * Sets the {@link InstantMessagingAccountType}
	 * @param accountType
	 */
	void setAccountType(InstantMessagingAccountType accountType);

	/**
	 * Retrieves the account
	 * @return the account
	 */
	String getAccount();

	/**
	 * Retrieves the {@link InstantMessagingAccountType}
	 * @return the {@link InstantMessagingAccountType}
	 */
	InstantMessagingAccountType getInstantMessagingAccountType();

}
