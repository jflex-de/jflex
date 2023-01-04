/*
 * JFlex 1.9.0-SNAPSHOT
 * Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
 * All rights reserved.
 *
 * SPDX-License-Identifier: GPL-2.0-only
 */

package jflex.examples.minijava;

class UnknownCharacterException extends Exception {
  UnknownCharacterException(String unknownInput) {
    super("Unknown character « " + unknownInput + " »");
  }
}
