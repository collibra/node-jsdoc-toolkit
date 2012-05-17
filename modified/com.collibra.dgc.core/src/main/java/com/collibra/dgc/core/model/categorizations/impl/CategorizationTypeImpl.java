package com.collibra.dgc.core.model.categorizations.impl;

import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

/**
 * Default implementation for {@link CategorizationType}.
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "CT")
public class CategorizationTypeImpl extends ObjectTypeImpl implements CategorizationType {
	private ObjectType isForConcept;

	protected CategorizationTypeImpl() {
		super();
	}

	public CategorizationTypeImpl(ObjectType forConcept) {
		super();
		setIsForConcept(forConcept);
	}

	public CategorizationTypeImpl(String uuid) {
		super(uuid);
	}

	public CategorizationTypeImpl(ObjectType ot, ObjectType forConcept, final String uuid) {
		super(ot, uuid);
		setIsForConcept(forConcept);

		// Now make all the terms to point to this new type. This is needed in case the Terms are referred again in
		// other places in the same transaction.
		for (Term term : ot.getTerms()) {
			((TermImpl) term).setObjectType(this);
		}
	}

	public CategorizationTypeImpl(CategorizationType type) {
		super(type, UUID.randomUUID().toString());
		setIsForConcept(type.getIsForConcept());
	}

	@ManyToOne
	@JoinColumn(name = "FOR_CONCEPT")
	@Target(value = ObjectTypeImpl.class)
	public ObjectType getIsForConcept() {
		return isForConcept;
	}

	public void setIsForConcept(ObjectType dedicatedObjectType) {
		this.isForConcept = dedicatedObjectType;
	}

	@Override
	public CategorizationTypeImpl clone() {
		return new CategorizationTypeImpl(this);
	}
}
