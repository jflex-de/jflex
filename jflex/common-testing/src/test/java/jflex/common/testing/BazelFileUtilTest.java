package jflex.common.testing;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import org.junit.Test;

public class BazelFileUtilTest {

  @Test
  public void openFile_sameDirAsTest() throws Exception {
    File f =
        BazelFileUtil.openFile("//jflex/common-testing/src/test:java/jflex/common/testing/a.txt");
    assertThat(f.isFile()).isTrue();
  }

  @Test
  public void openFile_withinProject() throws Exception {
    File f = BazelFileUtil.openFile("//jflex/common-testing/src/test:data/b.txt");
    assertThat(f.isFile()).isTrue();
  }
}
