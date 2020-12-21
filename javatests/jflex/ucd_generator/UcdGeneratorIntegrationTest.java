package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import jflex.testing.diff.DiffOutputStream;
import jflex.testing.javaast.BasicJavaInterpreter;
import jflex.ucd_generator.ucd.UcdVersion;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration test for {@link UcdGenerator}.
 *
 * <p>Checks that the generated {@code Unicode_x_y} has the same data content than the current
 * classes, using diff on the Java AST.
 */
public class UcdGeneratorIntegrationTest {

  // TODO(regisd) Earlier versions: 1.1, 2.0, 2.1, 3.0, 3.1, 3.2, 4.0.

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

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_5_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
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
  @Ignore // TODO(regisd) Fix test failure
  // propertyValueAliases  missing the following entries:
  // {
  //   scriptextensions=arab=scriptextensions=arabic,
  //   scriptextensions=armi=scriptextensions=imperialaramaic,
  //   etc.
  // }
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

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_6_3.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
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

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_10_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
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
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(ucdVersion, outputDir);

    File f = new File(outputDir, ucdVersion.version().unicodeClassName() + ".java");
    assertThat(f.exists()).isTrue();
    return f;
  }

  private void assertUnicodeProperties(UnicodePropertiesData expected, File src)
      throws IOException {
    ImmutableMap<String, Object> generated = BasicJavaInterpreter.parseJavaClass(src);

    List<String> actualPropertyValues = (List<String>) generated.get("propertyValues");
    assertWithMessage("propertyValues")
        .that(actualPropertyValues)
        .containsAllIn(expected.propertyValues());

    ImmutableMap<String, String> actualPropertyValuesAliases =
        pairListToMap((List<String>) generated.get("propertyValueAliases"));
    ImmutableMap<String, String> expectedPropertyValueAliases =
        pairListToMap(expected.propertyValueAliases());
    assertWithMessage("propertyValueAliases")
        .that(actualPropertyValuesAliases)
        .isEqualTo(expectedPropertyValueAliases);

    assertWithMessage("maximumCodePoint")
        .that(generated.get("maximumCodePoint"))
        .isEqualTo(expected.maximumCodePoint());

    List<String> actualIntervals = (List<String>) generated.get("intervals");
    assertWithMessage("intervals").that(actualIntervals).containsAllIn(expected.intervals());

    assertWithMessage("caselessMatchPartitions")
        .that(generated.get("caselessMatchPartitions"))
        .isEqualTo(expected.caselessMatchPartitions());

    assertWithMessage("caselessMatchPartitionSize")
        .that(generated.get("caselessMatchPartitionSize"))
        .isEqualTo(expected.caselessMatchPartitionSize());
  }

  /** Converts a List of {@code k1,v1,k2,v2,etc.} to a Map of {@code k1→v1, k2→v2, etc.}. */
  private static <E> ImmutableMap<E, E> pairListToMap(List<E> list) {
    ImmutableMap.Builder<E, E> map = ImmutableMap.builder();
    for (int i = 0; i < list.size(); i += 2) {
      map.put(list.get(i), list.get(i + 1));
    }
    return map.build();
  }

  @AutoValue
  abstract static class UnicodePropertiesData {
    abstract ImmutableList<String> propertyValues();

    abstract ImmutableList<String> intervals();

    abstract ImmutableList<String> propertyValueAliases();

    abstract int maximumCodePoint();

    abstract String caselessMatchPartitions();

    abstract int caselessMatchPartitionSize();

    static UnicodePropertiesData create(
        String[] propertyValues,
        String[] intervals,
        String[] propertyValueAliases,
        int maximumCodePoint,
        String caselessMatchPartitions,
        int caselessMatchPartitionSize) {
      return new AutoValue_UcdGeneratorIntegrationTest_UnicodePropertiesData(
          ImmutableList.copyOf(propertyValues),
          ImmutableList.copyOf(intervals),
          ImmutableList.copyOf(propertyValueAliases),
          maximumCodePoint,
          caselessMatchPartitions,
          caselessMatchPartitionSize);
    }
  }
}
