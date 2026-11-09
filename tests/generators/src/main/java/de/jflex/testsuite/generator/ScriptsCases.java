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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * The cases of the {@code Script} property, and from Unicode 6.0 on of {@code Script_Extensions}.
 *
 * <p>The {@code Script_Extensions} values of a code point are a set of scripts, and a scanner
 * cannot report more than one value per code point. So a case tests a group of scripts that have no
 * code point in common, and only prints code points with one of these scripts. The scripts that
 * {@code ScriptExtensions.txt} does not mention keep their {@code Script} value and are tested
 * together in the {@code missing} case.
 */
final class ScriptsCases {
  private static final String CASE_DIR = "unicode-scripts";
  private static final String PROPERTY = "Script_Extensions";
  private static final Pattern SURROGATE = Pattern.compile("(?i)surrogate");
  private static final Version UNICODE_4_1 = new Version(4, 1);

  /**
   * @param extensions {@code ScriptExtensions.txt}, or null before Unicode 6.0
   * @param aliases {@code PropertyValueAliases.txt}, for the short script names in {@code
   *     extensions}
   */
  static List<GeneratedCase> generate(Version version, File scripts, File extensions, File aliases)
      throws IOException {
    Property property = EnumeratedPropertyCases.parse(scripts);
    // Unicode 5.0 added the Unknown script for the code points that Scripts.txt does not list.
    String defaultValue =
        Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_4_1) > 0 ? "Unknown" : "Common";
    Set<String> skip = new HashSet<>();
    for (String value : property.values) {
      if (SURROGATE.matcher(value).find()) {
        skip.add(value);
      }
    }
    List<GeneratedCase> cases = new ArrayList<>();
    cases.add(
        EnumeratedPropertyCases.create(
            CASE_DIR, "UnicodeScripts", version, "Script", null, property, defaultValue, skip));
    if (extensions != null) {
      List<Range> allScripts =
          Ranges.complete(property.ranges, defaultValue, Ranges.MAX_CODE_POINT);
      SortedSet<String> scriptNames = new TreeSet<>(property.values);
      scriptNames.add(defaultValue);
      cases.addAll(extensions(version, allScripts, scriptNames, skip, extensions, aliases));
    }
    return cases;
  }

  /**
   * @param allScripts the {@code Script} value of every code point
   * @param scriptNames all {@code Script} values
   */
  private static List<GeneratedCase> extensions(
      Version version,
      List<Range> allScripts,
      SortedSet<String> scriptNames,
      Set<String> skip,
      File extensionsFile,
      File aliasesFile)
      throws IOException {
    Map<String, String> canonical = scriptAliases(aliasesFile);
    // The scripts that ScriptExtensions.txt does not mention.
    SortedSet<String> missing = new TreeSet<>(scriptNames);
    // For each script, the scripts that share a code point with it.
    Map<String, Set<String>> overlapping = new HashMap<>();
    SortedMap<String, List<Range>> ranges = new TreeMap<>();
    boolean[] hasExtensions = new boolean[Ranges.MAX_CODE_POINT + 1];
    for (String line : UcdText.lines(extensionsFile)) {
      Range range = UcdText.parseRange(UcdText.stripComment(line));
      if (range == null || range.start > Ranges.MAX_CODE_POINT) {
        continue;
      }
      int end = Math.min(range.end, Ranges.MAX_CODE_POINT);
      for (int c = range.start; c <= end; c++) {
        hasExtensions[c] = true;
      }
      List<String> scripts = new ArrayList<>();
      for (String alias : range.value.split("\\s+")) {
        String script = canonical.get(alias);
        if (script == null) {
          throw new IllegalStateException("Unknown script " + alias + " in " + extensionsFile);
        }
        scripts.add(script);
      }
      for (String script : scripts) {
        add(ranges, script, new Range(range.start, end, script));
        missing.remove(script);
        Set<String> others = overlapping.get(script);
        if (others == null) {
          others = new HashSet<>();
          overlapping.put(script, others);
        }
        for (String other : scripts) {
          if (!other.equals(script)) {
            others.add(other);
          }
        }
      }
    }
    // The code points without extensions keep their Script value.
    for (Range range : allScripts) {
      int start = -1;
      for (int c = range.start; c <= range.end + 1; c++) {
        boolean keeps = c <= range.end && !hasExtensions[c];
        if (keeps && start < 0) {
          start = c;
        } else if (!keeps && start >= 0) {
          add(ranges, range.value, new Range(start, c - 1, range.value));
          start = -1;
        }
      }
    }
    for (Map.Entry<String, List<Range>> entry : ranges.entrySet()) {
      entry.setValue(Ranges.merge(Ranges.sortByStart(entry.getValue())));
    }

    List<GeneratedCase> cases = new ArrayList<>();
    String namePrefix =
        "UnicodeScripts_" + GeneratedCase.underscoreVersion(version) + "_extensions_";
    String dotVersion = GeneratedCase.dotVersion(version);
    cases.add(
        create(
            version,
            namePrefix + "missing",
            "Tests character class syntax of the Unicode "
                + dotVersion
                + " "
                + PROPERTY
                + "\nproperty, for those scripts missing from ScriptExtensions.txt, which\n"
                + "are defined in Scripts.txt.",
            missing,
            skip,
            ranges));
    for (String script : missing) {
      ranges.remove(script);
    }
    // Groups of scripts that have no code point in common, each a case.
    for (int testNumber = 1; !ranges.isEmpty(); testNumber++) {
      SortedSet<String> group = new TreeSet<>();
      group.add(ranges.firstKey());
      nextScript:
      for (String script : ranges.keySet()) {
        for (String grouped : group) {
          if (overlapping.get(grouped).contains(script)) {
            continue nextScript;
          }
        }
        group.add(script);
      }
      cases.add(
          create(
              version,
              namePrefix + testNumber,
              "Tests character class syntax of the Unicode "
                  + dotVersion
                  + " "
                  + PROPERTY
                  + " property.",
              group,
              skip,
              ranges));
      for (String script : group) {
        ranges.remove(script);
      }
    }
    return cases;
  }

  /** The case of a group of scripts, whose scanner prints only the code points of these scripts. */
  private static GeneratedCase create(
      Version version,
      String name,
      String description,
      SortedSet<String> scripts,
      Set<String> skip,
      SortedMap<String, List<Range>> ranges) {
    StringBuilder rules = new StringBuilder("<<EOF>> { printOutput(); return 1; }\n");
    List<Range> output = new ArrayList<>();
    for (String script : scripts) {
      if (skip.contains(script)) {
        continue;
      }
      String label = EnumeratedPropertyCases.label(PROPERTY, script);
      rules.append(EnumeratedPropertyCases.rule(label));
      List<Range> scriptRanges = ranges.get(script);
      if (scriptRanges != null) {
        for (Range range : scriptRanges) {
          output.add(range.withValue(label));
        }
      }
    }
    rules.append("[^] { }\n");
    return new GeneratedCase(
        CASE_DIR,
        name,
        version,
        description,
        GeneratedCase.Include.ENUMERATED_DEFINED_VALUES_ONLY,
        rules.toString(),
        Ranges.sortByStart(output));
  }

  /** Maps the aliases of the scripts in {@code PropertyValueAliases.txt} to their long names. */
  private static Map<String, String> scriptAliases(File file) throws IOException {
    Map<String, String> canonical = new HashMap<>();
    for (String line : UcdText.lines(file)) {
      String[] fields = UcdText.stripComment(line).split(";");
      if (fields.length < 3 || !fields[0].trim().equals("sc")) {
        continue;
      }
      String script = fields[2].trim();
      for (String field : fields) {
        canonical.put(field.trim(), script);
      }
    }
    return canonical;
  }

  private static void add(Map<String, List<Range>> ranges, String script, Range range) {
    List<Range> list = ranges.get(script);
    if (list == null) {
      list = new ArrayList<>();
      ranges.put(script, list);
    }
    list.add(range);
  }

  private ScriptsCases() {}
}
