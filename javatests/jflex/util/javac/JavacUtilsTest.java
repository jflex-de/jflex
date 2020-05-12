package jflex.util.javac;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Test {@link JavacUtils}. */
public class JavacUtilsTest {

  @Mock JavaCompiler mockJavaCompiler;

  @Mock StandardJavaFileManager mockJavaFileManager;

  @Mock JavaFileObject mockCompilationUnit;
  Iterable<JavaFileObject> compilationUnits = Arrays.asList(mockCompilationUnit);

  @Mock CompilationTask mockTask;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
    when(mockJavaCompiler.getStandardFileManager(
            any(DiagnosticListener.class), eq(null), any(Charset.class)))
        .thenReturn(mockJavaFileManager);
    doReturn(compilationUnits).when(mockJavaFileManager).getJavaFileObjectsFromFiles(any());
  }

  @Test
  public void compile() throws CompilerException {
    Iterable<File> files = ImmutableList.of(new File("Foo.java", "Bar.java"));
    Iterable<File> classpath = ImmutableList.of(new File("/path/to/lib.jar"));

    ArgumentCaptor<Iterable<String>> argOptions = ArgumentCaptor.forClass(Iterable.class);
    when(mockJavaCompiler.getTask(
            /*out=*/ eq(null),
            eq(mockJavaFileManager),
            any(DiagnosticListener.class),
            argOptions.capture(),
            /*classes=*/ eq(null),
            eq(compilationUnits)))
        .thenReturn(mockTask);
    when(mockTask.call()).thenReturn(true);

    JavacUtils.compile(files, classpath, mockJavaCompiler);

    assertThat(argOptions.getValue())
        .containsExactly("-classpath", "/path/to/lib.jar")
        .inOrder();
  }
}
