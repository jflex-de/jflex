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
package velocity;

import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/** Instance holder of the Velocity runtime. */
public class Velocity {

  private static RuntimeInstance velocityRuntimeInstance = new RuntimeInstance();

  static {
    velocityRuntimeInstance.setProperty(RuntimeConstants.RUNTIME_REFERENCES_STRICT, "true");
  }

  public static SimpleNode parsedTemplateForResource(String templateStr, String resourceName) {
    try {
      return velocityRuntimeInstance.parse(templateStr, resourceName);
    } catch (ParseException e) {
      throw new AssertionError(e);
    }
  }

  private Velocity() {}
}
