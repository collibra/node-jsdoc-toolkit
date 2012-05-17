/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.user.Address;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.Email;
import com.collibra.dgc.core.model.user.InstantMessagingAccount;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.PhoneNumber;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.UserFactory;
import com.collibra.dgc.core.model.user.Website;
import com.collibra.dgc.core.model.user.WebsiteType;
import com.collibra.dgc.core.model.user.impl.AddressImpl;
import com.collibra.dgc.core.model.user.impl.EmailImpl;
import com.collibra.dgc.core.model.user.impl.InstantMessagingAccountImpl;
import com.collibra.dgc.core.model.user.impl.PhoneNumberImpl;
import com.collibra.dgc.core.model.user.impl.WebsiteImpl;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.UserEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.service.license.LicenseService;

import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.UsernameRule;

/**
 * @author dieterwachters
 * 
 */
@Service
public class UserServiceImpl implements UserService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserFactory userFactory;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private RightsService rightsService;
	@Autowired
	private LicenseService licenseService;

	private PasswordValidator passwordValidator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#getUser(java.lang.String)
	 */
	@Override
	public UserData getUser(String userName) {
		return userDao.findByUserName(userName);
	}

	@Override
	public UserData getUserWithError(String userName) {
		final UserData user = userDao.findByUserName(userName);

		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#addUser(java.lang.String, java.lang.String)
	 */
	@Override
	public UserData addUser(String userName, String password) {
		return addUser(userName, password, null, null, null);
	}

	@Override
	public boolean isPublicRegistrationAllowed() {
		return configurationService.getBoolean("/core/security/public-registration")
				&& licenseService.isGuestAccessAllowed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#addUser(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public UserData addUser(String userName, String password, String firstName, String lastName, String email) {
		if (!isPublicRegistrationAllowed()) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.PUBLIC_REGISTRATIONS_NOT_ALLOWED);
		}
		final UserData existing = getUser(userName);
		if (existing != null) {
			throw new IllegalArgumentException(DGCErrorCodes.USERNAME_ALREADY_EXISTS, userName);
		}
		validatePassword(password, userName);

		User user = userFactory.createUser(userName, password, firstName, lastName, email);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.ADDING));

		user = userDao.save(user);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.ADDED));

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#removeUser(java.lang.String)
	 */
	@Override
	public void removeUser(String userName) {
		final User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN, DGCErrorCodes.REMOVE_USER);
		}

		// Check if there are still other users with the admin rights.
		final Collection<Role> roles = rightsService.findRoles(Permissions.ADMIN);
		boolean foundOthers = false;
		outer: for (final Role role : roles) {
			final Collection<Member> members = rightsService.findMembersWithRole(role);
			for (final Member toCheck : members) {
				if (!toCheck.getOwnerId().equals(user.getId())) {
					foundOthers = true;
					break outer;
				}
			}
		}
		if (!foundOthers) {
			throw new IllegalArgumentException(DGCErrorCodes.LAST_USER_WITH_ADMIN_RIGHTS, userName);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.REMOVING));

		userDao.remove(user);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.REMOVED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#changeUser(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public UserData changeUser(String userName, String firstName, String lastName, String email) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN, DGCErrorCodes.CHANGE_USER);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));

		user.setEmailAddress(email);
		user.setLastName(lastName);
		user.setFirstName(firstName);
		user = userDao.save(user);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public UserData changePassword(String userName, String password) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.CHANGE_PASSWORD);
		}

		validatePassword(password, userName);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));

		user.setPassword(password);
		user = userDao.save(user);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));

		return user;
	}

	@Override
	public UserData changeLanguage(String userName, String langauge) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));

		user.setLanguage(langauge);
		user = userDao.save(user);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));

		return user;
	}

	/**
	 * Will validate the password given the rules specified in the configuration.
	 */
	private final void validatePassword(final String password, final String username) {

		final PasswordData passwordData = new PasswordData(new Password(password));
		passwordData.setUsername(username);

		final RuleResult result = passwordValidator.validate(passwordData);
		if (!result.isValid()) {
			StringBuilder buf = new StringBuilder();
			for (String msg : passwordValidator.getMessages(result)) {
				buf.append(msg).append("\r\n");
			}
			throw new IllegalArgumentException(DGCErrorCodes.PASSWORD_TOO_WEAK);
		}
	}

	private String getCurrentUserName() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() == null) {
			return Constants.GUEST_USER;
		}
		return currentUser.getPrincipal().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#getCurrentUser()
	 */
	@Override
	public UserData getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() != null) {
			final String userName = currentUser.getPrincipal().toString();
			return getUser(userName);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		final int min = configurationService.getInteger("core/security/password/minimum-length");
		final int max = configurationService.getInteger("core/security/password/maximum-length");
		final boolean digitRequired = configurationService.getBoolean("core/security/password/digit-required");
		final boolean upperCaseRequired = configurationService.getBoolean("core/security/password/uppercase-required");
		final boolean lowerCaseRequired = configurationService.getBoolean("core/security/password/lowercase-required");
		final boolean nonAlphanumericRequred = configurationService
				.getBoolean("core/security/password/non-alphanumeric-required");
		final boolean usernameDisallowed = configurationService
				.getBoolean("core/security/password/username-disallowed");

		final List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(new LengthRule(min, max));

		if (digitRequired || nonAlphanumericRequred || upperCaseRequired || lowerCaseRequired) {
			CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
			if (digitRequired) {
				charRule.getRules().add(new DigitCharacterRule(1));
			}
			if (nonAlphanumericRequred) {
				charRule.getRules().add(new NonAlphanumericCharacterRule(1));
			}
			if (upperCaseRequired) {
				charRule.getRules().add(new UppercaseCharacterRule(1));
			}
			if (lowerCaseRequired) {
				charRule.getRules().add(new LowercaseCharacterRule(1));
			}
			charRule.setNumberOfCharacteristics(charRule.getRules().size());
			ruleList.add(charRule);
		}

		if (usernameDisallowed) {
			ruleList.add(new UsernameRule(true, true));
		}

		passwordValidator = new PasswordValidator(ruleList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#logout()
	 */
	@Override
	public void logout() {
		final Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.UserService#findUsers(com.collibra.dgc.core.dto.filters.ResourceFilter, int,
	 * int)
	 */
	@Override
	public Collection<UserData> findUsers(ResourceFilter filter, int offset, int max) {
		return userDao.findUsers(filter, offset, max);
	}

	@Override
	public UserData addAditionalEmailaddress(String userName, String email) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		user.getAditionalEmailAddresses().add(new EmailImpl(email));

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData removeAditionalEmailaddress(String userName, String rId) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		EmailImpl email = new EmailImpl();
		email.setResourceId(rId);

		if (!user.getAditionalEmailAddresses().remove(email)) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_ADDITIONAL_EMAIL_NOT_FOUND, rId);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData changeAdditionalEmailaddress(String userName, String rId, String email) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		Email toChange = null;

		for (Email mail : user.getAditionalEmailAddresses()) {
			if (mail.getId().equals(rId)) {
				toChange = mail;
				break;
			}
		}
		if (toChange == null) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_ADDITIONAL_EMAIL_NOT_FOUND, rId);
		}
		toChange.setEmailAddress(email);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData addPhone(String userName, String phone, PhoneType phoneType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		user.getPhoneNumbers().add(new PhoneNumberImpl(phone, phoneType));

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;

	}

	@Override
	public UserData removePhone(String userName, String rId) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		PhoneNumberImpl phone = new PhoneNumberImpl();
		phone.setResourceId(rId);
		if (!user.getPhoneNumbers().remove(phone)) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_PHONE_NOT_FOUND, rId);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData changePhone(String userName, String rId, String phone, PhoneType phoneType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		PhoneNumber toChange = null;

		for (PhoneNumber phoneNumber : user.getPhoneNumbers()) {
			if (phoneNumber.getId().equals(rId)) {
				toChange = phoneNumber;
				break;
			}
		}
		if (toChange == null) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_PHONE_NOT_FOUND, rId);
		}
		if (phone != null)
			toChange.setPhoneNumber(phone);
		if (phoneType != null)
			toChange.setPhoneType(phoneType);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData addInstantMessagingAccount(String userName, String account, InstantMessagingAccountType accountType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		user.getInstantMessagingAccounts().add(new InstantMessagingAccountImpl(account, accountType));

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;

	}

	@Override
	public UserData removeInstantMessagingAccount(String userName, String rId) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		InstantMessagingAccountImpl ima = new InstantMessagingAccountImpl();
		ima.setResourceId(rId);
		if (!user.getInstantMessagingAccounts().remove(ima))
			throw new EntityNotFoundException(DGCErrorCodes.USER_IM_NOT_FOUND, rId);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData changeInstantMessagingAccount(String userName, String rId, String account,
			InstantMessagingAccountType accountType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		InstantMessagingAccount toChange = null;

		for (InstantMessagingAccount ima : user.getInstantMessagingAccounts()) {
			if (ima.getId().equals(rId)) {
				toChange = ima;
				break;
			}
		}

		if (toChange == null) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_IM_NOT_FOUND, rId);
		}

		if (account != null)
			toChange.setAccount(account);

		if (accountType != null)
			toChange.setAccountType(accountType);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData addWebsite(String userName, String url, WebsiteType websiteType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		user.getWebsites().add(new WebsiteImpl(url, websiteType));

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;

	}

	@Override
	public UserData removeWebsite(String userName, String rId) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		WebsiteImpl site = new WebsiteImpl();
		site.setResourceId(rId);
		if (!user.getWebsites().remove(site)) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_WEBSITE_NOT_FOUND, rId);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData changeWebsite(String userName, String rId, String url, WebsiteType websiteType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		Website toChange = null;

		for (Website website : user.getWebsites()) {
			if (website.getId().equals(rId)) {
				toChange = website;
				break;
			}
		}
		if (toChange == null) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_WEBSITE_NOT_FOUND, rId);
		}
		if (url != null)
			toChange.setUrl(url);

		if (websiteType != null)
			toChange.setWebsiteType(websiteType);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData addAddress(String userName, String city, String street, String number, String province,
			String country, AddressType addressType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		user.getAddresses().add(new AddressImpl(city, street, number, province, country, addressType));

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;

	}

	@Override
	public UserData removeAddress(String userName, String rid) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}

		AddressImpl address = new AddressImpl();
		address.setResourceId(rid);
		if (!user.getAddresses().remove(address)) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_ADDRESS_NOT_FOUND, rid);
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public UserData changeAddress(String userName, String rid, String city, String street, String number,
			String province, String country, AddressType addressType) {
		User user = userDao.findByUserName(userName);
		if (user == null) {
			String message = "User with user name '" + userName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.USER_OR_GROUP_NOT_FOUND_NAME, userName);
		}

		if (!userName.equals(getCurrentUserName())) {
			authorizationHelper.checkAuthorization(getCurrentUserName(), Permissions.ADMIN,
					DGCErrorCodes.GLOBAL_NO_PERMISSION);
		}
		Address toChange = null;

		for (Address address : user.getAddresses()) {
			if (address.getId().equals(rid)) {
				toChange = address;
				break;
			}
		}
		if (toChange == null) {
			throw new EntityNotFoundException(DGCErrorCodes.USER_ADDRESS_NOT_FOUND, rid);
		}
		if (addressType != null)
			toChange.setAddressType(addressType);
		if (city != null)
			toChange.setCity(city);
		if (street != null)
			toChange.setStreet(street);
		if (number != null)
			toChange.setNumber(number);
		if (country != null)
			toChange.setCountry(country);
		if (province != null)
			toChange.setProvince(province);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGING));
		userDao.save(user);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.USER,
				new UserEventData(user, EventType.CHANGED));
		return user;
	}

	@Override
	public User getUserById(String userRId) {
		return userDao.get(userRId);
	}

	@Override
	public User getUserByIdWithError(String userRId) {
		User u = getUserById(userRId);
		if (u == null)
			throw new EntityNotFoundException(DGCErrorCodes.USER_NOT_FOUND, "userRId");
		return u;
	}

}
