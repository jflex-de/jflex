package jflex.testcase.apipirivate;

import static org.junit.Assert.fail;

import jflex.testing.JFlexTestRunner;
import jflex.testing.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** test feature request #513500 [Generate cleaner interfaces], %apiprivate option */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "testsuite/javatests/jflex/testcase/apipirivate/private.flex")
public class ApiPrivateTest {
  @Test
  public void testFail() {
    fail();
  }
}
