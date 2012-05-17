/**
 * 
 */
package com.collibra.dgc.core.model.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Address;
import com.collibra.dgc.core.model.user.Email;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.InstantMessagingAccount;
import com.collibra.dgc.core.model.user.PhoneNumber;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.Website;
import com.collibra.dgc.core.util.Defense;
import com.collibra.dgc.core.util.HashUtil;

/**
 * @author dieterwachters
 * @author GKDAI63
 */
@Entity
@Audited
@Table(name = "USERS")
public class UserImpl extends ResourceImpl implements User, UserData {
	private String userName;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String passwordHash;
	private String salt;
	private String language;
	private List<Email> aditionalEmailAddresses;
	private List<PhoneNumber> phoneNumbers;
	private List<InstantMessagingAccount> instantMessagingAccounts;
	private List<Website> websites;
	private List<Address> addresses;
	private List<Group> groups;

	protected UserImpl() {
	}

	public UserImpl(String userName, String password, String firstName, String lastName, String email) {
		setUserName(userName);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setEmailAddress(email);
	}

	@Override
	@Column(name = "USERNAME", unique = true, nullable = false)
	public String getUserName() {
		return userName;
	}

	@Override
	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return firstName;
	}

	@Override
	@Column(name = "LASTNAME")
	public String getLastName() {
		return lastName;
	}

	@Override
	@Column(name = "EMAIL")
	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	@Column(name = "LANGUAGE", nullable = true)
	public String getLanguage() {
		return language;
	}

	@Override
	@Column(name = "password")
	public String getPasswordHash() {
		return passwordHash;
	}

	protected void setPasswordHash(final String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	@Column(name = "salt")
	public String getSalt() {
		return salt;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EmailImpl.class)
	@Override
	public List<Email> getAditionalEmailAddresses() {
		return aditionalEmailAddresses;
	}

	@Override
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = PhoneNumberImpl.class)
	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	@Override
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = InstantMessagingAccountImpl.class)
	public List<InstantMessagingAccount> getInstantMessagingAccounts() {
		return instantMessagingAccounts;
	}

	@Override
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = WebsiteImpl.class)
	public List<Website> getWebsites() {
		return websites;
	}

	@Override
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AddressImpl.class)
	public List<Address> getAddresses() {
		return addresses;
	}

	protected void setSalt(final String salt) {
		this.salt = salt;
	}

	@Override
	public void setPassword(final String password) {
		Defense.notEmpty(password, DGCErrorCodes.PASSWORD_NULL, DGCErrorCodes.PASSWORD_EMPTY, "password");
		final String[] result = HashUtil.hash(password);
		setPasswordHash(result[0]);
		setSalt(result[1]);
	}

	@Override
	public boolean checkPassword(String password) {
		final String[] result = HashUtil.hash(password, salt);
		return result[0].equals(password);
	}

	/**
	 * @param userName the userName to set
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param language the preferred language of this user.
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @param firstName the firstName to set
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	@Override
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public void setAditionalEmailAddresses(List<Email> aditionalEmailAddresses) {
		this.aditionalEmailAddresses = aditionalEmailAddresses;
	}

	@Override
	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	@Override
	public void setInstantMessagingAccounts(List<InstantMessagingAccount> instantMessagingAccounts) {
		this.instantMessagingAccounts = instantMessagingAccounts;
	}

	@Override
	public void setWebsites(List<Website> websites) {
		this.websites = websites;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	protected void initializeRelations() {
		aditionalEmailAddresses = new ArrayList<Email>();
		websites = new ArrayList<Website>();
		phoneNumbers = new ArrayList<PhoneNumber>();
		instantMessagingAccounts = new ArrayList<InstantMessagingAccount>();
		groups = new ArrayList<Group>();
	}
}
