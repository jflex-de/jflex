package jflex.testcase.action_pipe;

import jflex.testing.JFlexTestRunner;
import jflex.testing.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "testsuite/javatests/jflex/testcase/action_pipe/action-pipe.flex",
    generatorThrows = jflex.GeneratorException.class)
public class ActionPipeTest {
  @Test
  public void ok() {}
}
