package ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;

public class UcdGenerator {

  /** Generates {@code UnicodeProperties_X_Y} from {@code //third_paty/unicode_ucd_X}. */
  public static void generate(
      ImmutableMap<String, ImmutableMap<ucd_generator.UcdFileType, File>> versions)
      throws Exception {
    Emitter emitter = new Emitter(new File("java/jflex/core/unicode"), versions);
    emitter.emitUnicodeProperties();
  }

  private UcdGenerator() {}
}
