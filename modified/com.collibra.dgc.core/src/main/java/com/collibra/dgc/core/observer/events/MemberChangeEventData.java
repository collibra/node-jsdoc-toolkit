package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.Member;

/**
 * 
 * @author amarnath
 * 
 */
public class MemberChangeEventData extends MemberEventData {
	private final Member oldMember;

	public MemberChangeEventData(Member newMember, Member oldMember, EventType eventType) {
		super(newMember, eventType);
		this.oldMember = oldMember;
	}

	public Member getOldMember() {
		return oldMember;
	}
}
