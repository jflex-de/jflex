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
package jflex.velocity;

import static com.google.common.truth.Truth.assertThat;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

public class TemplateVarsTest {

  private Foo foo;

  @Before
  public void createFoo() {
    foo = new Foo();
  }

  @Test
  public void toVelocityContext_notSet() {
    assertThrows(NullPointerException.class, () -> foo.toVelocityContext());
  }

  @Test
  public void toVelocityContext() {
    foo.bar = "hello";
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("bar")).isEqualTo("hello");
  }

  @Test
  public void toVelocityContext_nonPublicField() {
    foo.bar = "ignored";
    VelocityContext context = foo.toVelocityContext();
    assertThat(context.get("secret")).isNull();
  }

  static class Foo extends TemplateVars {
    @SuppressWarnings("WeakerAccess") // Only public fields are exposed to the template.
    public String bar;

    @SuppressWarnings("unused") // unused, but that's for test
    String secret;
  }
}
