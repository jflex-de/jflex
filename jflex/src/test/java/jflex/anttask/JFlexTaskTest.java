/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>,
 *                          Régis Décamps <decamps@users.sf.net>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.anttask;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import jflex.l10n.ErrorMessages;
import org.junit.Before;
import org.junit.Test;

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

  @Before
  public void setUp() {
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
    assertThat(task.options().directory()).isEqualTo(new File(dir, "jflex"));
  }

  @Test
  public void testOutdir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    File dir = new File("src");
    task.setOutdir(dir);
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertThat(task.options().directory()).isEqualTo(dir);
  }

  @Test
  public void testDefaultDir() throws IOException {
    task.setFile(new File(DIR_RESOURCES + FILE_LEXSCAN));
    task.findPackageAndClass();
    task.normalizeOutdir();
    // this should be default jflex logic
    assertThat(task.options().directory()).isEqualTo(new File(DIR_RESOURCES + "/jflex"));
  }

  @Test
  public void testNomin() {
    assertThat(!task.options().noMinimize()).isTrue();
    task.setNomin(true);
    assertThat(task.options().noMinimize()).isTrue();
  }

  @Test
  public void testSkipMinimization() {
    assertThat(!task.options().noMinimize()).isTrue();
    task.setSkipMinimization(true);
    assertThat(task.options().noMinimize()).isTrue();
  }

  @Test
  public void testNobak() {
    assertThat(!task.options().noBackup()).isTrue();
    task.setNobak(true);
    assertThat(task.options().noBackup()).isTrue();
  }

  @Test
  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertThat(task.options().skeleton().section(3).indexOf("java.util.Deque") > 0).isTrue();
  }

  @Test
  public void testVerbose() {
    task.setVerbose(false);
    assertThat(!task.options().verbose()).isTrue();
    task.setVerbose(true);
    assertThat(task.options().verbose()).isTrue();
  }

  @Test
  public void testUnusedWarning() {
    // Defaults to enabled, for backward compatibility.
    assertWithMessage("Defaults to not suppressed")
        .that(task.options().isSuppressed(ErrorMessages.MACRO_UNUSED))
        .isFalse();
    task.setUnusedWarning(false);
    assertThat(task.options().isSuppressed(ErrorMessages.MACRO_UNUSED)).isTrue();
  }

  @Test
  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertWithMessage("Disabled in quiet mode")
        .that(task.options().isSuppressed(ErrorMessages.MACRO_UNUSED))
        .isTrue();
  }

  @Test
  public void testTime() {
    assertThat(!task.options().time()).isTrue();
    task.setTimeStatistics(true);
    assertThat(task.options().time()).isTrue();
    task.setTime(false);
    assertThat(!task.options().time()).isTrue();
  }

  @Test
  public void testDot() {
    assertThat(!task.options().dot()).isTrue();
    task.setDot(true);
    assertThat(task.options().dot()).isTrue();
    task.setGenerateDot(false);
    assertThat(!task.options().dot()).isTrue();
  }

  @Test
  public void testDump() {
    assertThat(!task.options().dump()).isTrue();
    task.setDump(true);
    assertThat(task.options().dump()).isTrue();
  }

  @Test
  public void testJlex() {
    assertThat(!task.options().jlex()).isTrue();
    task.setJLex(true);
    assertThat(task.options().jlex()).isTrue();
  }

  @Test
  public void testLegacyDot() {
    assertThat(task.options().legacyDot()).isFalse();
    task.setLegacyDot(true);
    assertThat(task.options().legacyDot()).isTrue();
  }

  @Test
  public void testSetEncoding() {
    Charset defaultSet = Charset.defaultCharset();
    String name = "utf-8";
    Charset charset = Charset.forName(name);
    assertThat(defaultSet).isEqualTo(task.options().encoding());
    task.setEncoding(name);
    assertThat(charset).isEqualTo(task.options().encoding());
  }
}
