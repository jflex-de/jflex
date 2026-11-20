/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.cup2private;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import edu.tum.cup2.scanner.Scanner;
import edu.tum.cup2.scanner.ScannerToken;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Regression test against <a href="https://github.com/jflex-de/jflex/issues/125">#125 %apiprivate
 * and %cup2 switches are incompatible </a>
 *
 * <p>The {@code %apiprivate} switch should not override the public visibility spec of {@link
 * edu.tum.cup2.scanner.Scanner#readNextTerminal} interface methods.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/cup2private/cup2private.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 3",
      "1 [a0]:",
      "2 [a1]:",
      "3 [a2]: 2 -> 3",
      "a0: return token(Token.OTHER, yytext());",
      "a1:",
      "a2: return token(Token.WORD, yytext());"
    },
    charClassesCount = 3,
    sameCharClass = {"!-\u0080", " \t\n\r\u0085\u00A0\u2028\u3000", "aZ_0\u00E4"},
    distinctCharClasses = {"! a"})
public class Cup2PrivateTest implements JFlexTest {

  private Cup2PrivateScanner scanner;

  private void assertToken(Token symbol, String text, int line, int column) throws Exception {
    ScannerToken<?> token = scanner.readNextTerminal();
    assertEquals(symbol + " (" + text + ")", token.toString());
    assertEquals(symbol, token.getSymbol());
    assertEquals(line, token.getLine(), "line of " + text);
    assertEquals(column, token.getColumn(), "column of " + text);
  }

  @Test
  public void readNextTerminal_isVisible() throws Exception {
    scanner = new Cup2PrivateScanner(new StringReader("Just checking\ncup2 !"));
    assertToken(Token.WORD, "Just", 0, 0);
    assertToken(Token.WORD, "checking", 0, 5);
    assertToken(Token.WORD, "cup2", 1, 0);
    assertToken(Token.OTHER, "!", 1, 5);
    ScannerToken<?> eof = scanner.readNextTerminal();
    assertEquals(Token.EOF, eof.getSymbol());
    assertEquals("EOF", eof.toString(), "no value");
  }

  @Test
  public void implementsCup2Scanner() throws Exception {
    Scanner cup2Scanner = new Cup2PrivateScanner(new StringReader("word"));
    assertEquals(Token.WORD, cup2Scanner.readNextTerminal().getSymbol());
    assertEquals(Token.EOF, cup2Scanner.readNextTerminal().getSymbol());
    Method scanMethod = Cup2PrivateScanner.class.getDeclaredMethod("readNextTerminal");
    assertTrue(Modifier.isPublic(scanMethod.getModifiers()));
  }

  @Test
  public void apiMethodsArePrivate() {
    List<String> apiMethods = new ArrayList<>();
    for (Method method : Cup2PrivateScanner.class.getDeclaredMethods()) {
      if (method.getName().startsWith("yy")) {
        assertTrue(Modifier.isPrivate(method.getModifiers()), method + " should be private");
        apiMethods.add(method.getName());
      }
    }
    for (String name : new String[] {"yytext", "yybegin", "yystate", "yyreset", "yyclose"}) {
      assertTrue(apiMethods.contains(name), "The scanner should have the method " + name);
    }
  }
}
