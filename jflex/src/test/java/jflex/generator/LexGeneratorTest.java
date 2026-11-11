/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Tests for {@link LexGenerator}. */
public class LexGeneratorTest {

  @Rule public TemporaryFolder tmp = new TemporaryFolder();

  /** Writes a minimal spec for class {@code name} with an extra option line. */
  private File writeSpec(String name, String directive) throws Exception {
    File spec = tmp.newFile(name + ".flex");
    List<String> lines =
        Arrays.asList("%%", "%class " + name, "%int", directive, "%%", "\"a\" { return 1; }");
    Files.write(spec.toPath(), lines, StandardCharsets.UTF_8);
    return spec;
  }

  private String generate(File spec, Options options) throws Exception {
    Out out = new Out(options, new ByteArrayOutputStream());
    String generated = new LexGenerator(spec, options, out).generate();
    return new String(Files.readAllBytes(new File(generated).toPath()), StandardCharsets.UTF_8);
  }

  /** {@code %apiprivate} in one spec does not make the API of the next spec private. */
  @Test
  public void generate_apiPrivateDoesNotLeak() throws Exception {
    Options options = new Options();
    options.setDir(tmp.getRoot());

    String privateApi = generate(writeSpec("PrivateApi", "%apiprivate"), options);
    String publicApi = generate(writeSpec("PublicApi", ""), options);

    assertTrue(privateApi, privateApi.contains("private final void yyclose()"));
    assertFalse(privateApi, privateApi.contains("public final void yyclose()"));
    assertTrue(publicApi, publicApi.contains("public final void yyclose()"));
  }

  /** Two generators with different options do not affect each other. */
  @Test
  public void generate_optionsAreSeparate() throws Exception {
    Options privateOptions = new Options();
    privateOptions.setDir(tmp.newFolder("private"));
    privateOptions.setSkeleton(privateOptions.skeleton().makePrivate());
    Options publicOptions = new Options();
    publicOptions.setDir(tmp.newFolder("public"));

    String privateApi = generate(writeSpec("PrivateApi", ""), privateOptions);
    String publicApi = generate(writeSpec("PublicApi", ""), publicOptions);

    assertTrue(privateApi, privateApi.contains("private final void yyclose()"));
    assertTrue(publicApi, publicApi.contains("public final void yyclose()"));
  }

  /** A generator run with its own options and output, for running concurrently. */
  private static final class Run implements Callable<String> {
    final File spec;
    final Options options = new Options();
    final ByteArrayOutputStream messages = new ByteArrayOutputStream();
    final Out out = new Out(options, messages);
    final CountDownLatch start;

    Run(File spec, File outDir, boolean verbose, CountDownLatch start) {
      this.spec = spec;
      this.start = start;
      options.setDir(outDir);
      options.setVerbose(verbose);
      options.setProgress(verbose);
    }

    /** Waits for the other runs, then generates; returns the generated code. */
    @Override
    public String call() throws Exception {
      start.countDown();
      start.await();
      String generated = new LexGenerator(spec, options, out).generate();
      return new String(Files.readAllBytes(new File(generated).toPath()), StandardCharsets.UTF_8);
    }

    String messages() {
      return messages.toString();
    }
  }

  /**
   * Two generators on two threads at the same time, with separate options and output, do not affect
   * each other.
   */
  @Test
  public void generate_concurrentRunsAreSeparate() throws Exception {
    File privateSpec = writeSpec("PrivateApi", "%apiprivate");
    File publicSpec = writeSpec("PublicApi", "");
    File privateDir = tmp.newFolder("private");
    File publicDir = tmp.newFolder("public");

    ExecutorService executor = Executors.newFixedThreadPool(2);
    try {
      // repeat to shake out ordering issues
      for (int i = 0; i < 5; i++) {
        CountDownLatch start = new CountDownLatch(2);
        Run verbose = new Run(privateSpec, privateDir, true, start);
        Run quiet = new Run(publicSpec, publicDir, false, start);
        Future<String> privateApi = executor.submit(verbose);
        Future<String> publicApi = executor.submit(quiet);

        assertTrue(privateApi.get(), privateApi.get().contains("private final void yyclose()"));
        assertTrue(publicApi.get(), publicApi.get().contains("public final void yyclose()"));

        String verboseMessages = verbose.messages();
        assertTrue(verboseMessages, verboseMessages.contains("Reading \"" + privateSpec + "\""));
        assertTrue(
            verboseMessages,
            verboseMessages.contains(
                "Writing code to \"" + new File(privateDir, "PrivateApi.java")));
        assertFalse(verboseMessages, verboseMessages.contains("PublicApi"));
        assertEquals("quiet run prints nothing", "", quiet.messages());
        assertEquals(0, verbose.out.errors());
        assertEquals(0, quiet.out.errors());
      }
    } finally {
      executor.shutdownNow();
    }
  }
}
