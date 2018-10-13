package jflex.testing;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JFlexTestRunner extends BlockJUnit4ClassRunner {

  public JFlexTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }
}
