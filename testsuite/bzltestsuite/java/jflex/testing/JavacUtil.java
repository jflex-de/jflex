package jflex.testing;

import java.io.File;
import java.nio.charset.StandardCharsets;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import jflex.testing.exception.CompilerException;

public class JavacUtil {

  public static void compile(Iterable<? extends File> files) throws CompilerException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    StandardJavaFileManager fileManager =
        compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);

    Iterable<? extends JavaFileObject> compilationUnits1 =
        fileManager.getJavaFileObjectsFromFiles(files);
    compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
    if (!diagnostics.getDiagnostics().isEmpty()) {
      throw new CompilerException(diagnostics.getDiagnostics());
    }
  }

  private JavacUtil() {}
}
