// test: macro

package jflex.testcase.macro_complement;

import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * test bug <a href="https://github.com/jflex-de/jflex/issues/50">#50 Definitions not checked in
 * complement</a>
 *
 * <p>When referring to an undeclared definition in a complement expression no error is reported
 *
 * <p>%% r = !{s} %% {r} { .. }
 *
 * <p>will be accepted, but a NullPointerException is thrown when the dfa is constructed as `s' is
 * not defined.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/macro_complement/macro.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = MacroException.class)
public class MacroComplementTest {

  @Test
  public void ok() {}
}
