package com.collibra.dgc.core.model.user.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserFactory;

/**
 * Singleton implementation for {@link UserFactory}.
 * 
 * @author amarnath
 * 
 */
@Service
public class UserFactoryImpl implements UserFactory {
	private static UserFactoryImpl instance = new UserFactoryImpl();

	private UserFactoryImpl() {

	}

	public static UserFactory getInstance() {
		return instance;
	}

	@Override
	public Member createMember(Class c, String userName, Resource resource, Role role) {
		return new MemberImpl(userName, resource, role, c.equals(Group.class));
	}

	@Override
	public Member createSimilarMember(String userName, Member member) {

		return new MemberImpl(userName, member);
	}

	@Override
	public Role createRole(Term term, boolean global) {
		if (MeaningConstants.META_ROLE_TYPE_UUID.equals(term.getObjectType().getType().getId())) {
			return new RoleImpl(term, global);
		}

		return null;
	}

	@Override
	public User createUser(String userName, String password, String firstName, String lastName, String email) {
		return new UserImpl(userName, password, firstName, lastName, email);
	}
}
