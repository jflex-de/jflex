/*
 * Copyright 2019, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/** Writes {@code All.Unicode.characters.input} of {@code src/test/resources}: every code point. */
public class OutputAllUnicodeCharacters {
  public static void main(String... args) throws Exception {
    String filename = "All.Unicode.characters.input";
    try (FileOutputStream stream = new FileOutputStream(filename);
        OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
      for (int ch = 0; ch <= 0x10FFFF; ++ch) {
        if (Character.charCount(ch) > 1 || !Character.isSurrogate((char) ch)) {
          writer.write(Character.toChars(ch));
        }
      }
    }
  }
}
