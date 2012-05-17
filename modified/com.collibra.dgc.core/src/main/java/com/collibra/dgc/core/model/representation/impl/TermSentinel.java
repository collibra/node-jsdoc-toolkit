package com.collibra.dgc.core.model.representation.impl;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

public class TermSentinel extends TermImpl implements Term {

	private final Vocabulary vocabulary;
	private final String signifier;

	public TermSentinel(Vocabulary voc, String signifier) {
		this.vocabulary = voc;
		this.signifier = signifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((signifier == null) ? 0 : signifier.hashCode());
		result = prime * result + ((vocabulary == null) ? 0 : vocabulary.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Term))
			return false;
		TermSentinel other = (TermSentinel) obj;
		if (signifier == null) {
			if (other.signifier != null)
				return false;
		} else if (!signifier.equals(other.signifier))
			return false;
		if (vocabulary == null) {
			if (other.vocabulary != null)
				return false;
		} else if (!vocabulary.equals(other.vocabulary))
			return false;
		return true;
	}

}
