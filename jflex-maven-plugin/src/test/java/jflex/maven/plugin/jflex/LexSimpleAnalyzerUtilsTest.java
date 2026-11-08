/*
 * JFlex Maven3 plugin
 * Copyright (c) 2019, Régis Décamps, Gerwin Klein, Chris Fraire
 * Credit goes to the authors of the ant task.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.jflex;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import org.junit.Test;

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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
  }

  @Test
  public void guessPackageAndClass_classWithLineComment() throws Exception {
    String lex = "%%\n%class Foo             // comment on same line allowed\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
  }

  @Test
  public void guessPackageAndClass_classWithBlockComment() throws Exception {
    String lex = "%%\n%class Foo/* comment */\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
  }

  @Test
  public void guessPackageAndClass_classWithTabsAndTrailingSpace() throws Exception {
    String lex = "%%\n%class\t\tFoo_1$ \t\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo_1$", null));
  }

  @Test
  public void guessPackageAndClass_classAfterOtherOption() throws Exception {
    String lex = "%%\n%public %class Foo\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
  }

  /** The generated file is named without the generic parameters (Emitter.getBaseName). */
  @Test
  public void guessPackageAndClass_genericClass() throws Exception {
    String lex = "%%\n%class Foo<T extends Comparable<T>>\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
  }

  /** {@code %class} without a name is a JFlex error, not a plugin error. */
  @Test
  public void guessPackageAndClass_classWithoutName_keepsLooking() throws Exception {
    String lex = "%%\n%class\n%class Foo\n%%\n";
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Foo", null));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Yylex", null));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Yylex", "org.example"));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new SpecInfo("Yylex", null));
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
    assertThat(parseIncludes(lex)).containsExactly("base.lexh", "two.lexh", "three.lexh");
  }

  private Set<String> parseIncludes(String lex) throws IOException {
    return LexSimpleAnalyzerUtils.parseIncludes(new StringReader(lex));
  }

  private SpecInfo guessPackageAndClass(String lex) throws IOException {
    // dummy file should throw IOException in %include parsing and be ignored
    return LexSimpleAnalyzerUtils.guessSpecInfo(new StringReader(lex), new File(""));
  }
}
