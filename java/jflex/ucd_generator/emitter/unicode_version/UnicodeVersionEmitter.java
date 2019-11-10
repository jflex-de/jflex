package jflex.ucd_generator.emitter.unicode_version;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import jflex.testing.javac.PackageUtil;
import jflex.ucd_generator.emitter.common.UcdEmitter;
import jflex.ucd_generator.scanner.UnicodeData;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.velocity.Velocity;
import org.apache.velocity.runtime.parser.ParseException;

/** Emitter for a {@code Unicode_x_y.java}. */
public class UnicodeVersionEmitter extends UcdEmitter {

  private static final String UNICODE_VERSION_TEMPLATE =
      PackageUtil.getPathForClass(UnicodeVersionEmitter.class) + "/Unicode_x_y.java.vm";

  private final UcdVersion ucdVersion;
  private final UnicodeData unicodeData;

  public UnicodeVersionEmitter(String packageName, UcdVersion ucdVersion, UnicodeData unicodeData) {
    super(packageName);
    this.ucdVersion = ucdVersion;
    this.unicodeData = unicodeData;
  }

  public void emitUnicodeVersion(OutputStream output) throws IOException, ParseException {
    UnicodeVersionVars unicodeVersionVars = createUnicodeVersionVars();
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
      Velocity.render(
          readResource(UNICODE_VERSION_TEMPLATE), "Unicode_x_y", unicodeVersionVars, writer);
    }
  }

  private UnicodeVersionVars createUnicodeVersionVars() {
    UnicodeVersionVars unicodeVersionVars = new UnicodeVersionVars();
    unicodeVersionVars.packageName = getTargetPackage();
    unicodeVersionVars.className = ucdVersion.version().unicodeClassName();
    unicodeVersionVars.maxCodePoint = unicodeData.maximumCodePoint();
    return unicodeVersionVars;
  }
}
