// test: eofclose
/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eofclose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.util.scanner.ScannerFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** Test for {@code eofclose} directive. */
public class EofCloseTest {

  @Test
  public void eofcloseDirective_true_closesReaderAtEof() throws Exception {
    Reader reader = readContent();
    ScannerFactory<EofClose> scannerFactory = ScannerFactory.of(EofClose::new);
    EofClose scanner = scannerFactory.createForReader(reader);
    while (scanner.yylex() != EofClose.YYEOF) {}
    assertThrows(
        IOException.class,
        reader::read,
        "The scanner should close the reader at EOF, making reading further impossible");
  }

  @Test
  public void eofcloseDirective_false_doesntCloseReaderAtEof() throws Exception {
    Reader reader = readContent();
    ScannerFactory<EofNoClose> scannerFactory = ScannerFactory.of(EofNoClose::new);
    EofNoClose scanner = scannerFactory.createForReader(reader);
    while (scanner.yylex() != EofNoClose.YYEOF) {}
    assertEquals(
        -1,
        reader.read(),
        "The scanner should not close the reader at EOF, making reading further legal,"
            + " even though there is no more content");
  }

  private static Reader readContent() throws IOException {
    return new StringReader(
        "" + //
            "bla\n" + // bla
            "blub\n" + // blub
            "\n");
  }
}
