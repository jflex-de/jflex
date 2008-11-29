package org.codehaus.mojo.jflex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class LexSimpleAnalyzer {
	public static final String DEFAULT_NAME = "Yylex";

	/**
	 * Guess the package and class name, based on this grammar definition. Does
	 * not overide the Mojo configuration if it exist.
	 * 
	 * @return The name of the java code to generate.
	 * 
	 * Credit goes to the authors of the maven-jlex-plugin
	 * 
	 * @throws IOException
	 * @author Rafal Mantiuk (Rafal.Mantiuk@bellstream.pl)
	 * @author Gerwin Klein (lsf@jflex.de)
	 * @throws FileNotFoundException
	 *             if the lex file does not exist
	 * 
	 */
	protected static ClassInfo guessPackageAndClass(File lexFile)
			throws FileNotFoundException, IOException {
		assert lexFile.isAbsolute() : lexFile;

		LineNumberReader reader = new LineNumberReader(new FileReader(lexFile));

		ClassInfo classInfo = new ClassInfo();
		// TODO Reuse code from parser if it exists.
		while (classInfo.className == null || classInfo.packageName == null) {
			String line = reader.readLine();
			if (line == null)
				break;

			if (classInfo.packageName == null) {
				int index = line.indexOf("package");
				if (index >= 0) {
					index += 7;

					int end = line.indexOf(';', index);
					if (end >= index) {
						classInfo.packageName = line.substring(index, end);
						classInfo.packageName = classInfo.packageName.trim();
					}
				}
			}

			if (classInfo.className == null) {
				int index = line.indexOf("%class");
				if (index >= 0) {
					index += 6;

					classInfo.className = line.substring(index);
					classInfo.className = classInfo.className.trim();
				}
			}
		}

		// package name may be null, but class name not
		if (classInfo.className == null) {
			// log.warn("Could not guess class name from " +
			// lexFile.getName()
			// + ". Class name set to " + DEFAULT_NAME);
			classInfo.className = DEFAULT_NAME;
		}
		return classInfo;
	}
}
