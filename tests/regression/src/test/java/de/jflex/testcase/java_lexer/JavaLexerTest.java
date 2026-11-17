/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.java_lexer;

import static de.jflex.testcase.java_lexer.Token.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Integration test on a scanner of the size of a programming language: a cut-down Java 1.2 lexer
 * with keywords, operators, literals with values, comments and two lexical states.
 *
 * <p>{@link #generatedScanner} scans a Java file that is larger than the scanner buffer.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/java_lexer/java.flex",
    // the fallback rule .|\n does not match all characters
    sysout = "src/test/java/de/jflex/testcase/java_lexer/java.sysout",
    quiet = true,
    minimizedDfaStatesCount = 426,
    charClassesCount = 76,
    sameCharClass = {
      // white space that is no line terminator
      " \t",
      // hex digits that are in no keyword and no suffix of a literal
      "ABC",
      // in identifiers only; j and q are in no keyword
      "GZ_$jq\u00E9",
      // line terminators for the dot that are in no other rule, and unpaired surrogates
      "\u000B\u2028\u2029\uD800"
    },
    distinctCharClasses = {
      // 0, the octal digits 1-3 and 4-7, and 8-9, split by the literal "-2147483648"
      "0123456789",
      // \f is white space, U+0085 can be part of an identifier
      "\n\r\f\u0085\u000B",
      // no rule, start of an identifier, digit, part of an identifier only (U+0660 is a digit)
      "#a0\u0660 "
    })
public class JavaLexerTest implements JFlexTest {

  private static final String BACKSLASH = "\\";

  private JavaLexer scanner;

  private void scan(String input) {
    scanner = new JavaLexer(new StringReader(input));
  }

  private void assertToken(Token token, String text, int line, int column, Object value)
      throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(token, scanner.yylex(), "token " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
    assertEquals(value, scanner.value(), "value " + at);
  }

  /** Asserts a token that has no value. */
  private void assertToken(Token token, String text, int line, int column) throws IOException {
    assertToken(token, text, line, column, null);
  }

  private void assertEof() throws IOException {
    assertEquals(EOF, scanner.yylex());
    assertEquals("", scanner.yytext());
    assertTrue(scanner.yyatEOF());
  }

  /** Asserts that the input is one token with the given value, at line 0 and column 0. */
  private void assertLiteral(Token token, String input, Object value) throws IOException {
    scan(input);
    assertToken(token, input, 0, 0, value);
    assertEof();
  }

  // --- a large input

  /** Number of tokens per kind in the input of {@link #generatedScanner}. */
  private static final Map<Token, Integer> EXPECTED_COUNTS = new EnumMap<>(Token.class);

  private static void expect(Token token, int count) {
    EXPECTED_COUNTS.put(token, count);
  }

  static {
    expect(BOOLEAN, 7);
    expect(BREAK, 142);
    expect(CASE, 277);
    expect(CATCH, 5);
    expect(CHAR, 15);
    expect(CLASS, 2);
    expect(DO, 5);
    expect(ELSE, 10);
    expect(FINAL, 27);
    expect(FOR, 4);
    expect(DEFAULT, 2);
    expect(IMPLEMENTS, 1);
    expect(INT, 104);
    expect(LONG, 3);
    expect(NEW, 38);
    expect(IF, 20);
    expect(PUBLIC, 17);
    expect(SWITCH, 2);
    expect(PRIVATE, 45);
    expect(RETURN, 139);
    expect(VOID, 6);
    expect(STATIC, 30);
    expect(WHILE, 12);
    expect(THIS, 2);
    expect(THROW, 5);
    expect(THROWS, 3);
    expect(TRY, 3);
    expect(BOOLEAN_LITERAL, 18);
    expect(NULL_LITERAL, 3);
    expect(LPAREN, 1019);
    expect(RPAREN, 1019);
    expect(LBRACE, 205);
    expect(RBRACE, 205);
    expect(LBRACK, 59);
    expect(RBRACK, 59);
    expect(SEMICOLON, 777);
    expect(COMMA, 71);
    expect(DOT, 643);
    expect(EQ, 144);
    expect(GT, 6);
    expect(LT, 13);
    expect(NOT, 1);
    expect(QUESTION, 1);
    expect(COLON, 281);
    expect(EQEQ, 9);
    expect(GTEQ, 1);
    expect(NOTEQ, 1);
    expect(ANDAND, 1);
    expect(PLUSPLUS, 25);
    expect(MINUSMINUS, 6);
    expect(PLUS, 1978);
    expect(MINUS, 13);
    expect(MULT, 1);
    expect(AND, 2);
    expect(OR, 1);
    expect(LSHIFT, 1);
    expect(PLUSEQ, 2);
    expect(MINUSEQ, 5);
    expect(MULTEQ, 1);
    expect(INTEGER_LITERAL, 622);
    expect(IDENTIFIER, 2202);
    expect(STRING_LITERAL, 1569);
    expect(CHARACTER_LITERAL, 25);
    expect(EOF, 1);
  }

