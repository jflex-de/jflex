/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
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
