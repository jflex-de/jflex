package jflex.testcase.arr_return;

import jflex.testing.JFlexTestRunner;
import jflex.testing.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "testsuite/javatests/jflex/testcase/arr_return/arr.flex")
public class ArrReturn {
  @Test
  public void ok() {}
}
