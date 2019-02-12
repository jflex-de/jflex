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
  public void expandVersions_majorOnly() throws Exception {
    assertThat(UcdVersions.expandVersion("1")).containsExactly("1");
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
}
