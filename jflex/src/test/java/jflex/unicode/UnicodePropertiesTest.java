/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (c) 2008  Steve Rowe                                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.unicode;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.fail;

import java.util.Objects;
import jflex.core.unicode.IntCharSet;
import jflex.core.unicode.UnicodeProperties;
import org.junit.Test;

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
        assertWithMessage(
                "intervals for 'Lu' property value for version "
                    + version
                    + " should not be null\n"
                    + "Supported properties: "
                    + properties.getPropertyValues())
            .that(intervals)
            .isNotNull();
        assertWithMessage("intervals for 'Lu' property value should have an interval")
            .that(intervals.numIntervals() > 0)
            .isTrue();
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
      assertWithMessage(
              "intervals for 'Lu' property value for default Unicode "
                  + "version should not be null\n"
                  + "Supported properties: "
                  + properties.getPropertyValues())
          .that(intervals)
          .isNotNull();
      assertWithMessage("intervals for 'Lu' property value should have an interval")
          .that(intervals.numIntervals() > 0)
          .isTrue();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Default version is unsupported: " + e);
    }
  }

  @Test
  public void testDefaultVersionAliases() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      IntCharSet set_1 = properties.getIntCharSet("General Category : Other Letter");
      assertWithMessage("Null interval set returned for " + "\\p{General Category : Other Letter}")
          .that(set_1)
          .isNotNull();
      assertWithMessage("Empty interval set returned for " + "\\p{General Category : Other Letter}")
          .that(set_1.containsElements())
          .isTrue();
      IntCharSet set_2 = properties.getIntCharSet("Lo");
      assertWithMessage("Null interval set returned for \\p{Lo}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set returned for \\p{Lo}")
          .that(set_1.containsElements())
          .isTrue();
      assertWithMessage(
              "\\p{General Category : Other Letter} and \\p{Lo} should" + " return the same thing.")
          .that(Objects.equals(set_1, set_2))
          .isTrue();

      set_1 = properties.getIntCharSet(" Script:Tibetan ");
      assertWithMessage("Null interval set returned for \\p{ Script:Tibetan }")
          .that(set_1)
          .isNotNull();
      assertWithMessage("Empty interval set returned for \\p{ Script:Tibetan }")
          .that(set_1.containsElements())
          .isTrue();
      set_2 = properties.getIntCharSet("-_T i b t_-");
      assertWithMessage("Null interval set returned for \\p{-_T i b t_-}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set returned for \\p{-_T i b t_-}")
          .that(set_1.containsElements())
          .isTrue();
      assertWithMessage(
              "\\p{ Script:Tibetan } and \\p{-_T i b t_-} should" + " return the same thing.")
          .that(Objects.equals(set_1, set_2))
          .isTrue();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Default version is unsupported: " + e);
    }
  }

  @Test
  public void testCaselessMatches_1_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("1.1");
      IntCharSet caselessMatches = properties.getCaselessMatches('i');
      assertWithMessage("'i' has no caseless matches except itself, but it should.")
          .that(caselessMatches)
          .isNotNull();
      assertWithMessage("Caseless match set for 'i' should contain 'i', but it doesn't.")
          .that(caselessMatches.contains('i'))
          .isTrue();
      assertWithMessage("Caseless match set for 'i' should contain 'I', but it doesn't.")
          .that(caselessMatches.contains('I'))
          .isTrue();
      assertWithMessage(
              "Caseless match set for 'i' should contain 2 members, but"
                  + " instead contains "
                  + caselessMatches.numIntervals())
          .that(caselessMatches.numIntervals() == 2)
          .isTrue();
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
    assertWithMessage("'i' has no caseless matches except itself, but it should.")
        .that(caselessMatches)
        .isNotNull();
    assertWithMessage("Caseless match set for 'i' should contain 'i', but it doesn't.")
        .that(caselessMatches.contains('i'))
        .isTrue();
    assertWithMessage("Caseless match set for 'i' should contain 'I', but it doesn't.")
        .that(caselessMatches.contains('I'))
        .isTrue();
    assertWithMessage(
            "Caseless match set for 'i' should contain uppercase 'I' with"
                + " dot above, but it doesn't.")
        .that(caselessMatches.contains('\u0130'))
        .isTrue();
    assertWithMessage(
            "Caseless match set for 'i' should contain lowercase dotless" + " 'i', but it doesn't.")
        .that(caselessMatches.contains('\u0131'))
        .isTrue();
    int charCount = caselessMatches.size();
    assertWithMessage(
            "Caseless match set for 'i' should contain 4 members, but"
                + " instead contains "
                + charCount)
        .that(charCount == 4)
        .isTrue();
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
      assertWithMessage("Null interval set for \\p{S}").that(set_1).isNotNull();
      assertWithMessage("Empty interval set for \\p{S}").that(set_1.containsElements()).isTrue();
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertWithMessage("Null interval set for \\p{Symbol}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set for \\p{Symbol}")
          .that(set_2.containsElements())
          .isTrue();

      assertWithMessage("\\p{S} is not the same as \\p{Symbol}")
          .that(Objects.equals(set_1, set_2))
          .isTrue();

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})")
          .that(set_1.contains('\u0024'))
          .isTrue();
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})")
          .that(set_1.contains('\u002B'))
          .isTrue();
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertWithMessage("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})")
          .that(set_1.contains('\u005E'))
          .isTrue();
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertWithMessage("\\p{S} does not contain \\u2196 (\\p{So})")
          .that(set_1.contains('\u2196'))
          .isTrue();
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\uFF04 (\\p{Sc}")
          .that(set_1.contains('\uFF04'))
          .isTrue();

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
      assertWithMessage("Null interval set for \\p{S}").that(set_1).isNotNull();
      assertWithMessage("Empty interval set for \\p{S}").that(set_1.containsElements()).isTrue();
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertWithMessage("Null interval set for \\p{Symbol}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set for \\p{Symbol}")
          .that(set_2.containsElements())
          .isTrue();

      assertWithMessage("\\p{S} is not the same as \\p{Symbol}")
          .that(Objects.equals(set_1, set_2))
          .isTrue();

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})")
          .that(set_1.contains('\u0024'))
          .isTrue();
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})")
          .that(set_1.contains('\u002B'))
          .isTrue();
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertWithMessage("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})")
          .that(set_1.contains('\u005E'))
          .isTrue();
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertWithMessage("\\p{S} does not contain \\u2196 (\\p{So})")
          .that(set_1.contains('\u2196'))
          .isTrue();
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\uFF04 (\\p{Sc}")
          .that(set_1.contains('\uFF04'))
          .isTrue();

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
      assertWithMessage("Null interval set for \\p{S}").that(set_1).isNotNull();
      assertWithMessage("Empty interval set for \\p{S}").that(set_1.containsElements()).isTrue();
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertWithMessage("Null interval set for \\p{Symbol}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set for \\p{Symbol}")
          .that(set_2.containsElements())
          .isTrue();

      assertWithMessage("\\p{S} is not the same as \\p{Symbol}")
          .that(Objects.equals(set_1, set_2))
          .isTrue();

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})")
          .that(set_1.contains('\u0024'))
          .isTrue();
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})")
          .that(set_1.contains('\u002B'))
          .isTrue();
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertWithMessage("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})")
          .that(set_1.contains('\u005E'))
          .isTrue();
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertWithMessage("\\p{S} does not contain \\u2196 (\\p{So})")
          .that(set_1.contains('\u2196'))
          .isTrue();
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\uFF04 (\\p{Sc}")
          .that(set_1.contains('\uFF04'))
          .isTrue();

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
      assertWithMessage("Null interval set for \\p{S}").that(set_1).isNotNull();
      assertWithMessage("Empty interval set for \\p{S}").that(set_1.containsElements()).isTrue();
      IntCharSet set_2 = properties.getIntCharSet("Symbol");
      assertWithMessage("Null interval set for \\p{Symbol}").that(set_2).isNotNull();
      assertWithMessage("Empty interval set for \\p{Symbol}")
          .that(set_2.containsElements())
          .isTrue();

      assertWithMessage("\\p{S} is not the same as \\p{Symbol}")
          .that(Objects.equals(set_1, set_2))
          .isTrue();

      // 0024;DOLLAR SIGN;Sc;0;ET;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u0024 '\u0024' (\\p{Sc})")
          .that(set_1.contains('\u0024'))
          .isTrue();
      // 002B;PLUS SIGN;Sm;0;ES;;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\u002B '\u002B' (\\p{Sm})")
          .that(set_1.contains('\u002B'))
          .isTrue();
      // 005E;CIRCUMFLEX ACCENT;Sk;0;ON;;;;;N;SPACING CIRCUMFLEX;;;;
      assertWithMessage("\\p{S} does not contain \\u005E '\u005E' (\\p{Sk})")
          .that(set_1.contains('\u005E'))
          .isTrue();
      // 2196;NORTH WEST ARROW;So;0;ON;;;;;N;UPPER LEFT ARROW;;;;
      assertWithMessage("\\p{S} does not contain \\u2196 (\\p{So})")
          .that(set_1.contains('\u2196'))
          .isTrue();
      // FF04;FULLWIDTH DOLLAR SIGN;Sc;0;ET;<wide> 0024;;;;N;;;;;
      assertWithMessage("\\p{S} does not contain \\uFF04 (\\p{Sc}")
          .that(set_1.contains('\uFF04'))
          .isTrue();

    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Version '6.0' not supported: " + e);
    }
  }
}
