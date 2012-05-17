package com.collibra.dgc.core.model.user;

public enum InstantMessagingAccountType {
	AOL("aol"), GTALK("gtalk"), ICQ("icq"), JABBER("jabber"), LIVE_MESSENGER("liveMessenger"), SKYPE("skype"), YAHOO_MESSENGER(
			"yahooMessenger");

	private final String localizationKey;

	private InstantMessagingAccountType(String localizationKey) {
		this.localizationKey = localizationKey;
	}

	public String getLocalizationKey() {
		return localizationKey;
	}

	@Override
	public String toString() {
		return localizationKey;
	}

}
