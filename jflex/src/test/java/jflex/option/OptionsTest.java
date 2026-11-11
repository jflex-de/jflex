/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.option;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for {@link Options}. */
public class OptionsTest {

  @TempDir File tmp;

  private static void assertAllWarningsEnabled(String msg, Options options) {
    for (ErrorMessages warning : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(warning)) {
        assertFalse(options.isSuppressed(warning), warning + " " + msg);
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
        options.isSuppressed(ErrorMessages.NEVER_MATCH), "NEVER_MATCH suppressed after reset()");
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
    assertTrue(options.isSuppressed(ErrorMessages.NEVER_MATCH), "original changed by copy");
    assertFalse(options.verbose(), "original changed by copy");
  }

  /** Check errors-via-exception reporting in Options. */
  @Test
  public void suppressWarning_unknown_throwsWithMessage() {
    Options options = new Options();
    GeneratorException e =
        assertThrows(GeneratorException.class, () -> options.suppressWarning("no-such-warning"));
    assertTrue(e.hasErrorMessage(), e.getMessage());
    assertTrue(e.getMessage().contains("\"no-such-warning\" unknown"), e.getMessage());
  }

  @Test
  public void setEncoding_unsupported_throwsWithMessage() {
    Options options = new Options();
    GeneratorException e =
        assertThrows(GeneratorException.class, () -> options.setEncoding("no-such-encoding"));
    assertTrue(e.hasErrorMessage(), e.getMessage());
    assertTrue(e.getMessage().contains("no-such-encoding not supported"), e.getMessage());
  }

  @Test
  public void setDir_file_throwsWithMessage() throws Exception {
    Options options = new Options();
    File file = new File(tmp, "not-a-dir");
    assertTrue(file.createNewFile());
    GeneratorException e = assertThrows(GeneratorException.class, () -> options.setDir(file));
    assertTrue(e.hasErrorMessage(), e.getMessage());
    assertEquals("Error: \"" + file + "\" is not a directory.", e.getMessage());
  }

  @Test
  public void setDir_createsDirectory() throws Exception {
    Options options = new Options();
    File dir = new File(tmp, "new/sub");

    options.setDir(dir);

    assertTrue(dir.isDirectory());
    assertEquals(dir, options.directory());
  }
}
