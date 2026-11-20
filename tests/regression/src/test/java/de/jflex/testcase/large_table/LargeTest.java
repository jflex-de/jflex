/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_table;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/952">#952</a>.
 *
 * <p>This class has no {@code @TestSpec}, because that would generate the large scanner a second
 * time. Instead, it reads the number of states and the character classes from the private tables of
 * the scanner via reflection.
 */
public class LargeTest {

  /** Number of states of the minimized DFA. */
  private static final int STATES = 79107;

  /** The length of a private static table of the scanner. */
  private static int tableLength(String name) throws Exception {
    Field table = Large.class.getDeclaredField(name);
    table.setAccessible(true);
    return Array.getLength(table.get(null));
  }

  /** The number of the character class of a code point in the scanner. */
  private static int charClass(int codePoint) throws Exception {
    Method cmap = Large.class.getDeclaredMethod("zzCMap", int.class);
    cmap.setAccessible(true);
    return (Integer) cmap.invoke(null, codePoint);
  }

  private static void assertSameCharClass(int... codePoints) throws Exception {
    for (int c : codePoints) {
      assertEquals(charClass(codePoints[0]), charClass(c), String.format("class of U+%04X", c));
    }
  }

  @Test
  public void largeTable() throws Exception {
    // ((a|b)*b.{10}){3}
    Large scanner = new Large(new StringReader("xababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz"));
    assertEquals(0, scanner.yylex());
    assertEquals("x", scanner.yytext());
    assertEquals(1, scanner.yylex());
    assertEquals("ababbbbbbbbbbbabbbbbbbbbbbbababbbbbbbbbbbazaz", scanner.yytext());
    assertEquals(-1, scanner.yylex());
  }

  /** The scanner has one entry per state in these tables. */
  @Test
  public void numberOfStates() throws Exception {
    assertEquals(STATES, tableLength("ZZ_ACTION"));
    assertEquals(STATES, tableLength("ZZ_ROWMAP"));
    assertEquals(STATES, tableLength("ZZ_ATTRIBUTE"));
  }

  /** Four classes: {@code a}, {@code b}, everything {@code .} excludes, and the rest. */
  @Test
  public void charClasses() throws Exception {
    Set<Integer> classes = new HashSet<>();
    for (int c = 0; c <= Character.MAX_CODE_POINT; c++) {
      classes.add(charClass(c));
    }
    assertEquals(4, classes.size(), "number of character classes");

    // what . does not match
    assertSameCharClass(
        '\n', '\r', 0x0B, 0x0C, 0x85, 0x2028, 0x2029, 0xD800, 0xDBFF, 0xDC00, 0xDFFF);
    assertSameCharClass(
        'x',
        0,
        '\t',
        0x0E,
        'c',
        'A',
        'B',
        ' ',
        0x84,
        0x86,
        0x2027,
        0x202A,
        0xD7FF,
        0xE000,
        0x1F600,
        Character.MAX_CODE_POINT);

    Set<Integer> distinct = new HashSet<>();
    for (char c : new char[] {'a', 'b', '\n', 'x'}) {
      distinct.add(charClass(c));
    }
    assertEquals(4, distinct.size(), "a, b, \\n and x are in different classes");
  }

  @Test
  public void lineTerminatorEndsLargeMatch() throws Exception {
    Large scanner = new Large(new StringReader("b\nbbbbbbbbb"));
    for (String c : new String[] {"b", "\n", "b"}) {
      assertEquals(0, scanner.yylex());
      assertEquals(c, scanner.yytext());
    }
    assertNotEquals(-1, scanner.yylex());
  }
}
