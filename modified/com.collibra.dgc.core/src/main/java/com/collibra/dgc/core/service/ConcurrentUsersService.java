package com.collibra.dgc.core.service;

import java.util.List;

/**
 * Service to keep track on the current logged in users
 * @author GKDAI63
 * 
 */
public interface ConcurrentUsersService {

	/**
	 * Retrieves the number of concurrent users.
	 * @return the ammount of active users
	 */
	public Integer getNumerOfActiveSessions();

	/**
	 * Retrieves the names of the current active users.
	 * @return a list of the active users their account name
	 */
	public List<String> getActiveUsers();

}
