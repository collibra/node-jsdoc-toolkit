/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

/**
 * @author dieterwachters
 * 
 */
public class AbstractPageDefinitionVisitor implements PageDefinitionVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionVisitor#visit(com.collibra.dgc.ui.core.pagedefinition
	 * .model.PageDefinition)
	 */
	@Override
	public boolean visit(PageDefinition def) throws PageDefinitionException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionVisitor#visit(com.collibra.dgc.ui.core.pagedefinition
	 * .model.Region)
	 */
	@Override
	public boolean visit(Region region) throws PageDefinitionException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionVisitor#visit(com.collibra.dgc.ui.core.pagedefinition
	 * .model.ModuleConfiguration)
	 */
	@Override
	public boolean visit(ModuleConfiguration moduleConfig) throws PageDefinitionException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionVisitor#visit(com.collibra.dgc.ui.core.pagedefinition
	 * .model.MatchingRule)
	 */
	@Override
	public boolean visit(MatchingRule rule) throws PageDefinitionException {
		return true;
	}

}
