package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.user.Member;

public class CommunityMemberEventData extends MemberEventData {

	private final Community comm;

	public CommunityMemberEventData(Community comm, Member member, EventType eventType) {
		super(member, eventType);
		this.comm = comm;
	}

	public Community getCommunity() {
		return comm;
	}
}
