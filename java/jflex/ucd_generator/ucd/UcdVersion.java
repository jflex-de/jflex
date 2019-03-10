/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
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
package jflex.ucd_generator.ucd;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;

/** Describes a single Unicode versionMajorMinor. */
public class UcdVersion implements jflex.ucd.UcdVersion {

  private final Version version;
  private final String versionMajorMinor;
  private final String versionMajorMinorPatch;
  private final String unicodeClassName;
  final ImmutableMap<jflex.ucd_generator.ucd.UcdFileType, File> files;

  UcdVersion(Version version, ImmutableMap<UcdFileType, File> files) {
    this.version = version;
    this.versionMajorMinor = version.toMajorMinorString();
    this.versionMajorMinorPatch = version.toMajorMinorPatchString();
    this.unicodeClassName = "Unicode_" + versionMajorMinor.replace(',', '_');
    this.files = files;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Version getVersion() {
    return new Version(versionMajorMinor);
  }

  @Override
  public void fetchAndParseDataFiles() throws IOException {
    // TODO(regisd) Implement the parser
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public void addCompatibilityProperties() {
    // TODO(regisd) Implement
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public String getMajorMinorVersion() {
    return versionMajorMinor;
  }

  @Override
  public String getMajorMinorPatchVersion() {
    return versionMajorMinorPatch;
  }

  @Override
  public String getUnicodeClassName() {
    return "Unicode_" + versionMajorMinor.replace('.', '_');
  }

  public static class Builder {
    private ImmutableMap.Builder<jflex.ucd_generator.ucd.UcdFileType, File> files =
        ImmutableMap.builder();
    private Version version;

    public Builder withVersion(Version version) {
      this.version = version;
      return this;
    }

    public Builder putFile(UcdFileType unicodeFileType, File file) {
      files.put(unicodeFileType, file);
      return this;
    }

    public UcdVersion build() {
      return new UcdVersion(version, files.build());
    }
  }
}
