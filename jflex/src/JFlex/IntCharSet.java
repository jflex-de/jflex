/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
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

package JFlex;

import java.util.*;


/** 
 * CharSet implemented with intervalls
 *
 * [fixme: optimizations possible]
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
public final class IntCharSet {

  private final static boolean DEBUG = false;

  /* invariant: all intervalls are disjoint, ordered */
  private Vector intervalls;  
  private int pos; 

  public IntCharSet() {
    this.intervalls = new Vector();
  }

  public IntCharSet(char c) {
    this(new Intervall(c,c));
  }

  public IntCharSet(Intervall intervall) {
    this();
    intervalls.addElement(intervall);
  }

  public IntCharSet(Vector /* Intervall */ chars) {
    int size = chars.size();

    this.intervalls = new Vector(size);
    
    for (int i = 0; i < size; i++) 
      add( (Intervall) chars.elementAt(i) );    
  }

  /**
   * returns the index of the intervall that contains
   * the character c, -1 if there is no such intevall
   *
   * prec: true
   * post: -1 <= return < intervalls.size()
   */
  private int indexOf(char c) {
    int start = 0;
    int end   = intervalls.size()-1;

    while (start <= end) {
      int check = (start+end) / 2;
      Intervall i = (Intervall) intervalls.elementAt(check);
      
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
    for (int i = 0; i < set.intervalls.size(); i++) 
      add( (Intervall) set.intervalls.elementAt(i) );    
    return this;
  }

  public void add(Intervall intervall) {
    
    int size = intervalls.size();

    for (int i = 0; i < size; i++) {
      Intervall elem = (Intervall) intervalls.elementAt(i);

      if ( elem.end+1 < intervall.start ) continue;
      
      if ( elem.contains(intervall) ) return;      

      if ( elem.start > intervall.end+1 ) {
        intervalls.insertElementAt(new Intervall(intervall), i);
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
        Intervall x = (Intervall) intervalls.elementAt(i);
        if (x.start > elem.end+1) return;
        
        elem.end = x.end;
        intervalls.removeElementAt(i);
        size--;
      }
      return;      
    }

    intervalls.addElement(new Intervall(intervall));
  }

  public void add(char singleChar) {
    add( new Intervall(singleChar, singleChar) );
  } 

 
  public boolean contains(char singleChar) {
    return indexOf(singleChar) >= 0;
  }

  /**
   * prec: intervall != null
   */
  public boolean contains(Intervall intervall) {
    int index = indexOf(intervall.start);
    if (index < 0) return false;    
    return ((Intervall) intervalls.elementAt(index)).contains(intervall);
  }

  public boolean contains(IntCharSet set) {
    int i = 0; 
    int j = 0;

    while (j < set.intervalls.size()) {
      Intervall x = (Intervall) intervalls.elementAt(i);
      Intervall y = (Intervall) intervalls.elementAt(j);

      if (x.contains(y)) j++;
      
      if (x.start > y.end) return false;
      if (x.end < y.start) i++;
    }

    return true;
  }


  /**
   * o instanceof Intervall
   */
  public boolean equals(Object o) {
    IntCharSet set = (IntCharSet) o;
    if ( intervalls.size() != set.intervalls.size() ) return false;

    for (int i = 0; i < intervalls.size(); i++) {
      if ( !intervalls.elementAt(i).equals( set.intervalls.elementAt(i)) ) 
        return false;
    }

    return true;
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
      Intervall x = (Intervall) this.intervalls.elementAt(i);
      Intervall y = (Intervall) set.intervalls.elementAt(j);

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      result.intervalls.addElement(
        new Intervall(
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
      Intervall x = (Intervall) this.intervalls.elementAt(i);
      Intervall y = (Intervall) set.intervalls.elementAt(j);

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
        intervalls.removeElementAt(i);
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

      intervalls.insertElementAt(new Intervall(x.start, (char) (y.start-1)), i);
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

  public Intervall getNext() {
    if (pos == intervalls.size()) pos = 0;
    return (Intervall) intervalls.elementAt(pos++);
  }

  public String toString() {
    StringBuffer result = new StringBuffer("{ ");

    for (int i = 0; i < intervalls.size(); i++)
      result.append( intervalls.elementAt(i) );

    result.append(" }");

    return result.toString();
  }
}
