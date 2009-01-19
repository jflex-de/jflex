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


/**
 * A simple table to store EOF actions for each lexical state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.4.2, $Revision$, $Date$
 */
public class EOFActions {

  /** maps lexical states to actions */
  private Map<Integer, Action> actions = new HashMap<Integer, Action>();
  private Action defaultAction;
  private int numLexStates;

  public void setNumLexStates(int num) {
    numLexStates = num;
  }

  public void add(List<Integer> stateList, Action action) {

    if (stateList != null && stateList.size() > 0) {
      for (Integer state : stateList)
        add( state, action );   
    }
    else {
      defaultAction = action.getHigherPriority(defaultAction);
      
      for (int state = 0; state < numLexStates; state++) {
        if ( actions.get(state) != null ) {
          Action oldAction = actions.get(state);
          actions.put(state, oldAction.getHigherPriority(action));
        }
      }
    }
  }

  public void add(Integer state, Action action) {
    if ( actions.get(state) == null )
      actions.put(state, action);
    else {
      Action oldAction = actions.get(state);
      actions.put(state, oldAction.getHigherPriority(action));
    }
  }

  public boolean isEOFAction(Object a) {
    if (a == defaultAction) return true;

    for (Action action : actions.values())
      if (a == action) return true;

    return false;
  }

  public Action getAction(int state) {
    return actions.get(state);
  }

  public Action getDefault() {
    return defaultAction;
  }

  public int numActions() {
    return actions.size();
  }
}
