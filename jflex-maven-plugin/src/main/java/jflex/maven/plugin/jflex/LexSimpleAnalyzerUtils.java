/*
 * JFlex Maven3 plugin
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>
 * Credit goes to the authors of the ant task.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.maven.plugin.jflex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

  /** Lex spec delimiter, needs to be at the beginning of a line. */
  private static final String SECTION_DELIMITER = "%%";

  /** A Java identifier, as in {@code LexScan.flex}. */
  private static final String IDENT = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";

  /** {@code %class} followed by an identifier, as in {@code LexScan.flex}. */
  private static final Pattern CLASS_DIRECTIVE_MATCHER =
      Pattern.compile("%class[ \\t\\x08]+(" + IDENT + ")");

  /** All directives that copy Java code up to a matching end line, e.g. <code>%{ .. %}</code>. */
  private static final String CODE_DIRECTIVE =
      "%(?:init|initthrow|eof|eofthrow|yylexthrow|eofval)?";

  private static final Pattern CODE_BLOCK_START =
      Pattern.compile("[ \\t\\x08]*" + CODE_DIRECTIVE + "\\{");
  private static final Pattern CODE_BLOCK_END = Pattern.compile(CODE_DIRECTIVE + "\\}");

  /**
   * A Java package declaration without comments. Group 1 is the package name. This may still
   * contain white space around the dots.
   */
  private static final Pattern PACKAGE_DECLARATION =
      Pattern.compile("\\s*package\\s+(" + IDENT + "(?:\\s*\\.\\s*" + IDENT + ")*)\\s*;");

  /**
   * Guesses package and class name, and {@code %include} files, based on this grammar definition.
   *
   * @param lexFile the lex spec to process
   * @return collected info about this lex spec.
   * @throws FileNotFoundException if the lex file does not exist
   * @throws IOException when an IO exception occurred while reading a file.
   */
  static SpecInfo guessSpecInfo(File lexFile) throws IOException {
    return guessSpecInfo(newReader(lexFile), lexFile);
  }

  /**
   * Opens a file for reading as UTF-8.
   *
   * @throws FileNotFoundException if the file does not exist
   */
  private static Reader newReader(File file) throws FileNotFoundException {
    return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
  }

  /**
   * Guesses package and class name, and {@code %include} files, based on this grammar definition.
   *
   * <p>The package name is only looked for in the user code section before the first {@code %%},
   * and the class name only in the directives section between the first and the second {@code %%}.
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
      String packageName = guessPackage(readUserCode(reader));
      String className = guessClass(reader);
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
      Set<File> newFiles = mapFiles(parseIncludes(newReader(file)), file.getParentFile());
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

  /**
   * Reads the user code section of a lex spec.
   *
   * @param reader the lex spec, positioned at its start. Afterwards positioned at the start of the
   *     directives section.
   * @return the section (all lines) before the first @{code %%}.
   */
  private static String readUserCode(LineNumberReader reader) throws IOException {
    StringBuilder userCode = new StringBuilder();
    String line = reader.readLine();
    while (line != null && !line.startsWith(SECTION_DELIMITER)) {
      userCode.append(line).append('\n');
      line = reader.readLine();
    }
    return userCode.toString();
  }

  /**
   * Finds the class name in the directives section of a lex spec.
   *
   * <p>Like in the JFlex scanner, take only the identifier after {@code %class}, so a trailing
   * comment or generic parameters are not included. The last {@code %class} takes precedence. Java
   * code blocks such as <code>%{ .. %}</code> and the rest of a line after {@code //} are skipped.
   *
   * @param reader the lex spec, positioned at the start of the directives section. Reads up to the
   *     next line that starts with {@code %%}.
   * @return the class name, or {@link #DEFAULT_NAME} if there is no {@code %class} directive.
   */
  private static String guessClass(LineNumberReader reader) throws IOException {
    String className = DEFAULT_NAME;
    boolean inCodeBlock = false;
    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
      if (inCodeBlock) {
        inCodeBlock = !CODE_BLOCK_END.matcher(line).lookingAt();
      } else if (line.startsWith(SECTION_DELIMITER)) {
        break;
      } else if (CODE_BLOCK_START.matcher(line).lookingAt()) {
        inCodeBlock = true;
      } else {
        int comment = line.indexOf("//");
        Matcher matcher =
            CLASS_DIRECTIVE_MATCHER.matcher(comment < 0 ? line : line.substring(0, comment));
        if (matcher.find()) {
          className = matcher.group(1);
        }
      }
    }
    return className;
  }

  /**
   * Finds the package name in the user code section of a lex spec.
   *
   * <p>A package declaration is the first thing in a Java file, so this only looks at the start of
   * the user code. White space and comments before and inside the declaration are ignored.
   *
   * @param userCode the user code section of the lex spec
   * @return the package name, or {@code null} if the user code does not start with a package
   *     declaration.
   */
  private static String guessPackage(String userCode) {
    Matcher matcher = PACKAGE_DECLARATION.matcher(firstStatement(userCode));
    if (matcher.matches()) {
      return matcher.group(1).replaceAll("\\s", "");
    }
    return null;
  }

  /**
   * Returns the start of {@code javaCode} up to and including the first {@code ;} outside of a
   * comment, with each comment replaced by a space.
   *
   * <p>Does not know about string literals, which cannot occur in a package declaration.
   */
  private static String firstStatement(String javaCode) {
    StringBuilder result = new StringBuilder();
    int pos = 0;
    while (pos < javaCode.length()) {
      boolean lineComment = javaCode.startsWith("//", pos);
      if (lineComment || javaCode.startsWith("/*", pos)) {
        int end = lineComment ? javaCode.indexOf('\n', pos) : javaCode.indexOf("*/", pos + 2);
        if (end < 0) {
          // the comment runs to the end of the code
          break;
        }
        pos = lineComment ? end : end + 2;
        // replace comment with empty string so surrounding tokens remain separated
        result.append(' ');
      } else {
        char c = javaCode.charAt(pos++);
        result.append(c);
        if (c == ';') {
          break;
        }
      }
    }
    return result.toString();
  }

  private static String guessIncluded(String line) {
    Matcher matcher = INCLUDE_DIRECTIVE_MATCHER.matcher(line);
    if (matcher.find()) {
      return matcher.group(INCLUDE_DIRECTIVE_ARG_OFFSET).trim();
    }
    return null;
  }

  private LexSimpleAnalyzerUtils() {}
}
