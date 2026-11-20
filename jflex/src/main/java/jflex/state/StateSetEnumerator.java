/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.state;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import jflex.logging.Out;

/**
 * Enumerates the states of a {@link StateSet} in ascending order. Also provides an iterator for
 * native int.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see StateSet
 */
public final class StateSetEnumerator implements PrimitiveIterator.OfInt {

  /** Local compile-time DEBUG flag */
  private static final boolean DEBUG = false;

  /**
   * Index of the next element in {@link #elems}. {@code index >= size} indicates that there are no
   * further elements in the set.
   */
  private int index;

  /** Reference to the elements of the StateSet to iterate over */
  private int[] elems;

  /** Number of elements in {@link #elems} */
  private int size;

  /**
   * Creates a new StateSetEnumerator that is not yet associated with a StateSet. It has no elements
   * before {@link #reset(StateSet)}.
   */
  public StateSetEnumerator() {}

  /**
   * Construct a StateSetEnumerator for a given StateSet. This should be the default constructor to
   * use.
   *
   * @param states the {@link StateSet} object to iterate over.
   * @see StateSet#states()
   */
  public StateSetEnumerator(StateSet states) {
    reset(states);
  }

  /**
   * Reset this enumerator/iterator and associate it with a given StateSet.
   *
   * @param states the {@link StateSet} object to iterate over.
   */
  public void reset(StateSet states) {
    this.elems = states.elems;
    this.size = states.numElems;
    this.index = 0;
  }

  /**
   * Determine if there are further elements in the set to be returned.
   *
   * @return true iff there are more elements in the set.
   */
  public boolean hasMoreElements() {
    if (DEBUG) {
      Out.trace("hasMoreElements, index = " + index);
    }
    return index < size;
  }

  /**
   * Return the next element from the set.
   *
   * <p>Precondition: {@link #hasMoreElements()} returns true
   *
   * @return the next element.
   * @exception NoSuchElementException if there is no further element
   * @see #hasMoreElements()
   */
  public int nextElement() {
    if (DEBUG) {
      Out.trace("nextElement, index = " + index);
    }
    if (index >= size) throw new NoSuchElementException();
    return elems[index++];
  }

  /** Iterator interface method for {@link #nextElement()}. */
  @Override
  public boolean hasNext() {
    return hasMoreElements();
  }

  /** Iterator interface method for {@link #hasMoreElements()} */
  @Override
  public int nextInt() {
    return nextElement();
  }
}
