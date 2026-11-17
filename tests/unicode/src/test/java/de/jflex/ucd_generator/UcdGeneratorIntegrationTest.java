/*
 * Copyright (C) 2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.util.JavaStrings;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration test for {@link UcdGenerator}.
 *
 * <p>Checks that the generated {@code Unicode_x_y} has the same data content as the current classes
 * by compiling the generated class and comparing its constants.
 */
public class UcdGeneratorIntegrationTest {

  @TempDir File tempDir;

  private final File testDir = new File("src/test/java/de/jflex/ucd_generator");

  @Test
  public void emitUnicodeVersionXY_1_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_1_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_1_1.propertyValues,
            jflex.core.unicode.data.Unicode_1_1.intervals,
            jflex.core.unicode.data.Unicode_1_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_1_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_1_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_1_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_2_0_14() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_2_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_2_0.propertyValues,
            jflex.core.unicode.data.Unicode_2_0.intervals,
            jflex.core.unicode.data.Unicode_2_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_2_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_2_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_2_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_2_1_9() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_2_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_2_1.propertyValues,
            jflex.core.unicode.data.Unicode_2_1.intervals,
            jflex.core.unicode.data.Unicode_2_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_2_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_2_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_2_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_3_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_3_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_3_0.propertyValues,
            jflex.core.unicode.data.Unicode_3_0.intervals,
            jflex.core.unicode.data.Unicode_3_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_3_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_3_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_3_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_3_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_3_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_3_1.propertyValues,
            jflex.core.unicode.data.Unicode_3_1.intervals,
            jflex.core.unicode.data.Unicode_3_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_3_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_3_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_3_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_3_2() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_3_2);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_3_2.propertyValues,
            jflex.core.unicode.data.Unicode_3_2.intervals,
            jflex.core.unicode.data.Unicode_3_2.propertyValueAliases,
            jflex.core.unicode.data.Unicode_3_2.maximumCodePoint,
            jflex.core.unicode.data.Unicode_3_2.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_3_2.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_4_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_4_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_4_0.propertyValues,
            jflex.core.unicode.data.Unicode_4_0.intervals,
            jflex.core.unicode.data.Unicode_4_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_4_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_4_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_4_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_4_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_4_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_4_1.propertyValues,
            jflex.core.unicode.data.Unicode_4_1.intervals,
            jflex.core.unicode.data.Unicode_4_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_4_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_4_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_4_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_5_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_5_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_5_0.propertyValues,
            jflex.core.unicode.data.Unicode_5_0.intervals,
            jflex.core.unicode.data.Unicode_5_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_5_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_5_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_5_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);

    File goldenFile = new File(testDir, "Unicode_5_0.java.golden");
    try (DiffOutputStream goldenOutputStream =
        new DiffOutputStream(
            Files.newBufferedReader(goldenFile.toPath(), StandardCharsets.UTF_8))) {
      Files.copy(f.toPath(), goldenOutputStream);
    }
  }

  @Test
  public void emitUnicodeVersionXY_5_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_5_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_5_1.propertyValues,
            jflex.core.unicode.data.Unicode_5_1.intervals,
            jflex.core.unicode.data.Unicode_5_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_5_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_5_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_5_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_5_2() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_5_2);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_5_2.propertyValues,
            jflex.core.unicode.data.Unicode_5_2.intervals,
            jflex.core.unicode.data.Unicode_5_2.propertyValueAliases,
            jflex.core.unicode.data.Unicode_5_2.maximumCodePoint,
            jflex.core.unicode.data.Unicode_5_2.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_5_2.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_6_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_6_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_6_0.propertyValues,
            jflex.core.unicode.data.Unicode_6_0.intervals,
            jflex.core.unicode.data.Unicode_6_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_6_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_6_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_6_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_6_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_6_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_6_1.propertyValues,
            jflex.core.unicode.data.Unicode_6_1.intervals,
            jflex.core.unicode.data.Unicode_6_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_6_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_6_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_6_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_6_2() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_6_2);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_6_2.propertyValues,
            jflex.core.unicode.data.Unicode_6_2.intervals,
            jflex.core.unicode.data.Unicode_6_2.propertyValueAliases,
            jflex.core.unicode.data.Unicode_6_2.maximumCodePoint,
            jflex.core.unicode.data.Unicode_6_2.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_6_2.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_6_3() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_6_3);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_6_3.propertyValues,
            jflex.core.unicode.data.Unicode_6_3.intervals,
            jflex.core.unicode.data.Unicode_6_3.propertyValueAliases,
            jflex.core.unicode.data.Unicode_6_3.maximumCodePoint,
            jflex.core.unicode.data.Unicode_6_3.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_6_3.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);

    File goldenFile = new File(testDir, "Unicode_6_3.java.golden");
    try (DiffOutputStream goldenOutputStream =
        new DiffOutputStream(
            Files.newBufferedReader(goldenFile.toPath(), StandardCharsets.UTF_8))) {
      Files.copy(f.toPath(), goldenOutputStream);
    }
  }

  @Test
  public void emitUnicodeVersionXY_7_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_7_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_7_0.propertyValues,
            jflex.core.unicode.data.Unicode_7_0.intervals,
            jflex.core.unicode.data.Unicode_7_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_7_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_7_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_7_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_8_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_8_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_8_0.propertyValues,
            jflex.core.unicode.data.Unicode_8_0.intervals,
            jflex.core.unicode.data.Unicode_8_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_8_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_8_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_8_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_9_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_9_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_9_0.propertyValues,
            jflex.core.unicode.data.Unicode_9_0.intervals,
            jflex.core.unicode.data.Unicode_9_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_9_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_9_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_9_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_10_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_10_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_10_0.propertyValues,
            jflex.core.unicode.data.Unicode_10_0.intervals,
            jflex.core.unicode.data.Unicode_10_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_10_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_10_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_10_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);

    File goldenFile = new File(testDir, "Unicode_10_0.java.golden");
    try (DiffOutputStream goldenOutputStream =
        new DiffOutputStream(
            Files.newBufferedReader(goldenFile.toPath(), StandardCharsets.UTF_8))) {
      Files.copy(f.toPath(), goldenOutputStream);
    }
  }

  @Test
  public void emitUnicodeVersionXY_11_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_11_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_11_0.propertyValues,
            jflex.core.unicode.data.Unicode_11_0.intervals,
            jflex.core.unicode.data.Unicode_11_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_11_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_11_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_11_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_12_0() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_12_0);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_12_0.propertyValues,
            jflex.core.unicode.data.Unicode_12_0.intervals,
            jflex.core.unicode.data.Unicode_12_0.propertyValueAliases,
            jflex.core.unicode.data.Unicode_12_0.maximumCodePoint,
            jflex.core.unicode.data.Unicode_12_0.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_12_0.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  @Test
  public void emitUnicodeVersionXY_12_1() throws Exception {
    File f = generateUnicodeProperties(TestedVersions.UCD_VERSION_12_1);

    UnicodePropertiesData expected =
        UnicodePropertiesData.create(
            jflex.core.unicode.data.Unicode_12_1.propertyValues,
            jflex.core.unicode.data.Unicode_12_1.intervals,
            jflex.core.unicode.data.Unicode_12_1.propertyValueAliases,
            jflex.core.unicode.data.Unicode_12_1.maximumCodePoint,
            jflex.core.unicode.data.Unicode_12_1.caselessMatchPartitions,
            jflex.core.unicode.data.Unicode_12_1.caselessMatchPartitionSize);
    assertUnicodeProperties(expected, f);
  }

  private File generateUnicodeProperties(UcdVersion ucdVersion) throws Exception {
    File outputDir = new File(tempDir, "generated");
    assertTrue(outputDir.mkdir());
    UcdGenerator.emitUnicodeVersionXY(ucdVersion, outputDir);

    File f = new File(outputDir, ucdVersion.version().unicodeClassName() + ".java");
    assertTrue(f.exists());
    return f;
  }

  private void assertUnicodeProperties(UnicodePropertiesData expected, File src) throws Exception {
    UnicodePropertiesData actual = parseUnicodeDataFromSource(src);

    assertEquals(expected.propertyValues(), actual.propertyValues(), "propertyValues");

    Map<String, String> actualPropertyValuesAliases = pairListToMap(actual.propertyValueAliases());
    Map<String, String> expectedPropertyValueAliases =
        pairListToMap(expected.propertyValueAliases());
    assertEquals(expectedPropertyValueAliases, actualPropertyValuesAliases, "propertyValueAliases");

    assertEquals(expected.maximumCodePoint(), actual.maximumCodePoint(), "maximumCodePoint");

    List<String> actualIntervals = escapeUnicodeCharacters(actual.intervals());
    List<String> expectedIntervals = escapeUnicodeCharacters(expected.intervals());
    for (int i = 0; i < min(expectedIntervals.size(), actualIntervals.size()); i++) {
      assertEquals(
          expectedIntervals.get(i),
          actualIntervals.get(i),
          "interval for " + actual.propertyValues().get(i));
    }
    assertEquals(expectedIntervals.size(), actualIntervals.size(), "Number of intervals");

    assertEquals(
        expected.caselessMatchPartitions(),
        actual.caselessMatchPartitions(),
        "caselessMatchPartitions");

    assertEquals(
        expected.caselessMatchPartitionSize(),
        actual.caselessMatchPartitionSize(),
        "caselessMatchPartitionSize");
  }

  private static List<String> escapeUnicodeCharacters(List<String> data) {
    return data.stream().map(JavaStrings::escapedUTF16String).collect(Collectors.toList());
  }

  /** Converts a List of {@code k1,v1,k2,v2,etc.} to a Map of {@code k1→v1, k2→v2, etc.}. */
  private static <E> Map<E, E> pairListToMap(List<E> list) {
    Map<E, E> map = new HashMap<>();
    for (int i = 0; i < list.size(); i += 2) {
      if (map.put(list.get(i), list.get(i + 1)) != null) {
        throw new IllegalArgumentException("Multiple entries with same key: " + list.get(i));
      }
    }
    return map;
  }

  /**
   * Compiles the generated class and reads its constants. The class loader has no parent, because
   * the class path already has the checked-in class of the same name.
   */
  private UnicodePropertiesData parseUnicodeDataFromSource(File src) throws Exception {
    File outputDir = new File(tempDir, "classes");
    assertTrue(outputDir.mkdir());
    JavacUtils.compile(Collections.singletonList(src), outputDir);
    String className = "jflex.core.unicode.data." + src.getName().replaceFirst("\\.java$", "");
    try (URLClassLoader loader =
        new URLClassLoader(new URL[] {outputDir.toURI().toURL()}, /* parent= */ null)) {
      Class<?> generated = loader.loadClass(className);
      return UnicodePropertiesData.create(
          (String[]) generated.getField("propertyValues").get(null),
          (String[]) generated.getField("intervals").get(null),
          (String[]) generated.getField("propertyValueAliases").get(null),
          generated.getField("maximumCodePoint").getInt(null),
          (String) generated.getField("caselessMatchPartitions").get(null),
          generated.getField("caselessMatchPartitionSize").getInt(null));
    }
  }

  /** The constants of a generated Unicode_x_y class. */
  static final class UnicodePropertiesData {
    private final List<String> propertyValues;
    private final List<String> intervals;
    private final List<String> propertyValueAliases;
    private final long maximumCodePoint;
    private final String caselessMatchPartitions;
    private final long caselessMatchPartitionSize;

    private UnicodePropertiesData(
        List<String> propertyValues,
        List<String> intervals,
        List<String> propertyValueAliases,
        long maximumCodePoint,
        String caselessMatchPartitions,
        long caselessMatchPartitionSize) {
      this.propertyValues = Collections.unmodifiableList(new ArrayList<>(propertyValues));
      this.intervals = Collections.unmodifiableList(new ArrayList<>(intervals));
      this.propertyValueAliases =
          Collections.unmodifiableList(new ArrayList<>(propertyValueAliases));
      this.maximumCodePoint = maximumCodePoint;
      this.caselessMatchPartitions = caselessMatchPartitions;
      this.caselessMatchPartitionSize = caselessMatchPartitionSize;
    }

    List<String> propertyValues() {
      return propertyValues;
    }

    List<String> intervals() {
      return intervals;
    }

    List<String> propertyValueAliases() {
      return propertyValueAliases;
    }

    long maximumCodePoint() {
      return maximumCodePoint;
    }

    String caselessMatchPartitions() {
      return caselessMatchPartitions;
    }

    long caselessMatchPartitionSize() {
      return caselessMatchPartitionSize;
    }

    static UnicodePropertiesData create(
        String[] propertyValues,
        String[] intervals,
        String[] propertyValueAliases,
        long maximumCodePoint,
        String caselessMatchPartitions,
        long caselessMatchPartitionSize) {
      return create(
          Arrays.asList(propertyValues),
          Arrays.asList(intervals),
          Arrays.asList(propertyValueAliases),
          maximumCodePoint,
          caselessMatchPartitions,
          caselessMatchPartitionSize);
    }

    static UnicodePropertiesData create(
        List<String> propertyValues,
        List<String> intervals,
        List<String> propertyValueAliases,
        long maximumCodePoint,
        String caselessMatchPartitions,
        long caselessMatchPartitionSize) {
      return new UnicodePropertiesData(
          propertyValues,
          intervals,
          propertyValueAliases,
          maximumCodePoint,
          caselessMatchPartitions,
          caselessMatchPartitionSize);
    }
  }
}
