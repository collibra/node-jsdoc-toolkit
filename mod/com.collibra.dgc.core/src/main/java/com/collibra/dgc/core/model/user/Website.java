package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

/**
 * Representation of a website
 * @author GKDAI63
 * 
 */
public interface Website extends Resource {
	/**
	 * Sets the url of the website
	 * @param url
	 */
	void setUrl(String url);

	/**
	 * Sets the {@link WebsiteType}
	 * @param type the {@link WebsiteType}
	 */
	void setWebsiteType(WebsiteType type);

	/**
	 * Retrieves the {@link WebsiteType}
	 * @return the {@link WebsiteType}
	 */
	WebsiteType getWebsiteType();

	/**
	 * Retrieves the url
	 * @return
	 */
	String getUrl();

}
