/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

/**
 * A set of NFA states (= integers).
 *
 * <p>Very similar to java.util.BitSet, but is faster and doesn't crash
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class StateSet {

  private final boolean DEBUG = false;

  /** Constant <code>EMPTY</code> */
  public static final StateSet EMPTY = new StateSet();

  static final int BITS = 6;
  static final int MASK = (1 << BITS) - 1;

  long bits[];

  /** Constructor for StateSet. */
  public StateSet() {
    this(256);
  }

  /**
   * Constructor for StateSet.
   *
   * @param size a int.
   */
  public StateSet(int size) {
    bits = new long[size2nbits(size)];
  }

  /**
   * Constructor for StateSet.
   *
   * @param size a int.
   * @param state a int.
   */
  public StateSet(int size, int state) {
    this(size);
    addState(state);
  }

  /**
   * Constructor for StateSet.
   *
   * @param set a {@link StateSet} object.
   */
  public StateSet(StateSet set) {
    bits = new long[set.bits.length];
    System.arraycopy(set.bits, 0, bits, 0, set.bits.length);
  }

  /**
   * addState.
   *
   * @param state a int.
   */
  public void addState(int state) {
    if (DEBUG) {
      Out.dump("StateSet.addState(" + state + ") start"); // $NON-NLS-1$ //$NON-NLS-2$
      Out.dump("Set is : " + this); // $NON-NLS-1$
    }

    int index = state >> BITS;
    if (index >= bits.length) resize(state);
    bits[index] |= (1L << (state & MASK));

    if (DEBUG) {
      Out.dump("StateSet.addState(" + state + ") end"); // $NON-NLS-1$ //$NON-NLS-2$
      Out.dump("Set is : " + this); // $NON-NLS-1$
    }
  }

  private int size2nbits(int size) {
    return ((size >> BITS) + 1);
  }

  private void resize(int size) {
    int needed = size2nbits(size);

    // if (needed < bits.length) return;

    long newbits[] = new long[Math.max(bits.length * 4, needed)];
    System.arraycopy(bits, 0, newbits, 0, bits.length);

    bits = newbits;
  }

  /** clear. */
  public void clear() {
    int l = bits.length;
    for (int i = 0; i < l; i++) bits[i] = 0;
  }

  /**
   * isElement.
   *
   * @param state a int.
   * @return a boolean.
   */
  public boolean isElement(int state) {
    int index = state >> BITS;
    if (index >= bits.length) return false;
    return (bits[index] & (1L << (state & MASK))) != 0;
  }

  /**
   * Returns one element of the set and removes it.
   *
   * <p>Precondition: the set is not empty.
   *
   * @return a int.
   */
  public int getAndRemoveElement() {
    int i = 0;
    int o = 0;
    long m = 1;

    while (bits[i] == 0) i++;

    while ((bits[i] & m) == 0) {
      m <<= 1;
      o++;
    }

    bits[i] &= ~m;

    return (i << BITS) + o;
  }

  /**
   * remove.
   *
   * @param state a int.
   */
  public void remove(int state) {
    int index = state >> BITS;
    if (index >= bits.length) return;
    bits[index] &= ~(1L << (state & MASK));
  }

  /**
   * Returns the set of elements that contained are in the specified set but are not contained in
   * this set.
   *
   * @param set a {@link jflex.StateSet} object.
   * @return a {@link jflex.StateSet} object.
   */
  public StateSet complement(StateSet set) {

    if (set == null) return null;

    StateSet result = new StateSet();

    result.bits = new long[set.bits.length];

    int i;
    int m = Math.min(bits.length, set.bits.length);

    for (i = 0; i < m; i++) {
      result.bits[i] = ~bits[i] & set.bits[i];
    }

    if (bits.length < set.bits.length)
      System.arraycopy(set.bits, m, result.bits, m, result.bits.length - m);

    if (DEBUG) {
      Out.dump(
          "Complement of "
              + this
              + Out.NL
              + "and "
              + set
              + Out.NL
              + " is :"
              + result); // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return result;
  }

  /**
   * add.
   *
   * @param set a {@link jflex.StateSet} object.
   */
  public void add(StateSet set) {

    if (DEBUG) {
      Out.dump("StateSet.add(" + set + ") start"); // $NON-NLS-1$ //$NON-NLS-2$
    }

    if (set == null) return;

    long tbits[];
    long sbits[] = set.bits;
    int sbitsl = sbits.length;

    if (bits.length < sbitsl) {
      tbits = new long[sbitsl];
      System.arraycopy(bits, 0, tbits, 0, bits.length);
    } else {
      tbits = this.bits;
    }

    for (int i = 0; i < sbitsl; i++) {
      tbits[i] |= sbits[i];
    }

    this.bits = tbits;

    if (DEBUG) {
      Out.dump("StateSet.add(" + set + ") end"); // $NON-NLS-1$ //$NON-NLS-2$
      Out.dump("Set is : " + this); // $NON-NLS-1$
    }
  }

  /**
   * containsSet.
   *
   * @param set a {@link jflex.StateSet} object.
   * @return a boolean.
   */
  public boolean containsSet(StateSet set) {

    if (DEBUG) {
      Out.dump("StateSet.containsSet(" + set + "), this=" + this); // $NON-NLS-1$ //$NON-NLS-2$
    }
    int i;
    int min = Math.min(bits.length, set.bits.length);

    for (i = 0; i < min; i++) if ((bits[i] & set.bits[i]) != set.bits[i]) return false;

    for (i = min; i < set.bits.length; i++) if (set.bits[i] != 0) return false;

    return true;
  }

  /** {@inheritDoc} */
  public boolean equals(Object b) {

    int i = 0;
    int l1, l2;
    StateSet set = (StateSet) b;

    if (DEBUG) {
      Out.dump("StateSet.equals(" + set + "), this=" + this); // $NON-NLS-1$ //$NON-NLS-2$
    }
    l1 = bits.length;
    l2 = set.bits.length;

    if (l1 <= l2) {
      while (i < l1) {
        if (bits[i] != set.bits[i]) return false;
        i++;
      }

      while (i < l2) if (set.bits[i++] != 0) return false;
    } else {
      while (i < l2) {
        if (bits[i] != set.bits[i]) return false;
        i++;
      }

      while (i < l1) if (bits[i++] != 0) return false;
    }

    return true;
  }

  /**
   * hashCode.
   *
   * @return a int.
   */
  public int hashCode() {
    long h = 1234;
    long[] _bits = bits;
    int i = bits.length - 1;

    // ignore zero high bits
    while (i >= 0 && _bits[i] == 0) i--;

    while (i >= 0) h ^= _bits[i--] * i;

    return (int) ((h >> 32) ^ h);
  }

  /**
   * states.
   *
   * @return a {@link jflex.StateSetEnumerator} object.
   */
  public StateSetEnumerator states() {
    return new StateSetEnumerator(this);
  }

  /**
   * containsElements.
   *
   * @return a boolean.
   */
  public boolean containsElements() {
    for (long bit : bits) if (bit != 0) return true;

    return false;
  }

  /**
   * copy.
   *
   * @return a {@link jflex.StateSet} object.
   */
  public StateSet copy() {
    StateSet set = new StateSet();
    set.bits = new long[bits.length];
    System.arraycopy(bits, 0, set.bits, 0, bits.length);
    return set;
  }

  /**
   * Copy specified StateSet into this.
   *
   * @param set the state set to copy.
   */
  public void copy(StateSet set) {

    if (DEBUG) {
      Out.dump("StateSet.copy(" + set + ") start"); // $NON-NLS-1$ //$NON-NLS-2$
    }
    if (set == null) {
      for (int i = 0; i < bits.length; i++) bits[i] = 0;
      return;
    }

    if (bits.length < set.bits.length) {
      bits = new long[set.bits.length];
    } else {
      for (int i = set.bits.length; i < bits.length; i++) bits[i] = 0;
    }

    System.arraycopy(set.bits, 0, bits, 0, bits.length);

    if (DEBUG) {
      Out.dump("StateSet.copy(" + set + ") end"); // $NON-NLS-1$ //$NON-NLS-2$
      Out.dump("Set is : " + this); // $NON-NLS-1$
    }
  }

  /**
   * toString.
   *
   * @return a {@link java.lang.String} object.
   */
  public String toString() {
    StateSetEnumerator set = states();

    StringBuilder result = new StringBuilder("{"); // $NON-NLS-1$

    if (set.hasMoreElements()) result.append("" + set.nextElement()); // $NON-NLS-1$

    while (set.hasMoreElements()) {
      int i = set.nextElement();
      result.append(", ").append(i); // $NON-NLS-1$
    }

    result.append("}"); // $NON-NLS-1$

    return result.toString();
  }
}
