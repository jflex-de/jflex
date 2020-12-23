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
