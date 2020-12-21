package jflex.testcase.caseless_jlex;

import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests that the generator throws a warning about "hello" being swallowed by "[a-z]+". */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/caseless_jlex/caseless.flex",
    sysout = "javatests/jflex/testcase/caseless_jlex/caseless.sysout",
    quiet = true,
    jlexCompat = true)
public class GenerationTest {
  @Test
  public void ok() {}
}
