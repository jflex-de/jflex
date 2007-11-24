package de.jflex.example.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import junit.framework.TestCase;



public class SubstTest extends TestCase {
	private static final String OUTPUT_FILE = "target/sample.out";

	public void testSample() throws IOException {
		// the standalon Subst prints status on stdout
		// redirecte it into a file
		String[] argv = new String[1];
		argv[0] = "src/test/resources/sample.in";
		File actual = new File(OUTPUT_FILE);
		actual.delete();
		FileOutputStream fos = new FileOutputStream(OUTPUT_FILE, true);
		System.setOut(new PrintStream(fos));

		Subst.main(argv);

		fos.close();

		BufferedReader actualContent = new BufferedReader(new FileReader(
				actual));

		// the expected result is in a file
		Reader expected = new FileReader("src/test/resources/sample.expected");
		BufferedReader expectedContent = new BufferedReader(expected);

		String expectedLine, actualLine;
		do {
			expectedLine = expectedContent.readLine();
			actualLine = actualContent.readLine();

			assertEquals(expectedLine, actualLine);
		} while (expectedLine != null);
	}
}
