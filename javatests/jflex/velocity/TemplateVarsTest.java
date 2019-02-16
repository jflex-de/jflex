/*
 * Copyright (C) 2018 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
