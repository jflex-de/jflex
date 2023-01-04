/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.assertion;

/**
 * Similar to {@link Runnable} where the {@link #run()} returns something and can throw something.
 *
 * <p>This facilitates the use of method references in Java 8.
 */
public interface ThrowingRunnable {
  void run() throws Throwable;
}
