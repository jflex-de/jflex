/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.opentest4j.TestAbortedException;

/**
 * Holds back what a test prints to {@code System.out}, e.g. the output of JFlex or of a {@code
 * %debug} scanner, and prints it only if the test fails.
 *
 * <p>Registered for all tests that use the framework by {@code META-INF/services} and {@code
 * junit-platform.properties}. Tests can still capture their own output with {@link
 * SystemOutCapture}.
 */
public class QuietOutputExtension implements BeforeEachCallback, AfterEachCallback {

  private static final Namespace NAMESPACE = Namespace.create(QuietOutputExtension.class);

  /** The output of one test. */
  private static final class Output {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    SystemOutCapture capture;
  }

  @Override
  public void beforeEach(ExtensionContext context) throws UnsupportedEncodingException {
    Output output = new Output();
    output.capture = SystemOutCapture.start(new PrintStream(output.buffer, true, "UTF-8"));
    context.getStore(NAMESPACE).put(Output.class, output);
  }

  @Override
  public void afterEach(ExtensionContext context) throws UnsupportedEncodingException {
    Output output = context.getStore(NAMESPACE).remove(Output.class, Output.class);
    if (output == null) {
      return;
    }
    output.capture.close();
    Throwable failure = context.getExecutionException().orElse(null);
    if (failure == null || failure instanceof TestAbortedException || output.buffer.size() == 0) {
      return;
    }
    printOutputOfFailedTest(
        context.getRequiredTestClass().getName() + " " + context.getDisplayName(),
        output.buffer.toString("UTF-8"));
  }

  /**
   * Prints {@code output} as the output of the failed test {@code name}, including when the current
   * thread has a {@link SystemOutCapture}. For test failures that this extension cannot detect in
   * {@link afterEach}, such as in dynamic tests.
   */
  public static void printOutputOfFailedTest(String name, String output) {
    PrintStream out = SystemOutCapture.original();
    synchronized (out) {
      out.println("---- Output of failed test " + name);
      out.print(output);
      out.println("---- End of output of " + name);
    }
  }
}
