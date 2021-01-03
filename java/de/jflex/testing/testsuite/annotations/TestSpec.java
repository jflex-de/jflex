/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.testing.testsuite.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
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

  /** Golden file for JFlex's log (System.out stream). */
  String sysout() default "";

  /** Golden file for JFlex's warning and errors (System.err stream). */
  String syserr() default "";
}
