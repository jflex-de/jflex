package jflex.common.testing;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestResourceFileUtil {

  /**
   * Opens the given file.
   *
   * <p>This method also works around a build difficulty:
   *
   * <ul>
   *   <li>Maven uses the directory that contains {@code pom.xml} as a working directory, i.e.
   *       {@code examples/simple}
   *   <li>ant uses the directory that contains {@code build.xml} as a working directory, i.e.
   *       {@code examples/simple}
   *   <li>bazel uses the directory that contains {@code WORKSPACE} as a working directory, i.e.
   *       {@code __main__} in <em>runfiles</em>.
   * </ul>
   */
  public static File openFile(String pathName) throws IOException {
    String path = pathName;
    File pwd = new File(".").getCanonicalFile();
    assertThat(pwd.isDirectory()).isTrue();
    if (Objects.equal(pwd.getName(), "__main__")) {
      path = "jflex/examples/simple/" + path;
    }
    File file = new File(path);
    if (!file.isFile()) {
      throw new FileNotFoundException(path);
    }
    return file;
  }

  private TestResourceFileUtil() {}
}
