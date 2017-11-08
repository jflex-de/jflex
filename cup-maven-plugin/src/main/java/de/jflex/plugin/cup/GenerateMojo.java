package de.jflex.plugin.cup;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Creates a Java parser from CUP definition, using CUP. */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = false)
public class GenerateMojo extends AbstractMojo {

  /** Constant {@code src/main/cup}. */
  private static final String DEFAULT_SRC_DIRECTORY = "src/main/cup";

  /** In a CUP definition, the Java package is introduce by the {@code package} keyword. */
  private static final String PACKAGE_DEFINITION = "package";

  private static final String DEFAULT_JAVA_PACKAGE = "";
  /** Constant {@code .java}. */
  private static final String JAVA_FILE_EXT = ".java";

  static final String DEFAULT_PARSER_NAME = "Parser";

  /** Name of the directory into which JFlex should generate the parser. */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/jflex")
  @SuppressWarnings("WeakerAccess")
  File generatedSourcesDirectory;

  /**
   * Whether to outputs the symbol constant code as an {@ code interface} rather than as a {@code
   * class}.
   */
  @Parameter(defaultValue = "false")
  boolean symbolInterface;

  @Parameter(defaultValue = DEFAULT_PARSER_NAME)
  String parserName;

  private CupInvoker cupInvoker;
  private final Logger log;

  /** Whether to force generation of parser and symbols. */
  private boolean force;

  @SuppressWarnings("WeakerAccess")
  public GenerateMojo() {
    log = new Logger(getLog());
    this.cupInvoker = new CliCupInvoker();
  }

  @VisibleForTesting
  GenerateMojo(CupInvoker cupInvoker, Log logger) {
    log = new Logger(logger);
    this.cupInvoker = cupInvoker;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    File[] srcs = getSources();
    for (File f : srcs) {
      try {
        generateParser(f);
      } catch (IOException e) {
        throw new MojoExecutionException("Could not generate parser for " + f.getPath(), e);
      }
    }
  }

  /** @param cupFile CUP definition file */
  @VisibleForTesting
  void generateParser(File cupFile) throws IOException, MojoExecutionException {
    String javaPackage = findJavaPackage(cupFile);
    String packageFilename = javaPackage.replace('.', File.separatorChar);

    File outputDirectory = new File(generatedSourcesDirectory, packageFilename);
    File parserFile = new File(outputDirectory, parserName + JAVA_FILE_EXT);
    File symFile = new File(outputDirectory, "sym" + JAVA_FILE_EXT);

    generateParser(javaPackage, cupFile, parserName, parserFile, symFile, symbolInterface);
  }

  private void generateParser(
      String javaPackage,
      File cupFile,
      String parserName,
      File parserFile,
      File symFile,
      boolean symbolInterface)
      throws MojoExecutionException {
    if (!force && parserFile.lastModified() <= cupFile.lastModified()) {
      log.d("Parser file %s is not actual", parserFile);
      force = true;
    }
    if (!force && symFile.lastModified() <= cupFile.lastModified()) {
      log.d("Symbol file %s is not actual", symFile);
      force = true;
    }
    try {
      cupInvoker.invoke(
          javaPackage,
          parserFile.getAbsolutePath(),
          symFile.getAbsolutePath(),
          cupFile.getAbsolutePath());
    } catch (Exception e) {
      throw new MojoExecutionException(
          "CUP failed to generate parser for " + cupFile.getAbsolutePath(), e);
    }
  }

  private String findJavaPackage(File cupFile) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(cupFile));
    while (br.ready()) {
      String line = br.readLine();
      Optional<String> optJavaPackage = optionalJavaPackage(line);
      if (optJavaPackage.isPresent()) {
        return optJavaPackage.get();
      }
    }
    return DEFAULT_JAVA_PACKAGE;
  }

  @VisibleForTesting
  Optional<String> optionalJavaPackage(String line) {
    if (line.startsWith(PACKAGE_DEFINITION)) {
      int endOfLine = line.indexOf(";");
      if (endOfLine > -1) {
        String javaPackage = line.substring(PACKAGE_DEFINITION.length(), endOfLine);
        javaPackage = javaPackage.trim();
        return Optional.of(javaPackage);
      }
    }
    return Optional.absent();
  }

  /**
   * Returns the cup source files.
   *
   * @return the files in the {@link #DEFAULT_SRC_DIRECTORY} directory.
   */
  private File[] getSources() throws MojoFailureException {
    File defaultDir = new File(DEFAULT_SRC_DIRECTORY);
    if (!defaultDir.isDirectory()) {
      throw new MojoFailureException(
          "Expected " + defaultDir.getAbsolutePath() + " to be a directory");
    }
    return defaultDir.listFiles();
  }
}
