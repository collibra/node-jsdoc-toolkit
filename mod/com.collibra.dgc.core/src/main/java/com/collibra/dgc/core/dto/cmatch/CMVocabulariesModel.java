/**
 * 
 */
package com.collibra.dgc.core.dto.cmatch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.matcher.common.model.DMEntity;
import com.collibra.matcher.common.model.DMModel;

/**
 * @author fvdmaele
 * 
 */
public class CMVocabulariesModel implements DMModel {

	LinkedList<Vocabulary> vocs;

	public CMVocabulariesModel(Collection<Vocabulary> vocs) {
		this.vocs = new LinkedList<Vocabulary>();
		this.vocs.addAll(vocs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.cmatch.model.DMModel#getEntities()
	 */
	public List<? extends DMEntity> getEntities() {
		LinkedList<CMTermEntity> result = new LinkedList<CMTermEntity>();
		for (Vocabulary voc : vocs) {
			for (Term t : voc.getTerms())
				result.add(new CMTermEntity(t.getSignifier().toLowerCase(), t.getId().toString()));
		}
		return result;
	}

	public Term findTermInVocabularies(String signifier) {
		Term t = null;
		for (Vocabulary voc : vocs) {
			t = voc.getTerm(signifier);
			if (t != null)
				return t;
		}
		return t;
	}

}
