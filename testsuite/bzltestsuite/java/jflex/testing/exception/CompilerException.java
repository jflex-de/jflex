package jflex.testing.exception;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilerException extends Exception {

  private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

  public CompilerException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    super("javac exception");
    this.diagnostics = diagnostics;
  }

  public CompilerException(Throwable e) {
    super("javac exception", e);
    this.diagnostics = ImmutableList.of();
  }

  public CompilerException(String message) {
    super("javac exception: " + message);
    this.diagnostics = ImmutableList.of();
  }

  @Override
  public String toString() {
    List<String> diagnosticMessages =
        diagnostics
            .stream()
            .map(
                d ->
                    String.format(
                        "javac error: %s, line %d in file %s",
                        d.getMessage(Locale.ENGLISH), d.getLineNumber(), d.getSource().getName()))
            .collect(Collectors.toList());
    return super.toString() + "\n" + Joiner.on('\n').join(diagnosticMessages);
  }
}
