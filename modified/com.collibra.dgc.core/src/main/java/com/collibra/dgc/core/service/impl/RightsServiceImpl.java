package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.impl.UserFactoryImpl;
import com.collibra.dgc.core.observer.BinaryFactTypeFormEventAdapter;
import com.collibra.dgc.core.observer.CharacteristicFormEventAdapter;
import com.collibra.dgc.core.observer.CommunityEventAdapter;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.TermEventAdapter;
import com.collibra.dgc.core.observer.UserEventAdapter;
import com.collibra.dgc.core.observer.VocabularyEventAdapter;
import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.CharacteristicFormEventData;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.CommunityMemberChangeEventData;
import com.collibra.dgc.core.observer.events.CommunityMemberEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.RoleEventData;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.observer.events.UserEventData;
import com.collibra.dgc.core.observer.events.VocabularyEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class RightsServiceImpl extends AbstractService implements RightsService {
	private final Logger log = LoggerFactory.getLogger(RightsService.class);

	@Autowired
	private RepresentationService representationService;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private RightsServiceHelper rightsServiceHelper;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private AuthorizationHelper authorizationHelper;

	@Autowired
	private ConstraintChecker constraintChecker;

	protected CommunityLisener communityListener;
	protected VocabularyListener vocabularyListener;
	protected TermListener termListener;
	protected BftfListener bftfListener;
	protected CfListener cfListener;

	public RightsServiceImpl() {
		communityListener = new CommunityLisener();
		vocabularyListener = new VocabularyListener();
		termListener = new TermListener();
		bftfListener = new BftfListener();
		cfListener = new CfListener();

		ObservationManager.getInstance().register(communityListener, 0, GlossaryEventCategory.COMMUNITY);
		ObservationManager.getInstance().register(vocabularyListener, 0, GlossaryEventCategory.VOCABULARY);
		ObservationManager.getInstance().register(termListener, 0, GlossaryEventCategory.TERM);
		ObservationManager.getInstance().register(bftfListener, 0, GlossaryEventCategory.BINARY_FACT_TYPE_FORM);
		ObservationManager.getInstance().register(cfListener, 0, GlossaryEventCategory.CHARACTERISTIC_FORM);
	}

	@Override
	public Member addMember(String ownerId, String roleName, Resource resource) {
		// TODO: check this:
		// Defense.notNull(resource, DGCErrorCodes.USER_NAME_NULL, "userName");

		return addMember(ownerId, roleName, resource.getId());
	}

	@Override
	public Member addMember(String ownerId, String roleName, String resourceId) {
		Role role = findRoleByName(roleName);

		if (role == null) {
			String message = "Role with name '" + roleName + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}

		return addMember(ownerId, role, resourceId);
	}

	@Override
	public Member addMember(String ownerId, Role role, String resourceId) {

		Vocabulary vocabulary = representationService.findVocabularyByResourceId(resourceId);
		if (vocabulary != null) {
			return addMember(ownerId, role, vocabulary);
		} else {
			Representation representation = representationService.findRepresentationByResourceId(resourceId);
			if (representation != null) {
				representation = ServiceUtility.deproxy(representation, Representation.class);
				if (representation instanceof Term) {
					return addMember(ownerId, role, (Term) representation);
				} else if (representation instanceof BinaryFactTypeForm) {
					return addMember(ownerId, role, (BinaryFactTypeForm) representation);
				} else if (representation instanceof CharacteristicForm) {
					return addMember(ownerId, role, (CharacteristicForm) representation);
				}
			} else {
				Community community = communityDao.findById(resourceId);
				if (community != null) {
					return addMember(ownerId, role, community);
				}
			}
		}

		return null;
	}

	@Override
	public Member addMember(String ownerId, String roleName, Term term) {

		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, roleName, term);
	}

	@Override
	public Member addMember(String ownerId, String roleName, BinaryFactTypeForm bftf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, roleName, bftf);
	}

	@Override
	public Member addMember(String ownerId, String roleName, CharacteristicForm cf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, roleName, cf);
	}

	@Override
	public Member addMember(String ownerId, String roleName, Vocabulary vocabulary) {

		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, roleName, vocabulary);
	}

	@Override
	public Member addMember(String ownerId, Role role, Resource resource) {

		return rightsServiceHelper.addMember(ownerId, role, resource);
	}

	@Override
	public Member addMember(String ownerId, Role role, Term term) {

		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, role, term);
	}

	@Override
	public Member addMember(String ownerId, Role role, BinaryFactTypeForm bftf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, role, bftf);
	}

	@Override
	public Member addMember(String ownerId, Role role, CharacteristicForm cf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, role, cf);
	}

	@Override
	public Member addMember(String ownerId, Role role, Vocabulary vocabulary) {

		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.addMember(ownerId, role, vocabulary);
	}

	@Override
	public Member addMember(String ownerId, String roleName, Community community) {

		Role role = findRoleByName(roleName);
		return addMember(ownerId, role, community);
	}

	@Override
	public Member addMember(String ownerId, Role role, Community community) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_ADD_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Member member = rightsServiceHelper.addMember(ownerId, role, community);

		if (member != null) {
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER_COMMUNITY,
					new CommunityMemberEventData(community, member, EventType.ADDED));
		}

		return member;
	}

	@Override
	public Member addMember(String ownerId, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		// Add the user as member with specified role to glossary.
		return rightsServiceHelper.addMember(ownerId, role, null);
	}

	@Override
	public Member removeMember(String ownerId, String roleName, Community community) {

		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role with name '" + roleName + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}

		return removeMember(ownerId, role, community);
	}

	@Override
	public Member removeMember(String ownerId, Role role, Community community) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Member member = findMember(ownerId, community.getId(), role);
		if (member == null) {
			throw new EntityNotFoundException(DGCErrorCodes.MEMBER_NOT_FOUND_COMMUNITY, ownerId, role.getName(),
					community.getId());
		}

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER_COMMUNITY,
				new CommunityMemberEventData(community, member, EventType.REMOVING));

		// Remove the member
		rightsServiceHelper.remove(member, false);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER_COMMUNITY,
				new CommunityMemberEventData(community, member, EventType.REMOVED));

		return member;
	}

	@Override
	public Member removeMember(String ownerId, String roleName, Resource resource) {

		return removeMember(ownerId, roleName, resource.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role, Resource resource) {

		return removeMember(ownerId, role, resource.getId());
	}

	@Override
	public Member removeMember(String ownerId, String roleName, String resourceId) {
		Role role = findRoleByName(roleName);
		return removeMember(ownerId, role, resourceId);
	}

	@Override
	public Member removeMember(String ownerId, Role role, String resourceId) {

		Vocabulary vocabulary = representationService.findVocabularyByResourceId(resourceId);
		if (vocabulary != null) {
			return removeMember(ownerId, role, vocabulary);
		} else {
			Representation representation = representationService.findRepresentationByResourceId(resourceId);
			if (representation != null) {
				representation = ServiceUtility.deproxy(representation, Representation.class);
				if (representation instanceof Term) {
					return removeMember(ownerId, role, (Term) representation);
				} else if (representation instanceof BinaryFactTypeForm) {
					return removeMember(ownerId, role, (BinaryFactTypeForm) representation);
				} else if (representation instanceof CharacteristicForm) {
					return removeMember(ownerId, role, (CharacteristicForm) representation);
				}
			} else {
				Community community = communityDao.findById(resourceId);
				if (community != null) {
					return removeMember(ownerId, role, community);
				}
			}
		}

		return null;
	}

	@Override
	public Member removeMember(String ownerId, String roleName, Term term) {

		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, roleName, term.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role, Term term) {

		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, role, term.getId());
	}

	@Override
	public Member removeMember(String ownerId, String roleName, BinaryFactTypeForm bftf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, roleName, bftf.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role, BinaryFactTypeForm bftf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, role, bftf.getId());
	}

	@Override
	public Member removeMember(String ownerId, String roleName, CharacteristicForm cf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, roleName, cf.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role, CharacteristicForm cf) {

		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, role, cf.getId());
	}

	@Override
	public Member removeMember(String ownerId, String roleName, Vocabulary vocabulary) {

		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, roleName, vocabulary.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role, Vocabulary vocabulary) {

		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_REMOVE_MEMBER,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, role, vocabulary.getId());
	}

	@Override
	public Member removeMember(String ownerId, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		return rightsServiceHelper.removeMember(ownerId, role, null);
	}

	@Override
	public void changeRole(Member member, String roleName) {

		// Find the role
		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role '" + roleName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}

		changeRole(member, role);
	}

	@Override
	public void changeRole(Member member, Role role) {

		if (member.getRole().equals(role)) {
			return;
		}

		Vocabulary vocabulary = representationService.findVocabularyByResourceId(member.getResourceId());
		if (vocabulary != null) {
			changeRole(vocabulary, member, role);
		} else {
			Representation representation = representationService
					.findRepresentationByResourceId(member.getResourceId());
			if (representation != null) {
				representation = ServiceUtility.deproxy(representation, Representation.class);
				if (representation instanceof Term) {
					changeRole((Term) representation, member, role);
				} else if (representation instanceof BinaryFactTypeForm) {
					changeRole((BinaryFactTypeForm) representation, member, role);
				} else if (representation instanceof CharacteristicForm) {
					changeRole((CharacteristicForm) representation, member, role);
				}
			} else {
				Community community = communityDao.findById(member.getResourceId());
				if (community != null) {
					changeRole(community, member, role);
				}
			}
		}

		rightsServiceHelper.changeRole(member, role);
	}

	public void changeRole(Vocabulary vocabulary, Member member, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_CHANGE_ROLE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		rightsServiceHelper.changeRole(member, role);
	}

	public void changeRole(Term term, Member member, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_CHANGE_ROLE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		rightsServiceHelper.changeRole(member, role);
	}

	public void changeRole(BinaryFactTypeForm bftf, Member member, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_CHANGE_ROLE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		rightsServiceHelper.changeRole(member, role);
	}

	public void changeRole(CharacteristicForm cf, Member member, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_CHANGE_ROLE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		rightsServiceHelper.changeRole(member, role);
	}

	public void changeRole(Community community, Member member, Role role) {

		authorizationHelper.checkAuthorization(getCurrentUser(), community, Permissions.COMMUNITY_CHANGE_ROLE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Member oldMember = member.clone();
		rightsServiceHelper.changeRole(member, role);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEMBER_COMMUNITY,
				new CommunityMemberChangeEventData(community, member, oldMember, EventType.CHANGED));

	}

	@Override
	public Role findRoleByName(String name) {
		return rightsServiceHelper.findRoleByName(name);
	}

	@Override
	public Collection<Role> findRoles(String permission) {
		return rightsServiceHelper.findRoles(permission);
	}

	@Override
	public Role findRole(String resourceId) {
		return rightsServiceHelper.findRole(resourceId);
	}

	@Override
	public Collection<Member> findMemberRoles(String name, String resourceId) {
		return rightsServiceHelper.findMemberRoles(name, resourceId);
	}

	@Override
	public Member findMember(String name, Resource resource, Role role) {
		return findMember(name, resource.getId(), role);
	}

	@Override
	public Member findMember(String name, Resource resource, String roleName) {
		return findMember(name, resource.getId(), roleName);
	}

	@Override
	public Member findMember(String userId, String resourceId, String roleName) {
		// Find the role
		Role role = findRoleByName(roleName);
		if (role == null) {
			return null;
		}

		return findMember(userId, resourceId, role);
	}

	@Override
	public Member findMember(String userId, String resourceId, Role role) {
		return rightsServiceHelper.findMember(userId, resourceId, role);
	}

	@Override
	public Collection<Member> findMembers(String... resourceId) {
		return rightsServiceHelper.findMembers(resourceId);
	}

	@Override
	public Collection<Member> findGlossaryMembers() {
		return rightsServiceHelper.findGlobalMembers();
	}

	@Override
	public Set<String> findOwners(String... resourceIds) {
		Set<String> users = new TreeSet<String>();
		for (Member member : findMembers(resourceIds)) {
			users.add(member.getOwnerId());
		}

		return users;
	}

	@Override
	public Collection<Member> findMembers(String resourceId, String expression, int maxNumber) {
		return rightsServiceHelper.findMembers(resourceId, expression, maxNumber);
	}

	@Override
	public Collection<Member> findMembers(String resourceId, Role role) {
		return findMembersWithRole(resourceId, role.getTerm().getSignifier());
	}

	@Override
	public Collection<Member> findMembersWithRole(String resourceId, String roleName) {
		return rightsServiceHelper.findMembersWithRole(resourceId, roleName);
	}

	@Override
	public Collection<Member> findMembersWithRole(Role role) {
		return rightsServiceHelper.findMembersWithRole(role);
	}

	@Override
	public Collection<Member> findMembers(String ownerId) {
		return rightsServiceHelper.findMembers(ownerId);
	}

	@Override
	public Collection<Role> getRoles() {
		return rightsServiceHelper.getRoles();
	}

	@Override
	public Collection<Role> getGlobalRoles() {
		String queryStr = "from RoleImpl role where role.global=true order by role.term.signifier asc";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@Override
	public Collection<Role> getResourceRoles() {
		String queryStr = "from RoleImpl role where role.global=false order by role.term.signifier asc";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@Override
	public Collection<Member> findMembers(String roleName, String name) {
		// Find the role
		Role role = findRoleByName(roleName);
		if (role == null) {
			return new LinkedList<Member>();
		}

		return findMembers(role, name);
	}

	@Override
	public Collection<Member> findMembers(Role role, String name) {
		return rightsServiceHelper.findMembers(role, name);
	}

	@Override
	public Role createRole(Term term, boolean global) {
		// Set the object type of the term to role type.
		if (!MeaningConstants.META_ROLE_TYPE_UUID.equals(term.getObjectType().getType().getId())) {
			term.getObjectType().setType(objectTypeDao.getMetaRoleType());
		}
		return createRoleInternal(term, global);
	}

	@Override
	public Role createRole(String roleName, String description, boolean global) {

		// Check for role creation constraints.
		checkConstraintNewRole(roleName);

		// Create role term with description
		Vocabulary rolesAndResponsibilities = representationService.findRolesAndResponsibilitiesVocabulary();
		Term roleTerm = rolesAndResponsibilities.getTerm(roleName);

		if (roleTerm == null) {

			roleTerm = representationFactory.makeTerm(rolesAndResponsibilities, roleName);
			roleTerm.getObjectType().setType(objectTypeDao.getMetaRoleType());
			StringAttribute descriptionStringAttribute = representationFactory.makeStringAttribute(
					attributeService.findMetaDescription(), roleTerm, description);
			constraintChecker.checkConstraints(descriptionStringAttribute);

			roleTerm = representationService.saveTerm(roleTerm);
		}

		// Create the role
		return createRoleInternal(roleTerm, global);
	}

	@Override
	public Role removeRole(Role role) {
		return removeRoleInternal(role, true, true);
	}

	@Override
	public Role removeRole(String roleName) {
		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role with name '" + roleName + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}

		return removeRoleInternal(role, true, true);
	}

	@Override
	public Role removeRoleIfNoMembersExist(String roleName) {
		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role with name '" + roleName + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}
		return removeRoleInternal(role, false, true);
	}

	@Override
	public Role removeRoleIfNoMembersExist(Role role) {
		return removeRoleInternal(role, false, true);
	}

	@Override
	public Role removeRole(String roleName, boolean removeMembers, boolean removeRoleTerm) {

		Role role = findRoleByName(roleName);
		if (role == null) {
			String message = "Role with name '" + roleName + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, roleName);
		}
		return removeRole(role, removeMembers, removeRoleTerm);
	}

	@Override
	public Role removeRole(Role role, boolean removeMembers, boolean removeRoleTerm) {
		return removeRoleInternal(role, removeMembers, removeRoleTerm);
	}

	@Override
	public void removeAllMembers(String user) {
		rightsServiceHelper.removeAllMembers(user);
	}

	@Override
	public Role getRoleWithError(String name) {

		Role role = findRoleByName(name);
		if (role == null) {
			String message = "Role '" + name + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ROLE_NOT_FOUND_NAME, name);
		}

		return role;
	}

	@Override
	public Member getMemberWithError(String userId, String roleName, String resourceId) {

		Member member = findMember(userId, resourceId, roleName);
		if (member == null) {
			String memberString = "[" + userId + ", " + roleName + ", " + resourceId + "]";
			String message = "Member '" + memberString + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.MEMBER_NOT_FOUND, userId, roleName, resourceId);
		}
		return member;
	}

	@Override
	public void checkLastAdminRole(Role role) {
		final Collection<Role> roles = findRoles(Permissions.ADMIN);
		boolean otherFound = false;
		for (final Role toCheck : roles) {
			if (!toCheck.equals(role) && findMembersWithRole(toCheck).size() > 0) {
				otherFound = true;
				break;
			}
		}
		if (!otherFound) {
			log.error("This would be the last role with admin rights. No other found in roles " + roles);
			throw new IllegalArgumentException(DGCErrorCodes.LAST_ROLE_WITH_ADMIN_RIGHTS, role.getName(), role
					.getTerm().getId());
		}
	}

	private Role removeRoleInternal(Role role, boolean removeMembers, boolean removeRoleTerm) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		checkLastAdminRole(role);

		// Get all members playing the role.
		Collection<Member> members = findMembersWithRole(role);

		// If members should not be removed then return null.
		if (members.size() > 0 && !removeMembers) {
			return null;
		}

		for (Member member : members) {
			rightsServiceHelper.remove(member, true);
		}

		// Notify the role removal.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.REMOVING));

		getCurrentSession().delete(role);

		if (removeRoleTerm) {
			Term roleTerm = representationService.findTermByResourceId(role.getTerm().getId());
			if (roleTerm != null) {
				representationService.remove(roleTerm);
			} else {
				log.error("Failed to find the term for role '" + role.getName() + "'");
			}
		}

		getCurrentSession().flush();

		// Notify the role removal.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ROLE,
				new RoleEventData(role, EventType.REMOVED));

		return role;
	}

	private Role createRoleInternal(Term term, boolean global) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.ADMIN, DGCErrorCodes.GLOBAL_NO_PERMISSION);

		checkConstraintNewRole(term.getSignifier());

		// Create the role
		Role role = UserFactoryImpl.getInstance().createRole(term, global);
		getCurrentSession().save(role);
		return role;
	}

	private void checkConstraintNewRole(String roleName) {
		Role existingRole = findRoleByName(roleName);
		if (existingRole != null) {
			String message = "Role '" + roleName + "' already exists.";
			log.error(message);
			throw new ConstraintViolationException(message, roleName, Role.class.getName(),
					DGCErrorCodes.ROLE_ALREADY_EXISTS);
		}
	}

	private void removeMemberInternal(Member member) {
		if (member != null) {
			getCurrentSession().delete(member);
		}
	}

	// When a semantic community is deleted delete all its members.
	class CommunityLisener extends CommunityEventAdapter {
		@Override
		public void onRemove(CommunityEventData data) {
			if (data.getCommunity() != null) {
				removeMembersFromResource(data.getCommunity());
			}
		}
	}

	class VocabularyListener extends VocabularyEventAdapter {
		@Override
		public void onRemove(VocabularyEventData data) {
			if (data.getVocabulary() != null) {
				removeMembersFromResource(data.getVocabulary());
			}
		}
	}

	class TermListener extends TermEventAdapter {
		@Override
		public void onRemove(TermEventData data) {
			Term term = data.getTerm();
			if (term != null) {
				removeMembersFromResource(term);
				removeIfRoleTerm(term);
			}
		}

		private void removeIfRoleTerm(Term term) {
			if (objectTypeDao.getMetaRoleType().equals(term.getObjectType().getType())) {
				Role role = findRoleByName(term.getId());
				if (role != null) {
					// Remove role with all members having this role on any
					// resource.
					removeRole(role, true, false);
				}
			}
		}
	}

	class BftfListener extends BinaryFactTypeFormEventAdapter {
		@Override
		public void onRemove(BinaryFactTypeFormEventData data) {
			if (data.getBinaryFactTypeForm() != null) {
				removeMembersFromResource(data.getBinaryFactTypeForm());
			}
		}
	}

	class CfListener extends CharacteristicFormEventAdapter {
		@Override
		public void onRemove(CharacteristicFormEventData data) {
			if (data.getCharacteristicForm() != null) {
				removeMembersFromResource(data.getCharacteristicForm());
			}
		}
	}

	class UserListener extends UserEventAdapter {
		@Override
		public void onRemove(UserEventData data) {
			if (data.getUser() != null) {
				removeMembersFromUser(data.getUser());
			}
		}
	}

	private void removeMembersFromResource(Resource resource) {
		for (Member member : findMembers(resource.getId())) {
			removeMemberInternal(member);
		}
	}

	private void removeMembersFromUser(User user) {
		for (Member member : findMembers(user.getUserName())) {
			removeMemberInternal(member);
		}
	}
}
