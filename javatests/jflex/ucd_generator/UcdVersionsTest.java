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
  public void getClassNameForVersion() throws Exception {
    assertThat(UcdVersions.getClassNameForVersion("4.2")).isEqualTo("Unicode_4_2");
  }

  @Test
  public void getClassNameForVersion_majorOnly() throws Exception {
    assertThat(UcdVersions.getClassNameForVersion("5")).isEqualTo("Unicode_5");
  }
}
