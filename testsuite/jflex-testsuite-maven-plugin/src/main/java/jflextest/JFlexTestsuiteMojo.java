package jflextest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Runs test cases in the JFlex test suite */
@Mojo(name = "run-test-suite", defaultPhase = LifecyclePhase.TEST)
public class JFlexTestsuiteMojo extends AbstractMojo {

  /** Name of the directory into which the code will be generated. */
  @Parameter(defaultValue = "src/test/cases")
  private String testDirectory = null;

  /** Whether test suite output should be verbose. */
  @Parameter() private boolean verbose;

  /**
   * (Comma-separated list of) name(s) of test case(s) to run.
   *
   * <p>By default, all test cases in src/test/cases/ will be run.
   */
  @Parameter() private String testcases;

  /** Path of the JFlex uber jar under test. */
  @Parameter(
      defaultValue = "${project.parent.basedir}/jflex/target/jflex-full-${project.version}.jar")
  private String jflexUberJarFilename;

  /** */
  public void execute() throws MojoExecutionException, MojoFailureException {
    boolean success = true;
    File jflexUberJar = new File(jflexUberJarFilename);
    try {
      System.setOut(new PrintStream(System.out, true));
      List<File> files = new ArrayList<>();
      getLog().info("JFlex: " + jflexUberJar.getAbsolutePath());
      getLog()
          .info("Testing version: " + PomUtils.getPomVersion("de.jflex", "jflex", jflexUberJar));
      getLog().info("Test directory: " + testDirectory);
      getLog().info("Test case(s): " + (null == testcases ? "All" : testcases));

      if (testcases != null && testcases.length() > 0) {
        for (String testCase : testcases.split("\\s*,\\s*")) {
          File dir = new File(testDirectory, testCase.trim());
          if (!dir.isDirectory()) {
            throw new FileNotFoundException("Test directory is not a directory: " + dir);
          }
          List<File> t = Tester.scan(dir, ".test", false);
          files.addAll(t);
        }
      }

      // if we still didn't find anything, scan the whole test path
      if (files.isEmpty()) files = Tester.scan(new File(testDirectory), ".test", true);

      Tester.verbose = verbose;
      getLog().info("verbose: " + verbose);

      success = Tester.runTests(files, jflexUberJar);

    } catch (Exception e) {
      throw new MojoExecutionException("Failed to execute test suite: " + e.getMessage(), e);
    }
    if (!success) {
      throw new MojoFailureException("Test(s) failed.");
    }
  }
}
