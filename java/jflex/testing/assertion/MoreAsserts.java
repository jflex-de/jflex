package jflex.testing.assertion;

public final class MoreAsserts {

  @SuppressWarnings("AvoidCatchingThrowable")
  public static <T extends Throwable> void assertThrows(
      String message, Class<T> expectedThrowable, ThrowingRunnable throwingRunnable) {
    try {
      throwingRunnable.run();
    } catch (Throwable actualThrowable) {
      if (expectedThrowable.isInstance(actualThrowable)) {
        // expected
        return;
      } else {
        // Unexpected exception
        String msg =
            String.format(
                "%s: expected to throw a %s but method has thrown a %s instead",
                message, expectedThrowable.getName(), actualThrowable.getClass().getName());
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
