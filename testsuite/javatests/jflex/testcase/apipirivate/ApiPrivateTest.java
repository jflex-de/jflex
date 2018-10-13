package jflex.testcase.apipirivate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import jflex.testing.JFlexTestRunner;
import jflex.testing.annotations.TestSpec;
import jflex.testing.javac.CompilerException;
import jflex.testing.javac.JavacUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

/** test feature request #513500 [Generate cleaner interfaces], %apiprivate option */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "testsuite/javatests/jflex/testcase/apipirivate/private.flex")
public class ApiPrivateTest {

  @Test
  public void compile() throws CompilerException {
    try {
      JavacUtil.compile(
          ImmutableList.of(
              "testsuite/javatests/jflex/testcase/apipirivate/Private.java",
              "testsuite/javatests/jflex/testcase/apipirivate/AttemptPrivateAccess.java"));
      fail("Class `Private` should have private access");
    } catch (CompilerException e) {
      assertThat(e).hasMessageThat().contains("compiler.err.report.access");
    }
  }
}
