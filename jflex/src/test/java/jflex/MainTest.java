/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jflex.core.OptionUtils;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Tests for {@link Main}. */
public class MainTest {

  @Rule public TemporaryFolder tmp = new TemporaryFolder();

  /** Has a rule that can never be matched, which is a configurable warning. */
  private static final String NEVER_MATCH_SPEC =
      "%%\n%class NeverMatch\n%int\n%%\n\"a\" { return 1; }\n\"a\" { return 2; }\n";

  @After
  public void resetOptions() {
    Out.setOutputStream(System.out);
    OptionUtils.setDefaultOptions();
  }

  /** Runs {@link Main#generate} with fresh (uninitialised) options and returns its output. */
  private String generateFresh(String... options) throws Exception {
    File spec = tmp.newFile("never-match.flex");
    Files.write(spec.toPath(), NEVER_MATCH_SPEC.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    Out.setOutputStream(output);
    // options not initialised, as in a fresh JVM
    Options.encoding = null;

    List<String> argv = new ArrayList<>(Arrays.asList(options));
    argv.addAll(Arrays.asList("-d", tmp.getRoot().getPath(), spec.getPath()));
    Main.generate(argv.toArray(new String[0]));

    assertTrue("NeverMatch.java generated", new File(tmp.getRoot(), "NeverMatch.java").isFile());
    return output.toString();
  }

  /** Control for the test below: the spec does produce the warning. */
  @Test
  public void generate_withoutMain_warns() throws Exception {
    String output = generateFresh();
    assertTrue(output, output.contains("never be matched"));
  }

  /**
   * Calling {@link Main#generate} without {@link Main#main} in a fresh JVM, where no options are
   * set yet, keeps the warning options from the command line.
   */
  @Test
  public void generate_withoutMain_keepsSuppressedWarnings() throws Exception {
    String output = generateFresh("--no-warn-never-match");
    assertFalse(output, output.contains("never be matched"));
  }
}
