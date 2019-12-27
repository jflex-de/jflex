package jflex.maven.plugin.jflex;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/** Test for {@link LexSimpleAnalyzerUtils}. */
public class LexSimpleAnalyzerUtilsTest {

  private static final File dot = new File(".");

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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new ClassInfo("Foo", null));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new ClassInfo("Yylex", null));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new ClassInfo("Yylex", "org.example"));
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
    assertThat(guessPackageAndClass(lex)).isEqualTo(new ClassInfo("Yylex", null));
  }

  @Test
  public void guessPackageAndClass_with_includedFiles() throws Exception {
    String lex =
        "\n"
            + "package org.example;\n"
            + "\n"
            + "import java.io.File;\n"
            + "\n"
            + "\t%include  base.lexh\t \n";
    ClassInfo classInfo = guessPackageAndClass(lex);
    assertThat(classInfo).isEqualTo(new ClassInfo("Yylex", "org.example"));
    assertThat(classInfo.includedFiles).containsExactly(new File(dot, "base.lexh"));
  }

  private ClassInfo guessPackageAndClass(String lex) throws IOException {
    return LexSimpleAnalyzerUtils.guessPackageAndClass(new StringReader(lex), dot);
  }
}
