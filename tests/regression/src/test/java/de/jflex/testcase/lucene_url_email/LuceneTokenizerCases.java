/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2001-2025, The Apache Software Foundation
 * SPDX-License-Identifier: Apache-2.0
 *
 * This file is part of Apache Lucene and was modified for the JFlex regression tests.
 */

package de.jflex.testcase.lucene_url_email;

import org.junit.jupiter.api.Test;

/**
 * Inputs with the tokens that Lucene expects from the scanner of {@code
 * UAX29URLEmailTokenizerImpl.jflex}, from the tests of Lucene. {@link LuceneUrlEmailTest} runs
 * them.
 */
abstract class LuceneTokenizerCases {

  /**
   * Asserts that the tokens of the input have the given texts and types.
   *
   * @param types the types of the tokens as in Lucene, e.g. {@code <ALPHANUM>}, or null to check
   *     the texts only
   */
  abstract void assertAnalyzesTo(String input, String[] output, String[] types) throws Exception;

  void assertAnalyzesTo(String input, String[] output) throws Exception {
    assertAnalyzesTo(input, output, null);
  }

  void checkOneTerm(String input, String expected) throws Exception {
    assertAnalyzesTo(input, new String[] {expected});
  }

  // ---- from TestUAX29URLEmailTokenizer

  @Test
  public void testEmpty() throws Exception {
    assertAnalyzesTo("", new String[] {});
    assertAnalyzesTo(".", new String[] {});
    assertAnalyzesTo(" ", new String[] {});
  }

  @Test
  public void testTypes() throws Exception {
    assertAnalyzesTo(
        "David has 5000 bones",
        new String[] {"David", "has", "5000", "bones"},
        new String[] {"<ALPHANUM>", "<ALPHANUM>", "<NUM>", "<ALPHANUM>"});
  }

  /* Tests from StandardAnalyzer, just to show behavior is similar */
  @Test
  public void testAlphanumericSA() throws Exception {
    // alphanumeric tokens
    assertAnalyzesTo("B2B", new String[] {"B2B"});
    assertAnalyzesTo("2B", new String[] {"2B"});
  }

  @Test
  public void testDelimitersSA() throws Exception {
    // other delimiters: "-", "/", ","
    assertAnalyzesTo("some-dashed-phrase", new String[] {"some", "dashed", "phrase"});
    assertAnalyzesTo("dogs,chase,cats", new String[] {"dogs", "chase", "cats"});
    assertAnalyzesTo("ac/dc", new String[] {"ac", "dc"});
  }

  @Test
  public void testApostrophesSA() throws Exception {
    // internal apostrophes: O'Reilly, you're, O'Reilly's
    assertAnalyzesTo("O'Reilly", new String[] {"O'Reilly"});
    assertAnalyzesTo("you're", new String[] {"you're"});
    assertAnalyzesTo("she's", new String[] {"she's"});
    assertAnalyzesTo("Jim's", new String[] {"Jim's"});
    assertAnalyzesTo("don't", new String[] {"don't"});
    assertAnalyzesTo("O'Reilly's", new String[] {"O'Reilly's"});
  }

  @Test
  public void testNumericSA() throws Exception {
    // floating point, serial, model numbers, ip addresses, etc.
    assertAnalyzesTo("21.35", new String[] {"21.35"});
    assertAnalyzesTo("R2D2 C3PO", new String[] {"R2D2", "C3PO"});
    assertAnalyzesTo("216.239.63.104", new String[] {"216.239.63.104"});
  }

  @Test
  public void testVariousTextSA() throws Exception {
    // various
    assertAnalyzesTo(
        "C embedded developers wanted", new String[] {"C", "embedded", "developers", "wanted"});
    assertAnalyzesTo("foo bar FOO BAR", new String[] {"foo", "bar", "FOO", "BAR"});
    assertAnalyzesTo("foo      bar .  FOO <> BAR", new String[] {"foo", "bar", "FOO", "BAR"});
    assertAnalyzesTo("\"QUOTED\" word", new String[] {"QUOTED", "word"});
  }

