/**
 * 
 */
package com.collibra.dgc.core.component.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.component.ApplicationComponent;

/**
 * @author dieterwachters
 * 
 */
@Service
public class ApplicationComponentImpl implements ApplicationComponent {

	public String getVersion() {
		return Application.VERSION;
	}

	public String getBuildNumber() {
		return Application.BUILD_NUMBER;
	}
}
