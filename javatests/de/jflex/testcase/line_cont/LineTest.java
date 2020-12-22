// test: line

package de.jflex.testcase.line_cont;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/line_cont/line.flex",
    sysout = "javatests/de/jflex/testcase/line_cont/line-flex.sysout",
    syserr = "javatests/de/jflex/testcase/line_cont/line-flex.syserr",
    dump = true)
public class LineTest {
  @Test
  public void ok() {}
}
