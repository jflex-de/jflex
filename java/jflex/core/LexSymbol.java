/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * Terminal symbol produced by {@link LexScan} for {@link LexParse}.
 *
 * <p>The parser uses {@link ComplexSymbolFactory}, which requires all symbols on the parse stack to
 * be {@link ComplexSymbol}s. JFlex uses {@link #left} for the line and {@link #right} for the
 * column of a symbol, which is what the grammar actions in {@code LexParse.cup} rely on. The {@code
 * ComplexSymbol} constructors would set those fields to the character offset of the {@link
 * Location} instead, so this class sets them explicitly.
 */
final class LexSymbol extends ComplexSymbol {

  /**
   * Creates a terminal symbol.
   *
   * @param type the terminal id, an index into {@link jflex.core.sym#terminalNames}
   * @param line the line the symbol starts in
   * @param column the column the symbol starts in
   * @param value the semantic value of the symbol, may be {@code null}
   */
  LexSymbol(int type, int line, int column, Object value) {
    // need fully qualified: the inherited Symbol.sym otherwise shadows the generated class sym
    super(
        jflex.core.sym.terminalNames[type],
        type,
        new Location(line, column),
        new Location(line, column),
        value);
    this.left = line;
    this.right = column;
  }
}
