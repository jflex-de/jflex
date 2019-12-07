/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>,                   *
 *                          Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.anttask;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import jflex.core.Options;
import jflex.core.Skeleton;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link jflex.anttask.JFlexTask}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class JFlexTaskTest {

  private JFlexTask task;
  private final String DIR_RESOURCES = "src/test/resources";
  private final String FILE_LEXSCAN = "/jflex/LexScan-test.flex";

  @Before
  protected void setUp() throws Exception {
    Options.setDefaults();
    task = new JFlexTask();
  }

  @Test
  public void testPackageAndClass() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    assertThat(task.getPackage()).isEqualTo("jflex");
    assertThat(task.getClassName()).isEqualTo("LexScan");
  }

  @Test
  public void testPackageAndClassDefaults() throws IOException {
    // FIXME
    task.setFile(new File(DIR_RESOURCES + "/jflex/simple.flex"));
    task.findPackageAndClass();
    assertThat(task.getPackage()).isEqualTo(null);
    assertThat(task.getClassName()).isEqualTo("Yylex");
  }

  @Test
  public void testDestdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("target/test/src");
    task.setDestdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // not default jflex logic, but javac (uses package name)
    assertThat(Options.getDir()).isEqualTo(new File(dir, "jflex"));
  }

  @Test
  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertThat(Options.getDir()).isEqualTo(dir);
  }

  @Test
  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertThat(Options.getDir()).isEqualTo(new File(DIR_RESOURCES + "/jflex"));
  }

  @Test
  public void testNomin() {
    assertTrue(!Options.no_minimize);
    task.setNomin(true);
    assertTrue(Options.no_minimize);
  }

  @Test
  public void testSkipMinimization() {
    assertTrue(!Options.no_minimize);
    task.setSkipMinimization(true);
    assertTrue(Options.no_minimize);
  }

  @Test
  public void testNobak() {
    assertTrue(!Options.no_backup);
    task.setNobak(true);
    assertTrue(Options.no_backup);
  }

  @Test
  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertTrue(Skeleton.line[3].indexOf("java.util.Stack") > 0);
  }

  @Test
  public void testVerbose() {
    task.setVerbose(false);
    assertTrue(!Options.verbose);
    task.setVerbose(true);
    assertTrue(Options.verbose);
  }

  @Test
  public void testUnusedWarning() {
    // Defaults to true, for backward compatibility.
    assertTrue("Defaults to true", Options.unused_warning);
    task.setUnusedWarning(false);
    assertFalse(Options.unused_warning);
  }

  @Test
  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertFalse("Disabled in quiet mode", Options.unused_warning);
  }

  @Test
  public void testTime() {
    assertTrue(!Options.time);
    task.setTimeStatistics(true);
    assertTrue(Options.time);
    task.setTime(false);
    assertTrue(!Options.time);
  }

  @Test
  public void testDot() {
    assertTrue(!Options.dot);
    task.setDot(true);
    assertTrue(Options.dot);
    task.setGenerateDot(false);
    assertTrue(!Options.dot);
  }

  @Test
  public void testDump() {
    assertTrue(!Options.dump);
    task.setDump(true);
    assertTrue(Options.dump);
  }

  @Test
  public void testJlex() {
    assertTrue(!Options.jlex);
    task.setJLex(true);
    assertTrue(Options.jlex);
  }

  @Test
  public void testLegacyDot() {
    assertFalse(Options.legacy_dot);
    task.setLegacyDot(true);
    assertTrue(Options.legacy_dot);
  }

  @Test
  public void testSetEncoding() {
    Charset defaultSet = Charset.defaultCharset();
    String name = "utf-8";
    Charset charset = Charset.forName(name);
    assertThat(defaultSet).isEqualTo(Options.encoding);
    task.setEncoding(name);
    assertThat(charset).isEqualTo(Options.encoding);
  }
}
