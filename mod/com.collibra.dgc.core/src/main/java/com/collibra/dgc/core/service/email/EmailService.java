/**
 * 
 */
package com.collibra.dgc.core.service.email;

import java.util.Map;
import java.util.Set;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;

/**
 * Service to send emails asynchronously with retries.
 * 
 * Configuration of the mail service is done inside the <core> element like this:
 * 
 * <pre>
 * {@code
 * 	<mail>
 * 	  <maximum-retries>5</maximum-retries>
 * 	  <port>25</port>
 * 	  <host>localhost</host>
 * 	  <username></username>
 * 	  <password></password>
 * 	  <from-address>info@collibra.com</from-address>
 * 	  <sending-threads>3</sending-threads>
 *  </mail>
 *  }
 * </pre>
 * 
 * Subject and body can be velocity templates. These templates are looked for in differnt places in the following order:
 * <ul>
 * <li>In directory <i>{user.home}/collibra/{contextPath}/email-template</i>. Note: '.vm' extension is added by the
 * service itself when looking for the template file.</li>
 * <li>In the configuration under <i>/configuration/system/core/mail/templates</i></li>
 * <li>In the classpath</li>
 * </ul>
 * @author dieterwachters
 */
public interface EmailService {
	/**
	 * The system property to use for disabling general email sending. To disable it:
	 * 
	 * <pre>
	 * System.setProperty(EmailService.EMAIL_DISABLED, &quot;true&quot;);
	 * </pre>
	 */
	public static final String EMAIL_DISABLED = "com.collibra.email.disabled";

	/**
	 * Send an email with the configured number of retries if something goes wrong.
	 * @param subject The subject string or location of the subject Velocity template.
	 * @param subjectIsTemplate If true, the subject string is interpreted as a Velocity template location. If false,
	 *            the subject string is taken as is.
	 * @param body The body string or location of the body Velocity template.
	 * @param bodyIsTemplate If true, the body string is interpreted as a Velocity template location. If false, the body
	 *            string is taken as is.
	 * @param model The optional model for passing to the Velocity render engine.
	 * @param receivers The list of receivers for this email.
	 * @param cc The list of people to put in CC.
	 * @param bcc The list of people to put in BCC.
	 * @throws DGCException With error code {@link DGCErrorCodes.EMAIL_RENDERING} when the rendering of the
	 *             email goes wrong.
	 */
	public void sendEmail(String subject, boolean subjectIsTemplate, String body, boolean bodyIsTemplate,
			Map<String, Object> model, Set<String> receivers, Set<String> cc, Set<String> bcc) throws DGCException;
}
