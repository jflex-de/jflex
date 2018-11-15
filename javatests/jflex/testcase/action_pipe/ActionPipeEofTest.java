package jflex.testcase.action_pipe;

import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test action piped with {@code <<EOF>>}. */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "javatests/jflex/testcase/action_pipe/action-pipe-eof.flex")
public class ActionPipeEofTest {
  @Test
  public void ok() {}
}
