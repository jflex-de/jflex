package jflex.testcase.charclass;

import jflex.exceptions.GeneratorException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that an impossible macro throws a {@link GeneratorException} rather than a broken scanner.
 *
 * <p>See https://github.com/jflex-de/jflex/issues/106
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/charclass/char-range-failing.flex",
    generatorThrows = GeneratorException.class)
public class ImpossibleScannerTest {
  @Test
  public void ok() {}
}
