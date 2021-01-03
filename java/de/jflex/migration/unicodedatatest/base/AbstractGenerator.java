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

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.velocity.runtime.parser.ParseException;

public abstract class AbstractGenerator {

  private static final Version VERSION_3_1 = new Version(3, 1);

  // TODO(regisd) Add This in UnicodeProperties
  private static final ImmutableList<Version> KNOWN_VERSIONS =
      ImmutableList.of(
          new Version(1, 1),
          new Version(2, 0),
          new Version(2, 1),
          new Version(3, 0),
          new Version(3, 1),
          new Version(3, 2),
          new Version(4, 0),
          new Version(4, 1),
          new Version(5, 0),
          new Version(5, 1),
          new Version(5, 2),
          new Version(6, 0),
          new Version(6, 1),
          new Version(6, 2),
          new Version(6, 3),
          new Version(7, 0),
          new Version(8, 0),
          new Version(9, 0),
          new Version(10, 0),
          new Version(11, 0),
          new Version(12, 0),
          new Version(12, 1));

  protected static ImmutableList<Version> olderAges(Version version) {
    return KNOWN_VERSIONS.stream()
        .filter(v -> Version.EXACT_VERSION_COMPARATOR.compare(v, version) <= 0)
        .collect(toImmutableList());
  }

  protected static int getMaxCodePoint(Version version) {
    boolean oldVersion = Version.MAJOR_MINOR_COMPARATOR.compare(version, VERSION_3_1) < 0;
    return oldVersion ? 0xFFFD : 0x10FFFF;
  }

  protected abstract void generate(Path outDir) throws IOException, ParseException;
}
