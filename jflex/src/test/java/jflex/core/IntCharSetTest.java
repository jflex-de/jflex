package jflex.core;

import static com.google.common.truth.Truth.assertWithMessage;

import jflex.chars.Interval;
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
    IntCharSet a = IntCharSet.ofCharacter(0);
    a.add(3);
    IntCharSet original_a = IntCharSet.copyOf(a);
    IntCharSet b = IntCharSet.ofCharacterRange(0, 4);
    a.add(b);
    assertWithMessage("a ← a + b = %s + %s should be b", original_a, b).that(a).isEqualTo(b);
  }

  public void testContainsSet() {
    IntCharSet a = IntCharSet.ofCharacterRange(3, 7);
    a.add(new Interval(10, 15));
    IntCharSet b = IntCharSet.ofCharacterRange(4, 6);
    IntCharSet c = IntCharSet.ofCharacterRange(1, 5);
    IntCharSet d = IntCharSet.ofCharacterRange(1, 20);

    assertTrue(a.contains(b));
    b.add(new Interval(10, 15));
    assertTrue(a.contains(b));
    a.sub(IntCharSet.ofCharacterRange(4, 7));
    a.sub(IntCharSet.ofCharacterRange(10, 15));
    assertFalse(a.contains(b));
    assertFalse(a.contains(c));
    assertFalse(c.contains(d));
    assertWithMessage("d.contains(a); d is %s and a is %s", d, a).that(d.contains(a)).isTrue();
  }
}
