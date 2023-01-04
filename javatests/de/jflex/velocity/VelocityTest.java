/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.velocity;

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
    templateVars.templateName = "VELOCITY_TEST_TEMPLATE";
    Writer writer = new StringWriter();
    Velocity.render(template, "HelloWorld", templateVars, writer);
    assertThat(writer.toString()).isEqualTo("Hello World!");
  }

  static class HelloWordVars extends TemplateVars {
    public String name = "World";
  }
}
