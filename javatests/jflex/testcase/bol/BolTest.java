package jflex.testcase.bol;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.CharSource;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Tests BOL and EOL operators. */
public class BolTest {
  BolScanner scanner;

  @Before
  public void testMustInitializeScanner() {
    scanner = null;
  }

  @After
  public void end() throws Exception {
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
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

  private static BolScanner createScanner(final String content) throws IOException {
    return new BolScanner(CharSource.wrap(content).openStream());
  }
}
