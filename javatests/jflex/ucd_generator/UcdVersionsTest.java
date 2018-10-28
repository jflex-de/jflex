package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class UcdVersionsTest {

  @Test
  public void getMajorVersion() throws Exception {
    assertThat(UcdVersions.getMajorVersion("1.2.3.4")).isEqualTo("1");
  }

  @Test
  public void getMajorVersion_majorOnly() throws Exception {
    assertThat(UcdVersions.getMajorVersion("1")).isEqualTo("1");
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
