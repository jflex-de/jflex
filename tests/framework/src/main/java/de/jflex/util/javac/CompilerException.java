/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.util.javac;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Exception while using the Java compiler.
 *
 * @author Régis Décamps
 */
public class CompilerException extends Exception {

  private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

  public CompilerException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    super("javac exception: " + diagnosticCode(diagnostics));
    this.diagnostics = diagnostics;
  }

  public CompilerException(Throwable e) {
    super("javac exception: " + e.getMessage(), e);
    this.diagnostics = Collections.emptyList();
  }

  public CompilerException(String message) {
    super("javac exception: " + message);
    this.diagnostics = Collections.emptyList();
  }

  @Override
  public String getMessage() {
    List<String> diagnosticMessages =
        diagnostics.stream()
            .map(
                d ->
                    String.format(
                        "javac error: %s, line %d in file %s",
                        d.getMessage(Locale.ENGLISH), d.getLineNumber(), d.getSource().getName()))
            .collect(Collectors.toList());
    return String.join("\n", diagnosticMessages);
  }

  private static String diagnosticCode(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    if (diagnostics.isEmpty()) {
      throw new IllegalArgumentException("Empty diagnostic");
    }
    Diagnostic<? extends JavaFileObject> firstDiagnostic = diagnostics.get(0);
    return firstDiagnostic.getCode();
  }
}
