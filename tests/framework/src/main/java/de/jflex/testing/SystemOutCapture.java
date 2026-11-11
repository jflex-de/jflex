/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 * Captures {@code System.out} output of the current thread to avoid interleaved output of
 * concurrent tests.
 *
 * <p>The first capture replaces {@code System.out} with a stream that forwards every call to the
 * capturing stream of the calling thread, or to the original {@code System.out} if the thread has
 * none. Tests must not call {@link System#setOut} themselves after that.
 *
 * <pre>{@code
 * try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(buffer, true, "UTF-8"))) {
 *   scannerMain.invoke(null, (Object) args);
 * }
 * }</pre>
 */
public final class SystemOutCapture implements AutoCloseable {

  private static final ThreadLocal<PrintStream> SINK = new ThreadLocal<>();

  private static boolean installed;

  private SystemOutCapture() {}

  /**
   * Redirects what the current thread prints to {@code System.out} into {@code sink} until the
   * returned capture is closed. Starting another capture on the same thread replaces the sink.
   */
  public static SystemOutCapture start(PrintStream sink) {
    install();
    SINK.set(sink);
    return new SystemOutCapture();
  }

  private static synchronized void install() {
    if (!installed) {
      System.setOut(new Dispatcher(System.out));
      installed = true;
    }
  }

  /** Flushes the sink and lets the current thread print to the real {@code System.out} again. */
  @Override
  public void close() {
    PrintStream sink = SINK.get();
    if (sink != null) {
      sink.flush();
    }
    SINK.remove();
  }

  /** Forwards every method to the sink of the current thread, or to the original stream. */
  private static final class Dispatcher extends PrintStream {

    private final PrintStream fallback;

    Dispatcher(PrintStream fallback) {
      super(fallback, false);
      this.fallback = fallback;
    }

    private PrintStream target() {
      PrintStream sink = SINK.get();
      return sink != null ? sink : fallback;
    }

    @Override
    public void flush() {
      target().flush();
    }

    @Override
    public void close() {
      target().close();
    }

    @Override
    public boolean checkError() {
      return target().checkError();
    }

    @Override
    public void write(int b) {
      target().write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
      target().write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
      target().write(b, off, len);
    }

    @Override
    public void print(boolean b) {
      target().print(b);
    }

    @Override
    public void print(char c) {
      target().print(c);
    }

    @Override
    public void print(int i) {
      target().print(i);
    }

    @Override
    public void print(long l) {
      target().print(l);
    }

    @Override
    public void print(float f) {
      target().print(f);
    }

    @Override
    public void print(double d) {
      target().print(d);
    }

    @Override
    public void print(char[] s) {
      target().print(s);
    }

    @Override
    public void print(String s) {
      target().print(s);
    }

    @Override
    public void print(Object obj) {
      target().print(obj);
    }

    @Override
    public void println() {
      target().println();
    }

    @Override
    public void println(boolean x) {
      target().println(x);
    }

    @Override
    public void println(char x) {
      target().println(x);
    }

    @Override
    public void println(int x) {
      target().println(x);
    }

    @Override
    public void println(long x) {
      target().println(x);
    }

    @Override
    public void println(float x) {
      target().println(x);
    }

    @Override
    public void println(double x) {
      target().println(x);
    }

    @Override
    public void println(char[] x) {
      target().println(x);
    }

    @Override
    public void println(String x) {
      target().println(x);
    }

    @Override
    public void println(Object x) {
      target().println(x);
    }

    @Override
    public PrintStream printf(String format, Object... args) {
      target().printf(format, args);
      return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
      target().printf(l, format, args);
      return this;
    }

    @Override
    public PrintStream format(String format, Object... args) {
      target().format(format, args);
      return this;
    }

    @Override
    public PrintStream format(Locale l, String format, Object... args) {
      target().format(l, format, args);
      return this;
    }

    @Override
    public PrintStream append(CharSequence csq) {
      target().append(csq);
      return this;
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
      target().append(csq, start, end);
      return this;
    }

    @Override
    public PrintStream append(char c) {
      target().append(c);
      return this;
    }
  }
}
