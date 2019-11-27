package jflex.testcase.ctorarg;

import jflex.core.ScannerException;
import jflex.exceptions.GeneratorException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/ctorarg/ctor-no-arg.flex",
    sysout = "javatests/jflex/testcase/ctorarg/ctor-no-arg.output",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class)
public class CtorNoArgTest {
  @Test
  public void ok() {}
}
