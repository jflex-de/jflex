/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.5                                                               *
 * Copyright (c) 2008  Steve Rowe                                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import junit.framework.TestCase;

import java.util.List;

public class UnicodePropertiesTest extends TestCase {

  /**
   * Constructor for UnicodePropertiesTest.
   * @param testName test name
   */
  public UnicodePropertiesTest(String testName) {
    super(testName);
  }

  public void testSupportedVersions() {
    String[] versions = new String[] {
      "1.1", "1.1.5", "2", "2.0", "2.1", "3", "3.0", "3.1", "3.2", "4", "4.0",
      "4.1", "5", "5.0"
    };
    for (String version : versions) {
      try {
        UnicodeProperties properties = new UnicodeProperties(version);
        List<Interval> intervals = properties.getIntervals("Lu");
        assertNotNull("intervals for 'Lu' property value for version " + version
                      + " should not be null\n" + "Supported properties: "
                      + properties.getPropertyValues(), intervals);
        assertTrue("intervals for 'Lu' property value should have an interval",
                   intervals.size() > 0);
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        assertTrue("Unsupported version '" + version
                   + "' should be supported: " + e, false);
      }
    }
  }

  public void testUnsupportedVersion() {
    try {
      new UnicodeProperties("1.0");
      assertTrue("new UnicodeProperties(\"1.0\") should trigger an"
                 + " UnsupportedUnicodeVersionException, but it did not.",false);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      // Drop the exception - it is expected.
    }
  }

  public void testDefaultVersion() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      List<Interval> intervals = properties.getIntervals("Lu");
      assertNotNull("intervals for 'Lu' property value for default Unicode "
                    + "version should not be null\n" + "Supported properties: "
                    + properties.getPropertyValues(), intervals);
      assertTrue("intervals for 'Lu' property value should have an interval",
                 intervals.size() > 0);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Default version is unsupported: " + e, false);
    }
  }

  public void testCaselessMatches_1_1() {
    try {
      UnicodeProperties properties = new UnicodeProperties("1.1");
      IntCharSet caselessMatches = properties.getCaselessMatches('i');
      assertNotNull("'i' has no caseless matches except itself, but it should.",
                    caselessMatches);
      assertTrue("Caseless match set for 'i' should contain 'i', but it doesn't.",
                 caselessMatches.contains('i'));
      assertTrue("Caseless match set for 'i' should contain 'I', but it doesn't.",
                 caselessMatches.contains('I'));
      assertTrue("Caseless match set for 'i' should contain 2 members, but"
                 +" instead contains " + caselessMatches.numIntervalls(),
                 caselessMatches.numIntervalls() == 2);
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
    assertNotNull("'i' has no caseless matches except itself, but it should.",
                  caselessMatches);
    assertTrue("Caseless match set for 'i' should contain 'i', but it doesn't.",
               caselessMatches.contains('i'));
    assertTrue("Caseless match set for 'i' should contain 'I', but it doesn't.",
               caselessMatches.contains('I'));
    assertTrue("Caseless match set for 'i' should contain uppercase 'I' with"
               + " dot above, but it doesn't.",
               caselessMatches.contains('\u0130'));
    assertTrue("Caseless match set for 'i' should contain lowercase dotless"
               + " 'i', but it doesn't.",
              caselessMatches.contains('\u0131'));
    Interval interval;
    int charCount = 0;
    for (int i = 0 ; i < caselessMatches.numIntervalls() ; ++i) {
      interval = caselessMatches.getNext();
      charCount += interval.end - interval.start + 1;
    }
    assertTrue("Caseless match set for 'i' should contain 4 members, but"
               + " instead contains " + charCount,
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

  public void testAliases() {
    try {
      UnicodeProperties properties = new UnicodeProperties();
      List<Interval> intervals_1
        = properties.getIntervals("General Category : Other Letter");
      assertNotNull("Null interval list returned for "
                    +"\\p{General Category : Other Letter}", intervals_1);
      assertFalse("Empty interval list returned for "
                  + "\\p{General Category : Other Letter}",
                  intervals_1.isEmpty());
      List<Interval> intervals_2 = properties.getIntervals("Lo");
      assertNotNull("Null interval list returned for \\p{Lo}", intervals_2);
      assertFalse("Empty interval list returned for \\p{Lo}",
                  intervals_1.isEmpty());
      assertTrue("\\p{General Category : Other Letter} and \\p{Lo} should"
                 +" return the same thing.", intervals_1.equals(intervals_2));
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Default version is unsupported: " + e, false);
    }
  }
}
