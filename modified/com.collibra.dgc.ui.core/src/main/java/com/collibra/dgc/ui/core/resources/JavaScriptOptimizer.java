/**
 * 
 */
package com.collibra.dgc.ui.core.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;

/**
 * Helper class for optimizing JavaScript
 * @author dieterwachters
 */
public class JavaScriptOptimizer {
	private static final Logger log = LoggerFactory.getLogger(JavaScriptOptimizer.class);
	private static final String CONTEXT_PATH = "${contextPath}";

	private static String EXTERNS_CODE = "";
	static {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(JavaScriptOptimizer.class.getResourceAsStream("/com/collibra/dgc/ui/core/externs.js"), writer,
					"UTF-8");
			EXTERNS_CODE = writer.toString();
		} catch (IOException e) {
			log.error("Internal server error: Error reading externals.js");
		}
	}

	/**
	 * Optimize the the given list of javascript resources and write it into the outputstream. If supplied, also the
	 * translations are appended and optimized.
	 */
	public static final void optimizeJavaScripts(final OutputStream out, final String contextPath,
			final List<String> wjs, final Set<String> externs, final String translations) throws IOException {
		if (wjs != null) {
			final long t1 = System.currentTimeMillis();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// If there are translations, first add them to the stream.
			if (translations != null) {
				final OutputStreamWriter writer = new OutputStreamWriter(baos);
				writer.write(translations);
				writer.flush();
			}
			// Concatenate all the javascript file together.
			concatenateFiles(wjs, baos, contextPath);
			baos.close();
			final long t2 = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("Done concatenating javascript files in " + (t2 - t1) + "ms.");
			}

			// Now optimize the JavaScript using
			final Compiler compiler = new Compiler();
			CompilerOptions options = new CompilerOptions();
			CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

			JSSourceFile[] externsArray = new JSSourceFile[1 + externs.size()];
			externsArray[0] = JSSourceFile.fromCode("externs.js", EXTERNS_CODE);
			int i = 1;
			for (final String external : externs) {
				externsArray[i] = JSSourceFile.fromCode("externs" + i + ".js", external);
				i++;
			}

			JSSourceFile input = JSSourceFile.fromInputStream("input.js", new ByteArrayInputStream(baos.toByteArray()));
			compiler.compile(externsArray, new JSSourceFile[] { input }, options);

			// Surround it with an anonymous function.
			String result = "(function() {" + compiler.toSource() + "})();";
			// result = result.replace(CONTEXT_PATH, contextPath);
			out.write(result.getBytes("UTF-8"));

			final long t3 = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("Done optimizing javascript in " + (t3 - t2) + "ms.");
			}
		}
	}

	/**
	 * Concatenates the given List of files.
	 */
	public static void concatenateFiles(final List<String> inputs, final OutputStream out, String contextPath)
			throws IOException {
		for (final String input : inputs) {
			final InputStream in = ResourceServlet.getFileData(input);
			if (in == null) {
				throw new IOException("Unable to find file '" + input + "'.");
			}
			try {
				IOUtils.copy(replaceContextPath(in, contextPath), out);
			} finally {
				in.close();
			}
		}
	}

	private static InputStream replaceContextPath(final InputStream is, final String contextPath) throws IOException,
			UnsupportedCharsetException {
		String result = IOUtils.toString(is);
		result = result.replace(CONTEXT_PATH, contextPath);
		return new ByteArrayInputStream(result.getBytes("UTF-8"));
	}
}
