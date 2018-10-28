package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class UcdVersionsTest {

  @Test
  public void getClassNameForVersion() throws Exception {
    assertThat(UcdVersions.getClassNameForVersion("4.2")).isEqualTo("Unicode_4_2");
  }
}
