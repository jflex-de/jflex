package jflex.ucd_generator.emitter.unicode_version;

import com.google.common.base.Charsets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.stream.Collectors;
import jflex.ucd_generator.emitter.common.UcdEmitter;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.util.javac.JavaPackageUtils;
import jflex.velocity.Velocity;
import org.apache.velocity.runtime.parser.ParseException;

/** Emitter for a {@code Unicode_x_y.java}. */
public class UnicodeVersionEmitter extends UcdEmitter {

  private static final String UNICODE_VERSION_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodeVersionEmitter.class) + "/Unicode_x_y.java.vm";
  private static final String CP_ZERO = escapedUTF16Char(0);

  private final UcdVersion ucdVersion;
  private final UnicodeData unicodeData;

  public UnicodeVersionEmitter(String packageName, UcdVersion ucdVersion, UnicodeData unicodeData) {
    super(packageName);
    this.ucdVersion = ucdVersion;
    this.unicodeData = unicodeData;
  }

  public void emitUnicodeVersion(OutputStream output) throws IOException, ParseException {
    UnicodeVersionVars unicodeVersionVars = createUnicodeVersionVars();
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output, Charsets.UTF_8))) {
      Velocity.render(
          readResource(UNICODE_VERSION_TEMPLATE), "Unicode_x_y", unicodeVersionVars, writer);
    }
  }

  private UnicodeVersionVars createUnicodeVersionVars() {
    UnicodeVersionVars unicodeVersionVars = new UnicodeVersionVars();
    unicodeVersionVars.packageName = getTargetPackage();
    unicodeVersionVars.className = ucdVersion.version().unicodeClassName();
    unicodeVersionVars.maxCodePoint = unicodeData.maximumCodePoint();
    unicodeVersionVars.propertyValues =
        String.join("\",\n    \"", unicodeData.propertyValueIntervals());
    unicodeVersionVars.maxCaselessMatchPartitionSize = unicodeData.maxCaselessMatchPartitionSize();
    unicodeVersionVars.caselessMatchPartitions =
        unicodeData.uniqueCaselessMatchPartitions().stream()
            .map(
                partition ->
                    partitionToString(partition, unicodeVersionVars.maxCaselessMatchPartitionSize))
            .collect(Collectors.joining("\"\n          + \""));
    return unicodeVersionVars;
  }

  private static String partitionToString(
      SortedSet<Integer> partition, int caselessMatchPartitionSize) {
    StringBuilder sb = new StringBuilder();
    Iterator<String> escapedChars =
        partition.stream().map(UnicodeVersionEmitter::escapedUTF16Char).iterator();
    while (escapedChars.hasNext()) {
      sb.append(escapedChars.next());
    }
    // Add \u0000 placeholders to fill out the fixed record size
    for (int i = 0; i < caselessMatchPartitionSize - partition.size(); ++i) {
      sb.append(CP_ZERO);
    }
    return sb.toString();
  }

  private static String escapedUTF16Char(Integer codePoint) {
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
      case 0x0:
        return ("\\000");
      case 0x9:
        return ("\\t");
      case 0xA:
        return ("\\n");
      case 0xC:
        return ("\\f");
      case 0xD:
        return ("\\r");
      case 0x5C:
        return ("\\\\");
      default:
        return (String.format("\\u%04x", codePoint));
    }
  }
}
