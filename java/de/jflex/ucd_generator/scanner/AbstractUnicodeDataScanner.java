package de.jflex.ucd_generator.scanner;

import com.google.common.base.Preconditions;
import de.jflex.ucd_generator.ucd.UcdVersion;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.ucd.Versions;
import de.jflex.version.Version;

/** Scanner of the {@code UnicodeData.txt}. */
abstract class AbstractUnicodeDataScanner {

  private static final String GENERAL_CATEGORY = "General_Category";

  private final UcdVersion ucdVersion;
  private final UnicodeData unicodeData;

  int codePoint = -1;
  int startCodePoint = -1;
  int prevCodePoint = 0;
  int assignedStartCodePoint = -1;
  int assignedEndCodePoint = -1;
  String genCatPropValue;
  String prevGenCatPropValue;
  String uppercaseMapping = null;
  String lowercaseMapping = null;
  String titlecaseMapping = null;
  boolean isLastInRange = false;

  protected AbstractUnicodeDataScanner(UcdVersion ucdVersion, UnicodeData unicodeData) {
    this.ucdVersion = ucdVersion;
    this.unicodeData = unicodeData;
  }

  public void handleEntry() {
    Preconditions.checkArgument(codePoint >= 0, "Negative codepoint: " + codePoint);
    unicodeData.addCaselessMatches(codePoint, uppercaseMapping, lowercaseMapping, titlecaseMapping);

    // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
    // range (starting point is listed as U+4E00).  This is U+9FFF according
    // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
    //
    //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
    //
    // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
    // value, as are the previous code points, so to include
    // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
    if (assignedEndCodePoint == 0x4E00
        && Version.MAJOR_MINOR_COMPARATOR.compare(ucdVersion.version(), Versions.VERSION_1_1)
            == 0) {
      assignedEndCodePoint = 0x9FFF;
    }

    if (assignedStartCodePoint == -1) {
      assignedStartCodePoint = startCodePoint;
    } else if (codePoint > assignedEndCodePoint + 1 && !isLastInRange) {
      unicodeData.addBinaryPropertyInterval(
          "Assigned", assignedStartCodePoint, assignedEndCodePoint);
      unicodeData.addEnumPropertyInterval(
          GENERAL_CATEGORY, "Cn", assignedEndCodePoint + 1, codePoint - 1);
      assignedStartCodePoint = codePoint;
    }
    assignedEndCodePoint = codePoint;

    if (startCodePoint != -1
        && prevGenCatPropValue.length() > 0
        && (((codePoint > prevCodePoint + 1) && !isLastInRange)
            || !genCatPropValue.equals(prevGenCatPropValue))) {
      // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
      // range (starting point is listed as U+4E00).  This is U+9FFF according
      // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
      //
      //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
      //
      // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
      // value, as are the previous code points, so to include
      // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
      if (prevCodePoint == 0x4E00
          && Version.MAJOR_MINOR_COMPARATOR.compare(ucdVersion.version(), Versions.VERSION_1_1)
              == 0) {
        prevCodePoint = 0x9FFF;
      }
      unicodeData.addEnumPropertyInterval(
          GENERAL_CATEGORY, prevGenCatPropValue, startCodePoint, prevCodePoint);
      startCodePoint = -1;
    }
    if (genCatPropValue.length() > 0 && startCodePoint == -1) {
      startCodePoint = codePoint;
    }
    prevCodePoint = codePoint;
    prevGenCatPropValue = genCatPropValue;
    reset();
  }

  public void reset() {
    isLastInRange = false;
    uppercaseMapping = null;
    lowercaseMapping = null;
    titlecaseMapping = null;
    codePoint = -1;
  }

  public void handleFinalInterval() {
    if (startCodePoint != -1 && prevGenCatPropValue.length() > 0) {
      unicodeData.addEnumPropertyInterval(
          GENERAL_CATEGORY, prevGenCatPropValue, startCodePoint, prevCodePoint);
    }

    // Handle the final Assigned interval
    unicodeData.addBinaryPropertyInterval("Assigned", assignedStartCodePoint, assignedEndCodePoint);

    // Round max code point up to end-of-plane.
    unicodeData.maximumCodePoint(((prevCodePoint + 0x800) & 0xFFF000) - 1);

    // Handle the final Unassigned (Cn) interval, if any
    if (assignedEndCodePoint < unicodeData.maximumCodePoint()) {
      unicodeData.addEnumPropertyInterval(
          GENERAL_CATEGORY, "Cn", assignedEndCodePoint + 1, unicodeData.maximumCodePoint());
    }
  }
}
