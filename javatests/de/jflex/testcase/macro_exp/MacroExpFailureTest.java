package de.jflex.testcase.macro_exp;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test <a href="https://github.com/jflex-de/jflex/issues/92>#92 Ambiguous Error Message [sf#90]</a>
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/macro_exp/macro2.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = MacroException.class)
public class MacroExpFailureTest {
  @Test
  public void ok() {}
}
