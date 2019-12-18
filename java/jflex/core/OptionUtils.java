package jflex.core;

import java.io.File;
import java.nio.charset.Charset;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.skeleton.Skeleton;

public class OptionUtils {
  private OptionUtils() {}

  /** Sets encoding for input files, and check availability of encoding on this JVM. */
  public static void setEncoding(String encodingName) {
    if (Charset.isSupported(encodingName)) {
      Options.encoding = Charset.forName(encodingName);
    } else {
      Out.error(ErrorMessages.CHARSET_NOT_SUPPORTED, encodingName);
      throw new GeneratorException();
    }
  }

  /** Sets all options back to default values. */
  public static void setDefaultOptions() {
    Options.directory = null;
    // System.getProperty("user.dir"), the directory where java was run from.
    Options.resetRootDirectory();
    Options.jlex = false;
    Options.no_minimize = false;
    Options.no_backup = false;
    Options.verbose = true;
    Options.progress = true;
    Options.unused_warning = true;
    Options.time = false;
    Options.dot = false;
    Options.dump = false;
    Options.legacy_dot = false;
    Options.encoding = Charset.defaultCharset();
    Skeleton.readDefault();
  }

  public static void setSkeleton(File skel) {
    Skeleton.readSkelFile(skel);
  }

  /**
   * Set output directory
   *
   * @param d the directory to write output files to
   */
  public static void setDir(File d) {
    if (d.isFile()) {
      Out.error("Error: \"" + d + "\" is not a directory.");
      throw new GeneratorException();
    }

    if (!d.isDirectory() && !d.mkdirs()) {
      Out.error("Error: couldn't create directory \"" + d + "\"");
      throw new GeneratorException();
    }

    Options.directory = d;
  }

  /**
   * Set output directory
   *
   * @param dirName the name of the directory to write output files to
   */
  public static void setDir(String dirName) {
    setDir(new File(dirName));
  }
}
