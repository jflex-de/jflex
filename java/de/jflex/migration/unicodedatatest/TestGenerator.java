/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.TemplateVars;
import de.jflex.velocity.Velocity;
import de.jflex.version.Version;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.velocity.runtime.parser.ParseException;

/** Generates a unicode age test. */
public class TestGenerator {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String ROOT_DIR = JavaPackageUtils.getPathForClass(TestGenerator.class);
  private static final String UNICODE_AGE_TEST_TEMPLATE = ROOT_DIR + "/UnicodeAgeTest_x_y.java.vm";
  private static final String BUILD_FILE_TEMPLATE = ROOT_DIR + "/BUILD.vm";

  private static final ImmutableList<Version> KNOWN_VERSIONS =
      ImmutableList.of(
          new Version(1, 1),
          new Version(2, 0),
          new Version(2, 1),
          new Version(3, 0),
          new Version(3, 1),
          new Version(3, 2),
          new Version(4, 0),
          new Version(4, 1),
          new Version(5, 0),
          new Version(5, 1),
          new Version(5, 2),
          new Version(6, 0),
          new Version(6, 0),
          new Version(6, 1),
          new Version(6, 2),
          new Version(7, 0),
          new Version(8, 0),
          new Version(9, 0),
          new Version(10, 0),
          new Version(11, 0),
          new Version(12, 0),
          new Version(12, 1));

  private TestGenerator() {}

  public static void main(String[] args) throws Exception {
    checkArgument(args.length >= 2, "Syntax error, expected: VERSION WORKSPACE_DIR");
    Version version = new Version(args[0]);
    Path workspaceDir = Paths.get(args[1]);
    generate(version, workspaceDir);
  }

  public static void generate(Version unicodeVersion, Path workspaceDir) throws IOException {
    Output out = Output.create(unicodeVersion);
    Path outDir = workspaceDir.resolve("javatests").resolve(out.javaPackageDirectory());
    Files.createDirectories(outDir);
    generateJavaTest(unicodeVersion, out, outDir);
    generateBuildFile(unicodeVersion, out, outDir);
  }

  private static void generateJavaTest(Version version, Output out, Path outDir)
      throws IOException {
    UnicodeAgeTestTemplateVars templateVars = createUnicodeAgeTemplateVars(out);
    Path outFile = outDir.resolve(templateVars.testClassName + ".java");
    try (OutputStream outputStream = new FileOutputStream(outFile.toFile())) {
      logger.atInfo().log("Generating %s", outFile);
      velocityRenderUnicodeTest(templateVars, outputStream);
    }
  }

  private static UnicodeAgeTestTemplateVars createUnicodeAgeTemplateVars(Output out) {
    UnicodeAgeTestTemplateVars vars = new UnicodeAgeTestTemplateVars();
    vars.unicodeVersion = out.version();
    vars.javaPackage = out.javaPackage();
    vars.javaPackageDir = out.javaPackageDirectory();
    vars.testClassName = "UnicodeAgeTest_" + out.underscoreVersion();
    vars.scannerPrefix = "UnicodeAge_" + out.underscoreVersion() + "_age";
    vars.ages = olderAges(out.version());
    return vars;
  }

  private static void velocityRenderUnicodeTest(
      UnicodeAgeTestTemplateVars vars, OutputStream output) throws IOException {
    velocityRender(vars, output, UNICODE_AGE_TEST_TEMPLATE, "UnicodeAge");
  }

  private static void generateBuildFile(Version unicodeVersion, Output out, Path outDir)
      throws IOException {
    BuildFileTemplateVars vars = createBuildTemplateVars(out);
    Path outFile = outDir.resolve("BUILD.bazel");
    try (OutputStream outputStream = new FileOutputStream(outFile.toFile())) {
      logger.atInfo().log("Generating %s", outFile);
      velocityRenderBuildFile(vars, outputStream);
    }
  }

  private static BuildFileTemplateVars createBuildTemplateVars(Output out) {
    BuildFileTemplateVars vars = new BuildFileTemplateVars();
    vars.baseClassName = "UnicodeAge_" + out.underscoreVersion();
    vars.underscoreVersion = out.underscoreVersion();
    vars.ages = olderAges(out.version());
    return vars;
  }

  private static void velocityRenderBuildFile(BuildFileTemplateVars vars, OutputStream output) {
    velocityRender(vars, output, BUILD_FILE_TEMPLATE, "BuildFile");
  }

  private static void velocityRender(
      TemplateVars vars, OutputStream output, String templateFile, String templateName) {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      Velocity.render(readResource(templateFile), templateName, vars, writer);
    } catch (ParseException | IOException e) {
      throw new RuntimeException("Failed to parse Velocity template " + templateFile, e);
    }
  }

  private static InputStreamReader readResource(String resourceName) {
    InputStream resourceAsStream =
        checkNotNull(
            ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName),
            "Null resource content for " + resourceName);
    return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
  }

  private static ImmutableList<Version> olderAges(Version version) {
    return KNOWN_VERSIONS.stream()
        .filter(v -> Version.EXACT_VERSION_COMPARATOR.compare(v, version) <= 0)
        .collect(toImmutableList());
  }
}
