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
    if (versions.versions().isEmpty()) {
      System.out.println("No unicode version specified. Nothing to do.");
      printUsage();
      return;
    }
    UcdGenerator.generate(versions);
  }

  private static void printUsage() {
    System.out.println("bazel run //java/jflex/ucd_generator:Main -- \\");
    System.out.println(
        String.format("  %s1.2.3 /absolute/path/to/ucd_1.2.3_files \\", ARG_VERSION));
    System.out.println(String.format("  %s4.5.6 /absolute/path/to/ucd_4.5.6_files", ARG_VERSION));
    System.out.println("or more simply, just run");
    System.out.println();
    System.out.println("    bazel build //java/jflex/ucd_generator:gen_unicode_properties");
    System.out.println();
    System.out.println("See also java/jflex/ucd_generator/README.md");
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
