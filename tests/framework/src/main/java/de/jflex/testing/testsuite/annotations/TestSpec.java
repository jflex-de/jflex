/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite.annotations;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.JFlexTestExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The scanner spec of a test class, which must implement {@link JFlexTest}. Before the tests of the
 * class run, {@link JFlexTestExtension} runs JFlex on the spec and compiles the generated scanner.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(JFlexTestExtension.class)
public @interface TestSpec {

  /** Lex specification. Required. */
  String lex();

  /** Exception that the generator is expected to throw. Defaults to none. */
  Class<? extends Throwable> generatorThrows() default NoExceptionThrown.class;

  /**
   * Cause why the generator has thrown an exception. Use {@link Void} to indicate that no cause is
   * expected.
   */
  Class<?> generatorThrowableCause() default NoExceptionThrown.class;

  /**
   * Skeleton file for the {@code --skel} option, relative to the working directory like {@link
   * #lex()}. Defaults to the built-in skeleton.
   */
  String skeleton() default "";

  /** Runs JFlex generation with the {@code --dump} option. */
  boolean dump() default false;

  /** Runs JFlex generation with the {@code --jlex} option. */
  boolean jlexCompat() default false;

  /**
   * Runs JFlex generation with the {@code --warn-unused} option. This is the default behaviour; set
   * to false to run with {@code --no-warn-unused}.
   */
  boolean warnUnused() default true;

  /**
   * The expected number of states in the minimized DFA. Negative values do not create an assertion.
   */
  int minimizedDfaStatesCount() default 0;

  /**
   * The expected minimized DFA up to the numbers of its states: the lines of {@code
   * jflex.dfa.DFA#canonicalLines()}, which describes the format. For instance
   *
   * <pre>{@code
   * minimizedDfa = {
   *   "states 2, inputs 2, lexical states 1",
   *   "entry: 0 0",
   *   "0: 1 -> 1",
   *   "1 [a0]:",
   *   "a0: return 1;"
   * }
   * }</pre>
   *
   * <p>The inputs of the DFA are the numbers of the character classes, so this needs {@link
   * #charClasses()}, or {@link #sameCharClass()} and {@link #distinctCharClasses()}. Empty skips
   * the assertion.
   *
   * <p>Only usable for specs with few states. Use {@link #minimizedDfaStatesCount()} otherwise.
   */
  String[] minimizedDfa() default {};

  /** The expected number of character classes. 0 skips the assertion. */
  int charClassesCount() default 0;

  /**
   * The expected character classes, one string per class in the format of the {@code --dump}
   * output, for instance <code>{"{ [0-'`']['b'-1114111] }", "{ ['a'] }"}</code>. The classes are in
   * canonical order: sorted by their first interval, so the class of character 0 is first. Empty
   * skips the assertion.
   *
   * <p>Only usable for specs with few classes. Use {@link #charClassesCount()} for large Unicode
   * sets.
   */
  String[] charClasses() default {};

  /**
   * Groups of characters (code pointst) hat must be in the same character class. For instance,
   * <code>
   * {"az", "09"}</code> means that {@code a} and {@code z} are in the same class, and {@code 0} and
   * {@code 9}. For specs where {@link #charClasses()} would be unreadable.
   */
  String[] sameCharClass() default {};

  /**
   * Groups of characters that the scanner must distinguish: the characters (code points) of one
   * string must be in pairwise different character classes, for instance {@code {"a_0"}} must all
   * be in different classes.
   */
  String[] distinctCharClasses() default {};

  /** Runs JFlex generation with the {@code -q} option. */
  boolean quiet() default false;

  /** Explicitly run JFlex with the {@code -v} option (even though verbose mode is default). */
  boolean verbose_provided() default false;

  /** Golden file for JFlex's log (System.out stream). */
  String sysout() default "";
}
