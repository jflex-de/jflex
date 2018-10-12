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

import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;

/**
 * @author Rafal Mantiuk (Rafal.Mantiuk@bellstream.pl)
 * @author Gerwin Klein (lsf@jflex.de)
 * @author Régis Décamps
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
    Reader lexFileReader = Files.newReader(lexFile, StandardCharsets.UTF_8);
    return guessPackageAndClass(lexFileReader);
  }

  static ClassInfo guessPackageAndClass(Reader lexFileReader) throws IOException {
    try (LineNumberReader reader = new LineNumberReader(lexFileReader)) {
      String className = null;
      String packageName = null;
      while (className == null || packageName == null) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        if (packageName == null) {
          packageName = guessPackage(line);
        }
        if (className == null) {
          className = guessClass(line);
        }
      }

      if (className == null) {
        className = DEFAULT_NAME;
      }
      return new ClassInfo(className, packageName);
    }
  }

  @Nullable
  private static String guessClass(String line) {
    int index = line.indexOf("%class");
    if (index > -1) {
      index += "%class".length();
      return line.substring(index).trim();
    }
    return null;
  }

  @Nullable
  private static String guessPackage(String line) {
    int index = line.trim().indexOf("package");
    if (index == 0) {
      index += "package".length();

      int end = line.indexOf(';', index);
      if (end >= index) {
        return line.substring(index, end).trim();
      }
    }

    return null;
  }
}
