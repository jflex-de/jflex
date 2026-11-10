/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jflex.l10n.ErrorMessages;
import org.junit.Test;

/** Tests for {@link Options}. */
public class OptionsTest {

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
}
