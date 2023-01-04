/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testderivedcoreprop;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.SimpleDerivedCorePropertiesParser;
import de.jflex.ucd.CodePointRanges;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DerivedCorePropertiesTestGenerator {

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);

    String propertyName = "Alphabetic";
    String testName =
        String.format(
            "UnicodeDerivedCoreProperties_%s_%s", propertyName, version.underscoreVersion());
    createFlexGenerator(version, testName, propertyName).generate(outDir);

    List<String> files = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
    UcdVersion ucd = UcdVersion.findUcdFiles(version.version(), files);
    createGoldenGenerator(version, testName, ucd, propertyName).generate(outDir);

    createJavaTestGenerator(version).generate(outDir);
  }

  private static UnicodePropertyFlexGenerator<Boolean> createFlexGenerator(
      UnicodeVersion version, String testName, String propertyName) {
    return UnicodePropertyFlexGenerator.createPropertyScanner(version, testName, propertyName);
  }

  private static UnicodeDerivedPropertyGoldenGenerator createGoldenGenerator(
      UnicodeVersion version, String testName, UcdVersion ucd, String propertyName)
      throws IOException {
    Path derivedCorePropFile = ucd.getFile(UcdFileType.DerivedCoreProperties).toPath();
    ImmutableList<CodepointRange> derivedCoreProperties =
        SimpleDerivedCorePropertiesParser.parseProperties(derivedCorePropFile).stream()
            .filter(b -> propertyName.equals(b.name()))
            .map(NamedCodepointRange::range)
            .collect(toImmutableList());
    derivedCoreProperties = CodePointRanges.merge(derivedCoreProperties);
    return new UnicodeDerivedPropertyGoldenGenerator(version, testName, derivedCoreProperties);
  }

  private static UnicodeDerivedCorePropertiesTestGenerator createJavaTestGenerator(
      UnicodeVersion unicodeVersion) {
    return new UnicodeDerivedCorePropertiesTestGenerator(unicodeVersion);
  }

  private DerivedCorePropertiesTestGenerator() {}
}
