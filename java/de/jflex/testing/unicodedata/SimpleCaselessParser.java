/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Parser of {@code UnicodeData.txt} for caseless declarations.
 *
 * <p>e.g. {@code AC00;<Hangul Syllable, First>;Lo;0;L;;;;;N;;;;;}.
 */
public class SimpleCaselessParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("^([A-F0-9a-f]{4,6});(?:[^;]*;){11}([^;]*);([^;]*);([^;]*)");

  public SimpleCaselessParser(Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }
}
