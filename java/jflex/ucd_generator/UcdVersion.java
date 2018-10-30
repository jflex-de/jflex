package jflex.ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;

public class UcdVersion {

  final String version;
  final ImmutableMap<UcdFileType, File> files;

  UcdVersion(String version, ImmutableMap<UcdFileType, File> files) {
    this.version = version;
    this.files = files;
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    ImmutableMap.Builder<UcdFileType, File> files = ImmutableMap.builder();
    private String version;

    Builder withVersion(String version) {
      this.version = version;
      return this;
    }

    Builder putFile(UcdFileType unicodeFileType, File file) {
      files.put(unicodeFileType, file);
      return this;
    }

    public UcdVersion build() {
      return new UcdVersion(version, files.build());
    }
  }
}
