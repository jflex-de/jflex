package jflex.ucd_generator.scanner;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.HashMultimap;
import com.google.common.io.CharSource;
import java.io.IOException;
import jflex.ucd_generator.TestedVersions;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.NamedCodepointRange;
import org.junit.Before;
import org.junit.Test;

/** Test for {@link DerivedAgeScanner}. */
public class DerivedAgeScannerTest {

  private DerivedAgeScanner derivedAgeScanner;
  private UnicodeData unicodeData;

  @Before
  public void setUp() throws IOException {
    unicodeData = new UnicodeData(TestedVersions.UCD_VERSION_10.version());
    derivedAgeScanner =
        new DerivedAgeScanner(
            CharSource.wrap("unused").openStream(),
            unicodeData,
            /*defaultPropertyName=*/ "defaultPropertyName");
  }

  @Test
  public void clusterCodePointRangesPerVersion() {

    derivedAgeScanner.addInterval(NamedCodepointRange.create("1", 10, 19));
    derivedAgeScanner.addInterval(NamedCodepointRange.create("2", 20, 29));
    derivedAgeScanner.addInterval(NamedCodepointRange.create("1", 100, 101));

    HashMultimap<String, CodepointRange> clusters =
        derivedAgeScanner.clusterCodePointRangesPerVersion();
    assertThat(clusters.keySet()).hasSize(2);
    assertThat(clusters.get("1"))
        .containsExactly(CodepointRange.create(10, 19), CodepointRange.create(100, 101));
    assertThat(clusters.get("2")).containsExactly(CodepointRange.create(20, 29));
  }

  @Test
  public void addPropertyValueIntervals_includeOlderVersions() {
    derivedAgeScanner.addInterval(NamedCodepointRange.create("1", 10, 19));
    derivedAgeScanner.addInterval(NamedCodepointRange.create("2", 20, 42));
    derivedAgeScanner.addInterval(NamedCodepointRange.create("3", 50, 101));
    derivedAgeScanner.addInterval(NamedCodepointRange.create("1", 200, 300));

    derivedAgeScanner.addPropertyValueIntervals();

    assertThat(unicodeData.getPropertyValueIntervals("defaultpropertyname=1"))
        .containsExactly(CodepointRange.create(10, 19), CodepointRange.create(200, 300));
    assertThat(unicodeData.getPropertyValueIntervals("defaultpropertyname=2"))
        .containsExactly(
            CodepointRange.create(20, 42),
            CodepointRange.create(10, 19),
            CodepointRange.create(200, 300));
    assertThat(unicodeData.getPropertyValueIntervals("defaultpropertyname=3"))
        .containsExactly(
            CodepointRange.create(50, 101),
            CodepointRange.create(20, 42),
            CodepointRange.create(10, 19),
            CodepointRange.create(200, 300));
  }
}
