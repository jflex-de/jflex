/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.1                                                             *
 * Copyright (C) 1998-2004  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.util.*;


/** 
 * CharSet implemented with intervalls
 *
 * [fixme: optimizations possible]
 *
 * @author Gerwin Klein
 * @version JFlex 1.4.1, $Revision$, $Date$
 */
public final class IntCharSet {

  private final static boolean DEBUG = false;

  /* invariant: all intervals are disjoint, ordered */
  private List<Interval> intervalls;  
  private int pos; 

  public IntCharSet() {
    this.intervalls = new ArrayList<Interval>();
  }

  public IntCharSet(char c) {
    this(new Interval(c,c));
  }

  public IntCharSet(Interval intervall) {
    this();
    intervalls.add(intervall);
  }

  public IntCharSet(List<Interval> chars) {
    int size = chars.size();
    intervalls = new ArrayList<Interval>(size);

    for (Interval intervall : chars)
      add(intervall);
  }
  
  

  /**
   * returns the index of the intervall that contains
   * the character c, -1 if there is no such intevall
   *
   * @prec: true
   * @post: -1 <= return < intervalls.size() && 
   *        (return > -1 --> intervalls[return].contains(c))
   * 
   * @param c  the character
   * @return the index of the enclosing interval, -1 if no such interval  
   */
  private int indexOf(char c) {
    int start = 0;
    int end   = intervalls.size()-1;

    while (start <= end) {
      int check = (start+end) / 2;
      Interval i = intervalls.get(check);
      
      if (start == end) 
        return i.contains(c) ? start : -1;      

      if (c < i.start) {
        end = check-1;
        continue;
      }

      if (c > i.end) {
        start = check+1;
        continue;
      }

      return check;
    }

    return -1;
  } 

  public IntCharSet add(IntCharSet set) {
    for (Interval intervall : set.intervalls) 
      add(intervall);
    return this;
  }

  public void add(Interval intervall) {
    
    int size = intervalls.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervalls.get(i);

      if ( elem.end+1 < intervall.start ) continue;
      
      if ( elem.contains(intervall) ) return;      

      if ( elem.start > intervall.end+1 ) {
        intervalls.add(i, new Interval(intervall));
        return;
      }
      
      if (intervall.start < elem.start)
        elem.start = intervall.start;
      
      if (intervall.end <= elem.end) 
        return;
        
      elem.end = intervall.end;
        
      i++;      
      // delete all x with x.contains( intervall.end )
      while (i < size) {
        Interval x = intervalls.get(i);
        if (x.start > elem.end+1) return;
        
        elem.end = x.end;
        intervalls.remove(i);
        size--;
      }
      return;      
    }

