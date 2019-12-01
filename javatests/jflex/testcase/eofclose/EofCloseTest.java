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
  public void eofcloseDirective_true_closesReaderAtEof() throws Exception {
    Reader reader = readContent();
    ScannerFactory<EofClose> scannerFactory = ScannerFactory.of(EofClose::new);
    EofClose scanner = scannerFactory.createForReader(reader);
    while (scanner.yylex() != EofClose.YYEOF) {}
    assertThrows(
        "The scanner should close the reader at EOF, making reading further impossible",
        IOException.class,
        reader::read);
  }

  @Test
  public void eofcloseDirective_false_doesntCloseReaderAtEof() throws Exception {
    Reader reader = readContent();
    ScannerFactory<EofNoClose> scannerFactory = ScannerFactory.of(EofNoClose::new);
    EofNoClose scanner = scannerFactory.createForReader(reader);
    while (scanner.yylex() != EofNoClose.YYEOF) {}
    // The scanner should not close the reader at EOF, making reading further legal
    reader.read();
  }

  private Reader readContent() throws IOException {
    CharSource content =
        CharSource.wrap(
            "" + //
                "bla\n" + // bla
                "blub\n" + // blub
                "\n");
    return content.openStream();
  }
}