  /** The line and column of every offset of the input. */
  private static int[][] positions(String input) {
    int[][] result = new int[input.length() + 1][];
    int line = 0;
    int column = 0;
    for (int i = 0; i < input.length(); i++) {
      result[i] = new int[] {line, column};
      char c = input.charAt(i);
      boolean beforeLf = i + 1 < input.length() && input.charAt(i + 1) == '\n';
      if ("\n\u000B\u000C\u0085\u2028\u2029".indexOf(c) >= 0 || (c == '\r' && !beforeLf)) {
        line++;
        column = 0;
      } else {
        column++;
      }
    }
    result[input.length()] = new int[] {line, column};
    return result;
  }

  /**
   * Scans a Java file of 88 KB, which larger than the scanner buffer. Check for every token that
   * the text is the text of the input at the offset of the token, and that line and column are
   * correct. Also check that the offset at the end of the input is the length of the input (=
   * everything is matched exactly once).
   */
  @Test
  public void generatedScanner() throws Exception {
    String input =
        new String(
            Files.readAllBytes(
                Paths.get("src/test/java/de/jflex/testcase/java_lexer/scanner.input")),
            StandardCharsets.UTF_8);
    assertTrue(input.length() > 16384, "Test input should be larger than the buffer");
    int[][] positions = positions(input);
    Map<Token, Integer> counts = new EnumMap<>(Token.class);
    scan(input);
    int end = 0;
    Token token;
    do {
      token = scanner.yylex();
      counts.merge(token, 1, Integer::sum);
      int offset = Math.toIntExact(scanner.offset());
      String at = token + " at offset " + offset;
      assertTrue(offset >= end, () -> "No overlap with the token before " + at);
      end = offset + scanner.yylength();
      assertEquals(input.substring(offset, end), scanner.yytext(), "text of " + at);
      assertEquals(positions[offset][0], scanner.line(), "line of " + at);
      assertEquals(positions[offset][1], scanner.column(), "column of " + at);
      if (token == IDENTIFIER) {
        assertEquals(scanner.yytext(), scanner.value(), "value of " + at);
      }
    } while (token != EOF);
    assertEquals(input.length(), scanner.offset(), "offset at the end of the input");
    assertTrue(scanner.yyatEOF());
    assertEquals(EXPECTED_COUNTS, counts);
    assertEquals(11916, counts.values().stream().mapToInt(Integer::intValue).sum());
  }

  // --- every keyword, separator and operator

  private static final String KEYWORDS =
      "abstract boolean break byte case catch char class const continue do double else extends"
          + " final finally float for default implements import instanceof int interface long"
          + " native new goto if public short super switch synchronized package private"
          + " protected transient return void static while this throw throws try volatile"
          + " strictfp";

  /** Pairs of text and token. */
  private static final String OPERATORS =
      "( LPAREN ) RPAREN { LBRACE } RBRACE [ LBRACK ] RBRACK ; SEMICOLON , COMMA . DOT = EQ > GT"
          + " < LT ! NOT ~ COMP ? QUESTION : COLON == EQEQ <= LTEQ >= GTEQ != NOTEQ && ANDAND"
          + " || OROR ++ PLUSPLUS -- MINUSMINUS + PLUS - MINUS * MULT / DIV & AND | OR ^ XOR"
          + " % MOD << LSHIFT >> RSHIFT >>> URSHIFT += PLUSEQ -= MINUSEQ *= MULTEQ /= DIVEQ"
          + " &= ANDEQ |= OREQ ^= XOREQ %= MODEQ <<= LSHIFTEQ >>= RSHIFTEQ >>>= URSHIFTEQ";

