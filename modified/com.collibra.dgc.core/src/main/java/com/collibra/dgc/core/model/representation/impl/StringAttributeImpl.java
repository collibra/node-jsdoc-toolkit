package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;

@Entity
@Audited
@DiscriminatorValue(value = "String")
public class StringAttributeImpl extends AttributeImpl implements StringAttribute {

	private String longExpression;

	public StringAttributeImpl() {
		super();
	}

	public StringAttributeImpl(Term label, Representation representation, String value) {
		super(label, representation);
		setLongExpression(value);
	}

	public void setLongExpression(String longExpression) {
		this.longExpression = longExpression;
		setValue(longExpression);
	}

	/**
	 * @return the longExpression
	 */
	@Override
	@Lob
	@Column(name = "EXPRESSION_LONG")
	@Access(value = AccessType.FIELD)
	public String getLongExpression() {
		return longExpression;
	}

	@Override
	@Transient
	public String getDiscriminator() {
		return "String";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!super.equals(obj))
			return false;

		if (!(obj instanceof StringAttribute))
			return false;

		StringAttribute objStringAttribute = (StringAttribute) obj;

		if ((objStringAttribute.getLongExpression() == null && this.longExpression == null)
				|| (objStringAttribute.getLongExpression() != null && objStringAttribute.getLongExpression().equals(
						this.longExpression)))
			return true;

		return false;
	}
}