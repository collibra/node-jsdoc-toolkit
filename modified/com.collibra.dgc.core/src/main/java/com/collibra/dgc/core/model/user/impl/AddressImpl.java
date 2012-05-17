package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Address;
import com.collibra.dgc.core.model.user.AddressType;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "ADDRESS")
public class AddressImpl extends ResourceImpl implements Address {
	private String city;
	private String street;
	private String number;
	private String province;
	private String country;
	private AddressType addressType;

	public AddressImpl() {
	}

	public AddressImpl(String city, String street, String number, String province, String country,
			AddressType addressType) {
		setCity(city);
		setStreet(street);
		setNumber(number);
		setProvince(province);
		setCountry(country);
		setAddressType(addressType);
	}

	@Override
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	@Column(name = "STREET")
	public String getStreet() {
		return street;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	@Column(name = "NR")
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	@Override
	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	@Column(name = "COUNTRY")
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	@Column(name = "ADDRESS_TYPE")
	public AddressType getAddressType() {
		return addressType;
	}

	@Override
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

}
