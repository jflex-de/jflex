/*
 * Copyright (C) 2018-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.ucd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import java.io.File;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

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
    assertEquals(Arrays.asList("1", "1.0", "1.0.2"), UcdVersions.expandVersion("1.0.2"));
  }

  @Test
  public void expandVersion_majorMinorUpdate() throws Exception {
    assertEquals(Arrays.asList("1.2", "1.2.3"), UcdVersions.expandVersion("1.2.3"));
  }

  @Test
  public void expandVersion_majorOnly() throws Exception {
    assertEquals(Arrays.asList("1"), UcdVersions.expandVersion("1"));
  }

  @Test
  public void expandAllVersions() throws Exception {
    UcdVersions ucdVersions = UcdVersions.builder().put("1.2.3", UCD1).put("1.3.5", UCD2).build();
    assertEquals(Arrays.asList("1.2", "1.2.3", "1.3", "1.3.5"), ucdVersions.expandAllVersions());
  }

  @Test
  public void expandAllVersions_withMajor() throws Exception {
    UcdVersions ucdVersions = UcdVersions.builder().put("1.0.3", UCD1).put("1.3.5", UCD2).build();
    assertEquals(
        Arrays.asList("1", "1.0", "1.0.3", "1.3", "1.3.5"), ucdVersions.expandAllVersions());
  }

  @Test
  public void expandAllVersions_unnaturalOrder() {
    UcdVersions ucdVersions =
        UcdVersions.builder().put("1.2.3", UCD1).put("2.4.6", UCD1).put("10.0.0", UCD1).build();
    assertEquals(
        Arrays.asList("1.2", "1.2.3", "2.4", "2.4.6", "10", "10.0", "10.0.0"),
        ucdVersions.expandAllVersions());
  }

  @Test
  public void getClassNameForVersion() throws Exception {
    assertEquals("Unicode_4_2", UcdVersions.getClassNameForVersion("4.2"));
  }

  @Test
  public void getClassNameForVersion_majorOnly() throws Exception {
    assertEquals("Unicode_5", UcdVersions.getClassNameForVersion("5"));
  }
}
