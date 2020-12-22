package de.jflex.ucd_generator;

import com.google.auto.value.AutoValue;
import de.jflex.ucd_generator.ucd.UcdVersions;
import java.io.File;

@AutoValue
abstract class UcdGeneratorParams {
  abstract UcdVersions ucdVersions();

  abstract File outputDir();

  static Builder builder() {
    return new AutoValue_UcdGeneratorParams.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setUcdVersions(UcdVersions ucdVersions);

    abstract Builder setOutputDir(File outputDir);

    abstract File outputDir();

    abstract UcdGeneratorParams build();
  }
}
