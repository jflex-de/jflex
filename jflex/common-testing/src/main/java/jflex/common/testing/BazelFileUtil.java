package jflex.common.testing;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Adapts a Bazel target to any build system. */
public class BazelFileUtil {

  private static final String BAZEL_MAIN_DIR_NAME = "__main__";
  public static final String CWD = ".";

  /**
   * Opens the given file.
   *
   * <p>This method works around a build-tool difficulty:
   *
   * <ul>
   *   <li>Maven uses the directory that contains {@code pom.xml} as a working directory, e.g.
   *       {@code examples/simple}
   *   <li>ant uses the directory that contains {@code build.xml} as a working directory, e.g.
   *       {@code examples/simple}
   *   <li>bazel uses the sanboxed <em>runfiles</em> directory, i.e. the path relative to the
   *       directory that contains {@code WORKSPACE}, e.g. {@code jflex/examples/simple}.
   * </ul>
   *
   * <p>Test author should use the Bazel convention, and prefix the path with {@code //} to make it
   * clear. This method will find the path for a Maven or ant project.
   *
   * @param bazelAbsolutePath An absolute path in the Bazel workspace, prefixed with {@code //}.
   * @return the file path relative to the current workspace (for the current build system).
   */
  public static File openFile(String bazelAbsolutePath) throws IOException {
    File file = findBazelAbsolutePath(bazelAbsolutePath);
    if (!file.isFile()) {
      throw new FileNotFoundException(file.getPath());
    }
    return file;
  }

  private static File findBazelAbsolutePath(String absoluteBazelPath) throws IOException {
    File pwd = new File(CWD).getCanonicalFile();
    assertThat(pwd.isDirectory()).isTrue();

    String bazelPath = BazelUtil.getPathInBazelWorkspace(absoluteBazelPath);
    String fileName = BazelUtil.getBazelTargetShortName(absoluteBazelPath);
    if (Objects.equal(pwd.getName(), BAZEL_MAIN_DIR_NAME)) {
      return new File(bazelPath, fileName);
    } else {
      // The test is running in Maven. Adapt the path.
      return new File(findPathInMaven(pwd, bazelPath), fileName);
    }
  }

  private static String findPathInMaven(File workingDirectory, String bazelPath) {
    // TODO Handle the case where the directory is not in the workspace
    String cwdPathInBazel = checkNotNull(findCwdDirInBazel(workingDirectory, bazelPath));
    return bazelPath.substring(cwdPathInBazel.length() + 1);
  }

  private static String findCwdDirInBazel(File workingDirectory, String bazelPath) {
    String absoluteWorkingDirectory = workingDirectory.getAbsolutePath();
    ImmutableList<String> bazelDirPath =
        ImmutableList.copyOf(bazelPath.split(BazelUtil.BAZEL_PATH_SEPARATOR));
    for (int i = 0; i < bazelDirPath.size(); i++) {
      // Candidate directory of the current dir relative to WORKSPACE
      String candidateCwdPath = getSubPath(bazelDirPath, i);
      if (absoluteWorkingDirectory.endsWith(candidateCwdPath)) {
        return candidateCwdPath;
      }
    }
    return null;
  }

  private static String getSubPath(ImmutableList<String> parts, int count) {
    return Joiner.on(File.separator).join(parts.subList(0, parts.size() - count));
  }

  private BazelFileUtil() {}
}
