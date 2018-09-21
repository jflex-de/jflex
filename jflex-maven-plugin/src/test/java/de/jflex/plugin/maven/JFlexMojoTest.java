/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.Rule;
import org.junit.Test;

/** @author Régis Décamps (decamps@users.sf.net) */
public class JFlexMojoTest {

  @Rule public MojoRule mojoRule = new MojoRule();
  @Rule public TestResources testResources = new TestResources();

  /**
   * Configures an instance of the jflex mojo for the specified test case.
   *
   * @param testCase The name of the test case (i.e. its directory name).
   * @return The configured mojo.
   */
  private JFlexMojo newMojo(String testCase) throws Exception {
    File unitBaseDir = testResources.getBasedir(testCase);
    File testPom = new File(unitBaseDir, "plugin-config.xml");
    JFlexMojo mojo = new JFlexMojo();
    mojoRule.configureMojo(mojo, "jflex-maven-plugin", testPom);
    if (mojoRule.getVariableValueFromObject(mojo, "project") == null) {
      mojoRule.setVariableValueToObject(mojo, "project", new MavenProjectStub());
    }
    return mojo;
  }

  @Test
  public void testTestResources() throws IOException {
    String actualResDir = testResources.getBasedir("single-file-test").getAbsolutePath();
    String expectedSuffix =
        "/jflex-maven-plugin/target/test-projects/JFlexMojoTest_testTestResources_single-file-test";
    assertTrue(
        actualResDir + " ends with " + expectedSuffix, actualResDir.endsWith(expectedSuffix));
  }

  /** Tests configuration with a single input file. */
  @Test
  public void testSingleFile() throws Exception {
    JFlexMojo mojo = newMojo("single-file-test");
    mojo.execute();

    File produced = getExpectedOutputFile(mojo);
    assertTrue("produced file is a file: " + produced, produced.isFile());

    long size = produced.length();
    /*
     * NOTE: The final file size also depends on the employed line
     * terminator. For this reason, the generated output will be longer on a
     * Windows platform ("\r\n") than on a Unix platform ("\n").
     */
    boolean correctSize = (size > 26624) && (size < 36696);
    assertTrue("size of produced file between 26k and 36k. Actual is " + size, correctSize);
  }

  /** Tests configuration with a single input directory. */
  @Test
  public void testSingleDir() throws Exception {
    JFlexMojo mojo = newMojo("single-dir-test");
    mojo.execute();

    File produced = getExpectedOutputFile(mojo);
    assertTrue("produced file is a file: " + produced, produced.isFile());
  }

  /** Tests configuration with a single input directory containing sub directories. */
  @Test
  public void testRecursion() throws Exception {
    JFlexMojo mojo = newMojo("recursion-test");
    mojo.execute();

    File produced = getExpectedOutputFile(mojo);
    assertTrue("produced file is a file: " + produced, produced.isFile());
  }

  /**
   * Gets the expected path to the output file from jflex.
   *
   * @param mojo The jflex mojo under test.
   * @return The expected path to the output file from jflex.
   */
  private File getExpectedOutputFile(JFlexMojo mojo) throws Exception {
    File outDir = (File) mojoRule.getVariableValueFromObject(mojo, "outputDirectory");
    return new File(outDir, "/org/jamwiki/parser/" + "JAMWikiPreProcessor.java");
  }
}
