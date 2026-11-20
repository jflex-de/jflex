/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jflex.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests that the {@code --dot} option writes graphviz files for the NFA, the DFA and the minimized
 * DFA of a spec.
 *
 * <p>See also <a href="https://github.com/jflex-de/jflex/issues/769">#769</a>.
 */
public class DotTest {

  private static final String SPEC = "src/test/java/de/jflex/testcase/dot/dot.flex";

  @TempDir static Path outDir;

  /** What JFlex printed. */
  private static String jflexOutput;

  @BeforeAll
  static void runJFlex() throws Exception {
    ByteArrayOutputStream log = new ByteArrayOutputStream();
    Main.generate(new String[] {"-q", "--dot", "-d", outDir.toString(), SPEC}, log);
    jflexOutput = log.toString("UTF-8");
  }

  /** The lines of a dot file, without the empty line at the end. */
  private static List<String> lines(String fileName) throws Exception {
    List<String> lines = Files.readAllLines(outDir.resolve(fileName), UTF_8);
    assertEquals("", lines.get(lines.size() - 1), "last line of " + fileName);
    return lines.subList(0, lines.size() - 1);
  }

  /** The lines of a dot file that are edges with a label. */
  private static List<String> labelledEdges(String fileName) throws Exception {
    return lines(fileName).stream()
        .filter(line -> line.contains("[label="))
        .collect(Collectors.toList());
  }

  /** The labels of the edges, without duplicates, in the order of the file. */
  private static List<String> labels(String fileName) throws Exception {
    List<String> labels = new ArrayList<>();
    for (String edge : labelledEdges(fileName)) {
      String label = edge.substring(edge.indexOf('"') + 1, edge.lastIndexOf('"'));
      if (!labels.contains(label)) {
        labels.add(label);
      }
    }
    return labels;
  }

  private static void assertIsGraph(String name, String fileName) throws Exception {
    List<String> lines = lines(fileName);
    assertEquals("digraph " + name + " {", lines.get(0), fileName);
    assertEquals("rankdir = LR", lines.get(1), fileName);
    assertEquals("}", lines.get(lines.size() - 1), fileName);
  }

  @Test
  public void writesFilesNextToScanner() throws Exception {
    List<String> files;
    try (Stream<Path> list = Files.list(outDir)) {
      files = list.map(p -> p.getFileName().toString()).sorted().collect(Collectors.toList());
    }
    assertEquals(Arrays.asList("Dot.java", "dfa-big.dot", "dfa-min.dot", "nfa.dot"), files);
  }

  @Test
  public void printsNothingWhenQuiet() {
    assertEquals("", jflexOutput);
  }

  /** The minimized DFA: 0 is the start, 1 is {@code [^]}, 2 is {@code "a" "b"*}. */
  @Test
  public void minimizedDfa() throws Exception {
    assertEquals(
        Arrays.asList(
            "digraph DFA {",
            "rankdir = LR",
            "1 [shape = doublecircle]",
            "2 [shape = doublecircle]",
            "0 -> 1 [label=\"[0]\"]",
            "0 -> 2 [label=\"[1]\"]",
            "0 -> 1 [label=\"[2]\"]",
            "2 -> 2 [label=\"[2]\"]",
            "}"),
        lines("dfa-min.dot"));
  }

  /** The DFA before minimization has the same inputs and at least as many states and edges. */
  @Test
  public void dfaBeforeMinimization() throws Exception {
    assertIsGraph("DFA", "dfa-big.dot");
    assertEquals(Arrays.asList("[0]", "[1]", "[2]"), labels("dfa-big.dot"));
    assertTrue(labelledEdges("dfa-big.dot").size() >= labelledEdges("dfa-min.dot").size());
  }

  /** The edges of the NFA have the character classes as labels, or are epsilon edges. */
  @Test
  public void nfa() throws Exception {
    assertIsGraph("NFA", "nfa.dot");
    List<String> labels = labels("nfa.dot");
    labels.sort(null);
    assertEquals(
        Arrays.asList("{ ['a'] }", "{ ['b'] }", "{ [0-'`']['c'-1114111] }"), labels, "labels");
    List<String> lines = lines("nfa.dot");
    assertTrue(
        lines.stream().anyMatch(line -> line.endsWith("[style=dotted]")), "has epsilon edges");
    assertEquals(2, lines.stream().filter(line -> line.endsWith("[shape = doublecircle]")).count());
  }
}
