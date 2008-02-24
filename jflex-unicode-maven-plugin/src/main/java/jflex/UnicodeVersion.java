/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Unicode plugin                                                    *
 * Copyright (c) 2008 Steve Rowe <steve_rowe@users.sf.net>                 *
 *                                                                         *
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

// TODO: For those versions of Unicode that don't specifically enumerate
// TODO: Unassigned characters, automatically determine and make intervals for them.

package jflex;

import java.net.URL;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class is instantiated for each version of Unicode for which data can
 * be found at unicode.org.  This class is responsible for parsing
 * UnicodeData(-X.X.X).txt and for emitting (into the source file generated
 * by jflex-unicode-maven-plugin: UnicodeProperties.java) the set of supported
 * Property Values supported by this version of Unicode, as wells as the
 * corresponding code point interval sets for each member of the Property Value
 * set.
 */
class UnicodeVersion
{
  /** Pattern for the full Unicode version */
  private final Pattern FULL_VERSION_PATTERN
    = Pattern.compile("(\\d+\\.\\d+)\\.\\d+");

  /** Pattern for the full Unicode version from the unicode data URL */
  private final Pattern FULL_VERSION_PATTERN_IN_URL
    = Pattern.compile("UnicodeData-(\\d+\\.\\d+\\.\\d+)\\.txt$");

  // v1.1: 00F8;LATIN SMALL LETTER O WITH STROKE;Ll;0;L;006F 0338;;;;N;LATIN SMALL LETTER O SLASH;;00D8;;00D8
  // v5.0: 00F8;LATIN SMALL LETTER O WITH STROKE;Ll;0;L;;;;;N;LATIN SMALL LETTER O SLASH;;00D8;;00D8
  /** Pattern to capture data from UnicodeData(-X.X.X).txt */
  private static final Pattern UNICODE_DATA_LINE_PATTERN
    = Pattern.compile("^([^;]*);(?:([^;]*,\\s*Last>)|[^;]*);([^;]*)");

  /** Unicode version X.X.X */
  String majorMinorUpdateVersion;

  /** Unicode version X.X */
  String majorMinorVersion;

  /** The URL at which UnicodeData(-X.X.X).txt is located. */
  URL unicodeDataURL;

  /** The greatest code point listed in UnicodeData(-X.X.X).txt */
  int maximumCodePoint;

  /**
   * Maps Unicode general category property values to the associated set of
   * code point ranges.
   */
  SortedMap<String,List<Interval>> propertyValueIntervals
    = new TreeMap<String,List<Interval>>();

  /**
   * The number of code point ranges to output per line in
   * UnicodeProperties.java.
   */
  private static final int INTERVALS_PER_LINE = 4;

  /**
   * Instantiates a container for versioned Unicode data.
   *
   * @param version The Unicode version, either in form "X.X.X" or "X.X".
   * @param unicodeDataURL The URL to the UnicodeData(-X.X.X).txt file for this
   *  version.
   * @throws IOException If there is a problem fetching or parsing
   *  UnicodeData(-X.X.X).txt
   */
  UnicodeVersion(String version, URL unicodeDataURL) throws IOException {
    this.unicodeDataURL = unicodeDataURL;
    setVersions(version);
    parseUnicodeData();
  }

  /**
   * Fills in majorMinorVersion and majorMinorUpdateVersion based on the passed
   * in version string.
   *
   * @param version The Unicode version, in form "X.X.X" or "X.X".
   */
  private void setVersions(String version) {
    Matcher matcher = FULL_VERSION_PATTERN.matcher(version);
    if (matcher.matches()) {
      majorMinorUpdateVersion = matcher.group(0);
      majorMinorVersion = matcher.group(1);
    } else {
      majorMinorVersion = version;
      matcher
        = FULL_VERSION_PATTERN_IN_URL.matcher(unicodeDataURL.toString());
      if (matcher.find()) {
        majorMinorUpdateVersion = matcher.group(1);
      }
    }
  }

