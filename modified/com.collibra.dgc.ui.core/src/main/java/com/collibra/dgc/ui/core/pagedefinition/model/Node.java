/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

/**
 * The abstract super class for
 * @author dieterwachters
 */
public abstract class Node {
	protected final String name;
	protected final Node parent;
	protected final PageDefinition pageDefinition;

	/**
	 * Constructor
	 */
	public Node(final PageDefinition pageDefinition, final Node parent, final String name) {
		this.pageDefinition = pageDefinition;
		this.name = name;
		this.parent = parent;
	}

	/**
	 * Retrieve the name of this node.
	 * @return The name of this node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieve the parent of this node in the tree
	 * @return The parent of this node.
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Build the path for this element in the page definition tree.
	 */
	public String getPath() {
		return (parent == null ? "" : parent.getPath()) + "|" + name;
	}

	/**
	 * Find the node that matches the given path.
	 */
	public abstract Node findMatchingNode(String path) throws PageDefinitionException;

	public abstract void accept(PageDefinitionVisitor visitor) throws PageDefinitionException;
}
