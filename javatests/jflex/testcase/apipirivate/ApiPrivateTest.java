package jflex.testcase.apipirivate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import jflex.testing.javac.CompilerException;
import jflex.testing.javac.JavacUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that JFlex can generate private classes.
 *
 * <p>See feature request #513500 (Generate cleaner interfaces), {@code %apiprivate} option
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "javatests/jflex/testcase/apipirivate/private.flex")
public class ApiPrivateTest {

  @Test
  public void compile() throws CompilerException {
    try {
      JavacUtil.compile(
          ImmutableList.of(
              "javatests/jflex/testcase/apipirivate/Private.java",
              "javatests/jflex/testcase/apipirivate/AttemptPrivateAccess.java"));
      fail("Class `Private` should have private access");
    } catch (CompilerException e) {
      assertThat(e).hasMessageThat().contains("compiler.err.report.access");
    }
  }
}
