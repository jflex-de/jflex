package jflex.testing.javac;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class PackageUtilTest {

  @Test
  public void getPathForPackage() throws Exception {
    assertThat(PackageUtil.getPathForPackage(getClass().getPackage()))
        .isEqualTo("jflex/testing/javac");
  }
}
