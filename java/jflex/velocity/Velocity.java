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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.apache.velocity.VelocityContext;
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

  public static void render(
      Reader templateReader, String templateName, TemplateVars templateVars, Writer writer)
      throws IOException, ParseException {
    VelocityContext velocityContext = templateVars.toVelocityContext();
    SimpleNode tpl = Velocity.parsedTemplateForResource(templateReader, templateName);
    velocityRuntimeInstance.render(velocityContext, writer, templateName, tpl);
  }

  private static SimpleNode parsedTemplateForResource(Reader template, String templateName) {
    try {
      return velocityRuntimeInstance.parse(template, templateName);
    } catch (ParseException e) {
      throw new AssertionError(e);
    }
  }

  private Velocity() {}
}
