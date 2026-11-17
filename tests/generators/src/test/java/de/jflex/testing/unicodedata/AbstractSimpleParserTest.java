/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.unicodedata;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Test for {@link AbstractSimpleParserTest}. */
@ExtendWith(MockitoExtension.class)
public class AbstractSimpleParserTest {

  @Mock AbstractSimpleParser.PatternHandler handler;

  @Test
  public void testParser_singleLine_nomatch() throws Exception {
    TestingParser parser = createWithContent("foo");
    assertThrows(IllegalArgumentException.class, parser::readNext);
    verify(handler, never()).onRegexMatch(any());
    assertFalse(parser.readNext());
  }

  @Test
  public void testParser_emptyLine() throws Exception {
    TestingParser parser = createWithContent("bar\n\n");
    assertThrows(IllegalArgumentException.class, parser::readNext); // bar\n
    assertTrue(parser.readNext()); // \n [empty line]
    assertFalse(parser.readNext());
  }

  @Test
  public void testParser_onRegexMatch_macth1() throws Exception {
    TestingParser parser = createWithContent("hellooo");
    assertTrue(parser.readNext());
    verify(handler).onRegexMatch(Arrays.asList("hellooo"));
  }

  @Test
  public void testParser_onRegexMatch_macth2() throws Exception {
    TestingParser parser = createWithContent("hello World");
    assertTrue(parser.readNext());
    verify(handler).onRegexMatch(Arrays.asList("hello", " World"));
  }

  TestingParser createWithContent(String inputContent) throws IOException {
    return new TestingParser(new StringReader(inputContent), handler);
  }

  static class TestingParser extends AbstractSimpleParser {
    private static final Pattern PATTERN = Pattern.compile("(hello+)( World)?");

    protected TestingParser(Reader reader, PatternHandler handler) {
      super(PATTERN, reader, handler);
    }
  }
}
