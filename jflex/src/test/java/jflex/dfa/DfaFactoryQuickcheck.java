/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.core.NFA;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.state.StateSet;
import jflex.state.StateSetEnumerator;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;

/**
 * Property-based tests for {@link DfaFactory}.
 *
 * @see DfaFactory
 */
public class DfaFactoryQuickcheck {

  /** Max nesting depth of the operators in a regular expression. */
  private static final int MAX_DEPTH = 4;

  /** Expressions that match at least one character. */
  private static final Arbitrary<String> NOT_EMPTY =
      Arbitraries.of("a", "b", "c", "[ab]", "[^a]", "[^]", "\"ab\"", "\"bca\"");

  /** Expressions without operators, including the empty character class. */
  private static final Arbitrary<String> ATOM =
      Arbitraries.frequencyOf(Tuple.of(12, NOT_EMPTY), Tuple.of(1, Arbitraries.of("\"\"", "[]")));

  /**
   * {@link DfaFactory#createFromNfa(NFA, Options, Out)} produces the same DFA as the previous
   * implementation.
   *
   * <p>Test that the DFA accepts the same language with the same actions and equal or fewer states.
   * After minimization it is the same DFA, modulo dead states. Without transitions to dead states
   * the minimized DFAs are equal.
   *
   * <p>Fewer tries than default because tests take fairly long.
   */
  @Property(tries = 200)
  @StatisticsReport(StatisticsReport.StatisticsReportMode.OFF)
  public void createFromNfaAgreesWithFullClosures(@ForAll("specs") String spec) throws Exception {
    Options options = new Options();
    Out out = new Out(options, new ByteArrayOutputStream());

    // each construction changes its NFA
    DFA expected = createFromNfaWithFullClosures(parse(spec, options, out));
    DFA dfa = DfaFactory.createFromNfa(parse(spec, options, out), options, out);

    boolean deadStates = expected.hasDeadStates();
    boolean fewerStates = dfa.numStates() < expected.numStates();
    // all cases have to occur for this property to mean something
    Statistics.label("dead states").collect(deadStates);
    Statistics.label("dead states")
        .coverage(
            coverage -> {
              coverage.check(true).percentage(p -> p > 5);
              coverage.check(false).percentage(p -> p > 50);
            });
    Statistics.label("fewer states").collect(fewerStates);
    Statistics.label("fewer states")
        .coverage(
            coverage -> {
              coverage.check(true).percentage(p -> p > 20);
              coverage.check(false).percentage(p -> p > 20);
            });

    assertTrue(dfa.numStates() <= expected.numStates(), spec);
    assertTrue(DfaQuickcheck.sameLanguage(expected, dfa), spec);

    DFA expectedPruned = DfaQuickcheck.withoutDeadTransitions(expected);
    DFA pruned = DfaQuickcheck.withoutDeadTransitions(dfa);
    expectedPruned.minimize();
    pruned.minimize();
    assertEquals(expectedPruned.canonicalLines(), pruned.canonicalLines(), spec);

    expected.minimize();
    dfa.minimize();
    if (!deadStates) {
      assertFalse(dfa.hasDeadStates(), spec);
      assertEquals(expected.canonicalLines(), dfa.canonicalLines(), spec);
    }
  }

  /**
   * Specs with up to four rules, with and without lexical states, {@code ^} and lookahead. No
   * macros.
   */
  @Provide
  static Arbitrary<String> specs() {
    return Combinators.combine(
            Arbitraries.of("", "%state S", "%xstate X", "%state S\n%xstate X"),
            rule().list().ofMinSize(1).ofMaxSize(4))
        .as(
            (states, rules) -> {
              StringBuilder spec = new StringBuilder("%%\n%unicode\n%int\n");
              spec.append(states).append("\n%%\n");
              for (int i = 0; i < rules.size(); i++) {
                String rule = rules.get(i);
                // a rule can only be in a lexical state that the spec declares
                if (!states.contains("S")) rule = rule.replace("<S> ", "");
                if (!states.contains("X")) rule = rule.replace("<X> ", "");
                spec.append(rule).append(" { return ").append(i).append("; }\n");
              }
              return spec.toString();
            });
  }

