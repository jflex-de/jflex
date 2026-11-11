/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Tests for {@link Options}. */
public class OptionsTest {

  @Rule public TemporaryFolder tmp = new TemporaryFolder();

  private static void assertAllWarningsEnabled(String msg, Options options) {
    for (ErrorMessages warning : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(warning)) {
        assertFalse(warning + " " + msg, options.isSuppressed(warning));
      }
    }
  }

  @Test
  public void defaults_enableAllWarnings() {
    assertAllWarningsEnabled("suppressed by default", new Options());
  }

  @Test
  public void reset_enablesSuppressedWarnings() {
    Options options = new Options();
    options.suppressAllWarnings();

    options.reset();

    assertAllWarningsEnabled("suppressed after reset()", options);
  }

  @Test
  public void reset_enablesSingleSuppressedWarning() {
    Options options = new Options();
    options.suppressWarning("never-match");
    assertTrue(options.isSuppressed(ErrorMessages.NEVER_MATCH));

    options.reset();

    assertFalse(
        "NEVER_MATCH suppressed after reset()", options.isSuppressed(ErrorMessages.NEVER_MATCH));
  }

  @Test
  public void copy_isIndependent() {
    Options options = new Options();
    options.suppressWarning("never-match");
    options.setVerbose(false);

    Options copy = options.copy();
    assertTrue(copy.isSuppressed(ErrorMessages.NEVER_MATCH));
    assertFalse(copy.verbose());

    copy.enable(ErrorMessages.NEVER_MATCH);
    copy.setVerbose(true);
    assertTrue("original changed by copy", options.isSuppressed(ErrorMessages.NEVER_MATCH));
    assertFalse("original changed by copy", options.verbose());
  }

  /** Check errors-via-exception reporting in Options. */
  @Test
  public void suppressWarning_unknown_throwsWithMessage() {
    Options options = new Options();
    try {
      options.suppressWarning("no-such-warning");
      fail("expected GeneratorException");
    } catch (GeneratorException e) {
      assertTrue(e.getMessage(), e.hasErrorMessage());
      assertTrue(e.getMessage(), e.getMessage().contains("\"no-such-warning\" unknown"));
    }
  }

  @Test
  public void setEncoding_unsupported_throwsWithMessage() {
    Options options = new Options();
    try {
      options.setEncoding("no-such-encoding");
      fail("expected GeneratorException");
    } catch (GeneratorException e) {
      assertTrue(e.getMessage(), e.hasErrorMessage());
      assertTrue(e.getMessage(), e.getMessage().contains("no-such-encoding not supported"));
    }
  }

  @Test
  public void setDir_file_throwsWithMessage() throws Exception {
    Options options = new Options();
    File file = tmp.newFile("not-a-dir");
    try {
      options.setDir(file);
      fail("expected GeneratorException");
    } catch (GeneratorException e) {
      assertTrue(e.getMessage(), e.hasErrorMessage());
      assertEquals("Error: \"" + file + "\" is not a directory.", e.getMessage());
    }
  }

  @Test
  public void setDir_createsDirectory() throws Exception {
    Options options = new Options();
    File dir = new File(tmp.getRoot(), "new/sub");

    options.setDir(dir);

    assertTrue(dir.isDirectory());
    assertEquals(dir, options.directory());
  }
}
