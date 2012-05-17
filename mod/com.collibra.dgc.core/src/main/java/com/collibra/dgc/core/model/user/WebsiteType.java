package com.collibra.dgc.core.model.user;

public enum WebsiteType {
	FACEBOOK("facebook"), LINKEDIN("linkedin"), MYSPACE("mySpace"), TWITTER("twitter"), WEBSITE("website");

	private final String localizationKey;

	private WebsiteType(String localizationKey) {
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
