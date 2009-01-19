/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.2                                                             *
 * Copyright (C) 1998-2008  Gerwin Klein <lsf@jflex.de>                    *
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
import jflex.unicode.UnicodeProperties;


/** 
 * CharSet implemented with intervals
 *
 * [fixme: optimizations possible]
 *
 * @author Gerwin Klein
 * @version JFlex 1.4.2, $Revision$, $Date$
 */
public final class IntCharSet {

  private final static boolean DEBUG = false;

  private static UnicodeProperties unicodeProperties;

  /* invariant: all intervals are disjoint, ordered */
  private List<Interval> intervals;
  private int pos;

  public IntCharSet() {
    this.intervals = new ArrayList<Interval>();
  }

  public IntCharSet(char c) {
    this(new Interval(c,c));
  }

  public IntCharSet(Interval interval) {
    this();
    intervals.add(interval);
  }

  public IntCharSet(List<Interval> chars) {
    int size = chars.size();
    intervals = new ArrayList<Interval>(size);

    for (Interval interval : chars)
      add(interval);
  }
  
  

  /**
   * returns the index of the interval that contains
   * the character c, -1 if there is no such intevall
   *
   * @prec: true
   * @post: -1 <= return < intervals.size() && 
   *        (return > -1 --> intervals[return].contains(c))
   * 
   * @param c  the character
   * @return the index of the enclosing interval, -1 if no such interval  
   */
  private int indexOf(char c) {
    int start = 0;
    int end   = intervals.size()-1;

    while (start <= end) {
      int check = (start+end) / 2;
      Interval i = intervals.get(check);
      
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
    for (Interval interval : set.intervals) 
      add(interval);
    return this;
  }

  public void add(Interval interval) {
    
    int size = intervals.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervals.get(i);

      if ( elem.end+1 < interval.start ) continue;
      
      if ( elem.contains(interval) ) return;      

      if ( elem.start > interval.end+1 ) {
        intervals.add(i, new Interval(interval));
        return;
      }
      
      if (interval.start < elem.start)
        elem.start = interval.start;
      
      if (interval.end <= elem.end) 
        return;
        
      elem.end = interval.end;
        
      i++;      
      // delete all x with x.contains( interval.end )
      while (i < size) {
        Interval x = intervals.get(i);
        if (x.start > elem.end+1) return;
        
        elem.end = x.end;
        intervals.remove(i);
        size--;
      }
      return;      
    }

    intervals.add(new Interval(interval));
  }

  public void add(char c) {
    int size = intervals.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervals.get(i);
      if (elem.end+1 < c) continue;

      if (elem.contains(c)) return; // already there, nothing to do

      // assert(elem.end+1 >= c && (elem.start > c || elem.end < c));
      
      if (elem.start > c+1) {
        intervals.add(i, new Interval(c,c));
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
      Interval x = intervals.get(i+1);
      if (x.start <= c+1) {
        elem.end = x.end;
        intervals.remove(i+1);
      }
      return;
    }
    
    // end reached but nothing found -> append at end
    intervals.add(new Interval(c,c));
  } 

 
  public boolean contains(char singleChar) {
    return indexOf(singleChar) >= 0;
  }


  /**
   * o instanceof Interval
   */
  public boolean equals(Object o) {
    IntCharSet set = (IntCharSet) o;
    
    return intervals.equals(set.intervals);
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

    int i = 0;  // index in this.intervals
    int j = 0;  // index in set.intervals

    int size = intervals.size();
    int setSize = set.intervals.size();

    while (i < size && j < setSize) {
      Interval x = this.intervals.get(i);
      Interval y = set.intervals.get(j);

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      result.intervals.add(
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

    int i = 0;  // index in this.intervals
    int j = 0;  // index in set.intervals

    int setSize = set.intervals.size();

    while (i < intervals.size() && j < setSize) {
      Interval x = this.intervals.get(i);
      Interval y = set.intervals.get(j);

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
        intervals.remove(i);
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

      intervals.add(i, new Interval(x.start, (char) (y.start-1)));
      x.start = (char) (y.end+1);

      i++;
      j++;
    }   

    if (DEBUG) {
      Out.dump("result: "+this);
    }
  }

  public boolean containsElements() {
    return intervals.size() > 0;
  }

  public int numIntervals() {
    return intervals.size();
  }

  public List<Interval> getIntervals() {
    return intervals;
  }

  // beware: depends on caller protocol, single user only 
  public Interval getNext() {
    if (pos == intervals.size()) pos = 0;
    return intervals.get(pos++);
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
        
    int size = intervals.size();
    for (int i=0; i < size; i++) {
      Interval elem = intervals.get(i);
      for (char c = elem.start; c <= elem.end; c++) {
        IntCharSet equivalenceClass = unicodeProperties.getCaselessMatches(c);
        if (null != equivalenceClass)
          n.add(equivalenceClass);
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

    for (Interval interval : intervals) 
      result.append(interval);

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
    for (Interval interval : intervals)
      result.intervals.add(interval.copy());
    return result;
  }

  static void setUnicodeProperties(UnicodeProperties unicodeProperties) {
    IntCharSet.unicodeProperties = unicodeProperties;
  }
}
