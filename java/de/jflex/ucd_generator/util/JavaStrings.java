/*
 * Copyright (C) 2009-2013 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2019-2020 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.ucd_generator.util;

public class JavaStrings {
  private JavaStrings() {}

  /** Escapes all characters from the given string. */
  public static String escapedUTF16String(String string) {
    StringBuilder sb = new StringBuilder();
    for (char c : string.toCharArray()) {
      sb.append(escapedUTF16Char(c));
    }
    return sb.toString();
  }

  /** Escapes the given codepoint as a UTF8 Java representation. */
  public static String escapedUTF16Char(int codePoint) {
    if (codePoint <= 0xFFFF) {
      return escapedBMPChar(codePoint);
    } else if (codePoint <= 0x10FFFF) {
      StringBuilder sb = new StringBuilder();
      for (int c : Character.toChars(codePoint)) {
        sb.append(escapedBMPChar(c));
      }
      return sb.toString();
    } else {
      // codePoint above the BMP
      return "<" + Integer.toHexString(codePoint) + ">";
    }
  }

  /**
   * Returns an escaped character in the form "\\uXXXX", where XXXX is the hexadecimal form of the
   * given code point, which must be in the Basic Multilingual Plane (BMP).
   *
   * @param codePoint The code point for which to emit an escaped character.
   */
  private static String escapedBMPChar(int codePoint) {
    switch (codePoint) {
        // Special treatment for the quotation mark (U+0022).  "\u0022" triggers
        // a syntax error when it is included in a literal string, because it is
        // interpreted as "[...]"[...]" (literally), and leads the compiler to
        // think that the enclosing quotation marks are unbalanced.
      case 0x22:
        return ("\\\"");
      case 0x00:
        return ("\\000");
      case 0x09:
        return ("\\t");
      case 0x0A:
        return ("\\n");
      case 0x0C:
        return ("\\f");
      case 0x0D:
        return ("\\r");
      case 0x5C:
        return ("\\\\");
      default:
        return (String.format("\\u%04x", codePoint));
    }
  }
}
