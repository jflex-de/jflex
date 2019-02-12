/*
 * Copyright (C) 2018 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

/** Test for {@link Emitter}. */
public class EmitterTest {

  @Test
  public void emitUnicodeProperties() throws Exception {
    File goldenFile = new File("javatests/jflex/ucd_generator/UnicodeProperties.java.golden");

    // in-memory output
    DiffOutputStream output =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));

    // fake ucd version 1.2
    UcdVersion.Builder ucd1_2 =
        UcdVersion.builder().putFile(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    UcdVersion.Builder ucd2_0 =
        UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
    UcdVersion.Builder ucd2_4 =
        UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
    UcdVersion.Builder ucd10_0 =
        UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
    UcdVersions versions =
        UcdVersions.of(
            "1.2.0", ucd1_2,
            "2.0.1", ucd2_0,
            "2.4.6", ucd2_4,
            "10.0.0", ucd10_0);
    Emitter emitter = new Emitter("org.example", versions);

    emitter.emitUnicodeProperties(output);
    assertThat(output.isCompleted()).isTrue();
  }
}
