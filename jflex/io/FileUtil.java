package jflex.io;

import java.io.File;
import java.io.IOException;

public class FileUtil {

  /** Returns the path of {@code file} relative to {@code rootDirectory}. */
  public static String getRelativePath(File rootDirectory, File file) {
    try {
      String rootDir = rootDirectory.getCanonicalPath() + File.separator;
      String f = file.getCanonicalPath();
      if (f.startsWith(rootDir)) {
        return f.substring(rootDir.length());
      }
    } catch (IOException e) {
      // fall back to file.getPath()
    }
    return file.getPath();
  }

  private FileUtil() {} // utility class
}
