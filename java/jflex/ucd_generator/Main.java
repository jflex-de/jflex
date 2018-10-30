package jflex.ucd_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  private static final String BAZEL_PREFIX =
      Main.class.getPackage().getName().replace('.', File.separatorChar);
  private static final String ARG_VERSION = "--version=";

  /** Args: {@code --version=X file file file --version=Y file file} */
  public static void main(String[] argv) throws Exception {
    UcdVersions versions = parseArgs(argv);
    UcdGenerator.generate(versions);
  }

  private static UcdVersions parseArgs(String[] argv) throws FileNotFoundException {
    UcdVersions.Builder versions = UcdVersions.builder();
    ArrayList<String> files = new ArrayList<>();
    for (int i = argv.length - 1; i >= 0; i--) {
      String arg = argv[i];
      if (arg.startsWith(ARG_VERSION)) {
        String version = arg.substring(ARG_VERSION.length());
        versions.put(version, findUcdFiles(files));
        files.clear();
      } else {
        files.add(arg);
      }
    }
    return versions.build();
  }

  private static UcdVersion.Builder findUcdFiles(List<String> argv) throws FileNotFoundException {
    UcdVersion.Builder version = UcdVersion.builder();
    for (String arg : argv) {
      for (UcdFileType type : UcdFileType.values()) {
        if (arg.contains(type.name())) {
          // File downloaded by Bazel in the external dir
          File externalFile = new File(BAZEL_PREFIX, arg);
          if (externalFile.exists()) {
            throw new FileNotFoundException(externalFile.getAbsolutePath());
          }
          version.putFile(type, externalFile);
        }
      }
    }
    return version;
  }

  private Main() {}
}
