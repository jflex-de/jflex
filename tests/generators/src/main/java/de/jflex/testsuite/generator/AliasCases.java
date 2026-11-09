/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.testsuite.generator.EnumeratedPropertyCases.Property;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Cases that use the aliases of {@code PropertyValueAliases.txt} in the character classes: for the
 * general category {@code \p{gc:Lu}} or {@code \p{Uppercase_Letter}}, and for the scripts {@code
 * \p{latn}} or {@code \p{sc=Latin}}. There is one case per syntax. Each rule prints the canonical
 * value, so the output is that of the case with the canonical syntax.
 */
final class AliasCases {

  /** How a rule writes the class of a value. */
  private interface Syntax {
    String classOf(String value, String alias);
  }

  /** The syntaxes of the general category. The values are the short names, e.g. {@code Lu}. */
  private static final List<Syntax> GENERAL_CATEGORY =
      Arrays.asList(
          (value, alias) -> "General_Category:" + value,
          (value, alias) -> "gc:" + value,
          (value, alias) -> alias,
          (value, alias) -> "gc:" + alias,
          (value, alias) -> "General_Category:" + alias);

  /**
   * The syntaxes of the scripts; the values are the long names, e.g. {@code Old_Italic}. The last
   * one writes the underscores as spaces, which JFlex also accepts.
   */
  private static final List<Syntax> SCRIPT =
      Arrays.asList(
          (value, alias) -> alias.toLowerCase(Locale.ENGLISH),
          (value, alias) -> "sc=" + alias.toLowerCase(Locale.ENGLISH),
          (value, alias) -> "sc=" + value.replace('_', ' '));

  /**
   * The alias cases of the general category.
   *
   * @param twoLetter the categories like {@code Lu}
   * @param singleLetter the groups like {@code L}
   */
  static List<GeneratedCase> generalCategory(
      Version version, Property twoLetter, Property singleLetter, File propertyValueAliases)
      throws IOException {
    Map<String, String> longNames = aliases(propertyValueAliases, "gc", true);
    List<GeneratedCase> cases = new ArrayList<>();
    cases.addAll(
        create(
            "unicode-general-category",
            "UnicodeGeneralCategoryAliases",
            version,
            "Tests the Unicode general category property character class syntax\n"
                + "for all general category aliases supported in Unicode ",
            twoLetter,
            "Cn",
            Collections.singleton("Cs"),
            longNames,
            GENERAL_CATEGORY));
    cases.addAll(
        create(
            "unicode-general-category",
            "UnicodeGeneralCategorySingleLetterAliases",
            version,
            "Tests the Unicode general category single letter property character\n"
                + "class syntax for all single letter general category aliases supported in Unicode"
                + " ",
            singleLetter,
            "C",
            Collections.<String>emptySet(),
            longNames,
            GENERAL_CATEGORY));
    return cases;
  }

  /** The alias cases of the {@code Script} property, see {@link ScriptsCases}. */
  static List<GeneratedCase> scripts(
      Version version,
      Property property,
      String defaultValue,
      Set<String> skip,
      File propertyValueAliases)
      throws IOException {
    return create(
        "unicode-scripts",
        "UnicodeScriptAliases",
        version,
        "Tests the Unicode script property character class syntax\n"
            + "for all script aliases supported in Unicode ",
        property,
        defaultValue,
        skip,
        aliases(propertyValueAliases, "sc", false),
        SCRIPT);
  }

  /**
   * One case per syntax with one rule per value.
   *
   * @param namePrefix the name of the cases before the part number and the version
   * @param description the description before the version, after {@code Part n -- }
   * @param aliases the alias of every value, see {@link #aliases}
   */
  private static List<GeneratedCase> create(
      String caseDir,
      String namePrefix,
      Version version,
      String description,
      Property property,
      String defaultValue,
      Set<String> skip,
      Map<String, String> aliases,
      List<Syntax> syntaxes) {
    SortedSet<String> values = new TreeSet<>(property.values);
    values.add(defaultValue);
    values.removeAll(skip);
    List<Range> output = EnumeratedPropertyCases.output(property, defaultValue, skip, null);
    List<GeneratedCase> cases = new ArrayList<>();
    for (int part = 1; part <= syntaxes.size(); part++) {
      Syntax syntax = syntaxes.get(part - 1);
      StringBuilder rules = new StringBuilder("<<EOF>> { printOutput(); return 1; }\n");
      for (String value : values) {
        String alias = aliases.get(value.toLowerCase(Locale.ENGLISH));
        if (alias == null) {
          throw new IllegalStateException(
              "No alias of " + value + " for " + namePrefix + " in Unicode " + version);
        }
        rules
            .append("\\p{")
            .append(syntax.classOf(value, alias))
            .append("} { setCurCharPropertyValue(\"")
            .append(value)
            .append("\"); }\n");
      }
      cases.add(
          new GeneratedCase(
              caseDir,
              namePrefix + part + "_" + GeneratedCase.underscoreVersion(version),
              version,
              "Part " + part + " -- " + description + GeneratedCase.dotVersion(version),
              GeneratedCase.Include.ENUMERATED,
              rules.toString(),
              output));
    }
    return cases;
  }

  /**
   * The aliases of the values of a property in {@code PropertyValueAliases.txt}, with syntax {@code
   * property ; short name ; long name}, by the value in lower case ({@code Scripts.txt} of Unicode
   * 3.2 has upper case spelling, later versions do not).
   *
   * @param property the short name of the property, e.g. {@code gc}
   * @param shortIsValue whether the short names are the values (for general category), and the long
   *     names the aliases or the other way around (for scripts).
   */
  private static Map<String, String> aliases(File file, String property, boolean shortIsValue)
      throws IOException {
    Map<String, String> aliases = new HashMap<>();
    for (String line : UcdText.lines(file)) {
      String[] fields = UcdText.stripComment(line).split(";");
      if (fields.length < 3 || !fields[0].trim().equals(property)) {
        continue;
      }
      String shortName = fields[1].trim();
      String longName = fields[2].trim();
      if (shortIsValue) {
        aliases.put(shortName.toLowerCase(Locale.ENGLISH), longName);
      } else {
        aliases.put(longName.toLowerCase(Locale.ENGLISH), shortName);
      }
    }
    return aliases;
  }

  private AliasCases() {}
}
