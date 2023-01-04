/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.util.scanner.ScannerFactory;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/** Utility class to verify the Age property of UnicodeProperties. */
public class UnicodeDataScanners {

  public static final File TEST_RESOURCES_DIR = new File("testsuite/testcases/src/test/resources");

  private UnicodeDataScanners() {}

  public static <T extends AbstractEnumeratedPropertyDefinedScanner<?>> T scanAllCodepoints(
      ScannerFactory<T> scannerFactory, int eof, Dataset dataset) throws IOException {
    T scanner = scannerFactory.createScannerForFile(new File(TEST_RESOURCES_DIR, dataset.dataFile));
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

  // TODO(regisd) The files can most likely be replaced by in-memory providers.
  public enum Dataset {
    BMP("All.Unicode.BMP.characters.input"),
    ALL("All.Unicode.characters.input"),
    ;

    private final String dataFile;

    Dataset(String filename) {
      this.dataFile = filename;
    }

    public String dataFile() {
      return dataFile;
    }
  }
}
