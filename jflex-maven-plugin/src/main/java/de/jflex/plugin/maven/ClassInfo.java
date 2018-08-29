package de.jflex.plugin.maven;

import java.io.File;

class ClassInfo {
  String className = null;
  String packageName = null;

  /**
   * Returns the (relative) path name of the java source code file that corresponds to the class.
   *
   * <p>For instance, "org.foo.Bar" returns "org/foo/Bar.java"
   *
   * @return Name of the java file.
   */
  String getOutputFilename() {
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
