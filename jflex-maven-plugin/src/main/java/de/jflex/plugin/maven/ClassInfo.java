/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import com.google.common.base.Strings;
import java.io.File;
import java.util.Objects;
import javax.annotation.Nullable;

class ClassInfo {

  final String className;

  /** dot-separated package name. Empty string for the default package. */
  final String packageName;

  ClassInfo(String className, @Nullable String packageName) {
    this.className = className;
    this.packageName = Strings.nullToEmpty(packageName);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ClassInfo)) {
      return false;
    }
    ClassInfo other = (ClassInfo) obj;
    return Objects.equals(className, other.className)
        && Objects.equals(packageName, other.packageName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className, packageName);
  }

  /**
   * Returns the (relative) path name of the java source code file that corresponds to the class.
   *
   * <p>For instance, "org.foo.Bar" returns "org/foo/Bar.java"
   *
   * @return Name of the java file.
   */
  String getOutputFilename() {
    String packageDir = packageName.replace('.', File.separatorChar);
    if (packageDir.length() > 0) {
      packageDir += File.separatorChar;
    }
    return packageDir + className + ".java";
  }
}
