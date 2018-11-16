package jflex.testcase.action_pipe;

import jflex.core.ScannerException;
import jflex.exceptions.GeneratorException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Reproduce Issue #201.
 *
 * <p>{@code <<EOF>>} action doesn't compile when piped with another action.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/action_pipe/eof-pipe-action.flex",
    generatorThrows = GeneratorException.class,
    // TODO(#201): Fix bug and remove generatorThrowableCause
    generatorThrowableCause = ScannerException.class)
public class EofPipeActionTest {
  @Test
  public void ok() {}
}
