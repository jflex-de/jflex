package de.jflex.testcase.bol;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.CharSource;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Tests BOL and EOL operators. */
public class BolTest {
  // standard BOL and EOL tests
  BolScanner scanner;
  // test BOL in absence of char class for NL characters
  Bol2Scanner scanner2;

  @Before
  public void testMustInitializeScanner() {
    scanner = null;
    scanner2 = null;
  }

  @After
  public void end() throws Exception {
    if (scanner != null) {
      assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
    }
    if (scanner2 != null) {
      assertThat(scanner2.yylex()).isEqualTo(State.END_OF_FILE);
    }
  }

  @Test
  public void initial() throws Exception {
    scanner = createScanner("");
    assertThat(scanner.state).isEqualTo(State.INITIAL);
  }

  @Test
  public void bolAndEol() throws Exception {
    scanner = createScanner("hello\n");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL_AND_EOL);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void bolAndEol2() throws Exception {
    scanner = createScanner("hello\r\n");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL_AND_EOL);
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void bolAndEol3() throws Exception {
    scanner = createScanner("\nhello\r\n");
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL_AND_EOL);
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void bolAndEof() throws Exception {
    // EOF does not count as EOL. Should it?
    scanner = createScanner("hello");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL);
  }

  @Test
  public void eol() throws Exception {
    scanner = createScanner("  hello\n");
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_EOL);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void hello() throws Exception {
    scanner = createScanner("  hello  \n");
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_SIMPLY);
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void bol() throws Exception {
    scanner = createScanner("hello \n");
    assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_BOL);
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void space() throws Exception {
    scanner = createScanner(" \n");
    assertThat(scanner.yylex()).isEqualTo(State.SPACE);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void other() throws Exception {
    scanner = createScanner("abc\n");
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
  }

  @Test
  public void bolNoNLCCL() throws Exception {
    scanner2 = createScanner2("hello");
    assertThat(scanner2.yylex()).isEqualTo(State.HELLO_AT_BOL);
  }

  @Test
  public void bolNoNLCCL2() throws Exception {
    scanner2 = createScanner2("hello\n");
    assertThat(scanner2.yylex()).isEqualTo(State.HELLO_AT_BOL);
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
  }

  @Test
  public void noBolNoNLCCL() throws Exception {
    scanner2 = createScanner2("\n  hello  ");
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner2.yylex()).isEqualTo(State.HELLO_SIMPLY);
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
    assertThat(scanner2.yylex()).isEqualTo(State.OTHER);
  }

  private static BolScanner createScanner(final String content) throws IOException {
    return new BolScanner(CharSource.wrap(content).openStream());
  }

  private static Bol2Scanner createScanner2(final String content) throws IOException {
    return new Bol2Scanner(CharSource.wrap(content).openStream());
  }
}
