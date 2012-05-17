package com.collibra.dgc.core.model.representation.impl;

import java.util.Calendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;

@Entity
@Audited
@DiscriminatorValue(value = "DateTime")
public class DateTimeAttributeImpl extends AttributeImpl implements DateTimeAttribute {

	Calendar dateTime;

	protected DateTimeAttributeImpl() {
		super();
	}

	public DateTimeAttributeImpl(Term label, Representation representation, Calendar dateTime) {
		super(label, representation);
		setDateTime(dateTime);
	}

	@Override
	@Temporal(TemporalType.DATE)
	public Calendar getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;

		if (dateTime != null)
			setValue(String.valueOf(dateTime.getTime().getTime()));
		else
			setValue(null);
	}

	@Transient
	@Override
	public String getDiscriminator() {
		return "DateTime";
	}
}
