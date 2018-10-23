
package jflex.base;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_IntPair extends IntPair {

  private final int start;
  private final int end;

  AutoValue_IntPair(
      int start,
      int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public int start() {
    return start;
  }

  @Override
  public int end() {
    return end;
  }

  @Override
  public String toString() {
    return "IntPair{"
        + "start=" + start + ", "
        + "end=" + end
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof IntPair) {
      IntPair that = (IntPair) o;
      return (this.start == that.start())
           && (this.end == that.end());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.start;
    h *= 1000003;
    h ^= this.end;
    return h;
  }

}
