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
package jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import org.junit.Before;
import org.junit.Test;

public class UcdVersionsTest {

  private UcdVersion.Builder ucd1;

  private UcdVersion.Builder ucd2;

  @Before
  public void createUcd() {
    ucd1 = UcdVersion.builder().putFile(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    ucd2 = UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
  }

  @Test
  public void expandVersion_majorUpdate() throws Exception {
    assertThat(UcdVersions.expandVersion("1.0.2")).containsExactly("1", "1.0", "1.0.2");
  }

  @Test
  public void expandVersion_majorMinorUpdate() throws Exception {
    assertThat(UcdVersions.expandVersion("1.2.3")).containsExactly("1.2", "1.2.3");
  }

  @Test
  public void expandVersion_majorOnly() throws Exception {
    assertThat(UcdVersions.expandVersion("1")).containsExactly("1");
  }

  @Test
  public void expandAllVersions() throws Exception {
    UcdVersions ucdVersions = UcdVersions.builder().put("1.2.3", ucd1).put("1.3.5", ucd2).build();
    assertThat(ucdVersions.expandAllVersions())
        .containsExactly("1.2", "1.2.3", "1.3", "1.3.5")
        .inOrder();
  }

  @Test
  public void expandAllVersions_withMajor() throws Exception {
    UcdVersions ucdVersions = UcdVersions.builder().put("1.0.3", ucd1).put("1.3.5", ucd2).build();
    assertThat(ucdVersions.expandAllVersions())
        .containsExactly("1", "1.0", "1.0.3", "1.3", "1.3.5")
        .inOrder();
  }

  @Test
  public void expandAllVersions_unnaturalOrder() {
    UcdVersions ucdVersions =
        UcdVersions.builder().put("1.2.3", ucd1).put("2.4.6", ucd1).put("10.0.0", ucd1).build();
    assertThat(ucdVersions.expandAllVersions())
        .containsExactly("1.2", "1.2.3", "2.4", "2.4.6", "10", "10.0", "10.0.0")
        .inOrder();
  }

  @Test
  public void getClassNameForVersion() throws Exception {
    assertThat(UcdVersions.getClassNameForVersion("4.2")).isEqualTo("Unicode_4_2");
  }

  @Test
  public void getClassNameForVersion_majorOnly() throws Exception {
    assertThat(UcdVersions.getClassNameForVersion("5")).isEqualTo("Unicode_5");
  }

  @Test
  public void findExternalPath() {
    File bazelDep =
        new File("jflex/ucd_generator/external/ucd_1_1_5_UnicodeData_1_1_5_txt/file/downloaded");
    ucd1.setVersion("1.1").putFile(UcdFileType.WordBreakProperty, bazelDep);
    assertThat(ucd1.build().getFile(UcdFileType.WordBreakProperty))
        .isEqualTo(new File("external/ucd_1_1_5_UnicodeData_1_1_5_txt/file/downloaded"));
  }
}