  /** The token of a keyword has the name of the keyword. */
  @Test
  public void keywords() throws Exception {
    String[] keywords = KEYWORDS.split(" ");
    assertEquals(48, keywords.length);
    for (String keyword : keywords) {
      assertLiteral(Token.valueOf(keyword.toUpperCase()), keyword, null);
    }
  }

  /** A keyword is only a keyword as a whole word, in lower case. */
  @Test
  public void keywordsAndIdentifiers() throws Exception {
    scan("int integer Int _a $1 if0 if");
    assertToken(INT, "int", 0, 0);
    assertToken(IDENTIFIER, "integer", 0, 4, "integer");
    assertToken(IDENTIFIER, "Int", 0, 12, "Int");
    assertToken(IDENTIFIER, "_a", 0, 16, "_a");
    assertToken(IDENTIFIER, "$1", 0, 19, "$1");
    assertToken(IDENTIFIER, "if0", 0, 22, "if0");
    assertToken(IF, "if", 0, 26);
    assertEof();
  }

  @Test
  public void separatorsAndOperators() throws Exception {
    String[] operators = OPERATORS.split(" ");
    assertEquals(2 * 46, operators.length);
    for (int i = 0; i + 1 < operators.length; i += 2) {
      assertLiteral(Token.valueOf(operators[i + 1]), operators[i], null);
    }
  }

  /** The longest operator that the input starts with is the match. */
  @Test
  public void longestOperator() throws Exception {
    scan(">>>>=");
    assertToken(URSHIFT, ">>>", 0, 0);
    assertToken(GTEQ, ">=", 0, 3);
    assertEof();
    scan("a+++b");
    assertToken(IDENTIFIER, "a", 0, 0, "a");
    assertToken(PLUSPLUS, "++", 0, 1);
    assertToken(PLUS, "+", 0, 3);
    assertToken(IDENTIFIER, "b", 0, 4, "b");
    assertEof();
  }

  // --- literals

  @Test
  public void booleanAndNullLiterals() throws Exception {
    assertLiteral(BOOLEAN_LITERAL, "true", true);
    assertLiteral(BOOLEAN_LITERAL, "false", false);
    assertLiteral(NULL_LITERAL, "null", null);
  }

  /**
   * The smallest {@code int} is one literal with sign, all other negative numbers are a minus and a
   * literal.
   */
  @Test
  public void smallestIntIsOneToken() throws Exception {
    scan("-2147483648\n- 2147483647\n-0xFFF\n-0372\n");
    assertToken(INTEGER_LITERAL, "-2147483648", 0, 0, Integer.MIN_VALUE);
    assertToken(MINUS, "-", 1, 0);
    assertToken(INTEGER_LITERAL, "2147483647", 1, 2, Integer.MAX_VALUE);
    assertToken(MINUS, "-", 2, 0);
    assertToken(INTEGER_LITERAL, "0xFFF", 2, 1, 4095);
    assertToken(MINUS, "-", 3, 0);
    assertToken(INTEGER_LITERAL, "0372", 3, 1, 250);
    assertEof();
  }

  @Test
  public void integerLiterals() throws Exception {
    assertLiteral(INTEGER_LITERAL, "0", 0);
    assertLiteral(INTEGER_LITERAL, "1234567890", 1234567890);
    assertLiteral(INTEGER_LITERAL, "0x7fffffff", Integer.MAX_VALUE);
    assertLiteral(INTEGER_LITERAL, "0XFFFFFFFF", -1);
    assertLiteral(INTEGER_LITERAL, "0x000000000A", 10);
    assertLiteral(INTEGER_LITERAL, "017", 15);
    assertLiteral(INTEGER_LITERAL, "037777777777", -1);
  }

