package com.collibra.dgc.core.model.user;

public enum AddressType {
	HOME("home"), WORK("work");

	private String localizationKey;

	private AddressType(String localizationKey) {
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