  /**
   * Multiple consecutive chars in \p{Word_Break = MidLetter}, \p{Word_Break = MidNumLet}, and/or
   * \p{Word_Break = MidNum} should trigger a token split.
   */
  @Test
  public void testMid() throws Exception {
    // ':' is in \p{WB:MidLetter}, which should trigger a split unless there is a Letter char on
    // both sides
    assertAnalyzesTo("A:B", new String[] {"A:B"});
    assertAnalyzesTo("A::B", new String[] {"A", "B"});

    // '.' is in \p{WB:MidNumLet}, which should trigger a split unless there is a Letter or Numeric
    // char on both sides
    assertAnalyzesTo("1.2", new String[] {"1.2"});
    assertAnalyzesTo("A.B", new String[] {"A.B"});
    assertAnalyzesTo("1..2", new String[] {"1", "2"});
    assertAnalyzesTo("A..B", new String[] {"A", "B"});

    // ',' is in \p{WB:MidNum}, which should trigger a split unless there is a Numeric char on both
    // sides
    assertAnalyzesTo("1,2", new String[] {"1,2"});
    assertAnalyzesTo("1,,2", new String[] {"1", "2"});

    // Mixed consecutive \p{WB:MidLetter} and \p{WB:MidNumLet} should trigger a split
    assertAnalyzesTo("A.:B", new String[] {"A", "B"});
    assertAnalyzesTo("A:.B", new String[] {"A", "B"});

    // Mixed consecutive \p{WB:MidNum} and \p{WB:MidNumLet} should trigger a split
    assertAnalyzesTo("1,.2", new String[] {"1", "2"});
    assertAnalyzesTo("1.,2", new String[] {"1", "2"});

    // '_' is in \p{WB:ExtendNumLet}

    assertAnalyzesTo("A:B_A:B", new String[] {"A:B_A:B"});
    assertAnalyzesTo("A:B_A::B", new String[] {"A:B_A", "B"});

    assertAnalyzesTo("1.2_1.2", new String[] {"1.2_1.2"});
    assertAnalyzesTo("A.B_A.B", new String[] {"A.B_A.B"});
    assertAnalyzesTo("1.2_1..2", new String[] {"1.2_1", "2"});
    assertAnalyzesTo("A.B_A..B", new String[] {"A.B_A", "B"});

    assertAnalyzesTo("1,2_1,2", new String[] {"1,2_1,2"});
    assertAnalyzesTo("1,2_1,,2", new String[] {"1,2_1", "2"});

    assertAnalyzesTo("C_A.:B", new String[] {"C_A", "B"});
    assertAnalyzesTo("C_A:.B", new String[] {"C_A", "B"});

    assertAnalyzesTo("3_1,.2", new String[] {"3_1", "2"});
    assertAnalyzesTo("3_1.,2", new String[] {"3_1", "2"});
  }

  @Test
  public void testLUCENE1545() throws Exception {
    /*
     * Standard analyzer does not correctly tokenize combining character U+0364 COMBINING LATIN SMALL LETTRE E.
     * The word "mo\u0364chte" is incorrectly tokenized into "mo" "chte", the combining character is lost.
     * Expected result is only on token "mo\u0364chte".
     */
    assertAnalyzesTo("mo\u0364chte", new String[] {"mo\u0364chte"});
  }

  @Test
  public void testCombiningMarks() throws Exception {
    checkOneTerm("さ\u3099", "さ\u3099"); // hiragana
    checkOneTerm("サ\u3099", "サ\u3099"); // katakana
    checkOneTerm("壹\u3099", "壹\u3099"); // ideographic
    checkOneTerm("아\u3099", "아\u3099"); // hangul
  }

  @Test
  public void testThai() throws Exception {
    assertAnalyzesTo(
        "การที่ได้ต้องแสดงว่างานดี. แล้วเธอจะไปไหน? ๑๒๓๔",
        new String[] {"การที่ได้ต้องแสดงว่างานดี", "แล้วเธอจะไปไหน", "๑๒๓๔"});
  }

  /*
   * For chinese, tokenize as char (these can later form bigrams or whatever)
   */
  @Test
  public void testChinese() throws Exception {
    assertAnalyzesTo("我是中国人。 １２３４ Ｔｅｓｔｓ ", new String[] {"我", "是", "中", "国", "人", "１２３４", "Ｔｅｓｔｓ"});
  }

  @Test
  public void testSupplementary() throws Exception {
    assertAnalyzesTo(
        "𩬅艱鍟䇹愯瀛",
        new String[] {"𩬅", "艱", "鍟", "䇹", "愯", "瀛"},
        new String[] {
          "<IDEOGRAPHIC>",
          "<IDEOGRAPHIC>",
          "<IDEOGRAPHIC>",
          "<IDEOGRAPHIC>",
          "<IDEOGRAPHIC>",
          "<IDEOGRAPHIC>"
        });
  }

  @Test
  public void testKorean() throws Exception {
    assertAnalyzesTo("훈민정음", new String[] {"훈민정음"}, new String[] {"<HANGUL>"});
  }

