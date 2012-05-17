/**
 * 
 */
package com.collibra.dgc.ui.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.application.DGCDataSourceFactory;
import com.collibra.dgc.core.component.ApplicationComponent;
import com.collibra.dgc.core.component.CategorizationComponent;
import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.HistoryComponent;
import com.collibra.dgc.core.component.ReportComponent;
import com.collibra.dgc.core.component.RightsComponent;
import com.collibra.dgc.core.component.RulesComponent;
import com.collibra.dgc.core.component.StatusComponent;
import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.component.WorkflowComponent;
import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.security.TooManyConcurrentUsersException;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.i18n.I18nService;
import com.collibra.dgc.core.service.license.LicenseService;
import com.collibra.dgc.core.service.restart.IRestartListener;
import com.collibra.dgc.core.service.restart.RestartService;
import com.collibra.dgc.ui.core.modules.Module;
import com.collibra.dgc.ui.core.modules.ModuleService;
import com.collibra.dgc.ui.core.pagedefinition.PageData;
import com.collibra.dgc.ui.core.pagedefinition.PageDefinitionService;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionException;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionInstance;

/**
 * The main Spring MVC controller for the UI
 * 
 * @author dieterwachters
 */
@Controller
@Lazy(true)
public class MainUIController implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(MainUIController.class);

	protected static final String PAGE_MODULE = "pages/shared/page";
	protected static final String CONTENT = "content";
	protected static final String MODULES = "modules";
	protected static final String MODULE_JAVASCRIPTS = "moduleJavascripts";
	protected static final String MODULE_STYLESHEETS = "moduleStylesheets";
	protected static final String MESSAGES = "msg";
	protected static final String CONTEXT_PATH = "contextPath";
	protected static final String PARAMETERS = "params";
	protected static final String ID = "id";
	protected static final String OPTIMIZE_CSS = "optimizeCSS";
	protected static final String OPTIMIZE_JS = "optimizeJS";
	protected static final String DEVELOPER = "developer";
	protected static final String NOCONTROLS = "noControls";
	public static final String NO_TRANSACTION = "noTransaction";

	protected static final String APPLICATION_COMPONENT = "applicationComponent";
	protected static final String ATTRIBUTE_COMPONENT = "attributeComponent";
	protected static final String ATTRIBUTETYPE_COMPONENT = "attributeTypeComponent";
	protected static final String CATEGORIZATION_COMPONENT = "categorizationComponent";
	protected static final String COMMUNITY_COMPONENT = "communityComponent";
	protected static final String HISTORY_COMPONENT = "historyComponent";
	protected static final String STATUS_COMPONENT = "statusComponent";
	protected static final String TERM_COMPONENT = "termComponent";
	protected static final String REPRESENTATION_COMPONENT = "representationComponent";
	protected static final String RIGHTS_COMPONENT = "rightsComponent";
	protected static final String RULES_COMPONENT = "rulesComponent";
	protected static final String VERBALISER_COMPONENT = "verbaliserComponent";
	protected static final String VOCABULARY_COMPONENT = "vocabularyComponent";
	protected static final String WORKFLOW_COMPONENT = "workflowComponent";
	protected static final String USER_COMPONENT = "userComponent";
	protected static final String REPORT_COMPONENT = "reportComponent";

	protected static final String PAGE_DATA = "pd";
	protected static final String JSON_PARSER = "jsonParser";

	protected static final String REGION = "region";
	protected static final String BARE = "bare";
	protected static final String EXCLUDE_MODULES = "exclude-modules";

	@Autowired
	protected I18nService i18nService;
	@Autowired
	protected ModuleService moduleService;
	@Autowired
	private ApplicationComponent applicationComponent;
	@Autowired
	private AttributeComponent attributeComponent;
	@Autowired
	private AttributeTypeComponent attributeTypeComponent;
	@Autowired
	private CategorizationComponent categorizationComponent;
	@Autowired
	private CommunityComponent communityComponent;
	@Autowired
	private HistoryComponent historyComponent;
	@Autowired
	private StatusComponent statusComponent;
	@Autowired
	private TermComponent termComponent;
	@Autowired
	@Qualifier("RepresentationComponentImpl")
	private RepresentationComponent representationComponent;
	@Autowired
	private RightsComponent rightsComponent;
	@Autowired
	private RulesComponent rulesComponent;
	@Autowired
	private VerbaliserComponent verbaliserComponent;
	@Autowired
	private VocabularyComponent vocabularyComponent;
	@Autowired
	private WorkflowComponent workflowComponent;
	@Autowired
	private UserComponent userComponent;
	@Autowired
	private ReportComponent reportComponent;

	@Autowired
	private RestartService restartService;
	@Autowired
	private ConfigurationService config;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private PageDefinitionService pageDefinitionService;

	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private LicenseService licenseService;

	/**
	 * Render the page with the given overridden path.
	 */
	protected ModelAndView renderPage(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model, String path) {
		if (model == null) {
			model = new HashMap<String, Object>();
		}
		try {
			final long tStart = System.currentTimeMillis();
			final PageData pageData = pageDefinitionService.createPageData(path, request.getParameter(REGION),
					"true".equalsIgnoreCase(request.getParameter(BARE)));
			if (pageData == null) {
				return renderPageNotFound(request, response);
			}

			final long t2 = System.currentTimeMillis();
			log.info("Retrieving the page data object for '" + path + "' took " + (t2 - tStart) + "ms.");

			final Locale locale = i18nService.getUserLocalization(request);
			final String context = request.getContextPath() + "/";

			final PageDefinitionInstance pdi = pageData.getPageDefinition().getPageDefinitionInstance(i18nService,
					locale, context, request.getParameter(EXCLUDE_MODULES));

			model.put(MODULE_JAVASCRIPTS, pdi.getJavascriptInclusions());
			model.put(MODULE_STYLESHEETS, pdi.getCSSInclusions());
			final Map<String, Module> moduleMap = pageData.getPageDefinition().getModuleMap();
			model.put(MESSAGES, pdi.getMessages());
			model.put(CONTEXT_PATH, context);
			model.put(PARAMETERS, request.getParameterMap());
			model.put(OPTIMIZE_CSS, pageData.getPageDefinition().getOptimizeCSS());
			model.put(OPTIMIZE_JS, pageData.getPageDefinition().getOptimizeJS());
			model.put(DEVELOPER, Application.DEVELOPER);
			if (!model.containsKey(NOCONTROLS)) {
				model.put(NOCONTROLS, false);
			}
			if (pageData.getResourceId() != null) {
				model.put(ID, pageData.getResourceId());
			}

			// Adding all the components
			model.put(APPLICATION_COMPONENT, applicationComponent);
			model.put(ATTRIBUTE_COMPONENT, attributeComponent);
			model.put(ATTRIBUTETYPE_COMPONENT, attributeTypeComponent);
			model.put(CATEGORIZATION_COMPONENT, categorizationComponent);
			model.put(COMMUNITY_COMPONENT, communityComponent);
			model.put(HISTORY_COMPONENT, historyComponent);
			model.put(STATUS_COMPONENT, statusComponent);
			model.put(TERM_COMPONENT, termComponent);
			model.put(REPRESENTATION_COMPONENT, representationComponent);
			model.put(RIGHTS_COMPONENT, rightsComponent);
			model.put(RULES_COMPONENT, rulesComponent);
			model.put(VERBALISER_COMPONENT, verbaliserComponent);
			model.put(VOCABULARY_COMPONENT, vocabularyComponent);
			model.put(WORKFLOW_COMPONENT, workflowComponent);
			model.put(USER_COMPONENT, userComponent);
			model.put(REPORT_COMPONENT, reportComponent);

			final long tEnd = System.currentTimeMillis();
			log.info("Gathering render-model for page " + path + " took " + (tEnd - tStart) + "ms.");

			final Module pageModule = moduleMap.get(pageData.getPageDefinition().getModuleConfiguration().getName());
			model.put(PAGE_DATA, pageData);
			String pm = pageModule.getTemplate();
			if (!pm.startsWith("/")) {
				pm = "/" + pm;
			}
			if (pm.endsWith(".vm")) {
				pm = pm.substring(0, pm.length() - 3);
			}

			return new ModelAndView(pm, model);

		} catch (PageDefinitionException ex) {
			model.put("contextPath", request.getContextPath() + "/");
			final StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			model.put("exception", ex);
			model.put("message", ex.getLocalizedMessage() == null ? "null" : ex.getLocalizedMessage());
			String stacktrace = sw.getBuffer().toString().replace(System.getProperty("line.separator"), "<br/>\n");
			model.put("stacktrace", stacktrace);
			return new ModelAndView("pages/error/error", model);
		}
	}

	protected ModelAndView renderPage(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) {
		String path = request.getRequestURI();
		path = path.substring(request.getContextPath().length());

		return renderPage(request, response, model, path);
	}

	@RequestMapping("/**")
	public ModelAndView handlePageRendering(HttpServletRequest request, HttpServletResponse response) {
		final long t1 = System.currentTimeMillis();
		if (!licenseService.isValid()) {
			// If we are an admin and the license expired we redirect to the license upload page
			if (authorizationService.isPermitted(Permissions.ADMIN)) {
				Map<String, Object> model = new HashMap<String, Object>();
				return new ModelAndView("pages/license/license", model);

				// If we aren't admin and the license expires, we redirect to the error page with as message
				// "License expired"
			} else {
				Map<String, Object> model = new HashMap<String, Object>();
				// TODO localization
				model.put("message", "License expired");
				return new ModelAndView("pages/error/error", model);

				// Otherwise we just render the page
			}
		} else {
			final ModelAndView ret = renderPage(request, response, null);
			final long t2 = System.currentTimeMillis();
			log.info("Rendering of '" + request.getRequestURI() + "' took " + (t2 - t1) + "ms in total.");
			return ret;
		}
	}

	@RequestMapping("/initialize")
	public ModelAndView initialize(HttpServletRequest request, HttpServletResponse response) {
		if (!DGCDataSourceFactory.isDataSourceCreated()) {
			final Map<String, Object> model = new HashMap<String, Object>();
			model.put(NO_TRANSACTION, true);
			model.put(NOCONTROLS, true);
			return renderPage(request, response, model);
		}

		return renderPageNotFound(request, response);
	}

	@RequestMapping("/bootstrap")
	public ModelAndView bootstrap(HttpServletRequest request, HttpServletResponse response) {
		if (DGCDataSourceFactory.isDataSourceCreated()) {
			Vocabulary sbvrvoc = null;
			try {
				sbvrvoc = vocabularyComponent.getVocabularyByUri(Constants.SBVR_VOC);
			} catch (Exception e) {
			}
			if (sbvrvoc == null || rightsComponent.isPermitted(Permissions.ADMIN)) {
				final Map<String, Object> model = new HashMap<String, Object>();
				model.put(NOCONTROLS, true);
				return renderPage(request, response, model);
			}
		}

		return renderPageNotFound(request, response);
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		final Map<String, Object> model = new HashMap<String, Object>();
		if (request.getAttribute("shiroLoginFailure") != null) {
			final Object loginFailure = request.getAttribute("shiroLoginFailure");
			if (loginFailure.equals(UnknownAccountException.class.getName())) {
				model.put("loginFailure", "login." + DGCErrorCodes.LOGIN_UNKNOWN_ACCOUNT);
			} else if (loginFailure.equals(IncorrectCredentialsException.class.getName())) {
				model.put("loginFailure", "login." + DGCErrorCodes.LOGIN_INCORRECT_CREDENTIALS);
			} else if (loginFailure.equals(DisabledAccountException.class.getName())) {
				model.put("loginFailure", "login." + DGCErrorCodes.LOGIN_DISABLED_ACCOUNT);
			} else if (loginFailure.equals(TooManyConcurrentUsersException.class.getName())) {
				model.put("loginFailure", "login." + DGCErrorCodes.LOGIN_TOO_MANY_CONCURRENT_USERS);
			} else {
				model.put("loginFailure", "login." + DGCErrorCodes.LOGIN_FAILED);
			}
		}
		return renderPage(request, response, model);
	}

	@RequestMapping("/license")
	public ModelAndView license(HttpServletRequest request, HttpServletResponse response) {
		final Map<String, Object> model = new HashMap<String, Object>();
		if (rightsComponent.isPermitted(Permissions.ADMIN)) {
			return renderPage(request, response, model);
		} else {
			try {
				response.sendRedirect(request.getContextPath() + "/login");
			} catch (IOException e) {
				log.warn("Failed to redirect non-admin user to login page", e);
			}
			return null;
		}
	}

	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("contextPath", request.getContextPath() + "/");
		model.put("message", request.getParameter("message"));
		return new ModelAndView("pages/error/error", model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		restartService.addRestartListener(new IRestartListener() {
			@Override
			public void restart() {
				if (applicationContext instanceof AbstractRefreshableApplicationContext) {
					((AbstractRefreshableApplicationContext) applicationContext).refresh();
				}
			}
		});

		// Clear the generated cache folder.
		final File folder = UICore.getCacheGeneratedDirectory();
		log.info("Clearing cache folder '" + folder.getAbsolutePath() + "'");
		if (folder.exists()) {
			final File[] files = folder.listFiles();
			for (final File file : files) {
				file.delete();
			}
		}

		// Contribute the default configuration values for the UI core.
		config.contributeDefaults(getClass().getResource("/com/collibra/dgc/ui/core/default-config.xml"));
	}

	protected ModelAndView renderPageNotFound(final HttpServletRequest req, final HttpServletResponse response) {
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "Data Governance Center - Page not found");
		model.put("path", req.getRequestURI());

		return renderPage(req, response, model, "notfound");
	}
}
