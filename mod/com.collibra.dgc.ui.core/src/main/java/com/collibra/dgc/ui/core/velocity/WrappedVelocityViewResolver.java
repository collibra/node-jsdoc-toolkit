/**
 * 
 */
package com.collibra.dgc.ui.core.velocity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * Wrapping the {@link VelocityViewResolver} to handle error handling more nicely and wrapping rendering in a
 * transaction.
 * @author dieterwachters
 */
public class WrappedVelocityViewResolver extends VelocityViewResolver {
	@Autowired
	private PlatformTransactionManager txManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#setViewClass(java.lang.Class)
	 */
	@Override
	public void setViewClass(Class viewClass) {
		if (viewClass.equals(VelocityToolboxView.class)) {
			super.setViewClass(WrappedVelocityView.class);
		} else {
			super.setViewClass(viewClass);
		}
	}

	/**
	 * Requires {@link VelocityView}.
	 */
	@Override
	protected Class requiredViewClass() {
		return WrappedVelocityView.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.velocity.VelocityViewResolver#buildView(java.lang.String)
	 */
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		if (view instanceof WrappedVelocityView) {
			((WrappedVelocityView) view).setTransactionManager(txManager);
		}
		return view;
	}
}
