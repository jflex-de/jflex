/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2021 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.state;

import java.util.Arrays;
import java.util.Iterator;
import jflex.logging.Out;

/**
 * A set of NFA states (= ints).
 *
 * <p>The set implementation is a sorted array. For sparse sets, such as in very large NFAs and DFAs
 * this turns out to be much cheaper in time and memory than a dense bitfield. For small, dense sets
 * it is not optimal, but does not add enough overhead to justify more complexity.
 *
 * <p>Provides an Integer iterator and a native int enumerator.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see StateSetEnumerator
 */
public final class StateSet implements Iterable<Integer> {

  /** Compile time {@code DEBUG} setting, local to {@code StateSet} */
  private static final boolean DEBUG = false;

  private static final int HASH_MULTIPLIER = 0x9E3779B1;

  private static final int[] NO_ELEMENTS = new int[0];

  /**
   * Content of the {@code StateSet}, {@code elems[0..numElems-1]} are the elements in ascending
   * order. The array may be larger than {@code numElems}. {@code elems} is never {@code null}.
   */
  int[] elems;

  /** Number of elements of the {@code StateSet}. */
  int numElems;

  /** Construct an empty StateSet. Memory is allocated as elements are added. */
  public StateSet() {
    elems = NO_ELEMENTS;
  }

  /**
   * Construct a StateSet with the specified initial element.
   *
   * @param state the element the set should contain.
   */
  public StateSet(int state) {
    elems = new int[] {state};
    numElems = 1;
  }

  /**
   * Copy the specified StateSet to create a new one. The copy has no unused memory.
   *
   * @param set the {@link StateSet} object to copy.
   */
  public StateSet(StateSet set) {
    numElems = set.numElems;
    elems = numElems == 0 ? NO_ELEMENTS : Arrays.copyOf(set.elems, numElems);
  }

  /**
   * Add an element (a state) to the set. Will automatically resize the set representation if
   * necessary.
   *
   * @param state the element to add.
   */
  public void addState(int state) {
    if (DEBUG) Out.debug("StateSet.addState(" + state + ") start to " + this);

    int n = numElems;
    int pos;
    if (n == 0 || state > elems[n - 1]) {
      pos = n;
    } else {
      pos = Arrays.binarySearch(elems, 0, n, state);
      if (pos >= 0) return;
      pos = -pos - 1;
    }

    ensureCapacity(n + 1);
    System.arraycopy(elems, pos, elems, pos + 1, n - pos);
    elems[pos] = state;
    numElems = n + 1;

    if (DEBUG) Out.debug("StateSet.addState(" + state + ") result: " + this);
  }

  /**
   * Makes sure that {@link #elems} can hold {@code needed} elements. Grows by at least a factor of
   * 2 to reduce the number of re-allocations.
   */
  private void ensureCapacity(int needed) {
    if (needed > elems.length) elems = Arrays.copyOf(elems, Math.max(needed, 2 * elems.length));
  }

  /** Remove all elements from this set. */
  public void clear() {
    numElems = 0;
  }

  /**
   * Determine if a given state is an element of the set.
   *
   * @param state the element to check for.
   * @return true iff this set has the element {@code state}.
   */
  public boolean hasElement(int state) {
    return Arrays.binarySearch(elems, 0, numElems, state) >= 0;
  }

  /**
   * Returns an element of the set and removes it.
   *
   * <p>Precondition: the set is not empty.
   *
   * @return an element of the set.
   */
  public int getAndRemoveElement() {
    return elems[--numElems];
  }

  /**
   * Remove a given state from the set.
   *
   * @param state the element to remove.
   */
  public void remove(int state) {
    int pos = Arrays.binarySearch(elems, 0, numElems, state);
    if (pos >= 0) {
      numElems--;
      System.arraycopy(elems, pos + 1, elems, pos, numElems - pos);
    }
  }

