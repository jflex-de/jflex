/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.version.Version;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A test case of the testsuite: a {@code .test} file, the {@code .flex} spec, the {@code .output}
 * golden of the scanner, and the {@code -flex.output} golden of JFlex's messages.
 *
 * <p>The scanner reads one of the inputs in {@code testsuite/testcases/src/test/resources} and
 * prints the ranges of code points with the same property value.
 */
final class GeneratedCase {

  /** The code that the spec includes from {@code testsuite/testcases/src/test/resources}. */
  enum Include {
    /** Prints every code point with the value set for it, or null. */
    ENUMERATED("enumerated-property"),
    /** Prints only the code points that have a value. */
    ENUMERATED_DEFINED_VALUES_ONLY("enumerated-property-defined-values-only"),
    /** Prints the code points that have the property. */
    BINARY("binary-property");

    private final String kind;

    Include(String kind) {
      this.kind = kind;
    }

    /** The file name; the versions before Unicode 3.0 use the ones for the BMP. */
    String fileName(boolean bmp) {
      return "common-unicode-" + (bmp ? "" : "all-") + kind + "-java";
    }
  }

  private static final Version UNICODE_3_0 = new Version(3, 0);

  /** The directory of the case, e.g. {@code unicode-line-break}. */
  final String caseDir;

  /** The name of the case and its files, e.g. {@code UnicodeLineBreak_12_1}. */
  final String name;

  final Version version;
  final String description;
  final Include include;

  /** The rules section of the spec. */
  final String rules;

  /** The ranges that the scanner prints. */
  final List<Range> output;

  /** The messages of JFlex, usually none. */
  String jflexOutput = "";

  GeneratedCase(
      String caseDir,
      String name,
      Version version,
      String description,
      Include include,
      String rules,
      List<Range> output) {
    this.caseDir = caseDir;
    this.name = name;
    this.version = version;
    this.description = description;
    this.include = include;
    this.rules = rules;
    this.output = output;
  }

  /**
   * Whether the version has only the Basic Multilingual Plane. The cases of these versions scan the
   * BMP input, which ends at U+FFFD.
   */
  static boolean isBmp(Version version) {
    return Version.MAJOR_MINOR_COMPARATOR.compare(version, UNICODE_3_0) < 0;
  }

  /** The version as in the case names, e.g. {@code 12_1}. */
  static String underscoreVersion(Version version) {
    return version.getMajor() + "_" + version.getMinor();
  }

  /** The version as in the {@code %unicode} directive, e.g. {@code 12.1}. */
  static String dotVersion(Version version) {
    return version.toMajorMinorString();
  }

  /** Writes the four files of the case into its directory below {@code casesDir}. */
  void write(Path casesDir) throws IOException {
    Path dir = casesDir.resolve(caseDir);
    Files.createDirectories(dir);
    boolean bmp = isBmp(version);
    String input = bmp ? "All.Unicode.BMP.characters.input" : "All.Unicode.characters.input";
    String test =
        "name: "
            + name
            + "\n\ndescription: \n"
            + description
            + "\n\njflex: -q\n\ninput-file-encoding: UTF-8\n\ncommon-input-file: ../../resources/"
            + input
            + "\n\n";
    String flex =
        "%%\n\n%unicode "
            + dotVersion(version)
            + "\n%public\n%class "
            + name
            + "\n\n%type int\n%standalone\n\n%include ../../resources/"
            + include.fileName(bmp)
            + "\n\n%%\n\n"
            + rules;
    int max = bmp ? Ranges.MAX_BMP_CODE_POINT : Ranges.MAX_CODE_POINT;
    StringBuilder golden = new StringBuilder();
    for (Range range : Ranges.upTo(Ranges.withoutSurrogates(output), max)) {
      golden.append(range.format()).append('\n');
    }
    write(dir.resolve(name + ".test"), test);
    write(dir.resolve(name + ".flex"), flex);
    write(dir.resolve(name + ".output"), golden.toString());
    write(dir.resolve(name + "-flex.output"), jflexOutput);
  }

  /** Writes ASCII text, with {@code \n} line ends whatever the platform. */
  static void write(Path file, String content) throws IOException {
    Files.write(file, content.getBytes(StandardCharsets.ISO_8859_1));
  }
}
