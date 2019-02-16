package jflex.ucd_generator.emitter.unicode_version;

import jflex.testing.javac.PackageUtil;

/** Emitter for a {@code Unicoder_x_y.java}. */
public class UnicodeVersionEmitter {
  private static final String UNICODE_VERSION_TEMPLATE =
      PackageUtil.getPathForClass(UnicodeVersionEmitter.class) + "/Unicode_x_y.java.vm";
}
