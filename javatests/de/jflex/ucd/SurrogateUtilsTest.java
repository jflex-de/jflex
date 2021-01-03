/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
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
package de.jflex.ucd;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.ucd.SurrogateUtils.containsSurrogate;
import static de.jflex.ucd.SurrogateUtils.isSurrogateProperty;
import static de.jflex.ucd.SurrogateUtils.removeSurrogates;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

/** Test for {@link SurrogateUtils}. */
public class SurrogateUtilsTest {
  @Test
  public void isSurrogate_not() {
    assertThat(isSurrogateProperty("block=generalcategory")).isFalse();
  }

  @Test
  public void isSurrogate_sc() {
    assertThat(isSurrogateProperty("cs")).isTrue();
    assertThat(isSurrogateProperty("csxxx")).isFalse();
    assertThat(isSurrogateProperty("xxxcs")).isFalse();
  }

  @Test
  public void isSurrogate_surrogate() {
    assertThat(isSurrogateProperty("block=surrogate")).isTrue();
    assertThat(isSurrogateProperty("block=hightprivateusesurrogates")).isTrue();
  }

  @Test
  public void removeSurrogate_surrogates() {
    assertThat(removeSurrogates(0xd83f, 0xdffe)).isEmpty();
  }

  @Test
  public void removeSurrogate_notSurrogates() {
    assertThat(removeSurrogates(0x0000, 0x01f5))
        .containsAllIn(ImmutableList.of(CodepointRange.create(0x0000, 0x01f5)));
  }

  @Test
  public void containsSurrogates_before() {
    assertThat(containsSurrogate(CodepointRange.create(0x0000, 0x01f5))).isFalse();
  }

  @Test
  public void containsSurrogates_within() {
    assertThat(containsSurrogate(CodepointRange.create(0xD801, 0xD802))).isTrue();
    assertThat(containsSurrogate(CodepointRange.create(0xd83f, 0xdffe))).isTrue();
  }

  @Test
  public void containsSurrogates_endOverlaps() {
    assertThat(containsSurrogate(CodepointRange.create(0x0000, 0xD802))).isTrue();
  }

  @Test
  public void containsSurrogates_after() {
    assertThat(containsSurrogate(CodepointRange.create(0xE0000, 0xE0001))).isFalse();
    assertThat(containsSurrogate(CodepointRange.create(0xE0000, 0xF8FF))).isFalse();
  }

  @Test
  public void containsSurrogates_larger() {
    assertThat(containsSurrogate(CodepointRange.create(0x20ad, 0xfffb))).isTrue();
  }
}
