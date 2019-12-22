/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * Credit goes to the authors of the ant task.                             *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.maven.plugin.jflex;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * @author Rafal Mantiuk (Rafal.Mantiuk@bellstream.pl)
 * @author Gerwin Klein (lsf@jflex.de)
 * @author Régis Décamps
 * @author Chris Fraire (cfraire@me.com)
 */
class LexSimpleAnalyzerUtils {

  static final String DEFAULT_NAME = "Yylex";

  private static final Pattern INCLUDE_DIRECTIVE_MATCHER = Pattern.compile("^\\s*%include\\s+(.+)");
  private static final int INCLUDE_DIRECTIVE_ARG_OFFSET = 1;

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
      List<String> includedFiles = new ArrayList<>();
      String line;
      while ((line = reader.readLine()) != null) {
        if (packageName == null) {
          packageName = guessPackage(line);
        }
        if (className == null) {
          className = guessClass(line);
        }
        String includedFile = guessIncluded(line);
        if (includedFile != null) {
          includedFiles.add(includedFile);
        }
      }

      if (className == null) {
        className = DEFAULT_NAME;
      }
      return new ClassInfo(className, packageName, includedFiles);
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

  @Nullable
  private static String guessIncluded(String line) {
    Matcher matcher = INCLUDE_DIRECTIVE_MATCHER.matcher(line);
    if (matcher.find()) {
      return matcher.group(INCLUDE_DIRECTIVE_ARG_OFFSET).trim();
    }
    return null;
  }

  private LexSimpleAnalyzerUtils() {}
}
