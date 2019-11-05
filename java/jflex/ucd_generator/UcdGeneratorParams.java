package jflex.ucd_generator;

import com.google.auto.value.AutoValue;
import java.io.File;
import jflex.ucd_generator.ucd.UcdVersions;

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

    abstract UcdGeneratorParams build();
  }
}
