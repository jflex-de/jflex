/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import de.jflex.testing.unicodedata.AbstractSimpleParser;
import java.io.Reader;
import java.util.regex.Pattern;

public class SimpleExpectationParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("input char ([0-9A-F]) matches ([0-9A-F]) case-insensitively");

  protected SimpleExpectationParser(Pattern pattern, Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }
}
