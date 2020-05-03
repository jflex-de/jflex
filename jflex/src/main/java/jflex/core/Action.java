/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import java.util.Objects;
import jflex.exceptions.GeneratorException;
import jflex.logging.Out;

/**
 * Encapsulates an action in the specification.
 *
 * <p>It stores the Java code as String together with a priority (line number in the specification).
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public final class Action {

  /** A normal action */
  public static final int NORMAL = 0;
  /** Action of a lookahead expression r1/r2 with fixed length r1 */
  public static final int FIXED_BASE = 1;
  /** Action of a lookahead expression r1/r2 with fixed length r2 */
  public static final int FIXED_LOOK = 2;
  /** Action of a lookahead expression r1/r2 with a finite choice of fixed lengths in r2 */
  public static final int FINITE_CHOICE = 3;
  /** Action of a general lookahead expression */
  public static final int GENERAL_LOOK = 4;
  /** Action of the 2nd forward pass for lookahead */
  public static final int FORWARD_ACTION = 5;
  /** Action of the backward pass for lookahead */
  public static final int BACKWARD_ACTION = 6;

  /** The Java code this Action represents */
  public final String content;

  /** The priority (i.e. line number in the specification) of this Action. */
  public final int priority;

  /**
   * Which kind of action this is. (normal, {@code a/b} with fixed length a, fixed length b, etc)
   */
  private int kind = NORMAL;

  /** The length of the lookahead (if fixed) */
  private int len;

  /** The entry state of the corresponding forward DFA (if general lookahead) */
  private int entryState;

  /**
   * Creates a new Action object with specified content and line number.
   *
   * @param content java code
   * @param priority line number
   */
  public Action(String content, int priority) {
    this.content = content.trim();
    this.priority = priority;
  }

  /**
   * Creates a new Action object of the specified kind. Only accepts FORWARD_ACTION or
   * BACKWARD_ACTION.
   *
   * @param kind the kind of action
   * @see #FORWARD_ACTION
   * @see #BACKWARD_ACTION
   * @see #FORWARD_ACTION
   * @see #BACKWARD_ACTION
   */
  public Action(int kind) {
    if (kind != FORWARD_ACTION && kind != BACKWARD_ACTION) throw new GeneratorException();
    this.content = "";
    this.priority = Integer.MAX_VALUE;
    this.kind = kind;
  }

  /**
   * Compares the priority value of this Action with the specified action.
   *
   * @param other the other Action to compare this Action with.
   * @return this Action if it has higher priority - the specified one, if not.
   */
  public Action getHigherPriority(Action other) {
    if (other == null) return this;

    // the smaller the number the higher the priority
    if (other.priority > this.priority) return this;
    else return other;
  }

  @Override
  public String toString() {
    return "Action (priority "
        + priority
        + ", lookahead "
        + kind
        + ") :"
        + Out.NL
        + content; // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * Returns {@code true} iff the parameter is an Action with the same content as this one.
   *
   * @param a the object to compare this Action with
   * @return true if the action strings are equal
   */
  public boolean isEquiv(Action a) {
    return Objects.equals(this.content, a.content)
        && this.kind == a.kind
        && this.len == a.len
        && this.entryState == a.entryState;
  }

  @Override
  public int hashCode() {
    return content.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Action) {
      if (o == this) {
        return true;
      }
      return isEquiv((Action) o);
    } else {
      return false;
    }
  }

  /**
   * Return true iff this is action belongs to a general lookahead rule.
   *
   * @return true if this actions belongs to a general lookahead rule.
   */
  public boolean isGenLookAction() {
    return kind == GENERAL_LOOK;
  }

  /**
   * Return true if code for this is action should be emitted, false if it is a BACK/FORWARD
   * lookahead action.
   *
   * @return true if code should be emitted for this action.
   */
  public boolean isEmittable() {
    return kind != BACKWARD_ACTION && kind != FORWARD_ACTION;
  }

  /**
   * Return kind of lookahead.
   *
   * @return a int.
   */
  public int lookAhead() {
    return kind;
  }

  /**
   * Sets the lookahead kind and data for this action
   *
   * @param kind which kind of lookahead it is
   * @param data the length for fixed length lookaheads.
   */
  public void setLookAction(int kind, int data) {
    this.kind = kind;
    this.len = data;
  }

  /**
   * The length of the lookahead or base if this is a fixed length lookahead action.
   *
   * @return a int.
   */
  public int getLookLength() {
    return len;
  }

  /**
   * Return the corresponding entry state for the forward DFA (if this is a general lookahead
   * expression)
   *
   * @return the forward DFA entry state (+1 is the backward DFA)
   */
  public int getEntryState() {
    return entryState;
  }

  /**
   * Set the corresponding entry state for the forward DFA of this action (if this is a general
   * lookahead expression)
   *
   * @param entryState a int.
   */
  public void setEntryState(int entryState) {
    this.entryState = entryState;
  }

  /**
   * copyChoice.
   *
   * @param length a int.
   * @return a {@link Action} object.
   */
  public Action copyChoice(int length) {
    Action a = new Action(this.content, this.priority);
    a.setLookAction(FINITE_CHOICE, length);
    return a;
  }

  /**
   * String representation of the lookahead kind of this action.
   *
   * @return the string representation
   */
  public String lookString() {
    switch (kind) {
      case NORMAL:
        return "";
      case BACKWARD_ACTION:
        return "LOOK_BACK";
      case FIXED_BASE:
        return "FIXED_BASE";
      case FIXED_LOOK:
        return "FIXED_LOOK";
      case FINITE_CHOICE:
        return "FINITE_CHOICE";
      case FORWARD_ACTION:
        return "LOOK_FORWARD";
      case GENERAL_LOOK:
        return "LOOK_ACTION";
      default:
        return "unknown lookahead type";
    }
  }
}