  /**
   * Fetches UnicodeData(-X.X.X).txt from the URL supplied in the constructor,
   * storing the code point ranges in {@link #propertyValueIntervals}.
   *
   * @throws IOException If there is a problem fetching or parsing
   *  UnicodeData(-X.X.X).txt.
   */
  private void parseUnicodeData() throws IOException {
    BufferedReader reader = new BufferedReader
      (new InputStreamReader(unicodeDataURL.openStream(), "UTF-8"));
    String line;
    int startCodePoint = -1;
    int prevCodePoint = 0;
    String prevGenCatPropValue = "";
    while (null != (line = reader.readLine()))
    {
      Matcher matcher = UNICODE_DATA_LINE_PATTERN.matcher(line);
      if (matcher.find()) {
        int codePoint = Integer.parseInt(matcher.group(1), 16);
        boolean isLastInRange = (null != matcher.group(2));
        String genCatPropValue = matcher.group(3);

        if (startCodePoint != -1
            && prevGenCatPropValue.length() > 0
            && (((codePoint > prevCodePoint + 1) && ! isLastInRange)
                || ! genCatPropValue.equals(prevGenCatPropValue))) {
          addInterval(prevGenCatPropValue, startCodePoint, prevCodePoint);
          startCodePoint = -1;
        }
        if (genCatPropValue.length() > 0 && startCodePoint == -1) {
          startCodePoint = codePoint;
        }
        prevCodePoint = codePoint;
        prevGenCatPropValue = genCatPropValue;
      }
    }
    // Handle the final interval, if there is one
    if (startCodePoint != -1 && prevGenCatPropValue.length() > 0) {
      addInterval(prevGenCatPropValue, startCodePoint, prevCodePoint);
    }
    maximumCodePoint = prevCodePoint;
  }

  /**
   * Given the general category property value, and starting and ending code
   * points, adds the interval to the {@link #propertyValueIntervals} map.
   *
   * @param genCatPropValue The general category property value, e.g. "Lu".
   * @param startCodePoint The first code point in the interval.
   * @param endCodePoint The last code point in the interval.
   */
  private void addInterval(String genCatPropValue,
                           int startCodePoint, int endCodePoint) {
    List<Interval> intervals = propertyValueIntervals.get(genCatPropValue);
    if (null == intervals) {
      intervals = new ArrayList<Interval>();
      propertyValueIntervals.put(genCatPropValue, intervals);
    }
    if (majorMinorVersion.equals("1.1")) {
      // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
      // range (starting point is listed as U+4E00).  This is U+9FFF according
      // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
      //
      //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
      //
      // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
      // value, as are the previous code points, so to include
      // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
      if (endCodePoint == 0x4E00) {
        endCodePoint = 0x9FFF;
      }
    }
    intervals.add(new Interval(startCodePoint, endCodePoint));
  }

  /**
   * Emits an int declaration and definition for the maximum code point listed
   * for this version in UnicodeData(-X.X.X).txt.
   *
   * @param builder Where to emit the maximum code point.
   */
  void emitMaximumCodePoint(StringBuilder builder) {
    builder.append("  private static final int maximumCodePoint")
      .append(getVersionSuffix()).append(" = 0x")
      .append(Integer.toString(maximumCodePoint, 16)).append(";\n");
  }

  /**
   * Emits an array declaration and definition for the set of general category
   * property values supported by this version of Unicode.
   *
   * @param builder Where to emit the property values array.
   */
  void emitPropertyValuesArray(StringBuilder builder) {
    builder.append("  private static final String[] propertyValues")
      .append(getVersionSuffix()).append("\n    = { ");
    int item = 0;
    for (String genCatPropValue : propertyValueIntervals.keySet()) {
      if (++item == 13) {
        builder.append(",\n        ");
        item = 1;
      } else if (item > 1) {
        builder.append(", ");
      }
      builder.append("\"").append(genCatPropValue).append("\"");
    }
    builder.append(" };\n");
  }

