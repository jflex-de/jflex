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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Cases for binary properties, one per property: the scanner prints the code points that have the
 * property.
 */
final class BinaryPropertyCases {
  private static final Pattern SURROGATE = Pattern.compile("(?i)surrogate");
  private static final Version UNICODE_1_1 = new Version(1, 1);
  private static final Version UNICODE_3_1 = new Version(3, 1);

  /** The cases of the properties in {@code PropList.txt}. */
  static List<GeneratedCase> propList(Version version, File propList) throws IOException {
    Map<String, List<Range>> properties =
        Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_1) < 0
            ? UcdText.propertyDump(propList)
            : UcdText.binaryProperties(propList);
    return create(
        "unicode-proplist",
        "UnicodePropList",
        version,
        properties,
        "defined in PropList(-X.X.X).txt.",
        Collections.<String>emptySet());
  }

  /**
   * The cases of the properties in {@code DerivedCoreProperties.txt}, except {@code Alphabetic},
   * which {@code de.jflex.migration.unicodedatatest.testderivedcoreprop} tests.
   */
  static List<GeneratedCase> derivedCoreProperties(Version version, File derivedCoreProperties)
      throws IOException {
    return create(
        "unicode-derived-core-properties",
        "UnicodeDerivedCoreProperties",
        version,
        UcdText.binaryProperties(derivedCoreProperties),
        "defined in DerivedCoreProperties(-X.X.X).txt.",
        Collections.singleton("Alphabetic"));
  }

  /**
   * The cases of the properties that are not in the UCD: {@code Any}, {@code ASCII}, and {@code
   * Assigned}, the code points that {@code UnicodeData.txt} lists.
   */
  static List<GeneratedCase> misc(Version version, File unicodeData) throws IOException {
    Map<String, List<Range>> properties = new TreeMap<>();
    properties.put("ASCII", Collections.singletonList(new Range(0x0000, 0x007F)));
    properties.put(
        "Any", Arrays.asList(new Range(0x0000, 0xD7FF), new Range(0xE000, Ranges.MAX_CODE_POINT)));
    List<Range> assigned = new ArrayList<>();
    boolean is11 = Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_1_1) == 0;
    int first = -1;
    for (String line : UcdText.lines(unicodeData)) {
      UnicodeDataLine data = UcdText.parseUnicodeData(line);
      if (data == null) {
        continue;
      }
      if (data.isFirst()) {
        first = data.codePoint;
      } else if (data.isLast()) {
        assigned.add(new Range(first, data.codePoint));
        first = -1;
      } else if (is11 && data.isCjkRepresentative()) {
        assigned.add(new Range(0x4E00, 0x9FFF));
      } else {
        assigned.add(new Range(data.codePoint, data.codePoint));
      }
    }
    properties.put("Assigned", assigned);
    List<GeneratedCase> cases =
        create(
            "unicode-misc-properties",
            "UnicodeMisc",
            version,
            properties,
            null,
            Collections.<String>emptySet());
    for (GeneratedCase c : cases) {
      if (c.name.startsWith("UnicodeMisc_Any_")) {
        // Any matches everything, so the second rule is dead.
        c.jflexOutput =
            "\nWarning in file \"src\\test\\cases\\unicode-misc-properties\\"
                + c.name
                + ".flex\" (line 15): \nRule can never be matched:\n[^] { }\n";
      }
    }
    return cases;
  }

  /**
   * One case per property, in the order of the names.
   *
   * @param properties the ranges of each property, as in the file
   * @param definedIn the second line of the description, or null for none
   * @param skip properties that get no case
   */
  private static List<GeneratedCase> create(
      String caseDir,
      String namePrefix,
      Version version,
      Map<String, List<Range>> properties,
      String definedIn,
      Set<String> skip) {
    List<GeneratedCase> cases = new ArrayList<>();
    for (String property : new TreeSet<>(properties.keySet())) {
      if (SURROGATE.matcher(property).find() || skip.contains(property)) {
        continue;
      }
      List<Range> output =
          Ranges.merge(
              Ranges.sortByStart(
                  Ranges.withoutSurrogates(
                      Ranges.upTo(
                          Ranges.withValue(properties.get(property), null),
                          Ranges.MAX_CODE_POINT))));
      String description =
          "Tests character class syntax of the Unicode "
              + GeneratedCase.dotVersion(version)
              + " '"
              + property
              + "' property"
              + (definedIn == null ? "." : ",\n" + definedIn);
      String rules =
          "\\p{"
              + property
              + "} { setCurCharPropertyValue(); }\n"
              + "[^] { }\n\n"
              + "<<EOF>> { printOutput(); return 1; }\n";
      cases.add(
          new GeneratedCase(
              caseDir,
              namePrefix
                  + "_"
                  + property.replaceAll("[^0-9A-Za-z]+", "_")
                  + "_"
                  + GeneratedCase.underscoreVersion(version),
              version,
              description,
              GeneratedCase.Include.BINARY,
              rules,
              output));
    }
    return cases;
  }

  private BinaryPropertyCases() {}
}
