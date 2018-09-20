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
 * Character set.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class CharSet {

  static final int BITS = 6; // the number of bits to shift (2^6 = 64)
  static final int MOD = (1 << BITS) - 1; // modulus

  long bits[];

  private int numElements;

  /** Constructor for CharSet. */
  public CharSet() {
    bits = new long[1];
  }

  /**
   * Constructor for CharSet.
   *
   * @param initialSize inital size.
   * @param character character.
   */
  public CharSet(int initialSize, int character) {
    bits = new long[(initialSize >> BITS) + 1];
    add(character);
  }

  /**
   * Add the character to the set.
   *
   * @param character character.
   */
  public void add(int character) {
    resize(character);

    if ((bits[character >> BITS] & (1L << (character & MOD))) == 0) numElements++;

    bits[character >> BITS] |= (1L << (character & MOD));
  }

  private int nbits2size(int nbits) {
    return ((nbits >> BITS) + 1);
  }

  private void resize(int nbits) {
    int needed = nbits2size(nbits);

    if (needed < bits.length) return;

    long newbits[] = new long[Math.max(bits.length * 2, needed)];
    System.arraycopy(bits, 0, newbits, 0, bits.length);

    bits = newbits;
  }

  /**
   * Returns whether the character belongs to the set.
   *
   * @param character character.
   * @return whether the given character is an element of this set.
   */
  public boolean isElement(int character) {
    int index = character >> BITS;
    if (index >= bits.length) return false;
    return (bits[index] & (1L << (character & MOD))) != 0;
  }

  /**
   * Enumerates all characters.
   *
   * @return a {@link jflex.CharSetEnumerator} over all characters.
   */
  public CharSetEnumerator characters() {
    return new CharSetEnumerator(this);
  }

  /**
   * Returns whether the set contains elements.
   *
   * @return whether the set is non-empty.
   */
  public boolean containsElements() {
    return numElements > 0;
  }

  /**
   * Number of characters in the set.
   *
   * @return size of the size.
   */
  public int size() {
    return numElements;
  }

  /**
   * Returns a representation of this set.
   *
   * @return a {@link java.lang.String} representation of this set.
   */
  public String toString() {
    CharSetEnumerator set = characters();

    StringBuilder result = new StringBuilder("{");

    if (set.hasMoreElements()) result.append("").append(set.nextElement());

    while (set.hasMoreElements()) {
      int i = set.nextElement();
      result.append(", ").append(i);
    }

    result.append("}");

    return result.toString();
  }
}