  /**
   * Emits an array declaration and definition for the set of code point ranges
   * in this version of Unicode, corresponding to and in the same order as the
   * array of general category property values emitted in
   * {@link #emitPropertyValuesArray(StringBuilder)}.
   * <p/>
   * Note that String form is required for the amount of data associated with
   * the existing Unicode versions - when coded as static two-dimensional arrays
   * of int, the Java byte compiler complains that "code too large".  This is
   * apparently due to size limits in the specification for Java .class format.
   *
   * @param builder Where to emit the intervals array
   */
  void emitIntervalsArray(StringBuilder builder) {
    builder.append("  private static final String[] intervals")
      .append(getVersionSuffix()).append(" = {\n");

    boolean isFirst = true;
    for (SortedMap.Entry<String,List<Interval>> entry
        : propertyValueIntervals.entrySet()) {
      String propertyValue = entry.getKey();
      List<Interval> intervals = entry.getValue();
      if (isFirst) {
        isFirst = false;
      } else {
        builder.append(",\n");
      }
      builder.append("    // Unicode ").append(majorMinorVersion)
        .append(" general category: {").append(propertyValue)
        .append("}\n");
      int count = 0;
      boolean isFirstIntervalLine = true;
      for (Interval interval : intervals) {
        if (++count > INTERVALS_PER_LINE) {
          builder.append("\n");
          count = 1;
        }
        if (count == 1) {
          builder.append(isFirstIntervalLine ? "        \"" : "      + \"");
        } else {
          builder.append("+\"");
        }
        isFirstIntervalLine = false;
        emitEscapedUTF16Char(builder, interval.start);
        emitEscapedUTF16Char(builder, interval.end);
        builder.append("\"");
      }
    }
    builder.append("  };\n");
  }

  /**
   * Emits an escaped character:
   * <ul>
   *   <li>in form "\\uXXXX", where XXXX is the hexadecimal form of the code
   *       point, if the given point is in the Basic Multilingual Plane (BMP);
   *       or</li>
   *   <li>in form "\\uXXXX\\uYYYY", where XXXX and YYYY are the high and low
   *       surrogates, respectively, representing the given point in UTF-16
   *       form, if the given code point is above the BMP.</li>
   * </ul>
   *
   * @param builder Where to emit the escaped character.
   * @param codePoint The code point for which to emit an escaped character.
   */
  private void emitEscapedUTF16Char(StringBuilder builder, int codePoint) {
    if (codePoint <= 0xFFFF) {
      emitEscapedBMPChar(builder, codePoint);
    } else { // codePoint > 0xFFFF - above the BMP
      for (char surrogate : Character.toChars(codePoint)) {
        emitEscapedBMPChar(builder, (int)surrogate);
      }
    }
  }

  /**
   * Emits an escaped character in the form "\\uXXXX", where XXXX is the
   * hexadecimal form of the given code point, which must be in the Basic
   * Multilingual Plane (BMP).  Called from 
   * {@link #emitEscapedUTF16Char(StringBuilder,int)}
   *
   * @param builder Where to emit the escaped character.
   * @param codePoint The code point for which to emit an escaped character.
   */
  private void emitEscapedBMPChar(StringBuilder builder, int codePoint) {
    if (codePoint == 0x22) {
      // Special treatment for the quotation mark (U+0022).  "\u0022" triggers
      // a syntax error when it is included in a literal string, because it is 
      // interpreted as "[...]"[...]" (literally), and leads the compiler to
      // think that the enclosing quotation marks are unbalanced.
      builder.append("\\\"");
    } else {
      builder.append("\\u");
      String hexCodePoint = Integer.toString(codePoint, 16);
      for (int i = 0 ; i < 4 - hexCodePoint.length() ; ++i) {
        builder.append('0');
      }
      builder.append(hexCodePoint);
    }
  }

  /**
   * Returns an identifier suffix based on the Unicode major.minor version,
   * substituting an underscore for the period, and with a leading underscore,
   * for use in naming versioned Unicode data in the generated
   * UnicodeProperties.java.
   *
   * @return "_X_Y", where X = major version, and Y = minor version.
   */
  String getVersionSuffix() {
    return "_" + majorMinorVersion.replace(".", "_");
  }

  /**
   * Internal-use class to represent code point intervals.
   */
  private class Interval {
    int start;
    int end;

    Interval(int start, int end) {
      this.start = start;
      this.end = end;
    }
  }
}
