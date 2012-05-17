/**
 * 
 */
package com.collibra.dgc.core.dto.cmatch;

import java.util.List;

import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.matcher.common.model.DMEntity;
import com.collibra.matcher.common.model.DMModel;

/**
 * @author fvdmaele
 * 
 */
public class CMVocabularyModel implements DMModel {

	protected Vocabulary voc;

	public CMVocabularyModel(Vocabulary voc) {
		this.voc = voc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.cmatch.model.DMModel#getEntities()
	 */
	public List<? extends DMEntity> getEntities() {
		CMModel tempModel = new CMModel(voc.getTerms());

		return tempModel.getEntities();
	}

	public Vocabulary getVocabulary() {
		return voc;
	}

}
