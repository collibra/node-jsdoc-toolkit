package com.collibra.dgc.core.model.user;

public enum PhoneType {
	FAX("Fax"), MOBILE("Mobile"), OTHER("Other"), PAGER("Pager"), PRIVATE("Private"), WORK("Work");

	private final String localizationKey;

	private PhoneType(String localizationKey) {
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
