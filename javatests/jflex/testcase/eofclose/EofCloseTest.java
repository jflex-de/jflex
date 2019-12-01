// test: eofclose

package jflex.testcase.eofclose;

import static jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.CharSource;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/** Test for {@code eofclose} directive. */
public class EofCloseTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/eofclose");

  @Test
  public void eofcloseDirective_closesReaderAtEof() throws Exception {
    Reader reader =
        CharSource.wrap(
                "" + //
                    "bla\n" + // bla
                    "blub\n" + // blub
                    "\n")
            .openStream();
    Eofclose scanner = new Eofclose(reader);
    while (scanner.yylex() != Eofclose.YYEOF) {}
    assertThrows("The scanner should close the reader at EOF", IOException.class, reader::read);
  }
}
