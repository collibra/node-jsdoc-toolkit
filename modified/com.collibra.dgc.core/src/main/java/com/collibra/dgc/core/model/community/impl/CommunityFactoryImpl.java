package com.collibra.dgc.core.model.community.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;

@Service
public class CommunityFactoryImpl implements CommunityFactory {
	@Autowired
	private RepresentationFactory representationFactory;

	public Community makeCommunity(final String name, final String uri) {
		return makeCommunity(name, uri, "English");
	}

	public Community makeCommunity(String name, String uri, String language) {

		return makeCommunity(null, name, uri, language);
	}

	public Community makeCommunity(final Community community, final String name, final String uri) {
		return makeCommunity(community, name, uri, "English");
	}

	public Community makeCommunity(Community parent, String name, String uri, String language) {

		final Community community = new CommunityImpl();
		community.setParentCommunity(parent);
		community.setName(name);
		community.setUri(uri);
		community.setLanguage(language);
		if (parent != null) {
			parent.addSubCommunity(community);
		}

		return community;
	}
}
