/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * A simple parser.
 *
 * <p>Hand-made parser to test JFlex against, and avoid testing JFlex parsing with JFlex itself.
 */
public abstract class AbstractSimpleParser {

  private final Pattern pattern;
  private final BufferedReader reader;
  private final PatternHandler handler;

  protected AbstractSimpleParser(Pattern pattern, Reader reader, PatternHandler handler) {
    this.pattern = pattern;
    this.reader = new BufferedReader(reader);
    this.handler = handler;
  }

  /**
   * Reads a line.
   *
   * @return true if content was read ; false if EOF was reached.
   * @throws IllegalArgumentException if the content doesn't match the pattern.
   */
  boolean readNext() throws IOException {
    String line = reader.readLine();
    if (line == null) {
      return false;
    }
    line = line.trim();
    if (line.isEmpty() || line.charAt(0) == '#') {
      // skip
      return true;
    }
    Matcher matcher = pattern.matcher(line);
    if (matcher.matches()) {
      List<String> regexpGroups =
          IntStream.range(1, /*endExclusive=*/ matcher.groupCount() + 1)
              .mapToObj(matcher::group)
              .filter(Objects::nonNull)
              .collect(toImmutableList());
      handler.onRegexMatch(regexpGroups);
    } else {
      throw new IllegalArgumentException("Line does not match regex: " + "⟪" + line + "⟫");
    }
    return true;
  }

  @SuppressWarnings("StatementWithEmptyBody") // While is used to loop over all the content
  public void parse() throws IOException {
    while (readNext()) {}
  }

  public interface PatternHandler {
    /** Called back when the line matches the pattern. */
    void onRegexMatch(List<String> regexpGroups);
  }
}
