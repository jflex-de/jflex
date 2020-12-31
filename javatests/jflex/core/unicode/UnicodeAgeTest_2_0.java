/*
 * Copyright (C) 2014-2020 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2020 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2020 Google, LLC.
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
package jflex.core.unicode;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;

public class UnicodeAgeTest_2_0 {

  UnicodeProperties properties;

  @Before
  public void init() throws Exception {
    properties = new UnicodeProperties("2.0");
  }

  @Test
  public void age() {
    assertThat(properties.getPropertyValues()).contains("age=1.1");
  }

  @Test
  public void ageIntervals() throws Exception {
    ScannerFactory<UnicodeAge_2_0_age_1_1> scannerFactory =
        ScannerFactory.of(UnicodeAge_2_0_age_1_1::new);
    UnicodeAge_2_0_age_1_1 scanner =
        scannerFactory.createScannerForFile(
            new File("java/de/jflex/testcase/resources/All.Unicode.BMP.characters.input"));
    while (scanner.yylex() != UnicodeAge_2_0_age_1_1.YYEOF) {}
    try (Stream<String> expectedOutput =
        Files.lines(Paths.get("javatests/jflex/core/unicode/UnicodeAge_2_0_age_1_1.output"))) {
      ImmutableList<String> expected = expectedOutput.collect(toImmutableList());
      assertThat(scanner.blocks()).containsAllIn(expected);
    }
  }
}
