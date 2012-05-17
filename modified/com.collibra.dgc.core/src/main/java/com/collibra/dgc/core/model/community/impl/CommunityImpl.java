package com.collibra.dgc.core.model.community.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;

/**
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "COMMUNITIES")
public class CommunityImpl extends ResourceImpl implements Community, Comparable<Community> {

	private String name;
	private String uri;
	private boolean isSBVR;
	private boolean isMeta;

	private Set<Community> subCommunities = new HashSet<Community>();
	private Community parentCommunity;

	private Set<Vocabulary> vocabularies = new HashSet<Vocabulary>();
	private String language = null;

	public CommunityImpl() {
		super();
	}

	public CommunityImpl(String name) {
		super();
		this.name = name;
	}

	public CommunityImpl(String name, String uri) {
		super();
		this.name = name;
		this.uri = uri;
	}

	public CommunityImpl(Community community) {
		super(community);
		setName(community.getName());
		setUri(community.getUri());
	}

	@Override
	@Column(name = "NAME", nullable = false, unique = true)
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Column(name = "URI", nullable = false, unique = true)
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	protected void initializeRelations() {
	}

	@Override
	public String verbalise() {
		return getName();
	}

	@Override
	public int compareTo(Community c) {
		return name.toLowerCase().compareTo(c.getName().toLowerCase());
	}

	@Override
	public String toString() {
		return verbalise();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#getSubCommunities()
	 */
	@Override
	@OneToMany(targetEntity = CommunityImpl.class, mappedBy = "parentCommunity")
	public Set<Community> getSubCommunities() {
		return subCommunities;
	}

	protected void setSubCommunities(final Set<Community> subCommunities) {
		this.subCommunities = subCommunities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.core.model.community.Community#addSubCommunity(com.collibra.dgc.core.model.community.Community)
	 */
	@Override
	public void addSubCommunity(Community community) {
		subCommunities.add(community);
		if (this.equals(community.getParentCommunity())) {
			return;
		}

		community.setParentCommunity(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#getParentCommunity()
	 */
	@Override
	@ManyToOne
	@JoinColumn(name = "PARENT_COMM")
	@Target(value = CommunityImpl.class)
	public Community getParentCommunity() {
		return parentCommunity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.core.model.community.Community#setParentCommunity(com.collibra.dgc.core.model.community.Community
	 * )
	 */
	@Override
	public void setParentCommunity(Community community) {
		parentCommunity = community;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#getLanguage()
	 */
	@Override
	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return language;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#getVocabularies()
	 */
	@Override
	@OneToMany(targetEntity = VocabularyImpl.class, mappedBy = "community")
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<Vocabulary> getVocabularies() {
		return vocabularies;
	}

	protected void setVocabularies(final Set<Vocabulary> vocabularies) {
		this.vocabularies = vocabularies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.core.model.community.Community#addVocabulary(com.collibra.dgc.core.model.representation.Vocabulary
	 * )
	 */
	@Override
	public void addVocabulary(Vocabulary vocabulary) {
		vocabularies.add(vocabulary);
		vocabulary.setCommunity(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.community.Community#getTopLevelCommunity()
	 */
	@Transient
	@Override
	public Community getTopLevelCommunity() {
		Community ret = this;
		while (ret.getParentCommunity() != null) {
			ret = ret.getParentCommunity();
		}
		return ret;
	}

	/**
	 * @return the isSBVR
	 */
	@Override
	public boolean isSBVR() {
		return isSBVR;
	}

	/**
	 * @param isSBVR the isSBVR to set
	 */
	public void setSBVR(boolean isSBVR) {
		this.isSBVR = isSBVR;
	}

	/**
	 * @return the isMeta
	 */
	@Override
	public boolean isMeta() {
		return isMeta;
	}

	/**
	 * @param isMeta the isMeta to set
	 */
	public void setMeta(boolean isMeta) {
		this.isMeta = isMeta;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Community))
			return false;
		Community other = (Community) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getParentCommunity() == null) {
			if (other.getParentCommunity() != null) {
				return false;
			}
		} else if (!getParentCommunity().equals(other.getParentCommunity())) {
			return false;
		}
		return true;
	}
}
