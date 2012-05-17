/**
 * 
 */
package com.collibra.dgc.core.util;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.collibra.dgc.core.application.Application;

/**
 * Helper class to quickly start a file monitoring thread.
 * @author dieterwachters
 */
public class FileMonitor {
	public enum EEvent {
		DELETE, CREATE, CHANGE, DIRECTORY_DELETE, DIRECTORY_CHANGE
	};

	public interface IExecutor {
		public void run(final File file, final EEvent event);
	}

	/**
	 * Start a file monitor on the given directory. This will create a daemon thread and listen on all the possible
	 * events.
	 * @param directory The directory to check for changes.
	 * @param executor The code to call when something changed.
	 * @param interval The interval to check at.
	 * @return The monitor that was started.
	 * @throws Exception
	 */
	public static FileAlterationMonitor startFileMonitor(File directory, final IExecutor executor, long interval)
			throws Exception {
		return startFileMonitor(directory, executor, interval, null);
	}

	/**
	 * Start a file monitor on the given directory. This will create a daemon thread.
	 * @param directory The directory to check for changes.
	 * @param executor The code to call when something changed.
	 * @param interval The interval to check at.
	 * @param events The list of {@link EEvent}s to listen too.
	 * @return The monitor that was started.
	 * @throws Exception
	 */
	public static FileAlterationMonitor startFileMonitor(File directory, final IExecutor executor, long interval,
			final List<EEvent> events) throws Exception {
		final FileAlterationMonitor alterationMonitor = new FileAlterationMonitor(interval);
		alterationMonitor.setThreadFactory(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable arg0) {
				final Thread t = new Thread(arg0);
				t.setDaemon(true);
				return t;
			}
		});
		final FileAlterationObserver observer = new FileAlterationObserver(Application.TRANSLATIONS_DIR);
		observer.addListener(new FileAlterationListenerAdaptor() {
			@Override
			public void onFileDelete(File file) {
				if (events == null || events.contains(EEvent.DELETE)) {
					executor.run(file, EEvent.DELETE);
				}
			}

			@Override
			public void onFileCreate(File file) {
				if (events == null || events.contains(EEvent.CREATE)) {
					executor.run(file, EEvent.CREATE);
				}
			}

			@Override
			public void onFileChange(File file) {
				if (events == null || events.contains(EEvent.CHANGE)) {
					executor.run(file, EEvent.CHANGE);
				}
			}

			@Override
			public void onDirectoryDelete(File directory) {
				if (events == null || events.contains(EEvent.DIRECTORY_DELETE)) {
					executor.run(directory, EEvent.DIRECTORY_DELETE);
				}
			}

			@Override
			public void onDirectoryChange(File directory) {
				if (events == null || events.contains(EEvent.DIRECTORY_CHANGE)) {
					executor.run(directory, EEvent.DIRECTORY_CHANGE);
				}
			}
		});
		alterationMonitor.addObserver(observer);
		alterationMonitor.start();
		return alterationMonitor;
	}
}
