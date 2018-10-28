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
