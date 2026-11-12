/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.AllCodePoints;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.util.scanner.ScannerFactory;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/** Utility class to verify the Age property of UnicodeProperties. */
public class UnicodeDataScanners {

  private UnicodeDataScanners() {}

  public static <T extends AbstractEnumeratedPropertyDefinedScanner<?>> T scanAllCodepoints(
      ScannerFactory<T> scannerFactory, int eof, Dataset dataset) throws IOException {
    T scanner = scannerFactory.createScannerWithContent(dataset.content());
    while (scanner.yylex() != eof) {}
    return scanner;
  }

  public static <T extends AbstractEnumeratedPropertyDefinedScanner<?>> void assertAgeInterval(
      ScannerFactory<T> scannerFactory, int eof, Dataset dataset, Path expectedFile)
      throws IOException {
    T scanner = scanAllCodepoints(scannerFactory, eof, dataset);
    ImmutableList<String> blocks =
        scanner.blocks().stream().map(NamedCodepointRange::toString).collect(toImmutableList());
    try (Stream<String> expectedOutput = Files.lines(expectedFile)) {
      ImmutableList<String> expected = expectedOutput.collect(toImmutableList());
      assertThat(blocks).containsExactlyElementsIn(expected);
    }
  }

  public static Dataset getDataset(Version version) {
    if (Versions.maxCodePoint(version) < 0x10000) {
      return Dataset.BMP;
    } else {
      return Dataset.ALL;
    }
  }

  /** The input of a scanner under test: all code points up to a last one. */
  public enum Dataset {
    BMP(0xFFFD),
    ALL(0x10FFFF),
    ;

    private final int lastCodePoint;
    private String content;

    Dataset(int lastCodePoint) {
      this.lastCodePoint = lastCodePoint;
    }

    /** Returns all code points up to {@link #lastCodePoint()} in order, except surrogates. */
    public synchronized String content() {
      if (content == null) {
        content = AllCodePoints.upTo(lastCodePoint);
      }
      return content;
    }

    /** Returns the last code point of {@link #content()}. */
    public int lastCodePoint() {
      return lastCodePoint;
    }
  }
}
