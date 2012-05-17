package com.collibra.dgc.core.component;

import java.util.List;

/**
 * Public API to retrieve the users active on the system.
 * @author GKDAI63
 * 
 */
public interface ConcurrentUsersComponent {

	/**
	 * Retrieves the number of concurrent users.
	 * @return the ammount of active users
	 */
	public Integer getNumerOfActiveSessions();

	/**
	 * Retrieves the names of the current active users. Duplicates in this list mean that there are in fact 2 sessions
	 * with 1 account.
	 * @return a set of the active users their account name
	 */
	public List<String> getActiveUsers();

}
