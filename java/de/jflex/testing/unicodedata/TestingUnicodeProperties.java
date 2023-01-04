/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.ucd.CodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.function.Function;

public class TestingUnicodeProperties {

  private TestingUnicodeProperties() {}

  public static <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>> void checkProperty(
      Function<Reader, T> constructorRef, int eof, Path expectedFile, Dataset dataset)
      throws IOException {
    T scanner =
        UnicodeDataScanners.scanAllCodepoints(ScannerFactory.of(constructorRef), eof, dataset);

    ImmutableList<CodepointRange> expectedBlocks = SimpleIntervalsParser.parseRanges(expectedFile);
    assertThat(scanner.ranges()).isEqualTo(expectedBlocks);
  }
}
