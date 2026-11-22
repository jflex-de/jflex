/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_esc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.IOException;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Tests strings and escaped characters in character classes, for <a
 * href="https://sourceforge.net/p/jflex/bugs/27/">bug #467469</a>. Strings in character classes
 * stand for their characters. {@code ["-_."]} is the three characters {@code -}, {@code _} and
 * {@code .}, not a range, and the quotes are not in the class.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_esc/ccl.flex",
    warnings = {
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 42),
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 45),
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 49)
    },
    quiet = true,
    minimizedDfaStatesCount = 35,
    charClassesCount = 17,
    charClasses = {
      "{ [0-8][11][14-31]['#'-','][':'-';']['?'-'@']['\\']['^']['`']['{'-1114111] }",
      "{ [9-10][12-13][' '] }",
      "{ ['!'] }",
      "{ ['\"'] }",
      "{ ['-'-'.']['_'] }",
      "{ ['/'] }",
      "{ ['0'-'9'] }",
      "{ ['<'] }",
      "{ ['='] }",
      "{ ['>'] }",
      "{ ['A'] }",
      "{ ['B']['E'-'S']['U'-'Z']['a'-'z'] }",
      "{ ['C'] }",
      "{ ['D'] }",
      "{ ['T'] }",
      "{ ['['] }",
      "{ [']'] }"
    })
public class CclTest implements JFlexTest {

  private Ccl scanner;

  private void scan(String input) {
    scanner = new Ccl(new StringReader(input));
  }

