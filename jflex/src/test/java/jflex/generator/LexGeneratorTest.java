/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import jflex.core.unicode.CharClasses;
import jflex.dfa.DFA;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for {@link LexGenerator}. */
public class LexGeneratorTest {

  @TempDir File tmp;

  /** Writes a minimal spec for class {@code name} with an extra option line. */
  private File writeSpec(String name, String directive) throws Exception {
    File spec = new File(tmp, name + ".flex");
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

  /** The character classes of a spec are available after generation, in canonical order. */
  @Test
  public void charClasses() throws Exception {
    Options options = new Options();
    options.setDir(tmp);
    LexGenerator generator =
        new LexGenerator(
            writeSpec("Ccl", "%unicode"), options, new Out(options, new ByteArrayOutputStream()));
    assertThrows(NullPointerException.class, generator::charClassesCount);

    generator.generate();

    assertEquals(2, generator.charClassesCount());
    CharClasses classes = generator.charClasses();
    assertTrue(classes.invariants());
    assertEquals("{ [0-'`']['b'-1114111] }", classes.toString(0));
    assertEquals("{ ['a'] }", classes.toString(1));
    assertEquals(1, classes.getClassCode('a'));
    assertEquals(0, classes.getClassCode('b'));
  }

  /** The minimized canonical DFA of a spec is available after generation. */
  @Test
  public void minimizedDfa() throws Exception {
    Options options = new Options();
    options.setDir(tmp);
    LexGenerator generator =
        new LexGenerator(
            writeSpec("Dfa", "%unicode"), options, new Out(options, new ByteArrayOutputStream()));
    assertThrows(NullPointerException.class, generator::minimizedDfa);

    generator.generate();

    DFA dfa = generator.minimizedDfa();
    assertTrue(dfa.isMinimized());
    assertEquals(generator.minimizedDfaStatesCount(), dfa.numStates());
    assertEquals(generator.charClassesCount(), dfa.numInput());
    assertEquals(
        Arrays.asList(
            "states 2, inputs 2, lexical states 1",
            "entry: 0 0",
            "0: 1 -> 1",
            "1 [a0]:",
            "a0: return 1;"),
        dfa.canonicalLines());
  }

  /** {@code %apiprivate} in one spec does not make the API of the next spec private. */
  @Test
  public void generate_apiPrivateDoesNotLeak() throws Exception {
    Options options = new Options();
    options.setDir(tmp);

    String privateApi = generate(writeSpec("PrivateApi", "%apiprivate"), options);
    String publicApi = generate(writeSpec("PublicApi", ""), options);

    assertTrue(privateApi.contains("private final void yyclose()"), privateApi);
    assertFalse(privateApi.contains("public final void yyclose()"), privateApi);
    assertTrue(publicApi.contains("public final void yyclose()"), publicApi);
  }

  /** Two generators with different options do not affect each other. */
  @Test
  public void generate_optionsAreSeparate() throws Exception {
    Options privateOptions = new Options();
    privateOptions.setDir(new File(tmp, "private"));
    privateOptions.setSkeleton(privateOptions.skeleton().makePrivate());
    Options publicOptions = new Options();
    publicOptions.setDir(new File(tmp, "public"));

    String privateApi = generate(writeSpec("PrivateApi", ""), privateOptions);
    String publicApi = generate(writeSpec("PublicApi", ""), publicOptions);

    assertTrue(privateApi.contains("private final void yyclose()"), privateApi);
    assertTrue(publicApi.contains("public final void yyclose()"), publicApi);
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
    File privateDir = new File(tmp, "private");
    File publicDir = new File(tmp, "public");

    ExecutorService executor = Executors.newFixedThreadPool(2);
    try {
      // repeat to shake out ordering issues
      for (int i = 0; i < 5; i++) {
        CountDownLatch start = new CountDownLatch(2);
        Run verbose = new Run(privateSpec, privateDir, true, start);
        Run quiet = new Run(publicSpec, publicDir, false, start);
        Future<String> privateApi = executor.submit(verbose);
        Future<String> publicApi = executor.submit(quiet);

        assertTrue(privateApi.get().contains("private final void yyclose()"), privateApi.get());
        assertTrue(publicApi.get().contains("public final void yyclose()"), publicApi.get());

        String verboseMessages = verbose.messages();
        assertTrue(verboseMessages.contains("Reading \"" + privateSpec + "\""), verboseMessages);
        assertTrue(
            verboseMessages.contains(
                "Writing code to \"" + new File(privateDir, "PrivateApi.java")),
            verboseMessages);
        assertFalse(verboseMessages.contains("PublicApi"), verboseMessages);
        assertEquals("", quiet.messages(), "quiet run prints nothing");
        assertEquals(0, verbose.out.errors());
        assertEquals(0, quiet.out.errors());
      }
    } finally {
      executor.shutdownNow();
    }
  }
}
