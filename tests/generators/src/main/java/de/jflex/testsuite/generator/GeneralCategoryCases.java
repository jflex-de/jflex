/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.testsuite.generator.EnumeratedPropertyCases.Property;
import de.jflex.testsuite.generator.UcdText.UnicodeDataLine;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The cases of the general category, from {@code UnicodeData.txt}: one for the two-letter
 * categories like {@code Lu}, one for the single-letter groups like {@code L}, and from Unicode 3.2
 * on the cases of their aliases, see {@link AliasCases}.
 */
final class GeneralCategoryCases {
  private static final String CASE_DIR = "unicode-general-category";
  private static final Version UNICODE_1_1 = new Version(1, 1);

  /**
   * @param propertyValueAliases {@code PropertyValueAliases.txt}, or null before Unicode 3.2
   */
  static List<GeneratedCase> generate(Version version, File unicodeData, File propertyValueAliases)
      throws IOException {
    List<String> lines = UcdText.lines(unicodeData);
    boolean is11 = Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_1_1) == 0;
    Property twoLetter = parse(lines, is11, false);
    Property singleLetter = parse(lines, is11, true);
    List<GeneratedCase> cases = new ArrayList<>();
    cases.add(
        EnumeratedPropertyCases.create(
            CASE_DIR,
            "UnicodeGeneralCategory",
            version,
            "General Category",
            null,
            twoLetter,
            "Cn",
            Collections.singleton("Cs")));
    cases.add(
        EnumeratedPropertyCases.create(
            CASE_DIR,
            "UnicodeGeneralCategorySingleLetter",
            version,
            "single-letter General\nCategory",
            null,
            singleLetter,
            "C",
            Collections.<String>emptySet()));
    if (propertyValueAliases != null) {
      cases.addAll(
          AliasCases.generalCategory(version, twoLetter, singleLetter, propertyValueAliases));
    }
    return cases;
  }

  /**
   * The general category of the code points in {@code UnicodeData.txt}.
   *
   * @param is11 whether the file is {@code UnicodeData-1.1.5.txt}, which has a single entry for the
   *     unified ideographs U+4E00..U+9FFF
   * @param singleLetter whether to use the first letter of the category only
   */
  private static Property parse(List<String> lines, boolean is11, boolean singleLetter) {
    Property property = new Property();
    for (int i = 0; i < lines.size(); i++) {
      UnicodeDataLine data = UcdText.parseUnicodeData(lines.get(i));
      if (data == null) {
        continue;
      }
      String category = singleLetter ? data.generalCategory.substring(0, 1) : data.generalCategory;
      if (is11 && data.isCjkRepresentative()) {
        property.ranges.add(new Range(0x4E00, 0x9FFF, category));
        continue;
      }
      // The surrogates get no rule: they are not in the input.
      if (!category.equals("Cs")) {
        property.values.add(category);
      }
      if (data.isFirst()) {
        if (++i < lines.size()) {
          UnicodeDataLine last = UcdText.parseUnicodeData(lines.get(i));
          if (last != null && last.isLast()) {
            property.add(new Range(data.codePoint, last.codePoint, category));
          }
        }
      } else {
        property.add(new Range(data.codePoint, data.codePoint, category));
      }
    }
    return property;
  }

  private GeneralCategoryCases() {}
}
