/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.maven.plugin.jflex;

import com.google.common.base.Strings;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

class ClassInfo {

  final String className;

  /** dot-separated package name. Empty string for the default package. */
  final String packageName;

  final Set<File> includedFiles;

  ClassInfo(String className, @Nullable String packageName) {
    this(className, packageName, Collections.emptySet());
  }

  ClassInfo(String className, @Nullable String packageName, Set<File> includedFiles) {
    this.className = className;
    this.packageName = Strings.nullToEmpty(packageName);
    this.includedFiles = new HashSet<>(includedFiles);
  }

  /** Compares the className and packageName of instances, ignoring includedFiles. */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ClassInfo)) {
      return false;
    }
    ClassInfo other = (ClassInfo) obj;
    return Objects.equals(className, other.className)
        && Objects.equals(packageName, other.packageName);
  }

  /** Computes a hash code from the className and packageName, ignoring includedFiles. */
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
    // TODO(regisd) Use PackageUtil
    String packageDir = packageName.replace('.', File.separatorChar);
    if (packageDir.length() > 0) {
      packageDir += File.separatorChar;
    }
    return packageDir + className + ".java";
  }
}
