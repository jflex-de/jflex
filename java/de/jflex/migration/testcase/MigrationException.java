/*
 * Copyright (C) 2019-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.testcase;

/**
 * Thrown when a severe problem happened while migrating a maven test case to bazel using {@code
 * //java/de/jflex/migration}.
 */
class MigrationException extends Exception {

  MigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
