/**
 * 
 */
package com.collibra.dgc.core.service.email.impl;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.email.EmailService;

/**
 * Implementation of the email service using an ExecutorService.
 * @author dieterwachters
 */
@Service
public class EmailServiceImpl implements EmailService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private ConfigurationService configService;

	private VelocityEngine velocityEngine;

	private ExecutorService executor;

	@Override
	public void sendEmail(String subject, boolean subjectIsTemplate, String body, boolean bodyIsTemplate,
			Map<String, Object> model, Set<String> receivers, Set<String> cc, Set<String> bcc) throws DGCException {
		if ("true".equals(System.getProperty(EMAIL_DISABLED))) {
			return;
		}
		try {
			executor.submit(new EmailSenderWithRetry(new Email(subject, subjectIsTemplate, body, bodyIsTemplate, model,
					receivers, cc, bcc)));
		} catch (Exception e) {
			throw new DGCException(e, DGCErrorCodes.EMAIL_RENDERING, subject);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		executor = Executors.newFixedThreadPool(configService.getInteger("core/mail/sending-threads"));

		velocityEngine = new VelocityEngine();

		velocityEngine.addProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
				Application.EMAIL_TEMPLATES_DIR.getAbsolutePath());
		velocityEngine.addProperty("file.resource.loader.class",
				"com.collibra.dgc.core.service.email.impl.VMFileResourceLoader");

		velocityEngine.addProperty(RuntimeConstants.RESOURCE_LOADER, "config");
		velocityEngine.addProperty("config.resource.loader.class",
				"com.collibra.dgc.core.service.email.impl.ConfigurationResourceLoader");
		velocityEngine.addProperty("config.resource.loader.configService", configService);

		velocityEngine.addProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath." + RuntimeConstants.RESOURCE_LOADER + ".class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.setProperty("classpath." + RuntimeConstants.RESOURCE_LOADER + ".cache", "true");

		velocityEngine.init();
	}

	/**
	 * The runnable to send emails. This will automatically do retries by adding a new runnable to the end of the
	 * execution queue.
	 * @author dieterwachters
	 */
	private class EmailSenderWithRetry implements Runnable {
		private int retry = 0;
		private final Email email;

		public EmailSenderWithRetry(final Email email) {
			retry = 0;
			this.email = email;
		}

		private EmailSenderWithRetry(int retry, final Email email) {
			this.retry = retry;
			this.email = email;
		}

		@Override
		public void run() {
			try {
				log.info("Sending email '" + email + "'" + (retry > 0 ? " (retry " + retry + ")." : "."));
				if (retry > 0) {
					Thread.sleep(Math.min(30000, 10000 * retry));
				}
				email.send();
			} catch (Exception e) {
				final int maxRetries = configService.getInteger("core/mail/maximum-retries");
				if (retry < maxRetries) {
					log.warn("Failed sending ending email '" + email + "'"
							+ (retry > 0 ? " (retry " + retry + ")." : ".") + " Requested a retry.", e);
					executor.execute(new EmailSenderWithRetry(retry + 1, email));
				} else {
					log.error("Unable to send email '" + email + "' after " + retry + " retries. Giving up.", e);
				}
			}
		}
	}

	/**
	 * Class representing an email.
	 * @author dieterwachters
	 */
	private class Email {
		private final MimeMessage message;
		private String body = null;
		private String subject = null;

		private final Set<String> receivers;

		/**
		 * Constructor
		 */
		public Email(String subject, boolean subjectIsTemplate, String body, boolean bodyIsTemplate,
				Map<String, Object> model, Set<String> receivers, Set<String> cc, Set<String> bcc) throws Exception {

			this.receivers = receivers;

			this.body = bodyIsTemplate ? renderTemplate(body, model) : body;
			this.subject = subjectIsTemplate ? renderTemplate(subject, model) : subject;
			this.body = this.body.trim();
			this.subject = this.subject.trim();

			final Properties properties = new Properties();
			// Setup mail server
			properties.setProperty("mail.smtp.host", configService.getString("core/mail/host"));
			properties.setProperty("mail.smtp.port", configService.getString("core/mail/port"));
			properties.setProperty("mail.user", configService.getString("core/mail/username"));
			properties.setProperty("mail.password", configService.getString("core/mail/password"));

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);

			// Create a default MimeMessage object.
			message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(configService.getString("core/mail/from-address")));

			// Set To: header field of the header.
			for (final String receiver : receivers) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			}

			if (cc != null && cc.size() > 0) {
				for (final String receiver : cc) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(receiver));
				}
			}

			if (bcc != null && bcc.size() > 0) {
				for (final String receiver : bcc) {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(receiver));
				}
			}

			// Set Subject: header field
			message.setSubject(this.subject);

			// Now set the actual message
			message.setText(this.body);
		}

		public void send() throws Exception {
			Transport.send(message);
		}

		private String renderTemplate(String templateLocation, final Map<String, Object> model) {
			final Template template = velocityEngine.getTemplate(templateLocation);
			final VelocityContext velocityContext = new VelocityContext(model);

			final StringWriter sw = new StringWriter();
			template.merge(velocityContext, sw);

			return sw.getBuffer().toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "[Recipents: " + receivers + "; Subject: " + subject + "]";
		}
	}
}
