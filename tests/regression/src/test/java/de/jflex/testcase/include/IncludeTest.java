/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Test for nested input streams with {@code skeleton.nested}, i.e. {@code yypushStream}, {@code
 * yypopStream} and {@code yymoreStreams} (<a
 * href="https://github.com/jflex-de/jflex/issues/51">#51, yyPushStream and yyPopStream bug</a>).
 *
 * <p>Test that the scanner reads the file of a {@code %include} directive in its input as a new
 * stream and goes back to the stream of the directive at the end of that file.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/include/include.flex",
    skeleton = "src/test/java/de/jflex/testcase/include/skeleton.nested",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/include/include.sysout",
    quiet = true,
    minimizedDfaStatesCount = 23,
    charClassesCount = 19,
    sameCharClass = {
      " \t\b",
      "0123456789",
      "abfghjmpqrsvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "\u000B\u000C\u0085\u2028\u2029",
      "\u0000-_.\"\u00E4\u2027\uD83D\uDE00"
    },
    distinctCharClasses = {" 0a\u000B-%/\n\rincludetok"})
public class IncludeTest implements JFlexTest {

  private static final int OTHER = IncludeScanner.OTHER;
  private static final int INCLUDE = IncludeScanner.INCLUDE;
  private static final int TOKEN = IncludeScanner.TOKEN;
  private static final int NEWLINE = IncludeScanner.NEWLINE;
  private static final int STREAM_END = IncludeScanner.STREAM_END;

  /** The directory of the includes, relative to the working directory of the test. */
  private static final String DIR = "src/test/java/de/jflex/testcase/include";

  /** Contains {@code token 1}, an include of {@link #INP3} and {@code token 2} on three lines. */
  private static final String INP2 = DIR + "/inp2";

  /** Contains the line {@code token 3}. */
  private static final String INP3 = DIR + "/inp3";

  /**
   * Directory for the files that the tests write. The spec only accepts relative paths of letters
   * and digits as file names, so this cannot be a {@code @TempDir} and uses the working directory
   * instead.
   */
  private static final Path FILES = Paths.get("IncludeTestFiles");

  private IncludeScanner scanner;

  /** The number of streams that have to be on the stack of the scanner. */
  private int depth;

  private void scan(String input) {
    scanner = new IncludeScanner(new StringReader(input));
    depth = 0;
  }

  /** Writes a file to include and returns its name. */
  private static String file(String name, String content) throws IOException {
    Files.createDirectories(FILES);
    Files.write(FILES.resolve(name), content.getBytes(StandardCharsets.UTF_8));
    return FILES + "/" + name;
  }

  @AfterAll
  public static void deleteFiles() throws IOException {
    if (Files.isDirectory(FILES)) {
      try (Stream<Path> files = Files.list(FILES)) {
        for (Path file : (Iterable<Path>) files::iterator) {
          Files.delete(file);
        }
      }
      Files.delete(FILES);
    }
  }

  /** Checks {@code yymoreStreams()} and that the scanner is not at the end of the input. */
  private void assertDepth(String at) {
    assertEquals(depth > 0, scanner.yymoreStreams(), "more streams " + at);
    assertFalse(scanner.yyatEOF(), "at EOF " + at);
  }

  private void assertToken(int rule, String text) throws IOException {
    String at = "at '" + text + "'";
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertDepth(at);
  }

