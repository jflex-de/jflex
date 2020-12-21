// test: initthrow

package de.jflex.testcase.initthrow_eol;

import static de.jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Regression test against bug <a href="https://github.com/jflex-de/jflex/issues/52">#52 single-line
 * %initthrow option fails</a> (for extra space before eol)
 */
public class InitThrowsTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    new Initthrow(CharSource.wrap("").openStream());
  }

  /**
   * Tests that the scanner constrcutor throws an UnsupportedOperationException if the reader is not
   * ready.
   */
  @Test
  public void canInstantiateScanner_throwsInInit() throws Exception {
    Reader reader = CharSource.wrap("").openStream();
    reader.close();
    assertThrows(IOException.class, () -> new Initthrow(reader));
  }
}
