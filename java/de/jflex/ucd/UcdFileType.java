/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

public enum UcdFileType {
  DerivedAge, // Common across all versions
  UnicodeData, // Always exists since version 1
  Blocks,
  DerivedCoreProperties,
  GraphemeBreakProperty,
  LineBreak,
  PropertyAliases,
  PropertyValueAliases,
  PropList,
  SentenceBreakProperty,
  Scripts,
  ScriptExtensions,
  WordBreakProperty,
  Emoji,
}
