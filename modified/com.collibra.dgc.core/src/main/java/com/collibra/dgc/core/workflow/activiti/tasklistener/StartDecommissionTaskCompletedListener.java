package com.collibra.dgc.core.workflow.activiti.tasklistener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.impl.ServiceUtility;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.activiti.delegate.AbstractDelegate;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * The {@link TaskListener} class to perform the required initialization for {@link Term}s decommission process after
 * user selects the {@link Term}s to be decommissioned.
 * @author amarnath
 * 
 */
public class StartDecommissionTaskCompletedListener extends AbstractDelegate implements TaskListener {
	private final Set<String> responsibleUsers = new HashSet<String>();
	private final Set<String> allOtherUsers = new HashSet<String>();

	private final MultiValueMap userToTerms = new MultiValueMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		execute(delegateTask.getExecution());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	@Transactional
	public void execute(DelegateExecution execution) {
		// For each of the term to be decommissioned get all the responsible and other users. For each user a task will
		// be created for their response.
		List<String> selectedResourceIds = (List<String>) execution
				.getVariable(WorkflowConstants.VAR_SELECTED_TERM_RESOURCEIDS_LIST);
		if (selectedResourceIds != null && selectedResourceIds.size() > 0) {

			StringBuilder decommissionedTerms = new StringBuilder();

			List<Term> terms = getTerms(selectedResourceIds);

			for (Term term : terms) {
				// Perform the state change if defined.
				changeState(term);

				// Save the current status of the term for restoring later when needed.
				Term termStatus = term.getStatus();
				if (termStatus != null) {
					execution.setVariable(WorkflowConstants.VAR_PREVIOUS_STATE_PREFIX + term.getId().toString(),
							termStatus.getSignifier());
				}

				initializeUsersForTerm(execution, term);

				decommissionedTerms.append(term.getVocabulary().verbalise()).append(".").append(term.verbalise())
						.append("<br>");
			}

			// Get email ids for notification.
			List<String> emailIds = new ArrayList<String>(userToTerms.size());
			for (Object user : userToTerms.keySet()) {
				// TODO PORT
				// String email = userComponent.getEmailAddress((String) user);
				String email = "";
				if (email != null && email.length() > 0) {
					emailIds.add(email);
				}

				List<String> userTermResourceIds = new LinkedList<String>();
				for (Object obj : userToTerms.getCollection(user)) {
					userTermResourceIds.add((String) obj);
				}

				// Set the process variable mapping user name to related terms resource ids.
				execution.setVariable((String) user, userTermResourceIds);
			}

			// Exclude responsible from all others list.
			allOtherUsers.removeAll(responsibleUsers);

			// FIXME : Activiti limitation that for multi-instance tasks there should be at least one loop data item.
			// If there is no user apart from responsible just add one dummy task owner.
			if (allOtherUsers.size() == 0) {
				allOtherUsers.add(WorkflowConstants.DUMMY_TASK_OWNER);
			}

			// There should be at least one responsible user.
			if (responsibleUsers.size() == 0) {
				throw new WorkflowException(WorkflowProcesses.DECOMMISSIONING,
						"No responsible users available for decommissioning of terms",
						WorkflowErrorCodes.RESPONSIBLE_USERS_NOT_FOUND);
			}

			// Add process variables with email list and assignee list for next tasks.
			execution.setVariable(WorkflowConstants.VAR_USERS_EMAIL_LIST, emailIds);
			execution.setVariable(WorkflowConstants.VAR_RESPONSIBLE_LIST, responsibleUsers);
			execution.setVariable(WorkflowConstants.VAR_OTHERS_LIST, allOtherUsers);

			// Set the email content about decommissioned terms.
			execution.setVariable(WorkflowConstants.VAR_TERMS_BEING_DECOMMISSIONED_MAIL_CONTENT,
					decommissionedTerms.toString());

			execution.setVariable(WorkflowConstants.VAR_ALL_RESPONSIBLE_USERS_RESPONDED, Boolean.FALSE);

			// Add comment that decommission stated.
			addCommentDecommissionStarted(execution, terms);
		}
	}