  @Test
  public void longLiterals() throws Exception {
    assertLiteral(INTEGER_LITERAL, "1L", 1L);
    assertLiteral(INTEGER_LITERAL, "2147483648l", 2147483648L);
    assertLiteral(INTEGER_LITERAL, "0x10L", 16L);
    assertLiteral(INTEGER_LITERAL, "0xFFFFFFFFFFFFFFFFL", -1L);
    assertLiteral(INTEGER_LITERAL, "010L", 8L);
  }

  /** A ninth hex digit is not part of an {@code int} literal. */
  @Test
  public void hexLiteralOfAtMostEightDigits() throws Exception {
    scan("0x123456789");
    assertToken(INTEGER_LITERAL, "0x12345678", 0, 0, 0x12345678);
    assertToken(INTEGER_LITERAL, "9", 0, 10, 9);
    assertEof();
  }

  @Test
  public void floatingPointLiterals() throws Exception {
    assertLiteral(FLOATING_POINT_LITERAL, "1.5", 1.5);
    assertLiteral(FLOATING_POINT_LITERAL, "1.", 1.0);
    assertLiteral(FLOATING_POINT_LITERAL, ".5", 0.5);
    assertLiteral(FLOATING_POINT_LITERAL, "1e3", 1000.0);
    assertLiteral(FLOATING_POINT_LITERAL, "1.5E-1", 0.15);
    assertLiteral(FLOATING_POINT_LITERAL, "2d", 2.0);
    assertLiteral(FLOATING_POINT_LITERAL, "2.5D", 2.5);
    assertLiteral(FLOATING_POINT_LITERAL, "1.5f", 1.5f);
    assertLiteral(FLOATING_POINT_LITERAL, "1e+1F", 10.0f);
  }

  /** Digits alone are an int. */
  @Test
  public void digitsAreAnInteger() throws Exception {
    assertLiteral(INTEGER_LITERAL, "15", 15);
    scan("1.e");
    assertToken(FLOATING_POINT_LITERAL, "1.", 0, 0, 1.0);
    assertToken(IDENTIFIER, "e", 0, 2, "e");
    assertEof();
  }

  /**
   * A string literal is matched in pieces in the lexical state {@code STRING}. The token is
   * returned for the closing quote and has the value of the string.
   */
  @Test
  public void stringLiteral() throws Exception {
    scan("x = \"ab\";");
    assertToken(IDENTIFIER, "x", 0, 0, "x");
    assertToken(EQ, "=", 0, 2);
    assertToken(STRING_LITERAL, "\"", 0, 7, "ab");
    assertEquals(JavaLexer.YYINITIAL, scanner.yystate());
    assertToken(SEMICOLON, ";", 0, 8);
    assertEof();
    scan("\"\"");
    assertToken(STRING_LITERAL, "\"", 0, 1, "");
    assertEof();
  }

  @Test
  public void stringLiteralWithEscapes() throws Exception {
    // "a\tb\101\"\"
    scan(
        "\"a" + BACKSLASH + "tb" + BACKSLASH + "101" + BACKSLASH + "\"" + BACKSLASH + BACKSLASH
            + "\"");
    assertToken(STRING_LITERAL, "\"", 0, 13, "a\tbA\"" + BACKSLASH);
    assertEof();
    // each of the escapes \b \t \n \f \r \' and an octal escape of one, two and three digits
    scan("\"" + String.join(BACKSLASH, "", "b", "t", "n", "f", "r", "'", "7", "77", "377") + "\"");
    assertToken(STRING_LITERAL, "\"", 0, 22, "\b\t\n\f\r'\u0007?\u00FF");
    assertEof();
  }

  /** The scanner does not convert Unicode escapes. */
  @Test
  public void stringLiteralWithUnicodeEscape() throws Exception {
    String escape = BACKSLASH + "u0041";
    scan("\"" + escape + "\"");
    assertToken(STRING_LITERAL, "\"", 0, 7, escape);
    assertEof();
  }

  /** The keywords and comments of the initial state do not apply inside a string. */
  @Test
  public void stringLiteralWithKeywordAndComment() throws Exception {
    scan("\"if /* a */ // b\" if");
    assertToken(STRING_LITERAL, "\"", 0, 16, "if /* a */ // b");
    assertToken(IF, "if", 0, 18);
    assertEof();
  }

