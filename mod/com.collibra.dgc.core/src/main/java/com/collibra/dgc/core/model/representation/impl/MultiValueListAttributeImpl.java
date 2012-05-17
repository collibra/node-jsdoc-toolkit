/**
 * 
 */
package com.collibra.dgc.core.model.representation.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;

/**
 * @author fvdmaele
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "ValueList")
public class MultiValueListAttributeImpl extends AttributeImpl implements MultiValueListAttribute {

	List<String> values = new LinkedList<String>();

	public MultiValueListAttributeImpl() {
		super();
	}

	public MultiValueListAttributeImpl(Term label, Representation representation, Collection<String> values) {
		super(label, representation);
		setValues(values);
	}

	public MultiValueListAttributeImpl(Term label, Representation representation, String value) {
		super(label, representation);

		if (value != null) {
			values.add(value);

		} else
			setValue("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.representation.impl.AttributeImpl#getValue()
	 */
	@Override
	@Transient
	public String getValue() {
		return values.toString();
	}

	@Override
	@ElementCollection
	@CollectionTable(name = "ATTRIBUTE_VALUES", joinColumns = @JoinColumn(name = "ID"))
	@Column(name = "ATTRIBUTE_VALUE")
	public List<String> getValues() {
		return this.values;
	}

	public void setValues(Collection<String> values) {
		this.values = new LinkedList<String>();

		if (values != null)
			this.values.addAll(values);

		setValue(concatValues());
	}

	@Override
	@Transient
	public String getDiscriminator() {
		return "ValueList";
	}

	private String concatValues() {
		String result = "";
		for (String val : getValues())
			result = result.concat(val);
		return result;
	}
}
