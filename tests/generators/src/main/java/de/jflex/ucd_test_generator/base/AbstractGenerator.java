/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.base;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static de.jflex.ucd_test_generator.base.JavaResources.readResource;

import com.google.common.collect.ImmutableList;
import de.jflex.template.Template;
import de.jflex.ucd.UcdReleases;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.version.Version;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractGenerator<T extends UnicodeVersionTemplateVars> {

  /** The Unicode versions of {@link UcdReleases}, without their update numbers, oldest first. */
  public static final ImmutableList<Version> KNOWN_VERSIONS =
      UcdReleases.versions().stream()
          .map(v -> new Version(v.getMajor(), v.getMinor()))
          .collect(toImmutableList());

  protected final String templateName;
  protected final UnicodeVersion unicodeVersion;

  protected AbstractGenerator(String templateName, UnicodeVersion unicodeVersion) {
    this.templateName = templateName;
    this.unicodeVersion = unicodeVersion;
  }

  public static ImmutableList<Version> olderAges(Version version) {
    return KNOWN_VERSIONS.stream()
        .filter(v -> Version.EXACT_VERSION_COMPARATOR.compare(v, version) <= 0)
        .collect(toImmutableList());
  }

  /** Returns the generated file. */
  public Path generate(Path outDir) throws IOException {
    T vars = createTemplateVars();
    vars.updateFrom(unicodeVersion);
    vars.templateName = templateName;

    Path javaPackageOutDir = outDir.resolve(unicodeVersion.javaPackageDirectory());
    Files.createDirectories(javaPackageOutDir);
    Path outFile = javaPackageOutDir.resolve(getOuputFileName(vars));
    String content;
    try {
      content = render(vars);
    } catch (Exception e) {
      throw new RuntimeException("Error rendering '" + templateName + "' with " + vars, e);
    }
    try (OutputStream out = Files.newOutputStream(outFile);
        Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
      writer.write(content);
    }
    return outFile;
  }

  /** Returns the content of the generated file: by default, the template filled in with vars. */
  protected String render(T vars) throws IOException {
    InputStreamReader templateReader;
    try {
      templateReader = readResourceTemplate();
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(
          "Could not read template in java resources for template", e);
    }
    return Template.render(Template.read(templateReader), vars);
  }

  /** Reads the template from base or from the test package. */
  private InputStreamReader readResourceTemplate() {
    try {
      return readResource(
          Paths.get(JavaPackageUtils.getPathForClass(AbstractGenerator.class))
              .resolve(templateName + ".template")
              .toString());
    } catch (NullPointerException e) {
      return readResource(
          Paths.get(JavaPackageUtils.getPathForClass(this.getClass()))
              .resolve(templateName + ".template")
              .toString());
    }
  }

  protected abstract T createTemplateVars();

  protected abstract String getOuputFileName(T vars);
}