  /**
   * Remove all states from {@code this} that are not contained in the provided {@link StateSet}.
   *
   * @param set the {@link StateSet} object to intersect with.
   */
  public void intersect(StateSet set) {
    if (set == null) {
      clear();
    } else if (set != this) {
      int n = 0;
      for (int i = 0; i < numElems; i++) {
        if (set.hasElement(elems[i])) elems[n++] = elems[i];
      }
      numElems = n;
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
  public StateSet complement(StateSet univ) {
    if (univ == null) {
      return null;
    }

    StateSet result = new StateSet();
    result.elems = new int[univ.numElems];

    for (int i = 0; i < univ.numElems; i++) {
      if (!hasElement(univ.elems[i])) result.elems[result.numElems++] = univ.elems[i];
    }

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

    if (set == null || set == this || set.numElems == 0) return;

    if (set.numElems == 1) {
      addState(set.elems[0]);
      return;
    }

    int[] add = set.elems;
    int n = set.numElems;
    int m = numElems;

    // number of elements in the union
    int union = m + n;
    if (m > 0 && add[0] <= elems[m - 1]) {
      union -= countCommon(elems, m, add, n);
      if (union == m) return;
    }

    ensureCapacity(union);

    // merge from the largest element down: e[k] is where the next one goes, and k >= i
    int[] e = elems;
    int i = m - 1;
    int j = n - 1;
    int k = union - 1;
    while (j >= 0) {
      if (i >= 0 && e[i] > add[j]) {
        e[k--] = e[i--];
      } else {
        if (i >= 0 && e[i] == add[j]) i--;
        e[k--] = add[j--];
      }
    }
    // now k == i, and the rest of elems is in place

    numElems = union;

    if (DEBUG) Out.debug("StateSet.add() result: " + this);
  }

  /**
   * Returns the number of elements that {@code a[0..m-1]} and {@code b[0..n-1]} have in common.
   * Both in ascending order.
   */
  private static int countCommon(int[] a, int m, int[] b, int n) {
    if (m < n) {
      int[] t = a;
      a = b;
      b = t;
      int l = m;
      m = n;
      n = l;
    }

    int count = 0;
    if (m > 8 * n) {
      // few elements in a much larger array
      int from = 0;
      for (int j = 0; j < n && from < m; j++) {
        int pos = Arrays.binarySearch(a, from, m, b[j]);
        if (pos >= 0) {
          count++;
          from = pos + 1;
        } else {
          from = -pos - 1;
        }
      }
    } else {
      int i = 0;
      int j = 0;
      while (i < m && j < n) {
        if (a[i] < b[j]) {
          i++;
        } else if (a[i] > b[j]) {
          j++;
        } else {
          count++;
          i++;
          j++;
        }
      }
    }
    return count;
  }

  @Override
  public boolean equals(Object b) {
    if (!(b instanceof StateSet)) {
      return false;
    }

    StateSet set = (StateSet) b;

    if (numElems != set.numElems) return false;

    for (int i = 0; i < numElems; i++) {
      if (elems[i] != set.elems[i]) return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int h = 1234;
    int[] _elems = elems;
    int n = numElems;

    for (int i = 0; i < n; i++) h = (h + _elems[i]) * HASH_MULTIPLIER;

    return h ^ (h >>> 15);
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
    return numElems > 0;
  }

  /**
   * Determine if the given set is a subset of this set.
   *
   * @param set the set to check containment of
   * @return true iff {@code set} is contained in this set.
   */
  public boolean contains(StateSet set) {
    for (int i = 0; i < set.numElems; i++) {
      if (!hasElement(set.elems[i])) return false;
    }
    return true;
  }

  /**
   * Return a copy of this StateSet.
   *
   * @return a {@link StateSet} object with the same content as this.
   * @see #StateSet(StateSet)
   */
  public StateSet copy() {
    return new StateSet(this);
  }

  /**
   * Copy specified StateSet into this. Reuses the memory of this set where possible.
   *
   * @param set the state set to copy.
   */
  public void copy(StateSet set) {
    if (set == null) clear();
    else if (set != this) {
      if (elems.length < set.numElems) elems = new int[set.numElems];
      System.arraycopy(set.elems, 0, elems, 0, set.numElems);
      numElems = set.numElems;
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
}
