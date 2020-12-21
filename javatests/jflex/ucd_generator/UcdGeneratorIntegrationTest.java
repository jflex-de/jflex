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
import org.junit.Test;

/**
 * Integration test for {@link UcdGenerator}.
 *
 * <p>Checks that the generated {@code Unicode_x_y} has the same data content than the current
 * classes, using diff on the Java AST.
 */
public class UcdGeneratorIntegrationTest {

  @Test
  public void emitUnicodeVersionXY_4_1() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_4_1, outputDir);

    File f = new File(outputDir, "Unicode_4_1.java");
    assertThat(f.exists()).isTrue();

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
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_5_0, outputDir);

    File f = new File(outputDir, "Unicode_5_0.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_5_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }

  @Test
  public void emitUnicodeVersionXY_6_3() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_6_3, outputDir);

    File f = new File(outputDir, "Unicode_6_3.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_6_3.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }

  @Test
  public void emitUnicodeVersionXY_10_0() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_10, outputDir);

    File f = new File(outputDir, "Unicode_10_0.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_10_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }

  private void assertUnicodeProperties(UnicodePropertiesData expected, File src)
      throws IOException {
    ImmutableMap<String, Object> generated = BasicJavaInterpreter.parseJavaClass(src);

    List actualPropertyValues = (List) generated.get("propertyValues");
    assertWithMessage("propertyValues")
        .that(actualPropertyValues)
        .containsAllIn(expected.propertyValues());

    Object actualIntervals = generated.get("intervals");
    assertWithMessage("intervals").that(actualIntervals).isEqualTo(expected.intervals());

    Object actualPropertyValuesAliases = generated.get("propertyValueAliases");
    assertWithMessage("propertyValueAliases")
        .that(actualPropertyValuesAliases)
        .isEqualTo(expected.propertyValueAliases());

    assertWithMessage("maximumCodePoint")
        .that(generated.get("maximumCodePoint"))
        .isEqualTo(expected.maximumCodePoint());

    assertWithMessage("caselessMatchPartitions")
        .that(generated.get("caselessMatchPartitions"))
        .isEqualTo(expected.caselessMatchPartitions());

    assertWithMessage("caselessMatchPartitionSize")
        .that(generated.get("caselessMatchPartitionSize"))
        .isEqualTo(expected.caselessMatchPartitionSize());
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
