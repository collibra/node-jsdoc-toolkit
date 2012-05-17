/**
 * 
 */
package com.collibra.dgc.ui.core.modules;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for handling modules (finding them etc).
 * @author dieterwachters
 */
@Service
public class ModuleService {
	@Autowired
	private IModuleProvider[] moduleProviders;
	private List<IModuleProvider> moduleProviderList;

	/**
	 * Found the module with the given name.
	 */
	public Module findModule(final String name) {
		synchronized (this) {
			if (moduleProviderList == null) {
				moduleProviderList = Arrays.asList(moduleProviders);
				Collections.sort(moduleProviderList, new Comparator<IModuleProvider>() {
					@Override
					public int compare(IModuleProvider o1, IModuleProvider o2) {
						return o1.getOrder() - o2.getOrder();
					}
				});
			}
		}
		for (final IModuleProvider mp : moduleProviderList) {
			final Module m = mp.getModule(name);
			if (m != null) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Found the last modified of the module with the given name.
	 */
	public long getModuleLastModified(final String name) {
		final Module module = findModule(name);
		return module == null ? -1 : module.getLastmodified();
	}
}
