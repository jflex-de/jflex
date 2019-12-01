// test: eofclose

package jflex.testcase.eofclose;

import static jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test for {@code eofclose} directive. */
public class EofCloseTest extends AbstractGoldenTest {

  @Test
  public void eofcloseDirective_closesReaderAtEof() throws Exception {
    CharSource content =
        CharSource.wrap(
            "" + //
                "bla\n" + // bla
                "blub\n" + // blub
                "\n");
    Reader reader = content.openStream();
    ScannerFactory<Eofclose> scannerFactory = ScannerFactory.of(Eofclose::new);
    Eofclose scanner = scannerFactory.createForReader(reader);
    while (scanner.yylex() != Eofclose.YYEOF) {}
    assertThrows(
        "The scanner should close the reader at EOF, making reading further impossible",
        IOException.class,
        reader::read);
  }
}
