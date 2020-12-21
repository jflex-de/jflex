package de.jflex.testcase.caseless_jflex;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.io.CharSource;
import java.io.IOException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test {@code %ignorecase} with JFlex semantics.
 *
 * <p>Only strings and chars are caseless.
 */
public class CaselessJflexTest {

  private CaselessScanner scanner;

  @After
  public void endOfFile() throws Exception {
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
  }

  @Test
  public void a() throws Exception {
    scanner = createScanner("a");
    assertThat(scanner.yylex()).isEqualTo(State.A);
  }

  @Test
  public void a_upper() throws Exception {
    scanner = createScanner("A");
    assertWithMessage("'A' matches 'a' in caseless mode").that(scanner.yylex()).isEqualTo(State.A);
  }

  @Test
  @Ignore // Is this a regression?
  public void hello() throws Exception {
    scanner = createScanner("hello");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO);
  }

  @Test
  public void hello_mixedCase() throws Exception {
    scanner = createScanner("HelLo");
    assertWithMessage("'HelLo' matches 'hello' in caseless mode")
        .that(scanner.yylex())
        .isEqualTo(State.HELLO);
  }

  @Test
  public void hello_within() throws Exception {
    scanner = createScanner("blubHELLObla");
    assertThat(scanner.yylex()).isEqualTo(State.WORD);
    assertThat(scanner.yylex()).isEqualTo(State.HELLO);
    assertThat(scanner.yylex()).isEqualTo(State.WORD);
  }

  @Test
  public void hello_withinMixedCase() throws Exception {
    scanner = createScanner("sdfahelLobla");
    assertThat(scanner.yylex()).isEqualTo(State.WORD); // sdfa
    assertWithMessage("'helLo' is mixed case and doesn't match 'hello' in JFlex mode")
        .that(scanner.yylex())
        .isEqualTo(State.OTHER); // helLo
    assertThat(scanner.yylex()).isEqualTo(State.WORD); // bla
  }

  @Test
  public void hello_newline() throws Exception {
    scanner = createScanner("HELLO\n;");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO);
    assertThat(scanner.yylex()).isEqualTo(State.NEW_LINE);
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
  }

  @Test
  public void other() throws Exception {
    scanner = createScanner("asd123hello;qwe");
    assertWithMessage("'asd' is word").that(scanner.yylex()).isEqualTo(State.WORD);
    assertWithMessage("'1' is other").that(scanner.yylex()).isEqualTo(State.OTHER); // 1
    assertWithMessage("'2' is other").that(scanner.yylex()).isEqualTo(State.OTHER); // 2
    assertWithMessage("'3' is other").that(scanner.yylex()).isEqualTo(State.OTHER); // 3
    assertWithMessage("'hello' is concatenated and doesn't create a HELLO qqtoken in that case")
        .that(scanner.yylex())
        .isEqualTo(State.WORD);
    assertWithMessage("';' is other").that(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.WORD); // qwe
  }

  private static CaselessScanner createScanner(String input) throws IOException {
    return new CaselessScanner(CharSource.wrap(input).openStream());
  }
}
