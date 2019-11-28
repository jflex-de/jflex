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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Helper class to invoke the {@link JavaCompiler}.
 *
 * @author Régis Décamps
 */
public final class JavacUtils {

  /** Compiles the given java source files. */
  public static void compile(Iterable<? extends File> files) throws CompilerException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager =
        compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);

    Iterable<? extends JavaFileObject> compilationUnit =
        fileManager.getJavaFileObjectsFromFiles(files);
    JavaCompiler.CompilationTask task =
        compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnit);
    Boolean success = task.call();
    try {
      if (!diagnostics.getDiagnostics().isEmpty()) {
        throw new CompilerException(diagnostics.getDiagnostics());
      }
      if (!Objects.equals(success, Boolean.TRUE)) {
        // Something went wrong: the default DiagnosticListener should add a diagnostic entry.
        // Anyway, let's fail to be sure.
        throw new CompilerException("unknown error");
      }
    } finally {
      try {
        fileManager.close();
      } catch (IOException ignore) {
      }
    }
  }

  /**
   * Compiles the given java source files.
   *
   * @see #compile(Iterable)
   */
  public static void compile(List<? extends String> javaSourceFileNames) throws CompilerException {
    List<File> javaSourceFiles =
        javaSourceFileNames.stream().map(File::new).collect(Collectors.toList());
    compile(javaSourceFiles);
  }

  private JavacUtils() {}
}
