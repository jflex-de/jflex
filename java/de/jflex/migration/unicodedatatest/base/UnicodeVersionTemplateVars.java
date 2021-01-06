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
package de.jflex.migration.unicodedatatest.base;

import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.velocity.TemplateVars;
import de.jflex.version.Version;

public abstract class UnicodeVersionTemplateVars extends TemplateVars {
  /** The class name produced by this Java template. */
  public String className;

  /** java package with '.', used by the scanner. */
  public String javaPackage;

  /** The unicode version under test. */
  public Version unicodeVersion;
  /** The maximum codepoint for this Unicode version. */
  public int maxCodePoint;

  /** The dataset to use, e.g. {@code Ages.Dataset.BMP} */
  public UnicodeDataScanners.Dataset dataset;

  public void updateFrom(UnicodeVersion version) {
    javaPackage = version.javaPackage();
    unicodeVersion = version.version();
    maxCodePoint = AbstractGenerator.getMaxCodePoint(version.version());
    dataset = UnicodeDataScanners.getDataset(version.version());
  }
}
