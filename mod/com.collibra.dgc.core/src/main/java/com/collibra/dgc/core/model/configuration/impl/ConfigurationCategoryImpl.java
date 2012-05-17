/**
 * 
 */
package com.collibra.dgc.core.model.configuration.impl;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DiscriminatorOptions;

import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;

/**
 * @author fvdmaele
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "CONFIGURATIONCATEGORIES", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@DiscriminatorColumn(name = "CONF_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public abstract class ConfigurationCategoryImpl extends ResourceImpl implements ConfigurationCategory {

	private List<Representation> representations;
	private String name;
	private String description;

	protected ConfigurationCategoryImpl(String name, String description, List<Representation> representations) {
		this.name = name;
		this.description = description;
		this.representations = representations;
	}

	protected ConfigurationCategoryImpl(String name, List<Representation> representations) {
		this.name = name;
		this.representations = representations;
	}

	protected ConfigurationCategoryImpl(String name) {
		this.name = name;
	}

	protected ConfigurationCategoryImpl() {

	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(targetEntity = RepresentationImpl.class)
	@OrderColumn(name = "repr_index")
	@Access(value = AccessType.FIELD)
	@Basic(fetch = FetchType.EAGER)
	@JoinTable(name = "CONFCATEGORY_REPR")
	protected List<Representation> getRepresentations() {
		return representations;
	}

	@Transient
	protected void addRepresentation(Representation t) {
		representations.add(t);
	}

	@Transient
	protected void addRepresentation(int index, Representation t) {
		representations.add(index, t);
	}

	@Transient
	protected boolean changeOrder(Representation t, int index) {
		if (index >= representations.size())
			return false;
		int currIndex = representations.indexOf(t);
		if (currIndex < 0)
			return false;
		representations.remove(currIndex);
		representations.add(index, t);
		return true;
	}

	protected void setRepresentations(List<Representation> representations) {
		this.representations = representations;
	}
}
