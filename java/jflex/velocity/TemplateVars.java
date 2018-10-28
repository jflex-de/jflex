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

import java.lang.reflect.Field;
import org.apache.velocity.VelocityContext;

public abstract class TemplateVars {
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
