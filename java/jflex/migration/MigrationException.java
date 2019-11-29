package jflex.migration;

/**
 * Thrown when a severe problem happened while migrating a maven test case to bazel using {@code
 * //java/jflex/migration}.
 */
class MigrationException extends Exception {

  MigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
