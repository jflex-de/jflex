package ucd_generator;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.List;

public class Main {

  static final String BAZEL_PREFIX =
      Main.class.getPackage().getName().replace('.', File.separatorChar);

  /** Args: version=file,file,file version=file,file, */
  public static void main(String[] argv) throws Exception {
    ImmutableMap<String, ImmutableMap<DataFileType, File>> versions = parseArgs(argv);
    UcdGenerator.generate(versions);
  }

  private static ImmutableMap<String, ImmutableMap<DataFileType, File>> parseArgs(String[] argv)
      throws FileNotFoundException {
    ImmutableMap.Builder<String, ImmutableMap<DataFileType, File>> versions =
        ImmutableMap.builder();
    for (String arg : argv) {
      List<String> a = Splitter.on("=").splitToList(arg);
      String version = a.get(0);
      List<String> files = ImmutableList.copyOf(Splitter.on(",").split(a.get(1)));
      ImmutableMap<DataFileType, File> fileMap = findUcdFiles(files);
      System.out.println(Joiner.on('\n').join(fileMap.entrySet()));
      versions.put(version, fileMap);
    }
    return versions.build();
  }

  private static ImmutableMap<DataFileType, File> findUcdFiles(List<String> argv)
      throws FileNotFoundException {
    EnumMap<DataFileType, File> files = new EnumMap<>(DataFileType.class);
    for (String arg : argv) {
      for (DataFileType type : DataFileType.values()) {
        if (arg.contains(type.name())) {
          // File downloaded by Bazel in the external dir
          File externalFile = new File(BAZEL_PREFIX, arg);
          if (externalFile.exists()) {
            throw new FileNotFoundException(externalFile.getAbsolutePath());
          }
          files.put(type, externalFile);
        }
      }
    }
    return Maps.immutableEnumMap(files);
  }

  private Main() {}
}
