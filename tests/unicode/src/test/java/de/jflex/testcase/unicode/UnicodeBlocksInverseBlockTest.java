/*
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/** Tests the inverse of the Unicode block property character class syntax for one block. */
public class UnicodeBlocksInverseBlockTest {

  /**
   * Tests that parsing all Unicode point with <code>\P{Block:Latin Extended Additional}</code>
   * detects
   *
   * <pre>
   * 0000..1DFF; Not Latin Extended Additional
   * 1E00..1EFF; Latin Extended Additional
   * 1F00..D7FF; Not Latin Extended Additional
   * E000..FFFD; Not Latin Extended Additional
   * </pre>
   */
  @Test
  public void testInverseBlock() throws Exception {
    AbstractEnumeratedPropertyDefinedScanner scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeBlocksInverseBlockScanner::new),
            UnicodeBlocksInverseBlockScanner.YYEOF,
            Dataset.BMP);
    assertThat(scanner.blocks())
        .containsExactly(
            NamedCodepointRange.create(TestingBlock.NOT_LATIN_EXTENDED_ADDITIONAL, 0x0000, 0x1DFF),
            NamedCodepointRange.create(TestingBlock.LATIN_EXTENDED_ADDITIONAL, 0x1E00, 0x1EFF),
            NamedCodepointRange.create(TestingBlock.NOT_LATIN_EXTENDED_ADDITIONAL, 0x1F00, 0xD7FF),
            NamedCodepointRange.create(TestingBlock.NOT_LATIN_EXTENDED_ADDITIONAL, 0xE000, 0xFFFD))
        .inOrder();
  }
}
