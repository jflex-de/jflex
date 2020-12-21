// test: eofclose

package jflex.testcase.eofclose;

import static com.google.common.truth.Truth.assertWithMessage;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Test for {@code eofclose} directive. */
public class EofCloseTest {

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
    assertWithMessage(
            "The scanner should not close the reader at EOF, making reading further legal,"
                + " even though there is no more content")
        .that(reader.read())
        .isEqualTo(-1);
  }

  private static Reader readContent() throws IOException {
    CharSource content =
        CharSource.wrap(
            "" + //
                "bla\n" + // bla
                "blub\n" + // blub
                "\n");
    return content.openStream();
  }
}
