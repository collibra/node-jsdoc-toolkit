/**
 * 
 */
package com.collibra.dgc.ui.core.velocity;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

import com.collibra.dgc.ui.core.MainUIController;

/**
 * Wrappping {@link VelocityView} to handle errors more nicely and to wrap the rendering in a transaction if necessary.
 * @author dieterwachters
 */
public class WrappedVelocityView extends VelocityToolboxView {
	static final Logger log = LoggerFactory.getLogger(WrappedVelocityView.class);
	private HttpServletRequest request;

	private PlatformTransactionManager txManager;

	protected void setTransactionManager(final PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.AbstractView#render(java.util.Map,
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.request = request;
		super.render(model, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.velocity.VelocityView#mergeTemplate(org.apache.velocity.Template,
	 * org.apache.velocity.context.Context, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void mergeTemplate(final Template template, final Context context, final HttpServletResponse response)
			throws Exception {
		// Check if we need to wrap this in a transaction or not.
		if (context.get(MainUIController.NO_TRANSACTION) != null
				&& (Boolean) context.get(MainUIController.NO_TRANSACTION)) {
			doMerge(template, context, response);
		} else {
			// Wrap the merging in a transaction
			new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					doMerge(template, context, response);
				}
			});
		}
	}

	private void doMerge(final Template template, final Context context, final HttpServletResponse response) {
		try {
			try {
				template.merge(context, response.getWriter());
			} catch (MethodInvocationException ex) {
				// In case of an error we redirect to the error page.
				log.error(
						"Error while rendering template '" + template.getName() + "'. Redirecting to the error page.",
						ex.getWrappedThrowable());
				response.sendRedirect(request.getContextPath()
						+ "/error?message="
						+ URLEncoder.encode(ex.getLocalizedMessage() == null ? "null" : ex.getLocalizedMessage(),
								"UTF-8"));
			} catch (Exception ex) {
				// In case of an error we redirect to the error page.
				log.error(
						"Error while rendering template '" + template.getName() + "'. Redirecting to the error page.",
						ex);
				response.sendRedirect(request.getContextPath()
						+ "/error?message="
						+ URLEncoder.encode(ex.getLocalizedMessage() == null ? "null" : ex.getLocalizedMessage(),
								"UTF-8"));
			}
		} catch (Exception e) {

		}
	}
}
