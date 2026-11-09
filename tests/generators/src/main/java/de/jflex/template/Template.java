/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal text templates: replaces each {@code ${name}} by the value of {@code name}.
 *
 * <p>There are no other directives and no escapes. A {@code $} that is not followed by {@code
 * {name}} is copied unchanged. Loops and conditionals go in the Java code that computes the values.
 */
public final class Template {

  private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([A-Za-z_][A-Za-z0-9_]*)}");

  /**
   * Returns {@code template} with each {@code ${name}} replaced by {@code values.get(name)}.
   *
   * @throws IllegalArgumentException if {@code values} has no entry for a placeholder.
   */
  public static String render(String template, Map<String, String> values) {
    Matcher m = PLACEHOLDER.matcher(template);
    StringBuffer result = new StringBuffer();
    while (m.find()) {
      String name = m.group(1);
      String value = values.get(name);
      if (value == null) {
        throw new IllegalArgumentException("Unknown template variable ${" + name + "}");
      }
      m.appendReplacement(result, Matcher.quoteReplacement(value));
    }
    m.appendTail(result);
    return result.toString();
  }

  /** Renders {@code template} with the public fields of {@code templateVars}. */
  public static String render(String template, TemplateVars templateVars) {
    try {
      return render(template, templateVars.toMap());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          e.getMessage() + " in template " + templateVars.templateName, e);
    }
  }

  /** Reads the template from {@code templateReader} and renders it into {@code writer}. */
  public static void render(Reader templateReader, TemplateVars templateVars, Writer writer)
      throws IOException {
    writer.write(render(read(templateReader), templateVars));
  }

  /** Reads the template from {@code templateReader} and renders it as UTF-8 into {@code output}. */
  public static void render(Reader templateReader, TemplateVars templateVars, OutputStream output)
      throws IOException {
    try (Writer writer =
        new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
      render(templateReader, templateVars, writer);
    }
  }

  /** Reads the template from {@code templateReader} and renders it as UTF-8 into {@code output}. */
  public static void render(Reader templateReader, TemplateVars templateVars, File output)
      throws IOException {
    try (OutputStream out = new FileOutputStream(output)) {
      render(templateReader, templateVars, out);
    }
  }

  /** Reads all of {@code reader} and closes it. */
  public static String read(Reader reader) throws IOException {
    try (Reader r = reader) {
      StringBuilder sb = new StringBuilder();
      char[] buf = new char[8192];
      int n;
      while ((n = r.read(buf)) != -1) {
        sb.append(buf, 0, n);
      }
      return sb.toString();
    }
  }

  private Template() {}
}
