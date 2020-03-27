// test: line

package jflex.testcase.line_cont;

import jflex.testing.testsuite.JFlexTestRunner;
import jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/jflex/testcase/line_cont/line.flex",
    sysout = "javatests/jflex/testcase/line_cont/line-flex.sysout",
    syserr = "javatests/jflex/testcase/line_cont/line-flex.syserr",
    dump = true)
public class LineTest {
  @Test
  public void ok() {}
}
