package com.collibra.dgc.core.service.license.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.LicenseKeyException;
import com.collibra.dgc.core.service.license.LicenseService;
import com.smardec.license4j.License;
import com.smardec.license4j.LicenseManager;
import com.smardec.license4j.LicenseNotFoundException;

/**
 * Quartz job for checking the license validity every day.
 * @author GKDAI63
 * 
 */
@Component
public class LicenseServiceImpl extends QuartzJobBean implements LicenseService {
	private static LicenseService INSTANCE;

	private final Logger log = LoggerFactory.getLogger(Application.class);
	private Date expireDate = new Date(0);
	private Boolean guestAccessAllowed = false;
	private Integer concurrentUserCount = 3;
	private boolean valid = false;

	public LicenseServiceImpl() {
		INSTANCE = this;
	}

	public static LicenseService getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer getConcurrentUserCount() {
		return concurrentUserCount;
	}

	@Override
	public Date getExpireDate() {
		return expireDate;
	}

	@Override
	public Boolean isGuestAccessAllowed() {
		return guestAccessAllowed;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		validateLicense();
	}

	@Override
	public void setLicense(InputStream is) {

		File currentLicense = new File(Application.USER_HOME, LicenseService.LICENSE_FILE);
		try {

			byte[] licenseArray = IOUtils.toByteArray(is);
			ByteArrayInputStream bais = new ByteArrayInputStream(licenseArray);
			License license = LicenseManager.loadLicense(bais);
			bais.reset();
			this.setLicense(license);
			currentLicense.delete();
			FileUtils.copyInputStreamToFile(bais, currentLicense);
		} catch (IOException ex) {
			log.error("Failed to write new license to disk", ex);
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_UNWRITABLE);
		} catch (LicenseNotFoundException ex) {
			log.error("Could not find license on disk", ex);
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_UNREADABLE);
		}
	}

	private void setLicense(License license) throws DGCException, LicenseKeyException {

		Integer concurrentUserCount = 0;
		Boolean guestAccessAllowed = false;
		Date expireDate = null;
		String key = null;

		try {
			key = getPublicKey();
		} catch (IOException e) {
			throw new DGCException(DGCErrorCodes.LICENSE_UNINITIALIZED_PUBLIC_KEY);
		}
		LicenseManager.setPublicKey(key);
		try {
			boolean isValid = LicenseManager.isValid(license);
			if (!isValid) {
				log.warn("License is invalid");
				throw new LicenseKeyException(DGCErrorCodes.LICENSE_INVALID);
			}
			this.setValid(isValid);
		} catch (GeneralSecurityException e1) {
			log.warn("Public key was not set before calling isValid()");
			throw new DGCException(DGCErrorCodes.LICENSE_UNINITIALIZED_PUBLIC_KEY);
		}

		// Validate concurrent Users
		String concurrentUserCountProperty = (String) license.getFeature(CONCURRENT_USER_FEATURE);

		if (concurrentUserCountProperty == null || concurrentUserCountProperty == "") {
			this.setValid(false);
			log.warn("Could not read" + CONCURRENT_USER_FEATURE + " from license");
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_FIELD_NOT_PRESENT, CONCURRENT_USER_FEATURE);
		}
		try {
			concurrentUserCount = Integer.parseInt(concurrentUserCountProperty);

		} catch (NumberFormatException ex) {
			log.warn("Could not read" + CONCURRENT_USER_FEATURE + " from license", ex);
			this.setValid(false);
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_FIELD_NOT_PRESENT, CONCURRENT_USER_FEATURE);

		}
		// validation guest access
		String guestAccess = (String) license.getFeature(GUEST_ACCESS);
		if (guestAccess == null || guestAccess.equals("")) {
			this.setValid(false);
			log.warn("Could not read" + GUEST_ACCESS + " from license");
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_FIELD_NOT_PRESENT, GUEST_ACCESS);
		}
		guestAccessAllowed = Boolean.valueOf(guestAccess);

		// Validate Expiration Date
		String expire = (String) license.getFeature(EXPIRE_KEY);

		if (expire == null || expire == "") {
			this.setValid(false);
			log.warn("Could not read" + CONCURRENT_USER_FEATURE + " from license");
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_FIELD_NOT_PRESENT, EXPIRE_KEY);
		}

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			expireDate = dateFormatter.parse(expire);

			if (expireDate.compareTo(new Date()) < 0) {
				log.warn("Could not read" + EXPIRE_KEY + " from license");
				throw new LicenseKeyException(DGCErrorCodes.LICENSE_EXPIRED);
			}

		} catch (ParseException e) {
			log.warn("invalid expiration date in license.");
			this.setValid(false);
			throw new LicenseKeyException(DGCErrorCodes.LICENSE_INVALID);
		}

		this.setMaximumNumberOfConcurrentUsers(concurrentUserCount);
		this.setGuestAccessAllowed(guestAccessAllowed);
		this.setExpirationDate(expireDate);

	}

	private void setValid(boolean valid) {
		this.valid = valid;
	}

	private void setMaximumNumberOfConcurrentUsers(Integer concurrentUserCount) {
		this.concurrentUserCount = concurrentUserCount;
	}

	private void setGuestAccessAllowed(Boolean guestAccessAllowed) {
		this.guestAccessAllowed = guestAccessAllowed;
	}

	private void setExpirationDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	private String getPublicKey() throws IOException {
		String key = IOUtils.toString(LicenseServiceImpl.class.getResourceAsStream(PUBLIC_KEY_FILE));
		return key;
	}

	@Override
	public void validateLicense() {

		File licenseFile = new File(Application.USER_HOME, LICENSE_FILE);

		if (!licenseFile.exists()) {
			log.warn("No license file found at " + licenseFile.getAbsolutePath() + ".");
			this.setValid(false);
		} else {
			License license = null;
			try {
				license = LicenseManager.loadLicense(licenseFile.getAbsolutePath());
			} catch (LicenseNotFoundException e) {
				log.error("could not load license in cron", e);
			}

			try {
				setLicense(license);
			} catch (LicenseKeyException e) {
				log.error("Problem encountered with licensing", e);
			}
		}
	}
}
