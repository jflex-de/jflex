package jflex.testcase.action_pipe;

import jflex.exceptions.GeneratorException;
import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Reproduce Issue #201. Pipe action doesn't compile when first action. */
// TODO(#201): Fix bug
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "testsuite/javatests/jflex/testcase/action_pipe/action-pipe.flex",
    generatorThrows = GeneratorException.class)
public class ActionPipeTest {
  @Test
  public void ok() {}
}
