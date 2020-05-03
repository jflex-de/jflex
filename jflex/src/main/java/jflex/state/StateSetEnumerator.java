/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.state;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import jflex.logging.Out;

/**
 * Enumerates the states of a {@link StateSet}. Also provides an iterator for native int.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 * @see StateSet
 */
public final class StateSetEnumerator implements PrimitiveIterator.OfInt {

  /** Local compile-time DEBUG flag */
  private static final boolean DEBUG = false;

  /**
   * Current index into the StateSet array. {@code index >= bits.length} indicates that there are no
   * further elements in the set.
   */
  private int index;
  /** Current offset into the StateSet array */
  private int offset;
  /** {@code mask = 1 << offset} */
  private long mask;

  /** Reference to the array of the StateSet to iterate over */
  private long[] bits;

  /**
   * Creates a new StateSetEnumerator that is not yet associated with a StateSet. {@link
   * #hasMoreElements()} and {@link #nextElement()} will throw {@link NullPointerException} when
   * used before {@link #reset(StateSet)}
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
    this.bits = states.bits;
    this.index = 0;
    this.offset = 0;
    this.mask = 1;

    // find the first index with elements in the array (= first non-zero bits[index])
    while (index < bits.length && bits[index] == 0) index++;

    // if there are none, the set is empty
    if (index >= bits.length) return;

    // find the first non-zero bit in bits[index]
    while (offset <= StateSet.MASK && (bits[index] & mask) == 0) {
      mask <<= 1;
      offset++;
    }
  }

  /**
   * Advance to the next element in the set.
   *
   * <p>Precondition: there are more elements in the set.
   */
  private void advance() {

    if (DEBUG) Out.dump("Advancing, at start, index = " + index + ", offset = " + offset);

    // cache fields in local variable for faster access
    int _index = this.index;
    int _offset = this.offset;
    long _mask = this.mask;
    long[] _bits = this.bits;

    long bi = _bits[_index];

    // check if there are further bits set at the current index
    do {
      _offset++;
      _mask <<= 1;
    } while (_offset <= StateSet.MASK && ((bi & _mask) == 0));

    // if there are no further bits set at the current index
    if (_offset > StateSet.MASK) {
      int length = _bits.length;

      // find next index with elements
      do _index++;
      while (_index < length && _bits[_index] == 0);

      // if there are none, there were no further elements
      if (_index >= length) {
        this.index = length; // indicates "no more elements"
        return;
      }

      // search for first non-zero bit in bits[index]
      _offset = 0;
      _mask = 1;
      bi = _bits[_index];

      // terminates, because bi != 0
      while ((bi & _mask) == 0) {
        _mask <<= 1;
        _offset++;
      }
    }

    // write back cached values
    this.index = _index;
    this.mask = _mask;
    this.offset = _offset;
  }

  /**
   * Determine if there are further elements in the set to be returned.
   *
   * @return true iff there are more elements in the set.
   */
  public boolean hasMoreElements() {
    if (DEBUG) {
      Out.dump("hasMoreElements, index = " + index + ", offset = " + offset);
    }
    return index < bits.length;
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
      Out.dump("nextElement, index = " + index + ", offset = " + offset);
    }
    if (index >= bits.length) throw new NoSuchElementException();
    int x = (index << StateSet.BITS) + offset;
    advance();
    return x;
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
