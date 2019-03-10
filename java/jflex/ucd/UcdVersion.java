package jflex.ucd;

import java.io.IOException;

public interface UcdVersion {
  /**
   * Fetches and parses the data files defined for this Unicode versionMajorMinor.
   *
   * @throws IOException If there is a problem fetching or parsing any of this versionMajorMinor's
   *     data files.
   */
  // TODO(regisd) Split the fetch and the parse parts. The Bazel impl doesn't fetch.
  void fetchAndParseDataFiles() throws IOException;

  void addCompatibilityProperties();

  /** Unicode versionMajorMinor in the form {@code X.Y}. */
  String getMajorMinorVersion();
}
