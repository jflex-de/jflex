/*
 * Copyright (C) 2019-2020 Google, LLC.
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
package de.jflex.version;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;

/** Test for {@link Version}. */
public class VersionTest {

  @Test
  public void version_major() {
    Version v = new Version("42");
    assertThat(v.major).isEqualTo(42);
    assertThat(v.minor).isEqualTo(-1);
    assertThat(v.patch).isEqualTo(-1);
  }

  @Test
  public void version_major_minor() {
    Version v = new Version("1.2");
    assertThat(v.major).isEqualTo(1);
    assertThat(v.minor).isEqualTo(2);
    assertThat(v.patch).isEqualTo(-1);
  }

  @Test
  public void version_major_patch() {
    Version v = new Version("1.2.3");
    assertThat(v.major).isEqualTo(1);
    assertThat(v.minor).isEqualTo(2);
    assertThat(v.patch).isEqualTo(3);
  }

  @Test
  public void compare_eaxact() {
    ImmutableSortedSet<Version> versions =
        ImmutableSortedSet.<Version>orderedBy(Version.EXACT_VERSION_COMPARATOR)
            .add(new Version("1.2.1"))
            .add(new Version("1.2.0"))
            .add(new Version("1.0"))
            .add(new Version("10.0"))
            .build();
    assertThat(versions)
        .containsExactly(
            new Version(1, 0), new Version(1, 2, 0), new Version(1, 2, 1), new Version(10, 0))
        .inOrder();
  }

  @Test
  public void compare_majorMinor() {
    ImmutableSortedSet<Version> versions =
        ImmutableSortedSet.<Version>orderedBy(Version.MAJOR_MINOR_COMPARATOR)
            .add(new Version("1.2.1"))
            .add(new Version("1.2.0"))
            .add(new Version("1.0"))
            .add(new Version("10.0"))
            .build();
    assertThat(versions)
        .containsExactly(new Version(1, 0), new Version(1, 2), new Version(10, 0))
        .inOrder();
  }
}
