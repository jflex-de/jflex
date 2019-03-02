package jflex.ucd_generator.emitter.unicode_version;

import java.io.FileOutputStream;
import jflex.testing.javac.PackageUtil;
import jflex.ucd_generator.emitter.common.UcdEmitter;
import jflex.ucd_generator.ucd.UcdVersion;

/** Emitter for a {@code Unicode_x_y.java}. */
public class UnicodeVersionEmitter extends UcdEmitter {

  private static final String UNICODE_VERSION_TEMPLATE =
      PackageUtil.getPathForClass(UnicodeVersionEmitter.class) + "/Unicode_x_y.java.vm";

  private final UcdVersion ucdVersion;

  public UnicodeVersionEmitter(String packageName, UcdVersion ucdVersion) {
    super(packageName);
    this.ucdVersion = ucdVersion;
  }

  public void emitUnicodeVersion(FileOutputStream out) {}
}
