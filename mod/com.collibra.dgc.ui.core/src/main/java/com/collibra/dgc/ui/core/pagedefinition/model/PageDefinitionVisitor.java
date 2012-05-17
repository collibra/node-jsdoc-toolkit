/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

/**
 * Interface for Page Definition visitors.
 * @author dieterwachters
 */
public interface PageDefinitionVisitor {
	boolean visit(PageDefinition def) throws PageDefinitionException;

	boolean visit(Region region) throws PageDefinitionException;

	boolean visit(ModuleConfiguration moduleConfig) throws PageDefinitionException;

	boolean visit(MatchingRule rule) throws PageDefinitionException;
}
