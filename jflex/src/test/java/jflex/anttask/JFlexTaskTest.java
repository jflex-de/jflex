/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>,
 *                          Régis Décamps <decamps@users.sf.net>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.anttask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link jflex.anttask.JFlexTask}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class JFlexTaskTest {

  private JFlexTask task;
  private final String DIR_RESOURCES = "src/test/resources";
  private final String FILE_LEXSCAN = "/jflex/LexScan-test.flex";

  @BeforeEach
  public void setUp() {
    task = new JFlexTask();
  }

  @Test
  public void testPackageAndClass() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    assertEquals("jflex", task.getPackage());
    assertEquals("LexScan", task.getClassName());
  }

  @Test
  public void testPackageAndClassDefaults() throws IOException {
    // FIXME
    task.setFile(new File(DIR_RESOURCES + "/jflex/simple.flex"));
    task.findPackageAndClass();
    assertNull(task.getPackage());
    assertEquals("Yylex", task.getClassName());
  }

  @Test
  public void testDestdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("target/test/src");
    task.setDestdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // not default jflex logic, but javac (uses package name)
    assertEquals(new File(dir, "jflex"), task.options().directory());
  }

  @Test
  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(dir, task.options().directory());
  }

  @Test
  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertEquals(new File(DIR_RESOURCES + "/jflex"), task.options().directory());
  }

  @Test
  public void testNomin() {
    assertFalse(task.options().noMinimize());
    task.setNomin(true);
    assertTrue(task.options().noMinimize());
  }

  @Test
  public void testSkipMinimization() {
    assertFalse(task.options().noMinimize());
    task.setSkipMinimization(true);
    assertTrue(task.options().noMinimize());
  }

  @Test
  public void testNobak() {
    assertFalse(task.options().noBackup());
    task.setNobak(true);
    assertTrue(task.options().noBackup());
  }

  @Test
  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertTrue(task.options().skeleton().section(3).indexOf("java.util.Deque") > 0);
  }

  @Test
  public void testVerbose() {
    task.setVerbose(false);
    assertFalse(task.options().verbose());
    task.setVerbose(true);
    assertTrue(task.options().verbose());
  }

  @Test
  public void testUnusedWarning() {
    // Defaults to enabled, for backward compatibility.
    assertFalse(
        task.options().isSuppressed(ErrorMessages.MACRO_UNUSED), "Defaults to not suppressed");
    task.setUnusedWarning(false);
    assertTrue(task.options().isSuppressed(ErrorMessages.MACRO_UNUSED));
  }

  @Test
  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertTrue(task.options().isSuppressed(ErrorMessages.MACRO_UNUSED), "Disabled in quiet mode");
  }

  @Test
  public void testTime() {
    assertFalse(task.options().time());
    task.setTimeStatistics(true);
    assertTrue(task.options().time());
    task.setTime(false);
    assertFalse(task.options().time());
  }

  @Test
  public void testDot() {
    assertFalse(task.options().dot());
    task.setDot(true);
    assertTrue(task.options().dot());
    task.setGenerateDot(false);
    assertFalse(task.options().dot());
  }

  @Test
  public void testDump() {
    assertFalse(task.options().dump());
    task.setDump(true);
    assertTrue(task.options().dump());
  }

  @Test
  public void testJlex() {
    assertFalse(task.options().jlex());
    task.setJLex(true);
    assertTrue(task.options().jlex());
  }

  @Test
  public void testLegacyDot() {
    assertFalse(task.options().legacyDot());
    task.setLegacyDot(true);
    assertTrue(task.options().legacyDot());
  }

  @Test
  public void testSetEncoding() {
    Charset defaultSet = Charset.defaultCharset();
    String name = "utf-8";
    Charset charset = Charset.forName(name);
    assertEquals(task.options().encoding(), defaultSet);
    task.setEncoding(name);
    assertEquals(task.options().encoding(), charset);
  }
}
