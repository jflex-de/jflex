// test: ctorarg-f
package jflex.testcase.ctorarg;

import jflex.core.ScannerException;
import jflex.exceptions.GeneratorException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Should fail on parsing 2nd identifier. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/ctorarg/ctorargf.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class)
public class CtorArgFailureTest {
  @Test
  public void ok() {}
}
