/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.6.1                                                             *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.util.*;


/**
 * A simple table to store EOF actions for each lexical state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.6.1
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
