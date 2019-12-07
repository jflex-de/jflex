package jflex.core;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import jflex.chars.Interval;
import org.junit.Test;

public class IntCharSetTest {

  @Test
  public void testAddIntCharSet() {
    IntCharSet a = new IntCharSet(0);
    a.add(3);
    IntCharSet original_a = a.copy();
    IntCharSet b = new IntCharSet(0, 4);
    a.add(b);
    assertWithMessage("a ← a + b = %s + %s should be b", original_a, b).that(a).isEqualTo(b);
  }

  @Test
  public void testContainsSet() {
    IntCharSet a = new IntCharSet(3, 7);
    a.add(new Interval(10, 15));
    IntCharSet b = new IntCharSet(4, 6);
    IntCharSet c = new IntCharSet(1, 5);
    IntCharSet d = new IntCharSet(1, 20);

    assertThat(a.contains(b)).isTrue();
    b.add(new Interval(10, 15));
    assertThat(a.contains(b)).isTrue();
    a.sub(new IntCharSet(4, 7));
    a.sub(new IntCharSet(10, 15));
    assertThat(a.contains(b)).isFalse();
    assertThat(a.contains(c)).isFalse();
    assertThat(c.contains(d)).isFalse();
    assertWithMessage("d.contains(a); d is %s and a is %s", d, a).that(d.contains(a)).isTrue();
  }
}
