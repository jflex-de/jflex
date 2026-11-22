/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java.io.File;
import java.util.Objects;
import jflex.exceptions.GeneratorException;
import jflex.logging.Out;

/**
 * Encapsulates an action in the specification.
 *
 * <p>It stores the Java code as String together with a priority (the nth action in the spec has
 * priority n), and the file and line number of the action for messages.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public final class Action {

  public enum Kind {
    NORMAL,
    FIXED_BASE, // lookahead expression r1/r2 with fixed length r1
    FIXED_LOOK, // lookahead expression r1/r2 with fixed length r2
    FINITE_CHOICE, // lookahead expression r1/r2 with a finite choice of fixed lengths in r2
    GENERAL_LOOK,
    FORWARD_ACTION, // Action of the 2nd forward pass for lookahead
    BACKWARD_ACTION // Action of the backward pass for lookahead
  }

  /** The Java code this Action represents */
  public final String content;

  /**
   * The priority of this Action. The nth action in the spec (including from include files) has
   * priority n.
   */
  public final int priority;

  /** The file this Action is in, and {@code null} if it is not from a file, for error reporting. */
  public final File file;

  /**
   * The line number of this Action in {@link #file}, counted from 1, for error reporting. It is -1
   * if the action is not from a file.
   */
  public final int line;

  /**
   * Which kind of action this is. (normal, {@code a/b} with fixed length a, fixed length b, etc)
   */
  private Kind kind = Kind.NORMAL;

  /** The length of the lookahead (if fixed) */
  private int len;

  /** The entry state of the corresponding forward DFA (if general lookahead) */
  private int entryState;

  /**
   * Creates a new Action object for a default fall-back rule. It has the lowest priority ({@code
   * Integer.MAX_VALUE}).
   *
   * @param content java code
   */
  public Action(String content) {
    this(content, Integer.MAX_VALUE);
  }

  /**
   * Creates a new Action object that is not from a specification file. It has no file ({@code
   * null}) and line number -1.
   *
   * @param content java code
   * @param priority the priority, smaller is higher
   */
  public Action(String content, int priority) {
    this(content, null, -1, priority);
  }

  /**
   * Creates a new Action object with specified content, position in the specification for error
   * reporting, and priority.
   *
   * @param content java code
   * @param file the file the action is in
   * @param line the line number of the action in that file, counted from 1
   * @param priority the position of the action in the order of the rules, smaller is higher
   */
  public Action(String content, File file, int line, int priority) {
    this.content = content.trim();
    this.file = file;
    this.line = line;
    this.priority = priority;
  }

  /**
   * Creates a new Action object of the specified kind. Only accepts FORWARD_ACTION or
   * BACKWARD_ACTION.
   *
   * @param kind the kind of action
   * @see Kind#FORWARD_ACTION
   * @see Kind#BACKWARD_ACTION
   */
  public Action(Kind kind) {
    if (kind != Kind.FORWARD_ACTION && kind != Kind.BACKWARD_ACTION) throw new GeneratorException();
    this.content = "";
    this.file = null;
    this.line = -1;
    this.priority = Integer.MAX_VALUE;
    this.kind = kind;
  }

  /**
   * Compares the priority value of this Action with the specified action.
   *
   * <p>The choices of a finite choice lookahead have the same priority number. Of these, the
   * shorter lookahead takes precendence, so that the main expression has the longest match.
   *
   * @param other the other Action to compare this Action with.
   * @return this Action if it has higher priority - the specified one, if not.
   */
  public Action getHigherPriority(Action other) {
    if (other == null) return this;

    // the smaller the number the higher the priority
    if (other.priority > this.priority) return this;
    if (other.priority == this.priority
        && this.kind == Kind.FINITE_CHOICE
        && other.kind == Kind.FINITE_CHOICE
        && this.len < other.len) return this;
    return other;
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
    return kind == Kind.GENERAL_LOOK;
  }

  /**
   * Return true if code for this is action should be emitted, false if it is a BACK/FORWARD
   * lookahead action.
   *
   * @return true if code should be emitted for this action.
   */
  public boolean isEmittable() {
    return kind != Kind.BACKWARD_ACTION && kind != Kind.FORWARD_ACTION;
  }

  /**
   * Return kind of lookahead.
   *
   * @return a int.
   */
  public Kind lookAhead() {
    return kind;
  }

  /**
   * Sets the lookahead kind and data for this action
   *
   * @param kind which kind of lookahead it is
   * @param data the length for fixed length lookaheads.
   */
  public void setLookAction(Kind kind, int data) {
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
   * Returns a copy of this Action for one choice of a finite choice lookahead.
   *
   * @param length the length of the lookahead of this choice.
   * @return the copy, with the same priority.
   */
  public Action copyChoice(int length) {
    Action a = new Action(this.content, this.file, this.line, this.priority);
    a.setLookAction(Kind.FINITE_CHOICE, length);
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
    }

    // unreachable, but not all static analysers know
    return "unknown lookahead type";
  }
}
