package jflex.testing;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Helps to manipulate files independently of the current working directory.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/449">#449</a>
 */
public class TestFileUtils {

  /** Whether the process is in a Bazel <em>runfiles</em> sandbox. */
  public static final boolean BAZEL_RUNFILES =
      "__main__".equals(new File(".").getAbsoluteFile().getParentFile().getName());

  /** Prefix used in Bazel packages to indicate an absolute paths. */
  private static final String BAZEL_ROOT_PREFIX = "//";

  private static final char BAZEL_SEP = '/';

  /**
   * Resolves a file or directory.
   *
   * @param bazelPackage Path of the working directory relative to the root of the git repository.
   * @param path Path of the file to open, relative to the working directory, e.g. {@code
   *     src/main/resources/foo.txt}. Can also be an absolute path in the file system, e.g. {@code
   *     /tmp/foo/bar.log}.
   */
  public static String resolvePath(String bazelPackage, String path) {
    Preconditions.checkNotNull(path, "Path must not be null");
    if (path.charAt(0) == File.separatorChar || path.charAt(0) == BAZEL_SEP) {
      // Absolute path. Why not.
      return path;
    }
    Preconditions.checkNotNull(bazelPackage, "You must provide the bazel package of the test");
    Preconditions.checkArgument(
        bazelPackage.startsWith(BAZEL_ROOT_PREFIX),
        "The bazel package should be absolute, i.e. starts with `%s`, but `$` doesn't",
        BAZEL_ROOT_PREFIX,
        bazelPackage);
    Preconditions.checkArgument(
        bazelPackage.indexOf(':') <= 0,
        "The bazel package is a valid name, but `%s` contain `:`",
        bazelPackage);
    String safeBazelPackage = bazelPackage.endsWith("/") ? bazelPackage : bazelPackage + "/";
    return resolveInternal(safeBazelPackage, path);
  }

  /**
   * Resolves the given path.
   *
   * @param safeBazelPackage Bazel package excluding the leading {@link #BAZEL_ROOT_PREFIX} and with
   *     a trailing "{@code /}".
   * @param safePath Path not starting by "/".
   * @return A PAth with "
   */
  private static String resolveInternal(String safeBazelPackage, String safePath) {
    if (BAZEL_RUNFILES) {
      String osPath =
          safeBazelPackage
              .substring(BAZEL_ROOT_PREFIX.length())
              .replace(BAZEL_SEP, File.separatorChar);
      return osPath + File.separator + safePath;
    } else {
      return safePath;
    }
  }

  public static File open(String bazelPackage, String path) throws FileNotFoundException {
    File file = new File(resolvePath(bazelPackage, path));
    if (!file.exists()) {
      throw new FileNotFoundException(
          String.format("Couldn't open %s in %s: %s", path, bazelPackage, file.getAbsolutePath()));
    }
    return file;
  }

  private TestFileUtils() {}
}
