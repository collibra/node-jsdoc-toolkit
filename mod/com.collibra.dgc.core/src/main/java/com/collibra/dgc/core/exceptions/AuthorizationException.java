package com.collibra.dgc.core.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.collibra.dgc.core.component.i18n.I18nComponent;

/**
 * Authorization exceptions are runtime exceptions thrown when user authorization fails capturing the information about
 * the failure.
 * 
 * @author amarnath
 * @author pmalarme
 */
public class AuthorizationException extends DGCException {

	private static final long serialVersionUID = 3810551155219855443L;

	private final String userName;
	private final String[] permissions;

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param ex The original exception
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permission The permission string
	 * @param resourceVerbalise The verbalise of the {@link Resource}
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(Exception ex, String errorCode, String userName, String permission,
			String resourceVerbalise, String resourceRId, Object... otherParams) {

		super(ex, errorCode, resourceVerbalise, resourceRId);

		if (otherParams != null) {

			params = new Object[otherParams.length + 2];

			params[0] = resourceVerbalise;
			params[1] = resourceRId;

			int i = 2;

			for (Object object : otherParams) {
				params[i] = object;
				i++;
			}

		}

		this.userName = userName;
		this.permissions = new String[] { permission };
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param ex The original exception
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permissions The permissions array
	 * @param resourceVerbalise The verbalise of the {@link Resource}
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(Exception ex, String errorCode, String userName, String[] permissions,
			String resourceVerbalise, String resourceRId, Object... otherParams) {

		super(ex, errorCode, resourceVerbalise, resourceRId);

		if (otherParams != null) {

			params = new Object[otherParams.length + 2];

			params[0] = resourceVerbalise;
			params[1] = resourceRId;

			int i = 2;

			for (Object object : otherParams) {
				params[i] = object;
				i++;
			}

		}

		this.userName = userName;
		this.permissions = permissions;
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permission The permission string
	 * @param resourceVerbalise The verbalise of the {@link Resource}
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(String errorCode, String userName, String permission, String resourceVerbalise,
			String resourceRId, Object... otherParams) {

		super(errorCode, resourceVerbalise, resourceRId);

		if (otherParams != null) {

			params = new Object[otherParams.length + 2];

			params[0] = resourceVerbalise;
			params[1] = resourceRId;

			int i = 2;

			for (Object object : otherParams) {
				params[i] = object;
				i++;
			}

		}

		this.userName = userName;
		this.permissions = new String[] { permission };
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permissions The permissions array
	 * @param resourceVerbalise The verbalise of the {@link Resource}
	 * @param resourceRId The resource id of the {@link Resource}
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(String errorCode, String userName, String[] permissions, String resourceVerbalise,
			String resourceRId, Object... otherParams) {

		super(errorCode, resourceVerbalise, resourceRId);

		if (otherParams != null) {

			params = new Object[otherParams.length + 2];

			params[0] = resourceVerbalise;
			params[1] = resourceRId;

			int i = 2;

			for (Object object : otherParams) {
				params[i] = object;
				i++;
			}

		}

		this.userName = userName;
		this.permissions = permissions;
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param ex The original exception
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permission The permission string
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(Exception ex, String errorCode, String userName, String permission,
			Object... otherParams) {

		super(ex, errorCode, otherParams);

		this.userName = userName;
		this.permissions = new String[] { permission };
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param ex The original exception
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permissions The permissions array
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(Exception ex, String errorCode, String userName, String[] permissions,
			Object... otherParams) {

		super(ex, errorCode, otherParams);

		this.userName = userName;
		this.permissions = permissions;
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permission The permission string
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(String errorCode, String userName, String permission, Object... otherParams) {

		super(errorCode, otherParams);

		this.userName = userName;
		this.permissions = new String[] { permission };
	}

	/**
	 * Create a new {@link AuthorizationException} with several parameters.
	 * 
	 * <br />
	 * <br />
	 * Remark: The order of the the parameters is the same that the order of the constructor arguments
	 * 
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param userName The user name
	 * @param permissions The permissions array
	 * @param otherParams Other parameters needed for the i18n
	 */
	public AuthorizationException(String errorCode, String userName, String[] permissions, Object... otherParams) {

		super(errorCode, otherParams);

		this.userName = userName;
		this.permissions = permissions;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public String getLocalizedMessage(String locale, I18nComponent i18nComponent) {

		if (permissions == null || permissions.length < 0)
			return super.getLocalizedMessage(locale, i18nComponent);

		String localizedPermissionsString = i18nComponent.getMessage(locale, permissions[0]);

		for (int i = 1; i < permissions.length; i++)
			localizedPermissionsString += ", " + i18nComponent.getMessage(locale, permissions[i]);

		return i18nComponent.getMessage(locale, errorCode, getParams(localizedPermissionsString));
	}

	@Override
	public String getLocalizedMessage(I18nComponent i18nComponent) {

		if (permissions == null || permissions.length < 0)
			return super.getLocalizedMessage(i18nComponent);

		String localizedPermissionsString = i18nComponent.getDefaultLocalizedMessage(permissions[0]);

		for (int i = 1; i < permissions.length; i++)
			localizedPermissionsString += ", " + i18nComponent.getDefaultLocalizedMessage(permissions[i]);

		return i18nComponent.getDefaultLocalizedMessage(errorCode, getParams(localizedPermissionsString));
	}

	@Override
	public String getLocalizedMessage(I18nComponent i18nComponent, HttpServletRequest req) {
		return getLocalizedMessage(i18nComponent.getUserLocalization(req), i18nComponent);
	}

	private Object[] getParams(String localizedPermissionsString) {

		Object[] newParams;

		if (params == null)
			newParams = new Object[2];
		else
			newParams = new Object[params.length + 2];

		newParams[0] = userName;
		newParams[1] = localizedPermissionsString;

		int i = 2;

		for (Object object : params) {
			newParams[i] = object;
			i++;
		}

		return newParams;
	}
}
