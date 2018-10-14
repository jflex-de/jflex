package jflex.common.testing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class BazelUtilTest {

  @Test
  public void getPathInBazelWorkspace_package() {
    assertThat(BazelUtil.getPathInBazelWorkspace("//foo/bar")).isEqualTo("foo/bar");
  }

  @Test
  public void getPathInBazelWorkspace_target() {
    assertThat(BazelUtil.getPathInBazelWorkspace("//foo/bar:content.txt")).isEqualTo("foo/bar");
  }

  @Test
  public void getBazelTargetShortName_packageConvention() throws Exception {
    assertThat(BazelUtil.getBazelTargetShortName("//foo/bar")).isEqualTo("bar");
  }

  @Test
  public void getBazelTargetShortName() throws Exception {
    assertThat(BazelUtil.getBazelTargetShortName("//foo/bar:content.txt")).isEqualTo("content.txt");
  }
}
