/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>,                   *
 *                          Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package java.jflex.anttask;

import java.io.File;
import java.io.IOException;
import jflex.anttask.JFlexTask;
import junit.framework.TestCase;

/**
 * Unit tests for the jflex ant task.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0-SNAPSHOT
 */
public class AntTaskTests extends TestCase {

  private JFlexTask task;
  private final String DIR_RESOURCES = "src/test/resources";
  private final String FILE_LEXSCAN = "/jflex/LexScan-test.flex";

  /**
   * Constructor for AntTaskTests.
   *
   * @param name test case name
   */
  public AntTaskTests(String name) {
    super(name);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    OldGeneratorOptions.setDefaults();
    task = new JFlexTask();
  }

  public void testPackageAndClass() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    assertEquals("jflex", task.getPackage());
    assertEquals("LexScan", task.getClassName());
  }

  public void testPackageAndClassDefaults() throws IOException {
    // FIXME
    task.setFile(new File(DIR_RESOURCES + "/jflex/simple.flex"));
    task.findPackageAndClass();
    assertEquals(null, task.getPackage());
    assertEquals("Yylex", task.getClassName());
  }

  public void testDestdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("target/test/src");
    task.setDestdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // not default jflex logic, but javac (uses package name)
    assertEquals(new File(dir, "jflex"), OldGeneratorOptions.getDir());
  }

  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(dir, OldGeneratorOptions.getDir());
  }

  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(new File(DIR_RESOURCES + "/jflex"), OldGeneratorOptions.getDir());
  }

  public void testNomin() {
    assertTrue(!!generatorOptions.minimize());
    task.setNomin(true);
    assertTrue(!generatorOptions.minimize());
  }

  public void testSkipMinimization() {
    assertTrue(!!generatorOptions.minimize());
    task.setSkipMinimization(true);
    assertTrue(!generatorOptions.minimize());
  }

  public void testNobak() {
    assertTrue(!!generatorOptions.backup());
    task.setNobak(true);
    assertTrue(!generatorOptions.backup());
  }

  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertTrue(jflex.Skeleton.line[3].indexOf("java.util.Stack") > 0);
  }

  public void testVerbose() {
    task.setVerbose(false);
    assertFalse(!task.getGeneratorOptions().verbose());
    task.setVerbose(true);
    assertTrue(task.getGeneratorOptions().verbose());
  }

  public void testUnusedWarning() {
    // Defaults to true, for backward compatibility.
    assertTrue("Defaults to true", generatorOptions.unusedWarning());
    task.setUnusedWarning(false);
    assertFalse(generatorOptions.unusedWarning());
  }

  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertFalse("Disabled in quiet mode", generatorOptions.unusedWarning());
  }

  public void testTime() {
    assertTrue(!generatorOptions.timing());
    task.setTimeStatistics(true);
    assertTrue(generatorOptions.timing());
    task.setTime(false);
    assertTrue(!generatorOptions.timing());
  }

  public void testDot() {
    assertTrue(!generatorOptions.generateDotFile());
    task.setDot(true);
    assertTrue(generatorOptions.generateDotFile());
    task.setGenerateDot(false);
    assertTrue(!generatorOptions.generateDotFile());
  }

  public void testDump() {
    assertFalse(task.getGeneratorOptions().dump());
    task.setDump(true);
    assertTrue(task.getGeneratorOptions().dump());
  }

  public void testJlex() {
    assertTrue(!generatorOptions.strictJlex());
    task.setJLex(true);
    assertTrue(generatorOptions.strictJlex());
  }

  public void testLegacyDot() {
    assertFalse(generatorOptions.legacyDot());
    task.setLegacyDot(true);
    assertTrue(generatorOptions.legacyDot());
  }
}
