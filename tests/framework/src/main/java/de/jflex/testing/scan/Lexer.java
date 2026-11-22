/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.scan;

import java.io.IOException;

/**
 * The scanning method of a scanner class, usually the method reference {@code MyScanner::yylex}.
 *
 * @param <S> the scanner class
 * @param <T> the type of the tokens that the scanning method returns. For a scanner that returns
 *     {@code int}, this is {@code Integer}.
 */
@FunctionalInterface
public interface Lexer<S, T> {

  /** Returns the next token of the scanner. */
  T next(S scanner) throws IOException;
}
