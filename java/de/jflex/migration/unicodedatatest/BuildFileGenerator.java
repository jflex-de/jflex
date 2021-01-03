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

import static de.jflex.migration.util.JavaResources.readResource;

import com.google.common.flogger.FluentLogger;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.velocity.runtime.parser.ParseException;

class BuildFileGenerator extends AbstractGenerator {

  private static final String ROOT_DIR = JavaPackageUtils.getPathForClass(BuildFileGenerator.class);
  private static final String BUILD_FILE_TEMPLATE = ROOT_DIR + "/BUILD.vm";

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final UnicodeVersion out;

  public BuildFileGenerator(UnicodeVersion out) {
    this.out = out;
  }

  @Override
  public void generate(Path outDir) throws IOException, ParseException {
    BuildFileTemplateVars vars = createBuildTemplateVars(out);
    Path outFile = outDir.resolve("BUILD.bazel");
    logger.atInfo().log("Generating %s", outFile);
    Velocity.render(readResource(BUILD_FILE_TEMPLATE), "BuildFile", vars, outFile.toFile());
  }

  private static BuildFileTemplateVars createBuildTemplateVars(UnicodeVersion out) {
    BuildFileTemplateVars vars = new BuildFileTemplateVars();
    vars.underscoreVersion = out.underscoreVersion();
    return vars;
  }

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path workspaceDir = Paths.get(args[1]);
    Path testDir = workspaceDir.resolve("javatests").resolve(version.javaPackageDirectory());
    Files.createDirectories(testDir);
    new BuildFileGenerator(version).generate(testDir);
  }
}
