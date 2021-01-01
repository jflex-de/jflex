/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.jflex.testing.unicodedata;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/** Utility class to verify the Age property of UnicodeProperties. */
public class Ages {

  public static final File TEST_RESOURCES_DIR = new File("java/de/jflex/testcase/resources");

  private Ages() {}

  private static ImmutableList<String> getBlocks(
      ScannerFactory<? extends AbstractEnumeratedPropertyDefinedScanner> scannerFactory,
      int eof,
      Dataset dataset)
      throws IOException {
    AbstractEnumeratedPropertyDefinedScanner scanner =
        scannerFactory.createScannerForFile(new File(TEST_RESOURCES_DIR, dataset.dataFile));
    while (scanner.yylex() != eof) {}
    return scanner.blocks();
  }

  public static void assertAgeInterval(
      ScannerFactory<? extends AbstractEnumeratedPropertyDefinedScanner> scannerFactory,
      int eof,
      Dataset dataset,
      Path expectedFile)
      throws IOException {
    ImmutableList<String> blocks = getBlocks(scannerFactory, eof, dataset);
    try (Stream<String> expectedOutput = Files.lines(expectedFile)) {
      ImmutableList<String> expected = expectedOutput.collect(toImmutableList());
      assertThat(blocks).containsAllIn(expected);
    }
  }

  // TODO(regisd) The files can most ikely be replaced by in-memory providers.
  public enum Dataset {
    BMP("All.Unicode.BMP.characters.input"),
    ALL("All.Unicode.characters.input"),
    ;

    final String dataFile;

    Dataset(String filename) {
      this.dataFile = filename;
    }
  }
}
