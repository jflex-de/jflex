/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.template;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** Template variables: each public field is available in the template by its name. */
public abstract class TemplateVars {

  /** Name of the template itself. */
  public String templateName;

  /** Returns the public fields of this object by name, each converted with {@code toString()}. */
  public Map<String, String> toMap() {
    Map<String, String> values = new HashMap<>();
    for (Field field : getClass().getFields()) {
      Object value = fieldValue(field, this);
      if (value == null) {
        throw new NullPointerException(
            "Field cannot be null. Make sure it is public and set: " + field);
      }
      Object old = values.put(field.getName(), value.toString());
      if (old != null) {
        throw new IllegalArgumentException("Two fields have the same name: " + field.getName());
      }
    }
    return values;
  }

  private static Object fieldValue(Field field, Object container) {
    try {
      return field.get(container);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
