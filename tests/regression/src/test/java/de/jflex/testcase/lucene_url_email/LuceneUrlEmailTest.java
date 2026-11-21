/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.lucene_url_email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.util.javac.JavacUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jflex.core.unicode.CharClasses;
import jflex.dfa.DFA;
import jflex.generator.LexGenerator;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The Lucense spec produces very large NFAs and DFAs (140k states NFA, 64k states DFA). Used here
 * as benchmark and generator peformance regression check for time and memory.
 *
 * <p>The test invokes JFlex directly, with {@code --time} for comparisons. Times are roughly
 * comparable if they are from the same machine without load and without running other tests
 * concurrently. E.g.
 *
 * <pre>
 * scripts/mvn-install-fastbuild.sh jflex-maven-plugin
 * scripts/test-regression.sh -Dtest=LuceneUrlEmailTest -Dsurefire.failIfNoSpecifiedTests=false
 * </pre>
 *
 * <p>The tests mainly check via {@link #fingerprint} that the generator produces the same character
 * classes and the same minimized DFA, but also runs some Lucene example inputs to check that it
 * generally works as expected.
 */
public class LuceneUrlEmailTest extends LuceneTokenizerCases {

  private static final String DIR = "src/test/java/de/jflex/testcase/lucene_url_email/";

  private static final String SPEC = DIR + "UAX29URLEmailTokenizerImpl.jflex";

  private static final String SCANNER_CLASS =
      "de.jflex.testcase.lucene_url_email.UAX29URLEmailTokenizerImpl";

  /** Where {@link #fingerprint} writes hash input. */
  private static final String FINGERPRINT_FILE = DIR + "UAX29URLEmailTokenizerImpl.fingerprint";

  /** Number of states of the minimized DFA. */
  private static final int STATES = 11669;

  /** Number of expected char classes. */
  private static final int CHAR_CLASSES = 99;

  /**
   * SHA-256 of {@link #fingerprintLines}. Fingerprint of the upstream scanner tables in {@code
   * UAX29URLEmailTokenizerImpl.java} at the commit of the spec.
   */
  private static final String FINGERPRINT =
      "67658da661693040609d1795df24eaca130cd3eada925afb72cca91b951ef65a";

  /** The names of the token types in Lucene, by the number that the scanner returns. */
  private static final String[] TOKEN_TYPES = {
    "<ALPHANUM>",
    "<NUM>",
    "<SOUTHEAST_ASIAN>",
    "<IDEOGRAPHIC>",
    "<HIRAGANA>",
    "<KATAKANA>",
    "<HANGUL>",
    "<URL>",
    "<EMAIL>",
    "<EMOJI>"
  };

  private static final int YYEOF = -1;

  private static LexGenerator generator;
  private static Out out;

  private static URLClassLoader loader;
  private static Constructor<?> newScanner;
  private static Method getNextToken;
  private static Method yytext;

  @BeforeAll
  static void generateScanner() throws Exception {
    Options options = new Options();
    options.setTime(true);
    options.setProgress(false);
    ByteArrayOutputStream log = new ByteArrayOutputStream();
    out = new Out(options, log);
    generator = new LexGenerator(new File(SPEC), options, out);
    File scanner;
    try {
      scanner = new File(generator.generate());
    } finally {
      // No test is running yet, so this is not held back like the output of tests.
      System.out.print("---- LuceneUrlEmailTest: output of JFlex" + Out.NL + log);
    }

    File classes = new File(DIR, "classes");
    assertTrue(classes.mkdir(), "Cannot create " + classes);
    JavacUtils.compile(Collections.singletonList(scanner), classes);
    loader = new URLClassLoader(new URL[] {classes.toURI().toURL()});
    Class<?> scannerClass = loader.loadClass(SCANNER_CLASS);
    newScanner = scannerClass.getConstructor(Reader.class);
    getNextToken = scannerClass.getMethod("getNextToken");
    yytext = scannerClass.getMethod("yytext");
  }

  @AfterAll
  static void closeLoader() throws Exception {
    if (loader != null) {
      loader.close();
    }
  }

  // --- the generator

  @Test
  public void noWarnings() {
    assertEquals(Collections.emptyList(), out.reportedWarnings());
  }

  @Test
  public void minimizedDfa() {
    assertEquals(STATES, generator.minimizedDfaStatesCount());
    DFA dfa = generator.minimizedDfa();
    assertEquals(STATES, dfa.numStates(), "states that can be reached from an entry state");
    assertFalse(dfa.hasDeadStates(), "no final state can be reached from some state");
  }

  @Test
  public void charClasses() {
    CharClasses classes = generator.charClasses();
    assertTrue(classes.invariants(), "character classes be consistent");
    assertEquals(CHAR_CLASSES, classes.getNumClasses());
  }

  /** The character classes and the minimized DFA of the scanner, both in canonical form. */
  private static List<String> fingerprintLines() {
    List<String> lines = new ArrayList<>();
    CharClasses classes = generator.charClasses();
    for (int i = 0; i < classes.getNumClasses(); i++) {
      lines.add("class " + i + ": " + classes.toString(i));
    }
    lines.addAll(generator.minimizedDfa().canonicalLines());
    return lines;
  }

  /**
   * Produce a fingerprint of the canonical form of character classes and DFA. As long as these do
   * not change, generator DFA output is equivalent.
   */
  @Test
  public void fingerprint() throws Exception {
    List<String> lines = fingerprintLines();
    File file = new File(FINGERPRINT_FILE);
    Files.write(file.toPath(), lines, StandardCharsets.UTF_8);

    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
    for (String line : lines) {
      sha256.update((line + "\n").getBytes(StandardCharsets.UTF_8));
    }
    StringBuilder actual = new StringBuilder();
    for (byte b : sha256.digest()) {
      actual.append(String.format("%02x", b));
    }
    assertEquals(
        FINGERPRINT,
        actual.toString(),
        "The character classes or the minimized DFA of the scanner have changed in: "
            + file.getAbsolutePath());
  }

  // --- the scanner

  /** Runs the scanner for the tests of {@link LuceneTokenizerCases}. */
  @Override
  void assertAnalyzesTo(String input, String[] output, String[] types) throws Exception {
    Object scanner = newScanner.newInstance(new StringReader(input));
    List<String> actualOutput = new ArrayList<>();
    List<String> actualTypes = new ArrayList<>();
    while (true) {
      int type = (Integer) getNextToken.invoke(scanner);
      if (type == YYEOF) {
        break;
      }
      actualOutput.add((String) yytext.invoke(scanner));
      actualTypes.add(TOKEN_TYPES[type]);
    }
    assertEquals(Arrays.asList(output), actualOutput, "tokens of: " + input);
    if (types != null) {
      assertEquals(Arrays.asList(types), actualTypes, "token types of: " + input);
    }
  }
}
