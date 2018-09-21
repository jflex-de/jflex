/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>,                   *
 *                          Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import jflex.anttask.JFlexTask;
import junit.framework.TestCase;

/**
 * Unit tests for the jflex ant task.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
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
    Options.setDefaults();
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
    assertEquals(new File(dir, "jflex"), Options.getDir());
  }

  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(dir, Options.getDir());
  }

  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(new File(DIR_RESOURCES + "/jflex"), Options.getDir());
  }

  public void testNomin() {
    assertTrue(!Options.no_minimize);
    task.setNomin(true);
    assertTrue(Options.no_minimize);
  }

  public void testSkipMinimization() {
    assertTrue(!Options.no_minimize);
    task.setSkipMinimization(true);
    assertTrue(Options.no_minimize);
  }

  public void testNobak() {
    assertTrue(!Options.no_backup);
    task.setNobak(true);
    assertTrue(Options.no_backup);
  }

  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertTrue(jflex.Skeleton.line[3].indexOf("java.util.Stack") > 0);
  }

  public void testVerbose() {
    task.setVerbose(false);
    assertTrue(!Options.verbose);
    task.setVerbose(true);
    assertTrue(Options.verbose);
  }

  public void testUnusedWarning() {
    // Defaults to true, for backward compatibility.
    assertTrue("Defaults to true", Options.unused_warning);
    task.setUnusedWarning(false);
    assertFalse(Options.unused_warning);
  }

  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertFalse("Disabled in quiet mode", Options.unused_warning);
  }

  public void testTime() {
    assertTrue(!Options.time);
    task.setTimeStatistics(true);
    assertTrue(Options.time);
    task.setTime(false);
    assertTrue(!Options.time);
  }

  public void testDot() {
    assertTrue(!Options.dot);
    task.setDot(true);
    assertTrue(Options.dot);
    task.setGenerateDot(false);
    assertTrue(!Options.dot);
  }

  public void testDump() {
    assertTrue(!Options.dump);
    task.setDump(true);
    assertTrue(Options.dump);
  }

  public void testJlex() {
    assertTrue(!Options.jlex);
    task.setJLex(true);
    assertTrue(Options.jlex);
  }

  public void testLegacyDot() {
    assertFalse(Options.legacy_dot);
    task.setLegacyDot(true);
    assertTrue(Options.legacy_dot);
  }

  public void testSetEncoding() {
    Charset defaultSet = Charset.defaultCharset();
    String name = "utf-8";
    Charset charset = Charset.forName(name);
    assertEquals(Options.encoding, defaultSet);
    task.setEncoding(name);
    assertEquals(Options.encoding, charset);
  }
}
