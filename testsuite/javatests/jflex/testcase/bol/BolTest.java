package jflex.testcase.bol;

import jflex.testing.JFlexTestRunner;
import jflex.testing.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests BOL and EOL operators. */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "testsuite/javatests/jflex/testcase/bol/bol.flex")
public class BolTest {
  @Test
  public void ok() {}
}
