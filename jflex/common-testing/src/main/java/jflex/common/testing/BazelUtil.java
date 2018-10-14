package jflex.common.testing;

import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Helper methods on Bazel target names.
 *
 * <p>This is pure string manipulation.
 */
public class BazelUtil {

  private static final String BAZEL_ABSOLUTE_PATH_PREFIX = "//";
  static final char BAZEL_PATH_SEP = '/';
  static final String BAZEL_PATH_SEPARATOR = String.valueOf(BAZEL_PATH_SEP);
  private static final int BAZEL_TARGET_SEPARATOR = ':';

  /**
   * Returns File path of the bazel workspace.
   *
   * @param bazelAbsolutePath An absolute path in the Bazel workspace, prefixed with {@code //}.
   */
  static String getPathInBazelWorkspace(String bazelAbsolutePath) {
    assertWithMessage("A bazel absolute path starts with `%s`", BAZEL_ABSOLUTE_PATH_PREFIX)
        .that(bazelAbsolutePath)
        .startsWith(BAZEL_ABSOLUTE_PATH_PREFIX);
    int packageSepPos = bazelAbsolutePath.lastIndexOf(BAZEL_TARGET_SEPARATOR);
    if (packageSepPos > 0) {
      return bazelAbsolutePath.substring(BAZEL_ABSOLUTE_PATH_PREFIX.length(), packageSepPos);
    } else {
      // The bazelAbsolutePath was just a package name
      return bazelAbsolutePath.substring(BAZEL_ABSOLUTE_PATH_PREFIX.length());
    }
  }

  static String getBazelTargetShortName(String bazelPath) {
    int packageSepPos = bazelPath.lastIndexOf(BAZEL_TARGET_SEPARATOR);
    if (packageSepPos > 0) {
      return bazelPath.substring(packageSepPos + 1);
    } else {
      // Used short form, where the contention is
      // //foo/bar -> //foo/bar:bar
      return bazelPath.substring(bazelPath.lastIndexOf(BAZEL_PATH_SEP) + 1);
    }
  }
}