  /** Checks that the scanner matches every character of the text with the fallback rule. */
  private void assertOther(String text) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(OTHER, text.substring(i, i + 1));
    }
  }

  /**
   * Checks that the next token is an include directive for the file. After {@code yypushStream},
   * the text must be empty.
   */
  private void assertInclude(String file) throws IOException {
    String at = "at include of " + file;
    assertEquals(INCLUDE, scanner.yylex(), "rule " + at);
    assertEquals(file, scanner.included, "file " + at);
    assertEquals("", scanner.yytext(), "text " + at);
    assertEquals(0, scanner.yylength(), "length " + at);
    depth++;
    assertDepth(at);
  }

  /**
   * Checks that the next token is the end of the stream for the file, i.e. that {@code
   * yymoreStreams()} is true at {@code <<EOF>>}. After {@code yypopStream}, the scanner is in the
   * stream of the include directive again: the text is the directive, and the scanner is not at the
   * end of the input.
   */
  private void assertStreamEnd(String file) throws IOException {
    String at = "at end of " + file;
    assertEquals(STREAM_END, scanner.yylex(), "rule " + at);
    assertEquals("%include " + file, scanner.yytext(), "text " + at);
    depth--;
    assertDepth(at);
  }

  /** Checks the end of the input, where {@code yymoreStreams()} is false at {@code <<EOF>>}. */
  private void assertEof() throws IOException {
    assertEquals(IncludeScanner.YYEOF, scanner.yylex());
    assertEquals(0, depth);
    assertFalse(scanner.yymoreStreams());
    assertTrue(scanner.yyatEOF());
  }

  /** The content of {@link #INP3}. */
  private void assertInp3() throws IOException {
    assertToken(TOKEN, "token 3");
    assertToken(NEWLINE, "\n");
  }

  /** The content of {@link #INP2}. */
  private void assertInp2() throws IOException {
    assertToken(TOKEN, "token 1");
    assertToken(NEWLINE, "\n");
    assertInclude(INP3);
    assertInp3();
    // still in inp2
    assertStreamEnd(INP3);
    assertToken(NEWLINE, "\n");
    assertToken(TOKEN, "token 2");
    assertToken(NEWLINE, "\n");
  }

  /**
   * An include in an included file, and an include in the middle of a line: the text after the
   * directive is scanned after the end of the included file.
   */
  @Test
  public void nestedIncludes() throws Exception {
    scan(
        "%include "
            + INP2
            + "\n\ntoken b blublw\nsd\nasf sad adsdfa\ntoken  s%include "
            + INP3
            + "-sdfs a\n");
    assertInclude(INP2);
    assertInp2();
    assertStreamEnd(INP2);
    assertToken(NEWLINE, "\n");
    assertToken(NEWLINE, "\n");
    assertToken(TOKEN, "token b");
    assertOther(" blublw");
    assertToken(NEWLINE, "\n");
    assertOther("sd");
    assertToken(NEWLINE, "\n");
    assertOther("asf sad adsdfa");
    assertToken(NEWLINE, "\n");
    assertToken(TOKEN, "token  s");
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    // "-" is not part of a file name
    assertOther("-sdfs a");
    assertToken(NEWLINE, "\n");
    assertEof();
  }

  /** The include is the first and the last thing in the input. */
  @Test
  public void includeIsWholeInput() throws Exception {
    scan("%include " + INP3);
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertEof();
  }

  @Test
  public void includeFirst() throws Exception {
    scan("%include " + INP3 + "\ntoken a");
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertToken(NEWLINE, "\n");
    assertToken(TOKEN, "token a");
    assertEof();
  }

  @Test
  public void includeLast() throws Exception {
    scan("token a\n%include " + INP3);
    assertToken(TOKEN, "token a");
    assertToken(NEWLINE, "\n");
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertEof();
  }

  @Test
  public void sameFileTwice() throws Exception {
    scan("%include " + INP3 + "%include " + INP3);
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertEof();
  }

  @Test
  public void sameNestedFileTwice() throws Exception {
    scan("%include " + INP2 + "\n%include " + INP2);
    assertInclude(INP2);
    assertInp2();
    assertStreamEnd(INP2);
    assertToken(NEWLINE, "\n");
    assertInclude(INP2);
    assertInp2();
    assertStreamEnd(INP2);
    assertEof();
  }

  /** The stream of an empty file ends at once. */
  @Test
  public void emptyFile() throws Exception {
    String empty = file("empty", "");
    scan("a%include " + empty + " token b");
    assertOther("a");
    assertInclude(empty);
    assertStreamEnd(empty);
    assertOther(" ");
    assertToken(TOKEN, "token b");
    assertEof();
  }

  @Test
  public void emptyFileIsWholeInput() throws Exception {
    String empty = file("empty2", "");
    scan("%include " + empty);
    assertInclude(empty);
    assertStreamEnd(empty);
    assertEof();
  }

  /** An include is the last thing in an included file: both streams end one after the other. */
  @Test
  public void includeLastInIncludedFile() throws Exception {
    String outer = file("outer", "%include " + INP3);
    scan("%include " + outer + "\n");
    assertInclude(outer);
    assertInclude(INP3);
    assertInp3();
    assertStreamEnd(INP3);
    assertStreamEnd(outer);
    assertToken(NEWLINE, "\n");
    assertEof();
  }

  /** A match ends with its stream: {@code token } in the file and {@code !} after the include. */
  @Test
  public void noMatchAcrossEndOfStream() throws Exception {
    String open = file("open", "token ");
    scan("%include " + open + "!");
    assertInclude(open);
    assertOther("token ");
    assertStreamEnd(open);
    assertOther("!");
    assertEof();
  }

  /** {@code token} before the include and a space and {@code x} in the file are no token either. */
  @Test
  public void noMatchAcrossStartOfStream() throws Exception {
    String rest = file("rest", " x");
    scan("token%include " + rest);
    assertOther("token");
    assertInclude(rest);
    assertOther(" x");
    assertStreamEnd(rest);
    assertEof();
  }

  @Test
  public void notAnInclude() throws Exception {
    scan("%include -\n%include");
    assertOther("%include -");
    assertToken(NEWLINE, "\n");
    assertOther("%include");
    assertEof();
  }

  /** The last character of a token is not a line terminator, and is not missing. */
  @Test
  public void notAToken() throws Exception {
    scan("token \ntokenx\ntoken");
    assertOther("token ");
    assertToken(NEWLINE, "\n");
    assertOther("tokenx");
    assertToken(NEWLINE, "\n");
    assertOther("token");
    assertEof();
  }

  @Test
  public void newlines() throws Exception {
    scan("\n\r\n\r\r");
    assertToken(NEWLINE, "\n");
    assertToken(NEWLINE, "\r\n");
    assertToken(NEWLINE, "\r");
    assertToken(NEWLINE, "\r");
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
