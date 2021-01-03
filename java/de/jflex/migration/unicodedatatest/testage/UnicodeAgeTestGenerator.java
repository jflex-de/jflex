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

import com.google.common.flogger.FluentLogger;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import org.apache.velocity.runtime.parser.ParseException;

class UnicodeAgeTestGenerator extends AbstractGenerator {

  private static final String ROOT_DIR =
      JavaPackageUtils.getPathForClass(UnicodeAgeTestGenerator.class);
  private static final String UNICODE_AGE_TEST_TEMPLATE = ROOT_DIR + "/UnicodeAgeTest_x_y.java.vm";

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final UnicodeVersion out;

  public UnicodeAgeTestGenerator(UnicodeVersion out) {
    this.out = out;
  }

  @Override
  public void generate(Path outDir) throws IOException, ParseException {
    UnicodeAgeTestTemplateVars templateVars = createUnicodeAgeTemplateVars(out);
    Path outFile = outDir.resolve(templateVars.className + ".java");
    try (OutputStream outputStream = new FileOutputStream(outFile.toFile())) {
      logger.atInfo().log("Generating %s", outFile);
      Velocity.render(
          readResource(UNICODE_AGE_TEST_TEMPLATE), "UnicodeAge", templateVars, outputStream);
    }
  }

  private static UnicodeAgeTestTemplateVars createUnicodeAgeTemplateVars(UnicodeVersion out) {
    UnicodeAgeTestTemplateVars vars = new UnicodeAgeTestTemplateVars();
    vars.updateFrom(out);
    vars.javaPackageDir = out.javaPackageDirectory();
    vars.className = "UnicodeAgeTest_" + out.underscoreVersion();
    vars.scannerPrefix = "UnicodeAge_" + out.underscoreVersion() + "_age";
    vars.ages = olderAges(out.version());
    Dataset dataset = UnicodeDataScanners.getDataset(out.version());
    vars.dataset = dataset;
    return vars;
  }
}
