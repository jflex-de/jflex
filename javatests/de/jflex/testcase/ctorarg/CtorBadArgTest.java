// test: ctorarg-f
package de.jflex.testcase.ctorarg;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.scanner.ScannerException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests that JFlex refuses to generate a parser if its argument name is not a Java identifier. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/ctorarg/ctor-bad-arg.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class,
    sysout = "javatests/de/jflex/testcase/ctorarg/ctor-bad-arg.output")
public class CtorBadArgTest {
  @Test
  public void ok() {}
}
