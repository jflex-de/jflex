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
import jflex.core.OptionUtils;
import jflex.l10n.ErrorMessages;
import jflex.option.Options;
import jflex.skeleton.Skeleton;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link jflex.anttask.JFlexTask}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class JFlexTaskTest {

  private JFlexTask task;
  private final String DIR_RESOURCES = "src/test/resources";
  private final String FILE_LEXSCAN = "/jflex/LexScan-test.flex";

  @Before
  public void setUp() {
    OptionUtils.setDefaultOptions();
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
    assertThat(!Options.no_minimize).isTrue();
    task.setNomin(true);
    assertThat(Options.no_minimize).isTrue();
  }

  @Test
  public void testSkipMinimization() {
    assertThat(!Options.no_minimize).isTrue();
    task.setSkipMinimization(true);
    assertThat(Options.no_minimize).isTrue();
  }

  @Test
  public void testNobak() {
    assertThat(!Options.no_backup).isTrue();
    task.setNobak(true);
    assertThat(Options.no_backup).isTrue();
  }

  @Test
  public void testSkel() {
    task.setVerbose(false); // avoid to java console pop up
    task.setSkeleton(new File("src/main/jflex/skeleton.nested"));
    assertThat(Skeleton.line[3].indexOf("java.util.Deque") > 0).isTrue();
  }

  @Test
  public void testVerbose() {
    task.setVerbose(false);
    assertThat(!Options.verbose).isTrue();
    task.setVerbose(true);
    assertThat(Options.verbose).isTrue();
  }

  @Test
  public void testUnusedWarning() {
    // Defaults to enabled, for backward compatibility.
    assertWithMessage("Defaults to not suppressed")
        .that(Options.isSuppressed(ErrorMessages.MACRO_UNUSED))
        .isFalse();
    task.setUnusedWarning(false);
    assertThat(Options.isSuppressed(ErrorMessages.MACRO_UNUSED)).isTrue();
  }

  @Test
  public void testUnusedWarning_Verbose() {
    task.setVerbose(false);
    assertWithMessage("Disabled in quiet mode")
        .that(Options.isSuppressed(ErrorMessages.MACRO_UNUSED))
        .isTrue();
  }

  @Test
  public void testTime() {
    assertThat(!Options.time).isTrue();
    task.setTimeStatistics(true);
    assertThat(Options.time).isTrue();
    task.setTime(false);
    assertThat(!Options.time).isTrue();
  }

  @Test
  public void testDot() {
    assertThat(!Options.dot).isTrue();
    task.setDot(true);
    assertThat(Options.dot).isTrue();
    task.setGenerateDot(false);
    assertThat(!Options.dot).isTrue();
  }

  @Test
  public void testDump() {
    assertThat(!Options.dump).isTrue();
    task.setDump(true);
    assertThat(Options.dump).isTrue();
  }

  @Test
  public void testJlex() {
    assertThat(!Options.jlex).isTrue();
    task.setJLex(true);
    assertThat(Options.jlex).isTrue();
  }

  @Test
  public void testLegacyDot() {
    assertThat(Options.legacy_dot).isFalse();
    task.setLegacyDot(true);
    assertThat(Options.legacy_dot).isTrue();
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
