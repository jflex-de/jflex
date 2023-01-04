/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import java.io.Reader;
import java.util.regex.Pattern;

/** Parser of the golden file for caseless tests. */
public class SimpleGoldenCaselessParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("input char ([0-9A-F]{4,6}) matches ([0-9A-F]{4,6}) case-insensitively");

  public SimpleGoldenCaselessParser(Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }
}
