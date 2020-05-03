/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple table to store EOF actions for each lexical state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class EOFActions {

  /** maps lexical states to actions */
  private final Map<Integer, Action> actions = new HashMap<>();

  private Action defaultAction;
  private int numLexStates;

  /**
   * Sets the number of lexical states.
   *
   * @param num number of states.
   */
  public void setNumLexStates(int num) {
    numLexStates = num;
  }

  /**
   * Add.
   *
   * @param stateList a {@link java.util.List} object.
   * @param action a {@link Action} object.
   */
  public void add(List<Integer> stateList, Action action) {

    if (stateList != null && stateList.size() > 0) {
      for (Integer state : stateList) add(state, action);
    } else {
      defaultAction = action.getHigherPriority(defaultAction);

      for (int state = 0; state < numLexStates; state++) {
        if (actions.get(state) != null) {
          Action oldAction = actions.get(state);
          actions.put(state, oldAction.getHigherPriority(action));
        }
      }
    }
  }

  /**
   * Add.
   *
   * @param state a {@link java.lang.Integer} object.
   * @param action a {@link Action} object.
   */
  public void add(Integer state, Action action) {
    if (actions.get(state) == null) actions.put(state, action);
    else {
      Action oldAction = actions.get(state);
      actions.put(state, oldAction.getHigherPriority(action));
    }
  }

  public boolean isEOFAction(Object a) {
    if (a == defaultAction) return true;

    for (Action action : actions.values()) if (a == action) return true;

    return false;
  }

  /**
   * getAction.
   *
   * @param state a int.
   * @return a {@link Action} object.
   */
  public Action getAction(int state) {
    return actions.get(state);
  }

  /**
   * Returns the default action.
   *
   * @return a default {@link Action}.
   */
  public Action getDefault() {
    return defaultAction;
  }

  /**
   * Returns thenumber of {@link Action}s.
   *
   * @return number of actions.
   */
  public int numActions() {
    return actions.size();
  }
}
