/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/** Test {@link Action}. */
public class ActionTest {

  private static Action lookAction(String content, int priority) {
    Action action = new Action(content, priority);
    action.setLookAction(Action.Kind.FINITE_CHOICE, 0);
    return action;
  }

  @Test
  public void getHigherPriority_smallerNumber() {
    Action first = new Action("return 1;", 1);
    Action second = new Action("return 2;", 2);

    assertSame(first, first.getHigherPriority(second));
    assertSame(first, second.getHigherPriority(first));
    assertSame(first, first.getHigherPriority(null));
  }

  /** Of the choices of a lookahead, the one with the shorter lookahead. */
  @Test
  public void getHigherPriority_shorterChoice() {
    Action action = lookAction("return 1;", 1);
    Action shortChoice = action.copyChoice(1);
    Action longChoice = action.copyChoice(2);

    assertSame(shortChoice, shortChoice.getHigherPriority(longChoice));
    assertSame(shortChoice, longChoice.getHigherPriority(shortChoice));
  }

  /** The priority of the rule decides before the length of the lookahead. */
  @Test
  public void getHigherPriority_ruleBeforeChoice() {
    Action longChoice = lookAction("return 1;", 1).copyChoice(2);
    Action shortChoice = lookAction("return 2;", 2).copyChoice(1);

    assertSame(longChoice, shortChoice.getHigherPriority(longChoice));
    assertSame(longChoice, longChoice.getHigherPriority(shortChoice));
  }
}
