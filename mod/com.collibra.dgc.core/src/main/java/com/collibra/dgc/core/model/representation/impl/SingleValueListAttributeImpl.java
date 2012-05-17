package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.Term;

@Entity
@Audited
@DiscriminatorValue(value = "SingleValueList")
public class SingleValueListAttributeImpl extends AttributeImpl implements SingleValueListAttribute {

	protected SingleValueListAttributeImpl() {
		super();
	}
	
	public SingleValueListAttributeImpl(Term label, Representation representation, String value) {
		super(label, representation);
		setValue(value);
	}
	
	public void setValue(String value) {
		super.setValue(value);
	}

	@Override
	@Transient
	public String getDiscriminator() {
		return "SingleValueList";
	}
}
