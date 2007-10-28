package org.codehaus.mojo.jflex;

import java.io.File;

public class ClassInfo {
	public String className = null;
	public String packageName = null;

	/**
	 * Return the (relative) path name of the java source code file that corresponds to the
	 * class.
	 * 
	 * For instance, "org.foo.Bar" returns "org/foo/Bar.java"
	 * 
	 * @return Name of the java file.
	 */
	public String getOutputFilename() {
		String packageDir = "";
		if (packageName != null) {
			packageDir += packageName.replace('.', File.separatorChar);
		}
		if (packageDir.length() > 0) {
			packageDir += File.separatorChar;
		}
		return packageDir + className + ".java";
	}

}
