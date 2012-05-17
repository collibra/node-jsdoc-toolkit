import java.io.File;

import com.googlecode.lesscss4j.LessCompiler;

/**
 * 
 */

/**
 * @author dieterwachters
 * 
 */
public class CompileLess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String input = args[0];
		final String output = args[1];

		System.out.println("Input: " + input);
		System.out.println("Output: " + input);

		final LessCompiler compiler = new LessCompiler();
		compiler.setCompress(true);
		try {
			compiler.compile(new File(input), new File(output));
			System.out.println("Compilation successful.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

}