  @Test
  public void testJapanese() throws Exception {
    assertAnalyzesTo(
        "仮名遣い カタカナ",
        new String[] {"仮", "名", "遣", "い", "カタカナ"},
        new String[] {
          "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<HIRAGANA>", "<KATAKANA>"
        });
  }

  /** simple emoji */
  @Test
  public void testEmoji() throws Exception {
    assertAnalyzesTo(
        "💩 💩💩", new String[] {"💩", "💩", "💩"}, new String[] {"<EMOJI>", "<EMOJI>", "<EMOJI>"});
  }

  /** emoji zwj sequence */
  @Test
  public void testEmojiSequence() throws Exception {
    assertAnalyzesTo(
        "👩\u200D❤\uFE0F\u200D👩",
        new String[] {"👩\u200D❤\uFE0F\u200D👩"},
        new String[] {"<EMOJI>"});
  }

  /** emoji zwj sequence with fitzpatrick modifier */
  @Test
  public void testEmojiSequenceWithModifier() throws Exception {
    assertAnalyzesTo(
        "👨🏼\u200D⚕\uFE0F", new String[] {"👨🏼\u200D⚕\uFE0F"}, new String[] {"<EMOJI>"});
  }

  /** regional indicator */
  @Test
  public void testEmojiRegionalIndicator() throws Exception {
    assertAnalyzesTo(
        "🇺🇸🇺🇸", new String[] {"🇺🇸", "🇺🇸"}, new String[] {"<EMOJI>", "<EMOJI>"});
  }

  /** variation sequence */
  @Test
  public void testEmojiVariationSequence() throws Exception {
    assertAnalyzesTo("#\uFE0F\u20E3", new String[] {"#\uFE0F\u20E3"}, new String[] {"<EMOJI>"});
    assertAnalyzesTo(
        "3\uFE0F\u20E3",
        new String[] {
          "3\uFE0F\u20E3",
        },
        new String[] {"<EMOJI>"});

    // text presentation sequences
    assertAnalyzesTo("#\uFE0E", new String[] {}, new String[] {});
    assertAnalyzesTo(
        "3\uFE0E", // \uFE0E is included in \p{WB:Extend}
        new String[] {
          "3\uFE0E",
        },
        new String[] {"<NUM>"});
    assertAnalyzesTo(
        "\u2B55\uFE0E", // \u2B55 = HEAVY BLACK CIRCLE
        new String[] {
          "\u2B55",
        },
        new String[] {"<EMOJI>"});
    assertAnalyzesTo(
        "\u2B55\uFE0E\u200D\u2B55\uFE0E",
        new String[] {"\u2B55", "\u200D\u2B55"},
        new String[] {"<EMOJI>", "<EMOJI>"});
  }

  @Test
  public void testEmojiTagSequence() throws Exception {
    assertAnalyzesTo(
        "🏴\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F",
        new String[] {"🏴\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"},
        new String[] {"<EMOJI>"});
  }

  @Test
  public void testEmojiTokenization() throws Exception {
    // simple emoji around latin
    assertAnalyzesTo(
        "poo💩poo",
        new String[] {"poo", "💩", "poo"},
        new String[] {"<ALPHANUM>", "<EMOJI>", "<ALPHANUM>"});
    // simple emoji around non-latin
    assertAnalyzesTo(
        "💩中國💩",
        new String[] {"💩", "中", "國", "💩"},
        new String[] {"<EMOJI>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<EMOJI>"});
  }

  @Test
  public void testEmailTokenization() throws Exception {
    assertAnalyzesTo(
        "foo,bar@yahoo.com",
        new String[] {"foo", "bar@yahoo.com"},
        new String[] {"<ALPHANUM>", "<EMAIL>"});
  }

  @Test
  public void testMailtoSchemeEmails() throws Exception {
    // See LUCENE-3880
    assertAnalyzesTo(
        "mailto:test@example.org",
        new String[] {"mailto", "test@example.org"},
        new String[] {"<ALPHANUM>", "<EMAIL>"});

    // TODO: Support full mailto: scheme URIs. See RFC 6068: http://tools.ietf.org/html/rfc6068
    assertAnalyzesTo(
        "mailto:personA@example.com,personB@example.com?cc=personC@example.com"
            + "&subject=Subjectivity&body=Corpusivity%20or%20something%20like%20that",
        new String[] {
          "mailto",
          "personA@example.com",
          // Also, see examples of ';' delimiter use at:
          // http://www.mailto.co.uk/
          "personB@example.com",
          "?cc=personC@example.com", // TODO: split field keys/values
          "subject",
          "Subjectivity",
          "body",
          "Corpusivity",
          "20or",
          "20something",
          "20like",
          "20that"
        }, // TODO: Hex decoding + re-tokenization
        new String[] {
          "<ALPHANUM>",
          "<EMAIL>",
          "<EMAIL>",
          "<EMAIL>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>",
          "<ALPHANUM>"
        });
  }

