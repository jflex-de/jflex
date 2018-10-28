package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class UcdVersionsTest {

  @Test
  public void expandVersions() throws Exception {
    assertThat(UcdVersions.expandVersion("1.2.3.4"))
        .containsExactly("1", "1.2", "1.2.3", "1.2.3.4");
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
