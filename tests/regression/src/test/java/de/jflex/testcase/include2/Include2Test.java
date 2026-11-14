/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for nested input streams with {@code skeleton.nested} ({@code yypushStream}, {@code
 * yypopStream}, {@code yymoreStreams}) together with {@code %char} and the line start operator
 * {@code ^}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/include2/include2.flex",
    skeleton = "src/test/java/de/jflex/testcase/include2/skeleton.nested",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/include2/include2.sysout",
    quiet = true,
    minimizedDfaStatesCount = 25,
    charClassesCount = 20,
    sameCharClass = {
      " \t\b",
      "0123456789",
      "abfghjmpqrsvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "\u000B\u000C\u0085\u2028\u2029",
      "\u0000-_.\u00E4\u2027\uD83D\uDE00"
    },
    distinctCharClasses = {" 0a\u000B-%/\"\n\rincludetok"})
public class Include2Test implements JFlexTest {

  private static final int OTHER = Include2.OTHER;
  private static final int INCLUDE = Include2.INCLUDE;
  private static final int TOKEN = Include2.TOKEN;
  private static final int NEWLINE = Include2.NEWLINE;
  private static final int STREAM_END = Include2.STREAM_END;

  /** The directory of the includes, relative to the working directory of the test. */
  private static final String DIR = "src/test/java/de/jflex/testcase/include2";

  /**
   * Contains {@code token 1}, {@code something else} with an include of {@link #NESTED3}, and
   * {@code token 2} on three lines.
   */
  private static final String NESTED2 = DIR + "/nested2";

  /** Contains the line {@code token 3}. */
  private static final String NESTED3 = DIR + "/nested3";

  /** The length of the include directive for either file: 9 + 1 + 40 + 8 + 1. */
  private static final int DIRECTIVE = 59;

  private Include2 scanner;

  /** The number of streams that have to be on the stack of the scanner. */
  private int depth;

  private void scan(String input) {
    scanner = new Include2(new StringReader(input));
    depth = 0;
  }

  private static String include(String file) {
    return "%include \"" + file + "\"";
  }

  /** Checks {@code yymoreStreams()} and that the scanner is not at the end of the input. */
  private void assertDepth(String at) {
    assertEquals(depth > 0, scanner.yymoreStreams(), "more streams " + at);
    assertFalse(scanner.yyatEOF(), "at EOF " + at);
  }

  private void assertToken(int rule, String text, long charPos) throws IOException {
    String at = "at char " + charPos;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(charPos, scanner.charPos(), "yychar " + at);
    assertDepth(at);
  }

