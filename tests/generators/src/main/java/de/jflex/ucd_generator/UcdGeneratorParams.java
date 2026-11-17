/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator;

import de.jflex.ucd_generator.ucd.UcdVersions;
import java.io.File;
import java.util.Objects;

final class UcdGeneratorParams {
  private final UcdVersions ucdVersions;
  private final File outputDir;

  private UcdGeneratorParams(UcdVersions ucdVersions, File outputDir) {
    this.ucdVersions = Objects.requireNonNull(ucdVersions, "ucdVersions");
    this.outputDir = Objects.requireNonNull(outputDir, "outputDir");
  }

  UcdVersions ucdVersions() {
    return ucdVersions;
  }

  File outputDir() {
    return outputDir;
  }

  @Override
  public String toString() {
    return "UcdGeneratorParams{ucdVersions=" + ucdVersions + ", outputDir=" + outputDir + "}";
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private UcdVersions ucdVersions;
    private File outputDir;

    private Builder() {}

    Builder setUcdVersions(UcdVersions ucdVersions) {
      this.ucdVersions = Objects.requireNonNull(ucdVersions, "ucdVersions");
      return this;
    }

    Builder setOutputDir(File outputDir) {
      this.outputDir = Objects.requireNonNull(outputDir, "outputDir");
      return this;
    }

    /** The output directory, or {@code null} if it has not been set. */
    File outputDir() {
      return outputDir;
    }

    /** Both properties must have been set. */
    UcdGeneratorParams build() {
      return new UcdGeneratorParams(ucdVersions, outputDir);
    }
  }
}
