/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.testsuite.generator.UcdText.UnicodeDataLine;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Cases for the character class syntaxes {@code [:letter:]}, {@code [:lowercase:]}, {@code
 * [:uppercase:]}, {@code \s} and {@code \w}, and their negations.
 *
 * <p>The scanner prints every code point, with the property or its complement, {@code Not-}. The
 * properties come from {@code UnicodeData.txt} in the versions that have no other source for them.
 */
final class ClassSyntaxCases {
  private static final Version UNICODE_1_1 = new Version(1, 1);
  private static final Version UNICODE_3_0 = new Version(3, 0);
  private static final Version UNICODE_3_1 = new Version(3, 1);

  /** The entry for the unified ideographs in {@code UnicodeData-1.1.5.txt}. */
  private enum Cjk {
    /** It is an ordinary code point. */
    IGNORE,
    /** It extends the current range to U+9FFF. */
    EXTEND,
    /** U+4E00..U+9FFF have the property. */
    RANGE
  }

  /** The {@code [:letter:]} case: the categories {@code L*}. */
  static GeneratedCase letter(Version version, File unicodeData) throws IOException {
    List<Range> letters =
        runs(unicodeData, version, Pattern.compile("^L.$"), true, Cjk.EXTEND, "Letter");
    return create(
        "unicode-letter", "UnicodeLetter", version, "Letter", "Letter", "[:letter:]", letters);
  }

  /**
   * The {@code [:lowercase:]} case: {@code Lowercase} in {@code DerivedCoreProperties.txt}, the
   * {@code PropList} of Unicode 3.0, or the category {@code Ll} before that.
   */
  static GeneratedCase lowercase(
      Version version, File unicodeData, File propList, File derivedCoreProperties)
      throws IOException {
    return caseProperty(
        version, unicodeData, propList, derivedCoreProperties, "Ll", "Lowercase", "[:lowercase:]");
  }

  /** The {@code [:uppercase:]} case, see {@link #lowercase}. */
  static GeneratedCase uppercase(
      Version version, File unicodeData, File propList, File derivedCoreProperties)
      throws IOException {
    return caseProperty(
        version, unicodeData, propList, derivedCoreProperties, "Lu", "Uppercase", "[:uppercase:]");
  }

