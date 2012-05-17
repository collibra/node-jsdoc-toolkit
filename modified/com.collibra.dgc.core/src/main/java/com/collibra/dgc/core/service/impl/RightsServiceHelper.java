package com.collibra.dgc.core.service.impl;

import java.util.Collection;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.impl.MemberImpl;
import com.collibra.dgc.core.model.user.impl.UserFactoryImpl;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.MemberChangeEventData;
import com.collibra.dgc.core.observer.events.MemberEventData;
import com.collibra.dgc.core.security.authorization.Permissions;

/**
 * TODO: This class is a combination of DAOs and common methods used by services. Do we need to have a DAO for
 * {@link Member} ??
 * 
 * @author amarnath
 * 
 */
@Service
public class RightsServiceHelper {
	private static final Logger log = LoggerFactory.getLogger(RightsServiceHelper.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDao userDao;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public Collection<Member> findMembers(String ownerId) {
		String queryStr = "from MemberImpl m where m.ownerId = :ownerId";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("ownerId", ownerId);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Role> getRoles() {
		String queryStr = "from RoleImpl role order by role.term.signifier asc";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Role> findRoles(String permission) {
		log.error("All the existing roles: " + getRoles());
		String queryStr = "from RoleImpl role where :permission in elements(role.rights)";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("permission", permission);
		query.setReadOnly(true);
		query.setFlushMode(FlushMode.MANUAL);

		final Collection<Role> ret = query.list();
		log.error("Number of roles found with permission '" + permission + "': " + ret.size());
		return ret;
	}

	public Role findRole(Term roleTerm) {
		return findRoleByName(roleTerm.getId());
	}

	public Member addMember(String ownerId, Role role, Resource resource) {

		// for a resource role, a specific resource must be specified
		if (resource == null && !role.isGlobal()) {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(
					DGCErrorCodes.MEMBER_WITHOUT_RESOURCE_AND_RESOURCE_ROLE);
		}
		// for a global role, no resource should be specified (always for all resources)
		if (role.isGlobal() && resource != null) {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(
					DGCErrorCodes.MEMBER_WITHOUT_RESOURCE_AND_RESOURCE_ROLE);
		}

		Class c = userDao.getOwnerType(ownerId);

		if (c == null) {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(
					DGCErrorCodes.USER_OR_GROUP_ID_DOES_NOT_EXIST, ownerId);
		}

		// Check all constraints
		Member existingMember = findMember(ownerId, resource == null ? null : resource.getId(), role);
		if (existingMember != null) {
			return existingMember;
		}

		// Create new member
		Member member = UserFactoryImpl.getInstance().createMember(c, ownerId, resource, role);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
				new MemberEventData(member, EventType.ADDING));

		getCurrentSession().save(member);
		getCurrentSession().flush();

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
				new MemberEventData(member, EventType.ADDED));

		return member;
	}

	public Member addMember(String ownerId, String roleName, Resource resource) {
		// Find the role
		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role '" + roleName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}

		return addMember(ownerId, role, resource);
	}

	public Member removeMember(String ownerId, String roleName, String resourceId) {
		Role role = findRoleByName(roleName);
		return removeMember(ownerId, role, resourceId);
	}

	public Member removeMember(String ownerId, Role role, String resourceId) {
		Member member = findMember(ownerId, resourceId, role);
		if (member != null) {
			remove(member, false);
		}

		return member;
	}

	public void remove(Member member, boolean skipAdminCheck) {
		if (member != null) {
			if (!skipAdminCheck && member.getRole().isGlobal()
					&& member.getRole().getRights().contains(Permissions.ADMIN)) {
				// We are removing a member where the role has the admin rights, so we should check if this isn't the
				// last.
				final Collection<Role> roles = findRoles(Permissions.ADMIN);
				boolean foundOthers = false;
				outer: for (final Role role : roles) {
					final Collection<Member> members = findMembersWithRole(role);
					// TODO remove
					log.error("Found members for role " + role.getName() + ": " + members);
					for (final Member toCheck : members) {
						if (toCheck.getResourceId() == null && !toCheck.equals(member)) {
							foundOthers = true;
							break outer;
						}
					}
				}
				if (!foundOthers) {
					// TODO remove
					log.error("This would be the last member with admin rights. No other found in roles " + roles);
					throw new IllegalArgumentException(DGCErrorCodes.LAST_MEMBER_WITH_ADMIN_RIGHTS,
							member.getOwnerId(), member.getResourceId());
				}
			}

			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
					new MemberEventData(member, EventType.REMOVING));

			getCurrentSession().delete(member);
			getCurrentSession().flush();

			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
					new MemberEventData(member, EventType.REMOVED));
		}
	}

	public void removeAllMembers(String user) {
		for (Member member : findMembers(user)) {
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
					new MemberEventData(member, EventType.REMOVING));

			getCurrentSession().delete(member);

			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
					new MemberEventData(member, EventType.REMOVED));
		}

		getCurrentSession().flush();
	}

	public Member findMember(String ownerId, String resourceId, Role role) {
		String queryStr = resourceId != null ? "from MemberImpl m where m.ownerId = :ownerId and m.resourceId = :resourceId and m.role = :role"
				: "from MemberImpl m where m.ownerId = :ownerId and m.resourceId IS NULL and m.role = :role";
		;
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("ownerId", ownerId);
		if (resourceId != null) {
			query.setParameter("resourceId", resourceId);
		}
		query.setParameter("role", role);
		query.setFlushMode(FlushMode.MANUAL);

		return (Member) query.uniqueResult();
	}

	public Collection<Member> findGlobalMembers() {
		String queryStr = "from MemberImpl m where m.resourceId IS NULL";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Member> findMembers(String... resourceIds) {
		String queryStr = "from MemberImpl m where m.resourceId in :resourceIds";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameterList("resourceIds", resourceIds);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Member> findMembers(String resourceId, String expression, int maxNumber) {
		String searchExpression = expression;

		if (!searchExpression.startsWith("%")) {
			searchExpression = "%" + searchExpression;
		}

		if (!searchExpression.endsWith("%")) {
			searchExpression = searchExpression + "%";
		}

		if (maxNumber < 0) {
			maxNumber = 100;
		}

		String queryStr = "from MemberImpl m where m.resourceId = :resourceId and m.ownerId like :expression order by m.ownerId";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("expression", searchExpression);
		query.setParameter("resourceId", resourceId);
		query.setMaxResults(maxNumber);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Role findRoleByName(String name) {
		String queryStr = "from RoleImpl role where role.term.signifier = :name";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("name", name);
		query.setFlushMode(FlushMode.MANUAL);

		return (Role) query.uniqueResult();
	}

	public Role findRole(String resourceId) {
		String queryStr = "from RoleImpl role where role.term.id = :resourceId";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("id", resourceId);
		query.setFlushMode(FlushMode.MANUAL);

		return (Role) query.uniqueResult();
	}

	public Collection<Member> findMemberRoles(String ownerId, String resourceId) {
		String queryStr = "from MemberImpl m where m.ownerId = :ownerId and m.resourceId = :resourceId";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("ownerId", ownerId);
		query.setParameter("resourceId", resourceId);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Member> findMembersWithRole(String resourceId, String roleName) {
		String queryStr = "from MemberImpl m where m.resourceId = :resourceId and m.role.term.signifier = :rolenName";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("resourceId", resourceId);
		query.setParameter("rolenName", roleName);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Member> findMembersWithRole(Role role) {
		String queryStr = "from MemberImpl m where m.role = :role";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("role", role);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Collection<Member> findMembers(Role role, String ownerId) {
		String queryStr = "from MemberImpl m where m.ownerId = :ownerId and m.role = :role";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("ownerId", ownerId);
		query.setParameter("role", role);
		query.setFlushMode(FlushMode.MANUAL);

		return query.list();
	}

	public void changeRole(Member member, Role role) {
		// Do not change anything.
		if (member.getRole().equals(role)) {
			return;
		}

		Member existingMember = findMember(member.getOwnerId(), member.getResourceId(), role);
		if (existingMember != null) {
			String message = "User/group '" + member.getOwnerId() + "' already a member with role '" + role.getName()
					+ "'";
			log.error(message);
			throw new ConstraintViolationException(message, member.getResourceId(), Member.class.getName(),
					DGCErrorCodes.MEMBER_DUPLICATE);
		}

		Member oldMember = member.clone();
		((MemberImpl) member).setRole(role);
		getCurrentSession().flush();
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER,
				new MemberChangeEventData(member, oldMember, EventType.CHANGED));
	}
}
