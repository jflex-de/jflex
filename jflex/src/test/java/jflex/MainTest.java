/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.SilentExit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for {@link Main}. */
public class MainTest {

  @TempDir File tmp;

  /**
   * Writes a spec with a rule that can never be matched, which is a configurable warning.
   *
   * @param name file name of the spec, without extension; also the class name
   * @param directive an extra option line, e.g. {@code %suppress all}
   * @return the path of the spec
   */
  private String writeSpec(String name, String directive) throws Exception {
    File spec = new File(tmp, name + ".flex");
    String text =
        "%%\n%class "
            + name
            + "\n%int\n"
            + directive
            + "\n%%\n\"a\" { return 1; }\n\"a\" { return 2; }\n";
    Files.write(spec.toPath(), text.getBytes(StandardCharsets.UTF_8));
    return spec.getPath();
  }

  /** Runs {@link Main#generate} on the given options and specs and returns its output. */
  private String generate(String... argv) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    Main.generate(argv, output);
    return output.toString();
  }

  /**
   * Runs {@link Main#generate} with the given options on a spec with a rule that can never be
   * matched, and returns its output.
   */
  private String generateNeverMatch(String... options) throws Exception {
    String spec = writeSpec("NeverMatch", "");

    List<String> argv = new ArrayList<>(Arrays.asList(options));
    argv.addAll(Arrays.asList("-d", tmp.getPath(), spec));
    String output = generate(argv.toArray(new String[0]));

    assertTrue(new File(tmp, "NeverMatch.java").isFile(), "NeverMatch.java generated");
    return output;
  }

  private static boolean warnsFor(String output, String spec) {
    return output.contains("Warning in file \"" + spec + "\"");
  }

  /** Control for the test below: the spec does produce the warning. */
  @Test
  public void generate_neverMatch_warns() throws Exception {
    String output = generateNeverMatch();
    assertTrue(output.contains("never be matched"), output);
  }

  /** The warning options from the command line apply. */
  @Test
  public void generate_noWarnNeverMatch_suppressesWarning() throws Exception {
    String output = generateNeverMatch("--no-warn-never-match");
    assertFalse(output.contains("never be matched"), output);
  }

  /** A {@code %suppress} in one spec does not apply to the next spec on the command line. */
  @Test
  public void generate_twoSpecs_suppressDoesNotLeak() throws Exception {
    String suppressAll = writeSpec("SuppressAll", "%suppress all");
    String plain = writeSpec("Plain", "");

    String output = generate("-d", tmp.getPath(), suppressAll, plain);

    assertFalse(warnsFor(output, suppressAll), output);
    assertTrue(warnsFor(output, plain), output);
  }

  /** A {@code %warn} in one spec does not undo the command line for the next spec. */
  @Test
  public void generate_twoSpecs_warnDoesNotLeak() throws Exception {
    String warnAll = writeSpec("WarnAll", "%warn all");
    String plain = writeSpec("Plain", "");

    String output = generate("--no-warn-never-match", "-d", tmp.getPath(), warnAll, plain);

    assertTrue(warnsFor(output, warnAll), output);
    assertFalse(warnsFor(output, plain), output);
  }

  /** An invalid command line option is reported in output before the exception. */
  @Test
  public void generate_unknownWarningOption_reportsError() throws Exception {
    String spec = writeSpec("Unused", "");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    assertThrows(
        GeneratorException.class,
        () -> Main.generate(new String[] {"--no-warn-no-such-warning", spec}, output));
    assertTrue(output.toString().contains("\"no-such-warning\" unknown"), output.toString());
  }

  /** An unknown command line option is reported with a single prefix. */
  @Test
  public void generate_unknownOption_reportsError() throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    assertThrows(SilentExit.class, () -> Main.generate(new String[] {"--no-such-option"}, output));
    assertTrue(output.toString().contains("Error: unknown option"), output.toString());
    assertFalse(output.toString().contains("Error: Error"), output.toString());
  }

  /** An invalid directive in the spec is reported in output before the exception. */
  @Test
  public void generate_unknownWarningDirective_reportsError() throws Exception {
    String spec = writeSpec("BadSuppress", "%suppress no-such-warning");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    assertThrows(
        GeneratorException.class,
        () -> Main.generate(new String[] {"-d", tmp.getPath(), spec}, output));
    assertTrue(output.toString().contains("\"no-such-warning\" unknown"), output.toString());
  }
}
