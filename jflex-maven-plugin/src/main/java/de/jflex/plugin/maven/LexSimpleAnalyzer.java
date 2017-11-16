/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * Credit goes to the authors of the ant task.                             *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @author Rafal Mantiuk (Rafal.Mantiuk@bellstream.pl)
 * @author Gerwin Klein (lsf@jflex.de)
 */
class LexSimpleAnalyzer {
  static final String DEFAULT_NAME = "Yylex";

  /**
   * Guesses the package and class name, based on this grammar definition. Does not override the
   * Mojo configuration if it exist.
   *
   * @return The name of the java code to generate.
   * @throws FileNotFoundException if the lex file does not exist
   * @throws IOException when an IO exception occurred while reading a file.
   */
  static ClassInfo guessPackageAndClass(File lexFile) throws IOException {
    assert lexFile.isAbsolute() : lexFile;

    try (LineNumberReader reader = new LineNumberReader(new FileReader(lexFile))) {
      ClassInfo classInfo = new ClassInfo();
      while (classInfo.className == null || classInfo.packageName == null) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }

        guessPackage(classInfo, line);
        guessClass(classInfo, line);
      }

      if (classInfo.className == null) {
        classInfo.className = DEFAULT_NAME;
      }
      return classInfo;
    }
  }

  private static void guessClass(ClassInfo classInfo, String line) {
    if (classInfo.className == null) {
      int index = line.indexOf("%class");
      if (index >= 0) {
        index += 6;

        classInfo.className = line.substring(index);
        classInfo.className = classInfo.className.trim();
      }
    }
  }

  private static void guessPackage(ClassInfo classInfo, String line) {
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
  }
}
