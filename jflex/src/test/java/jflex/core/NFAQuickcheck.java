/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import jflex.dfa.DfaFactoryQuickcheck;
import jflex.logging.Out;
import jflex.option.Options;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

/**
 * Property-based tests for {@link NFA}.
 *
 * @see NFA
 */
public class NFAQuickcheck {

  /**
   * The initial size of the NFA of a spec without negation is the number of states that the NFA has
   * in the end: {@link NFA#initialSize} and {@link RegExp#size} correctly predict what {@link
   * NFA#addRegExp} and {@link NFA#insertNFA} create.
   */
  @Property(tries = 300)
  public void initialSizeIsNumStates(@ForAll("specsWithoutNegation") String spec) throws Exception {
    Options options = new Options();
    Out out = new Out(options, new ByteArrayOutputStream());

    NFA nfa = DfaFactoryQuickcheck.parse(spec, options, out);

    // the NFA only grows, so the capacity is the initial size if that was enough
    assertEquals(nfa.numStates(), nfa.capacity(), spec);
  }

  /** A string has one state per code point, not per {@code char}, plus one more. */
  @Example
  public void initialSizeIsNumStates_supplementaryCharacter() throws Exception {
    Options options = new Options();
    Out out = new Out(options, new ByteArrayOutputStream());
    String spec = "%%\n%unicode\n%int\n%%\n\"a\uD83D\uDE00b\" | \"c\" \"d\" { return 0; }\n";

    NFA nfa = DfaFactoryQuickcheck.parse(spec, options, out);

    // two entry states, four for the string, two for each character, two for the alternatives
    assertEquals(12, nfa.numStates());
    assertEquals(12, nfa.capacity());
  }

  /**
   * The specs of {@link DfaFactoryQuickcheck#specs()} with the content of each negation in place of
   * the negation, some of them with {@code %standalone}.
   */
  @Provide
  static Arbitrary<String> specsWithoutNegation() {
    return Combinators.combine(DfaFactoryQuickcheck.specs(), Arbitraries.of("%int", "%standalone"))
        .as((spec, type) -> spec.replace("!(", "(").replace("~(", "(").replace("%int", type));
  }
}
