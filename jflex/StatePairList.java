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
 * A list of pairs of states. Used in DFA minimization.
 *
 * @author Gerwin Klein
 * @version JFlex 1.6.1
 */
final public class StatePairList {

  // implemented as two arrays of integers.
  // java.util classes proved too inefficient.

  int p [];
  int q [];
  
  int num;

  public StatePairList() {
    p = new int [8];
    q = new int [8];
    num = 0;
  }

  public void addPair(int i, int j) {
    for (int x = 0; x < num; x++)
      if (p[x] == i && q[x] == j) return;

    if (num >= p.length) increaseSize(num);

    p[num] = i;
    q[num] = j;
    
    num++;
  }

  public void markAll(StatePairList [] [] list, boolean [] [] equiv) {
    for (int x = 0; x < num; x++) {
      int i = p[x];
      int j = q[x];
      
      if (equiv[i][j]) {
        equiv[i][j] = false;
        if (list[i][j] != null) 
          list[i][j].markAll(list, equiv);
      }
    }
  }

  private void increaseSize(int length) {
    length = Math.max(length+1, 4*p.length);
    Out.debug("increasing length to "+length); //$NON-NLS-1$
                               
    int pn [] = new int[length];
    int qn [] = new int[length];

    System.arraycopy(p, 0, pn, 0, p.length);
    System.arraycopy(q, 0, qn, 0, q.length);

    p = pn; 
    q = qn;
  } 
}
