/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.ucd.CodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class TestingUnicodeProperties {

  private TestingUnicodeProperties() {}

  public static <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>> void checkProperty(
      Function<Reader, T> constructorRef, int eof, Path expectedFile, Dataset dataset)
      throws IOException {
    T scanner =
        UnicodeDataScanners.scanAllCodepoints(ScannerFactory.of(constructorRef), eof, dataset);

    List<CodepointRange> expectedBlocks = SimpleIntervalsParser.parseRanges(expectedFile);
    assertEquals(expectedBlocks, scanner.ranges());
  }
}
