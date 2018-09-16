/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (c) 2008  Steve Rowe                                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import jflex.unicode.UnicodeProperties;
import junit.framework.TestCase;

public class UnicodePropertiesTest extends TestCase {

  /**
   * Constructor for UnicodePropertiesTest.
   *
   * @param testName test name
   */
  public UnicodePropertiesTest(String testName) {
    super(testName);
  }

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
            "intervals for 'Lu' property value for version "
                + version
                + " should not be null\n"
                + "Supported properties: "
                + properties.getPropertyValues(),
            intervals);
        assertTrue(
            "intervals for 'Lu' property value should have an interval",
            intervals.numIntervals() > 0);
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        assertTrue("Unsupported version '" + version + "' should be supported: " + e, false);
      }
    }
  }

  public void testUnsupportedVersion() {
    try {
      new UnicodeProperties("1.0");
      assertTrue(
          "new UnicodeProperties(\"1.0\") should trigger an"
              + " UnsupportedUnicodeVersionException, but it did not.",
          false);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      // Drop the exception - it is expected.
    }
  }

  public void testDefaultVersion() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      IntCharSet intervals = properties.getIntCharSet("Lu");
      assertNotNull(
          "intervals for 'Lu' property value for default Unicode "
              + "version should not be null\n"
              + "Supported properties: "
              + properties.getPropertyValues(),
          intervals);
      assertTrue(
          "intervals for 'Lu' property value should have an interval",
          intervals.numIntervals() > 0);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Default version is unsupported: " + e, false);
    }
  }

  public void testDefaultVersionAliases() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      IntCharSet set_1 = properties.getIntCharSet("General Category : Other Letter");
      assertNotNull(
          "Null interval set returned for " + "\\p{General Category : Other Letter}", set_1);
      assertTrue(
          "Empty interval set returned for " + "\\p{General Category : Other Letter}",
          set_1.containsElements());
      IntCharSet set_2 = properties.getIntCharSet("Lo");
      assertNotNull("Null interval set returned for \\p{Lo}", set_2);
      assertTrue("Empty interval set returned for \\p{Lo}", set_1.containsElements());
      assertTrue(
          "\\p{General Category : Other Letter} and \\p{Lo} should" + " return the same thing.",
          set_1.equals(set_2));

      set_1 = properties.getIntCharSet(" Script:Tibetan ");
      assertNotNull("Null interval set returned for \\p{ Script:Tibetan }", set_1);
      assertTrue("Empty interval set returned for \\p{ Script:Tibetan }", set_1.containsElements());
      set_2 = properties.getIntCharSet("-_T i b t_-");
      assertNotNull("Null interval set returned for \\p{-_T i b t_-}", set_2);
      assertTrue("Empty interval set returned for \\p{-_T i b t_-}", set_1.containsElements());
      assertTrue(
          "\\p{ Script:Tibetan } and \\p{-_T i b t_-} should" + " return the same thing.",
          set_1.equals(set_2));
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Default version is unsupported: " + e, false);
    }
  }

  public void testCaselessMatches_1_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("1.1");
      IntCharSet caselessMatches = properties.getCaselessMatches('i');
      assertNotNull("'i' has no caseless matches except itself, but it should.", caselessMatches);
      assertTrue(
          "Caseless match set for 'i' should contain 'i', but it doesn't.",
          caselessMatches.contains('i'));
      assertTrue(
          "Caseless match set for 'i' should contain 'I', but it doesn't.",
          caselessMatches.contains('I'));
      assertTrue(
          "Caseless match set for 'i' should contain 2 members, but"
              + " instead contains "
              + caselessMatches.numIntervals(),
          caselessMatches.numIntervals() == 2);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '1.1' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_2_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("2.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '2.0' should be supported: " + e, false);
    }
  }

  private void checkCaseless_i_matches(UnicodeProperties properties) {
    IntCharSet caselessMatches = properties.getCaselessMatches('i');
    assertNotNull("'i' has no caseless matches except itself, but it should.", caselessMatches);
    assertTrue(
        "Caseless match set for 'i' should contain 'i', but it doesn't.",
        caselessMatches.contains('i'));
    assertTrue(
        "Caseless match set for 'i' should contain 'I', but it doesn't.",
        caselessMatches.contains('I'));
    assertTrue(
        "Caseless match set for 'i' should contain uppercase 'I' with"
            + " dot above, but it doesn't.",
        caselessMatches.contains('\u0130'));
    assertTrue(
        "Caseless match set for 'i' should contain lowercase dotless" + " 'i', but it doesn't.",
        caselessMatches.contains('\u0131'));
    Interval interval;
    int charCount = 0;
    for (int i = 0; i < caselessMatches.numIntervals(); ++i) {
      interval = caselessMatches.getNext();
      charCount += interval.end - interval.start + 1;
    }
    assertTrue(
        "Caseless match set for 'i' should contain 4 members, but"
            + " instead contains "
            + charCount,
        charCount == 4);
  }

  public void testCaselessMatches_2_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("2.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '2.1' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_3_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '3.0' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_3_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '3.1' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_3_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("3.2");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '3.2' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_4_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("4.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '4.0' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_4_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("4.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '4.1' should be supported: " + e, false);
    }
  }

  public void testCaselessMatches_5_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '5.0' should be supported: " + e, false);
    }
  }

  public void testSingleLetterProperties_5_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.0");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull("Null interval set for \\p{S}", set_1);
      assertTrue("Empty interval set for \\p{S}", set_1.containsElements());
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull("Null interval set for \\p{Symbol}", set_2);
      assertTrue("Empty interval set for \\p{Symbol}", set_2.containsElements());

      assertTrue("\\p{S} is not the same as \\p{Symbol}", set_1.equals(set_2));

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})", set_1.contains('\u0024'));
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})", set_1.contains('\u002B'));
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})", set_1.contains('\u005E'));
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue("\\p{S} does not contain \\u2196 (\\p{So})", set_1.contains('\u2196'));
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\uFF04 (\\p{Sc}", set_1.contains('\uFF04'));

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Version '5.0' not supported: " + e, false);
    }
  }

  public void testCaselessMatches_5_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.1");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '5.1' should be supported: " + e, false);
    }
  }

  public void testSingleLetterProperties_5_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.1");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull("Null interval set for \\p{S}", set_1);
      assertTrue("Empty interval set for \\p{S}", set_1.containsElements());
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull("Null interval set for \\p{Symbol}", set_2);
      assertTrue("Empty interval set for \\p{Symbol}", set_2.containsElements());

      assertTrue("\\p{S} is not the same as \\p{Symbol}", set_1.equals(set_2));

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})", set_1.contains('\u0024'));
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})", set_1.contains('\u002B'));
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})", set_1.contains('\u005E'));
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue("\\p{S} does not contain \\u2196 (\\p{So})", set_1.contains('\u2196'));
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\uFF04 (\\p{Sc}", set_1.contains('\uFF04'));

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Version '5.1' not supported: " + e, false);
    }
  }

  public void testCaselessMatches_5_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.2");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '5.2' should be supported: " + e, false);
    }
  }

  public void testSingleLetterProperties_5_2() {
    try {
      UnicodeProperties properties = new UnicodeProperties("5.2");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull("Null interval set for \\p{S}", set_1);
      assertTrue("Empty interval set for \\p{S}", set_1.containsElements());
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull("Null interval set for \\p{Symbol}", set_2);
      assertTrue("Empty interval set for \\p{Symbol}", set_2.containsElements());

      assertTrue("\\p{S} is not the same as \\p{Symbol}", set_1.equals(set_2));

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})", set_1.contains('\u0024'));
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})", set_1.contains('\u002B'));
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})", set_1.contains('\u005E'));
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue("\\p{S} does not contain \\u2196 (\\p{So})", set_1.contains('\u2196'));
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\uFF04 (\\p{Sc}", set_1.contains('\uFF04'));

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Version '5.2' not supported: " + e, false);
    }
  }

  public void testCaselessMatches_6_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("6.0");
      checkCaseless_i_matches(properties);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported version '6.0' should be supported: " + e, false);
    }
  }

  public void testSingleLetterProperties_6_0() {
    try {
      UnicodeProperties properties = new UnicodeProperties("6.0");
      IntCharSet set_1 = properties.getIntCharSet("S");
      assertNotNull("Null interval set for \\p{S}", set_1);
      assertTrue("Empty interval set for \\p{S}", set_1.containsElements());
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertNotNull("Null interval set for \\p{Symbol}", set_2);
      assertTrue("Empty interval set for \\p{Symbol}", set_2.containsElements());

      assertTrue("\\p{S} is not the same as \\p{Symbol}", set_1.equals(set_2));

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})", set_1.contains('\u0024'));
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})", set_1.contains('\u002B'));
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertTrue("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})", set_1.contains('\u005E'));
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertTrue("\\p{S} does not contain \\u2196 (\\p{So})", set_1.contains('\u2196'));
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertTrue("\\p{S} does not contain \\uFF04 (\\p{Sc}", set_1.contains('\uFF04'));

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Version '6.0' not supported: " + e, false);
    }
  }
}
