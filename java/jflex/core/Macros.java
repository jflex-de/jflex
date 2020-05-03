/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static jflex.l10n.ErrorMessages.MACRO_CYCLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jflex.base.Build;
import jflex.exceptions.MacroException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * Symbol table and expander for macros.
 *
 * <p>Maps macros to their (expanded) definitions, detects cycles and unused macros.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public final class Macros {

  /** Maps names of macros to their definition */
  private final Map<String, RegExp> macros;

  /** Maps names of macros to their "used" flag */
  private final Map<String, Boolean> used;

  /** Creates a new macro expander. */
  public Macros() {
    macros = new HashMap<>();
    used = new HashMap<>();
  }

  /**
   * Stores a new macro and its definition.
   *
   * @param name the name of the new macro
   * @param definition the definition of the new macro
   * @return {@code true}, iff the macro name has not been stored before.
   */
  public boolean insert(String name, RegExp definition) {

    if (Build.DEBUG)
      Out.debug(
          "inserting macro "
              + name
              + " with definition :"
              + Out.NL
              + definition); // $NON-NLS-1$ //$NON-NLS-2$

    used.put(name, Boolean.FALSE);
    return macros.put(name, definition) == null;
  }

  /**
   * Marks a macro as used.
   *
   * @return {@code true}, iff the macro name has been stored before.
   * @param name a {@link java.lang.String} object.
   */
  public boolean markUsed(String name) {
    return used.put(name, Boolean.TRUE) != null;
  }

  /**
   * Tests if a macro has been used.
   *
   * @return {@code true}, iff the macro has been used in a regular expression.
   * @param name a {@link java.lang.String} object.
   */
  public boolean isUsed(String name) {
    return used.get(name);
  }

  /**
   * Returns all unused macros.
   *
   * @return the macro names that have not been used.
   */
  public List<String> unused() {

    List<String> unUsed = new ArrayList<>();

    for (String name : used.keySet()) {
      Boolean isUsed = used.get(name);
      if (!isUsed) unUsed.add(name);
    }

    return unUsed;
  }

  /**
   * Fetches the definition of the macro with the specified name,
   *
   * <p>The definition will either be the same as stored (expand() not called), or an equivalent
   * one, that doesn't contain any macro usages (expand() called before).
   *
   * @param name the name of the macro
   * @return the definition of the macro, {@code null} if no macro with the specified name has been
   *     stored.
   * @see Macros#expand
   */
  public RegExp getDefinition(String name) {
    return macros.get(name);
  }

  /**
   * Expands all stored macros, so that getDefinition always returns a definition that doesn't
   * contain any macro usages.
   *
   * @throws MacroException if there is a cycle in the macro usage graph.
   */
  public void expand() throws MacroException {
    for (String name : macros.keySet()) {
      if (isUsed(name)) {
        macros.put(name, expandMacro(name, getDefinition(name)));
      }
      // this put doesn't get a new key, so only a new value is set for the key "name"
    }
  }

  /**
   * Expands the specified macro by replacing each macro usage with the stored definition.
   *
   * @param name the name of the macro to expand (for detecting cycles)
   * @param definition the definition of the macro to expand
   * @return the expanded definition of the macro.
   * @throws MacroException when an error (such as a cyclic definition) occurs during expansion
   */
  @SuppressWarnings("unchecked")
  private RegExp expandMacro(String name, RegExp definition) throws MacroException {

    // Out.print("checking macro "+name);
    // Out.print("definition is "+definition);

    switch (definition.type) {
      case sym.BAR:
      case sym.CONCAT:
        RegExp2 binary = (RegExp2) definition;
        binary.r1 = expandMacro(name, binary.r1);
        binary.r2 = expandMacro(name, binary.r2);
        return definition;

      case sym.STAR:
      case sym.PLUS:
      case sym.QUESTION:
      case sym.BANG:
      case sym.TILDE:
        RegExp1 unary = (RegExp1) definition;
        unary.content = expandMacro(name, (RegExp) unary.content);
        return definition;

      case sym.MACROUSE:
        String usename = (String) ((RegExp1) definition).content;

        if (Objects.equals(name, usename))
          throw new MacroException(ErrorMessages.get(MACRO_CYCLE, name));

        RegExp usedef = getDefinition(usename);

        if (usedef == null) {
          throw new MacroException(
              ErrorMessages.get(ErrorMessages.MACRO_DEF_MISSING, usename, name));
        }

        markUsed(usename);

        return expandMacro(name, usedef);

      case sym.STRING:
      case sym.STRING_I:
      case sym.CHAR:
      case sym.CHAR_I:
      case sym.PRIMCLASS:
        return definition;

      case sym.CCLASS:
      case sym.CCLASSNOT:
        RegExp1 cclass = (RegExp1) definition;
        List<RegExp> classes = new ArrayList<>();
        for (RegExp regexp : (List<RegExp>) cclass.content) {
          classes.add(expandMacro(name, regexp));
        }
        cclass.content = classes;
        return cclass;

      case sym.CCLASSOP:
        RegExp2 cclassOp = (RegExp2) ((RegExp1) definition).content;
        cclassOp.r1 = expandMacro(name, cclassOp.r1);
        cclassOp.r2 = expandMacro(name, cclassOp.r2);
        return definition;

      default:
        throw new MacroException(
            "unknown expression type " + definition.typeName() + " in macro expansion");
    }
  }
}
