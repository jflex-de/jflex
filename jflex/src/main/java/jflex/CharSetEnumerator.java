/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.6.1                                                             *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;


/**
 * Enumerator for the elements of a CharSet.
 *
 * Does not implement java.util.Enumeration, but supports the same protocol.
 *  
 * @author Gerwin Klein
 * @version JFlex 1.6.1
 */
final public class CharSetEnumerator {

  private int index;
  private int offset;
  private long mask = 1;

  private CharSet set;
  
  public CharSetEnumerator(CharSet characters) {
    set = characters;

    while (index < set.bits.length && set.bits[index] == 0) 
      index++;

    if (index >= set.bits.length) return;
        
    while (offset <= CharSet.MOD && ((set.bits[index] & mask) == 0)) {
      mask<<= 1;
      offset++;
    }
  }

  private void advance() {
    do {
      offset++;
      mask<<= 1;
    } while (offset <= CharSet.MOD && ((set.bits[index] & mask) == 0));

    if (offset > CharSet.MOD) {
      do 
        index++;
      while (index < set.bits.length && set.bits[index] == 0);
        
      if (index >= set.bits.length) return;
        
      offset = 0;
      mask = 1;
      
      while (offset <= CharSet.MOD && ((set.bits[index] & mask) == 0)) {
        mask<<= 1;
        offset++;
      } 
    }
  }

  public boolean hasMoreElements() {
    return index < set.bits.length;
  }

  public int nextElement() {
    int x = (index << CharSet.BITS) + offset;
    advance();
    return x;
  }

}

