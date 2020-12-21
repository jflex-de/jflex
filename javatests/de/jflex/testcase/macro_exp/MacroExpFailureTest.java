package jflex.testcase.macro_exp;

import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test <a href="https://github.com/jflex-de/jflex/issues/92>#92 Ambiguous Error Message [sf#90]</a>
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/macro_exp/macro2.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = MacroException.class)
public class MacroExpFailureTest {
  @Test
  public void ok() {}
}