  @Test
  public void stringLiteralErrors() throws Exception {
    scan("\"a\n\"");
    assertEquals(
        "Unterminated string at end of line",
        assertThrows(RuntimeException.class, scanner::yylex).getMessage());
    scan("\"" + BACKSLASH + "q\"");
    assertEquals(
        "Illegal escape sequence \"" + BACKSLASH + "q\"",
        assertThrows(RuntimeException.class, scanner::yylex).getMessage());
  }

  /** The match of a character literal is the text after the opening quote. */
  @Test
  public void characterLiterals() throws Exception {
    scan("'a' '\"' '" + BACKSLASH + "n' '" + BACKSLASH + "'' '" + BACKSLASH + "101'");
    assertToken(CHARACTER_LITERAL, "a'", 0, 1, 'a');
    assertToken(CHARACTER_LITERAL, "\"'", 0, 5, '"');
    assertToken(CHARACTER_LITERAL, BACKSLASH + "n'", 0, 9, '\n');
    assertToken(CHARACTER_LITERAL, BACKSLASH + "''", 0, 14, '\'');
    assertToken(CHARACTER_LITERAL, BACKSLASH + "101'", 0, 19, 'A');
    assertEquals(JavaLexer.YYINITIAL, scanner.yystate());
    assertEof();
  }

  @Test
  public void characterLiteralErrors() throws Exception {
    scan("'\n'");
    assertEquals(
        "Unterminated character literal at end of line",
        assertThrows(RuntimeException.class, scanner::yylex).getMessage());
    // two characters: no rule of the state matches, and the fallback rule does
    scan("'ab'");
    assertEquals(
        "Illegal character \"a\" at line 0, column 1",
        assertThrows(RuntimeException.class, scanner::yylex).getMessage());
  }

  // --- comments

  @Test
  public void comments() throws Exception {
    scan("/* a */x// b\n/** c */y/**/z/***/w // d");
    assertToken(IDENTIFIER, "x", 0, 7, "x");
    assertToken(IDENTIFIER, "y", 1, 8, "y");
    assertToken(IDENTIFIER, "z", 1, 13, "z");
    assertToken(IDENTIFIER, "w", 1, 19, "w");
    assertEof();
  }

  /** A comment ends at the first end marker and can have several lines. */
  @Test
  public void commentEnd() throws Exception {
    scan("/* a\n * b */ c */");
    assertToken(IDENTIFIER, "c", 1, 8, "c");
    assertToken(MULT, "*", 1, 10);
    assertToken(DIV, "/", 1, 11);
    assertEof();
  }

  /** Without an end marker there is no comment, and the scanner goes on with the operators. */
  @Test
  public void commentWithoutEnd() throws Exception {
    scan("/* a");
    assertToken(DIV, "/", 0, 0);
    assertToken(MULT, "*", 0, 1);
    assertToken(IDENTIFIER, "a", 0, 3, "a");
    assertEof();
  }

  // --- the rest

  /** The three line terminators of the spec and other white space. */
  @Test
  public void whiteSpace() throws Exception {
    scan("a\nb\rc\r\nd \t\fe");
    assertToken(IDENTIFIER, "a", 0, 0, "a");
    assertToken(IDENTIFIER, "b", 1, 0, "b");
    assertToken(IDENTIFIER, "c", 2, 0, "c");
    assertToken(IDENTIFIER, "d", 3, 0, "d");
    // the form feed is a line terminator for the line count of JFlex
    assertToken(IDENTIFIER, "e", 4, 0, "e");
    assertEof();
  }

  @Test
  public void illegalCharacter() throws Exception {
    scan("a #");
    assertToken(IDENTIFIER, "a", 0, 0, "a");
    assertEquals(
        "Illegal character \"#\" at line 0, column 2",
        assertThrows(RuntimeException.class, scanner::yylex).getMessage());
  }

  /**
   * The fallback rule {@code .|\n} does not match the line terminators other than {@code \n}, which
   * is what the JFlex warning is about.
   */
  @Test
  public void characterWithoutRule() throws Exception {
    scan("\u0085");
    Error error = assertThrows(Error.class, scanner::yylex);
    assertEquals("Error: could not match input", error.getMessage());
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
