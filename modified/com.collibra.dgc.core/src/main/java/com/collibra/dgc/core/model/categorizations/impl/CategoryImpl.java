/**
 * 
 */
package com.collibra.dgc.core.model.categorizations.impl;

import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

/**
 * Default implementation for {@link Category}.
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "CA")
public class CategoryImpl extends ObjectTypeImpl implements Category {
	protected CategoryImpl() {
		super();
	}

	public CategoryImpl(CategorizationType categorizationType) {
		super();
		initialize(categorizationType);
	}

	public CategoryImpl(String uuid, CategorizationType categorizationType) {
		super(uuid);
		initialize(categorizationType);
	}

	public CategoryImpl(ObjectType ot, CategorizationType categorizationType, final String uuid) {
		super(ot, uuid);
		initialize(categorizationType);

		// Now make all the terms to point to this new type. This is needed in case the Terms are referred again in
		// other places in the same transaction.
		for (Term term : getTerms()) {
			((TermImpl) term).setObjectType(this);
		}
	}

	private void initialize(CategorizationType categorizationType) {
		setType(categorizationType);
		setGeneralConcept(categorizationType.getIsForConcept());
	}

	public CategoryImpl(Category cat) {
		super(cat, UUID.randomUUID().toString());
	}

	@Override
	public String toString() {
		return "CategoryImpl [getResourceId()=" + getId() + "]";
	}

	@Override
	public CategoryImpl clone() {
		return new CategoryImpl(this);
	}

}
