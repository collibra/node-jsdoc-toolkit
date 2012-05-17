package com.collibra.dgc.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.observer.BinaryFactTypeFormEventAdapter;
import com.collibra.dgc.core.observer.CharacteristicFormEventAdapter;
import com.collibra.dgc.core.observer.CommunityEventAdapter;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.TermEventAdapter;
import com.collibra.dgc.core.observer.VocabularyEventAdapter;
import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.CharacteristicFormEventData;
import com.collibra.dgc.core.observer.events.CommunityEventData;
import com.collibra.dgc.core.observer.events.EventListenerPriority;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.observer.events.VocabularyEventData;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.LockingService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * 
 * @author amarnath
 * 
 */
@Service
@Lazy(true)
public class DomainObjectListener {
	@Autowired
	private CategorizationService categorizationService;
	@Autowired
	private LockingService lockingService;
	@Autowired
	private RepresentationService representationService;

	@Autowired
	private WorkflowService workflowService;

	public DomainObjectListener() {
		ObservationManager.getInstance().register(new CommunityListener(), EventListenerPriority.DEFAULT,
				GlossaryEventCategory.COMMUNITY);

		ObservationManager.getInstance().register(new VocabularyListener(), EventListenerPriority.DEFAULT,
				GlossaryEventCategory.VOCABULARY);

		ObservationManager.getInstance().register(new TermListener(), EventListenerPriority.DEFAULT,
				GlossaryEventCategory.TERM);

		ObservationManager.getInstance().register(new BftfListener(), EventListenerPriority.DEFAULT,
				GlossaryEventCategory.BINARY_FACT_TYPE_FORM);

		ObservationManager.getInstance().register(new CfListener(), EventListenerPriority.DEFAULT,
				GlossaryEventCategory.CHARACTERISTIC_FORM);
	}

	class CommunityListener extends CommunityEventAdapter {
		@Override
		public void onRemove(CommunityEventData data) {
			Community community = data.getCommunity();
			if (community != null) {
				handleRemove(community);
			}
		}
	}

	class VocabularyListener extends VocabularyEventAdapter {
		@Override
		public void onRemove(VocabularyEventData data) {
			Vocabulary vocabulary = data.getVocabulary();
			if (vocabulary != null) {
				handleRemove(vocabulary);
			}
		}
	}

	class TermListener extends TermEventAdapter {
		@Override
		public void onRemove(TermEventData data) {
			termRemoved(data);
		}

		private void termRemoved(TermEventData data) {
			Term term = data.getTerm();
			if (term != null) {
				removeAssociatedWorkflowProcessInstances(term);
			}
		}
	}

	class BftfListener extends BinaryFactTypeFormEventAdapter {
		@Override
		public void onRemove(BinaryFactTypeFormEventData data) {
			BinaryFactTypeForm bftf = data.getBinaryFactTypeForm();
			if (bftf != null) {
				removeAssociatedWorkflowProcessInstances(bftf);
			}
		}
	}

	class CfListener extends CharacteristicFormEventAdapter {
		@Override
		public void onRemove(CharacteristicFormEventData data) {
			CharacteristicForm cf = data.getCharacteristicForm();
			if (cf != null) {
				removeAssociatedWorkflowProcessInstances(cf);
			}
		}
	}

	private void handleRemove(Community community) {
		for (Community spComm : community.getSubCommunities()) {
			handleRemove(spComm);
		}
		for (Vocabulary vocabulary : community.getVocabularies()) {
			handleRemove(vocabulary);
		}
	}

	private void handleRemove(Vocabulary vocabulary) {
		for (Term term : vocabulary.getTerms()) {
			removeAssociatedWorkflowProcessInstances(term);
			categorizationService.removeCategorizationRelations(term);
		}

		for (BinaryFactTypeForm bftf : vocabulary.getBinaryFactTypeForms()) {
			removeAssociatedWorkflowProcessInstances(bftf);
		}

		for (CharacteristicForm cf : vocabulary.getCharacteristicForms()) {
			removeAssociatedWorkflowProcessInstances(cf);
		}

		removeFromIncorporatedVocabulary(vocabulary);
	}

	private void removeAssociatedWorkflowProcessInstances(Representation representation) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, representation.getId().toString());
		for (ProcessInstance processInstance : workflowService.findProcessInstances(variables)) {
			if (processInstance != null) {
				workflowService.stopProcessInstance(processInstance.getId(), "Representation deleted.");
			}
		}
	}

	private void removeFromIncorporatedVocabulary(Vocabulary incvocabulary) {
		for (Vocabulary voc : representationService.findAllIncorporatingVocabularies(incvocabulary)) {
			try {
				voc = (Vocabulary) lockingService.lock(VocabularyImpl.class, voc.getId());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			representationService.disincorporateVocabulary(voc, incvocabulary);
		}
	}
}
