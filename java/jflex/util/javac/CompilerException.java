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
package jflex.util.javac;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
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
    this.diagnostics = ImmutableList.of();
  }

  public CompilerException(String message) {
    super("javac exception: " + message);
    this.diagnostics = ImmutableList.of();
  }

  @Override
  public String toString() {
    List<String> diagnosticMessages =
        diagnostics.stream()
            .map(
                d ->
                    String.format(
                        "javac error: %s, line %d in file %s",
                        d.getMessage(Locale.ENGLISH), d.getLineNumber(), d.getSource().getName()))
            .collect(Collectors.toList());
    return super.toString() + "\n" + Joiner.on('\n').join(diagnosticMessages);
  }

  private static String diagnosticCode(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    Preconditions.checkArgument(!diagnostics.isEmpty(), "Empty diagnostic");
    Diagnostic<? extends JavaFileObject> firstDiagnostic = diagnostics.get(0);
    return firstDiagnostic.getCode();
  }
}