    intervalls.add(new Interval(intervall));
  }

  public void add(char c) {
    int size = intervalls.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervalls.get(i);
      if (elem.end+1 < c) continue;

      if (elem.contains(c)) return; // already there, nothing to do

      // assert(elem.end+1 >= c && (elem.start > c || elem.end < c));
      
      if (elem.start > c+1) {
        intervalls.add(i, new Interval(c,c));
        return;                 
      }

      // assert(elem.end+1 >= c && elem.start <= c+1 && (elem.start > c || elem.end < c));
 
      if (c+1 == elem.start) {
        elem.start = c;
        return;
      }
      
      // assert(elem.end+1 == c);
      elem.end = c;

      // merge with next interval if it contains c
      if (i+1 >= size) return;
      Interval x = intervalls.get(i+1);
      if (x.start <= c+1) {
        elem.end = x.end;
        intervalls.remove(i+1);
      }
      return;
    }
    
    // end reached but nothing found -> append at end
    intervalls.add(new Interval(c,c));
  } 

 
  public boolean contains(char singleChar) {
    return indexOf(singleChar) >= 0;
  }


  /**
   * o instanceof Interval
   */
  public boolean equals(Object o) {
    IntCharSet set = (IntCharSet) o;
    
    return intervalls.equals(set.intervalls);
  }

  private char min(char a, char b) {
    return a <= b ? a : b;
  }

  private char max(char a, char b) {
    return a >= b ? a : b;
  }

  /* intersection */
  public IntCharSet and(IntCharSet set) {
    if (DEBUG) {
      Out.dump("intersection");
      Out.dump("this  : "+this);
      Out.dump("other : "+set);
    }

    IntCharSet result = new IntCharSet();

    int i = 0;  // index in this.intervalls
    int j = 0;  // index in set.intervalls

    int size = intervalls.size();
    int setSize = set.intervalls.size();

    while (i < size && j < setSize) {
      Interval x = this.intervalls.get(i);
      Interval y = set.intervalls.get(j);

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      result.intervalls.add(
        new Interval(
          max(x.start, y.start), 
          min(x.end, y.end)
          )
        );

      if (x.end >= y.end) j++;
      if (y.end >= x.end) i++;
    }

    if (DEBUG) {
      Out.dump("result: "+result);
    }

    return result;
  }
  
  /* complement */
  /* prec: this.contains(set), set != null */
  public void sub(IntCharSet set) {
    if (DEBUG) {
      Out.dump("complement");
      Out.dump("this  : "+this);
      Out.dump("other : "+set);
    }

    int i = 0;  // index in this.intervalls
    int j = 0;  // index in set.intervalls

    int setSize = set.intervalls.size();

    while (i < intervalls.size() && j < setSize) {
      Interval x = this.intervalls.get(i);
      Interval y = set.intervalls.get(j);

      if (DEBUG) {
        Out.dump("this      : "+this);
        Out.dump("this  ["+i+"] : "+x);
        Out.dump("other ["+j+"] : "+y);
      }

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      // x.end >= y.start && y.end >= x.start ->
      // x.end <= y.end && x.start >= y.start (prec)
      
      if ( x.start == y.start && x.end == y.end ) {
        intervalls.remove(i);
        j++;
        continue;
      }

      // x.end <= y.end && x.start >= y.start &&
      // (x.end < y.end || x.start > y.start) ->
      // x.start < x.end 

      if ( x.start == y.start ) {
        x.start = (char) (y.end+1);
        j++;
        continue;
      }

      if ( x.end == y.end ) {
        x.end = (char) (y.start-1);
        i++;
        j++;
        continue;
      }

      intervalls.add(i, new Interval(x.start, (char) (y.start-1)));
      x.start = (char) (y.end+1);

      i++;
      j++;
    }   

    if (DEBUG) {
      Out.dump("result: "+this);
    }
  }

  public boolean containsElements() {
    return intervalls.size() > 0;
  }

  public int numIntervalls() {
    return intervalls.size();
  }

  // beware: depends on caller protocol, single user only 
  public Interval getNext() {
    if (pos == intervalls.size()) pos = 0;
    return intervalls.get(pos++);
  }

  /**
   * Create a caseless version of this charset.
   * <p>
   * The caseless version contains all characters of this char set,
   * and additionally all lower/upper/title case variants of the 
   * characters in this set.
   * 
   * @return a caseless copy of this set
   */
  public IntCharSet getCaseless() {
    IntCharSet n = copy();
        
    int size = intervalls.size();
    for (int i=0; i < size; i++) {
      Interval elem = intervalls.get(i);
      for (char c = elem.start; c <= elem.end; c++) {
        n.add(Character.toLowerCase(c)); 
        n.add(Character.toUpperCase(c)); 
        n.add(Character.toTitleCase(c)); 
      }
    }
    
    return n;    
  }


  /**
   * Make a string representation of this char set.
   * 
   * @return a string representing this char set.
   */
  public String toString() {
    StringBuilder result = new StringBuilder("{ ");

    for (Interval intervall : intervalls) 
      result.append(intervall);

    result.append(" }");

    return result.toString();
  }
  
  
  /** 
   * Return a (deep) copy of this char set
   * 
   * @return the copy
   */
  public IntCharSet copy() {
    IntCharSet result = new IntCharSet();
    for (Interval intervall : intervalls)
      result.intervalls.add(intervall.copy());
    return result;
  }
}
