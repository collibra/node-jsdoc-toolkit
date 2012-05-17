package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * Represents a phone number
 * @author GKDAI63
 * 
 */
public interface PhoneNumber extends Resource {

	/**
	 * sSets the phone number
	 * @param number the number
	 */
	public abstract void setPhoneNumber(String number);

	/**
	 * Sets the {@link PhoneType}
	 * @param phoneType the phone type
	 */
	public abstract void setPhoneType(PhoneType phoneType);

	/**
	 * Retrieves the phone number
	 * @return the number
	 */
	public abstract String getPhoneNumber();

	/**
	 * Retreievs the {@link PhoneType}
	 * @return the {@link PhoneType}
	 */
	public abstract PhoneType getPhoneType();

}