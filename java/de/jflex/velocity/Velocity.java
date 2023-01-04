/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.velocity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/** Instance holder of the Velocity runtime. */
public class Velocity {

  private static final RuntimeInstance VELOCITY_INSTANCE = new RuntimeInstance();

  static {
    VELOCITY_INSTANCE.setProperty(RuntimeConstants.RUNTIME_REFERENCES_STRICT, "true");
  }

  public static void render(
      Reader templateReader, String templateName, TemplateVars templateVars, Writer writer)
      throws IOException, ParseException {
    VelocityContext velocityContext = templateVars.toVelocityContext();
    templateVars.templateName = templateName;
    SimpleNode tpl = Velocity.parsedTemplateForResource(templateReader, templateName);
    VELOCITY_INSTANCE.render(velocityContext, writer, templateName, tpl);
  }

  public static void render(
      Reader templateReader, String templateName, TemplateVars templateVars, OutputStream output)
      throws IOException, ParseException {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      render(templateReader, templateName, templateVars, writer);
    }
  }

  public static void render(
      Reader templateReader, String templateName, TemplateVars templateVars, File output)
      throws IOException, ParseException {
    try (OutputStream out = new FileOutputStream(output)) {
      render(templateReader, templateName, templateVars, out);
    }
  }

  private static SimpleNode parsedTemplateForResource(Reader template, String templateName) {
    try {
      return VELOCITY_INSTANCE.parse(template, templateName);
    } catch (ParseException e) {
      throw new AssertionError(e);
    }
  }

  private Velocity() {}
}
