package com.collibra.dgc.core.model.community;

import java.util.Set;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * A Community is a group of people having a particular unifying characteristic in common
 * 
 * @author dtrog
 * 
 */
public interface Community extends Resource, Verbalisable {

	/**
	 * 
	 * @return The name by which this Community is known.
	 */
	public String getName();

	/**
	 * 
	 * @param name The name by which this Community is known.
	 */
	public void setName(String name);

	/**
	 * 
	 * @return The Unique Resource Identifier for this {@link Community}.
	 */
	String getUri();

	/**
	 * @param uri The Unique Resource Identifier for this {@link Community}.
	 */
	void setUri(String namespace);

	/**
	 * 
	 * @return The subcommunities of this community
	 */
	Set<Community> getSubCommunities();

	/**
	 * Makes the given community a subcommunity of this one.
	 * 
	 * @param community The community to add as subcommunity.
	 */
	void addSubCommunity(Community community);

	/**
	 * 
	 * @return The parent community. <code>null</code> if top level community.
	 */
	Community getParentCommunity();

	/**
	 * 
	 * @param community The parent community. <code>null</code> if top level community.
	 */
	void setParentCommunity(Community community);

	/**
	 * 
	 * @return The language in which this Community communicates.
	 */
	String getLanguage();

	/**
	 * Set the language that this community communicates in.
	 * 
	 * @param language The language in which this community communicates.
	 */
	void setLanguage(String language);

	/**
	 * 
	 * @return All the vocabularies that are owned by this community.
	 */
	Set<Vocabulary> getVocabularies();

	/**
	 * Add a {@link Vocabulary} that this Community is the owner of.
	 * @param vocabulary The {@link Vocabulary} to own.
	 */
	void addVocabulary(Vocabulary vocabulary);

	/**
	 * Looks for the top level community (no parent).
	 * @return
	 */
	Community getTopLevelCommunity();

	/**
	 * Check if this community is the SBVR community. This is a helper field for filtering communities in a performant
	 * way.
	 */
	boolean isSBVR();

	/**
	 * Check if this community is a meta community (in the Metamodel community). This is a helper field for filtering
	 * communities in a performant way.
	 */
	boolean isMeta();
}
