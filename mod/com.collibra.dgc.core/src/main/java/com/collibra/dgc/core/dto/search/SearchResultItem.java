/**
 * 
 */
package com.collibra.dgc.core.dto.search;

/**
 * Represents a search result item.
 * @author dieterwachters
 */
public class SearchResultItem {
	protected final String type;
	protected final String name;
	protected final String attribute;
	protected final SearchResultItem parent;
	protected final String resourceID;
	protected final double score;

	public SearchResultItem(final String name, final String type, final String resourceID, final String attribute,
			final double score) {
		this(name, type, resourceID, attribute, score, null);
	}

	public SearchResultItem(final String name, final String type, final String resourceID, final String attribute,
			final double score, final SearchResultItem parent) {
		this.type = type;
		this.attribute = attribute;
		this.resourceID = resourceID;
		this.name = name;
		this.parent = parent;
		this.score = score;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return the attribute
	 */
	public final String getAttribute() {
		return attribute;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the parent
	 */
	public final SearchResultItem getParent() {
		return parent;
	}

	/**
	 * @return the resourceID
	 */
	public final String getResourceID() {
		return resourceID;
	}

	/**
	 * @return the score
	 */
	public final double getScore() {
		return score;
	}
}
