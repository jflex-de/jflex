/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
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
package de.jflex.testing.unicodedata;

import static com.google.common.truth.Truth.assertThat;
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
  public void testParser_singleLine() throws Exception {
    TestingParser parser = createWithContent("foo");
    assertThat(parser.readNext()).isTrue();
    assertThat(parser.readNext()).isFalse();
  }

  @Test
  public void testParser_emptyLine() throws Exception {
    TestingParser parser = createWithContent("bar\n\n");
    assertThat(parser.readNext()).isTrue(); // bar\n
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

  @Test
  public void testParser_onRegexMatch_noMatch() throws Exception {
    TestingParser parser = createWithContent("foo");
    assertThat(parser.readNext()).isTrue();
    verify(handler, never()).onRegexMatch(any());
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
