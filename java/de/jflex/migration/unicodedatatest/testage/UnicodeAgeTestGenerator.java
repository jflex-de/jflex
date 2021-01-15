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
package de.jflex.migration.unicodedatatest.testage;

import static de.jflex.ucd.Versions.VERSION_3_1;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.version.Version;
import java.nio.file.Paths;

class UnicodeAgeTestGenerator extends AbstractGenerator<UnicodeAgeTestTemplateVars> {

  private final ImmutableList<Version> ages;

  public UnicodeAgeTestGenerator(UnicodeVersion unicodeVersion, ImmutableList<Version> ages) {
    super("UnicodeAgeTest_x_y.java", unicodeVersion);
    this.ages = ages;
  }

  @Override
  protected UnicodeAgeTestTemplateVars createTemplateVars() {
    UnicodeAgeTestTemplateVars vars = new UnicodeAgeTestTemplateVars();
    vars.dataset = getDataset(unicodeVersion.version());
    vars.className = "UnicodeAgeTest_" + unicodeVersion.underscoreVersion();
    vars.scannerPrefix = "UnicodeAge_" + unicodeVersion.underscoreVersion() + "_age";
    vars.javaPackageDir =
        Paths.get(JavaPackageUtils.getPathForPackage(unicodeVersion.javaPackage()));
    vars.ages = ages;
    return vars;
  }

  private static Dataset getDataset(Version version) {
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, VERSION_3_1) < 0) {
      return Dataset.BMP;
    } else {
      return Dataset.ALL;
    }
  }

  @Override
  protected String getOuputFileName(UnicodeAgeTestTemplateVars vars) {
    return String.format("UnicodeAgeTest_%s.java", unicodeVersion.underscoreVersion());
  }
}
