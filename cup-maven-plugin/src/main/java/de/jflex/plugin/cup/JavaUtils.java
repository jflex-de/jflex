package de.jflex.plugin.cup;

import java.io.File;

public class JavaUtils {
  /** Constant {@code .java}. */
  public static final String JAVA_FILE_EXT = ".java";

  public static String packageToPath(String javaPackage) {
    return javaPackage.replace('.', File.separatorChar);
  }

  public static String fileToClassName(File javaFile) {
    String fn = javaFile.getName();
    return fn.substring(0, fn.length() - JAVA_FILE_EXT.length());
  }

  public static File file(File srcDirectory, String javaPackage, String className) {
    File dir = new File(srcDirectory, packageToPath(javaPackage));
    return new File(dir, className + JAVA_FILE_EXT);
  }
}
