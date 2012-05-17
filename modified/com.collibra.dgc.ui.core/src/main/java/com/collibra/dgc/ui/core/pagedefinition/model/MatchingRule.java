/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

/**
 * Represents a rule this page definition matches on.
 * @author dieterwachters
 */
public class MatchingRule {
	private final String path;
	private final String type;

	/**
	 * Constructor
	 * @param path The URL path to match on.
	 * @param type The resource id of the type of object to match on
	 */
	public MatchingRule(String path, final String type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public String getType() {
		return type;
	}

	public void accept(PageDefinitionVisitor visitor) throws PageDefinitionException {
		visitor.visit(this);
	}
}
