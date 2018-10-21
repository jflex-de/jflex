/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>,                   *
 *                          Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.anttask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import jflex.Options;
import jflex.Skeleton;
import jflex.Options;
import junit.framework.TestCase;

/**
 * Unit tests for the {@link jflex.anttask.JFlexTask}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
public class JFlexTaskTest extends TestCase {

  private final String DIR_RESOURCES = "src/test/resources";
  private final String FILE_LEXSCAN = "/jflex/LexScan-test.flex";

  private JFlexTask task;
  private PrintWriter out;

  /**
   * Constructor for JFlexTaskTest.
   *
   * @param name test case name
   */
  public JFlexTaskTest(String name) {
    super(name);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    task = new JFlexTask();
    out = new PrintWriter(new ByteArrayOutputStream());
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
    assertEquals(new File(dir, "jflex"), getOptions().outputDirectory());
  }

  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(dir, getOptions().outputDirectory());
  }

  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(new File(DIR_RESOURCES + "/jflex"), getOptions().outputDirectory());
  }

  public void testNomin() {
    assertTrue(getOptions().minimize());
    task.setNomin(true);
    assertFalse(getOptions().minimize());
  }

  public void testSkipMinimization() {
    assertTrue(getOptions().minimize());
    task.setSkipMinimization(true);
    assertFalse(getOptions().minimize());
  }

  public void testNobak() {
    assertTrue(getOptions().backup());
    task.setNobak(true);
    assertFalse(getOptions().backup());
  }

  public void testSkel() throws IOException {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    Skeleton skeleton = new Skeleton(out);
    skeleton.readSkelFile(getOptions().skeleton());
    assertTrue(skeleton.getLine(3).indexOf("java.util.Stack") > 0);
  }

  public void testVerbose() {
    task.setVerbose(false);
    assertFalse(getOptions().verbose());
    task.setVerbose(true);
    assertTrue(getOptions().verbose());
  }

  public void testUnusedWarnings() {
    // Defaults to true, for backward compatibility.
    assertTrue("Defaults to true", getOptions().unusedWarnings());
    task.setUnusedWarning(false);
    assertFalse(getOptions().unusedWarnings());
  }

  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertFalse("Disabled in quiet mode", getOptions().unusedWarnings());
  }

  public void testTime() {
    assertFalse(getOptions().timing());
    task.setTimeStatistics(true);
    assertTrue(getOptions().timing());
    task.setTime(false);
    assertFalse(getOptions().timing());
  }

  public void testDot() {
    assertFalse(getOptions().generateDotFile());
    task.setDot(true);
    assertTrue(getOptions().generateDotFile());
    task.setGenerateDot(false);
    assertFalse(getOptions().generateDotFile());
  }

  public void testDump() {
    assertFalse(getOptions().dump());
    task.setDump(true);
    assertTrue(getOptions().dump());
  }

  public void testJlex() {
    assertFalse(getOptions().strictJlex());
    task.setJLex(true);
    assertTrue(getOptions().strictJlex());
  }

  public void testLegacyDot() {
    assertFalse(getOptions().legacyDot());
    task.setLegacyDot(true);
    assertTrue(getOptions().legacyDot());
  }

  public void testSetEncoding() {
    Charset defaultSet = Charset.defaultCharset();
    String name = "utf-8";
    Charset charset = Charset.forName(name);
    assertEquals(defaultSet, getOptions().encoding());
    task.setEncoding(name);
    assertEquals(charset, getOptions().encoding());
  }

  private Options getOptions() {
    return task.getGeneratorOptions();
  }

  private String[] readLines(File file) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      ArrayList<String> lines = new ArrayList<>();
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
      return lines.toArray(new String[lines.size()]);
    }
  }
}
