package com.collibra.dgc.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.observer.BinaryFactTypeFormEventAdapter;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * Forwards the service request to {@link RepresentationService}. Any class requiring
 * @author amarnath
 * 
 */
@Service
public class RepresentationServiceRequestHandler {
	private static BinaryFactTypeFormServiceRequest eventListener;

	@Autowired
	private RepresentationService representationService;

	public RepresentationServiceRequestHandler() {
		eventListener = new BinaryFactTypeFormServiceRequest();
		ObservationManager.getInstance().register(eventListener, 0, GlossaryEventCategory.SERVICE_BFTF);
	}

	class BinaryFactTypeFormServiceRequest extends BinaryFactTypeFormEventAdapter {
		@Override
		public void onAdd(BinaryFactTypeFormEventData data) {
			BinaryFactTypeForm bftf = data.getBinaryFactTypeForm();

			// If the BFTF is derived then set the general BFTF.
			if (data.isDerived() && data.getDerivedForTerm() != null) {
				BinaryFactsDerivator derivator = ((RepresentationServiceImpl) representationService)
						.createBinaryFactsDerivator();
				derivator.findDerivedFacts(data.getDerivedForTerm());
				BinaryFactTypeForm generalBFTF = derivator.getGeneralBftfs().get(bftf);
				if (generalBFTF != null) {
					bftf.getBinaryFactType().setGeneralConcept(generalBFTF.getBinaryFactType());
					bftf = representationService.saveBinaryFactTypeForm(bftf);
					return;
				}
			}

			bftf = representationService.saveBinaryFactTypeForm(bftf);
		}

		// TODO: Override all the methods that are needed in future.
	}
}