  // ---- from TestUAX29URLEmailAnalyzer
  //
  // The analyzer of these tests converts the tokens of the tokenizer to lower case and removes
  // English stop words. Here, the expected tokens have the case of the input instead, and
  // testBasicURLs() expects the stop word "a" as first token.

  @Test
  public void testBasicEmails() throws Exception {
    assertAnalyzesTo(
        "one test@example.com two three [A@example.CO.UK] \"ArakaBanassaMassanaBakarA\""
            + " <info@Info.info>",
        new String[] {
          "one",
          "test@example.com",
          "two",
          "three",
          "A@example.CO.UK",
          "ArakaBanassaMassanaBakarA",
          "info@Info.info",
        },
        new String[] {
          "<ALPHANUM>", "<EMAIL>", "<ALPHANUM>", "<ALPHANUM>", "<EMAIL>", "<ALPHANUM>", "<EMAIL>"
        });
  }

  @Test
  public void testBasicURLs() throws Exception {
    assertAnalyzesTo(
        "a <HTTPs://example.net/omg/isnt/that/NICE?no=its&n%30t#mntl-E>b-D"
            + " ftp://www.example.com/ABC.txt file:///C:/path/to/a/FILE.txt C",
        new String[] {
          "a",
          "HTTPs://example.net/omg/isnt/that/NICE?no=its&n%30t#mntl-E",
          "b",
          "D",
          "ftp://www.example.com/ABC.txt",
          "file:///C:/path/to/a/FILE.txt",
          "C"
        },
        new String[] {
          "<ALPHANUM>", "<URL>", "<ALPHANUM>", "<ALPHANUM>", "<URL>", "<URL>", "<ALPHANUM>"
        });
  }

  @Test
  public void testNoSchemeURLs() throws Exception {
    // ".ph" is a Top Level Domain
    assertAnalyzesTo("<index.ph>", new String[] {"index.ph"}, new String[] {"<URL>"});

    assertAnalyzesTo("index.ph", new String[] {"index.ph"}, new String[] {"<URL>"});

    assertAnalyzesTo("index.php", new String[] {"index.php"}, new String[] {"<ALPHANUM>"});

    assertAnalyzesTo("index.phα", new String[] {"index.phα"}, new String[] {"<ALPHANUM>"});

    assertAnalyzesTo(
        "index-h.php", new String[] {"index", "h.php"}, new String[] {"<ALPHANUM>", "<ALPHANUM>"});

    assertAnalyzesTo(
        "index2.php", new String[] {"index2", "php"}, new String[] {"<ALPHANUM>", "<ALPHANUM>"});

    assertAnalyzesTo(
        "index2.ph９,", new String[] {"index2", "ph９"}, new String[] {"<ALPHANUM>", "<ALPHANUM>"});

    assertAnalyzesTo(
        "example.com,example.ph,index.php,index2.php,example2.ph",
        new String[] {"example.com", "example.ph", "index.php", "index2", "php", "example2.ph"},
        new String[] {"<URL>", "<URL>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<URL>"});

    assertAnalyzesTo(
        "example.com:8080 example.com/path/here example.com?query=something example.com#fragment",
        new String[] {
          "example.com:8080",
          "example.com/path/here",
          "example.com?query=something",
          "example.com#fragment"
        },
        new String[] {"<URL>", "<URL>", "<URL>", "<URL>"});

    assertAnalyzesTo(
        "example.com:8080/path/here?query=something#fragment",
        new String[] {"example.com:8080/path/here?query=something#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080/path/here?query=something",
        new String[] {"example.com:8080/path/here?query=something"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080/path/here#fragment",
        new String[] {"example.com:8080/path/here#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080/path/here",
        new String[] {"example.com:8080/path/here"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080?query=something#fragment",
        new String[] {"example.com:8080?query=something#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080?query=something",
        new String[] {"example.com:8080?query=something"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com:8080#fragment",
        new String[] {"example.com:8080#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com/path/here?query=something#fragment",
        new String[] {"example.com/path/here?query=something#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com/path/here?query=something",
        new String[] {"example.com/path/here?query=something"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com/path/here#fragment",
        new String[] {"example.com/path/here#fragment"},
        new String[] {"<URL>"});

    assertAnalyzesTo(
        "example.com?query=something#fragment",
        new String[] {"example.com?query=something#fragment"},
        new String[] {"<URL>"});
  }
}
