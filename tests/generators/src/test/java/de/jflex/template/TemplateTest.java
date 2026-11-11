/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TemplateTest {
  @Test
  public void testRendering() throws IOException {
    StringReader template = new StringReader("Hello ${name}!");
    TemplateVars templateVars = new HelloWordVars();
    templateVars.templateName = "TEST_TEMPLATE";
    Writer writer = new StringWriter();
    Template.render(template, templateVars, writer);
    assertEquals("Hello World!", writer.toString());
  }

  @Test
  public void repeatedAndAdjacent() {
    assertEquals("a-ab-b", Template.render("${x}-${x}${y}-${y}", vars("x", "a", "y", "b")));
  }

  @Test
  public void otherDollarsUnchanged() {
    assertEquals("$x ${ $ {x}", Template.render("$x ${ $ {x}", vars("x", "x")));
  }

  @Test
  public void noEscapes() {
    assertEquals("\\a", Template.render("\\${x}", vars("x", "a")));
  }

  @Test
  public void specialCharactersInValue() {
    assertEquals("a$1\\b", Template.render("${x}", vars("x", "a$1\\b")));
  }

  @Test
  public void unknownVariable() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Template.render("${unknown}", Collections.<String, String>emptyMap()));
  }

  @Test
  public void unknownVariable_namesTemplate() {
    TemplateVars templateVars = new HelloWordVars();
    templateVars.templateName = "TEST_TEMPLATE";
    IllegalArgumentException e =
        assertThrows(IllegalArgumentException.class, () -> Template.render("${nam}", templateVars));
    assertEquals("Unknown template variable ${nam} in template TEST_TEMPLATE", e.getMessage());
  }

  private static Map<String, String> vars(String... keysAndValues) {
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i + 1 < keysAndValues.length; i += 2) {
      map.put(keysAndValues[i], keysAndValues[i + 1]);
    }
    return map;
  }

  static class HelloWordVars extends TemplateVars {
    public String name = "World";
  }
}
