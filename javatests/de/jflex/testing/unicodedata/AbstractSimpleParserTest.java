/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.testing.assertion.MoreAsserts.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/** Test for {@link AbstractSimpleParserTest}. */
public class AbstractSimpleParserTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule();
  @Mock AbstractSimpleParser.PatternHandler handler;

  @Test
  public void testParser_singleLine_nomatch() throws Exception {
    TestingParser parser = createWithContent("foo");
    assertThrows(IllegalArgumentException.class, parser::readNext);
    verify(handler, never()).onRegexMatch(any());
    assertThat(parser.readNext()).isFalse();
  }

  @Test
  public void testParser_emptyLine() throws Exception {
    TestingParser parser = createWithContent("bar\n\n");
    assertThrows(IllegalArgumentException.class, parser::readNext); // bar\n
    assertThat(parser.readNext()).isTrue(); // \n [empty line]
    assertThat(parser.readNext()).isFalse();
  }

  @Test
  public void testParser_onRegexMatch_macth1() throws Exception {
    TestingParser parser = createWithContent("hellooo");
    assertThat(parser.readNext()).isTrue();
    verify(handler).onRegexMatch(ImmutableList.of("hellooo"));
  }

  @Test
  public void testParser_onRegexMatch_macth2() throws Exception {
    TestingParser parser = createWithContent("hello World");
    assertThat(parser.readNext()).isTrue();
    verify(handler).onRegexMatch(ImmutableList.of("hello", " World"));
  }

  TestingParser createWithContent(String inputContent) throws IOException {
    return new TestingParser(CharSource.wrap(inputContent).openStream(), handler);
  }

  static class TestingParser extends AbstractSimpleParser {
    private static final Pattern PATTERN = Pattern.compile("(hello+)( World)?");

    protected TestingParser(Reader reader, PatternHandler handler) {
      super(PATTERN, reader, handler);
    }
  }
}
