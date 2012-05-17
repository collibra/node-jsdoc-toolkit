/**
 * 
 */
package com.collibra.dgc.ui.core.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lesscss4j.LessCompiler;
import com.googlecode.lesscss4j.LessException;

/**
 * @author dieterwachters
 * 
 */
public class CSSCompiler {
	private static final Logger log = LoggerFactory.getLogger(CSSCompiler.class);
	private static final String MODULE_PATH = "${modulePath}";
	private static final String CONTEXT_PATH = "${contextPath}";

	public static final void compile(final OutputStream out, final Collection<String> inputs,
			final Map<String, String> modulePaths, final String contextPath, final boolean optimize) throws IOException {
		if (inputs != null) {
			final StringBuilder buf = new StringBuilder();
			for (final String input : inputs) {
				concatenateFile(buf, input, modulePaths.get(input), contextPath);
			}
			final LessCompiler compiler = new LessCompiler();
			compiler.setCompress(optimize);
			try {
				final String compiled = compiler.compile(buf.toString());
				final Writer writer = new OutputStreamWriter(out);
				writer.write(compiled);
				writer.close();
			} catch (LessException e) {
				log.error("Error while compiling concatenated less code.", e);
				log.error("The failed LESS Code: \r\n" + buf.toString());
				throw new IOException("Error while compiling concatenated less code.", e);
			}
		}
	}

	/**
	 * Concatenates the given List of files.
	 */
	public static void concatenateFile(final StringBuilder buf, final String input, final String modulePath,
			final String contextPath) throws IOException {
		final InputStream in = ResourceServlet.getFileData(input);
		if (in == null) {
			throw new IOException("Unable to find file '" + input + "'.");
		}
		String content = IOUtils.toString(in, "UTF-8");
		if (modulePath != null) {
			content = content.replace(MODULE_PATH, modulePath);
			content = content.replace(CONTEXT_PATH, contextPath);
		}
		buf.append(content);
	}
}
