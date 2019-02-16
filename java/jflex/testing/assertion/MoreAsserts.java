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
package jflex.testing.assertion;

import javax.annotation.Nullable;

public final class MoreAsserts {

  private static final String MESSAGE = "Expected to throw a %s but method has thrown a %s instead";

  public static <T extends Throwable> void assertThrows(
      Class<T> expectedThrowable, ThrowingRunnable throwingRunnable) {
    assertThrows(/*message=*/ null, expectedThrowable, throwingRunnable);
  }

  @SuppressWarnings("AvoidCatchingThrowable")
  public static <T extends Throwable> void assertThrows(
      @Nullable String message, Class<T> expectedThrowable, ThrowingRunnable throwingRunnable) {
    try {
      throwingRunnable.run();
    } catch (Throwable actualThrowable) {
      if (expectedThrowable.isInstance(actualThrowable)) {
        // expected
        return;
      } else {
        // Unexpected exception
        String msg =
            message == null
                ? String.format(
                    MESSAGE, expectedThrowable.getName(), actualThrowable.getClass().getName())
                : String.format(
                    "%s: " + MESSAGE,
                    message,
                    expectedThrowable.getName(),
                    actualThrowable.getClass().getName());
        throw new AssertionError(msg, actualThrowable);
      }
    }
    throw new AssertionError(
        String.format(
            "%s: expected to throw a %s but nothing was thrown",
            message, expectedThrowable.getName()));
  }

  private MoreAsserts() {}
}
