package com.collibra.dgc.core.observer.events;

import com.collibra.dgc.core.model.user.Member;

public class MemberEventData extends AbstractEventData implements EventData {
	private final Member member;

	public MemberEventData(Member member, EventType eventType) {
		super(eventType);
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
