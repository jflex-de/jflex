package jflex.core;

import static com.google.common.truth.Truth.assertWithMessage;

import jflex.chars.Interval;
import jflex.core.unicode.IntCharSet;
import junit.framework.TestCase;

public class IntCharSetTest extends TestCase {
  /**
   * Constructor for IntCharSetTest.
   *
   * @param name the test name
   */
  public IntCharSetTest(String name) {
    super(name);
  }

  public void testAddIntCharSet() {
    IntCharSet a = new IntCharSet((char) 0);
    a.add((char) 3);
    IntCharSet original_a = a.copy();
    IntCharSet b = new IntCharSet(new Interval((char) 0, (char) 4));
    a.add(b);
    assertWithMessage("a ← a + b = %s + %s should be b", original_a, b).that(a).isEqualTo(b);
  }
}
