/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Runs independent tasks on all processors. */
public final class Parallel {

  /** A function that may throw. */
  public interface Task<T, R> {
    R apply(T item) throws Exception;
  }

  /**
   * Applies {@code task} to each of {@code items} concurrently, and returns the results in the
   * order of {@code items}.
   *
   * <p>The task must not share mutable state between items.
   *
   * @throws Exception the exception of the first item, in the order of {@code items}, that failed.
   */
  public static <T, R> List<R> map(List<T> items, Task<T, R> task) throws Exception {
    int threads = Math.max(1, Math.min(items.size(), Runtime.getRuntime().availableProcessors()));
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    try {
      List<Future<R>> futures = new ArrayList<>();
      for (T item : items) {
        futures.add(executor.submit(() -> task.apply(item)));
      }
      List<R> results = new ArrayList<>();
      for (Future<R> future : futures) {
        try {
          results.add(future.get());
        } catch (ExecutionException e) {
          Throwable cause = e.getCause();
          if (cause instanceof Error) {
            throw (Error) cause;
          }
          throw (Exception) cause;
        }
      }
      return results;
    } finally {
      executor.shutdownNow();
    }
  }

  private Parallel() {}
}
