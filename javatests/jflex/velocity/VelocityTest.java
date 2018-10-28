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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.velocity.runtime.parser.ParseException;
import org.junit.Test;

public class VelocityTest {
  @Test
  public void testRendering() throws IOException, ParseException {
    StringReader template = new StringReader("Hello $name!");
    TemplateVars templateVars = new HelloWordVars();
    Writer writer = new StringWriter();
    Velocity.render(template, "HelloWorld", templateVars, writer);
    assertThat(writer.toString()).isEqualTo("Hello World!");
  }

  static class HelloWordVars extends TemplateVars {
    public String name = "World";
  }
}
