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

  /** Runs JFlex generation with the {@code -q} option. */
  boolean quiet() default false;

  /** Explicitly run JFlex with the {@code -v} option (even though verbose mode is default). */
  boolean verbose_provided() default false;

  /** Golden file for JFlex's log (System.out stream). */
  String sysout() default "";
}
