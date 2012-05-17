package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * A representation for a {@link User}'s Address
 * @author GKDAI63
 * 
 */
public interface Address extends Resource {

	/**
	 * Sets the {@link AddressType}
	 * @param addressType
	 */
	public abstract void setAddressType(AddressType addressType);

	/**
	 * Retrieves the {@link AddressType}
	 * @return the {@link AddressType}
	 */
	public abstract AddressType getAddressType();

	/**
	 * Sets the country
	 * @param country
	 */
	public abstract void setCountry(String country);

	/**
	 * Retrieves the country
	 * @return the country
	 */
	public abstract String getCountry();

	/**
	 * Sets the province or state
	 * @param province
	 */
	public abstract void setProvince(String province);

	/**
	 * Retrieves the province or state
	 * @return the province or state
	 */
	public abstract String getProvince();

	/**
	 * Sets the house number
	 * @param number
	 */
	public abstract void setNumber(String number);

	/**
	 * retrieves the house number
	 * @return the number
	 */
	public abstract String getNumber();

	/**
	 * Sets the street
	 * @param street
	 */
	public abstract void setStreet(String street);

	/**
	 * Retrieves the street
	 * @return the street
	 */
	public abstract String getStreet();

	/**
	 * sets the city
	 * @param city
	 */
	public abstract void setCity(String city);

	/**
	 * retrieves the city
	 * @return the city
	 */
	public abstract String getCity();

}
