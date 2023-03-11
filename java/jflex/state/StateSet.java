/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2021 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.state;

import java.util.Iterator;
import javax.annotation.Nullable;
import jflex.logging.Out;

/**
 * A set of NFA states (= ints).
 *
 * <p>Similar to {@link java.util.BitSet}, but tuned for sets of states. Can hold at most {@code
 * 2^64} elements, but is only useful for much smaller sets (ideally not more than tens of
 * thousands).
 *
 * <p>Provides an Integer iterator and a native int enumerator.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see StateSetEnumerator
 */
public final class StateSet implements Iterable<Integer> {

  /** Compile time {@code DEBUG} setting, local to {@code StateSet} */
  private final boolean DEBUG = false;

  /** {@code 2^BITS} per word */
  static final int BITS = 6;

  static final int MASK = (1 << BITS) - 1;

  /**
   * Content of the {@code StateSet}, one bit per int, i.e. bit 0 of {@code bits[0]} stands for 0,
   * bit 1 of {@code bits[0]} stands for 1, bit 1 of {@code bits[1]} stands for 65, etc.
   */
  long[] bits;

  /** Construct an empty StateSet with default memory backing. */
  public StateSet() {
    this(256);
  }

  /**
   * Construct an empty StateSet with specified memory backing.
   *
   * @param size an int specifying the largest number this set will store. The StateSet will
   *     automatically resize of a larger number is added; specifying the size avoids re-allocation.
   */
  public StateSet(int size) {
    bits = new long[size2nbits(size)];
  }

  /**
   * Construct an StateSet with specified initial element and memory backing.
   *
   * @param size an int specifying the largest number this set will store. The StateSet will
   *     automatically resize of a larger number is added; specifying the size avoids re-allocation.
   * @param state the element the set should contain.
   */
  public StateSet(int size, int state) {
    this(size);
    addState(state);
  }

  /**
   * Copy the specified StateSet to create a new one.
   *
   * @param set the {@link StateSet} object to copy.
   */
  public StateSet(StateSet set) {
    bits = new long[set.bits.length];
    System.arraycopy(set.bits, 0, bits, 0, set.bits.length);
  }

  /** Return a new StateSet of the specified length. */
  public static StateSet emptySet(int length) {
    return new StateSet(nbits2size(length));
  }

  /**
   * Add an element (a state) to the set. Will automatically resize the set representation if
   * necessary.
   *
   * @param state the element to add.
   */
  public void addState(int state) {
    if (DEBUG) Out.debug("StateSet.addState(" + state + ") start to " + this);

    int index = state >> BITS;
    if (index >= bits.length) resize(state);
    bits[index] |= (1L << (state & MASK));

    if (DEBUG) Out.debug("StateSet.addState(" + state + ") result: " + this);
  }

  /**
   * Compute the array size for a given set size.
   *
   * @param size the desired size of the set.
   * @return an array size such that the set can hold at least {@code size} elements.
   */
  static int size2nbits(int size) {
    return (size >> BITS) + 1;
  }

  /**
   * Compute a set size that will lead to an array of the given length.
   *
   * <p>Precondition: length > 0 && length <= 2^58 (58=64-BITS)
   *
   * @param length desired length of the StateSet array
   * @return an int {@code val} such that {@code size2nbits(val) = length}
   */
  static int nbits2size(int length) {
    // size2nbits((length - 1) << BITS)
    // = (((length - 1) << BITS) >> BITS) + 1
    // = length, if (length - 1) << BITS has no overflow
    return (length - 1) << BITS;
  }

  /**
   * Resize this set so it can hold at least {@code size} elements.
   *
   * @param size new maximum element
   */
  private void resize(int size) {
    int needed = size2nbits(size);

    // grow at least by a factor of 4 to reduce number of re-allocations
    long[] newbits = new long[Math.max(bits.length * 4, needed)];
    System.arraycopy(bits, 0, newbits, 0, bits.length);

    bits = newbits;
  }

  /** Remove all elements from this set. */
  public void clear() {
    int l = bits.length;
    for (int i = 0; i < l; i++) bits[i] = 0;
  }

  /**
   * Determine if a given state is an element of the set.
   *
   * @param state the element to check for.
   * @return true iff this set has the element {@code state}.
   */
  public boolean hasElement(int state) {
    int index = state >> BITS;
    if (index >= bits.length) return false;
    return (bits[index] & (1L << (state & MASK))) != 0;
  }

  /**
   * Returns an element of the set and removes it.
   *
   * <p>Precondition: the set is not empty.
   *
   * @return an element of the set.
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
   * Remove a given state from the set.
   *
   * @param state the element to remove.
   */
  public void remove(int state) {
    int index = state >> BITS;
    if (index >= bits.length) return;
    bits[index] &= ~(1L << (state & MASK));
  }