  private void assertToken(Token rule, String text, int line, int column, int state)
      throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(line, scanner.yyline(), "line " + at);
    assertEquals(column, scanner.yycolumn(), "column " + at);
    assertEquals(state, scanner.yystate(), "lexical state after the token " + at);
  }

  private void assertEof() throws IOException {
    assertNull(scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Starts a scan of {@code <} and the text, and asserts the token for {@code <}. */
  private void scanInTag(String text) throws IOException {
    scan("<" + text);
    assertToken(Token.FIRST_TAG_OPEN, "<", 0, 0, Ccl.IN_TAG);
  }

  /** Starts a scan of {@code <![CDATA[} and the text, and asserts the tokens of the start. */
  private void scanInCdata(String text) throws IOException {
    scanInTag("![CDATA[" + text);
    assertToken(Token.CDATA_START, "![CDATA[", 0, 1, Ccl.IN_CDATA);
  }

  private static String hex(String text) {
    StringBuilder result = new StringBuilder();
    for (char c : text.toCharArray()) {
      result.append(String.format("\\u%04X", (int) c));
    }
    return result.toString();
  }

  // --- ["-_."]

  /** Test that the string adds the characters to the class. */
  @Test
  public void stringInClass() throws Exception {
    scanInTag("-_.");
    assertToken(Token.NAME, "-_.", 0, 1, Ccl.IN_TAG);
    assertEof();
    scanInTag("a-b_c.d9");
    assertToken(Token.NAME, "a-b_c.d9", 0, 1, Ccl.IN_TAG);
    assertEof();
  }

  /** Check that the {@code -} in the string does not create a range. */
  @Test
  public void stringInClassIsNoRange() throws Exception {
    int names = 0;
    for (char c = '-'; c <= '_'; c++) {
      String text = String.valueOf(c);
      Token expected;
      int state = Ccl.IN_TAG;
      if (c == '-' || c == '.' || c == '_' || Character.isDigit(c) || Character.isUpperCase(c)) {
        expected = Token.NAME;
        names++;
      } else if (c == '/') {
        expected = Token.SLASH;
      } else if (c == '=') {
        expected = Token.EQ;
      } else if (c == '>') {
        expected = Token.TAG_CLOSE;
        state = Ccl.IN_DATA;
      } else {
        // : ; < ? @ [ \ ] ^
        expected = Token.OTHER;
      }
      scanInTag(text);
      assertToken(expected, text, 0, 1, state);
      assertEof();
    }
    assertEquals(3 + 10 + 26, names);
  }

  /** The quotes around the string are not in the class. */
  @Test
  public void quotesOfStringAreNotInClass() throws Exception {
    scanInTag("\"");
    assertToken(Token.QUOTE_OPEN, "\"", 0, 1, Ccl.IN_ATTRIBUTE);
    assertEof();
    scanInTag("a\"");
    assertToken(Token.NAME, "a", 0, 1, Ccl.IN_TAG);
    assertToken(Token.QUOTE_OPEN, "\"", 0, 2, Ccl.IN_ATTRIBUTE);
    assertEof();
  }

  // --- [^"]>"]

  @Test
  public void negatedClassWithString() throws Exception {
    String[] others = {"\u00E9", "\u2028", "\uFFFF", "\uD83D\uDE00"};
    int matched = 0;
    for (int i = 0; i < 128 + others.length; i++) {
      String text = i < 128 ? String.valueOf((char) i) : others[i - 128];
      boolean excluded = text.equals("]") || text.equals(">");
      scanInCdata(text);
      assertEquals(excluded ? Token.OTHER : Token.CDATA, scanner.yylex(), "rule for " + hex(text));
      assertEquals(text, scanner.yytext(), "text for " + hex(text));
      assertEquals(Ccl.IN_CDATA, scanner.yystate());
      assertEof();
      if (!excluded) {
        matched++;
      }
    }
    assertEquals(126 + others.length, matched);
  }

  /** The quotes around the string are not excluded from the negated class. */
  @Test
  public void quotesOfStringAreInNegatedClass() throws Exception {
    scanInCdata("a\"b\"");
    assertToken(Token.CDATA, "a\"b\"", 0, 9, Ccl.IN_CDATA);
    assertEof();
  }

  /** {@code "]"[^"]"]} is {@code ]} and a character that is not {@code ]}. */
  @Test
  public void negatedClassWithStringAfterString() throws Exception {
    scanInCdata("]x");
    assertToken(Token.CDATA, "]x", 0, 9, Ccl.IN_CDATA);
    assertEof();
    scanInCdata("]>");
    assertToken(Token.CDATA, "]>", 0, 9, Ccl.IN_CDATA);
    assertEof();
    // too short for "]]"[^">"], and no other alternative starts with ]
    scanInCdata("]]");
    assertToken(Token.OTHER, "]", 0, 9, Ccl.IN_CDATA);
    assertToken(Token.OTHER, "]", 0, 10, Ccl.IN_CDATA);
    assertEof();
  }

  /** {@code "]]"[^">"]} is {@code ]]} and a character that is not {@code >}. */
  @Test
  public void negatedClassOfOneCharacterString() throws Exception {
    scanInCdata("]]x");
    assertToken(Token.CDATA, "]]x", 0, 9, Ccl.IN_CDATA);
    assertEof();
    scanInCdata("]]]");
    assertToken(Token.CDATA, "]]]", 0, 9, Ccl.IN_CDATA);
    assertEof();
    scanInCdata("]]>");
    assertToken(Token.CDATA_END, "]]>", 0, 9, Ccl.IN_DATA);
    assertEof();
  }

  @Test
  public void cdataWithBracketsAndGreaterThan() throws Exception {
    scanInCdata("a]b]]cd>e]>f]]>");
    assertToken(Token.CDATA, "a]b]]cd>e]>f", 0, 9, Ccl.IN_CDATA);
    assertToken(Token.CDATA_END, "]]>", 0, 21, Ccl.IN_DATA);
    assertEof();
  }

  @Test
  public void cdataEndsBeforeGreaterThan() throws Exception {
    scanInCdata("]]c>d");
    assertToken(Token.CDATA, "]]c", 0, 9, Ccl.IN_CDATA);
    assertToken(Token.OTHER, ">", 0, 12, Ccl.IN_CDATA);
    assertToken(Token.CDATA, "d", 0, 13, Ccl.IN_CDATA);
    assertEof();
  }

  // --- [^\"]

  /** Escaped quote in the negated class. */
  @Test
  public void escapedQuoteInNegatedClass() throws Exception {
    scanInTag("\"a b\n<>]'\\\">");
    assertToken(Token.QUOTE_OPEN, "\"", 0, 1, Ccl.IN_ATTRIBUTE);
    assertToken(Token.VALUE, "a b\n<>]'\\", 0, 2, Ccl.IN_ATTRIBUTE);
    assertToken(Token.QUOTE_CLOSE, "\"", 1, 5, Ccl.IN_TAG);
    assertToken(Token.TAG_CLOSE, ">", 1, 6, Ccl.IN_DATA);
    assertEof();
  }

  /** The value rule matches the empty string. */
  @Test
  public void emptyAttributeValue() throws Exception {
    scanInTag("\"\"");
    assertToken(Token.QUOTE_OPEN, "\"", 0, 1, Ccl.IN_ATTRIBUTE);
    assertToken(Token.QUOTE_CLOSE, "\"", 0, 2, Ccl.IN_TAG);
    assertEof();
  }

  // --- [\ \n\r\t\f]

  /** The escaped space and the escape sequences in the white space class. */
  @Test
  public void escapedCharactersInClass() throws Exception {
    // space and tab are in the line of the tag, the others end it
    for (String space : new String[] {" ", "\t"}) {
      scanInTag(space + "a");
      assertToken(Token.NAME, "a", 0, 2, Ccl.IN_TAG);
      assertEof();
    }
    for (String space : new String[] {"\n", "\r", "\f"}) {
      scanInTag(space + "a");
      assertToken(Token.NAME, "a", 1, 0, Ccl.IN_TAG);
      assertEof();
    }
    scanInTag(" \n\r\t\f a");
    assertToken(Token.NAME, "a", 3, 1, Ccl.IN_TAG);
    assertEof();
  }

  /** Other space characters are not in the white space class. */
  @Test
  public void otherSpaceCharacters() throws Exception {
    for (String space : new String[] {"\u000B", "\u00A0", "\u0085", "\u2028"}) {
      scanInTag(space);
      assertToken(Token.OTHER, space, 0, 1, Ccl.IN_TAG);
      assertEof();
    }
  }

  // --- the rules together

  @Test
  public void element() throws Exception {
    scan(" \n<a x=\"1\">text</a>");
    assertToken(Token.FIRST_TAG_OPEN, "<", 1, 0, Ccl.IN_TAG);
    assertToken(Token.NAME, "a", 1, 1, Ccl.IN_TAG);
    assertToken(Token.NAME, "x", 1, 3, Ccl.IN_TAG);
    assertToken(Token.EQ, "=", 1, 4, Ccl.IN_TAG);
    assertToken(Token.QUOTE_OPEN, "\"", 1, 5, Ccl.IN_ATTRIBUTE);
    assertToken(Token.VALUE, "1", 1, 6, Ccl.IN_ATTRIBUTE);
    assertToken(Token.QUOTE_CLOSE, "\"", 1, 7, Ccl.IN_TAG);
    assertToken(Token.TAG_CLOSE, ">", 1, 8, Ccl.IN_DATA);
    assertToken(Token.DATA, "text", 1, 9, Ccl.IN_DATA);
    assertToken(Token.TAG_OPEN, "<", 1, 13, Ccl.IN_TAG);
    assertToken(Token.SLASH, "/", 1, 14, Ccl.IN_TAG);
    assertToken(Token.NAME, "a", 1, 15, Ccl.IN_TAG);
    assertToken(Token.TAG_CLOSE, ">", 1, 16, Ccl.IN_DATA);
    assertEof();
  }

  @Test
  public void cdataSection() throws Exception {
    scanInCdata("x]]>y<b>");
    assertToken(Token.CDATA, "x", 0, 9, Ccl.IN_CDATA);
    assertToken(Token.CDATA_END, "]]>", 0, 10, Ccl.IN_DATA);
    assertToken(Token.DATA, "y", 0, 13, Ccl.IN_DATA);
    assertToken(Token.TAG_OPEN, "<", 0, 14, Ccl.IN_TAG);
    assertToken(Token.NAME, "b", 0, 15, Ccl.IN_TAG);
    assertToken(Token.TAG_CLOSE, ">", 0, 16, Ccl.IN_DATA);
    assertEof();
  }

  /** Before the first tag, only white space and {@code <} have a rule. */
  @Test
  public void beforeFirstTag() throws Exception {
    scan("a <");
    assertToken(Token.OTHER, "a", 0, 0, Ccl.YYINITIAL);
    assertToken(Token.FIRST_TAG_OPEN, "<", 0, 2, Ccl.IN_TAG);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
