/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    velocityRuntimeInstance.setProperty("directive.foreach.counter.initial.value", 0);
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
