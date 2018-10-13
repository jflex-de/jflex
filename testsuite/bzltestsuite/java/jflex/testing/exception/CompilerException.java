package jflex.testing.exception;

import com.google.common.base.Joiner;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilerException extends Throwable {

  private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

  public CompilerException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    this.diagnostics = diagnostics;
  }

  @Override
  public String toString() {
    List<String> diagnosticMessages =
        diagnostics
            .stream()
            .map(
                d ->
                    String.format(
                        "javac error line %d in file %s", d.getLineNumber(), d.getSource()))
            .collect(Collectors.toList());
    return super.toString() + "\n" + Joiner.on('\n').join(diagnosticMessages);
  }
}
