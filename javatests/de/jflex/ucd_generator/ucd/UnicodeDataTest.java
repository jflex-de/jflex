/*
 * Copyright (C) 2020 Google, LLC.
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
package de.jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_generator.TestedVersions;
import org.junit.Test;

public class UnicodeDataTest {

  @Test
  public void addCompatibilityProperties() {
    UnicodeData unicodeData = new UnicodeData(TestedVersions.UCD_VERSION_6_3.version());
    unicodeData.addBinaryPropertyInterval("nd", '\u0030', '\u0039');
    unicodeData.addBinaryPropertyInterval("hexdigit", '\u0041', '\u0046');
    unicodeData.addBinaryPropertyInterval("zl", '\u2028', '\u2028');
    unicodeData.addBinaryPropertyInterval("zp", '\u2029', '\u2029');
    unicodeData.addBinaryPropertyInterval("whitespace", '\t', '\r');
    unicodeData.addBinaryPropertyInterval("whitespace", '\u2028', '\u2029');
    unicodeData.addCompatibilityProperties();
    assertThat(unicodeData.intervals().get("blank").ranges())
        .contains(CodepointRange.createPoint('\t'));
  }
}