  /** A rule without action. */
  private static Arbitrary<String> rule() {
    Arbitrary<String> regExp =
        Arbitraries.integers().between(0, MAX_DEPTH).flatMap(DfaFactoryQuickcheck::regExp);
    Arbitrary<String> lookAhead =
        Arbitraries.frequencyOf(
                Tuple.of(1, Arbitraries.just("$")),
                Tuple.of(
                    3, Arbitraries.integers().between(0, 2).flatMap(DfaFactoryQuickcheck::regExp)))
            .map(l -> l.equals("$") ? l : "/ " + l);
    return Combinators.combine(
            Arbitraries.frequencyOf(
                Tuple.of(2, Arbitraries.just("")), Tuple.of(1, Arbitraries.of("<S> ", "<X> "))),
            Arbitraries.frequencyOf(
                Tuple.of(5, Arbitraries.just("")), Tuple.of(1, Arbitraries.just("^"))),
            NOT_EMPTY,
            regExp,
            Arbitraries.frequencyOf(Tuple.of(3, Arbitraries.just("")), Tuple.of(1, lookAhead)))
        .as(
            (state, bol, first, r, look) ->
                // the expression before a lookahead must not match the empty string
                state + bol + (look.isEmpty() ? r : first + " " + r + " " + look));
  }

  /** Regular expressions with operators nested up to the given depth. */
  private static Arbitrary<String> regExp(int depth) {
    if (depth == 0) {
      return ATOM;
    }
    Arbitrary<String> r = regExp(depth - 1);
    return Arbitraries.frequencyOf(
        Tuple.of(2, r),
        Tuple.of(3, Combinators.combine(r, r).as((a, b) -> "(" + a + " " + b + ")")),
        Tuple.of(2, Combinators.combine(r, r).as((a, b) -> "(" + a + " | " + b + ")")),
        Tuple.of(1, r.map(a -> "(" + a + ")*")),
        Tuple.of(1, r.map(a -> "(" + a + ")+")),
        Tuple.of(1, r.map(a -> "(" + a + ")?")),
        Tuple.of(1, r.map(a -> "!(" + a + ")")),
        Tuple.of(1, r.map(a -> "~(" + a + ")")));
  }

  /** The NFA of a spec. */
  private static NFA parse(String spec, Options options, Out out) throws Exception {
    LexScan scanner = new LexScan(new StringReader(spec));
    scanner.setOptions(options);
    scanner.setOut(out);
    scanner.setFile(new File("spec.flex"));
    NFA nfa = (NFA) new LexParse(scanner).parse().value;
    out.checkErrors();
    return nfa;
  }

  /**
   * The implementation of {@code DfaFactory.createFromNfa} before it reduced the epsilon closures
   * to the important states. Now as test oracle.
   */
  private static DFA createFromNfaWithFullClosures(NFA nfa) {
    int numStates = nfa.numStates();
    Map<StateSet, Integer> dfaStates = new HashMap<>(numStates);
    List<StateSet> dfaList = new ArrayList<>(numStates);

    DFA dfa = new DFA(nfa.numEntryStates(), nfa.numInput(), nfa.numLexStates());

    int numDFAStates = 0;
    int currentDFAState = 0;

    nfa.epsilonFill();

    StateSet currentState;
    // will be reused
    StateSet newState;

    // create the initial states of the DFA
    for (int i = 0; i < nfa.numEntryStates(); i++) {
      newState = nfa.epsilon(i);

      dfaStates.put(newState, numDFAStates);
      dfaList.add(newState);

      dfa.setEntryState(i, numDFAStates);

      dfa.setFinal(numDFAStates, nfa.containsFinal(newState));
      dfa.setAction(numDFAStates, nfa.getAction(newState));

      numDFAStates++;
    }

    numDFAStates--;

    StateSet tempStateSet = nfa.tempStateSet();
    StateSetEnumerator states = nfa.states();
    newState = new StateSet();
    while (currentDFAState <= numDFAStates) {

      currentState = dfaList.get(currentDFAState);

      for (int input = 0; input < nfa.numInput(); input++) {

        tempStateSet.clear();
        states.reset(currentState);
        while (states.hasMoreElements())
          tempStateSet.add(nfa.reachableStates(states.nextElement(), input));

        newState.copy(tempStateSet);

        states.reset(tempStateSet);
        while (states.hasMoreElements()) newState.add(nfa.epsilon(states.nextElement()));

        if (newState.containsElements()) {
          Integer nextDFAState = dfaStates.get(newState);

          if (nextDFAState != null) {
            dfa.addTransition(currentDFAState, input, nextDFAState);
          } else {
            numDFAStates++;

            // make a new copy of newState to store in dfaStates
            StateSet storeState = new StateSet(newState);

            dfaStates.put(storeState, numDFAStates);
            dfaList.add(storeState);

            dfa.addTransition(currentDFAState, input, numDFAStates);
            dfa.setFinal(numDFAStates, nfa.containsFinal(storeState));
            dfa.setAction(numDFAStates, nfa.getAction(storeState));
          }
        }
      }

      currentDFAState++;
    }

    return dfa;
  }
}
