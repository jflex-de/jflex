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
package de.jflex.migration.unicodedatatest;

import static de.jflex.migration.unicodedatatest.JavaResources.readResource;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import de.jflex.velocity.Velocity;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.velocity.runtime.parser.ParseException;

/** Generates the flex of the scanners for a all ages of a given Unicode version. */
public class UnicodeAge extends AbstractGenerator {

  private static final String FLEX_FILE_TEMPLATE = ROOT_DIR + "/UnicodeAge.flex.vm";

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final Output output;

  public UnicodeAge(Output output) {
    this.output = output;
  }

  @Override
  void generate(Path outDir) throws IOException, ParseException {
    ImmutableList<Version> ages = olderAges(output.version());
    for (Version age : ages) {
      generateAge(age, outDir);
    }
  }

  private void generateAge(Version age, Path outDir) throws IOException, ParseException {
    UnicodeAgeTemplateVars vars = createFlexTemplateVars(age);
    Path outFile = outDir.resolve(vars.className + ".flex");
    logger.atInfo().log("Generating %s", outFile);
    Velocity.render(readResource(FLEX_FILE_TEMPLATE), "FlexFile", vars, outFile.toFile());
  }

  private UnicodeAgeTemplateVars createFlexTemplateVars(Version age) {
    UnicodeAgeTemplateVars vars = new UnicodeAgeTemplateVars();
    vars.javaPackage = output.javaPackage();
    vars.className =
        String.format(
            "UnicodeAge_%s_age_%s", output.version().underscoreVersion(), age.underscoreVersion());
    vars.unicodeVersion = output.version();
    vars.age = age;
    return vars;
  }
}
