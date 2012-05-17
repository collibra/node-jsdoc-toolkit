package com.collibra.dgc.core.model.user;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Term;

/**
 * Factory interface for creating {@link Member}s and {@link Role}s.
 * 
 * @author amarnath
 * 
 */
@Service
public interface UserFactory {
	/**
	 * Create a new user with the given parameters
	 */
	User createUser(String userName, String password, String firstName, String lastName, String email);

	/**
	 * Create member.
	 * @param c the class of the owner of the membership
	 * 
	 * @param userName User name.
	 * @param resourceId {@link Resource}.
	 * @param role The {@link Role}.
	 * @return {@link Member} instance.
	 */
	Member createMember(Class c, String userName, Resource resource, Role role);

	/**
	 * Create {@link Member} similar to specified {@link Member}.
	 * 
	 * @param userName The user name.
	 * @param member The {@link Member} whose profile need to be copied.
	 * @return The {@link Member}.
	 */
	Member createSimilarMember(String userName, Member member);

	/**
	 * Create {@link Role}.
	 * 
	 * @param term The {@link Term} with {@link Concept} 'RoleType'.
	 * @param global Determines if this role is a global role or a resource role.
	 * @return
	 */
	Role createRole(Term term, boolean global);
}
