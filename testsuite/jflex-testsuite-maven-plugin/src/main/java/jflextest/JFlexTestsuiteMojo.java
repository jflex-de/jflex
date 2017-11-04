package jflextest;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;


/**
 * Runs test cases in the JFlex test suite
 * 
 * @goal run-test-suite
 * @phase test
 *
 */
public class JFlexTestsuiteMojo extends AbstractMojo {

  /**
   * Name of the directory into which the code will be generated.
   *
   * @parameter default-value="src/test/cases"
   */
  private String testDirectory = null;

  /**
   * Whether test suite output should be verbose.
   * 
   * @parameter default-value="false"
   */
  private boolean verbose;

	/**
	 * (Comma-separated list of) name(s) of test case(s) to run.
   * 
	 * By default, all test cases in src/test/cases/ will be run.
	 * 
	 * @parameter
	 */
	private String testcases;

  /**
   * JFlex test version
   * 
   * @parameter 
   */
  private String jflexTestVersion;
  
  
  /**
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    boolean success = true;
    try {
      System.setOut(new PrintStream(System.out,true));
      List<File> files = new ArrayList<File>();
      getLog().info("JFlexTest Version: " + Main.version);
      getLog().info("Testing version: " + Exec.getJFlexVersion());    
      getLog().info("Test directory: " + testDirectory);
      getLog().info("Test case(s): " 
                    + (null == testcases ? "All" : testcases));

      if (testcases != null && testcases.length() > 0) {
        for (String testCase : testcases.split("\\s*,\\s*")) {
          File dir = new File(testDirectory, testCase.trim());
          if ( ! dir.isDirectory()) {
            throw new MojoFailureException(dir + " - test path not found");
          }
          List<File> t = Main.scan(dir, ".test", false);
          files.addAll(t);
        }
      }

      // if we still didn't find anything, scan the whole test path
      if (files.isEmpty()) 
        files = Main.scan(new File(testDirectory), ".test", true);
      
      Main.verbose = verbose;
      Main.jflexTestVersion = jflexTestVersion;
      getLog().info("verbose: " + verbose);
      success = Main.runTests(files);
      
    } catch (Exception e) {
      throw new MojoExecutionException("Exception", e);
    }
    if ( ! success) {
      throw new MojoFailureException("Test(s) failed.");
    }
  }
}
