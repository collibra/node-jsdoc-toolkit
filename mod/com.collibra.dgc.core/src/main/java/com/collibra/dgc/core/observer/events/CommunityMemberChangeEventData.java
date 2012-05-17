package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.user.Member;

/**
 * 
 * @author amarnath
 * 
 */
public class CommunityMemberChangeEventData extends CommunityMemberEventData {
	private final Member oldMember;

	public CommunityMemberChangeEventData(Community comm, Member newMember, Member oldMember, EventType eventType) {
		super(comm, newMember, eventType);
		this.oldMember = oldMember;
	}

	public Member getOldMember() {
		return oldMember;
	}
}
