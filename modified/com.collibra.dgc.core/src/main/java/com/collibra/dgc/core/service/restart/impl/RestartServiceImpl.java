/**
 * 
 */
package com.collibra.dgc.core.service.restart.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.service.restart.IRestartListener;
import com.collibra.dgc.core.service.restart.RestartService;

/**
 * @author dieterwachters
 * 
 */
@Service
public class RestartServiceImpl implements RestartService {
	@Autowired
	private ApplicationContext applicationContext;

	private static final Logger log = LoggerFactory.getLogger(RestartServiceImpl.class);

	private final Set<IRestartListener> restartListeners = new HashSet<IRestartListener>();

	@Override
	public void restart() {
		final long t1 = System.currentTimeMillis();
		log.info("Start refreshing spring context.");

		final Set<IRestartListener> listeners = new HashSet<IRestartListener>(restartListeners);
		// We don't want to keep the old stuff.
		restartListeners.clear();

		if (applicationContext instanceof AbstractRefreshableApplicationContext) {
			((AbstractRefreshableApplicationContext) applicationContext).refresh();
		}
		for (IRestartListener listener : listeners) {
			listener.restart();
		}

		final long t2 = System.currentTimeMillis();
		log.info("Restart of DGC took " + (t2 - t1) + "ms");
	}

	@Override
	public void addRestartListener(IRestartListener listener) {
		restartListeners.add(listener);
	}

	@Override
	public void removeRestartListener(IRestartListener listener) {
		restartListeners.remove(listener);
	}
}
