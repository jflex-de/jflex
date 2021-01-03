/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.migration.unicodedatatest.base;

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
   */
  boolean readNext() throws IOException {
    String line = reader.readLine();
    if (line == null) {
      return false;
    }
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
    }
    return true;
  }

  public void parse() throws IOException {
    while (readNext()) {}
  }

  public interface PatternHandler {
    /** Called back when the line matches the pattern. */
    void onRegexMatch(List<String> regexpGroups);
  }
}
