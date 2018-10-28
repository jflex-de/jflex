package ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import jflex.maven.plugin.unicode.Emitter;

public class UcdGenerator {

  /** Generates {@code UnicodeProperties_X_Y} from {@code //third_paty/unicode_ucd_X}. */
  public static void generate(ImmutableMap<String, ImmutableMap<DataFileType, File>> versions)
      throws IOException {

    new Emitter(new File("."));
  }

  private UcdGenerator() {}
}