	private void initializeUsersForTerm(DelegateExecution execution, Term term) {
		List<String> roleAndEntities = (List<String>) execution.getVariable(WorkflowConstants.VAR_RESPONSIBLE_ROLES);
		if (roleAndEntities == null) {
			return;
		}

		MultiValueMap roleToEntityLevels = AppContext.getWorkflowUtility().getRoleToEntityLevelsMap(roleAndEntities);

		Set<String> termResponsibleUsers = new HashSet<String>();
		Set<String> termAllOtherUsers = new HashSet<String>();

		// Get all responsible users
		termResponsibleUsers.addAll(AppContext.getWorkflowUtility().getUsers(term, roleToEntityLevels));

		// Get all other users

		termAllOtherUsers.addAll(AppContext.getWorkflowUtility().getUserNames(
				AppContext.getRightsService().findMembers(term.getId())));

		// Get all dependent representations.
		Collection<Representation> dependentRepresentations = AppContext.getRepresentationService()
				.findAllRelatedRepresentations(term);
		for (Representation representation : dependentRepresentations) {
			termResponsibleUsers.addAll(AppContext.getWorkflowUtility().getUsers(representation, roleToEntityLevels));

			termAllOtherUsers.addAll(AppContext.getWorkflowUtility().getUserNames(
					AppContext.getRightsService().findMembers(representation.getId())));
		}

		// Get all categorization schemes.
		Collection<Concept> schemes = AppContext.getCategorizationService().findSchemesForConcept(term.getObjectType());
		for (Concept scheme : schemes) {
			scheme = ServiceUtility.deproxy(scheme, Concept.class);
			termResponsibleUsers.addAll(AppContext.getWorkflowUtility().getUsers(
					scheme.getRepresentations().iterator().next(), roleToEntityLevels));
			termAllOtherUsers.addAll(AppContext.getWorkflowUtility().getUserNames(
					AppContext.getRightsService().findMembers(scheme.getId())));
		}

		// Filter the users
		termAllOtherUsers.removeAll(termResponsibleUsers);

		// Set process variables.
		execution.setVariable(WorkflowConstants.VAR_RESPONSIBLE_REPRESENTATION_PREFIX + term.getId(),
				termResponsibleUsers);
		execution.setVariable(WorkflowConstants.VAR_OTHERS_REPRESENTATION_PREFIX + term.getId(), termAllOtherUsers);

		// Set all users of the term as process variable.
		Set<String> allusers = new HashSet<String>(termAllOtherUsers);
		allusers.addAll(termResponsibleUsers);
		execution.setVariable(term.getId().toString(), allusers);

		// Update user to term map.
		for (String user : allusers) {
			userToTerms.put(user, term.getId().toString());
		}

		// Add the users to complete list for all representations.
		responsibleUsers.addAll(termResponsibleUsers);
		allOtherUsers.addAll(termAllOtherUsers);
	}

	/**
	 * 
	 * @param resourceIds
	 * @return
	 */
	private List<Term> getTerms(List<String> resourceIds) {
		List<String> resourceIdsList = resourceIds;
		List<Term> terms = new ArrayList<Term>(resourceIdsList.size());

		for (String resourceId : resourceIdsList) {
			terms.add(AppContext.getRepresentationService().findTermByResourceId(resourceId));
		}

		return terms;
	}

	/**
	 * Add comment to the {@link Term} page about the starting of decommission.
	 * @param execution
	 * @param terms
	 */
	private void addCommentDecommissionStarted(DelegateExecution execution, List<Term> terms) {
		// TODO PORT
		// for (Term term : terms) {
		// DocumentHandler docHandler = ComponentManagerUtil.getDocumentHandler();
		// if (docHandler != null && docHandler.isXWikiRunning()) {
		// List<String> parameters = new LinkedList<String>();
		// parameters.add(term.getVocabulary().verbalise() + "." + term.verbalise());
		// String comment = docHandler.getXWikiContext().getMessageTool()
		// .get(WorkflowConstants.DECOMMISSION_COMMENT_KEY, parameters);
		// int commentNumber = WorkflowUtility.addComment(term.getResourceId(), comment);
		// execution.setVariable(WorkflowConstants.VAR_COMMENT_ID_PREFIX + term.getResourceId(), commentNumber);
		// }
		// }
	}
}
