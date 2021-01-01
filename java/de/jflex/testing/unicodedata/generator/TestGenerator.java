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
package de.jflex.testing.unicodedata.generator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.flogger.FluentLogger;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
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

/**
 * Generates a unicode age test.
 */
public class TestGenerator {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String ROOT_DIR = JavaPackageUtils.getPathForClass(TestGenerator.class);
  private static final String UNICODE_AGE_TEST_TEMPLATE = ROOT_DIR + "/UnicodeAgeTest_x_y.java.vm";

  private TestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeAgeTestTemplateVars templateVars = createUnicodeAgeTemplateVars();
    Path outDir = Paths.get("javatests", templateVars.javaPackageDir.toString());
    Files.createDirectories(outDir);
    Path outFile = outDir.resolve(
        templateVars.testClassName + ".java");
    try (OutputStream outputStream = new FileOutputStream(outFile.toFile())) {
      logger.atInfo().log("Generating %s", outFile);
      velocityRenderUnicodeTest(templateVars, outputStream);
    }
  }

  private static void velocityRenderUnicodeTest(UnicodeAgeTestTemplateVars templateVars,
      OutputStream output) throws IOException {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      Velocity.render(readResource(UNICODE_AGE_TEST_TEMPLATE), "UnicodeAge", templateVars, writer);
    } catch (ParseException e) {
      throw new RuntimeException("Failed to parse Velocity template " + UNICODE_AGE_TEST_TEMPLATE, e);
    }
  }

  private static UnicodeAgeTestTemplateVars createUnicodeAgeTemplateVars() {
    return new UnicodeAgeTestTemplateVars();
  }

  private static InputStreamReader readResource(String resourceName) {
    InputStream resourceAsStream =
        checkNotNull(
            ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName),
            "Null resource content for " + resourceName);
    return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
  }
}