package jflex.core.unicode;

import static jflex.base.Build.DEBUG;

import java.util.Comparator;

/** Comparator of {@link IntCharSet} . */
public class IntCharSetComparator implements Comparator<IntCharSet> {

  /**
   * Compares two IntCharSets.
   *
   * <p>Assumption: the IntCharSets are disjoint, e.g. members of a partition.
   *
   * <p>This method <em>does not</em> implement subset order, but instead compares the smallest
   * elements of the two sets, with the empty set smaller than any other set. This is to make the
   * order total for partitions as in {@link CharClasses}. It is unlikely to otherwise be a useful
   * order, and it does probably not implement the contract for {@link Comparable#compareTo}
   * correctly if the sets have the same smallest element but are not equal.
   *
   * @return 0 if the parameter is equal, -1 if its smallest element (if any) is larger than the
   *     smallest element of this set, and +1 if it is larger.
   */
  @Override
  public int compare(IntCharSet left, IntCharSet right) {
    if (left == null || right == null) {
      throw new NullPointerException();
    }

    if (left.equals(right)) {
      return 0;
    }

    if (DEBUG) {
      assert !left.and(right).containsElements();
    }

    if (!left.containsElements()) {
      return -1;
    }
    if (!right.containsElements()) {
      return 1;
    }

    if (left.getFirstInterval().start < right.getFirstInterval().start) {
      return -1;
    } else {
      return 1;
    }
  }
}
