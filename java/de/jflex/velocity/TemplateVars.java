/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.velocity;

import java.lang.reflect.Field;
import org.apache.velocity.VelocityContext;

public abstract class TemplateVars {

  /** Name of the template itself. Set by the Velocity engine. */
  public String templateName;

  public VelocityContext toVelocityContext() {
    VelocityContext velocityContext = new VelocityContext();
    for (Field field : getClass().getFields()) {
      Object value = fieldValue(field, this);
      if (value == null) {
        throw new NullPointerException(
            "Field cannot be null. Make sure it is public and set: " + field);
      }
      Object old = velocityContext.put(field.getName(), value);
      if (old != null) {
        throw new IllegalArgumentException("Two fields have the same name: " + field.getName());
      }
    }
    return velocityContext;
  }

  private static Object fieldValue(Field field, Object container) {
    try {
      return field.get(container);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
