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

  // Seriously – by default generates lower case class names.
  static final String DEFAULT_PARSER_NAME = "parser";
  static final String DEFAULT_SYMBOLS_NAME = "sym";

  /** Name of the directory into which JFlex should generate the parser. */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/cup")
  @SuppressWarnings("WeakerAccess")
  File generatedSourcesDirectory;

  /**
   * Whether to outputs the symbol constant code as an {@ code interface} rather than as a {@code
   * class}.
   */
  @Parameter(defaultValue = "false")
  boolean symbolInterface;

  @Parameter(defaultValue = DEFAULT_SYMBOLS_NAME)
  String symbolsName;

  @Parameter(defaultValue = DEFAULT_PARSER_NAME)
  String parserName;

  private CliCupInvoker cupInvoker;
  private final Logger log;

  /** Whether to force generation of parser and symbols. */
  // private boolean force;
  @SuppressWarnings("WeakerAccess")
  public GenerateMojo() {
    log = new Logger(getLog());
    this.cupInvoker = new CliCupInvoker();
  }

  @VisibleForTesting
  GenerateMojo(CliCupInvoker cupInvoker, Log logger) {
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

    boolean skipGeneration = !isGeneratedCodeOutdated(cupFile, javaPackage);
    if (skipGeneration) {
      // do nothing.
      log.d("Generated code for " + cupFile + " is up to date. Do nothing");
      return;
    }
    try {
      log.d("Generate CUP parser for " + cupFile.getAbsolutePath());
      log.d("CUP output directory: " + generatedSourcesDirectory);
      if (!generatedSourcesDirectory.exists()) {
        if (!generatedSourcesDirectory.mkdirs()) {
          throw new IOException("Could not create " + generatedSourcesDirectory);
        }
      }
      cupInvoker.invoke(
          generatedSourcesDirectory,
          javaPackage,
          parserName,
          symbolsName,
          symbolInterface,
          cupFile.getAbsolutePath());
    } catch (Exception e) {
      throw new MojoExecutionException(
          "CUP failed to generate parser for " + cupFile.getAbsolutePath(), e);
    }
  }

  private boolean isGeneratedCodeOutdated(File cupFile, String javaPackage) {
    File parserFile = JavaUtils.file(generatedSourcesDirectory, javaPackage, parserName);
    File symFile = JavaUtils.file(generatedSourcesDirectory, javaPackage, symbolsName);
    if (parserFile.lastModified() <= cupFile.lastModified()) {
      log.d("Parser file for %s is not actual: %s", cupFile.getName(), parserFile);
      return true;
    }
    if (symFile.lastModified() <= cupFile.lastModified()) {
      log.d("Symbol file for %s is not actual: %s", symFile.getName(), symFile);
      return true;
    }
    return false;
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
