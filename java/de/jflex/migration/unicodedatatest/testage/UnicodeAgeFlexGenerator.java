/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.migration.unicodedatatest.testage;

import static de.jflex.migration.unicodedatatest.util.JavaResources.readResource;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.flogger.FluentLogger;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.Pair;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.velocity.runtime.parser.ParseException;

/** Generates the flex of the scanners for a all ages of a given Unicode version. */
class UnicodeAgeFlexGenerator extends AbstractGenerator {

  private static final String ROOT_DIR =
      JavaPackageUtils.getPathForClass(UnicodeAgeFlexGenerator.class);
  private static final String FLEX_FILE_TEMPLATE = ROOT_DIR + "/UnicodeAge.flex.vm";
  private static final String FLEX_SUBSTRACTION_FILE_TEMPLATE =
      ROOT_DIR + "/UnicodeAgeSubtraction.flex.vm";

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final Version VERSION_3_1 = new Version(3, 1);

  private final UnicodeVersion unicodeVersion;

  public UnicodeAgeFlexGenerator(UnicodeVersion unicodeVersion) {
    this.unicodeVersion = unicodeVersion;
  }

  @Override
  public void generate(Path outDir) throws IOException, ParseException {
    ImmutableList<Version> ages = olderAges(unicodeVersion.version());
    for (Version age : ages) {
      generateAge(age, outDir);
    }
    generateAgeUnassigned(outDir);
    generateAgeSubstraction(outDir);
  }

  private void generateAge(Version age, Path outDir) throws IOException, ParseException {
    UnicodeAgeFlexTemplateVars vars = createFlexTemplateVars(age);
    Path outFile = outDir.resolve(vars.className + ".flex");
    logger.atInfo().log("Generating %s", outFile);
    Velocity.render(readResource(FLEX_FILE_TEMPLATE), "AgeFlexFile", vars, outFile.toFile());
  }

  private UnicodeAgeFlexTemplateVars createFlexTemplateVars(Version age) {
    UnicodeAgeFlexTemplateVars vars = new UnicodeAgeFlexTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.className =
        String.format(
            "UnicodeAge_%s_age_%s",
            unicodeVersion.version().underscoreVersion(), age.underscoreVersion());
    vars.age = age.toString();
    return vars;
  }

  private void generateAgeUnassigned(Path outDir) throws IOException, ParseException {
    UnicodeAgeFlexTemplateVars vars = createAgeUnassignedTemplateVars();
    Path outFile = outDir.resolve(vars.className + ".flex");
    logger.atInfo().log("Generating %s", outFile);
    Velocity.render(readResource(FLEX_FILE_TEMPLATE), "AgeFlexFile", vars, outFile.toFile());
  }

  private UnicodeAgeFlexTemplateVars createAgeUnassignedTemplateVars() {
    UnicodeAgeFlexTemplateVars vars = new UnicodeAgeFlexTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.className =
        String.format(
            "UnicodeAge_%s_age_%s", unicodeVersion.version().underscoreVersion(), "unassigned");
    vars.age = "Unassigned";
    return vars;
  }

  private void generateAgeSubstraction(Path outDir) throws IOException, ParseException {
    UnicodeAgeSubtractionTemplateVars vars = createFlexSubstractionTemplateVars();
    Path outFile = outDir.resolve(vars.className + ".flex");
    logger.atInfo().log("Generating %s", outFile);
    Velocity.render(
        readResource(FLEX_SUBSTRACTION_FILE_TEMPLATE),
        "AgeSubtractionFlexFile",
        vars,
        outFile.toFile());
  }

  private UnicodeAgeSubtractionTemplateVars createFlexSubstractionTemplateVars() {
    UnicodeAgeSubtractionTemplateVars vars = new UnicodeAgeSubtractionTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.className =
        String.format(
            "UnicodeAge_%s_age_subtraction", unicodeVersion.version().underscoreVersion());
    ImmutableList<Version> ages = olderAges(unicodeVersion.version());
    Stream<Pair<Version>> agePairs =
        Streams.zip(ages.stream(), ages.stream().skip(1), Pair::create);
    vars.ages = agePairs.collect(ImmutableList.toImmutableList());
    return vars;
  }
}
