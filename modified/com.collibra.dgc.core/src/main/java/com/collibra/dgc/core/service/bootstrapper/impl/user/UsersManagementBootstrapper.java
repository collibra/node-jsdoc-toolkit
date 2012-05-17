package com.collibra.dgc.core.service.bootstrapper.impl.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserFactory;
import com.collibra.dgc.core.model.user.impl.UserFactoryImpl;
import com.collibra.dgc.core.security.authorization.AuthorizationsFactory;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.collibra.dgc.core.security.authorization.impl.RightImpl;

public class UsersManagementBootstrapper {
	private final UserFactory userFactory = UserFactoryImpl.getInstance();
	private final RepresentationFactory representationFactory;
	private final AuthorizationsFactory authorizationsFactory;
	private final Vocabulary vocabulary;
	private final Vocabulary extVoc;
	private final Session session;
	private Role sysadminRole;

	public UsersManagementBootstrapper(Session session, Vocabulary vocabulary, final Vocabulary extVoc,
			final RepresentationFactory representationFactory, AuthorizationsFactory authorizationsFactory) {
		this.session = session;
		this.vocabulary = vocabulary;
		this.extVoc = extVoc;
		this.representationFactory = representationFactory;
		this.authorizationsFactory = authorizationsFactory;
	}

	public User bootstrap() {
		// To add rights for the defined roles.
		final Map<String, Role> roles = new HashMap<String, Role>();

		sysadminRole = null;
		Role adminRole = null;
		for (Term roleTerm : vocabulary.getTerms()) {
			if (MeaningConstants.META_ROLE_TYPE_UUID.equals(roleTerm.getObjectType().getType().getId())) {
				List<String> rights = new LinkedList<String>();
				boolean isGlobal = false;
				boolean isAdmin = false;
				if (Constants.ADMIN.equals(roleTerm.getSignifier())) {
					rights.add("VIEW");
					rights.add("MODIFY");
					rights.add("DELETE");
					isAdmin = true;
				} else if (Constants.STEWARD.equals(roleTerm.getSignifier())) {
					rights.add("VIEW");
					rights.add("MODIFY");
				} else if (Constants.SYSADMIN.equals(roleTerm.getSignifier())) {
					isGlobal = true;
				} else {
					rights.add("VIEW");
				}

				Role role = createRole(roleTerm, rights, isGlobal);
				if (isGlobal) {
					sysadminRole = role;
				} else if (isAdmin) {
					adminRole = role;
				}

				roles.put(roleTerm.getSignifier(), role);
			}
		}

		// Add rights from the authorization xml
		addRights(roles);

		User adminUser = userFactory.createUser(Constants.ADMIN_USER, "admin1906", "Admin", "Istrator", null);
		session.save(adminUser);

		Member member = userFactory.createMember(User.class, adminUser.getId(), null, sysadminRole);
		session.save(member);
		member = userFactory.createMember(User.class, adminUser.getId(), null, adminRole);
		session.save(member);

		return adminUser;
	}

	public void addAdminAsMember(User adminUser, Community community) {
		// For SBVR communities and vocabularies created add 'Admin' as
		// administrator.
		Member member = userFactory.createMember(User.class, adminUser.getId(), community, sysadminRole);
		session.save(member);

		addAdminAsMember(community, adminUser.getId(), member);
	}

	private void addAdminAsMember(Community community, String user, Member member) {
		if (community.getVocabularies() != null) {
			for (Vocabulary vocabulary : community.getVocabularies()) {
				member = userFactory.createMember(User.class, user, vocabulary, sysadminRole);
				session.save(member);
			}
		}
		if (community.getSubCommunities() != null) {
			for (final Community sc : community.getSubCommunities()) {
				addAdminAsMember(sc, user, member);
			}
		}
	}

	private Role createRole(Term roleTerm, List<String> rights, boolean global) {
		// Create the role object in the database.
		Role role = userFactory.createRole(roleTerm, global);
		role.setRights(new HashSet<String>(rights));
		session.save(role);
		return role;
	}

	private void addRights(final Map<String, Role> roles) {
		final Collection<RightCategory> categories = authorizationsFactory.getRightCategories();
		for (final RightCategory rightCategory : categories) {
			addRights(roles, rightCategory);
		}
	}

	private void addRights(final Map<String, Role> roles, final RightCategory rightCategory) {
		final Collection<com.collibra.dgc.core.security.authorization.Right> rights = rightCategory.getRights();
		for (com.collibra.dgc.core.security.authorization.Right right : rights) {
			final String[] supportedRights = ((RightImpl) right).getSupportedByRoles();
			for (String supportedByRole : supportedRights) {
				Role role = roles.get(supportedByRole);
				if (role != null) {
					role.getRights().add(right.getId());
				}
			}
		}

		for (RightCategory subCategory : rightCategory.getSubcategories()) {
			addRights(roles, subCategory);
		}
	}
}