  private static GeneratedCase caseProperty(
      Version version,
      File unicodeData,
      File propList,
      File derivedCoreProperties,
      String category,
      String property,
      String syntax)
      throws IOException {
    List<Range> ranges;
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_0) < 0) {
      ranges = runs(unicodeData, version, exactly(category), false, Cjk.IGNORE, property);
    } else if (Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_1) < 0) {
      ranges = binary(UcdText.propertyDump(propList), exactly(property));
    } else {
      ranges = binary(UcdText.binaryProperties(derivedCoreProperties), exactly(property));
    }
    return create(
        "unicode-" + property.toLowerCase(),
        "Unicode" + property,
        version,
        property,
        property,
        syntax,
        ranges);
  }

  /**
   * The cases of {@code \S} and {@code \s}: {@code White_Space} in {@code PropList.txt}, or the
   * category {@code Zs} in Unicode 1.1.
   */
  static List<GeneratedCase> whiteSpace(Version version, File unicodeData, File propList)
      throws IOException {
    List<Range> spaces;
    Pattern whiteSpace = Pattern.compile("(?i)^White[ _]?Space$");
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_1_1) == 0) {
      spaces = runs(unicodeData, version, Pattern.compile("^Zs$"), false, Cjk.IGNORE, "Whitespace");
    } else if (Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_1) < 0) {
      spaces = binary(UcdText.propertyDump(propList), whiteSpace);
    } else {
      spaces = binary(UcdText.binaryProperties(propList), whiteSpace);
    }
    return Arrays.asList(
        create(
            "unicode-space",
            "UnicodeWhiteSpace1",
            version,
            "WhiteSpace",
            "Whitespace",
            "\\S",
            spaces),
        create(
            "unicode-space",
            "UnicodeWhiteSpace2",
            version,
            "WhiteSpace",
            "Whitespace",
            "\\s",
            spaces));
  }

  /**
   * The cases of {@code \W} and {@code \w}. UTS #18 defines {@code \w} as {@code
   * [\p{Alphabetic}\p{M}\p{Nd}\p{Pc}]}, with {@code Alphabetic} from {@code
   * DerivedCoreProperties.txt}, or from {@code PropList} before Unicode 3.1. Unicode 1.1 has no
   * {@code Alphabetic}; it uses {@code \p{L}}, and the underscore for the missing {@code Pc}.
   *
   * @param propList null for Unicode 1.1 and from 3.1 on
   * @param derivedCoreProperties null before Unicode 3.1
   */
  static List<GeneratedCase> word(
      Version version, File unicodeData, File propList, File derivedCoreProperties)
      throws IOException {
    boolean is11 = Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_1_1) == 0;
    Pattern categories = Pattern.compile(is11 ? "^(?:L.|M.|Nd|Pc)$" : "^(?:M.|Nd|Pc)$");
    List<Range> words = runs(unicodeData, version, categories, false, Cjk.RANGE, "Word");
    Pattern alphabetic = Pattern.compile("(?i)^Alphabetic$");
    if (is11) {
      words.add(new Range(0x005F, 0x005F, "Word"));
    } else if (Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_1) < 0) {
      words.addAll(binary(UcdText.propertyDump(propList), alphabetic));
    } else {
      words.addAll(binary(UcdText.binaryProperties(derivedCoreProperties), alphabetic));
    }
    return Arrays.asList(
        create("unicode-word", "UnicodeWord1", version, "Word", "Word", "\\W", words),
        create("unicode-word", "UnicodeWord2", version, "Word", "Word", "\\w", words));
  }

  /**
   * The ranges of the code points in {@code UnicodeData.txt} with the property, in file order.
   *
   * <p>A range is a run of consecutive code points with the same general category, or the same
   * first letter of it; a run belongs to the property if its category matches.
   *
   * @param cjk what to do with the entry for the unified ideographs of Unicode 1.1
   */
  private static List<Range> runs(
      File unicodeData,
      Version version,
      Pattern categories,
      boolean byFirstLetter,
      Cjk cjk,
      String property)
      throws IOException {
    boolean is11 = Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_1_1) == 0;
    List<Range> ranges = new ArrayList<>();
    int previous = -2;
    int start = -1;
    String category = "";
    for (String line : UcdText.lines(unicodeData)) {
      UnicodeDataLine data = UcdText.parseUnicodeData(line);
      if (data == null) {
        continue;
      }
      if (is11 && cjk != Cjk.IGNORE && data.isCjkRepresentative()) {
        if (cjk == Cjk.EXTEND) {
          previous = 0x9FFF;
        } else {
          ranges.add(new Range(0x4E00, 0x9FFF, property));
        }
        continue;
      }
      if (data.isLast()) {
        previous = data.codePoint;
        continue;
      }
      boolean sameRun =
          byFirstLetter
              ? category
                  .substring(0, Math.min(1, category.length()))
                  .equals(data.generalCategory.substring(0, 1))
              : category.equals(data.generalCategory);
      if (previous + 1 == data.codePoint && sameRun) {
        previous = data.codePoint;
      } else {
        if (categories.matcher(category).find()) {
          ranges.add(new Range(start, previous, property));
        }
        start = data.codePoint;
        category = data.generalCategory;
        previous = data.codePoint;
      }
    }
    if (categories.matcher(category).find()) {
      ranges.add(new Range(start, previous, property));
    }
    return ranges;
  }

  private static Pattern exactly(String name) {
    return Pattern.compile("^" + Pattern.quote(name) + "$");
  }

  /** The ranges of the properties whose name matches, as in the file. */
  private static List<Range> binary(Map<String, List<Range>> properties, Pattern name) {
    List<Range> ranges = new ArrayList<>();
    for (Map.Entry<String, List<Range>> entry : properties.entrySet()) {
      if (name.matcher(entry.getKey()).find()) {
        ranges.addAll(entry.getValue());
      }
    }
    return ranges;
  }

  /**
   * @param propertyName the name in the description, e.g. {@code WhiteSpace}
   * @param value the value in the output, e.g. {@code Whitespace}; the complement is {@code
   *     Not-Whitespace}
   * @param syntax the character class, e.g. {@code \s}; an upper-case letter negates
   * @param ranges the code points with the property, in any order, surrogates allowed
   */
  private static GeneratedCase create(
      String caseDir,
      String namePrefix,
      Version version,
      String propertyName,
      String value,
      String syntax,
      List<Range> ranges) {
    List<Range> positive =
        Ranges.merge(
            Ranges.sortByStart(
                Ranges.withoutSurrogates(
                    Ranges.upTo(Ranges.withValue(ranges, value), Ranges.MAX_CODE_POINT))));
    List<Range> output = new ArrayList<>(positive);
    output.addAll(Ranges.gaps(positive, Ranges.MAX_CODE_POINT, "Not-" + value));
    boolean negated = Character.isUpperCase(syntax.charAt(syntax.length() - 1));
    String rules =
        "<<EOF>> { printOutput(); return 1; }\n"
            + syntax
            + " { setCurCharPropertyValue(\""
            + (negated ? "Not-" : "")
            + value
            + "\"); }\n[^"
            + syntax
            + "] { setCurCharPropertyValue(\""
            + (negated ? "" : "Not-")
            + value
            + "\"); }\n";
    return new GeneratedCase(
        caseDir,
        namePrefix + "_" + GeneratedCase.underscoreVersion(version),
        version,
        "Tests character class syntax of the Unicode "
            + GeneratedCase.dotVersion(version)
            + " "
            + propertyName
            + " property\nusing the '"
            + syntax
            + "' syntax.",
        GeneratedCase.Include.ENUMERATED,
        rules,
        Ranges.sortByStart(output));
  }

  private ClassSyntaxCases() {}
}
