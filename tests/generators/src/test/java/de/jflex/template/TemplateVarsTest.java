/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TemplateVarsTest {

  private Foo foo;

  @BeforeEach
  public void createFoo() {
    foo = new Foo();
    foo.templateName = "TemplateVarsTest";
  }

  @Test
  public void toMap_notSet() {
    assertThrows(NullPointerException.class, () -> foo.toMap());
  }

  @Test
  public void toMap() {
    foo.bar = "hello";
    foo.number = 42;
    Map<String, String> values = foo.toMap();
    assertEquals("hello", values.get("bar"));
    assertEquals("42", values.get("number"));
    assertEquals("TemplateVarsTest", values.get("templateName"));
  }

  @Test
  public void toMap_nonPublicField() {
    foo.bar = "ignored";
    assertFalse(foo.toMap().containsKey("secret"));
  }

  static class Foo extends TemplateVars {
    @SuppressWarnings("WeakerAccess") // Only public fields are exposed to the template.
    public String bar;

    @SuppressWarnings("WeakerAccess")
    public int number;

    @SuppressWarnings("unused") // unused, but that's for test
    String secret;
  }
}
