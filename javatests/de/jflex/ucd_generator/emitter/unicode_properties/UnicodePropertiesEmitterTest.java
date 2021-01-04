/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
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
package de.jflex.ucd_generator.emitter.unicode_properties;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.ucd.UcdVersions;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

/** Test for {@link UnicodePropertiesEmitter}. */
public class UnicodePropertiesEmitterTest {

  @Test
  public void emitUnicodeProperties() throws Exception {
    File goldenFile =
        new File(
            "javatests/de/jflex/ucd_generator/emitter/unicode_properties/"
                + "UnicodeProperties.java.golden");

    // in-memory output
    DiffOutputStream output =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));

    UcdVersion ucd2_4 =
        UcdVersion.builder("2.4.0")
            .putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"))
            .build();
    UcdVersion ucd10_0 =
        UcdVersion.builder("10.0")
            .putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"))
            .build();
    UcdVersions versions =
        UcdVersions.builder()
            .put(ucd2_4.version().toString(), ucd2_4)
            .put(ucd10_0.version().toString(), ucd10_0)
            .build();
    UnicodePropertiesEmitter emitter = new UnicodePropertiesEmitter("org.example", versions);

    emitter.emitUnicodeProperties(output);
    assertThat(output.remainingContent()).isEmpty();
  }
}