  /**
   * Remove all states from {@code this} that are not contained in the provided {@link StateSet}.
   *
   * @param set the {@link StateSet} object to intersect with.
   */
  public void intersect(StateSet set) {
    if (set == null) {
      clear();
    } else {
      int l = Math.min(bits.length, set.bits.length);
      for (int i = 0; i < l; i++) bits[i] &= set.bits[i];
      for (int i = l; i < bits.length; i++) bits[i] = 0;
    }
  }

  /**
   * Returns the complement of this set with respect to the specified set, that is, the set of
   * elements that are contained in the specified set but are not contained in this set.
   *
   * <p>Does not change this set.
   *
   * @param univ the {@link StateSet} that determines which elements can at most be returned.
   * @return the {@link StateSet} that contains all elements of {@code univ} that are not in this
   *     set.
   */
  @Nullable
  public StateSet complement(StateSet univ) {
    if (univ == null) {
      return null;
    }

    StateSet result = emptySet(univ.bits.length);

    int i;
    int m = Math.min(bits.length, univ.bits.length);

    for (i = 0; i < m; i++) result.bits[i] = ~bits[i] & univ.bits[i];

    if (bits.length < univ.bits.length)
      System.arraycopy(univ.bits, m, result.bits, m, result.bits.length - m);

    if (DEBUG) {
      Out.debug("Complement of " + this + Out.NL + "and " + univ + Out.NL + " is :" + result);
    }

    return result;
  }

  /**
   * Add all elements of the specified StateSet to this one.
   *
   * @param set a {@link StateSet} object to be added.
   */
  public void add(StateSet set) {
    if (DEBUG) Out.debug("StateSet.add(" + set + "), this = " + this);

    if (set == null) return;

    long[] this_bits;
    long[] add_bits = set.bits;
    int add_bits_length = add_bits.length;

    if (bits.length < add_bits_length) {
      this_bits = new long[add_bits_length];
      System.arraycopy(bits, 0, this_bits, 0, bits.length);
    } else {
      this_bits = this.bits;
    }

    for (int i = 0; i < add_bits_length; i++) this_bits[i] |= add_bits[i];

    this.bits = this_bits;

    if (DEBUG) Out.debug("StateSet.add() result: " + this);
  }

  @Override
  public boolean equals(@Nullable Object b) {
    if (!(b instanceof StateSet)) {
      return false;
    }

    int i = 0;
    int l1, l2;
    StateSet set = (StateSet) b;

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

  @Override
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
   * Construct an enumerator for this set.
   *
   * @return a {@link StateSetEnumerator} object for this set.
   */
  public StateSetEnumerator states() {
    return new StateSetEnumerator(this);
  }

  /**
   * Determine if the State set contains elements.
   *
   * @return true iff the set is not empty.
   */
  public boolean containsElements() {
    for (long bit : bits) if (bit != 0) return true;
    return false;
  }

  /**
   * Determine if the given set is a subset of this set.
   *
   * @param set the set to check containment of
   * @return true iff {@code set} is contained in this set.
   */
  public boolean contains(StateSet set) {
    if (set.bits.length > bits.length) {
      for (int i = bits.length; i < set.bits.length; i++) {
        if (set.bits[i] != 0) return false;
      }
    }
    for (int i = 0; i < Math.min(bits.length, set.bits.length); i++) {
      if ((bits[i] | set.bits[i]) != bits[i]) return false;
    }
    return true;
  }

  /**
   * Return a copy of this StateSet.
   *
   * @return a {@link StateSet} object with the same content as this.
   */
  public StateSet copy() {
    StateSet set = emptySet(bits.length);
    System.arraycopy(bits, 0, set.bits, 0, bits.length);
    return set;
  }

  /**
   * Copy specified StateSet into this.
   *
   * @param set the state set to copy.
   */
  public void copy(StateSet set) {
    if (set == null) clear();
    else {
      if (bits.length < set.bits.length) bits = new long[set.bits.length];
      else for (int i = set.bits.length; i < bits.length; i++) bits[i] = 0;

      System.arraycopy(set.bits, 0, bits, 0, Math.min(bits.length, set.bits.length));
    }
  }

  @Override
  public String toString() {
    StateSetEnumerator set = states();

    StringBuilder result = new StringBuilder("{");

    if (set.hasMoreElements()) {
      result.append(set.nextElement());
    }

    while (set.hasMoreElements()) {
      int i = set.nextElement();
      result.append(", ").append(i);
    }

    result.append("}");

    return result.toString();
  }

  /**
   * Construct an Integer iterator for this StateSet.
   *
   * @return an iterator for this StateSet.
   */
  @Override
  public Iterator<Integer> iterator() {
    return states();
  }

  /**
   * Provide the max value that can be stored without a resize
   *
   * @return an int of the max value
   */
  public int getCurrentMaxState() {
    return (bits.length << BITS) | ~(0xFFFFFFFF << BITS);
  }
}