  /**
   * Checks that the scanner matches every character of the text with the fallback rule.
   *
   * @param charPos the value of {@code yychar} for the first character
   */
  private void assertOther(String text, long charPos) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(OTHER, text.substring(i, i + 1), charPos + i);
    }
  }

  /** Checks that the next token is an include directive for the file. */
  private void assertInclude(String file) throws IOException {
    String at = "at include of " + file;
    assertEquals(INCLUDE, scanner.yylex(), "rule " + at);
    assertEquals(file, scanner.included, "file " + at);
    assertEquals("", scanner.yytext(), "text " + at);
    assertEquals(0, scanner.yylength(), "length " + at);
    assertEquals(0, scanner.charPos(), "yychar " + at);
    depth++;
    assertDepth(at);
  }

  /**
   * Checks that the next token is the end of the stream for the file. After {@code yypopStream},
   * text and {@code yychar} should be those of the include directive again.
   *
   * @param charPos the value of {@code yychar} for the directive
   */
  private void assertStreamEnd(String file, long charPos) throws IOException {
    String at = "at end of " + file;
    assertEquals(STREAM_END, scanner.yylex(), "rule " + at);
    assertEquals(include(file), scanner.yytext(), "text " + at);
    assertEquals(charPos, scanner.charPos(), "yychar " + at);
    depth--;
    assertDepth(at);
  }

  /**
   * Checks the end of the input.
   *
   * @param length the length of the input, without the included files
   */
  private void assertEof(long length) throws IOException {
    assertEquals(Include2.YYEOF, scanner.yylex());
    assertEquals(length, scanner.charPos(), "yychar at EOF");
    assertEquals(0, depth);
    assertFalse(scanner.yymoreStreams());
    assertTrue(scanner.yyatEOF());
  }

  /** The content of {@link #NESTED3} */
  private void assertNested3() throws IOException {
    assertToken(TOKEN, "token 3", 0);
    assertToken(NEWLINE, "\n", 7);
  }

  @Test
  public void directiveLength() {
    assertEquals(DIRECTIVE, include(NESTED2).length());
    assertEquals(DIRECTIVE, include(NESTED3).length());
  }

  @Test
  public void nestedIncludes() throws Exception {
    String input =
        include(NESTED2)
            + "token a\n\ntoken b blublw\nsd\nasf sad adsdfa\ntoken  s"
            + include(NESTED3)
            + "token c sdfs a\ntoken d\n";
    scan(input);
    assertInclude(NESTED2);
    // in nested2
    assertToken(TOKEN, "token 1", 0);
    assertToken(NEWLINE, "\n", 7);
    // second line of nested2, from 7 + 1
    assertOther("something else ", 8);
    // the directive is at 8 + 15 = 23
    assertInclude(NESTED3);
    assertNested3();
    assertStreamEnd(NESTED3, 23);
    // nested2 after the directive, 23 + 59 = 82
    assertToken(NEWLINE, "\n", 82);
    assertToken(TOKEN, "token 2", 83);
    assertToken(NEWLINE, "\n", 90);
    // the first directive of the input is at 0
    assertStreamEnd(NESTED2, 0);
    // The input after the directive, 0 + 59 = 59. This is not the start of a line, although the
    // last character that the scanner has read is the newline at the end of nested2.
    assertOther("token a", 59);
    assertToken(NEWLINE, "\n", 66);
    assertToken(NEWLINE, "\n", 67);
    assertToken(TOKEN, "token b", 68);
    assertOther(" blublw", 75);
    assertToken(NEWLINE, "\n", 82);
    assertOther("sd", 83);
    assertToken(NEWLINE, "\n", 85);
    assertOther("asf sad adsdfa", 86);
    assertToken(NEWLINE, "\n", 100);
    assertToken(TOKEN, "token  s", 101);
    // the directive is at 101 + 8 = 109
    assertInclude(NESTED3);
    assertNested3();
    assertStreamEnd(NESTED3, 109);
    // 109 + 59 = 168
    assertOther("token c sdfs a", 168);
    assertToken(NEWLINE, "\n", 182);
    assertToken(TOKEN, "token d", 183);
    assertToken(NEWLINE, "\n", 190);
    assertEquals(191, input.length());
    assertEof(191);
  }

  /**
   * After {@code yypopStream}, the scanner is behind the directive and not at the start of a line.
   */
  @Test
  public void noLineStartAfterPop() throws Exception {
    scan(include(NESTED3) + "token a\ntoken b");
    assertInclude(NESTED3);
    assertNested3();
    assertStreamEnd(NESTED3, 0);
    assertOther("token a", 59);
    assertToken(NEWLINE, "\n", 66);
    assertToken(TOKEN, "token b", 67);
    assertEof(74);
  }

  /** The first character of an included file is at the start of a line, wherever the include is. */
  @Test
  public void lineStartInPushedStream() throws Exception {
    scan("x" + include(NESTED3));
    assertOther("x", 0);
    assertInclude(NESTED3);
    assertNested3();
    assertStreamEnd(NESTED3, 1);
    assertEof(60);
  }

  /** The newline after the directive makes a line start as usual. */
  @Test
  public void lineStartAfterDirectiveAndNewline() throws Exception {
    scan(include(NESTED3) + "\ntoken a");
    assertInclude(NESTED3);
    assertNested3();
    assertStreamEnd(NESTED3, 0);
    assertToken(NEWLINE, "\n", 59);
    assertToken(TOKEN, "token a", 60);
    assertEof(67);
  }

  @Test
  public void tokenOnlyAtLineStart() throws Exception {
    scan("token a token b\n token c\ntoken d");
    assertToken(TOKEN, "token a", 0);
    assertOther(" token b", 7);
    assertToken(NEWLINE, "\n", 15);
    assertOther(" token c", 16);
    assertToken(NEWLINE, "\n", 24);
    assertToken(TOKEN, "token d", 25);
    assertEof(32);
  }

  /** The file name has to be in quotes. */
  @Test
  public void notAnInclude() throws Exception {
    String unquoted = "%include " + NESTED3;
    String open = "%include \"" + NESTED3;
    scan(unquoted + "\n" + open);
    assertOther(unquoted, 0);
    assertToken(NEWLINE, "\n", 57);
    assertOther(open, 58);
    assertEof(58 + 58);
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof(0);
  }
}
