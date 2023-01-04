/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import java.io.File;
import org.junit.Test;

public class UcdVersionsTest {

  private final UcdVersion UCD1 =
      UcdVersion.builder("1.2.3")
          .putFile(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"))
          .build();

  private final UcdVersion UCD2 =
      UcdVersion.builder("2.0")
          .putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"))
          .build();

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
    UcdVersions ucdVersions = UcdVersions.builder().put("1.2.3", UCD1).put("1.3.5", UCD2).build();
    assertThat(ucdVersions.expandAllVersions())
        .containsExactly("1.2", "1.2.3", "1.3", "1.3.5")
        .inOrder();
  }

  @Test
  public void expandAllVersions_withMajor() throws Exception {
    UcdVersions ucdVersions = UcdVersions.builder().put("1.0.3", UCD1).put("1.3.5", UCD2).build();
    assertThat(ucdVersions.expandAllVersions())
        .containsExactly("1", "1.0", "1.0.3", "1.3", "1.3.5")
        .inOrder();
  }

  @Test
  public void expandAllVersions_unnaturalOrder() {
    UcdVersions ucdVersions =
        UcdVersions.builder().put("1.2.3", UCD1).put("2.4.6", UCD1).put("10.0.0", UCD1).build();
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
  public void findExternalPath_oldVersion() {
    File bazelDep =
        new File("jflex/ucd_generator/external/ucd_1_1_5_UnicodeData_1_1_5_txt/file/downloaded");
    UcdVersion ucd =
        UCD1.toBuilder().setVersion("1.1").putFile(UcdFileType.WordBreakProperty, bazelDep).build();
    assertThat(ucd.getFile(UcdFileType.WordBreakProperty))
        .isEqualTo(new File("external/ucd_1_1_5_UnicodeData_1_1_5_txt/file/downloaded"));
  }
}
