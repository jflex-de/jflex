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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
   * Guesses package and class name, and {@code %include} files, based on this grammar definition.
   *
   * @param lexFile the lex spec to process
   * @return collected info about this lex spec.
   * @throws FileNotFoundException if the lex file does not exist
   * @throws IOException when an IO exception occurred while reading a file.
   */
  static SpecInfo guessSpecInfo(File lexFile) throws IOException {
    Reader lexFileReader = Files.newReader(lexFile, StandardCharsets.UTF_8);
    return guessSpecInfo(lexFileReader, lexFile);
  }

  /**
   * Guesses package and class name, and {@code %include} files, based on this grammar definition.
   *
   * @param lexFileReader reader for lex spec to process
   * @param lexFile the lex spec to process, used for relative path name resolution of {@code
   *     %incude}s.
   * @return collected info about this lex spec.
   * @throws IOException when an IO exception occurred while processing the reader. Ignores IO
   *     errors for {@code %incude} files.
   */
  static SpecInfo guessSpecInfo(Reader lexFileReader, File lexFile) throws IOException {
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
      return new SpecInfo(className, packageName, guessIncludes(lexFile));
    }
  }

  /**
   * Processes a file for {@code %include} directives.
   *
   * @param file the lex file to process.
   * @return the set of files (recursively) mentioned in {@code %include}s.
   */
  private static Set<File> guessIncludes(File file) {
    return nestedIncludes(new HashSet<>(), file);
  }

  /**
   * Recursively processes a file for {@code %include} directives.
   *
   * @param file the file to process; itself assumed to be an {@code %include} or lex file. Path
   *     names in the file are relative to the file location.
   * @param seen the set of files seen so far, to avoid following cycles.
   * @return the set of files (recursively) mentioned in {@code %include}s.
   */
  private static Set<File> nestedIncludes(Set<File> seen, File file) {
    Set<File> includedFiles = new HashSet<>();
    Set<File> newSeen = new HashSet<>(seen);
    try {
      Reader reader = Files.newReader(file, StandardCharsets.UTF_8);
      Set<File> newFiles = mapFiles(parseIncludes(reader), file.getParentFile());
      newFiles.removeAll(seen);
      newSeen.addAll(newFiles);
      includedFiles.addAll(newFiles);
      Set<File> nested =
          newFiles.stream()
              .flatMap(f -> nestedIncludes(newSeen, f).stream())
              .collect(Collectors.toSet());
      includedFiles.addAll(nested);
    } catch (IOException e) {
      // silently ignore IO exceptions in include file processing
    }
    return includedFiles;
  }

  /**
   * Resolves path names relative to parent.
   *
   * @param set a set of relative path names
   * @param parent the parent file of these path names
   * @return the set of files relative to {@code parent}
   */
  static Set<File> mapFiles(Set<String> set, File parent) {
    return set.stream().map(s -> new File(parent, s)).collect(Collectors.toSet());
  }

  /**
   * Parses input for {@code %include} directives.
   *
   * @param fileReader the input
   * @return the set of path names mentioned after {@code %include} directives in the input.
   */
  static Set<String> parseIncludes(Reader fileReader) throws IOException {
    Set<String> includedFiles = new HashSet<>();
    try (LineNumberReader reader = new LineNumberReader(fileReader)) {
      String line = reader.readLine();
      while (line != null) {
        String includedFile = guessIncluded(line);
        if (includedFile != null) {
          includedFiles.add(includedFile);
        }
        line = reader.readLine();
      }
    }
    return includedFiles;
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
