package jflex.testing.javac;

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
public final class JavacUtil {

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

  private JavacUtil() {}
}
