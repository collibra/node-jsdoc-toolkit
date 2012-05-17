package com.collibra.dgc.core.component.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.ConfigurationComponent;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.util.Defense;

@Component
public class ConfigurationComponentImpl implements ConfigurationComponent {

	private final Logger log = LoggerFactory.getLogger(ConfigurationComponentImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private AuthorizationHelper authorizationHelper;

	@Override
	public String getString(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();
		return configurationService.getString(path);
	}

	@Override
	public String getString(String path, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkResourcePermission(rId);
		return configurationService.getString(path, rId, null);
	}

	@Override
	@Transactional
	public String getString(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getString(path, rId);

		UserData user = userService.getUser(userName);
		if (user != null)
			checkUserPermission(user);
		return configurationService.getString(path, rId, user);
	}

	@Override
	@Transactional
	public String getLocalizedString(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getString(path, rId);

		UserData user = userService.getUser(userName);
		if (user != null)
			checkUserPermission(user);

		return configurationService.getLocalizedString(path, rId, user);
	}

	@Override
	public void clear(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();
		configurationService.clear(path);
		configurationService.flush();
	}

	@Override
	@Transactional
	public void clear(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		configurationService.clear(path, rId, user);
		configurationService.flush();
	}

	@Override
	@Transactional
	public void setProperty(String path, Object value) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();

		configurationService.setProperty(path, value);
		configurationService.flush();
	}

	@Override
	@Transactional
	public void setProperty(String path, Object value, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (rId == null)
			setProperty(path, value);

		checkResourcePermission(rId);

		configurationService.setProperty(path, value, rId);
		configurationService.flush();
	}

	@Override
	@Transactional
	public void setProperty(String path, Object value, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null || userName.isEmpty()) {
			setProperty(path, value, rId);
		} else {
			UserData user = userService.getUserWithError(userName);
			checkUserPermission(user);

			configurationService.setProperty(path, value, rId, user);
			configurationService.flush();
		}
	}

	@Override
	@Transactional
	public Collection<String> getStringList(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();

		return configurationService.getStringList(path);
	}

	@Override
	@Transactional
	public Collection<String> getStringList(String path, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkResourcePermission(rId);

		return configurationService.getStringList(path, rId, null);
	}

	@Override
	@Transactional
	public Collection<String> getStringList(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getStringList(path, rId, user);
	}

	@Override
	@Transactional
	public Collection<String> getLocalizedStringList(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getLocalizedStringList(path, rId, user);
	}

	@Override
	@Transactional
	public Integer getInteger(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();

		return configurationService.getInteger(path);
	}

	@Override
	@Transactional
	public Integer getInteger(String path, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkResourcePermission(rId);

		return configurationService.getInteger(path, rId, null);
	}

	@Override
	@Transactional
	public Integer getInteger(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getInteger(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getInteger(path, rId, user);
	}

	@Override
	@Transactional
	public Integer getLocalizedInteger(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getInteger(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getLocalizedInteger(path, rId, user);
	}

	@Override
	@Transactional
	public Boolean getBoolean(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		return configurationService.getBoolean(path);
	}

	@Override
	@Transactional
	public Boolean getBoolean(String path, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkResourcePermission(rId);

		return configurationService.getBoolean(path, rId, null);
	}

	@Override
	@Transactional
	public Boolean getBoolean(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getBoolean(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getBoolean(path, rId, user);
	}

	@Override
	@Transactional
	public Boolean getLocalizedBoolean(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getBoolean(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getLocalizedBoolean(path, rId, user);
	}

	@Override
	@Transactional
	public Map<String, String> getProperties(String path) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkAdminPermission();

		return configurationService.getProperties(path);
	}

	@Override
	@Transactional
	public Map<String, String> getProperties(String path, String rId) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");
		checkResourcePermission(rId);

		return configurationService.getProperties(path, rId, null);
	}

	@Override
	@Transactional
	public Map<String, String> getProperties(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getProperties(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getProperties(path, rId, user);
	}

	@Override
	@Transactional
	public Map<String, String> getLocalizedProperties(String path, String rId, String userName) {
		Defense.notEmpty(path, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY, "path");

		if (userName == null)
			return getProperties(path, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getLocalizedProperties(path, rId, user);
	}

	@Override
	public long getLastModified() {

		return configurationService.getLastModified();
	}

	@Override
	@Transactional
	public Map<String, String> getConfigurationSection(String startPath) {
		Defense.notEmpty(startPath, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY,
				"startPath");
		checkAdminPermission();

		return configurationService.getConfigurationSection(startPath);
	}

	@Override
	@Transactional
	public Map<String, String> getConfigurationSection(String startPath, String rId) {
		Defense.notEmpty(startPath, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY,
				"startPath");
		checkResourcePermission(rId);

		return configurationService.getConfigurationSection(startPath, rId, null);
	}

	@Override
	@Transactional
	public Map<String, String> getConfigurationSection(String startPath, String rId, String userName) {
		Defense.notEmpty(startPath, DGCErrorCodes.CONFIGURATION_PATH_NULL, DGCErrorCodes.CONFIGURATION_PATH_EMPTY,
				"startPath");

		if (userName == null)
			return getConfigurationSection(startPath, rId);

		UserData user = userService.getUser(userName);
		if(user != null)
			checkUserPermission(user);

		return configurationService.getConfigurationSection(startPath, rId, user);
	}

	protected void checkResourcePermission(String rId) {
		authorizationHelper.checkAuthorizationAtleastForOne(userService.getCurrentUser().getUserName(), new String[] {
			Permissions.CUSTOMIZE_CONFIG, Permissions.ADMIN }, DGCErrorCodes.CONFIGURATION_EDIT_NO_PERMISSION);

	}

	protected void checkUserPermission(UserData user) {
		if (user == null || !(user.equals(userService.getCurrentUser()) || authorizationService.isPermitted(Permissions.ADMIN)))
			throw new AuthorizationException(DGCErrorCodes.CONFIGURATION_EDIT_NO_PERMISSION, user.getUserName(),
					Permissions.CUSTOMIZE_CONFIG);

	}

	protected void checkAdminPermission() {
		authorizationHelper.checkAuthorization(userService.getCurrentUser().getUserName(), Permissions.ADMIN,
				DGCErrorCodes.CONFIGURATION_EDIT_NO_PERMISSION);
	}
}
