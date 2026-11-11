/*
 * JFlex Maven3 plugin
 * Copyright (c) 2019, Régis Décamps, Gerwin Klein, Chris Fraire
 * Credit goes to the authors of the ant task.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Test for {@link LexSimpleAnalyzerUtils}. */
public class LexSimpleAnalyzerUtilsTest {

  @Test
  public void guessPackageAndClass_givenClass_defaultPackage() throws Exception {
    String lex =
        "\n"
            + "%%\n"
            + "\n"
            + "%public\n"
            + "%class Foo\n"
            + "\n"
            + "%apiprivate\n"
            + "%int\n"
            + "\n"
            + "%%\n"
            + "\n"
            + "[^]  { /* no action */ }\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classWithLineComment() throws Exception {
    String lex = "%%\n%class Foo             // comment on same line allowed\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classWithBlockComment() throws Exception {
    String lex = "%%\n%class Foo/* comment */\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classWithTabsAndTrailingSpace() throws Exception {
    String lex = "%%\n%class\t\tFoo_1$ \t\n%%\n";
    assertEquals(new SpecInfo("Foo_1$", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classAfterOtherOption() throws Exception {
    String lex = "%%\n%public %class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  /** The generated file is named without the generic parameters (Emitter.getBaseName). */
  @Test
  public void guessPackageAndClass_genericClass() throws Exception {
    String lex = "%%\n%class Foo<T extends Comparable<T>>\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  /** {@code %class} without a name is a JFlex error, not a plugin error. */
  @Test
  public void guessPackageAndClass_classWithoutName_keepsLooking() throws Exception {
    String lex = "%%\n%class\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_defaultClass_defaultPackage() throws Exception {
    String lex =
        "\n"
            + "%%\n"
            + "\n"
            + "%public\n"
            + "\n"
            + "%%\n"
            + "\n"
            + "^\"hello\"$  { System.out.println(\"hello\"); }\n"
            + "\n";
    assertEquals(new SpecInfo("Yylex", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_defaultClass_hintPackage() throws Exception {
    String lex =
        "\n"
            + "package org.example;\n"
            + "\n"
            + "import java.io.File;\n"
            + "\n"
            + "%%\n"
            + "\n"
            + "%final\n"
            + "%public\n"
            + "\n";
    assertEquals(new SpecInfo("Yylex", "org.example"), guessPackageAndClass(lex));
  }

  /**
   * Tests that a random "package" string doesn't mislead JFlex in finding a package name.
   *
   * <p>See <a href="https://github.com/jflex-de/jflex/issues/104">issue #104</a>.
   */
  @Test
  public void guessPackageAndClass_defaultClass_misleadingPackage() throws Exception {
    String lex =
        "\n"
            + "%%\n"
            + "\n"
            + "%public\n"
            + "\n"
            + "%%\n"
            + "\n"
            + "<YYINITIAL> {"
            + "  \"package\"                      { return symbol(PACKAGE); }\n"
            + "  \"private\"                      { return symbol(PRIVATE); }"
            + "}\n"
            + "\n";
    assertEquals(new SpecInfo("Yylex", null), guessPackageAndClass(lex));
  }

  /**
   * Tests that a rule that starts with "package" is not taken for a package declaration.
   *
   * <p>See <a href="https://github.com/jflex-de/jflex/issues/539">issue #539</a>.
   */
  @Test
  public void guessPackageAndClass_packageInRule() throws Exception {
    String lex =
        "\n"
            + "%%\n"
            + "\n"
            + "%public\n"
            + "%class Foo\n"
            + "\n"
            + "%apiprivate\n"
            + "%int\n"
            + "\n"
            + "%%\n"
            + "\n"
            + "package X  { /* no action; */ }"
            + "\n"
            + "[^]  { /* no action */ }\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_packageInActionCode() throws Exception {
    String lex = "%%\n%class Foo\n%%\n[a] {\n  packageName = yytext(); }\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_packageLikeUserCode() throws Exception {
    String lex = "packageName = 1;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  /** A package declaration can only be the first thing in the user code. */
  @Test
  public void guessPackageAndClass_packageAfterImport() throws Exception {
    String lex = "import java.io.File;\npackage org.example;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_indentedPackage() throws Exception {
    String lex = "  \tpackage org.example;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", "org.example"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_packageAfterLicenceHeader() throws Exception {
    String lex =
        "/*\n"
            + " * Copyright; all rights reserved.\n"
            + " * package not.this;\n"
            + " */\n"
            + "// package nor.that;\n"
            + "\n"
            + "/* licence */ package org.example;\n"
            + "\n"
            + "import java.io.File;\n"
            + "%%\n"
            + "%class Foo\n"
            + "%%\n";
    assertEquals(new SpecInfo("Foo", "org.example"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_packageOverSeveralLines() throws Exception {
    String lex =
        "package\n  org . example // comment;\n  /* more; */ . sub\n;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", "org.example.sub"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_packageWithTrailingComment() throws Exception {
    String lex = "package org.example; // was: package old.name;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", "org.example"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_commentedOutPackage() throws Exception {
    String lex = "/* package org.example;\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_userCodeOnly() throws Exception {
    String lex = "package org.example;";
    assertEquals(new SpecInfo("Yylex", "org.example"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classInUserCode() throws Exception {
    String lex = "package p;\n// see %class Bar below\n%%\n%class Foo\n%%\n";
    assertEquals(new SpecInfo("Foo", "p"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_classInActionCode() throws Exception {
    String lex = "package p;\n%%\n%public\n%%\n[a] { String s = \"%class Bar\"; }\n";
    assertEquals(new SpecInfo("Yylex", "p"), guessPackageAndClass(lex));
  }

  @Test
  public void guessPackageAndClass_commentedOutClass() throws Exception {
    String lex = "%%\n// %class Bar\n%class Foo\n%public // was %class Baz\n%%\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  /** Java code blocks in the directives section are not scanned, and can contain anything. */
  @Test
  public void guessPackageAndClass_classInCodeBlock() throws Exception {
    String lex =
        "%%\n"
            + "%class Foo\n"
            + "%{\n"
            + "  /* generated from\n"
            + "%% a spec with\n"
            + "     %class Bar */\n"
            + "%}\n"
            + "%init{\n"
            + "  String s = \"%class Baz\";\n"
            + "%init}\n"
            + "%%\n"
            + "[a] { }\n";
    assertEquals(new SpecInfo("Foo", null), guessPackageAndClass(lex));
  }

  /** As in the JFlex scanner, the last {@code %class} directive wins. */
  @Test
  public void guessPackageAndClass_lastClassWins() throws Exception {
    String lex = "%%\n%class Foo\n%{\n%}\n%class Bar\n%%\n%class Baz\n";
    assertEquals(new SpecInfo("Bar", null), guessPackageAndClass(lex));
  }

  @Test
  public void parseIncludedFiles() throws Exception {
    String lex =
        "\n"
            + "package org.example;\n"
            + "\n"
            + "import java.io.File;\n"
            + "\n"
            + "\t%include  base.lexh\t \n"
            + "\n"
            + "%%\n"
            + "\n"
            + "  %include  two.lexh \n"
            + "\n"
            + "%%\n"
            + "\n"
            + "%include three.lexh\t \n"
            + " %include three.lexh\n";
    assertEquals(
        new HashSet<>(Arrays.asList("base.lexh", "two.lexh", "three.lexh")), parseIncludes(lex));
  }

  private Set<String> parseIncludes(String lex) throws IOException {
    return LexSimpleAnalyzerUtils.parseIncludes(new StringReader(lex));
  }

  private SpecInfo guessPackageAndClass(String lex) throws IOException {
    // dummy file should throw IOException in %include parsing and be ignored
    return LexSimpleAnalyzerUtils.guessSpecInfo(new StringReader(lex), new File(""));
  }
}
