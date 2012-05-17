package com.collibra.dgc.core.model.user;

import java.util.Set;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Term;

/**
 * {@link Role} defines the permissions for users playing the {@link Role} for a {@link Resource}.
 * @author amarnath
 * 
 */
public interface Role {
	/**
	 * 
	 * @param right
	 * @return
	 */
	boolean isPermitted(String right);

	/**
	 * 
	 * @return
	 */
	Set<String> getRights();

	void setRights(Set<String> rights);

	/**
	 * 
	 * @return
	 */
	Term getTerm();

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return True if this role is a global one. If false, this is a resource role.
	 */
	boolean isGlobal();
}
