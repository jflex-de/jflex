package de.flex.maven.plugin;

import java.io.File;

public class ClassInfo {
	public String className = null;
	public String packageName = null;

	/**
	 * Return the name of the java source code file that corresponds to the
	 * class.
	 * 
	 * For instance, "org.foo.Bar" returns "/org/foo/Bar.java"
	 * 
	 * @return Name of the java file.
	 */
	public String getOutputFilename() {
		String pacakgenameDir = File.separator;
		if (packageName != null) {
			pacakgenameDir += packageName.replace('.', File.separatorChar)
					+ File.separator;
		}
		return pacakgenameDir + className + ".java";
	}

}
