/*
 * Copyright (c) 2008  Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.unicode;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;
import jflex.core.unicode.IntCharSet;
import jflex.core.unicode.UnicodeProperties;
import org.junit.jupiter.api.Test;

public class UnicodePropertiesTest {

  @Test
  public void testSupportedVersions() {
    String[] versions =
        new String[] {
          "1.1", "1.1.5", "2", "2.0", "2.1", "3", "3.0", "3.1", "3.2", "4", "4.0", "4.1", "5",
          "5.0", "5.1", "5.2", "6", "6.0"
        };
    for (String version : versions) {
      try {
        UnicodeProperties properties = new UnicodeProperties(version);
        IntCharSet intervals = properties.getIntCharSet("Lu");
        assertNotNull(
            intervals,
            "intervals for 'Lu' property value for version "
                + version
                + " should not be null\n"
                + "Supported properties: "
                + properties.getPropertyValues());
        assertTrue(
            intervals.numIntervals() > 0,
            "intervals for 'Lu' property value should have an interval");
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        fail("Unsupported version '" + version + "' should be supported: " + e);
      }
    }
  }

  @Test
  public void testUnsupportedVersion() {
    try {
      new UnicodeProperties("1.0");
      fail(
          "new UnicodeProperties(\"1.0\") should trigger an"
              + " UnsupportedUnicodeVersionException, but it did not.");
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      // Drop the exception - it is expected.
    }
  }

  @Test
  public void testDefaultVersion() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      IntCharSet intervals = properties.getIntCharSet("Lu");
      assertNotNull(
          intervals,
          "intervals for 'Lu' property value for default Unicode "
              + "version should not be null\n"
              + "Supported properties: "
              + properties.getPropertyValues());
      assertTrue(
          intervals.numIntervals() > 0,
          "intervals for 'Lu' property value should have an interval");
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Default version is unsupported: " + e);
    }
  }

  @Test
  public void testDefaultVersionAliases() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      IntCharSet set_1 = properties.getIntCharSet("General Category : Other Letter");
      assertNotNull(
          set_1, "Null interval set returned for " + "\\p{General Category : Other Letter}");
      assertTrue(
          set_1.containsElements(),
          "Empty interval set returned for " + "\\p{General Category : Other Letter}");
      IntCharSet set_2 = properties.getIntCharSet("Lo");
      assertNotNull(set_2, "Null interval set returned for \\p{Lo}");
      assertTrue(set_1.containsElements(), "Empty interval set returned for \\p{Lo}");
      assertTrue(
          Objects.equals(set_1, set_2),
          "\\p{General Category : Other Letter} and \\p{Lo} should" + " return the same thing.");

      set_1 = properties.getIntCharSet(" Script:Tibetan ");
      assertNotNull(set_1, "Null interval set returned for \\p{ Script:Tibetan }");
      assertTrue(set_1.containsElements(), "Empty interval set returned for \\p{ Script:Tibetan }");
      set_2 = properties.getIntCharSet("-_T i b t_-");
      assertNotNull(set_2, "Null interval set returned for \\p{-_T i b t_-}");
      assertTrue(set_1.containsElements(), "Empty interval set returned for \\p{-_T i b t_-}");
      assertTrue(
          Objects.equals(set_1, set_2),
          "\\p{ Script:Tibetan } and \\p{-_T i b t_-} should" + " return the same thing.");
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Default version is unsupported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_1_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("1.1");
      IntCharSet caselessMatches = properties.getCaselessMatches('i');
      assertNotNull(caselessMatches, "'i' has no caseless matches except itself, but it should.");
      assertTrue(
          caselessMatches.contains('i'),
          "Caseless match set for 'i' should contain 'i', but it doesn't.");
      assertTrue(
          caselessMatches.contains('I'),
          "Caseless match set for 'i' should contain 'I', but it doesn't.");
      assertTrue(
          caselessMatches.numIntervals() == 2,
          "Caseless match set for 'i' should contain 2 members, but"
              + " instead contains "
              + caselessMatches.numIntervals());
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '1.1' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_2_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("2.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '2.0' should be supported: " + e);
    }
  }

  private static void checkCaseless_i_matches(UnicodeProperties properties) {
    IntCharSet caselessMatches = properties.getCaselessMatches('i');
    assertNotNull(caselessMatches, "'i' has no caseless matches except itself, but it should.");
    assertTrue(
        caselessMatches.contains('i'),
        "Caseless match set for 'i' should contain 'i', but it doesn't.");
    assertTrue(
        caselessMatches.contains('I'),
        "Caseless match set for 'i' should contain 'I', but it doesn't.");
    assertTrue(
        caselessMatches.contains('\u0130'),
        "Caseless match set for 'i' should contain uppercase 'I' with"
            + " dot above, but it doesn't.");
    assertTrue(
        caselessMatches.contains('\u0131'),
        "Caseless match set for 'i' should contain lowercase dotless" + " 'i', but it doesn't.");
    int charCount = caselessMatches.size();
    assertTrue(
        charCount == 4,
        "Caseless match set for 'i' should contain 4 members, but"
            + " instead contains "
            + charCount);
  }

  @Test
  public void testCaselessMatches_2_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("2.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '2.1' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_3_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '3.0' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_3_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '3.1' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_3_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.2");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '3.2' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_4_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("4.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '4.0' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_4_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("4.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '4.1' should be supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_5_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '5.0' should be supported: " + e);
    }
  }

  @Test
  public void testSingleLetterProperties_5_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.0");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull(set_1, "Null interval set for \\p{S}");
      assertTrue(set_1.containsElements(), "Empty interval set for \\p{S}");
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull(set_2, "Null interval set for \\p{Symbol}");
      assertTrue(set_2.containsElements(), "Empty interval set for \\p{Symbol}");

      assertTrue(Objects.equals(set_1, set_2), "\\p{S} is not the same as \\p{Symbol}");

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue(set_1.contains('\u0024'), "\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})");
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue(set_1.contains('\u002B'), "\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})");
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue(set_1.contains('\u005E'), "\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})");
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue(set_1.contains('\u2196'), "\\p{S} does not contain \\u2196 (\\p{So})");
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue(set_1.contains('\uFF04'), "\\p{S} does not contain \\uFF04 (\\p{Sc}");

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Version '5.0' not supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_5_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '5.1' should be supported: " + e);
    }
  }

  @Test
  public void testSingleLetterProperties_5_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.1");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull(set_1, "Null interval set for \\p{S}");
      assertTrue(set_1.containsElements(), "Empty interval set for \\p{S}");
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull(set_2, "Null interval set for \\p{Symbol}");
      assertTrue(set_2.containsElements(), "Empty interval set for \\p{Symbol}");

      assertTrue(Objects.equals(set_1, set_2), "\\p{S} is not the same as \\p{Symbol}");

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue(set_1.contains('\u0024'), "\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})");
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue(set_1.contains('\u002B'), "\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})");
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue(set_1.contains('\u005E'), "\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})");
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue(set_1.contains('\u2196'), "\\p{S} does not contain \\u2196 (\\p{So})");
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue(set_1.contains('\uFF04'), "\\p{S} does not contain \\uFF04 (\\p{Sc}");

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Version '5.1' not supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_5_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.2");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '5.2' should be supported: " + e);
    }
  }

  @Test
  public void testSingleLetterProperties_5_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.2");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull(set_1, "Null interval set for \\p{S}");
      assertTrue(set_1.containsElements(), "Empty interval set for \\p{S}");
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull(set_2, "Null interval set for \\p{Symbol}");
      assertTrue(set_2.containsElements(), "Empty interval set for \\p{Symbol}");

      assertTrue(Objects.equals(set_1, set_2), "\\p{S} is not the same as \\p{Symbol}");

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue(set_1.contains('\u0024'), "\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})");
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue(set_1.contains('\u002B'), "\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})");
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue(set_1.contains('\u005E'), "\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})");
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue(set_1.contains('\u2196'), "\\p{S} does not contain \\u2196 (\\p{So})");
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue(set_1.contains('\uFF04'), "\\p{S} does not contain \\uFF04 (\\p{Sc}");

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Version '5.2' not supported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_6_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("6.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported version '6.0' should be supported: " + e);
    }
  }

  @Test
  public void testSingleLetterProperties_6_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("6.0");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull(set_1, "Null interval set for \\p{S}");
      assertTrue(set_1.containsElements(), "Empty interval set for \\p{S}");
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull(set_2, "Null interval set for \\p{Symbol}");
      assertTrue(set_2.containsElements(), "Empty interval set for \\p{Symbol}");

      assertTrue(Objects.equals(set_1, set_2), "\\p{S} is not the same as \\p{Symbol}");

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue(set_1.contains('\u0024'), "\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})");
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue(set_1.contains('\u002B'), "\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})");
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue(set_1.contains('\u005E'), "\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})");
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue(set_1.contains('\u2196'), "\\p{S} does not contain \\u2196 (\\p{So})");
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue(set_1.contains('\uFF04'), "\\p{S} does not contain \\uFF04 (\\p{Sc}");

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Version '6.0' not supported: " + e);
    }
  }
}
